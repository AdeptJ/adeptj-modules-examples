package com.adeptj.modules.examples.jaxrs;

import com.adeptj.modules.examples.mongodb.MongoUserRepository;
import com.adeptj.modules.examples.mongodb.User;
import com.adeptj.modules.jaxrs.api.JaxRSResource;
import com.adeptj.modules.restclient.api.ClientRequest;
import com.adeptj.modules.restclient.api.ClientResponse;
import com.adeptj.modules.restclient.api.HttpMethod;
import com.adeptj.modules.restclient.api.RestClient;
import org.jetbrains.annotations.NotNull;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.json.bind.Jsonb;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Providers;
import java.net.URI;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

@JaxRSResource(name = "MongoUserResource")
@Path("/mongo/users")
@Component(service = MongoUserResource.class)
public class MongoUserResource {

    private final MongoUserRepository userRepository;

    private final RestClient restClient;

    @Activate
    public MongoUserResource(@Reference MongoUserRepository userRepository, @Reference RestClient restClient) {
        this.userRepository = userRepository;
        this.restClient = restClient;
    }

    @GET
    @Path("/reqres")
    @Produces(MediaType.APPLICATION_JSON)
    public ReqResData getUsersFromReqResService() {
        ClientRequest<Void, ReqResData> request = ClientRequest.<Void, ReqResData>builder()
                .uri(URI.create("https://reqres.in/api/users"))
                .responseAs(ReqResData.class)
                .build();
        ClientResponse<ReqResData> response = this.restClient.GET(request);
        this.restClient.doWithHttpClient((client) -> {
            try {
                client.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return response.getContent();
    }

    @POST
    @Path("/typicode")
    @Produces(MediaType.APPLICATION_JSON)
    public String savePost() {
        Post post = new Post();
        post.setTitle("Post From AdeptJ");
        post.setUserId("AdeptJ");
        post.setBody("A short body");
        ClientRequest<Post, String> request = ClientRequest.<Post, String>builder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/posts"))
                .responseAs(String.class)
                .body(post)
                .build();
        ClientResponse<String> response = this.restClient.POST(request);
        return response.getContent();
    }

    @PUT
    @Path("/typicode")
    @Produces(MediaType.APPLICATION_JSON)
    public String updatePost() {
        Post post = new Post();
        post.setId("1");
        post.setTitle("Updated Title - Post From AdeptJ");
        post.setUserId("Updated UserId - AdeptJ");
        post.setBody("UpdatedBody - A short body");
        ClientRequest<Post, String> request = ClientRequest.<Post, String>builder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/posts/1"))
                .responseAs(String.class)
                .body(post)
                .build();
        ClientResponse<String> response = this.restClient.PUT(request);
        return response.getContent();
    }

    @DELETE
    @Path("/typicode")
    @Produces(MediaType.APPLICATION_JSON)
    public String deletePost() {
        ClientRequest<Void, String> request = ClientRequest.<Void, String>builder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/posts/1"))
                .responseAs(String.class)
                .build();
        ClientResponse<String> response = this.restClient.DELETE(request);
        return response.getContent();
    }

    @DELETE
    @Path("/typicode-execute-req")
    @Produces(MediaType.APPLICATION_JSON)
    public String deletePostByExecuteRequest() {
        ClientRequest<Void, String> request = ClientRequest.<Void, String>builder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/posts/1"))
                .method(HttpMethod.DELETE)
                .responseAs(String.class)
                .build();
        ClientResponse<String> response = this.restClient.executeRequest(request);
        return response.getContent();
    }

    @Path("/create")
    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public void createUser(@NotNull String json, @NotNull @Context Providers providers) {
        ContextResolver<Jsonb> resolver = providers.getContextResolver(Jsonb.class, APPLICATION_JSON_TYPE);
        User document = resolver.getContext(Jsonb.class).fromJson(json, User.class);
        this.userRepository.insert(document);
    }

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public User getUser(@PathParam("id") String id) {
        return this.userRepository.findOneById(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getUsers() {
        return this.userRepository.findMany();
    }
}
