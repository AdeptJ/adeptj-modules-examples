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

import com.adeptj.modules.jaxrs.api.UsernamePasswordCredential;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.osgi.annotation.versioning.ProviderType;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.metatype.annotations.Designate;

import static com.adeptj.modules.examples.jaxrs.internal.JaxRSCredentialsFactory.PID;
import static org.osgi.service.component.annotations.ConfigurationPolicy.REQUIRE;

/**
 * Manages {@link JaxRSCredentialsConfig} created vis OSGi web console.
 *
 * @author Rakesh.Kumar, AdeptJ
 */
@ProviderType
@Designate(ocd = JaxRSCredentialsConfig.class, factory = true)
@Component(service = JaxRSCredentialsFactory.class, name = PID, configurationPolicy = REQUIRE)
public class JaxRSCredentialsFactory {

    static final String PID = "com.adeptj.modules.examples.jaxrs.JaxRSCredentials.factory";

    private final UsernamePasswordCredential credential;

    @Activate
    public JaxRSCredentialsFactory(JaxRSCredentialsConfig config) {
        Validate.isTrue(StringUtils.isNotEmpty(config.username()), "Username can't be blank!!");
        Validate.isTrue(StringUtils.isNotEmpty(config.password()), "Password can't be blank!!");
        this.credential = new UsernamePasswordCredential(config.username(), config.password());
        this.credential.addRoles(config.roles());
    }

    UsernamePasswordCredential getCredential() {
        return this.credential;
    }

    // <<------------------------------------- OSGi Internal  -------------------------------------->>

    @Deactivate
    protected void stop() {
        this.credential.clear();
    }
}
