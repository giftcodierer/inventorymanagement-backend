package org.example.authentication;

public class AuthenticationResponse {
    private boolean success;
    private String role;
    private String jwt;

    public AuthenticationResponse(boolean success, String role, String jwt) {
        this.success = success;
        this.role = role;
        this.jwt = jwt;
    }

    // Getters
    public boolean isSuccess() {
        return success;
    }

    public String getRole() {
        return role;
    }

    public String getJwt() {
        return jwt;
    }
}