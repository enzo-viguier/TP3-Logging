package fr.umontpellier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class ProductRepository {
    private java.util.Map<java.lang.Integer, fr.umontpellier.Product> products = new java.util.HashMap<>();

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(fr.umontpellier.ProductRepository.class);

    public void addProduct(fr.umontpellier.Product product) throws java.lang.IllegalArgumentException {
        logger.info("Executing method: addProduct");
        if (products.containsKey(product.getId())) {
            fr.umontpellier.ProductRepository.logger.error("Failed to add product: {}", new java.lang.IllegalArgumentException(("Product with ID " + product.getId()) + " already exists."));
        }
        fr.umontpellier.ProductRepository.logger.info("Product added successfully: {}", product.getName());
        products.put(product.getId(), product);
    }

    public fr.umontpellier.Product getProductById(int id) throws java.util.NoSuchElementException {
        logger.info("Executing method: getProductById");
        if (!products.containsKey(id)) {
            fr.umontpellier.ProductRepository.logger.warn("No product found with ID {}", id);
            throw new java.util.NoSuchElementException("No product found with ID " + id);
        }
        fr.umontpellier.ProductRepository.logger.info("Product found: {}", products.get(id).getName());
        return products.get(id);
    }

    public void updateProduct(int id, double newPrice, java.time.LocalDate newExpirationDate) throws java.util.NoSuchElementException {
        logger.info("Executing method: updateProduct");
        fr.umontpellier.Product product = getProductById(id);
        product.setPrice(newPrice);
        product.setExpirationDate(newExpirationDate);
    }

    public void deleteProduct(int id) throws java.util.NoSuchElementException {
        logger.info("Executing method: deleteProduct");
        if (!products.containsKey(id)) {
            throw new java.util.NoSuchElementException("No product found with ID " + id);
        }
        products.remove(id);
    }

    public void displayProducts() {
        logger.info("Executing method: displayProducts");
        fr.umontpellier.ProductRepository.logger.debug("Displaying all products");
        if (products.isEmpty()) {
            java.lang.System.out.println("No products available.");
        } else {
            products.values().forEach(product -> {
                java.lang.System.out.println((((((("ID: " + product.getId()) + ", Name: ") + product.getName()) + ", Price: ") + product.getPrice()) + ", Expiration Date: ") + product.getExpirationDate());
            });
        }
    }
}
