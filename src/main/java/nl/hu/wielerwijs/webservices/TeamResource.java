package nl.hu.wielerwijs.webservices;

import nl.hu.wielerwijs.data.TeamRepository;
import nl.hu.wielerwijs.domain.Renner;
import nl.hu.wielerwijs.domain.Team;

import javax.json.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static nl.hu.wielerwijs.webservices.RennerResource.rennerToJsonConverter;

@Path("team")
public class TeamResource {
    private final TeamRepository teamRepository;

    public TeamResource(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    private JsonObject teamToJsonConverter(Team team) {
        JsonObjectBuilder job = Json.createObjectBuilder();
        job.add("id", team.getId());
        job.add("naam", team.getNaam());
        if (!team.getRenners().isEmpty()) {
            System.out.println(team.getRenners());
            JsonArrayBuilder renners = Json.createArrayBuilder();
            for (Renner renner : team.getRenners()) {
                renners.add(rennerToJsonConverter(renner));
            }
            job.add("renners", renners);
            job.add("likes", team.getLikes());
            job.add("dislikes", team.getDislikes());
        }
        return job.build();
    }

    private JsonArray teamListToJsonConverter(List<Team> teams) {
        JsonArrayBuilder jab = Json.createArrayBuilder();
        for (Team team : teams) {
            jab.add(teamToJsonConverter(team));
        }
        return jab.build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getTeams() {
        return teamListToJsonConverter(teamRepository.getTeams()).toString();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getTeam(@PathParam("id") String id) {
        if (teamRepository.getTeamById(id) == null) {
            JsonObjectBuilder job = Json.createObjectBuilder();
            job.add("message", "Team with ID " + id + " not found");
            return job.build().toString();
        }
        return teamToJsonConverter(teamRepository.getTeamById(id)).toString();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public String addTeam(String requestBody) {
        JsonObject jsonObject = Json.createReader(new StringReader(requestBody)).readObject();

        Team team = new Team(jsonObject.getString("naam"));
        System.out.println("Nieuw team: " + team);
        List<String> rennerIds = new ArrayList<>();
        if (jsonObject.containsKey("renners")) {
            for (JsonValue renner : jsonObject.getJsonArray("renners")) {
                System.out.println(renner);
                // Renner IDs will still have the quotes around them, remove them
                rennerIds.add(renner.toString().replace("\"", ""));
            }
            System.out.println(rennerIds);
        }

        try {
            teamRepository.addTeam(team, rennerIds);
        } catch (IllegalArgumentException e) {
            JsonObjectBuilder job = Json.createObjectBuilder();
            job.add("message", e.getMessage());
            return job.build().toString();
        }
        return getTeams();
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String removeTeam(@PathParam("id") String id) {
        teamRepository.removeTeam(teamRepository.getTeamById(id));
        return getTeams();
    }

    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String handleTeamLike(@PathParam("id") String id, String requestBody) {
        JsonObject jsonObject = Json.createReader(new StringReader(requestBody)).readObject();
        if (jsonObject.containsKey("like")) {
            boolean like = jsonObject.getBoolean("like");
            if (like) {
                teamRepository.likeTeam(id);
            } else {
                teamRepository.dislikeTeam(id);
            }
        }
        return getTeam(id);
    }
}
