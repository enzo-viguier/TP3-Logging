package fr.umontpellier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class ProductRepository {
    private Map<Integer, Product> products = new HashMap<>();
    private static final Logger logger = LogManager.getLogger(Menu.class);


    public void addProduct(Product product) throws IllegalArgumentException {
        if (products.containsKey(product.getId())) {
            logger.error("Failed to add product: {}", new IllegalArgumentException("Product with ID " + product.getId() + " already exists."));
        }
        logger.info("Product added successfully: {}", product.getName());
        products.put(product.getId(), product);
    }

    public Product getProductById(int id) throws NoSuchElementException {
        if (!products.containsKey(id)) {
            logger.warn("No product found with ID {}", id);
            throw new NoSuchElementException("No product found with ID " + id);
        }
        logger.info("Product found: {}", products.get(id).getName());
        return products.get(id);
    }

    public void updateProduct(int id, double newPrice, LocalDate newExpirationDate) throws NoSuchElementException {
        Product product = getProductById(id);
        product.setPrice(newPrice);
        product.setExpirationDate(newExpirationDate);
    }

    public void deleteProduct(int id) throws NoSuchElementException {
        if (!products.containsKey(id)) {
            throw new NoSuchElementException("No product found with ID " + id);
        }
        products.remove(id);
    }

    public void displayProducts() {
        logger.debug("Displaying all products");
        if (products.isEmpty()) {
            System.out.println("No products available.");
        } else {
            products.values().forEach(product -> {
                System.out.println("ID: " + product.getId() + ", Name: " + product.getName() +
                        ", Price: " + product.getPrice() + ", Expiration Date: " + product.getExpirationDate());
            });
        }
    }
}
