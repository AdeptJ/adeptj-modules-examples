package com.adeptj.modules.examples.jaxrs;

import com.adeptj.modules.jaxrs.api.UsernamePasswordCredential;

import javax.security.auth.callback.Callback;

public class SimpleCredentialsCallback implements Callback {

    private final UsernamePasswordCredential credential;

    public SimpleCredentialsCallback(UsernamePasswordCredential credentials) {
        this.credential = credentials;
    }

    public UsernamePasswordCredential getCredential() {
        return credential;
    }
}
