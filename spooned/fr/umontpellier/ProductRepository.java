package fr.umontpellier;
public class ProductRepository {
    private java.util.Map<java.lang.Integer, fr.umontpellier.Product> products = new java.util.HashMap<>();

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(fr.umontpellier.ProductRepository.class);

    public void addProduct(fr.umontpellier.Product product) throws java.lang.IllegalArgumentException {
        org.apache.logging.log4j.LogManager.getLogger(ProductRepository.class).info(String.format("{\"timestamp\": \"%s\", \"phase\": \"START\", \"class\": \"ProductRepository\", \"method\": \"addProduct\", \"parameters\": {%s}, \"operation_type\": \"%s\"}", java.time.LocalDateTime.now(), "\"product\": \"" + product + "\"", "WRITE");
        fr.umontpellier.ProductRepository.logger.info("Executing method: addProduct");
        if (products.containsKey(product.getId())) {
            fr.umontpellier.ProductRepository.logger.error("Failed to add product: {}", new java.lang.IllegalArgumentException(("Product with ID " + product.getId()) + " already exists."));
        }
        fr.umontpellier.ProductRepository.logger.info("Product added successfully: {}", product.getName());
        products.put(product.getId(), product);
        org.apache.logging.log4j.LogManager.getLogger(ProductRepository.class).info(String.format("{\"timestamp\": \"%s\", \"phase\": \"END\", \"class\": \"ProductRepository\", \"method\": \"addProduct\", \"parameters\": {%s}, \"operation_type\": \"%s\"}", java.time.LocalDateTime.now(), "\"product\": \"" + product + "\"", "WRITE");
    }

    public fr.umontpellier.Product getProductById(int id) throws java.util.NoSuchElementException {
        org.apache.logging.log4j.LogManager.getLogger(ProductRepository.class).info(String.format("{\"timestamp\": \"%s\", \"phase\": \"START\", \"class\": \"ProductRepository\", \"method\": \"getProductById\", \"parameters\": {%s}, \"operation_type\": \"%s\"}", java.time.LocalDateTime.now(), "\"id\": \"" + id + "\"", "READ");
        fr.umontpellier.ProductRepository.logger.info("Executing method: getProductById");
        if (!products.containsKey(id)) {
            fr.umontpellier.ProductRepository.logger.warn("No product found with ID {}", id);
            throw new java.util.NoSuchElementException("No product found with ID " + id);
        }
        fr.umontpellier.ProductRepository.logger.info("Product found: {}", products.get(id).getName());
        return products.get(id);
        org.apache.logging.log4j.LogManager.getLogger(ProductRepository.class).info(String.format("{\"timestamp\": \"%s\", \"phase\": \"END\", \"class\": \"ProductRepository\", \"method\": \"getProductById\", \"parameters\": {%s}, \"operation_type\": \"%s\"}", java.time.LocalDateTime.now(), "\"id\": \"" + id + "\"", "READ");
    }

    public void updateProduct(int id, double newPrice, java.time.LocalDate newExpirationDate) throws java.util.NoSuchElementException {
        org.apache.logging.log4j.LogManager.getLogger(ProductRepository.class).info(String.format("{\"timestamp\": \"%s\", \"phase\": \"START\", \"class\": \"ProductRepository\", \"method\": \"updateProduct\", \"parameters\": {%s}, \"operation_type\": \"%s\"}", java.time.LocalDateTime.now(), "\"id\": \"" + id + "\", \"newPrice\": \"" + newPrice + "\", \"newExpirationDate\": \"" + newExpirationDate + "\"", "WRITE");
        fr.umontpellier.ProductRepository.logger.info("Executing method: updateProduct");
        fr.umontpellier.Product product = getProductById(id);
        product.setPrice(newPrice);
        product.setExpirationDate(newExpirationDate);
        org.apache.logging.log4j.LogManager.getLogger(ProductRepository.class).info(String.format("{\"timestamp\": \"%s\", \"phase\": \"END\", \"class\": \"ProductRepository\", \"method\": \"updateProduct\", \"parameters\": {%s}, \"operation_type\": \"%s\"}", java.time.LocalDateTime.now(), "\"id\": \"" + id + "\", \"newPrice\": \"" + newPrice + "\", \"newExpirationDate\": \"" + newExpirationDate + "\"", "WRITE");
    }

    public void deleteProduct(int id) throws java.util.NoSuchElementException {
        org.apache.logging.log4j.LogManager.getLogger(ProductRepository.class).info(String.format("{\"timestamp\": \"%s\", \"phase\": \"START\", \"class\": \"ProductRepository\", \"method\": \"deleteProduct\", \"parameters\": {%s}, \"operation_type\": \"%s\"}", java.time.LocalDateTime.now(), "\"id\": \"" + id + "\"", "WRITE");
        fr.umontpellier.ProductRepository.logger.info("Executing method: deleteProduct");
        if (!products.containsKey(id)) {
            throw new java.util.NoSuchElementException("No product found with ID " + id);
        }
        products.remove(id);
        org.apache.logging.log4j.LogManager.getLogger(ProductRepository.class).info(String.format("{\"timestamp\": \"%s\", \"phase\": \"END\", \"class\": \"ProductRepository\", \"method\": \"deleteProduct\", \"parameters\": {%s}, \"operation_type\": \"%s\"}", java.time.LocalDateTime.now(), "\"id\": \"" + id + "\"", "WRITE");
    }

    public void displayProducts() {
        org.apache.logging.log4j.LogManager.getLogger(ProductRepository.class).info(String.format("{\"timestamp\": \"%s\", \"phase\": \"START\", \"class\": \"ProductRepository\", \"method\": \"displayProducts\", \"parameters\": {%s}, \"operation_type\": \"%s\"}", java.time.LocalDateTime.now(), "", "READ");
        fr.umontpellier.ProductRepository.logger.info("Executing method: displayProducts");
        fr.umontpellier.ProductRepository.logger.debug("Displaying all products");
        if (products.isEmpty()) {
            java.lang.System.out.println("No products available.");
        } else {
            products.values().forEach(product -> {
                java.lang.System.out.println((((((("ID: " + product.getId()) + ", Name: ") + product.getName()) + ", Price: ") + product.getPrice()) + ", Expiration Date: ") + product.getExpirationDate());
            });
        }
        org.apache.logging.log4j.LogManager.getLogger(ProductRepository.class).info(String.format("{\"timestamp\": \"%s\", \"phase\": \"END\", \"class\": \"ProductRepository\", \"method\": \"displayProducts\", \"parameters\": {%s}, \"operation_type\": \"%s\"}", java.time.LocalDateTime.now(), "", "READ");
    }
}