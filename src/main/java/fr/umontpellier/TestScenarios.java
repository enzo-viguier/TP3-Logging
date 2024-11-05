package fr.umontpellier;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TestScenarios {
    private static final Random random = new Random();

    // Test users with different profiles
    private static final List<User> users = Arrays.asList(
            new User(1, "John_Manager", 45, "john@company.com", "pass123"),
            new User(2, "Alice_Inventory", 32, "alice@company.com", "pass456"),
            new User(3, "Bob_Clerk", 25, "bob@company.com", "pass789"),
            new User(4, "Emma_Supervisor", 38, "emma@company.com", "pass321"),
            new User(5, "Mike_Trainee", 22, "mike@company.com", "pass654"),
            new User(6, "Sarah_Manager", 41, "sarah@company.com", "pass987"),
            new User(7, "David_Stock", 29, "david@company.com", "pass147"),
            new User(8, "Lisa_Admin", 35, "lisa@company.com", "pass258"),
            new User(9, "Tom_Intern", 23, "tom@company.com", "pass369"),
            new User(10, "Anna_Senior", 48, "anna@company.com", "pass741")
    );

    // Sample products for testing
    private static final List<Product> sampleProducts = Arrays.asList(
            new Product(1, "Milk", 2.99, LocalDate.now().plusDays(14)),
            new Product(2, "Bread", 1.99, LocalDate.now().plusDays(7)),
            new Product(3, "Cheese", 4.99, LocalDate.now().plusDays(30)),
            new Product(4, "Yogurt", 3.49, LocalDate.now().plusDays(21)),
            new Product(5, "Eggs", 2.49, LocalDate.now().plusDays(28))
    );

    public static void main(String[] args) {
        ProductRepository repository = new ProductRepository();

        // Execute scenarios for each user
        for (User user : users) {
            System.out.println("\nExecuting scenarios for user: " + user.getName());
            executeUserScenarios(repository, user);
        }
    }

    private static void executeUserScenarios(ProductRepository repository, User user) {
        // Each user performs about 20 operations
        for (int i = 0; i < 20; i++) {
            int scenario = random.nextInt(6);

            try {
                switch (scenario) {
                    case 0:
                        // Add new product
                        addRandomProduct(repository, user);
                        break;
                    case 1:
                        // View products
                        System.out.println(user.getName() + " viewing all products");
                        repository.displayProducts();
                        break;
                    case 2:
                        // Fetch random product
                        int randomId = random.nextInt(10) + 1;
                        System.out.println(user.getName() + " fetching product with ID: " + randomId);
                        try {
                            repository.getProductById(randomId);
                        } catch (Exception e) {
                            System.out.println("Product not found: " + e.getMessage());
                        }
                        break;
                    case 3:
                        // Update random product
                        updateRandomProduct(repository, user);
                        break;
                    case 4:
                        // Delete random product
                        deleteRandomProduct(repository, user);
                        break;
                    case 5:
                        // Add multiple products in succession
                        addMultipleProducts(repository, user);
                        break;
                }

                // Simulate some thinking time between operations

            } catch (Exception e) {
                System.out.println("Error in scenario: " + e.getMessage());
            }
        }
    }

    private static void addRandomProduct(ProductRepository repository, User user) {
        int id = random.nextInt(100) + 1;
        String[] products = {"Apple", "Orange", "Banana", "Cereal", "Coffee", "Tea", "Sugar", "Salt", "Pepper", "Pasta"};
        String name = products[random.nextInt(products.length)];
        double price = 0.99 + random.nextDouble() * 9.0;
        LocalDate expiryDate = LocalDate.now().plusDays(random.nextInt(90) + 1);

        try {
            Product product = new Product(id, name, price, expiryDate);
            repository.addProduct(product);
            System.out.println(user.getName() + " added product: " + name);
        } catch (IllegalArgumentException e) {
            System.out.println("Failed to add product: " + e.getMessage());
        }
    }

    private static void updateRandomProduct(ProductRepository repository, User user) {
        int id = random.nextInt(10) + 1;
        double newPrice = 0.99 + random.nextDouble() * 9.0;
        LocalDate newDate = LocalDate.now().plusDays(random.nextInt(90) + 1);

        try {
            repository.updateProduct(id, newPrice, newDate);
            System.out.println(user.getName() + " updated product ID: " + id);
        } catch (Exception e) {
            System.out.println("Failed to update product: " + e.getMessage());
        }
    }

    private static void deleteRandomProduct(ProductRepository repository, User user) {
        int id = random.nextInt(10) + 1;
        try {
            repository.deleteProduct(id);
            System.out.println(user.getName() + " deleted product ID: " + id);
        } catch (Exception e) {
            System.out.println("Failed to delete product: " + e.getMessage());
        }
    }

    private static void addMultipleProducts(ProductRepository repository, User user) {
        int numProducts = random.nextInt(3) + 2; // Add 2-4 products at once
        for (int i = 0; i < numProducts; i++) {
            addRandomProduct(repository, user);
        }
    }
}