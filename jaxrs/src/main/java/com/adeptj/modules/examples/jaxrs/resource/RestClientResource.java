package com.adeptj.modules.examples.jaxrs.resource;

import com.adeptj.modules.examples.jaxrs.Post;
import com.adeptj.modules.examples.jaxrs.ReqResData;
import com.adeptj.modules.jaxrs.api.JaxRSResource;
import com.adeptj.modules.restclient.core.ClientRequest;
import com.adeptj.modules.restclient.core.ClientResponse;
import com.adeptj.modules.restclient.core.HttpMethod;
import com.adeptj.modules.restclient.core.RestClient;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.net.URI;

@JaxRSResource(name = "RestClientResource")
@Path("/rest-client")
@Component(service = RestClientResource.class)
public class RestClientResource {

    private final RestClient restClient;

    @Activate
    public RestClientResource(@Reference RestClient restClient) {
        this.restClient = restClient;
    }

    @GET
    @Path("/reqres")
    @Produces(MediaType.APPLICATION_JSON)
    public ReqResData getUsersFromReqResService() {
        ClientRequest<?, ReqResData> request = ClientRequest.builder(ReqResData.class)
                .uri(URI.create("https://reqres.in/api/users"))
                .build();
        ClientResponse<ReqResData> response = this.restClient.get(request);
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
        ClientRequest<Post, String> request = ClientRequest.<Post, String>builder(String.class)
                .uri(URI.create("https://jsonplaceholder.typicode.com/posts"))
                .body(post)
                .build();
        ClientResponse<String> response = this.restClient.post(request);
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
        ClientRequest<Post, String> request = ClientRequest.<Post, String>builder(String.class)
                .uri(URI.create("https://jsonplaceholder.typicode.com/posts/1"))
                .body(post)
                .build();
        ClientResponse<String> response = this.restClient.put(request);
        return response.getContent();
    }

    @DELETE
    @Path("/typicode")
    @Produces(MediaType.APPLICATION_JSON)
    public String deletePost() {
        ClientRequest<?, String> request = ClientRequest.builder(String.class)
                .uri(URI.create("https://jsonplaceholder.typicode.com/posts/1"))
                .build();
        ClientResponse<String> response = this.restClient.delete(request);
        return response.getContent();
    }

    @DELETE
    @Path("/typicode-execute-req")
    @Produces(MediaType.APPLICATION_JSON)
    public String deletePostByExecuteRequest() {
        ClientRequest<?, String> request = ClientRequest.builder(String.class)
                .uri(URI.create("https://jsonplaceholder.typicode.com/posts/1"))
                .method(HttpMethod.DELETE)
                .build();
        ClientResponse<String> response = this.restClient.execute(request);
        return response.getContent();
    }
}
