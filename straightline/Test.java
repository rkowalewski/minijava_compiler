package straightline;

import minijava.semantic.visitor.SymbolTableVisitor;
import minijava.syntax.Prg;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Test {

    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("First argument must be file name of straigtline program.");
            System.exit(-1);
        }

        String filename = args[0];
        java.io.InputStream inp;

        try {
            inp = new FileInputStream(filename);
        } catch (FileNotFoundException e) {
            throw new Error("File not found: " + filename);
        }

        Lexer lex = new Lexer(inp);
        Parser parser = new Parser(lex);

        java_cup.runtime.Symbol parseTree;
        try {
            try {
                parseTree = parser.parse();
                Prg prg = (Prg) parseTree.value;
                SymbolTableVisitor typeInfo = new SymbolTableVisitor();
                prg.accept(typeInfo);
                System.out.println("ErrorCount: " + typeInfo.getErrorCount());
                //System.out.println(prg.prettyPrint());
            } finally {
                inp.close();
            }
        } catch (ParseException ex) {
            System.out.println("Parse error!\n" + ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
