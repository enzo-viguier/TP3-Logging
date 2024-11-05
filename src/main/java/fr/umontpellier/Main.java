package fr.umontpellier;

import fr.umontpellier.spoon.LogParser;
import fr.umontpellier.spoon.LoggingProcessor;
import org.apache.logging.log4j.LogManager;

import org.apache.logging.log4j.Logger;
import spoon.Launcher;

import java.io.IOException;

public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws IOException {

        Launcher launcher = new Launcher();
        launcher.addProcessor(new LoggingProcessor());
        launcher.addInputResource("./src/main/java");
        launcher.run();

        LogParser parser = new LogParser();
        parser.parseLogs("./logs/app.log");
        parser.generateProfiles("./profiles");
        System.out.println("Profiles generated successfully!");//
//        ProductRepository repository = new ProductRepository();
//
//        TestScenarios.main(new String[]{});
//
//        Menu menu = new Menu(repository);
//        menu.start();
    }
}
