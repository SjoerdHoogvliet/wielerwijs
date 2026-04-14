package nl.hu.wielerwijs.webservices;

import nl.hu.security.domain.User;
import nl.hu.wielerwijs.data.CategoryVoteRepository;
import nl.hu.wielerwijs.data.Exceptions.AlreadyVotedException;
import nl.hu.wielerwijs.data.Exceptions.CategoryVoteNotFoundException;
import nl.hu.wielerwijs.data.Exceptions.NoUserFoundException;
import nl.hu.wielerwijs.domain.CategoryVote;

import javax.json.*;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
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
        job.add("category", categoryVote.getCategory().toString());
        job.add("userId", categoryVote.getUser().getId());
        job.add("username", categoryVote.getUser().getUsername());
        job.add("rennerId", categoryVote.getRenner().getId());
        job.add("rennerName", categoryVote.getRenner().getNaam());
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
    public Response getVotesForRenner(@PathParam("rennerId") String rennerId, @Context HttpServletRequest request) {
        try {
            return Response.ok().entity(categoryVoteListToJsonConverter(categoryVoteRepository.getVotesForRenner(rennerId)).toString()).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Json.createObjectBuilder().add("message", "Something went wrong").build().toString()).build();
        }
    }

    @GET
    @Path("user/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getVotesForUser(@PathParam("userId") String userId) {
        try {
            return Response.ok().entity(categoryVoteListToJsonConverter(categoryVoteRepository.getVotesForUser(userId)).toString()).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Json.createObjectBuilder().add("message", "Something went wrong").build().toString()).build();
        }
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

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteVote(String requestBody) {
        JsonObject jsonObject = Json.createReader(new StringReader(requestBody)).readObject();

        try {
            String userId = jsonObject.getString("userId");
            String rennerId = jsonObject.getString("rennerId");

            categoryVoteRepository.removeVote(userId, rennerId);
        } catch (CategoryVoteNotFoundException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).entity(Json.createObjectBuilder().add("message", e.getMessage()).build().toString()).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Json.createObjectBuilder().add("message", "Something went wrong").build().toString()).build();
        }

        return Response.ok().build();
    }

    @GET
    @Path("renner/{rennerId}/statistics")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRiderVoteStatistics(@PathParam("rennerId") String rennerId) {
        try {
            return Response.ok(categoryVoteRepository.getVoteStatisticsForRenner(rennerId).toString()).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Json.createObjectBuilder().add("message", "Something went wrong").build().toString()).build();
        }
    }

    @GET
    @Path("user/{userId}/statistics")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserVoteStatistics(@PathParam("userId") String userId) {
        try {
            return Response.ok(categoryVoteRepository.getVoteStatisticsForUser(userId).toString()).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Json.createObjectBuilder().add("message", "Something went wrong").build().toString()).build();
        }
    }
}
