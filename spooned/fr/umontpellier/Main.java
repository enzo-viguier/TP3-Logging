package fr.umontpellier;
public class Main {
    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(fr.umontpellier.Main.class);

    public static void main(java.lang.String[] args) throws java.io.IOException {
        spoon.Launcher launcher = new spoon.Launcher();
        launcher.addProcessor(new fr.umontpellier.spoon.LoggingProcessor());
        launcher.addInputResource("./src/main/java");
        launcher.run();
        fr.umontpellier.spoon.LogParser parser = new fr.umontpellier.spoon.LogParser();
        parser.parseLogs("./logs/app.log");
        parser.generateProfiles("./profiles");
        java.lang.System.out.println("Profiles generated successfully!");// 

        // ProductRepository repository = new ProductRepository();
        // 
        // TestScenarios.main(new String[]{});
        // 
        // Menu menu = new Menu(repository);
        // menu.start();
    }
}