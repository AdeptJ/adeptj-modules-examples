package com.adeptj.modules.examples.jaxrs;

import com.adeptj.modules.commons.jdbc.service.DataSourceService;
import com.adeptj.modules.examples.mybatis.domain.User;
import com.adeptj.modules.jaxrs.core.JaxRSResource;
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

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.lang.invoke.MethodHandles;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Map<String, String> columnMapping = new HashMap<>();
        columnMapping.put("FIRST_NAME", "firstName");
        columnMapping.put("LAST_NAME", "lastName");
        columnMapping.put("MOBILE_NO", "contact");
        RowProcessor rowProcessor = new BasicRowProcessor(new BeanProcessor(columnMapping));
        QueryRunner queryRunner = new QueryRunner(this.dataSourceService.getDataSource());
        try {
            return queryRunner.query("SELECT * from users", new BeanListHandler<>(User.class, rowProcessor));
        } catch (SQLException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        return new ArrayList<>();
    }
}
