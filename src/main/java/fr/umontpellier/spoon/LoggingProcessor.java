package fr.umontpellier.spoon;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.factory.Factory;
import spoon.reflect.code.CtCodeSnippetStatement;

public class LoggingProcessor extends AbstractProcessor<CtMethod<?>> {

    @Override
    public boolean isToBeProcessed(CtMethod<?> method) {
        CtClass<?> declaringClass = (CtClass<?>) method.getDeclaringType();
        return declaringClass.getSimpleName().equals("ProductRepository") ||
                declaringClass.getSimpleName().equals("Menu");
    }

    @Override
    public void process(CtMethod<?> method) {
        Factory factory = getFactory();

        // Only process methods that interact with products
        if (!isProductOperation(method)) {
            return;
        }

        // Create logging statement at the beginning of the method
        String logStartStatement = createLogStatement(method, "START");
        insertLoggingStatement(method, logStartStatement, true);

        // Create logging statement at the end of the method
        String logEndStatement = createLogStatement(method, "END");
        insertLoggingStatement(method, logEndStatement, false);
    }

    private boolean isProductOperation(CtMethod<?> method) {
        String methodName = method.getSimpleName();
        return methodName.contains("Product") ||
                methodName.equals("displayProducts") ||
                methodName.equals("start");
    }

    private String createLogStatement(CtMethod<?> method, String phase) {
        StringBuilder logBuilder = new StringBuilder();
        logBuilder.append("org.apache.logging.log4j.LogManager.getLogger(")
                .append(method.getDeclaringType().getSimpleName())
                .append(".class).info(");

        // Build JSON-structured log message
        logBuilder.append("String.format(\"{\\\"timestamp\\\": \\\"%s\\\", ")
                .append("\\\"phase\\\": \\\"").append(phase).append("\\\", ")
                .append("\\\"class\\\": \\\"").append(method.getDeclaringType().getSimpleName()).append("\\\", ")
                .append("\\\"method\\\": \\\"").append(method.getSimpleName()).append("\\\", ")
                .append("\\\"parameters\\\": {%s}, ")
                .append("\\\"operation_type\\\": \\\"%s\\\"}\", ")
                .append("java.time.LocalDateTime.now(), ");

        // Add parameter logging
        logBuilder.append("\"");
        for (CtParameter<?> param : method.getParameters()) {
            logBuilder.append(String.format("\\\"%s\\\": \\\"\" + %s + \"\\\", ",
                    param.getSimpleName(), param.getSimpleName()));
        }
        if (!method.getParameters().isEmpty()) {
            logBuilder.setLength(logBuilder.length() - 2); // Remove last comma
        }
        logBuilder.append("\", ");

        // Determine operation type
        String operationType = determineOperationType(method);
        logBuilder.append("\"").append(operationType).append("\"");

        logBuilder.append(")");

        return logBuilder.toString();
    }

    private String determineOperationType(CtMethod<?> method) {
        String methodName = method.getSimpleName().toLowerCase();
        if (methodName.contains("get") || methodName.contains("display")) {
            return "READ";
        } else if (methodName.contains("add") || methodName.contains("update") ||
                methodName.contains("delete")) {
            return "WRITE";
        }
        return "UNKNOWN";
    }

    private void insertLoggingStatement(CtMethod<?> method, String logStatement, boolean start) {
        CtCodeSnippetStatement snippet = getFactory().Code().createCodeSnippetStatement(logStatement);
        CtBlock<?> body = method.getBody();

        if (start) {
            body.insertBegin(snippet);
        } else {
            body.insertEnd(snippet);
        }
    }
}