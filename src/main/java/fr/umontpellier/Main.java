package fr.umontpellier;

public class Main {
    public static void main(String[] args) {
        ProductRepository repository = new ProductRepository();
        Menu menu = new Menu(repository);
        menu.start();
    }
}
