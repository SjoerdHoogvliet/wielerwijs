package nl.hu.setup;

import javax.ws.rs.ApplicationPath;

import nl.hu.wielerwijs.data.RennerRepository;
import nl.hu.wielerwijs.data.TeamRepository;
import nl.hu.wielerwijs.webservices.RennerResource;
import nl.hu.wielerwijs.webservices.TeamResource;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("api")
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        System.out.println("Initializing JerseyConfig");
        register(CORSFilter.class);
        RennerRepository rennerRepository = new RennerRepository();
        TeamRepository teamRepository = new TeamRepository(rennerRepository);
        register(new RennerResource(rennerRepository));
        register(new TeamResource(teamRepository));
        packages("nl.hu.wielerwijs.webservices");
    }
}
