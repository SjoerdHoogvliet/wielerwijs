package nl.hu.wielerwijs.webservices;

import nl.hu.wielerwijs.data.CategoryVoteRepository;
import nl.hu.wielerwijs.data.Exceptions.AlreadyVotedException;
import nl.hu.wielerwijs.data.Exceptions.NoUserFoundException;
import nl.hu.wielerwijs.domain.CategoryVote;

import javax.json.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.StringReader;
import java.util.List;

@Path("categoryvote")
public class CategoryVoteResource {
    CategoryVoteRepository categoryVoteRepository;

    public CategoryVoteResource(CategoryVoteRepository categoryVoteRepository) {
        this.categoryVoteRepository = categoryVoteRepository;
    }

    public JsonObject categoryVoteToJsonConverter(CategoryVote categoryVote) {
        JsonObjectBuilder job = Json.createObjectBuilder();
        job.add("category", categoryVote.getCategory());
        job.add("userId", categoryVote.getUser().getId());
        job.add("rennerId", categoryVote.getRenner().getId());
        return job.build();
    }

    public JsonArray categoryVoteListToJsonConverter(List<CategoryVote> categoryVotes) {
        JsonArrayBuilder jab = Json.createArrayBuilder();
        for (CategoryVote categoryVote : categoryVotes) {
            jab.add(categoryVoteToJsonConverter(categoryVote));
        }
        return jab.build();
    }

    @GET
    @Path("renner/{rennerId}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getVotesForRenner(@PathParam("rennerId") String rennerId) {
        return categoryVoteListToJsonConverter(categoryVoteRepository.getVotesForRenner(rennerId)).toString();
    }

    @GET
    @Path("user/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getVotesForUser(@PathParam("userId") String userId) {
        return categoryVoteListToJsonConverter(categoryVoteRepository.getVotesForUser(userId)).toString();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response castVote(String requestBody) {
        JsonObject jsonObject = Json.createReader(new StringReader(requestBody)).readObject();

        try {
            categoryVoteRepository.addVote(jsonObject.getString("category"), jsonObject.getString("userId"), jsonObject.getString("rennerId"));
        } catch (AlreadyVotedException e) {
            e.printStackTrace();
            return Response.status(Response.Status.CONFLICT).entity(Json.createObjectBuilder().add("message", e.getMessage()).build().toString()).build();
        } catch (NoUserFoundException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).entity(Json.createObjectBuilder().add("message", e.getMessage()).build().toString()).build();
        } catch (NullPointerException e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).entity(Json.createObjectBuilder().add("message", "Make sure category, userId and rennerId are all present").build().toString()).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Json.createObjectBuilder().add("message", "Something went wrong").build().toString()).build();
        }

        return Response.ok().build();
    }
}
