package com.adeptj.modules.examples.jaxrs.resource;

import com.adeptj.modules.jaxrs.api.JaxRSResource;
import com.adeptj.modules.jaxrs.core.SecurityContextUtil;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import org.osgi.service.component.annotations.Component;

import java.security.Principal;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

@JaxRSResource(name = "MiscResource")
@Path("/misc")
@Component(service = MiscResource.class)
public class MiscResource {

    @RolesAllowed({"admin", "OSGiAdmin"})
    @Path("/me-roles-allowed")
    @POST
    @Produces(APPLICATION_JSON)
    public Principal meRolesAllowed(@Context SecurityContext securityContext) {
        return SecurityContextUtil.getJwtPrincipal(securityContext);
    }
}
