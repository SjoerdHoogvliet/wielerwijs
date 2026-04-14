package nl.hu.security.webservices;

import nl.hu.security.data.UserRepository;
import nl.hu.security.domain.User;
import org.mindrot.jbcrypt.BCrypt;

import javax.json.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.StringReader;
import java.util.List;

import static nl.hu.security.webservices.JwtUtil.generateToken;

@Path("user")
public class UserResource {
    private final UserRepository userRepository;

    public UserResource(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private JsonObject userToJsonConverter(User user) {
        JsonObjectBuilder job = Json.createObjectBuilder();
        job.add("id", user.getID());
        job.add("username", user.getUsername());
        job.add("passwordHash", user.getPasswordHash());
        job.add("role", user.getRole());
        return job.build();
    }

    private JsonArray userListToJsonConverter(List<User> users) {
        JsonArrayBuilder jab = Json.createArrayBuilder();
        for (User user : users) {
            jab.add(userToJsonConverter(user));
        }
        return jab.build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public JsonArray getUsers() {
        return userListToJsonConverter(userRepository.getUsers());
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject register(String requestBody) {
        JsonObject jsonObject = Json.createReader(new StringReader(requestBody)).readObject();


        String passwordHash = BCrypt.hashpw(jsonObject.getString("password"), BCrypt.gensalt(10));
        User user = new User(jsonObject.getString("username"), passwordHash, "ROLE_USER");

        userRepository.addUser(user);
        return userToJsonConverter(userRepository.getUserById(user.getID()));
    }

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(String requestBody) {
        if (requestBody == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("No credentials provided").build();
        }
        JsonObject jsonObject = Json.createReader(new StringReader(requestBody)).readObject();
        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");
        User user = userRepository.getUserByUsername(username);
        if (user == null || !BCrypt.checkpw(password, user.getPasswordHash())) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid credentials").build();
        }

        String token = generateToken(username, user.getRole());
        JsonObject tokenResponse = Json.createObjectBuilder()
            .add("token", token)
            .build();

        return Response.ok(tokenResponse.toString()).build();
    }
}
