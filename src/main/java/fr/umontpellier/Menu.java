package fr.umontpellier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Menu {
    private static final Logger logger = LogManager.getLogger(Menu.class);
    private ProductRepository repository;
    private User currentUser; // Ajout de l'utilisateur courant

    public Menu(ProductRepository repository) {
        this.repository = repository;
    }

    // Nouvelle méthode pour définir l'utilisateur courant
    public void setCurrentUser(User user) {
        this.currentUser = user;
        logger.info("User {} logged in", user.getName());
    }

    public void start() {
        if (currentUser == null) {
            logger.error("No user set for this session");
            return;
        }

        logger.info("Menu started for user {}", currentUser.getName());
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n1. Display products");
            System.out.println("2. Fetch product by ID");
            System.out.println("3. Add product");
            System.out.println("4. Delete product");
            System.out.println("5. Update product");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            logger.info("User {} selected option: {}", currentUser.getName(), choice);

            switch (choice) {
                case 1:
                    logger.debug("User {} viewing all products", currentUser.getName());
                    repository.displayProducts();
                    break;
                case 2:
                    logger.debug("User {} attempting to fetch a product", currentUser.getName());
                    System.out.print("Enter product ID: ");
                    int fetchId = scanner.nextInt();
                    try {
                        Product product = repository.getProductById(fetchId);
                        logger.info("User {} fetched product: {} (Price: {})",
                                currentUser.getName(), product.getName(), product.getPrice());
                    } catch (NoSuchElementException e) {
                        logger.warn("User {} - No product found with ID {}", currentUser.getName(), fetchId);
                        System.out.println(e.getMessage());
                    }
                    break;
                case 3:
                    logger.debug("User {} attempting to add a new product", currentUser.getName());
                    System.out.print("Enter product ID: ");
                    int id = scanner.nextInt();
                    System.out.print("Enter product name: ");
                    String name = scanner.next();
                    System.out.print("Enter product price: ");
                    double price = scanner.nextDouble();
                    System.out.print("Enter product expiration date (YYYY-MM-DD): ");
                    String date = scanner.next();
                    try {
                        Product newProduct = new Product(id, name, price, LocalDate.parse(date));
                        repository.addProduct(newProduct);
                        logger.info("User {} added product: {} (Price: {})",
                                currentUser.getName(), name, price);
                    } catch (IllegalArgumentException e) {
                        logger.error("User {} failed to add product: {}", currentUser.getName(), e.getMessage());
                        System.out.println(e.getMessage());
                    }
                    break;
                case 4:
                    logger.debug("User {} attempting to delete a product", currentUser.getName());
                    System.out.print("Enter product ID to delete: ");
                    int deleteId = scanner.nextInt();
                    try {
                        repository.deleteProduct(deleteId);
                        logger.info("User {} deleted product with ID: {}", currentUser.getName(), deleteId);
                    } catch (NoSuchElementException e) {
                        logger.warn("User {} failed to delete product: {}", currentUser.getName(), e.getMessage());
                        System.out.println(e.getMessage());
                    }
                    break;
                case 5:
                    logger.debug("User {} attempting to update a product", currentUser.getName());
                    System.out.print("Enter product ID to update: ");
                    int updateId = scanner.nextInt();
                    System.out.print("Enter new price: ");
                    double newPrice = scanner.nextDouble();
                    System.out.print("Enter new expiration date (YYYY-MM-DD): ");
                    String newDate = scanner.next();
                    try {
                        repository.updateProduct(updateId, newPrice, LocalDate.parse(newDate));
                        logger.info("User {} updated product ID: {} (New Price: {})",
                                currentUser.getName(), updateId, newPrice);
                    } catch (NoSuchElementException e) {
                        logger.warn("User {} failed to update product: {}", currentUser.getName(), e.getMessage());
                        System.out.println(e.getMessage());
                    }
                    break;
                case 6:
                    logger.info("User {} logged out", currentUser.getName());
                    return;
                default:
                    logger.warn("User {} made invalid choice: {}", currentUser.getName(), choice);
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}