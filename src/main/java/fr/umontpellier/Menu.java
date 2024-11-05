package fr.umontpellier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Menu {
    private static final Logger logger = LogManager.getLogger(Menu.class);
    private ProductRepository repository;

    public Menu(ProductRepository repository) {
        this.repository = repository;
    }

    public void start() {
        logger.info("Menu started");
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

            logger.info("User selected option: {}", choice);

            switch (choice) {
                case 1:
                    repository.displayProducts();
                    break;
                case 2:
                    logger.debug("User chose to fetch a product by ID");
                    System.out.print("Enter product ID: ");
                    int fetchId = scanner.nextInt();
                    try {
                        Product product = repository.getProductById(fetchId);
                        System.out.println("Product found: " + product.getName() + " - Price: " + product.getPrice() + " - Expiry: " + product.getExpirationDate());
                    } catch (NoSuchElementException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 3:
                    logger.debug("User chose to add a new product");
                    System.out.print("Enter product ID: ");
                    int id = scanner.nextInt();
                    System.out.print("Enter product name: ");
                    String name = scanner.next();
                    System.out.print("Enter product price: ");
                    double price = scanner.nextDouble();
                    System.out.print("Enter product expiration date (YYYY-MM-DD): ");
                    String date = scanner.next();
                    try {
                        repository.addProduct(new Product(id, name, price, LocalDate.parse(date)));
                        System.out.println("Product added successfully.");
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 4:
                    System.out.print("Enter product ID to delete: ");
                    int deleteId = scanner.nextInt();
                    try {
                        repository.deleteProduct(deleteId);
                        System.out.println("Product deleted successfully.");
                    } catch (NoSuchElementException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 5:
                    System.out.print("Enter product ID to update: ");
                    int updateId = scanner.nextInt();
                    System.out.print("Enter new price: ");
                    double newPrice = scanner.nextDouble();
                    System.out.print("Enter new expiration date (YYYY-MM-DD): ");
                    String newDate = scanner.next();
                    try {
                        repository.updateProduct(updateId, newPrice, LocalDate.parse(newDate));
                        System.out.println("Product updated successfully.");
                    } catch (NoSuchElementException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 6:
                    System.out.println("Exiting program.");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
