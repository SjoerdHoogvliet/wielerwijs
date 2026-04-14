package nl.hu.wielerwijs.data;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import nl.hu.security.data.UserRepository;
import nl.hu.security.domain.User;
import nl.hu.wielerwijs.data.Exceptions.AlreadyVotedException;
import nl.hu.wielerwijs.data.Exceptions.CategoryVoteNotFoundException;
import nl.hu.wielerwijs.data.Exceptions.NoRennerFoundException;
import nl.hu.wielerwijs.data.Exceptions.NoUserFoundException;
import nl.hu.wielerwijs.domain.CategoryVote;
import nl.hu.wielerwijs.domain.Renner;
import nl.hu.wielerwijs.domain.RiderCategoryEnum;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
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

                categoryVotes.add(new CategoryVote(RiderCategoryEnum.valueOf(nextLine[0]), user, renner));
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
                writer.writeNext(new String[]{categoryVote.getCategory().toString(), categoryVote.getUser().getId(), categoryVote.getRenner().getId()});
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

        CategoryVote categoryVote = new CategoryVote(RiderCategoryEnum.valueOf(category), user, renner);

        for(CategoryVote vote: getVotesForRenner(categoryVote.getRenner().getId())){
            if(vote.getUser().equals(categoryVote.getUser())) {
                throw new AlreadyVotedException("You've already voted for this renner");
            }
        }
        categoryVotes.add(categoryVote);
        saveVotes();
    }

    public void removeVote(String userId, String rennerId) {
        // Since users can only vote for a rider once we know that the following for statement will find a vote if it exists
        for(CategoryVote categoryVote: getVotesForRenner(rennerId)) {
            if(categoryVote.getUser().getId().equals(userId)) {
                categoryVotes.remove(categoryVote);
                saveVotes();
                return;
            }
        }

        throw new CategoryVoteNotFoundException("Category vote not found");
    }

    private JsonObject calculateVoteStatistics(List<CategoryVote> categoryVotes) {
        int totalVotes = categoryVotes.size();

        int klassementsRennerVotes = 0;
        int klassiekeRennerVotes = 0;
        int sprinterVotes = 0;
        int klimmerVotes = 0;
        int knechtVotes = 0;
        int tijdrijderVotes = 0;
        int aanvallerVotes = 0;

        for(CategoryVote categoryVote: categoryVotes) {
            switch(categoryVote.getCategory()) {
                case KLASSEMENTSRENNER -> klassementsRennerVotes++;
                case KLASSIEKE_RENNER -> klassiekeRennerVotes++;
                case SPRINTER -> sprinterVotes++;
                case KLIMMER -> klimmerVotes++;
                case KNECHT -> knechtVotes++;
                case TIJDRIJDER -> tijdrijderVotes++;
                case AANVALLER -> aanvallerVotes++;
            }
        }

        double percentageKlassementsRennerVotes = 0;
        double percentageKlassiekeRennerVotes = 0;
        double percentageSprinterVotes = 0;
        double percentageKlimmerVotes = 0;
        double percentageKnechtVotes = 0;
        double percentageTijdrijderVotes = 0;
        double percentageAanvallerVotes = 0;

        if(totalVotes != 0) {
            percentageKlassementsRennerVotes = (double) klassementsRennerVotes / totalVotes * 100;
            percentageKlassiekeRennerVotes = (double) klassiekeRennerVotes / totalVotes * 100;
            percentageSprinterVotes = (double) sprinterVotes / totalVotes * 100;
            percentageKlimmerVotes = (double) klimmerVotes / totalVotes * 100;
            percentageKnechtVotes = (double) knechtVotes / totalVotes * 100;
            percentageTijdrijderVotes = (double) tijdrijderVotes / totalVotes * 100;
            percentageAanvallerVotes = (double) aanvallerVotes / totalVotes * 100;
        }
        JsonObjectBuilder job = Json.createObjectBuilder();
        job.add("totalVotes", totalVotes);
        job.add("klassementsRennerVotes", klassementsRennerVotes);
        job.add("klassiekeRennerVotes", klassiekeRennerVotes);
        job.add("sprinterVotes", sprinterVotes);
        job.add("klimmerVotes", klimmerVotes);
        job.add("knechtVotes", knechtVotes);
        job.add("tijdrijderVotes", tijdrijderVotes);
        job.add("aanvallerVotes", aanvallerVotes);
        job.add("percentageKlassementsRennerVotes", percentageKlassementsRennerVotes);
        job.add("percentageKlassiekeRennerVotes", percentageKlassiekeRennerVotes);
        job.add("percentageSprinterVotes", percentageSprinterVotes);
        job.add("percentageKlimmerVotes", percentageKlimmerVotes);
        job.add("percentageKnechtVotes", percentageKnechtVotes);
        job.add("percentageTijdrijderVotes", percentageTijdrijderVotes);
        job.add("percentageAanvallerVotes", percentageAanvallerVotes);

        return job.build();
    }

    public JsonObject getVoteStatisticsForRenner(String rennerId) {
        return calculateVoteStatistics(getVotesForRenner(rennerId));
    }

    public JsonObject getVoteStatisticsForUser(String userId) {
        return calculateVoteStatistics(getVotesForUser(userId));
    }
}
