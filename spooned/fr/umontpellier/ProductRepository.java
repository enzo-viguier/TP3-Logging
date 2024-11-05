package fr.umontpellier;
public class ProductRepository {
    private java.util.Map<java.lang.Integer, fr.umontpellier.Product> products = new java.util.HashMap<>();

    public void addProduct(fr.umontpellier.Product product) throws java.lang.IllegalArgumentException {
        info("Executing method: addProduct");
        if (products.containsKey(product.getId())) {
            throw new java.lang.IllegalArgumentException(("Product with ID " + product.getId()) + " already exists.");
        }
        products.put(product.getId(), product);
    }

    public fr.umontpellier.Product getProductById(int id) throws java.util.NoSuchElementException {
        info("Executing method: getProductById");
        if (!products.containsKey(id)) {
            throw new java.util.NoSuchElementException("No product found with ID " + id);
        }
        return products.get(id);
    }

    public void updateProduct(int id, double newPrice, java.time.LocalDate newExpirationDate) throws java.util.NoSuchElementException {
        info("Executing method: updateProduct");
        fr.umontpellier.Product product = getProductById(id);
        product.setPrice(newPrice);
        product.setExpirationDate(newExpirationDate);
    }

    public void deleteProduct(int id) throws java.util.NoSuchElementException {
        info("Executing method: deleteProduct");
        if (!products.containsKey(id)) {
            throw new java.util.NoSuchElementException("No product found with ID " + id);
        }
        products.remove(id);
    }

    public void displayProducts() {
        info("Executing method: displayProducts");
        if (products.isEmpty()) {
            java.lang.System.out.println("No products available.");
        } else {
            products.values().forEach(product -> {
                java.lang.System.out.println((((((("ID: " + product.getId()) + ", Name: ") + product.getName()) + ", Price: ") + product.getPrice()) + ", Expiration Date: ") + product.getExpirationDate());
            });
        }
    }
}