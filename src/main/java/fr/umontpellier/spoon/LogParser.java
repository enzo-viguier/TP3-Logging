package fr.umontpellier.spoon;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogParser {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final Pattern LOG_PATTERN = Pattern.compile(
            "(\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2})\\s+(INFO|DEBUG|WARN|ERROR)\\s+Menu\\s+-\\s+(.+)"
    );
    private static final Pattern USER_NAME_PATTERN = Pattern.compile("(\\w+)\\s+(?:viewing|fetching|added|updated|deleted|searching)");
    private static final Pattern PRICE_PATTERN = Pattern.compile("Price: (\\d+\\.\\d+)");
    private static final Pattern PRODUCT_ID_PATTERN = Pattern.compile("ID: (\\d+)");

    public static class UserProfile {
        private String userName;
        private int readOperations;
        private int writeOperations;
        private List<Double> searchedPrices;
        private Set<Integer> searchedIds;
        private List<String> addedProducts;
        private LocalDateTime firstSeen;
        private LocalDateTime lastSeen;

        public UserProfile(String userName, LocalDateTime timestamp) {
            this.userName = userName;
            this.readOperations = 0;
            this.writeOperations = 0;
            this.searchedPrices = new ArrayList<>();
            this.searchedIds = new HashSet<>();
            this.addedProducts = new ArrayList<>();
            this.firstSeen = timestamp;
            this.lastSeen = timestamp;
        }

        public String determineUserType() {
            double avgSearchPrice = searchedPrices.stream()
                    .mapToDouble(Double::doubleValue)
                    .average()
                    .orElse(0.0);

            // Premium searcher if average search price > 50 and they've done at least 3 searches
            if (avgSearchPrice > 50.0 && searchedPrices.size() >= 3) {
                return "PREMIUM_SEARCHER";
            }
            // Reader if they do 50% more reads than writes and have done at least 5 operations
            else if (readOperations > writeOperations * 1.5 && (readOperations + writeOperations) >= 5) {
                return "READER";
            }
            // Writer if they do 50% more writes than reads and have done at least 5 operations
            else if (writeOperations > readOperations * 1.5 && (readOperations + writeOperations) >= 5) {
                return "WRITER";
            }
            return "BALANCED";
        }

        public ObjectNode toJson() {
            ObjectNode profile = mapper.createObjectNode();
            profile.put("userName", userName);
            profile.put("firstSeen", firstSeen.toString());
            profile.put("lastSeen", lastSeen.toString());
            profile.put("readOperations", readOperations);
            profile.put("writeOperations", writeOperations);
            profile.put("totalOperations", readOperations + writeOperations);

            double avgSearchPrice = searchedPrices.stream()
                    .mapToDouble(Double::doubleValue)
                    .average()
                    .orElse(0.0);
            profile.put("averageSearchPrice", avgSearchPrice);
            profile.put("numberOfPriceSearches", searchedPrices.size());

            ArrayNode pricesArray = profile.putArray("searchedPrices");
            searchedPrices.forEach(pricesArray::add);

            ArrayNode idsArray = profile.putArray("searchedIds");
            searchedIds.forEach(idsArray::add);

            ArrayNode productsArray = profile.putArray("addedProducts");
            addedProducts.forEach(productsArray::add);

            profile.put("userType", determineUserType());

            return profile;
        }
    }

    private Map<String, UserProfile> userProfiles = new HashMap<>();

    public void parseLogs(String logFile) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("\t")) continue;
                processLogLine(line);
            }
        }
    }

    private void processLogLine(String line) {
        Matcher matcher = LOG_PATTERN.matcher(line);
        if (!matcher.find()) return;

        LocalDateTime timestamp = LocalDateTime.parse(matcher.group(1), formatter);
        String message = matcher.group(3);

        // Extract user name
        Matcher userMatcher = USER_NAME_PATTERN.matcher(message);
        if (!userMatcher.find()) return;

        String userName = userMatcher.group(1);
        UserProfile profile = userProfiles.computeIfAbsent(userName,
                k -> new UserProfile(k, timestamp));
        profile.lastSeen = timestamp;

        // Update operations count and collect data
        if (message.contains("viewing") || message.contains("fetching") || message.contains("searching")) {
            profile.readOperations++;

            // Extract product ID if present
            Matcher idMatcher = PRODUCT_ID_PATTERN.matcher(message);
            if (idMatcher.find()) {
                profile.searchedIds.add(Integer.parseInt(idMatcher.group(1)));
            }

            // Extract price if present
            Matcher priceMatcher = PRICE_PATTERN.matcher(message);
            if (priceMatcher.find()) {
                double price = Double.parseDouble(priceMatcher.group(1));
                profile.searchedPrices.add(price);
            }
        } else if (message.contains("added") || message.contains("updated") || message.contains("deleted")) {
            profile.writeOperations++;

            // If it's an add operation, store the product name
            if (message.contains("added")) {
                String[] parts = message.split("added product: ");
                if (parts.length > 1) {
                    String productName = parts[1].split(" \\(")[0];
                    profile.addedProducts.add(productName);
                }
            }
        }
    }

    public void generateProfiles(String outputDir) throws IOException {
        ObjectNode rootNode = mapper.createObjectNode();

        // Create profile groups
        ObjectNode profileGroups = rootNode.putObject("profileGroups");
        Map<String, ArrayNode> typeArrays = new HashMap<>();
        typeArrays.put("PREMIUM_SEARCHER", profileGroups.putArray("premiumSearchers"));
        typeArrays.put("READER", profileGroups.putArray("readers"));
        typeArrays.put("WRITER", profileGroups.putArray("writers"));
        typeArrays.put("BALANCED", profileGroups.putArray("balanced"));

        // Add profiles to their respective groups
        for (UserProfile profile : userProfiles.values()) {
            String type = profile.determineUserType();
            typeArrays.get(type).add(profile.toJson());
        }

        // Add statistics
        ObjectNode stats = rootNode.putObject("statistics");
        stats.put("totalUsers", userProfiles.size());
        stats.put("premiumSearchers", typeArrays.get("PREMIUM_SEARCHER").size());
        stats.put("readers", typeArrays.get("READER").size());
        stats.put("writers", typeArrays.get("WRITER").size());
        stats.put("balanced", typeArrays.get("BALANCED").size());

        // Create output directory if it doesn't exist
        File outputDirectory = new File(outputDir);
        outputDirectory.mkdirs();

        // Write to file with pretty printing
        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(new File(outputDir + "/user_profiles.json"), rootNode);
    }

    public Map<String, UserProfile> getUserProfiles() {
        return userProfiles;
    }
}