package nl.hu.setup;

import javax.ws.rs.ApplicationPath;

import nl.hu.security.data.UserRepository;
import nl.hu.security.webservices.UserResource;
import nl.hu.wielerwijs.data.CategoryVoteRepository;
import nl.hu.wielerwijs.data.RennerRepository;
import nl.hu.wielerwijs.data.TeamRepository;
import nl.hu.wielerwijs.webservices.CategoryVoteResource;
import nl.hu.wielerwijs.webservices.RennerResource;
import nl.hu.wielerwijs.webservices.TeamResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

@ApplicationPath("api")
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        register(CORSFilter.class);
        RennerRepository rennerRepository = new RennerRepository();
        UserRepository userRepository = new UserRepository();
        TeamRepository teamRepository = new TeamRepository(rennerRepository, userRepository);
        CategoryVoteRepository categoryVoteRepository = new CategoryVoteRepository(rennerRepository, userRepository);
        register(new RennerResource(rennerRepository));
        register(new TeamResource(teamRepository));
        register(new UserResource(userRepository));
        register(new CategoryVoteResource(categoryVoteRepository));
        register(RolesAllowedDynamicFeature.class);
        packages("nl.hu.wielerwijs.webservices");
        packages("nl.hu.security.webservices");
    }
}
