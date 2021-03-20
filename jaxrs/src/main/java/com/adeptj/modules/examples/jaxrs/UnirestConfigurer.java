package com.adeptj.modules.examples.jaxrs;

import com.adeptj.modules.commons.utils.JacksonJsonUtil;
import kong.unirest.Unirest;
import kong.unirest.jackson.JacksonObjectMapper;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

@Component(immediate = true)
public class UnirestConfigurer {

    @Activate
    protected void start() {
        Unirest.config()
                .defaultBaseUrl("https://api.github.com")
                .setObjectMapper(new JacksonObjectMapper(JacksonJsonUtil.objectMapper()));
    }

    @Deactivate
    protected void stop() {
        Unirest.shutDown();
    }
}
