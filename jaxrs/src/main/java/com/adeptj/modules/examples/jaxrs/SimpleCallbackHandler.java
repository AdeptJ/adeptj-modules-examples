package com.adeptj.modules.examples.jaxrs;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;

public class SimpleCallbackHandler implements CallbackHandler {

    private final String username;

    private final char[] password;

    public SimpleCallbackHandler(String username, char[] password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public void handle(Callback[] callbacks) {
        for (Callback c : callbacks) {
            if (c instanceof NameCallback) {
                ((NameCallback) c).setName(this.username);
            }
            if (c instanceof PasswordCallback) {
                ((PasswordCallback) c).setPassword(this.password);
            }
        }
    }
}
