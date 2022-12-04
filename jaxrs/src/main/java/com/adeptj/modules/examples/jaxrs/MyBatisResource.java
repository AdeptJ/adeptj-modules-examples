package com.adeptj.modules.examples.jaxrs;

import com.adeptj.modules.commons.utils.JakartaJsonUtil;
import com.adeptj.modules.examples.mybatis.MyBatisUserRepository;
import com.adeptj.modules.examples.mybatis.UserXmlMapper;
import com.adeptj.modules.examples.mybatis.domain.User;
import com.adeptj.modules.jaxrs.api.JaxRSResource;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import java.util.List;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

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
        return this.userRepository.findAll(UserXmlMapper.class);
    }

    @Path("/me/{id}")
    @GET
    @Produces(APPLICATION_JSON)
    public User getUser(@PathParam("id") String id) {
        return this.userRepository.findById(UserXmlMapper.class, Long.parseLong(id));
    }

    @Path("/create")
    @POST
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    public Response insertUser(String payload) {
        User user = JakartaJsonUtil.deserialize(payload, User.class);
        this.userRepository.insert(UserXmlMapper.class, user);
        return Response.ok(user).build();
    }

    @Path("/delete/{id}")
    @GET
    public Response deleteUser(@PathParam("id") String id) {
        this.userRepository.deleteById(UserXmlMapper.class, Long.parseLong(id));
        return Response.ok().build();
    }

    @Path("/update/{id}")
    @PUT
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    public Response updateUser(@PathParam("id") String id, String payload) {
        User user = JakartaJsonUtil.deserialize(payload, User.class);
        user.setId(Long.parseLong(id));
        this.userRepository.update(UserXmlMapper.class, user);
        return Response.ok(user).build();
    }
}
