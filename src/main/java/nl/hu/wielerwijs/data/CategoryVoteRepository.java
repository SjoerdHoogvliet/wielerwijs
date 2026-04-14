package nl.hu.wielerwijs.data;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import nl.hu.security.data.UserRepository;
import nl.hu.security.domain.User;
import nl.hu.wielerwijs.data.Exceptions.AlreadyVotedException;
import nl.hu.wielerwijs.data.Exceptions.NoRennerFoundException;
import nl.hu.wielerwijs.data.Exceptions.NoUserFoundException;
import nl.hu.wielerwijs.domain.CategoryVote;
import nl.hu.wielerwijs.domain.Renner;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class CategoryVoteRepository {
    private List<CategoryVote> categoryVotes = new ArrayList<>();

    private final RennerRepository rennerRepository;
    private final UserRepository userRepository;

    public CategoryVoteRepository(RennerRepository rennerRepository, UserRepository userRepository) {
        this.rennerRepository = rennerRepository;
        this.userRepository = userRepository;

        loadVotes();
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
                User user = userRepository.getUserById(nextLine[1]);
                Renner renner = rennerRepository.getRennerById(nextLine[2]);
                if(renner == null) {
                    throw new NoRennerFoundException("Renner with Id " + nextLine[2] + " not found");
                }
                if(user == null) {
                    throw new NoUserFoundException("User with Id " + nextLine[1] + " not found");
                }

                categoryVotes.add(new CategoryVote(nextLine[0], user, renner));
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
                writer.writeNext(new String[]{categoryVote.getCategory(), categoryVote.getUser().getId(), categoryVote.getRenner().getId()});
            }
        } catch (Exception e) {
            System.out.println("Error while saving votestore");
            e.printStackTrace();
        }
    }

    public List<CategoryVote> getVotesForRenner(String rennerId) {
        List<CategoryVote> returnList = new ArrayList<>();

        for (CategoryVote categoryVote : categoryVotes) {
            if(categoryVote.getRenner().getId().equals(rennerId)) {
                returnList.add(categoryVote);
            }
        }

        return returnList;
    }

    public List<CategoryVote> getVotesForUser(String userId) {
        List<CategoryVote> returnList = new ArrayList<>();

        for (CategoryVote categoryVote : categoryVotes) {
            if(categoryVote.getUser().getId().equals(userId)) {
                returnList.add(categoryVote);
            }
        }
        return returnList;
    }

    public void addVote(String category, String userId, String rennerId) {
        User user = userRepository.getUserById(userId);
        Renner renner = rennerRepository.getRennerById(rennerId);
        if(renner == null) {
            throw new NoRennerFoundException("Renner with Id " + rennerId + " not found");
        }
        if(user == null) {
            throw new NoUserFoundException("User with Id " + userId + " not found");
        }

        CategoryVote categoryVote = new CategoryVote(category, user, renner);

        for(CategoryVote vote: getVotesForRenner(categoryVote.getRenner().getId())){
            if(vote.getUser().equals(categoryVote.getUser())) {
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
