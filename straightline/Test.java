package straightline;

import minijava.semantic.symbol.SymbolTable;
import minijava.semantic.visitor.ErrorMsg;
import minijava.semantic.visitor.SymbolTableVisitor;
import minijava.semantic.visitor.TypeCheckVisitor;
import minijava.syntax.Prg;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

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
        SymbolTable symbolTable = new SymbolTable();
        List<ErrorMsg> errors = new ArrayList<ErrorMsg>();
        try {
            try {
                parseTree = parser.parse();
                Prg prg = (Prg) parseTree.value;
                prg.accept(new SymbolTableVisitor(symbolTable, errors));

                if (errors.size() > 0) {
                    System.out.println("number of errors: " + errors.size());
                    System.exit(1);
                } else {
                    TypeCheckVisitor typeCheckVisitor = new TypeCheckVisitor(symbolTable, errors);
                    prg.accept(typeCheckVisitor);
                    System.out.println("number of errors: " + errors.size());
                }
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
