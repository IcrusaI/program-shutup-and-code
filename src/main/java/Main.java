import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            // Замените на путь к вашему входному файлу или другому источнику ввода
            String inputFile = "src/main/resources/input.txt";

            // Чтение входного файла
            CharStream input = CharStreams.fromFileName(inputFile);

            // Создание лексера и парсера
            BazhenovLexer lexer = new BazhenovLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            BazhenovParser parser = new BazhenovParser(tokens);

            // Удаление стандартных слушателей ошибок и добавление своего (если необходимо)
            parser.removeErrorListeners();
            parser.addErrorListener(new MyErrorListener());

            // Парсинг стартового правила (замените 'program' на ваше стартовое правило)
            ParseTree tree = parser.program();

            // Обход дерева с помощью вашего TypeChecker
            ParseTreeWalker walker = new ParseTreeWalker();
            TypeChecker typeChecker = new TypeChecker();
            walker.walk(typeChecker, tree);

            // Дополнительная обработка результатов (при необходимости)

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
