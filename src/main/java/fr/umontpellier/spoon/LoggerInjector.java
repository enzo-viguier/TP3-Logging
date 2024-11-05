package fr.umontpellier.spoon;

import spoon.Launcher;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtExecutableReference;
import spoon.reflect.reference.CtTypeReference;

import java.util.Set;

public class LoggerInjector {

    public static void main(String[] args) {
        // Initialisation de Spoon
        Launcher launcher = new Launcher();
        launcher.addInputResource("src/main/java"); // Indique le répertoire source de ton projet
        launcher.buildModel();

        // Obtenir le modèle de toutes les classes dans le package fr.umontpellier
        Factory factory = launcher.getFactory();
        factory.Package().get("fr.umontpellier").getTypes().forEach(type -> {
            if (type instanceof CtClass) {
                CtClass<?> ctClass = (CtClass<?>) type;

                // Ajouter un logger si la classe n'en a pas
                addLoggerIfAbsent(ctClass, factory);

                // Ajouter des logs dans chaque méthode
                ctClass.getMethods().forEach(method -> addLogging(method, factory));
            }
        });

        // Générer le code transformé
        launcher.setSourceOutputDirectory("spooned"); // Dossier où Spoon sauvegardera les fichiers modifiés
        launcher.prettyprint();
    }

    private static void addLoggerIfAbsent(CtClass<?> ctClass, Factory factory) {
        // Vérifie si la classe possède déjà un logger
        boolean hasLogger = ctClass.getFields().stream()
                .anyMatch(field -> field.getType().getSimpleName().equals("Logger"));

        // Si la classe n'a pas de logger, en ajoute un
        if (!hasLogger) {
            CtTypeReference<org.apache.logging.log4j.Logger> loggerType = factory.createCtTypeReference(org.apache.logging.log4j.Logger.class);
            CtField<org.apache.logging.log4j.Logger> loggerField = factory.createField(
                    ctClass,
                    Set.of(ModifierKind.PRIVATE, ModifierKind.STATIC, ModifierKind.FINAL),
                    loggerType,
                    "logger",
                    factory.Code().createCodeSnippetExpression("LogManager.getLogger(" + ctClass.getSimpleName() + ".class)")
            );
            ctClass.addField(loggerField);
        }
    }

    private static void addLogging(CtMethod<?> method, Factory factory) {
        // Crée une invocation de log "logger.info(...)"
        CtTypeReference<org.apache.logging.log4j.Logger> loggerType = factory.createCtTypeReference(org.apache.logging.log4j.Logger.class);

        CtExecutableReference<Void> infoMethod = factory.createExecutableReference();
        CtInvocation<Void> logInfo = factory.createInvocation(
                factory.createVariableRead(factory.createFieldReference().setSimpleName("logger"), false),
                infoMethod,
                factory.createLiteral("Executing method: " + method.getSimpleName())
        );
        infoMethod.setDeclaringType(loggerType);
        infoMethod.setSimpleName("info");
        infoMethod.setType(factory.Type().VOID_PRIMITIVE);

        // Injecter le log au début de la méthode
        method.getBody().insertBegin(logInfo);
    }
}
