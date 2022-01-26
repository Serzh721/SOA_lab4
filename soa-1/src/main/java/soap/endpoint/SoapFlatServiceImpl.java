package soap.endpoint;

import lombok.SneakyThrows;
import model.Flat;
import model.FlatListWrap;
import service.FlatService;
import soap.util.Filter;

import javax.ejb.EJB;
import javax.jws.WebService;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;

@WebService(endpointInterface = "soap.endpoint.SoapFlatService")
public class SoapFlatServiceImpl implements SoapFlatService {

    @EJB(lookup = "java:global/ejb-1-1.0-SNAPSHOT/FlatServiceImpl!service.FlatService")
    private FlatService flatService;

    @Override
    public Flat getFlat(Integer id) {
        return flatService.getFlat(id);
    }

    @SneakyThrows
    @Override
    public FlatListWrap getFlats(Filter filter) {
        if (filter == null) return flatService.getFlatList(new HashMap<>());
        return flatService.getFlatList(filter.toMap());
    }

    @Override
    @Consumes(MediaType.APPLICATION_XML)
    public Flat modifyFlat(Integer id, Flat Flat) {
        return flatService.modifyFlat(id, Flat);
    }

    @Override
    @Consumes(MediaType.APPLICATION_XML)
    public Flat addFlat(Flat Flat) {
        return flatService.addFlat(Flat);
    }

    @Override
    public void deleteFlat(Integer id) {
        flatService.deleteFlat(id);
    }

    @Override
    public Long getNumberPriceLower(String s) {
        return flatService.getNumberPriceLower(s);
    }

    @Override
    public FlatListWrap getNamesContain(String s) {
        return flatService.getNamesContain(s);
    }

    @Override
    public FlatListWrap getNamesStart(String s) {
        return flatService.getNamesStart(s);
    }
}
