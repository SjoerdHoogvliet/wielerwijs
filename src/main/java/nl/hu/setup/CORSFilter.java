/*
 * Simple CORS filter to make sure no CORS issues arise,
 * Made based on code from both a couple of StackOverflow answers and help from ChatGPT
 */

package nl.hu.setup;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
@PreMatching
public class CORSFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        responseContext.getHeaders().putSingle(
                "Access-Control-Allow-Origin", "*"
        );
        responseContext.getHeaders().putSingle(
                "Access-Control-Allow-Headers", "Origin, Content-Type, Accept, Authorization"
        );
        responseContext.getHeaders().putSingle(
                "Access-Control-Allow-Credentials", "true"
        );
        responseContext.getHeaders().putSingle(
                "Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD"
        );
    }
}