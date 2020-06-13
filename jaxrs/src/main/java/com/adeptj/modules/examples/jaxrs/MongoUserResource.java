package com.adeptj.modules.examples.jaxrs;

import com.adeptj.modules.examples.mongodb.MongoUserRepository;
import com.adeptj.modules.examples.mongodb.User;
import com.adeptj.modules.jaxrs.core.JaxRSResource;
import org.jetbrains.annotations.NotNull;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.json.JsonObject;
import javax.json.bind.Jsonb;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Providers;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

@JaxRSResource(name = "MongoUserResource")
@Path("/mongo/users")
@Component(service = MongoUserResource.class)
public class MongoUserResource {

    private final MongoUserRepository userRepository;

    @Activate
    public MongoUserResource(@Reference MongoUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Path("/create")
    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public void createUser(@NotNull JsonObject object, @NotNull @Context Providers providers) {
        ContextResolver<Jsonb> resolver = providers.getContextResolver(Jsonb.class, APPLICATION_JSON_TYPE);
        User document = resolver.getContext(Jsonb.class).fromJson(object.toString(), User.class);
        this.userRepository.insert(document);
    }

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public User getUser(@PathParam("id") String id) {
        return this.userRepository.findOneById(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getUsers() {
        return this.userRepository.findMany();
    }
}
