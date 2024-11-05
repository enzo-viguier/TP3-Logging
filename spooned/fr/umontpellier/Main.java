package fr.umontpellier;
public class Main {
    public static void main(java.lang.String[] args) {
        fr.umontpellier.ProductRepository repository = new fr.umontpellier.ProductRepository();
        fr.umontpellier.Menu menu = new fr.umontpellier.Menu(repository);
        menu.start();
    }
}