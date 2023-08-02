package com.adeptj.modules.examples.jaxrs.internal;

import com.adeptj.modules.security.oauth.OAuthAccessToken;
import com.adeptj.modules.security.oauth.OAuthAccessTokenConsumer;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

@Component
public class OAuthAccessTokenConsumerImpl implements OAuthAccessTokenConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public String consume(OAuthAccessToken token) {
        LOGGER.info("access token - {}", token.getAccessToken());
        return "/admin/login";
    }
}
