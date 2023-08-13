package com.adeptj.modules.examples.jaxrs;

import com.adeptj.modules.examples.mongodb.MongoUserRepository;
import com.adeptj.modules.examples.mongodb.User;
import com.adeptj.modules.jaxrs.api.JaxRSResource;
import com.adeptj.modules.restclient.core.ClientRequest;
import com.adeptj.modules.restclient.core.ClientResponse;
import com.adeptj.modules.restclient.core.HttpMethod;
import com.adeptj.modules.restclient.core.RestClient;
import org.jetbrains.annotations.NotNull;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import jakarta.json.bind.Jsonb;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.Providers;
import java.net.URI;
import java.util.List;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

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
        return response.getContent();
    }

    @POST
    @Path("/solify/save-application")
    @Produces(MediaType.APPLICATION_JSON)
    public String saveApplication(String payload) {
        ClientRequest<String, String> request = ClientRequest.<String, String>builder()
                .uri(URI.create("https://api.fittleuat.idscloud.io/rapport-v1.0/api/Application/SendApplication"))
                .header("Authorization", "Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6Ijh5MEczWFd4T2hDakhhdnlLbWZzMSJ9.eyJodHRwczovL2lkc2Nsb3VkLmlvL2NsaWVudF9uYW1lIjoiZml0dGxlLXVhdC1jbGllbnRpbnRlZ3JhdGlvbi05IiwiaHR0cHM6Ly9pZHNjbG91ZC5jb20vYXBwX25hbWUiOiJkZWFsZXJwb3J0YWwiLCJpc3MiOiJodHRwczovL2F1dGh1YXQuaWRzY2xvdWQuaW8vIiwic3ViIjoiWERySTRNWlJvMzNXelVHZEpoazlBQ1Z2R2JLSk93ellAY2xpZW50cyIsImF1ZCI6Imh0dHBzOi8vZml0dGxlLXVhdC1pZHNjbG91ZC1hcGkvIiwiaWF0IjoxNjY4MTYwNDcwLCJleHAiOjE2NjgyNDY4NzAsImF6cCI6IlhEckk0TVpSbzMzV3pVR2RKaGs5QUNWdkdiS0pPd3pZIiwiZ3R5IjoiY2xpZW50LWNyZWRlbnRpYWxzIn0.Y0fertVolhQ-zhIQQ8V1Ku3bVtn0rSRv9OGy-HCkUNG2Idf28YLTXUt04VYR-eEW4zb_SP5dQcKXvKBHtCGgLfVr6RIt2YdqgHfxP2tSuGoCa-XJ2Wv96gHoFG7JfIfKQBNr9E5qt4DfldxVE1aQATLzyBbd-siYztedtzylDgROLLlTH8uV5QptTcHfmKJHL5vxLWRmNrmvwNGW1F7ykWyOT0LyiDt2bDYI45wDHQaQozJNHXspb3DqpBMGC50I9MxgGZkk5NSxstOnXZ9QUSBGstrhxTNjg9p1uUrrF2gdtvBxOrzbESg3Sy14nNoFm76nYihHjlmwUm52IDvtIQ")
                .header("Content-Type", "application/json")
                .responseAs(String.class)
                .body(payload)
                .build();
        ClientResponse<String> response = this.restClient.POST(request);
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

    @Path("/create-new")
    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public User createUserNew(@NotNull User document) {
        this.userRepository.insert(document);
        return document;
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
