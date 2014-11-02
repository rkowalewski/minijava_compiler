package minijava.semantic.symbol;

import java.util.HashMap;
import java.util.Map;

/**
 * User: kowa
 * Date: 10/29/14
 */
public class Symbol {
    String name;
    private static Map<String, Symbol> table = new HashMap<String, Symbol>();

    private Symbol(String s) {
        this.name = s;
    }

    public static Symbol get(String key) {
        Symbol symbol = table.get(key);

        if(symbol == null) {
            symbol = new Symbol(key);
            table.put(key, symbol);
        }

        return symbol;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean equals(Object o) {
        if(o == null || !(o instanceof Symbol)) {
            return false;
        } else {
            Symbol s = (Symbol)o;
            return s.name.equals(this.name);
        }
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
