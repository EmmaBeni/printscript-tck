package implementation;

// Archivo temporal para explorar qué clases están disponibles
import java.lang.reflect.*;

public class ClassExplorer {
  public static void main(String[] args) {
    String[] possibleClasses = {
      // Módulos principales disponibles
      "interpreter.Interpreter",
      "lexer.Lexer",
      "linter.Linter",
      "parser.Parser",
      "formatter.Formatter",
      "executor.Executor",

      // Clases de datos
      "ast.ASTNode",
      "ast.AST",
      "token.Token",
      "tokendata.TokenData",
      "container.Container",
      "inputprovider.InputProvider",
      "analyzer.Analyzer",
      "formatteraction.FormatterAction",
      "progress.Progress",

      // Versiones con sufijo Kt (objetos Kotlin)
      "interpreter.InterpreterKt",
      "lexer.LexerKt",
      "linter.LinterKt",
      "parser.ParserKt",
      "formatter.FormatterKt",
      "executor.ExecutorKt",

      // Posibles clases principales por módulo
      "interpreter.PrintScriptInterpreter",
      "linter.PrintScriptLinter",
      "formatter.PrintScriptFormatter",

      // Posibles implementaciones específicas
      "interpreter.InterpreterImpl",
      "linter.LinterImpl",
      "formatter.FormatterImpl",
      "parser.ParserImpl",

      // Clases factory o builder
      "interpreter.InterpreterFactory",
      "linter.LinterFactory",
      "formatter.FormatterFactory"
    };

    System.out.println("Explorando clases de IngsisTP (versión 1.1.202509160331296)...\n");

    for (String className : possibleClasses) {
      try {
        Class<?> clazz = Class.forName(className);
        System.out.println("✓ ENCONTRADA: " + className);
        System.out.println("  Tipo: " + (clazz.isInterface() ? "Interface" : "Class"));

        // Mostrar constructores públicos
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        System.out.println("  Constructores públicos:");
        boolean hasPublicConstructor = false;
        for (Constructor<?> constructor : constructors) {
          if (Modifier.isPublic(constructor.getModifiers())) {
            System.out.println("    - " + constructor.toString().replaceAll(".*\\.", ""));
            hasPublicConstructor = true;
          }
        }
        if (!hasPublicConstructor) {
          System.out.println("    - No hay constructores públicos (puede ser object Kotlin)");
        }

        // Mostrar algunos métodos públicos importantes
        Method[] methods = clazz.getDeclaredMethods();
        System.out.println("  Métodos públicos relevantes:");
        int count = 0;
        for (Method method : methods) {
          if (Modifier.isPublic(method.getModifiers()) && count < 8) {
            String params = "";
            Class<?>[] paramTypes = method.getParameterTypes();
            if (paramTypes.length > 0) {
              params = java.util.Arrays.stream(paramTypes)
                .map(Class::getSimpleName)
                .reduce((a, b) -> a + ", " + b)
                .orElse("");
            }
            System.out.println("    - " + method.getName() + "(" + params + ") : " + method.getReturnType().getSimpleName());
            count++;
          }
        }

        // Verificar si es un object Kotlin (singleton)
        try {
          Field instanceField = clazz.getField("INSTANCE");
          if (Modifier.isStatic(instanceField.getModifiers())) {
            System.out.println("  ⭐ Es un Kotlin object - usar: " + className + ".INSTANCE");
          }
        } catch (NoSuchFieldException ignored) {}

        System.out.println();

      } catch (ClassNotFoundException e) {
        System.out.println("✗ No encontrada: " + className);
      } catch (Exception e) {
        System.out.println("? Error con: " + className + " - " + e.getMessage());
      }
    }

    System.out.println("\n=== Exploración completada ===");
    System.out.println("Usa las clases marcadas con ✓ en tu implementación");
    System.out.println("Las marcadas con ⭐ son Kotlin objects - usar .INSTANCE");
  }
}