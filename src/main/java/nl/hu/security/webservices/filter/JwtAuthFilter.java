package nl.hu.security.webservices.filter;

import nl.hu.security.webservices.JwtUtil;
import nl.hu.security.webservices.UserRole;

import javax.annotation.Priority;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.security.Principal;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class JwtAuthFilter implements ContainerRequestFilter {
    @Override
    public void filter(ContainerRequestContext requestContext) {
        String path = requestContext.getUriInfo().getPath();
        if (path.startsWith("user")) {
            return;
        }

        if (HttpMethod.OPTIONS.equalsIgnoreCase(requestContext.getMethod())) {
            return;
        }

        String authHeader = requestContext.getHeaderString("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .header("Access-Control-Allow-Origin", "http://localhost:5173")
                    .header("Access-Control-Allow-Headers", "Origin, Content-Type, Accept, Authorization")
                    .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                    .build());
            return;
        }

        String token = authHeader.substring("Bearer ".length());

        try {
            JwtUtil.validateToken(token);

            String username = JwtUtil.getUsername(token);
            String role = JwtUtil.getRole(token).name();

            final SecurityContext originalContext = requestContext.getSecurityContext();

            final SecurityContext securityContext = new SecurityContext() {
                @Override
                public Principal getUserPrincipal() {
                    return () -> username;
                }

                @Override
                public boolean isUserInRole(String role) {
                    return this.getUserPrincipal().getName().equals(username) && UserRole.valueOf(role).equals(JwtUtil.getRole(token));
                }

                @Override
                public boolean isSecure() {
                    return originalContext != null && originalContext.isSecure();
                }

                @Override
                public String getAuthenticationScheme() {
                    return "Bearer ";
                }
            };

            requestContext.setSecurityContext(securityContext);
        } catch (Exception e) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }
}
