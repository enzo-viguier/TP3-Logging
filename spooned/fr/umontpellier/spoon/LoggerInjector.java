package fr.umontpellier.spoon;
public class LoggerInjector {
    public static void main(java.lang.String[] args) {
        // Initialisation de Spoon
        spoon.Launcher launcher = new spoon.Launcher();
        launcher.addInputResource("src/main/java");// Indique le répertoire source de ton projet

        launcher.buildModel();
        // Obtenir le modèle de toutes les classes dans le package fr.umontpellier
        spoon.reflect.factory.Factory factory = launcher.getFactory();
        factory.Package().get("fr.umontpellier").getTypes().forEach(type -> {
            if (type instanceof spoon.reflect.declaration.CtClass) {
                spoon.reflect.declaration.CtClass<?> ctClass = ((spoon.reflect.declaration.CtClass<?>) (type));
                // Ajouter un logger si la classe n'en a pas
                fr.umontpellier.spoon.LoggerInjector.addLoggerIfAbsent(ctClass, factory);
                // Ajouter des logs dans chaque méthode
                ctClass.getMethods().forEach(method -> fr.umontpellier.spoon.LoggerInjector.addLogging(method, factory));
            }
        });
        // Générer le code transformé
        launcher.setSourceOutputDirectory("spooned");// Dossier où Spoon sauvegardera les fichiers modifiés

        launcher.prettyprint();
    }

    private static void addLoggerIfAbsent(spoon.reflect.declaration.CtClass<?> ctClass, spoon.reflect.factory.Factory factory) {
        // Vérifie si la classe possède déjà un logger
        boolean hasLogger = ctClass.getFields().stream().anyMatch(field -> field.getType().getSimpleName().equals("Logger"));
        // Si la classe n'a pas de logger, en ajoute un
        if (!hasLogger) {
            spoon.reflect.reference.CtTypeReference<org.apache.logging.log4j.Logger> loggerType = factory.createCtTypeReference(org.apache.logging.log4j.Logger.class);
            spoon.reflect.declaration.CtField<org.apache.logging.log4j.Logger> loggerField = factory.createField(ctClass, java.util.Set.of(spoon.reflect.declaration.ModifierKind.PRIVATE, spoon.reflect.declaration.ModifierKind.STATIC, spoon.reflect.declaration.ModifierKind.FINAL), loggerType, "logger", factory.Code().createCodeSnippetExpression(("LogManager.getLogger(" + ctClass.getSimpleName()) + ".class)"));
            ctClass.addField(loggerField);
        }
    }

    private static void addLogging(spoon.reflect.declaration.CtMethod<?> method, spoon.reflect.factory.Factory factory) {
        // Crée une invocation de log "logger.info(...)"
        spoon.reflect.reference.CtTypeReference<org.apache.logging.log4j.Logger> loggerType = factory.createCtTypeReference(org.apache.logging.log4j.Logger.class);
        spoon.reflect.reference.CtExecutableReference<java.lang.Void> infoMethod = factory.createExecutableReference();
        spoon.reflect.code.CtInvocation<java.lang.Void> logInfo = factory.createInvocation(factory.createVariableRead(factory.createFieldReference().setSimpleName("logger"), false), infoMethod, factory.createLiteral("Executing method: " + method.getSimpleName()));
        infoMethod.setDeclaringType(loggerType);
        infoMethod.setSimpleName("info");
        infoMethod.setType(factory.Type().VOID_PRIMITIVE);
        // Injecter le log au début de la méthode
        method.getBody().insertBegin(logInfo);
    }
}