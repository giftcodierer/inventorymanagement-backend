package org.example.resource;

import org.example.authentication.AuthenticationResponse;
import org.example.authentication.JwtUtil;
import org.example.authentication.UserRepository;
import org.example.model.User;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.logging.Logger;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    UserRepository userRepository;

    @Inject
    JwtUtil jwtUtil;

    private static final Logger LOGGER = Logger.getLogger(AuthResource.class.getName());

    @POST
    @Path("/login")
    public Response login(User loginUser) {
        LOGGER.info("Attempting to log in user: " + loginUser.getUsername());
        User user = userRepository.findByUsername(loginUser.getUsername());
        if (user == null) {
            LOGGER.warning("User not found: " + loginUser.getUsername());
            return Response.status(Response.Status.UNAUTHORIZED).entity(new AuthenticationResponse(false, null, null)).build();
        }
        if (!checkPassword(loginUser.getPassword(), user.getPassword())) {
            LOGGER.warning("Password mismatch for user: " + loginUser.getUsername());
            return Response.status(Response.Status.UNAUTHORIZED).entity(new AuthenticationResponse(false, null, null)).build();
        }
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole(), user.getId());
        AuthenticationResponse response = new AuthenticationResponse(true, user.getRole(), token);
        LOGGER.info("Generated token: " + token); // Log the generated token
        LOGGER.info("User logged in successfully: " + loginUser.getUsername());
        return Response.ok(response).build();
    }

    private boolean checkPassword(String plainPassword, String hashedPassword) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(plainPassword.getBytes());
            String encodedHash = Base64.getEncoder().encodeToString(hash);
            return encodedHash.equals(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error checking password", e);
        }
    }
}