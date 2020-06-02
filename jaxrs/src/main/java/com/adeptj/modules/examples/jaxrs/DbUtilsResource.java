package com.adeptj.modules.examples.jaxrs;

import com.adeptj.modules.commons.jdbc.service.DataSourceService;
import com.adeptj.modules.examples.mybatis.domain.User;
import com.adeptj.modules.jaxrs.core.JaxRSResource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.jetbrains.annotations.NotNull;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.lang.invoke.MethodHandles;
import java.sql.SQLException;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@JaxRSResource(name = "DbUtilsResource")
@Path("/dbutils/users")
@Component(service = DbUtilsResource.class)
public class DbUtilsResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final DataSourceService dataSourceService;

    @Activate
    public DbUtilsResource(@NotNull @Reference DataSourceService dataSourceService) {
        this.dataSourceService = dataSourceService;
    }

    @GET
    @Produces(APPLICATION_JSON)
    public List<User> getUsers() {
        List<User> users = null;
        String query = "SELECT u.FIRST_NAME as firstName, u.LAST_NAME as lastName FROM Users u";
        QueryRunner queryRunner = new QueryRunner(this.dataSourceService.getDataSource());
        try {
            users = queryRunner.query(query, new BeanListHandler<>(User.class));
        } catch (SQLException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        return users;
    }
}
