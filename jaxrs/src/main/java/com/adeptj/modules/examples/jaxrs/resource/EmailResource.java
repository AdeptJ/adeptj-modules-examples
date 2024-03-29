package com.adeptj.modules.examples.jaxrs.resource;

import com.adeptj.modules.commons.email.EmailInfo;
import com.adeptj.modules.commons.email.EmailService;
import com.adeptj.modules.jaxrs.api.JaxRSResource;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import static jakarta.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED;

@JaxRSResource(name = "EmailResource")
@Path("/email-sender")
@Component(service = EmailResource.class)
public class EmailResource {

    private final EmailService emailService;

    @Activate
    public EmailResource(@Reference EmailService emailService) {
        this.emailService = emailService;
    }

    @POST
    @Consumes(APPLICATION_FORM_URLENCODED)
    public String sendEmail(@FormParam("message") String message, @FormParam("toAddress") String toAddress) {
        String subject = "Test email from AdeptJ EmailService";
        EmailInfo info = EmailInfo.builder()
                .emailType(EmailInfo.EmailType.HTML)
                .subject(subject)
                .addToAddresses(toAddress)
                .addCcAddresses("myemail@mygmail.com", "mygmail@myemail.com")
                .addBccAddresses("irobot1@gmail.com", "irobot2@gmail.com")
                .message(message)
                .build();
        this.emailService.sendEmail(info);
        return "OK";
    }
}
