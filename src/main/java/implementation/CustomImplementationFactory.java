package implementation;

// Imports de las interfaces del TCK
import interpreter.PrintScriptFormatter;
import interpreter.PrintScriptInterpreter;
import interpreter.PrintScriptLinter;
import interpreter.ErrorHandler;
import interpreter.InputProvider;
import interpreter.PrintEmitter;

import java.io.*;
import java.util.List;

public class CustomImplementationFactory implements PrintScriptFactory {

    @Override
    public PrintScriptInterpreter interpreter() {
        return (src, version, emitter, handler, provider) -> {
            try {
                // Usar los módulos de IngsisTP con la nueva versión

                // 1. Lexer - tokenizar el código
                Class<?> lexerClass = Class.forName("lexer.Lexer");
                Object lexer = createInstance(lexerClass);

                // 2. Parser - crear AST
                Class<?> parserClass = Class.forName("parser.Parser");
                Object parser = createInstance(parserClass);

                // 3. Interpreter/Executor - ejecutar
                Class<?> interpreterClass = null;
                Object interpreterInstance = null;

                try {
                    // Probar con Interpreter primero
                    interpreterClass = Class.forName("interpreter.Interpreter");
                    interpreterInstance = createInstance(interpreterClass);
                } catch (Exception e1) {
                    try {
                        // Si no existe, probar con Executor
                        interpreterClass = Class.forName("executor.Executor");
                        interpreterInstance = createInstance(interpreterClass);
                    } catch (Exception e2) {
                        throw new RuntimeException("No se encontró clase Interpreter ni Executor", e2);
                    }
                }

                // Por ahora, implementación básica hasta encontrar los métodos exactos
                emitter.print("PrintScript interpreter ready for version " + version);
                emitter.print("Using modules: lexer, parser, " + interpreterClass.getSimpleName().toLowerCase());

            } catch (Exception e) {
                handler.reportError("Error durante la interpretación: " + e.getMessage());
            }
        };
    }

    @Override
    public PrintScriptFormatter formatter() {
        return (src, version, config, writer) -> {
            try {
                // Usar el formatter de IngsisTP
                Class<?> formatterClass = Class.forName("formatter.Formatter");
                Object formatter = createInstance(formatterClass);

                // También podríamos usar FormatterAction si existe
                try {
                    Class<?> actionClass = Class.forName("formatteraction.FormatterAction");
                    Object action = createInstance(actionClass);
                } catch (Exception ignored) {}

                // Implementación básica por ahora - copia con formato básico
                BufferedReader reader = new BufferedReader(new InputStreamReader(src));
                String line;
                while ((line = reader.readLine()) != null) {
                    // Formato básico: espacios alrededor de operadores
                    line = line.replaceAll("=", " = ")
                      .replaceAll("\\+", " + ")
                      .replaceAll("-", " - ");
                    writer.write(line + "\n");
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
                // Usar el linter de IngsisTP
                Class<?> linterClass = Class.forName("linter.Linter");
                Object linter = createInstance(linterClass);

                // También podríamos usar Analyzer si existe
                try {
                    Class<?> analyzerClass = Class.forName("analyzer.Analyzer");
                    Object analyzer = createInstance(analyzerClass);
                } catch (Exception ignored) {}

                // Implementación básica por ahora - linting simple
                BufferedReader reader = new BufferedReader(new InputStreamReader(src));
                String line;
                int lineNumber = 1;
                while ((line = reader.readLine()) != null) {
                    // Reglas básicas de ejemplo
                    if (line.trim().startsWith("let ") && !line.trim().endsWith(";")) {
                        handler.reportError("Line " + lineNumber + ": Missing semicolon after variable declaration");
                    }
                    if (line.contains("=") && !line.contains(" = ")) {
                        handler.reportError("Line " + lineNumber + ": Assignment operator should have spaces around it");
                    }
                    lineNumber++;
                }

            } catch (Exception e) {
                handler.reportError("Error durante el linting: " + e.getMessage());
            }
        };
    }

    /**
     * Método auxiliar para crear instancias, manejando tanto clases normales como Kotlin objects
     */
    private Object createInstance(Class<?> clazz) throws Exception {
        try {
            // Primero intentar obtener INSTANCE (para Kotlin objects)
            java.lang.reflect.Field instanceField = clazz.getField("INSTANCE");
            if (java.lang.reflect.Modifier.isStatic(instanceField.getModifiers())) {
                return instanceField.get(null);
            }
        } catch (NoSuchFieldException ignored) {}

        // Si no es un Kotlin object, usar constructor normal
        return clazz.getDeclaredConstructor().newInstance();
    }
}