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

import com.adeptj.modules.commons.cache.Cache;
import com.adeptj.modules.commons.cache.CacheService;
import com.adeptj.modules.commons.crypto.CryptoService;
import com.adeptj.modules.commons.crypto.PasswordEncoder;
import com.adeptj.modules.commons.email.EmailInfo;
import com.adeptj.modules.commons.email.EmailService;
import com.adeptj.modules.commons.email.EmailType;
import com.adeptj.modules.commons.utils.JakartaJsonUtil;
import com.adeptj.modules.commons.utils.TimeUtil;
import com.adeptj.modules.examples.jpa.UserRepository;
import com.adeptj.modules.examples.jpa.entity.User;
import com.adeptj.modules.jaxrs.api.JaxRSResource;
import com.adeptj.modules.jaxrs.api.RequiresAuthentication;
import com.adeptj.modules.jaxrs.core.SecurityContextUtil;
import org.jetbrains.annotations.NotNull;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.json.bind.Jsonb;
import javax.validation.constraints.NotEmpty;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Providers;
import java.lang.invoke.MethodHandles;
import java.security.Principal;
import java.util.List;
import java.util.Set;

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

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final UserRepository userRepository;

    private final CacheService cacheService;

    private final PasswordEncoder passwordEncoder;

    private final CryptoService cryptoService;

    private final EmailService emailService;

    @Activate
    public JpaUserResource(@Reference UserRepository userRepository,
                           @Reference CacheService cacheService,
                           @Reference PasswordEncoder passwordEncoder,
                           @Reference CryptoService cryptoService,
                           @Reference EmailService emailService) {
        this.userRepository = userRepository;
        this.cacheService = cacheService;
        this.passwordEncoder = passwordEncoder;
        this.cryptoService = cryptoService;
        this.emailService = emailService;
    }

    @GET
    @Produces(APPLICATION_JSON)
    public List<User> getUsers(@QueryParam("cacheName") String cacheName) {
        final Cache<String, List<User>> cache = this.cacheService.getCache(cacheName);
        final List<User> users = this.userRepository.findAll(User.class);
        cache.put("users", users);
        LOGGER.info("Cache size before eviction {}", cache.size());
        this.cacheService.evictCaches(cacheName);
        LOGGER.info("Cache size after eviction {}", cache.size());
        return users;
    }

    @Path("/me")
    @GET
    @Produces(APPLICATION_JSON)
    public User me(@Context SecurityContext securityContext, @QueryParam("id") String id) {
        return this.userRepository.findById(User.class, Long.parseLong(id));
        //LOGGER.debug("securityContext: {}", securityContext);
        //return SecurityContextUtil.getJwtPrincipal(securityContext);
    }

    @RolesAllowed({"admin", "OSGiAdmin"})
    @Path("/me-roles-allowed")
    @POST
    @Produces(APPLICATION_JSON)
    public Principal meRolesAllowed(@Context SecurityContext securityContext) {
        return SecurityContextUtil.getJwtPrincipal(securityContext);
    }

    @Path("/encode-password")
    @POST
    @Consumes(APPLICATION_FORM_URLENCODED)
    public String encodePassword(@NotEmpty @FormParam("password") String password) {
        return this.passwordEncoder.encode(password.toCharArray());
    }

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
    public Response insertUser(@NotNull String json, @NotNull @Context Providers providers) {
        ContextResolver<Jsonb> resolver = providers.getContextResolver(Jsonb.class, APPLICATION_JSON_TYPE);
        Jsonb jsonb = resolver.getContext(Jsonb.class);
        long start = System.nanoTime();
        User entity = jsonb.fromJson(json, User.class);
        LOGGER.info("(create) Unmarshalling took: {}", TimeUtil.elapsedMillis(start));
        User insert = this.userRepository.insert(entity);
        return Response.ok(insert).build();
    }

    @Path("/create1")
    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response insertUser1(@NotNull String json) {
        long start = System.nanoTime();
        User entity = JakartaJsonUtil.deserialize(json, User.class);
        LOGGER.info("(create1) Unmarshalling took: {}", TimeUtil.elapsedMillis(start));
        User insert = this.userRepository.insert(entity);
        return Response.ok(insert).build();
    }

    @Path("/send-email")
    @POST
    @Consumes(APPLICATION_FORM_URLENCODED)
    public String sendEmail(@FormParam("message") String message, @FormParam("toAddress") String toAddress) {
        EmailInfo info = new EmailInfo();
        info.setSubject("Test email from AdeptJ EmailService");
        info.setMessage(message);
        info.setToAddresses(Set.of(toAddress));
        this.emailService.sendEmail(EmailType.SIMPLE, info);
        return "OK";
    }
}
