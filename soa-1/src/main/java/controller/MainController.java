package controller;

import model.Flat;
import model.FlatListWrap;
import service.FlatService;
import util.FlatServiceException;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.lang.String.format;

@Path("/flats")
public class MainController {

    @EJB(lookup = "java:global/ejb-1-1.0-SNAPSHOT/FlatServiceImpl!service.FlatService")
    private FlatService flatService;

    @GET
    @Produces(MediaType.APPLICATION_XML)
    public FlatListWrap getFlatList(@Context UriInfo uriParams) {
        try {
            return flatService.getFlatList(prepareParams(uriParams));
        } catch (FlatServiceException e) {
            throw e.toWebApplicationException();
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_XML)
    public Flat getFlat(@PathParam("id") Integer id) {
        try {
            return flatService.getFlat(id);
        } catch (FlatServiceException e) {
            throw e.toWebApplicationException();
        }
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_XML)
    @Consumes(MediaType.APPLICATION_XML)
    public Flat modifyFlat(@PathParam("id") Integer id, Flat flat) {
        try {
            return flatService.modifyFlat(id, flat);
        } catch (FlatServiceException e) {
            throw e.toWebApplicationException();
        }
    }

    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_XML)
    @Consumes(MediaType.APPLICATION_XML)
    public Flat addFlat(Flat flat) {
        try {
            return flatService.addFlat(flat);
        } catch (FlatServiceException e) {
            throw e.toWebApplicationException();
        }
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_XML)
    public Response deleteFlat(@PathParam("id") Integer id) {
        try {
            flatService.deleteFlat(id);
            return Response.ok().build();
        } catch (FlatServiceException e) {
            throw e.toWebApplicationException();
        }
    }

    @GET
    @Path("/pricelower")
    @Produces(MediaType.APPLICATION_XML)
    public String getPriceLower(@QueryParam(value = "price") String str) {
        return flatService.getNumberPriceLower(validateQueryParam(str, "price")).toString();
    }

    @GET
    @Path("/namescontain")
    @Produces(MediaType.APPLICATION_XML)
    public FlatListWrap getNamesContain(@QueryParam(value = "string") String str) {
        return flatService.getNamesContain(validateQueryParam(str, "string"));
    }

    @GET
    @Path("/namesstart")
    @Produces(MediaType.APPLICATION_XML)
    public FlatListWrap getNamesStart(@QueryParam(value = "string") String str) {
        return flatService.getNamesStart(validateQueryParam(str, "string"));
    }

    private Map<String, String[]> prepareParams(UriInfo uriParams) {
        Map<String, String[]> params = new HashMap<>();
        MultivaluedMap<String, String> mpAllQueParams = uriParams.getQueryParameters();
        for (String s : mpAllQueParams.keySet()) {
            List<String> values = mpAllQueParams.get(s);
            String[] newValues = new String[values.size()];
            for (int i = 0; i < values.size(); i++) {
                newValues[i] = values.get(i);
            }
            params.put(s, newValues);
        }
        return params;
    }

    private <T> T validateQueryParam(T param, String paramName) {
        return Optional.ofNullable(param)
                .orElseThrow(() -> new BadRequestException(format("Missing required parameter: parameterName=\"%s\"", paramName)));
    }
}
