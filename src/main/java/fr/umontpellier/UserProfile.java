package fr.umontpellier;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.List;

public class UserProfile {
    private int userId;
    private int readOperations;
    private int writeOperations;
    private List<String> expensiveProductSearches;

    public UserProfile(int userId) {
        this.userId = userId;
        this.expensiveProductSearches = new ArrayList<>();
    }

    public void incrementReadOperations() {
        readOperations++;
    }

    public void incrementWriteOperations() {
        writeOperations++;
    }

    public void addExpensiveProduct(String productName) {
        expensiveProductSearches.add(productName);
    }

    public ObjectNode toJson(ObjectMapper mapper) {
        ObjectNode node = mapper.createObjectNode();
        node.put("userId", userId);
        node.put("readOperations", readOperations);
        node.put("writeOperations", writeOperations);
        ArrayNode productsArray = mapper.createArrayNode();
        for (String product : expensiveProductSearches) {
            productsArray.add(product);
        }
        node.set("expensiveProductSearches", productsArray);
        return node;
    }
}
