package com.adeptj.modules.examples.jaxrs;

import com.adeptj.modules.examples.mybatis.MyBatisUserRepository;
import com.adeptj.modules.examples.mybatis.UserAnnotationMapper;
import com.adeptj.modules.examples.mybatis.domain.User;
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
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Providers;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

@JaxRSResource(name = "MyBatisResource")
@Path("/mybatis/users")
@Component(service = MyBatisResource.class)
public class MyBatisResource {

    private final MyBatisUserRepository userRepository;

    @Activate
    public MyBatisResource(@Reference MyBatisUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GET
    @Produces(APPLICATION_JSON)
    public List<User> getUsers() {
        return this.userRepository.findAll("findAll");
    }

    @Path("/me/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public User getUser(@PathParam("id") String id) {
        User user = this.userRepository.doInSession(session -> {
            return session.getMapper(UserAnnotationMapper.class).findById(Long.parseLong(id));
        });
        return user;
        // return this.userRepository.findById("findById", Long.parseLong(id));
    }

    @Path("/create")
    @POST
    @Consumes(APPLICATION_JSON)
    public Response insertUser(@NotNull JsonObject object, @NotNull @Context Providers providers) {
        ContextResolver<Jsonb> resolver = providers.getContextResolver(Jsonb.class, APPLICATION_JSON_TYPE);
        User user = resolver.getContext(Jsonb.class).fromJson(object.toString(), User.class);
        this.userRepository.insert("insert", user);
        return Response.ok(user).build();
    }

    @Path("/delete/{id}")
    @GET
    public Response deleteUser(@PathParam("id") String id) {
        this.userRepository.deleteById("deleteById", Long.parseLong(id));
        return Response.ok().build();
    }

    @Path("/update/{id}")
    @GET
    public Response updateUser(@PathParam("id") String id) {
        User user = this.userRepository.findById("findById", Long.parseLong(id));
        user.setEmail("u.johndoe@johndoe.com");
        user.setFirstName("New John");
        user.setLastName("New Doe");
        this.userRepository.update("update", user);
        return Response.ok().build();
    }
}
