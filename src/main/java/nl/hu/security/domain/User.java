package nl.hu.security.domain;

import java.util.UUID;

public class User {
    private String ID;
    private String username;
    private String passwordHash;
    private String role;

    public User(String username, String passwordHash, String role) {
        this.ID = UUID.randomUUID().toString();
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public User(String ID, String username, String passwordHash, String role) {
        this.ID = ID;
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
