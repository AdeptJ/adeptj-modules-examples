package com.adeptj.modules.examples.jaxrs;

import com.adeptj.modules.commons.jdbc.service.DataSourceService;
import com.adeptj.modules.commons.utils.Functions;
import com.adeptj.modules.examples.jpa.entity.User;
import com.adeptj.modules.jaxrs.core.JaxRSResource;
import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.jetbrains.annotations.NotNull;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.function.Function;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

@JaxRSResource(name = "MyBatisResource")
@Path("/mybatis/users")
@Component(service = MyBatisResource.class)
public class MyBatisResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String MYBATIS_CONFIG = "mybatis-config.xml";

    private SqlSessionFactory sessionFactory;

    @Activate
    public MyBatisResource(@Reference DataSourceService dataSourceService, @NotNull BundleContext context) {
        try (InputStream stream = context.getBundle().getEntry(MYBATIS_CONFIG).openStream()) {
            Functions.executeUnderContextClassLoader(this.getClass().getClassLoader(), () -> {
                Configuration configuration = new XMLConfigBuilder(stream).parse();
                configuration.setEnvironment(new Environment.Builder("development")
                        .dataSource(dataSourceService.getDataSource())
                        .transactionFactory(new JdbcTransactionFactory())
                        .build());
                this.sessionFactory = new SqlSessionFactoryBuilder().build(configuration);
            });
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw new RuntimeException(ex);
        }
    }

    @GET
    @Produces(APPLICATION_JSON)
    public List<User> getUsers() {
        return this.doInSession((session) -> session.getMapper(UserMapper.class).findAll());
    }

    @Path("/me/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public User getUser(@PathParam("id") String id) {
        return this.doInSession((session) -> session.getMapper(UserMapper.class).findById(Long.parseLong(id)));
    }

    @Path("/create")
    @POST
    @Consumes(APPLICATION_JSON)
    public Response insertUser(@NotNull JsonObject object, @NotNull @Context Providers providers) {
        ContextResolver<Jsonb> resolver = providers.getContextResolver(Jsonb.class, APPLICATION_JSON_TYPE);
        User user = resolver.getContext(Jsonb.class).fromJson(object.toString(), User.class);
        try (SqlSession session = this.sessionFactory.openSession()) {
            session.getMapper(UserMapper.class).insert(user);
            session.commit();
            return Response.ok(user).build();
        }
    }

    @Path("/delete/{id}")
    @GET
    public Response deleteUser(@PathParam("id") String id) {
        try (SqlSession session = this.sessionFactory.openSession()) {
            session.getMapper(UserMapper.class).deleteById(Long.parseLong(id));
            session.commit();
            return Response.ok().build();
        }
    }

    private <T> T doInSession(@NotNull Function<SqlSession, T> function) {
        try (SqlSession session = this.sessionFactory.openSession()) {
            return function.apply(session);
        }
    }
}
