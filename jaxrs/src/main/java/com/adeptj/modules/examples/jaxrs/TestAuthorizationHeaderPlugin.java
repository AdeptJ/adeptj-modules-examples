package com.adeptj.modules.examples.jaxrs;

import com.adeptj.modules.restclient.core.plugin.AuthorizationHeaderPlugin;
import org.osgi.service.component.annotations.Component;

import java.util.List;

@Component
public class TestAuthorizationHeaderPlugin implements AuthorizationHeaderPlugin {

    @Override
    public String getType() {
        return "Bearer";
    }

    @Override
    public String getValue() {
        return "1234567890";
    }

    @Override
    public List<String> getPathPatterns() {
        return List.of("/api/user", "/api/users/*", "/api/users");
    }
}
