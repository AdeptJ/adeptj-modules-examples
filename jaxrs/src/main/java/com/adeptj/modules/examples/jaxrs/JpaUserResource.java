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

package com.adeptj.modules.examples.jaxrs;

import com.adeptj.modules.cache.caffeine.Cache;
import com.adeptj.modules.cache.caffeine.CacheService;
import com.adeptj.modules.commons.crypto.CryptoService;
import com.adeptj.modules.commons.crypto.PasswordEncoder;
import com.adeptj.modules.examples.jpa.UserRepository;
import com.adeptj.modules.examples.jpa.entity.User;
import com.adeptj.modules.jaxrs.core.JaxRSResource;
import com.adeptj.modules.jaxrs.core.RequiresAuthentication;
import com.adeptj.modules.jaxrs.core.SecurityContextUtil;
import org.jetbrains.annotations.NotNull;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.annotation.security.RolesAllowed;
import javax.json.JsonObject;
import javax.json.bind.Jsonb;
import javax.validation.constraints.NotEmpty;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Providers;
import java.security.Principal;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

/**
 * JAX-RS resource for issuance and verification of JWT.
 *
 * @author Rakesh.Kumar, AdeptJ
 */
@JaxRSResource(name = "JpaUserResource")
@Path("/jpa/users")
@Component(service = JpaUserResource.class)
public class JpaUserResource {

    private final UserRepository userRepository;

    private final CacheService cacheService;

    private final PasswordEncoder passwordEncoder;

    private final CryptoService cryptoService;

    @Activate
    public JpaUserResource(@Reference UserRepository userRepository,
                           @Reference CacheService cacheService,
                           @Reference PasswordEncoder passwordEncoder,
                           @Reference CryptoService cryptoService) {
        this.userRepository = userRepository;
        this.cacheService = cacheService;
        this.passwordEncoder = passwordEncoder;
        this.cryptoService = cryptoService;
    }

    @RequiresAuthentication
    @GET
    @Produces(APPLICATION_JSON)
    public List<User> getUsers() {
        final Cache<String, List<User>> cache = this.cacheService.getCache("MyCache");
        return cache.get("user", o -> this.userRepository.findAll(User.class));
    }

    @RequiresAuthentication
    @Path("/me")
    @POST
    @Produces(APPLICATION_JSON)
    public Principal me(@Context SecurityContext securityContext) {
        return SecurityContextUtil.getJwtPrincipal(securityContext);
    }

    @RolesAllowed({"admin", "OSGiAdmin"})
    @Path("/me-roles-allowed")
    @POST
    @Produces(APPLICATION_JSON)
    public Principal meRolesAllowed(@Context SecurityContext securityContext) {
        return SecurityContextUtil.getJwtPrincipal(securityContext);
    }

    @RequiresAuthentication
    @Path("/encode-password")
    @POST
    @Consumes(APPLICATION_FORM_URLENCODED)
    public String encodePassword(@NotEmpty @FormParam("password") String password) {
        return this.passwordEncoder.encode(password.toCharArray());
    }

    @RequiresAuthentication
    @Path("/match-password")
    @POST
    @Consumes(APPLICATION_FORM_URLENCODED)
    public boolean matchPassword(@FormParam("password") String password,
                                 @FormParam("encodedPassword") String encodedPassword) {
        return this.passwordEncoder.matches(password.toCharArray(), encodedPassword.toCharArray());
    }

    @RequiresAuthentication
    @Path("/encrypt-text")
    @POST
    @Consumes(APPLICATION_FORM_URLENCODED)
    public String encryptText(@NotEmpty @FormParam("plainText") String plainText) {
        return this.cryptoService.encrypt(plainText);
    }

    @RequiresAuthentication
    @Path("/decrypt-text")
    @POST
    @Consumes(APPLICATION_FORM_URLENCODED)
    public String decryptText(@FormParam("encryptedText") String encryptedText) {
        return this.cryptoService.decrypt(encryptedText);
    }

    @Path("/create")
    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response insertUser(@NotNull JsonObject object, @NotNull @Context Providers providers) {
        ContextResolver<Jsonb> resolver = providers.getContextResolver(Jsonb.class, APPLICATION_JSON_TYPE);
        User entity = resolver.getContext(Jsonb.class).fromJson(object.toString(), User.class);
        User insert = this.userRepository.insert(entity);
        return Response.ok(insert).build();
    }
}
