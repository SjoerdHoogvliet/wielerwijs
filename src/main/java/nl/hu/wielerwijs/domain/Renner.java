package nl.hu.wielerwijs.domain;

import java.util.UUID;

public class Renner {
    private String id;
    private String naam;

    // Constructor for getting a renner from the database
    public Renner(String id, String naam) {
        this.id = id;
        this.naam = naam;
    }

    // Constructor for creating a new renner
    public Renner(String naam) {
        this.id = UUID.randomUUID().toString();
        this.naam = naam;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public String toString() {
        return "ID: " + id + ", Naam: " + naam;
    }
}
