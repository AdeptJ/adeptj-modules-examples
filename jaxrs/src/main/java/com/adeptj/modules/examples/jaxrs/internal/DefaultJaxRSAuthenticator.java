/*
###############################################################################
#                                                                             #
#    Copyright 2016, AdeptJ (http://www.adeptj.com)                           #
#                                                                             #
#    Licensed under the Apache License, Version 2.0 (the "License");          #
#    you may not use this file except in compliance with the License.         #
#    You may obtain a copy of the License at                                  #
#                                                                             #
#        http://www.apache.org/licenses/LICENSE-2.0                           #
#                                                                             #
#    Unless required by applicable law or agreed to in writing, software      #
#    distributed under the License is distributed on an "AS IS" BASIS,        #
#    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. #
#    See the License for the specific language governing permissions and      #
#    limitations under the License.                                           #
#                                                                             #
###############################################################################
*/

package com.adeptj.modules.examples.jaxrs.internal;

import com.adeptj.modules.jaxrs.api.JaxRSAuthenticationOutcome;
import com.adeptj.modules.jaxrs.api.JaxRSAuthenticator;
import com.adeptj.modules.jaxrs.api.UsernamePasswordCredential;
import org.jetbrains.annotations.NotNull;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.osgi.service.component.annotations.ReferenceCardinality.MULTIPLE;
import static org.osgi.service.component.annotations.ReferencePolicy.DYNAMIC;

/**
 * Provides {@link JaxRSAuthenticationOutcome} by querying the ConfigAdmin configuration based data store.
 *
 * @author Rakesh.Kumar, AdeptJ
 */
@Component
public class DefaultJaxRSAuthenticator implements JaxRSAuthenticator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final List<UsernamePasswordCredential> credentials;

    public DefaultJaxRSAuthenticator() {
        this.credentials = new CopyOnWriteArrayList<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JaxRSAuthenticationOutcome authenticate(@NotNull UsernamePasswordCredential credential) {
        return this.credentials.stream()
                .filter(credential::equals)
                .map(sc -> new JaxRSAuthenticationOutcome().addRolesInJwtClaim(sc.getRoles()))
                .findFirst()
                .orElse(null);
    }

    // <<------------------------------------------ OSGi INTERNAL -------------------------------------------->>

    @Deactivate
    protected void stop() {
        this.credentials.forEach(UsernamePasswordCredential::clear);
        this.credentials.clear();
    }

    @Reference(service = JaxRSCredentialsFactory.class, cardinality = MULTIPLE, policy = DYNAMIC)
    protected void bindJaxRSCredentialsFactory(@NotNull JaxRSCredentialsFactory credentialsFactory) {
        UsernamePasswordCredential credential = credentialsFactory.getCredential();
        if (this.credentials.contains(credential)) {
            String username = credential.getUsername();
            LOGGER.warn("Username: [{}] already present, ignoring these credentials!!", username);
            throw new IllegalStateException(String.format("Username: [%s] already present, ignoring these credentials!!", username));
        }
        this.credentials.add(credential);
    }

    protected void unbindJaxRSCredentialsFactory(@NotNull JaxRSCredentialsFactory credentialsFactory) {
        this.credentials.remove(credentialsFactory.getCredential());
    }
}
