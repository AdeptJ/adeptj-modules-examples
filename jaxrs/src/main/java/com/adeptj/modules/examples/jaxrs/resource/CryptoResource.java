package com.adeptj.modules.examples.jaxrs.resource;

import com.adeptj.modules.commons.crypto.CryptoService;
import com.adeptj.modules.jaxrs.api.JaxRSResource;
import com.adeptj.modules.jaxrs.api.RequiresAuthentication;
import jakarta.validation.constraints.NotEmpty;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import static jakarta.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED;

@JaxRSResource(name = "CryptoResource")
@Path("/crypto")
@Component(service = CryptoResource.class)
public class CryptoResource {

    private final CryptoService cryptoService;

    @Activate
    public CryptoResource(@Reference CryptoService cryptoService) {
        this.cryptoService = cryptoService;
    }

    @RequiresAuthentication
    @Path("/encrypt")
    @POST
    @Consumes(APPLICATION_FORM_URLENCODED)
    public String encryptText(@NotEmpty @FormParam("plainText") String plainText) {
        return this.cryptoService.encrypt(plainText);
    }

    @Path("/decrypt")
    @POST
    @Consumes(APPLICATION_FORM_URLENCODED)
    public String decryptText(@FormParam("encryptedText") String encryptedText) {
        return this.cryptoService.decrypt(encryptedText);
    }
}
