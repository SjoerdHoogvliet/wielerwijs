package nl.hu.wielerwijs.domain;

import nl.hu.security.domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Team {
    private String id;
    private String naam;
    private List<Renner> renners;
    private User owner;
    private int likes;
    private int dislikes;

    // Constructor for creating a new team
    public Team(String naam, User owner) {
        this.id = UUID.randomUUID().toString();
        this.naam = naam;
        this.renners = new ArrayList<>();
        this.owner = owner;
        this.likes = 0;
        this.dislikes = 0;
    }

    // Constructor for getting a team from the database
    public Team(String id, String naam, List<Renner> renners, User owner, int likes, int dislikes) {
        this.id = id;
        this.naam = naam;
        this.renners = renners;
        this.owner = owner;
        this.likes = likes;
        this.dislikes = dislikes;
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

    public User getOwner() {
        return owner;
    }

    public List<Renner> getRenners() {
        return renners;
    }

    public void setRenners(List<Renner> renners) {
        this.renners = renners;
    }

    public void addRenner(Renner renner) {
        this.renners.add(renner);
    }

    public void removeRenner(Renner renner) {
        this.renners.remove(renner);
    }

    public String toString() {
        String returnString = "ID: " + id + ", Naam: " + naam;
        if (renners.size() > 0) {
            returnString += ", Renners: ";
            for (Renner renner : renners) {
                returnString += renner.getId() + " ";
            }
        }
        returnString += ", Likes: " + likes + ", Dislikes: " + dislikes + " Saldo: " + (likes - dislikes);
        return returnString;
    }

    public int getLikes() {
        return likes;
    }

    public void likeTeam() {
        likes++;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void dislikeTeam() {
        dislikes++;
    }
}
