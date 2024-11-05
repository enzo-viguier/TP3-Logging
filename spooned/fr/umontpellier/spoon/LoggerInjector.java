package fr.umontpellier.spoon;
public class LoggerInjector {
    public static void main(java.lang.String[] args) {
        // Initialisation de Spoon
        spoon.Launcher launcher = new spoon.Launcher();
        launcher.addInputResource("src/main/java");// Indique le répertoire source de ton projet

        launcher.buildModel();
        // Obtenir la classe ProductRepository
        spoon.reflect.factory.Factory factory = launcher.getFactory();
        spoon.reflect.declaration.CtClass<?> productRepositoryClass = factory.Class().get(fr.umontpellier.ProductRepository.class);
        // Ajouter un logger à chaque méthode de ProductRepository
        productRepositoryClass.getMethods().forEach(method -> fr.umontpellier.spoon.LoggerInjector.addLogging(method, factory));
        // Générer le code transformé
        launcher.setSourceOutputDirectory("spooned");// Dossier où Spoon sauvegardera les fichiers modifiés

        launcher.prettyprint();
    }

    private static void addLogging(spoon.reflect.declaration.CtMethod<?> method, spoon.reflect.factory.Factory factory) {
        // Crée un logger pour chaque méthode
        spoon.reflect.reference.CtTypeReference<org.apache.logging.log4j.Logger> loggerType = factory.createCtTypeReference(org.apache.logging.log4j.Logger.class);
        spoon.reflect.code.CtInvocation<java.lang.Void> logInfo = factory.createInvocation();
        spoon.reflect.reference.CtExecutableReference<java.lang.Void> infoMethod = factory.createExecutableReference();
        infoMethod.setDeclaringType(loggerType);
        infoMethod.setSimpleName("info");
        infoMethod.setType(factory.Type().voidPrimitiveType());
        logInfo.setExecutable(infoMethod);
        logInfo.addArgument(factory.createLiteral("Executing method: " + method.getSimpleName()));
        // Injecter le log au début de la méthode
        method.getBody().insertBegin(logInfo);
    }
}