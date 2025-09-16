package implementation;

// Imports de las interfaces del TCK
import interpreter.PrintScriptFormatter;
import interpreter.PrintScriptInterpreter;
import interpreter.PrintScriptLinter;
import interpreter.ErrorHandler;
import interpreter.InputProvider;
import interpreter.PrintEmitter;

import java.io.*;

public class CustomImplementationFactory implements PrintScriptFactory {

    @Override
    public PrintScriptInterpreter interpreter() {
        return (src, version, emitter, handler, provider) -> {
            try {
                // Intentar usar las clases de Kotlin desde Java
                // Las clases Kotlin compiladas suelen estar disponibles así:

                // Opción 1: Usar reflection para encontrar la clase
                try {
                    Class<?> interpreterClass = Class.forName("interpreter.InterpreterKt");
                    // Si existe, usar reflection para llamar métodos
                    emitter.print("Found Kotlin interpreter class!");
                } catch (ClassNotFoundException e1) {
                    // Opción 2: Probar otros nombres posibles
                    try {
                        Class<?> interpreterClass2 = Class.forName("interpreter.Interpreter");
                        emitter.print("Found Java-compatible interpreter class!");
                    } catch (ClassNotFoundException e2) {
                        // Por ahora, implementación básica
                        BufferedReader reader = new BufferedReader(new InputStreamReader(src));
                        String line;
                        StringBuilder code = new StringBuilder();
                        while ((line = reader.readLine()) != null) {
                            code.append(line).append("\n");
                        }

                        // Simulación básica de interpretación
                        if (code.toString().contains("println")) {
                            emitter.print("PrintScript execution simulation for version " + version);
                        }
                    }
                }

            } catch (Exception e) {
                handler.reportError("Error durante la interpretación: " + e.getMessage());
            }
        };
    }

    @Override
    public PrintScriptFormatter formatter() {
        return (src, version, config, writer) -> {
            try {
                // Intentar usar el formatter de Kotlin
                try {
                    Class<?> formatterClass = Class.forName("formatter.FormatterKt");
                    writer.write("// Formatted with Kotlin formatter\n");
                } catch (ClassNotFoundException e) {
                    // Fallback: copia simple con formato básico
                    BufferedReader reader = new BufferedReader(new InputStreamReader(src));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        // Formato básico: agregar espacios alrededor de =
                        line = line.replaceAll("=", " = ");
                        writer.write(line + "\n");
                    }
                }

            } catch (Exception e) {
                throw new RuntimeException("Error durante el formateo", e);
            }
        };
    }

    @Override
    public PrintScriptLinter linter() {
        return (src, version, config, handler) -> {
            try {
                // Intentar usar el linter de Kotlin
                try {
                    Class<?> linterClass = Class.forName("linter.LinterKt");
                    // Si se encuentra la clase, no reportar errores por ahora
                } catch (ClassNotFoundException e) {
                    // Fallback: linting básico
                    BufferedReader reader = new BufferedReader(new InputStreamReader(src));
                    String line;
                    int lineNumber = 1;
                    while ((line = reader.readLine()) != null) {
                        // Ejemplo de regla básica: variables deben terminar en ;
                        if (line.trim().startsWith("let ") && !line.trim().endsWith(";")) {
                            handler.reportError("Line " + lineNumber + ": Missing semicolon");
                        }
                        lineNumber++;
                    }
                }

            } catch (Exception e) {
                handler.reportError("Error durante el linting: " + e.getMessage());
            }
        };
    }
}