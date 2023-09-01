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

package com.adeptj.modules.examples.jaxrs.resource;

import com.adeptj.modules.commons.cache.Cache;
import com.adeptj.modules.commons.cache.CacheService;
import com.adeptj.modules.examples.jpa.UserRepository;
import com.adeptj.modules.examples.jpa.entity.User;
import com.adeptj.modules.jaxrs.api.JaxRSResource;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import org.jetbrains.annotations.NotNull;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.ArrayList;
import java.util.List;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

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

    @Activate
    public JpaUserResource(@Reference UserRepository userRepository, @Reference CacheService cacheService) {
        this.userRepository = userRepository;
        this.cacheService = cacheService;
    }

    @GET
    @Produces(APPLICATION_JSON)
    public List<User> getUsers(@QueryParam("cacheName") String cacheName) {
        Cache<String, List<User>> cache = this.cacheService.getCache(cacheName);
        if (cache == null) {
            return new ArrayList<>();
        }
        return cache.get("users", (key) -> this.userRepository.findAll(User.class));
    }

    @Path("/{id}")
    @GET
    @Produces(APPLICATION_JSON)
    public User getUser(@PathParam("id") Long id) {
        return this.userRepository.findById(User.class, id);
    }

    @Path("/create")
    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response insertUser(@NotNull User entity) {
        User insert = this.userRepository.insert(entity);
        return Response.ok(insert).build();
    }

    @Path("/update")
    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response updateUser(@NotNull User entity) {
        User updated = this.userRepository.update(entity);
        return Response.ok(updated).build();
    }

    @Path("/delete/{id}")
    @GET
    public Response deleteUser(@PathParam("id") Long id) {
        this.userRepository.delete(User.class, id);
        return Response.ok().build();
    }
}
