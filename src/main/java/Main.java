import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            String inputFile = "src/main/resources/input.txt";

            CharStream input = CharStreams.fromFileName(inputFile);

            BazhenovLexer lexer = new BazhenovLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            BazhenovParser parser = new BazhenovParser(tokens);

            parser.removeErrorListeners();
            parser.addErrorListener(new MyErrorListener());

            ParseTree tree = parser.program();

            ParseTreeWalker walker = new ParseTreeWalker();
            TypeChecker typeChecker = new TypeChecker();
            walker.walk(typeChecker, tree);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
