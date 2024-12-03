package fr.umontpellier;
public class Menu {
    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(fr.umontpellier.Menu.class);

    private fr.umontpellier.ProductRepository repository;

    private fr.umontpellier.User currentUser;// Ajout de l'utilisateur courant


    public Menu(fr.umontpellier.ProductRepository repository) {
        this.repository = repository;
    }

    // Nouvelle méthode pour définir l'utilisateur courant
    public void setCurrentUser(fr.umontpellier.User user) {
        this.currentUser = user;
        fr.umontpellier.Menu.logger.info("User {} logged in", user.getName());
    }

    public void start() {
        org.apache.logging.log4j.LogManager.getLogger(Menu.class).info(String.format("{\"timestamp\": \"%s\", \"phase\": \"START\", \"class\": \"Menu\", \"method\": \"start\", \"parameters\": {%s}, \"operation_type\": \"%s\"}", java.time.LocalDateTime.now(), "", "UNKNOWN");
        if (currentUser == null) {
            fr.umontpellier.Menu.logger.error("No user set for this session");
            return;
        }
        fr.umontpellier.Menu.logger.info("Menu started for user {}", currentUser.getName());
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
            fr.umontpellier.Menu.logger.info("User {} selected option: {}", currentUser.getName(), choice);
            switch (choice) {
                case 1 :
                    fr.umontpellier.Menu.logger.debug("User {} viewing all products", currentUser.getName());
                    repository.displayProducts();
                    break;
                case 2 :
                    fr.umontpellier.Menu.logger.debug("User {} attempting to fetch a product", currentUser.getName());
                    java.lang.System.out.print("Enter product ID: ");
                    int fetchId = scanner.nextInt();
                    try {
                        fr.umontpellier.Product product = repository.getProductById(fetchId);
                        fr.umontpellier.Menu.logger.info("User {} fetched product: {} (Price: {})", currentUser.getName(), product.getName(), product.getPrice());
                    } catch (java.util.NoSuchElementException e) {
                        fr.umontpellier.Menu.logger.warn("User {} - No product found with ID {}", currentUser.getName(), fetchId);
                        java.lang.System.out.println(e.getMessage());
                    }
                    break;
                case 3 :
                    fr.umontpellier.Menu.logger.debug("User {} attempting to add a new product", currentUser.getName());
                    java.lang.System.out.print("Enter product ID: ");
                    int id = scanner.nextInt();
                    java.lang.System.out.print("Enter product name: ");
                    java.lang.String name = scanner.next();
                    java.lang.System.out.print("Enter product price: ");
                    double price = scanner.nextDouble();
                    java.lang.System.out.print("Enter product expiration date (YYYY-MM-DD): ");
                    java.lang.String date = scanner.next();
                    try {
                        fr.umontpellier.Product newProduct = new fr.umontpellier.Product(id, name, price, java.time.LocalDate.parse(date));
                        repository.addProduct(newProduct);
                        fr.umontpellier.Menu.logger.info("User {} added product: {} (Price: {})", currentUser.getName(), name, price);
                    } catch (java.lang.IllegalArgumentException e) {
                        fr.umontpellier.Menu.logger.error("User {} failed to add product: {}", currentUser.getName(), e.getMessage());
                        java.lang.System.out.println(e.getMessage());
                    }
                    break;
                case 4 :
                    fr.umontpellier.Menu.logger.debug("User {} attempting to delete a product", currentUser.getName());
                    java.lang.System.out.print("Enter product ID to delete: ");
                    int deleteId = scanner.nextInt();
                    try {
                        repository.deleteProduct(deleteId);
                        fr.umontpellier.Menu.logger.info("User {} deleted product with ID: {}", currentUser.getName(), deleteId);
                    } catch (java.util.NoSuchElementException e) {
                        fr.umontpellier.Menu.logger.warn("User {} failed to delete product: {}", currentUser.getName(), e.getMessage());
                        java.lang.System.out.println(e.getMessage());
                    }
                    break;
                case 5 :
                    fr.umontpellier.Menu.logger.debug("User {} attempting to update a product", currentUser.getName());
                    java.lang.System.out.print("Enter product ID to update: ");
                    int updateId = scanner.nextInt();
                    java.lang.System.out.print("Enter new price: ");
                    double newPrice = scanner.nextDouble();
                    java.lang.System.out.print("Enter new expiration date (YYYY-MM-DD): ");
                    java.lang.String newDate = scanner.next();
                    try {
                        repository.updateProduct(updateId, newPrice, java.time.LocalDate.parse(newDate));
                        fr.umontpellier.Menu.logger.info("User {} updated product ID: {} (New Price: {})", currentUser.getName(), updateId, newPrice);
                    } catch (java.util.NoSuchElementException e) {
                        fr.umontpellier.Menu.logger.warn("User {} failed to update product: {}", currentUser.getName(), e.getMessage());
                        java.lang.System.out.println(e.getMessage());
                    }
                    break;
                case 6 :
                    fr.umontpellier.Menu.logger.info("User {} logged out", currentUser.getName());
                    return;
                default :
                    fr.umontpellier.Menu.logger.warn("User {} made invalid choice: {}", currentUser.getName(), choice);
                    java.lang.System.out.println("Invalid choice. Please try again.");
            }
        } 
        org.apache.logging.log4j.LogManager.getLogger(Menu.class).info(String.format("{\"timestamp\": \"%s\", \"phase\": \"END\", \"class\": \"Menu\", \"method\": \"start\", \"parameters\": {%s}, \"operation_type\": \"%s\"}", java.time.LocalDateTime.now(), "", "UNKNOWN");
    }
}