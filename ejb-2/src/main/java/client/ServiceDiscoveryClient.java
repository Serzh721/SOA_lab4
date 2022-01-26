package client;

import com.orbitz.consul.Consul;
import com.orbitz.consul.HealthClient;
import com.orbitz.consul.model.health.ServiceHealth;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.glassfish.jersey.SslConfigurator;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.util.List;

@Singleton
public class ServiceDiscoveryClient {

    @Inject
    @ConfigProperty(name = "service1.name")
    private String serviceName;

    private Consul client;

    @PostConstruct
    private void init() {
        try {
            client = Consul.builder().build();
        } catch (Exception e) {
            System.err.println("Consul is unavailable");
        }
    }

    private String getUriFromMule() {
        return System.getenv("SOA_MULE_PATH");
    }

    private String getUriFromConsul() {
        if (client != null) {
            HealthClient healthClient = client.healthClient();
            List<ServiceHealth> nodes = healthClient.getHealthyServiceInstances(serviceName).getResponse();
            if (nodes.size() > 0) {
                ServiceHealth service = nodes.get(0);
                String address = service.getNode().getAddress();
                int port = service.getService().getPort();
                String app = service.getService().getMeta().get("api_address");
                return String.format("https://%s:%d/%s", address, port, app);
            }
        }
        throw new RuntimeException("Service is not available from consul");
    }

    public WebTarget getTarget() {
        String backFirst = getUriFromMule();
        return client().target(backFirst);
    }

    private Client client() {
        SslConfigurator sslConfigurator = SslConfigurator.newInstance()
                .keyStoreFile(System.getenv("SOA_KEYSTOREFILE"))
                .keyStorePassword(System.getenv("SOA_KEYSTORE_PASSWORD"))
                .trustStoreFile(System.getenv("SOA_TRUSTSTOREFILE"))
                .trustStorePassword(System.getenv("SOA_TRUSTSTORE_PASSWORD"));

        return ClientBuilder.newBuilder()
                .sslContext(sslConfigurator.createSSLContext())
                .hostnameVerifier((hostname, sslSession) -> hostname.equals("localhost")
                        || hostname.equals("127.0.0.1"))
                .build();
    }
}