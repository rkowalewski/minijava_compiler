package minijava.semantic.symbol;

import minijava.semantic.node.Declaration;

import java.util.Stack;

public class SymbolTable {

    Stack<Symbol> symbols;
    Integer level;

    public SymbolTable() {
        symbols = new Stack<Symbol>();
        level = 0;
    }

    public Symbol push(String name, Declaration declaration) {
        Symbol symbol = new Symbol(name, level, declaration);
        symbols.push(symbol);
        return symbol;
    }

    public Symbol scopeContains(String name) {
        Symbol symbol = null;
        for (int i = symbols.size() - 1; i >= 0; i--) {
            symbol = symbols.get(i);
            if (symbol.getScope().equals(level)) {
                if (symbol.getName().equals(name)) {
                    return symbol;
                }
            } else {
                break;
            }
        }
        return null;
    }

    public Symbol lookup(String name) {
        for (int i = symbols.size() - 1; i >= 0; i--) {
            Symbol symbol = symbols.get(i);
            if (symbol.getName().equals(name)) {
                return symbol;
            }
        }
        return null;
    }

    public void beginScope() {
        level += 1;
    }

    public void endScope() {
        level -= 1;
    }

    public void discardScope() {
        for (int i = symbols.size() - 1; i >= 0; i--) {
            Symbol symbol = symbols.get(i);
            if (symbol.getScope().equals(level)) {
                symbols.remove(i);
            } else {
                break;
            }
        }
        level -= 1;
    }
}
