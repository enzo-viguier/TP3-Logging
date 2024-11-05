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
            "(\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2})\\s+(INFO|WARN|ERROR|DEBUG)\\s+\\w+\\s+-\\s+(.+)"
    );
    private static final Pattern USER_LOGIN_PATTERN = Pattern.compile("User (\\w+) logged in");
    private static final Pattern METHOD_PATTERN = Pattern.compile("Executing method: (\\w+)");
    private static final Pattern PRODUCT_FOUND_PATTERN = Pattern.compile("Product found: ([\\w\\s]+)");

    public static class UserProfile {
        String userName;
        LocalDateTime firstSeen;
        LocalDateTime lastSeen;
        int readOperations;
        int writeOperations;
        Set<String> productsAccessed;
        List<String> productsAdded;
        List<String> productsModified;
        List<String> productsDeleted;

        public UserProfile(String userName, LocalDateTime timestamp) {
            this.userName = userName;
            this.firstSeen = timestamp;
            this.lastSeen = timestamp;
            this.readOperations = 0;
            this.writeOperations = 0;
            this.productsAccessed = new HashSet<>();
            this.productsAdded = new ArrayList<>();
            this.productsModified = new ArrayList<>();
            this.productsDeleted = new ArrayList<>();
        }

        public String determineUserType() {
            if (readOperations > writeOperations * 1.5 && readOperations >= 5) {
                return "READER";
            } else if (writeOperations > readOperations * 1.5 && writeOperations >= 5) {
                return "WRITER";
            } else if (productsAccessed.stream().anyMatch(p ->
                    p.contains("Premium") || p.contains("Luxury") ||
                            p.contains("Organic") || p.contains("Aged"))) {
                return "PREMIUM_SEARCHER";
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

            ArrayNode productsAccessedNode = profile.putArray("productsAccessed");
            productsAccessed.forEach(productsAccessedNode::add);

            ArrayNode productsAddedNode = profile.putArray("productsAdded");
            productsAdded.forEach(productsAddedNode::add);

            ArrayNode productsModifiedNode = profile.putArray("productsModified");
            productsModified.forEach(productsModifiedNode::add);

            ArrayNode productsDeletedNode = profile.putArray("productsDeleted");
            productsDeleted.forEach(productsDeletedNode::add);

            profile.put("userType", determineUserType());

            return profile;
        }
    }

    private Map<String, UserProfile> userProfiles = new HashMap<>();
    private String currentUser = null;

    public void parseLogs(String logFile) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                processLogLine(line);
            }
        }
    }

    private void processLogLine(String line) {
        Matcher matcher = LOG_PATTERN.matcher(line);
        if (!matcher.find()) return;

        LocalDateTime timestamp = LocalDateTime.parse(matcher.group(1), formatter);
        String level = matcher.group(2);
        String message = matcher.group(3);

        // Check for user login
        Matcher loginMatcher = USER_LOGIN_PATTERN.matcher(message);
        if (loginMatcher.find()) {
            currentUser = loginMatcher.group(1);
            userProfiles.computeIfAbsent(currentUser, k -> new UserProfile(k, timestamp));
            return;
        }

        if (currentUser == null) return;

        UserProfile profile = userProfiles.get(currentUser);
        profile.lastSeen = timestamp;

        // Process method execution
        Matcher methodMatcher = METHOD_PATTERN.matcher(message);
        if (methodMatcher.find()) {
            String method = methodMatcher.group(1);
            switch (method) {
                case "getProductById":
                case "displayProducts":
                    profile.readOperations++;
                    break;
                case "addProduct":
                case "updateProduct":
                case "deleteProduct":
                    profile.writeOperations++;
                    break;
            }
        }

        // Track accessed products
        Matcher productMatcher = PRODUCT_FOUND_PATTERN.matcher(message);
        if (productMatcher.find()) {
            String productName = productMatcher.group(1);
            profile.productsAccessed.add(productName);
        }

        // Track product operations
        if (message.contains("Product added successfully:")) {
            String productName = message.split("Product added successfully: ")[1];
            profile.productsAdded.add(productName);
        }
    }

    public void generateProfiles(String outputDir) throws IOException {
        ObjectNode rootNode = mapper.createObjectNode();

        // Group profiles by type
        Map<String, ArrayNode> typeArrays = new HashMap<>();
        typeArrays.put("PREMIUM_SEARCHER", rootNode.putArray("premiumSearchers"));
        typeArrays.put("READER", rootNode.putArray("readers"));
        typeArrays.put("WRITER", rootNode.putArray("writers"));
        typeArrays.put("BALANCED", rootNode.putArray("balanced"));

        // Add profiles to their respective groups
        for (UserProfile profile : userProfiles.values()) {
            String type = profile.determineUserType();
            typeArrays.get(type).add(profile.toJson());
        }

        // Add statistics
        ObjectNode stats = rootNode.putObject("statistics");
        stats.put("totalUsers", userProfiles.size());
        typeArrays.forEach((type, array) -> stats.put(
                type.toLowerCase() + "Count",
                array.size()
        ));

        // Create output directory if it doesn't exist
        File outputDirectory = new File(outputDir);
        outputDirectory.mkdirs();

        // Write to file with pretty printing
        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(new File(outputDir + "/user_profiles.json"), rootNode);
    }
}