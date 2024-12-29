package org.example.authentication;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.security.Principal;

@Provider
public class JwtRequestFilter implements ContainerRequestFilter {

    private JwtUtil jwtUtil = new JwtUtil();

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String path = requestContext.getUriInfo().getPath();

        if (path.equals("/auth/login")) {
            return;
        }

        String authorizationHeader = requestContext.getHeaderString("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            try {
                String username = jwtUtil.extractUsername(token);
                String role = jwtUtil.extractRole(token);

                if (username != null && jwtUtil.validateToken(token, username)) {
                    SecurityContext originalContext = requestContext.getSecurityContext();
                    CustomSecurityContext customContext = new CustomSecurityContext(role, new Principal() {
                        @Override
                        public String getName() {
                            return username;
                        }
                    });
                    requestContext.setSecurityContext(customContext);
                    return;
                }
            } catch (Exception e) {
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("Invalid token").build());
            }
        }
        requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("Authorization header must be provided").build());
    }
}