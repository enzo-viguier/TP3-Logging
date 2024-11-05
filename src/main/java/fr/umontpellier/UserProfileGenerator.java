package fr.umontpellier;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserProfileGenerator {

    private static final Pattern LOG_PATTERN = Pattern.compile("UserID:(\\d+) - Action:(\\w+)( - Product Price: (\\d+\\.\\d+))?");

    public static void main(String[] args) throws IOException {
        Map<Integer, UserProfile> userProfiles = new HashMap<>();

        // Lire le fichier de logs
        try (BufferedReader reader = new BufferedReader(new FileReader("logs/app.log"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Matcher matcher = LOG_PATTERN.matcher(line);
                if (matcher.find()) {
                    int userId = Integer.parseInt(matcher.group(1));
                    String action = matcher.group(2);
                    double price = matcher.group(4) != null ? Double.parseDouble(matcher.group(4)) : 0.0;

                    userProfiles.putIfAbsent(userId, new UserProfile(userId));
                    UserProfile profile = userProfiles.get(userId);

                    switch (action) {
                        case "READ":
                            profile.incrementReadOperations();
                            break;
                        case "WRITE":
                            profile.incrementWriteOperations();
                            break;
                        case "SEARCH_EXPENSIVE":
                            if (price > 100) { // Définit un prix minimum pour être considéré comme "coûteux"
                                profile.addExpensiveProduct("Product with price " + price);
                            }
                            break;
                    }
                }
            }
        }

        // Sauvegarde des profils en JSON
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode profilesArray = mapper.createArrayNode();
        for (UserProfile profile : userProfiles.values()) {
            profilesArray.add(profile.toJson(mapper));
        }
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File("user_profiles.json"), profilesArray);

        System.out.println("Profiles saved to user_profiles.json");
    }
}
