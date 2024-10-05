package com.adeptj.modules.examples.jaxrs;

import com.adeptj.modules.restclient.core.plugin.AuthorizationHeaderPlugin;
import org.jetbrains.annotations.NotNull;
import org.osgi.service.component.annotations.Component;

import java.util.List;

@Component
public class TestAuthorizationHeaderPlugin implements AuthorizationHeaderPlugin {

    @Override
    public @NotNull AuthType getType() {
        return AuthType.BEARER;
    }

    @Override
    public @NotNull String getValue() {
        return "1234567890";
    }

    @Override
    public @NotNull List<String> getPathPatterns() {
        return List.of("/api/user", "/api/users/*", "/api/users");
    }
}
