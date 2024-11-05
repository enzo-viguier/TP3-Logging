package fr.umontpellier.spoon;

import fr.umontpellier.ProductRepository;
import spoon.Launcher;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtExecutableReference;
import spoon.reflect.reference.CtTypeReference;
import spoon.support.reflect.code.CtInvocationImpl;

public class LoggerInjector {

    public static void main(String[] args) {
        // Initialisation de Spoon
        Launcher launcher = new Launcher();
        launcher.addInputResource("src/main/java"); // Indique le répertoire source de ton projet
        launcher.buildModel();

        // Obtenir la classe ProductRepository
        Factory factory = launcher.getFactory();
        CtClass<?> productRepositoryClass = factory.Class().get(ProductRepository.class);

        // Ajouter un logger à chaque méthode de ProductRepository
        productRepositoryClass.getMethods().forEach(method -> addLogging(method, factory));

        // Générer le code transformé
        launcher.setSourceOutputDirectory("spooned"); // Dossier où Spoon sauvegardera les fichiers modifiés
        launcher.prettyprint();
    }

    private static void addLogging(CtMethod<?> method, Factory factory) {
        // Crée un logger pour chaque méthode
        CtTypeReference<org.apache.logging.log4j.Logger> loggerType = factory.createCtTypeReference(org.apache.logging.log4j.Logger.class);
        CtInvocation<Void> logInfo = factory.createInvocation();

        CtExecutableReference<Void> infoMethod = factory.createExecutableReference();
        infoMethod.setDeclaringType(loggerType);
        infoMethod.setSimpleName("info");
        infoMethod.setType(factory.Type().voidPrimitiveType());

        logInfo.setExecutable(infoMethod);
        logInfo.addArgument(factory.createLiteral("Executing method: " + method.getSimpleName()));

        // Injecter le log au début de la méthode
        method.getBody().insertBegin(logInfo);
    }
}
