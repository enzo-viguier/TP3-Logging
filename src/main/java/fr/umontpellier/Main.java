package fr.umontpellier;

import fr.umontpellier.spoon.LogParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import spoon.Launcher;

import java.io.IOException;

public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws IOException {
        ProductRepository repository = new ProductRepository();
//        TestScenarios.main(new String[]{});

        LogParser parser = new LogParser();
        parser.parseLogs("./logs/app.log");
        parser.generateProfiles("./profiles");
        System.out.println("Profiles generated successfully!");

        Menu menu = new Menu(repository);
        User interactiveUser = new User(0, "Interactive_User", 30, "interactive@test.com", "pass123");
        menu.setCurrentUser(interactiveUser);
        menu.start();
    }
}