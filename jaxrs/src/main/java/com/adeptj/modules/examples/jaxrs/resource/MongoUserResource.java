package com.adeptj.modules.examples.jaxrs.resource;

import com.adeptj.modules.examples.mongodb.MongoUserRepository;
import com.adeptj.modules.examples.mongodb.User;
import com.adeptj.modules.jaxrs.api.JaxRSResource;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.jetbrains.annotations.NotNull;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.List;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

@JaxRSResource(name = "MongoUserResource")
@Path("/mongo/users")
@Component(service = MongoUserResource.class)
public class MongoUserResource {

    private final MongoUserRepository userRepository;

    @Activate
    public MongoUserResource(@Reference MongoUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getUsers() {
        return this.userRepository.findMany();
    }

    @Path("/create")
    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public User createUserNew(@NotNull User document) {
        this.userRepository.insert(document);
        return document;
    }

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public User getUser(@PathParam("id") String id) {
        return this.userRepository.findOneById(id);
    }
}
