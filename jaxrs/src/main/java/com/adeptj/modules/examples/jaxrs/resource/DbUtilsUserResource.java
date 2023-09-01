package com.adeptj.modules.examples.jaxrs.resource;

import com.adeptj.modules.commons.jdbc.DataSourceService;
import com.adeptj.modules.examples.mybatis.domain.User;
import com.adeptj.modules.jaxrs.api.JaxRSResource;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.RowProcessor;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.jetbrains.annotations.NotNull;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

@JaxRSResource(name = "DbUtilsResource")
@Path("/dbutils/users")
@Component(service = DbUtilsUserResource.class)
public class DbUtilsUserResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final DataSourceService dataSourceService;

    @Activate
    public DbUtilsUserResource(@NotNull @Reference DataSourceService dataSourceService) {
        this.dataSourceService = dataSourceService;
    }

    @GET
    @Produces(APPLICATION_JSON)
    public List<User> getUsers() {
        String query = "SELECT * from users";
        Map<String, String> columnMapping = new HashMap<>();
        columnMapping.put("FIRST_NAME", "firstName");
        columnMapping.put("LAST_NAME", "lastName");
        columnMapping.put("MOBILE_NO", "contact");
        RowProcessor rowProcessor = new BasicRowProcessor(new BeanProcessor(columnMapping));
        QueryRunner queryRunner = new QueryRunner(this.dataSourceService.getDataSource());
        try {
            return queryRunner.query(query, new BeanListHandler<>(User.class, rowProcessor));
        } catch (SQLException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        return new ArrayList<>();
    }
}
