package com.adeptj.modules.examples.jaxrs.resource;

import com.adeptj.modules.commons.crypto.PasswordEncoder;
import com.adeptj.modules.jaxrs.api.JaxRSResource;
import jakarta.validation.constraints.NotEmpty;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import static jakarta.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED;

@JaxRSResource(name = "PasswordEncoderResource")
@Path("/password-encoder")
@Component(service = PasswordEncoderResource.class)
public class PasswordEncoderResource {

    private final PasswordEncoder passwordEncoder;

    @Activate
    public PasswordEncoderResource(@Reference PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Path("/encode")
    @POST
    @Consumes(APPLICATION_FORM_URLENCODED)
    public String encodePassword(@NotEmpty @FormParam("password") String password) {
        return this.passwordEncoder.encode(password);
    }

    @Path("/match")
    @POST
    @Consumes(APPLICATION_FORM_URLENCODED)
    public boolean matchPassword(@NotEmpty @FormParam("password") String password,
                                 @NotEmpty @FormParam("encodedPassword") String encodedPassword) {
        return this.passwordEncoder.matches(password, encodedPassword);
    }
}
