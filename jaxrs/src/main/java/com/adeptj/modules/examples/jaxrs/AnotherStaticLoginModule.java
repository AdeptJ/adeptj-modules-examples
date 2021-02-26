package com.adeptj.modules.examples.jaxrs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import java.lang.invoke.MethodHandles;
import java.util.Map;

public class AnotherStaticLoginModule implements LoginModule {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public void initialize(Subject subject, CallbackHandler callbackHandler,
                           Map<String, ?> sharedState, Map<String, ?> options) {
        LOGGER.info("in initialize!!");
    }

    @Override
    public boolean login() throws LoginException {
        LOGGER.info("in login!!");
        return true;
    }

    @Override
    public boolean commit() throws LoginException {
        LOGGER.info("in commit!!");
        return false;
    }

    @Override
    public boolean abort() throws LoginException {
        LOGGER.info("in abort!!");
        return false;
    }

    @Override
    public boolean logout() throws LoginException {
        LOGGER.info("in logout!!");
        return false;
    }
}
