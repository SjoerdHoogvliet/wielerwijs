package nl.hu.wielerwijs.data;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import nl.hu.security.data.UserRepository;
import nl.hu.security.domain.User;
import nl.hu.wielerwijs.data.Exceptions.AlreadyVotedException;
import nl.hu.wielerwijs.domain.CategoryVote;
import nl.hu.wielerwijs.domain.Renner;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CategoryVoteRepository {
    private List<CategoryVote> categoryVotes = new ArrayList<>();

    private final RennerRepository rennerRepository;
    private final UserRepository userRepository;

    public CategoryVoteRepository(RennerRepository rennerRepository, UserRepository userRepository) {
        this.rennerRepository = rennerRepository;
        this.userRepository = userRepository;
    }

    public void loadVotes() {
        if (!new File("categoryvotestore.csv").exists()) {
            System.out.println("No categoryvotestore.csv found, creating new file");
            saveVotes();
        }

        try (CSVReader reader = new CSVReader(new FileReader("categoryvotestore.csv"))) {
            String[] nextLine;
            reader.readNext(); // skip header
            while ((nextLine = reader.readNext()) != null) {
                Renner renner = rennerRepository.getRennerById(nextLine[1]);
                User user = userRepository.getUserByUsername(nextLine[2]);

                new CategoryVote(nextLine[0], user, renner);
            }
        } catch (Exception e) {
            System.out.println("Error while initializing context");
            e.printStackTrace();
        }
    }

    public void saveVotes() {
        try (CSVWriter writer = new CSVWriter(new FileWriter("categoryvotestore.csv"))) {
            writer.writeNext(new String[]{"Category", "Username", "RennerID"});
            for (CategoryVote categoryVote : categoryVotes) {
                writer.writeNext(new String[]{categoryVote.category(), categoryVote.user().getID(), categoryVote.renner().getId()});
                System.out.println(categoryVote);
            }
        } catch (Exception e) {
            System.out.println("Error while saving votestore");
            e.printStackTrace();
        }
    }

    public List<CategoryVote> getVotes() {
        return categoryVotes;
    }

    public List<CategoryVote> getVotesForRenner(Renner renner) {
        List<CategoryVote> returnList = new ArrayList<>();

        for (CategoryVote categoryVote : categoryVotes) {
            if(categoryVote.renner().equals(renner)) {
                returnList.add(categoryVote);
            }
        }

        return returnList;
    }

    public List<CategoryVote> getVotesForUser(User user) {
        List<CategoryVote> returnList = new ArrayList<>();

        for (CategoryVote categoryVote : categoryVotes) {
            if(categoryVote.user().equals(user)) {
                returnList.add(categoryVote);
            }
        }
        return returnList;
    }

    public void addVote(CategoryVote categoryVote) {
        for(CategoryVote vote: getVotesForRenner(categoryVote.renner())){
            if(vote.user().equals(categoryVote.user())) {
                throw new AlreadyVotedException("You've already voted for this renner");
            }
        }
        categoryVotes.add(categoryVote);
        saveVotes();
    }

    public void removeVote(CategoryVote categoryVote) {
        categoryVotes.remove(categoryVote);
        saveVotes();
    }
}
