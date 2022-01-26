package sd;

import com.orbitz.consul.AgentClient;
import com.orbitz.consul.Consul;
import com.orbitz.consul.model.agent.ImmutableRegistration;
import com.orbitz.consul.model.agent.Registration;
import lombok.SneakyThrows;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.Collections;

@Singleton
@Startup
public class ServiceDiscoveryWorkerImpl implements ServiceDiscoveryWorker {

    private Consul client = null;

    @Inject
    @ConfigProperty(name = "service.port")
    private String port;

    @Inject
    @ConfigProperty(name = "service.url")
    private String url;

    @Inject
    @ConfigProperty(name = "service.name")
    private String name;

    @Inject
    @ConfigProperty(name = "service.id")
    private String serviceId;

    @PostConstruct
    private void register() {
        try {
            client = Consul.builder().build();
            Registration service = ImmutableRegistration.builder()
                    .id(serviceId)
                    .name(name)
                    .port(Integer.parseInt(port))
                    .check(Registration.RegCheck.ttl(30L))
                    .meta(Collections.singletonMap("api_address", url))
                    .build();

            client.agentClient().register(service);
        } catch (Exception e) {
            System.err.println("Consul is unavailable");
        }
    }

    @PreDestroy
    private void unregister() {
        try {
            client.agentClient().deregister(serviceId);
        } catch (Exception e) {
            System.err.println("Consul is unavailable");
        }
    }

    @SneakyThrows
    @Schedule(hour = "*", minute = "*", second = "*/15")
    private void checkIn() {
        AgentClient agentClient = client.agentClient();
        agentClient.pass(serviceId);
    }

}