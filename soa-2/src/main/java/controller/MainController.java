package controller;

import model.FlatListWrap;
import service.AdditionalTaskService;
import util.FlatServiceException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import static java.lang.String.format;

@Path("/agency")
public class MainController {

    private AdditionalTaskService service;

    {
        try {
            service = (AdditionalTaskService) new InitialContext()
                    .lookup(format("java:global/%s/AdditionalTaskServiceImpl!service.AdditionalTaskService", System.getProperty("context")));
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    @GET
    @Path("/get-most-expensive/{id1}/{id2}/{id3}")
    @Produces(MediaType.APPLICATION_XML)
    public String getMostExpensive(@PathParam("id1") Integer id1,
                                   @PathParam("id2") Integer id2,
                                   @PathParam("id3") Integer id3) {
        try {
            return service.getMostExpensive(id1, id2, id3).toString();
        } catch (FlatServiceException e) {
            throw e.toWebApplicationException();
        }
    }

    @GET
    @Path("/get-ordered-by-area/{view}/{desc}")
    @Produces(MediaType.APPLICATION_XML)
    public FlatListWrap getOrderedByArea(@PathParam("view") String view,
                                         @PathParam("desc") String desc) {
        try {
            return service.getOrderedByArea(view, desc);
        } catch (FlatServiceException e) {
            throw e.toWebApplicationException();
        }
    }
}
