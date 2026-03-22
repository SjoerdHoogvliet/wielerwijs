package nl.hu.wielerwijs.data;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import nl.hu.wielerwijs.domain.Renner;
import nl.hu.wielerwijs.domain.Team;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TeamRepository {
    private List<Team> teams = new ArrayList<>();
    private final RennerRepository rennerRepository;

    public TeamRepository(RennerRepository rennerRepository) {
        this.rennerRepository = rennerRepository;
        loadTeams();
    }

    public void loadTeams() {
        if (!new File("teamstore.csv").exists()) {
            System.out.println("No teamstore.csv found, creating new file");
            saveTeams();
        }

        try (CSVReader reader = new CSVReader(new FileReader("teamstore.csv"))) {
            String[] nextLine;
            reader.readNext(); // skip header
            while ((nextLine = reader.readNext()) != null) {
                System.out.println(nextLine[0] + " " + nextLine[1]);
                String riders = nextLine[2];
                String[] riderIds = riders.split(";");
                List<Renner> ridersList = new ArrayList<>();
                for (String riderId : riderIds) {
                    if (!riderId.isEmpty()) {
                        Renner renner = rennerRepository.getRennerById(riderId);
                        ridersList.add(renner);
                    }
                }
                Team team = new Team(nextLine[0], nextLine[1], ridersList, Integer.parseInt(nextLine[3]), Integer.parseInt(nextLine[4]));
                teams.add(team);
            }
        } catch (Exception e) {
            System.out.println("Error while initializing context");
            e.printStackTrace();
        }
    }

    public void saveTeams() {
        try (CSVWriter writer = new CSVWriter(new FileWriter("teamstore.csv"))) {
            writer.writeNext(new String[]{"ID", "Naam", "Renners", "Likes", "Dislikes"});
            for (Team team : teams) {
                String renners = "";
                for (Renner renner : team.getRenners()) {
                    renners += renner.getId() + ";";
                }
                writer.writeNext(new String[]{team.getId(), team.getNaam(), renners, String.valueOf(team.getLikes()), String.valueOf(team.getDislikes())});
                System.out.println(team);
            }
        } catch (Exception e) {
            System.out.println("Error while saving teamstore");
            e.printStackTrace();
        }
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void addTeam(Team team, List<String> renners) {
        // Though UUID basically guarantees uniqueness, there is no absolute guarantee, check uniqueness for certainty
        while (getTeamById(team.getId()) != null) {
            String newId = UUID.randomUUID().toString();
            team.setId(newId);
        }

        for (String renner : renners) {
            team.addRenner(rennerRepository.getRennerById(renner));
        }

        teams.add(team);
        saveTeams();
    }

    public void removeTeam(Team team) {
        teams.remove(team);
        saveTeams();
    }

    public Team getTeamById(String id) {
        for (Team team : teams) {
            if (team.getId().equals(id)) {
                return team;
            }
        }
        return null;
    }

    public void likeTeam(String id) {
        Team team = getTeamById(id);
        team.likeTeam();
        saveTeams();
    }

    public void dislikeTeam(String id) {
        Team team = getTeamById(id);
        team.dislikeTeam();
        saveTeams();
    }
}
