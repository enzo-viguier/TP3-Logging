package fr.umontpellier;
public class Product {
    private final int id;

    private java.lang.String name;

    private double price;

    private java.time.LocalDate expirationDate;

    public Product(int id, java.lang.String name, double price, java.time.LocalDate expirationDate) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.expirationDate = expirationDate;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public java.lang.String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public java.time.LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setExpirationDate(java.time.LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }
}