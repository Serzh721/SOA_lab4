package service;

import client.ServiceDiscoveryClient;
import com.thoughtworks.xstream.XStream;
import model.Flat;
import model.FlatListWrap;
import util.FlatServiceException;
import util.XStreamUtil;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Objects;

@Stateless
public class AdditionalTaskServiceImpl implements AdditionalTaskService {

    @Inject
    private ServiceDiscoveryClient serviceDiscoveryClient;

    @Override
    public Integer getMostExpensive(Integer id1, Integer id2, Integer id3) {
        Response flat1Response = serviceDiscoveryClient.getTarget().path(id1.toString()).request().accept(MediaType.APPLICATION_XML).get();
        Response flat2Response = serviceDiscoveryClient.getTarget().path(id2.toString()).request().accept(MediaType.APPLICATION_XML).get();
        Response flat3Response = serviceDiscoveryClient.getTarget().path(id3.toString()).request().accept(MediaType.APPLICATION_XML).get();

        if (flat1Response.getStatus() == 200 && flat2Response.getStatus() == 200 && flat3Response.getStatus() == 200) {
            XStream xStream = XStreamUtil.createXStream();
            Flat flat1 = (Flat) xStream.fromXML(flat1Response.readEntity(String.class));
            Flat flat2 = (Flat) xStream.fromXML(flat2Response.readEntity(String.class));
            Flat flat3 = (Flat) xStream.fromXML(flat3Response.readEntity(String.class));

            Double flat1Price = Objects.requireNonNull(flat1).getPrice();
            Double flat2Price = Objects.requireNonNull(flat2).getPrice();
            Double flat3Price = Objects.requireNonNull(flat3).getPrice();

            if (flat1Price == null) flat1Price = 0.0;
            if (flat2Price == null) flat2Price = 0.0;
            if (flat3Price == null) flat3Price = 0.0;

            Integer num;
            if (flat1Price >= flat2Price && flat1Price >= flat3Price) {
                num = id1;
            } else if (flat2Price >= flat1Price && flat2Price >= flat3Price) {
                num = id2;
            } else {
                num = id3;
            }
            return num;
        }
        throw new FlatServiceException("Flat with one of these ids doesn't exist", 404);
    }

    @Override
    public FlatListWrap getOrderedByArea(String view, String desc) {
        String response = null;
        try {
            response = serviceDiscoveryClient.getTarget()
                    .queryParam("view", view)
                    .queryParam("orderBy", "area," + desc)
                    .request()
                    .accept(MediaType.APPLICATION_XML)
                    .get()
                    .readEntity(String.class);
            return (FlatListWrap) XStreamUtil.createXStream().fromXML(response);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new FlatServiceException(response, 400);
        }
    }

}
