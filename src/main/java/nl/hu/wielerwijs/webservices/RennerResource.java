package nl.hu.wielerwijs.webservices;

import javax.json.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import nl.hu.wielerwijs.data.RennerRepository;
import nl.hu.wielerwijs.data.TeamRepository;
import nl.hu.wielerwijs.domain.Renner;

import java.io.StringReader;
import java.util.List;

@Path("renner")
public class RennerResource {
    private final RennerRepository rennerRepository;

    public RennerResource(RennerRepository rennerRepository) {
        System.out.println("Init resource");
        this.rennerRepository = rennerRepository;
    }

    // TODO: static?
    public static JsonObject rennerToJsonConverter(Renner renner) {
        JsonObjectBuilder job = Json.createObjectBuilder();
        job.add("id", renner.getId());
        job.add("naam", renner.getNaam());
        return job.build();
    }

    private JsonArray rennerListToJsonConverter(List<Renner> renners) {
        JsonArrayBuilder jab = Json.createArrayBuilder();
        for (Renner renner : renners) {
            jab.add(rennerToJsonConverter(renner));
        }
        return jab.build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getRiders() {
        List<Renner> renners = rennerRepository.getRenners();
        return rennerListToJsonConverter(renners).toString();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public String addRider(String requestBody) {
        JsonObject jsonObject = Json.createReader(new StringReader(requestBody)).readObject();

        Renner renner = new Renner(jsonObject.getString("naam"));

        rennerRepository.addRenner(renner);
        return getRiders();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getRider(@PathParam("id") String id) {
        Renner renner = rennerRepository.getRennerById(id);
        return rennerToJsonConverter(renner).toString();
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String removeRider(@PathParam("id") String id) {
        rennerRepository.removeRenner(rennerRepository.getRennerById(id));
        return getRiders();
    }
}
