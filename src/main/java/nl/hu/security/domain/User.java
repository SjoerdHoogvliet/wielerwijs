package nl.hu.security.domain;

import nl.hu.security.webservices.UserRole;

import java.util.UUID;

public class User {
    private String Id;
    private String username;
    private String passwordHash;
    private UserRole role;

    public User(String username, String passwordHash, UserRole role) {
        this.Id = UUID.randomUUID().toString();
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public User(String Id, String username, String passwordHash, UserRole role) {
        this.Id = Id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        this.Id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
