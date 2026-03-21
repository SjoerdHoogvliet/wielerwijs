package nl.hu.wielerwijs.data;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import nl.hu.wielerwijs.domain.Renner;

import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RennerRepository {
    private List<Renner> renners = new ArrayList<>();

    public RennerRepository() {
        System.out.println("Init repo");
        loadRenners();
    }

    public void loadRenners() {
        Path path = Paths.get("rennerstore.csv");
        System.out.println(path.toAbsolutePath());
        try (CSVReader reader = new CSVReader(new FileReader("rennerstore.csv"))) {
            String[] nextLine;
            reader.readNext(); // skip header
            while ((nextLine = reader.readNext()) != null) {
                System.out.println(nextLine[0] + " " + nextLine[1]);
                Renner renner = new Renner(nextLine[0], nextLine[1]);
                renners.add(renner);
            }
        } catch (Exception e) {
            System.out.println("Error while loading renners");
            e.printStackTrace();
        }
    }

    public void saveRenners() {
        System.out.println("Saving renners");
        try (CSVWriter writer = new CSVWriter(new FileWriter("rennerstore.csv"))) {
            writer.writeNext(new String[]{"ID", "Naam"});
            for (Renner renner : renners) {
                writer.writeNext(new String[]{renner.getId(), renner.getNaam()});
                System.out.println(renner);
            }
        } catch (Exception e) {
            System.out.println("Error while saving renners");
            e.printStackTrace();
        }
    }

    public List<Renner> getRenners() {
        return renners;
    }

    public void addRenner(Renner renner) {
        // Though UUID basically guarantees uniqueness, there is no absolute guarantee, check uniqueness for certainty
        while (getRennerById(renner.getId()) != null) {
            String newId = UUID.randomUUID().toString();
            renner.setId(newId);
        }

        renners.add(renner);
        saveRenners();
    }

    public void removeRenner(Renner renner) {
        renners.remove(renner);
        saveRenners();
    }

    public Renner getRennerById(String id) {
        for (Renner renner : renners) {
            System.out.println(id.trim() + " vs " + renner.getId() + " = " + renner.getId().equals(id));
            if (renner.getId().trim().equals(id.trim())) {
                return renner;
            }
        }
        return null;
    }
}
