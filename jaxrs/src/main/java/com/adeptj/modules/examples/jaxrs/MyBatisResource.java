package com.adeptj.modules.examples.jaxrs;

import com.adeptj.modules.commons.utils.JavaxJsonUtil;
import com.adeptj.modules.examples.mybatis.MyBatisUserRepository;
import com.adeptj.modules.examples.mybatis.UserXmlMapper;
import com.adeptj.modules.examples.mybatis.domain.User;
import com.adeptj.modules.jaxrs.api.JaxRSResource;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

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
        User user = JavaxJsonUtil.deserialize(payload, User.class);
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
        User user = JavaxJsonUtil.deserialize(payload, User.class);
        user.setId(Long.parseLong(id));
        this.userRepository.update(UserXmlMapper.class, user);
        return Response.ok(user).build();
    }
}
