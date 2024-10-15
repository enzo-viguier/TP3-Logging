package fr.umontpellier;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        ProductRepository repository = new ProductRepository();
        Menu menu = new Menu(repository);
        menu.start();
    }
}
