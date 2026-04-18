package nl.hu.security.webservices;

import nl.hu.security.data.Exceptions.UserNotFoundException;
import nl.hu.security.data.UserRepository;
import nl.hu.security.domain.User;
import org.mindrot.jbcrypt.BCrypt;

import javax.annotation.security.RolesAllowed;
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
        job.add("id", user.getId());
        job.add("username", user.getUsername());
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
    public String getUsers() {
        return userListToJsonConverter(userRepository.getUsers()).toString();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public String register(String requestBody) {
        try {
            JsonObject jsonObject = Json.createReader(new StringReader(requestBody)).readObject();

            String passwordHash = BCrypt.hashpw(jsonObject.getString("password"), BCrypt.gensalt(10));
            User user = new User(jsonObject.getString("username"), passwordHash, UserRole.ROLE_USER);

            userRepository.addUser(user);
            return userToJsonConverter(userRepository.getUserById(user.getId())).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).entity(Json.createObjectBuilder().add("message", "Something went wrong").build().toString()).build().toString();
        }
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
        JsonObject loginResponse = Json.createObjectBuilder()
            .add("token", token)
            .add("userId", user.getId())
            .add("role", user.getRole().toString())
            .build();

        return Response.ok(loginResponse.toString()).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserById(@PathParam("id") String id) {
        try {
            if (userRepository.getUserById(id) == null) {
                throw new UserNotFoundException("User not found");
            }
            return Response.ok(userToJsonConverter(userRepository.getUserById(id)).toString()).build();
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).entity(Json.createObjectBuilder().add("message", e.getMessage()).build().toString()).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Json.createObjectBuilder().add("message", "Something went wrong").build().toString()).build();
        }
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(@PathParam("id") String id) {
        try {
            userRepository.removeUser(id);
            return Response.ok().build();
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).entity(Json.createObjectBuilder().add("message", e.getMessage()).build().toString()).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Json.createObjectBuilder().add("message", "Something went wrong").build().toString()).build();
        }
    }

    @PUT
    @RolesAllowed("ROLE_ADMIN")
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response makeUserAdmin(@PathParam("id") String id) {
        try {
            userRepository.makeUserAdmin(id);
            return Response.ok().build();
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).entity(Json.createObjectBuilder().add("message", e.getMessage()).build().toString()).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Json.createObjectBuilder().add("message", "Something went wrong").build().toString()).build();
        }
    }
}
