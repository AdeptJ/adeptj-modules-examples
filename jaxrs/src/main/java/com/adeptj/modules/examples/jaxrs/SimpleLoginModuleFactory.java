package com.adeptj.modules.examples.jaxrs;

import org.apache.felix.jaas.LoginModuleFactory;
import org.osgi.service.component.annotations.Component;

import javax.security.auth.spi.LoginModule;

@Component(property = {
        "jaas.controlFlag=Sufficient",
        "jaas.realmName=AdeptJ Realm",
        "jaas.ranking=Integer:1"
})
public class SimpleLoginModuleFactory implements LoginModuleFactory {

    @Override
    public LoginModule createLoginModule() {
        return new SimpleLoginModule();
    }
}
