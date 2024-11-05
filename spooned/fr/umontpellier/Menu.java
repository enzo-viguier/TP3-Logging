package fr.umontpellier;
public class Menu {
    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(fr.umontpellier.Menu.class);

    private fr.umontpellier.ProductRepository repository;

    public Menu(fr.umontpellier.ProductRepository repository) {
        this.repository = repository;
    }

    public void start() {
        fr.umontpellier.Menu.logger.info("Menu started");
        java.util.Scanner scanner = new java.util.Scanner(java.lang.System.in);
        while (true) {
            java.lang.System.out.println("\n1. Display products");
            java.lang.System.out.println("2. Fetch product by ID");
            java.lang.System.out.println("3. Add product");
            java.lang.System.out.println("4. Delete product");
            java.lang.System.out.println("5. Update product");
            java.lang.System.out.println("6. Exit");
            java.lang.System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            fr.umontpellier.Menu.logger.info("User selected option: {}", choice);
            switch (choice) {
                case 1 :
                    fr.umontpellier.Menu.logger.debug("Displaying all products");
                    repository.displayProducts();
                    break;
                case 2 :
                    fr.umontpellier.Menu.logger.debug("User chose to fetch a product by ID");
                    java.lang.System.out.print("Enter product ID: ");
                    int fetchId = scanner.nextInt();
                    try {
                        fr.umontpellier.Product product = repository.getProductById(fetchId);
                        fr.umontpellier.Menu.logger.info("Product found: {}", product.getName());
                        java.lang.System.out.println((((("Product found: " + product.getName()) + " - Price: ") + product.getPrice()) + " - Expiry: ") + product.getExpirationDate());
                    } catch (java.util.NoSuchElementException e) {
                        fr.umontpellier.Menu.logger.warn("No product found with ID {}", fetchId);
                        java.lang.System.out.println(e.getMessage());
                    }
                    break;
                case 3 :
                    fr.umontpellier.Menu.logger.debug("User chose to add a new product");
                    java.lang.System.out.print("Enter product ID: ");
                    int id = scanner.nextInt();
                    java.lang.System.out.print("Enter product name: ");
                    java.lang.String name = scanner.next();
                    java.lang.System.out.print("Enter product price: ");
                    double price = scanner.nextDouble();
                    java.lang.System.out.print("Enter product expiration date (YYYY-MM-DD): ");
                    java.lang.String date = scanner.next();
                    try {
                        repository.addProduct(new fr.umontpellier.Product(id, name, price, java.time.LocalDate.parse(date)));
                        fr.umontpellier.Menu.logger.info("Product added successfully: {}", name);
                        java.lang.System.out.println("Product added successfully.");
                    } catch (java.lang.IllegalArgumentException e) {
                        fr.umontpellier.Menu.logger.error("Failed to add product: {}", e.getMessage());
                        java.lang.System.out.println(e.getMessage());
                    }
                    break;
                case 4 :
                    java.lang.System.out.print("Enter product ID to delete: ");
                    int deleteId = scanner.nextInt();
                    try {
                        repository.deleteProduct(deleteId);
                        java.lang.System.out.println("Product deleted successfully.");
                    } catch (java.util.NoSuchElementException e) {
                        java.lang.System.out.println(e.getMessage());
                    }
                    break;
                case 5 :
                    java.lang.System.out.print("Enter product ID to update: ");
                    int updateId = scanner.nextInt();
                    java.lang.System.out.print("Enter new price: ");
                    double newPrice = scanner.nextDouble();
                    java.lang.System.out.print("Enter new expiration date (YYYY-MM-DD): ");
                    java.lang.String newDate = scanner.next();
                    try {
                        repository.updateProduct(updateId, newPrice, java.time.LocalDate.parse(newDate));
                        java.lang.System.out.println("Product updated successfully.");
                    } catch (java.util.NoSuchElementException e) {
                        java.lang.System.out.println(e.getMessage());
                    }
                    break;
                case 6 :
                    java.lang.System.out.println("Exiting program.");
                    return;
                default :
                    java.lang.System.out.println("Invalid choice. Please try again.");
            }
        } 
    }
}