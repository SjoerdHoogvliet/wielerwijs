package nl.hu.security.data;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import nl.hu.security.domain.User;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserRepository {
    ArrayList<User> users = new ArrayList<>();

    public UserRepository() {
        System.out.println("Init user repo");
        loadUsers();
    }

    public void loadUsers() {
        if (!new File("userstore.csv").exists()) {
            System.out.println("No userstore.csv found, creating new file");
            saveUsers();
        }

        Path path = Paths.get("userstore.csv");
        try (CSVReader reader = new CSVReader(new FileReader("userstore.csv"))) {
            String[] nextLine;
            reader.readNext(); // skip header
            while ((nextLine = reader.readNext()) != null) {
                User user = new User(nextLine[0], nextLine[1], nextLine[2], nextLine[3]);
                users.add(user);
            }
        }
        // TODO: More specific exception handling
        catch (Exception e) {
            System.out.println("Error while loading users");
            e.printStackTrace();
        }
    }

    public void saveUsers() {
        try (CSVWriter writer = new CSVWriter(new FileWriter("userstore.csv"))) {
            writer.writeNext(new String[]{"ID", "Naam", "Gebruikersnaam", "Wachtwoord", "Rol"});
            for (User user : users) {
                writer.writeNext(new String[]{user.getId(), user.getUsername(), user.getPasswordHash(), user.getRole()});
                System.out.println(user);
            }
        } catch (Exception e) {
            System.out.println("Error while saving users");
            e.printStackTrace();
        }
    }

    public List<User> getUsers() {
        return users;
    }

    public User getUserById(String id) {
        for (User user : users) {
            if (user.getId().trim().equals(id.trim())) {
                return user;
            }
        }
        return null;
    }

    public User getUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().trim().equals(username.trim())) {
                return user;
            }
        }
        return null;
    }

    public void addUser(User user) {
        // Though UUID basically guarantees uniqueness, there is no absolute guarantee, check uniqueness for certainty
        while (getUserById(user.getId()) != null) {
            String newId = UUID.randomUUID().toString();
            user.setId(newId);
        }

        users.add(user);
        saveUsers();
    }

    public void removeUser(User user) {
        users.remove(user);
        saveUsers();
    }
}
