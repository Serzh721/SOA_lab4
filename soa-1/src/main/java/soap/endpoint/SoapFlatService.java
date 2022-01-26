package soap.endpoint;

import model.Flat;
import model.FlatListWrap;
import soap.util.Filter;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService(name = "SoapFlatService")
public interface SoapFlatService {
    @WebMethod
    FlatListWrap getFlats(Filter filter);

    @WebMethod
    Flat getFlat(Integer id);

    @WebMethod
    Flat addFlat(Flat flat);

    @WebMethod
    void deleteFlat(Integer id);

    @WebMethod
    Flat modifyFlat(Integer id, Flat flat);

    @WebMethod
    Long getNumberPriceLower(String s);

    @WebMethod
    FlatListWrap getNamesContain(String s);

    @WebMethod
    FlatListWrap getNamesStart(String s);
}
