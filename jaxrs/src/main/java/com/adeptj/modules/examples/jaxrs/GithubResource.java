package com.adeptj.modules.examples.jaxrs;

import com.adeptj.modules.jaxrs.core.JaxRSResource;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.osgi.service.component.annotations.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@JaxRSResource(name = "GithubResource")
@Path("/github")
@Component(service = GithubResource.class)
public class GithubResource {

    @GET
    @Path("{path}")
    @Produces(APPLICATION_JSON)
    public Response getData(@PathParam("path") String p) {
        HttpResponse<String> httpResponse = Unirest.get("/{path}")
                .routeParam("path", p)
                .asString();
        return Response.ok(httpResponse
                .getBody()).build();
    }

    @GET
    @Path("gists/{path}")
    @Produces(APPLICATION_JSON)
    public Response getPublicGists(@PathParam("path") String p) {
        HttpResponse<String> httpResponse = Unirest.get("/gists/{path}")
                .routeParam("path", p)
                .asString();
        return Response.ok(httpResponse
                .getBody()).build();
    }
}
