package implementation;

// Archivo temporal para explorar qué clases están disponibles
import java.lang.reflect.*;

public class ClassExplorer {
  public static void main(String[] args) {
    String[] possibleClasses = {
      // Clases principales que probablemente existen
      "interpreter.Interpreter",
      "interpreter.InterpreterKt",
      "lexer.Lexer",
      "lexer.LexerKt",
      "linter.Linter",
      "linter.LinterKt",
      "formatter.Formatter",
      "formatter.FormatterKt",
      "parser.Parser",
      "parser.ParserKt",

      // Clases de AST y Token
      "ast.ASTNode",
      "ast.ASTNodeKt",
      "token.Token",
      "token.TokenKt",
      "tokendata.TokenData",
      "tokendata.TokenDataKt",

      // Posibles nombres alternativos
      "interpreter.PrintScriptInterpreterImpl",
      "linter.PrintScriptLinterImpl",
      "formatter.PrintScriptFormatterImpl"
    };

    System.out.println("Explorando clases disponibles...\n");

    for (String className : possibleClasses) {
      try {
        Class<?> clazz = Class.forName(className);
        System.out.println("✓ ENCONTRADA: " + className);

        // Mostrar algunos métodos públicos
        Method[] methods = clazz.getDeclaredMethods();
        System.out.println("  Métodos disponibles:");
        for (int i = 0; i < Math.min(5, methods.length); i++) {
          Method method = methods[i];
          if (Modifier.isPublic(method.getModifiers())) {
            System.out.println("    - " + method.getName() + "()");
          }
        }
        System.out.println();

      } catch (ClassNotFoundException e) {
        System.out.println("✗ No encontrada: " + className);
      } catch (Exception e) {
        System.out.println("? Error con: " + className + " - " + e.getMessage());
      }
    }

    System.out.println("\n=== Exploración completada ===");
  }
}