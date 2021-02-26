package com.adeptj.modules.examples.jaxrs;

import com.adeptj.modules.jaxrs.api.JaxRSResource;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.apache.felix.jaas.LoginContextFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.validation.constraints.NotEmpty;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.lang.invoke.MethodHandles;

import static javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@JaxRSResource(name = "GithubResource")
@Path("/github")
@Component(service = GithubResource.class)
public class GithubResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Reference
    private LoginContextFactory loginContextFactory;

    @Path("/login")
    @POST
    @Consumes(APPLICATION_FORM_URLENCODED)
    public Response login(@NotEmpty @FormParam("j_username") String username,
                              @NotEmpty @FormParam("j_password") String password) {
        CallbackHandler handler = new SimpleCallbackHandler(username, password.toCharArray());
        Subject subject = new Subject();
        try {
            LoginContext lc = this.loginContextFactory.createLoginContext("AdeptJ Realm", subject, handler);
            lc.login();
        } catch (LoginException le) {
            LOGGER.error(le.getMessage(), le);
        }
        return Response.ok().entity(subject).build();
    }

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
