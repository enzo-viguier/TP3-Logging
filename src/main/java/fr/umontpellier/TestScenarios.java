package fr.umontpellier;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TestScenarios {
    private static final Random random = new Random();

    // Test users with different profiles and their behavior weights
    private static final List<TestUser> users = Arrays.asList(
            // Readers (more likely to perform read operations)
            new TestUser(new User(1, "John_Analyst", 45, "john@company.com", "pass123"),
                    new BehaviorProfile(0.7, 0.2, 0.1)), // 70% read, 20% write, 10% premium searches
            new TestUser(new User(2, "Alice_Researcher", 32, "alice@company.com", "pass456"),
                    new BehaviorProfile(0.8, 0.1, 0.1)),
            new TestUser(new User(3, "Bob_Viewer", 25, "bob@company.com", "pass789"),
                    new BehaviorProfile(0.75, 0.15, 0.1)),

            // Writers (more likely to perform write operations)
            new TestUser(new User(4, "Emma_Manager", 38, "emma@company.com", "pass321"),
                    new BehaviorProfile(0.2, 0.7, 0.1)),
            new TestUser(new User(5, "Mike_Inventory", 22, "mike@company.com", "pass654"),
                    new BehaviorProfile(0.1, 0.8, 0.1)),
            new TestUser(new User(6, "Sarah_Stock", 41, "sarah@company.com", "pass987"),
                    new BehaviorProfile(0.15, 0.75, 0.1)),

            // Premium searchers (more likely to search expensive products)
            new TestUser(new User(7, "David_Luxury", 29, "david@company.com", "pass147"),
                    new BehaviorProfile(0.2, 0.2, 0.6)),
            new TestUser(new User(8, "Lisa_Premium", 35, "lisa@company.com", "pass258"),
                    new BehaviorProfile(0.15, 0.15, 0.7)),
            new TestUser(new User(9, "Tom_HighEnd", 23, "tom@company.com", "pass369"),
                    new BehaviorProfile(0.1, 0.2, 0.7)),
            new TestUser(new User(10, "Anna_Executive", 48, "anna@company.com", "pass741"),
                    new BehaviorProfile(0.2, 0.1, 0.7))
    );

    private static class TestUser {
        User user;
        BehaviorProfile profile;

        TestUser(User user, BehaviorProfile profile) {
            this.user = user;
            this.profile = profile;
        }
    }

    private static class BehaviorProfile {
        double readProbability;
        double writeProbability;
        double premiumSearchProbability;

        BehaviorProfile(double read, double write, double premium) {
            this.readProbability = read;
            this.writeProbability = write;
            this.premiumSearchProbability = premium;
        }
    }

    private static final List<Product> premiumProducts = Arrays.asList(
            new Product(1, "Premium Coffee", 29.99, LocalDate.now().plusDays(90)),
            new Product(2, "Organic Truffles", 89.99, LocalDate.now().plusDays(30)),
            new Product(3, "Aged Wine", 199.99, LocalDate.now().plusDays(365)),
            new Product(4, "Luxury Chocolate", 49.99, LocalDate.now().plusDays(60)),
            new Product(5, "Caviar", 299.99, LocalDate.now().plusDays(45))
    );

    private static final List<Product> regularProducts = Arrays.asList(
            new Product(6, "Milk", 2.99, LocalDate.now().plusDays(14)),
            new Product(7, "Bread", 1.99, LocalDate.now().plusDays(7)),
            new Product(8, "Cheese", 4.99, LocalDate.now().plusDays(30)),
            new Product(9, "Yogurt", 3.49, LocalDate.now().plusDays(21)),
            new Product(10, "Eggs", 2.49, LocalDate.now().plusDays(28))
    );

    public static void main(String[] args) {
        ProductRepository repository = new ProductRepository();

        // Add some initial products
        premiumProducts.forEach(p -> {
            try {
                repository.addProduct(p);
            } catch (IllegalArgumentException e) {
                // Ignore duplicates
            }
        });

        regularProducts.forEach(p -> {
            try {
                repository.addProduct(p);
            } catch (IllegalArgumentException e) {
                // Ignore duplicates
            }
        });

        // Execute scenarios for each user
        for (TestUser testUser : users) {
            System.out.println("\nExecuting scenarios for user: " + testUser.user.getName());
            executeUserScenarios(repository, testUser);
        }
    }

    private static void executeUserScenarios(ProductRepository repository, TestUser testUser) {
        for (int i = 0; i < 20; i++) {
            try {
                double action = random.nextDouble();

                // Determine action based on user's behavior profile
                if (action < testUser.profile.readProbability) {
                    executeReadOperation(repository, testUser);
                } else if (action < testUser.profile.readProbability + testUser.profile.writeProbability) {
                    executeWriteOperation(repository, testUser);
                } else {
                    executePremiumSearch(repository, testUser);
                }

            } catch (Exception e) {
                System.out.println("Error in scenario: " + e.getMessage());
            }
        }
    }

    private static void executeReadOperation(ProductRepository repository, TestUser testUser) {
        int subOperation = random.nextInt(2);
        switch (subOperation) {
            case 0:
                System.out.println(testUser.user.getName() + " viewing all products");
                repository.displayProducts();
                break;
            case 1:
                int randomId = random.nextInt(10) + 1;
                System.out.println(testUser.user.getName() + " fetching product with ID: " + randomId);
                try {
                    repository.getProductById(randomId);
                } catch (Exception e) {
                    System.out.println("Product not found: " + e.getMessage());
                }
                break;
        }
    }

    private static void executeWriteOperation(ProductRepository repository, TestUser testUser) {
        int subOperation = random.nextInt(3);
        switch (subOperation) {
            case 0:
                addRandomProduct(repository, testUser.user);
                break;
            case 1:
                updateRandomProduct(repository, testUser.user);
                break;
            case 2:
                deleteRandomProduct(repository, testUser.user);
                break;
        }
    }

    private static void executePremiumSearch(ProductRepository repository, TestUser testUser) {
        // Premium users tend to look at expensive products
        int productId = random.nextInt(5) + 1; // IDs 1-5 are premium products
        System.out.println(testUser.user.getName() + " searching premium product with ID: " + productId);
        try {
            Product product = repository.getProductById(productId);
            System.out.println(testUser.user.getName() + " viewed premium product: " + product.getName() +
                    " (Price: " + product.getPrice() + ")");
        } catch (Exception e) {
            System.out.println("Premium product not found: " + e.getMessage());
        }
    }

    private static void addRandomProduct(ProductRepository repository, User user) {
        int id = random.nextInt(100) + 11; // Avoid conflicting with initial products
        String[] products = {"Apple", "Orange", "Banana", "Cereal", "Coffee", "Tea", "Sugar", "Salt", "Pepper", "Pasta"};
        String name = products[random.nextInt(products.length)];
        double price = 0.99 + random.nextDouble() * 9.0;
        LocalDate expiryDate = LocalDate.now().plusDays(random.nextInt(90) + 1);

        try {
            Product product = new Product(id, name, price, expiryDate);
            repository.addProduct(product);
            System.out.println(user.getName() + " added product: " + name + " (Price: " + price + ")");
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
            System.out.println(user.getName() + " updated product ID: " + id + " (New Price: " + newPrice + ")");
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
}