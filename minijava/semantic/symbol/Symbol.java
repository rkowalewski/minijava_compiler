package minijava.semantic.symbol;

import minijava.semantic.node.Declaration;

/**
 * User: kowa
 * Date: 10/29/14
 */
public class Symbol {
    String name;
    Integer scope;
    Declaration declaration;

    public Symbol(String _name, Integer _scope, Declaration _declaration) {
        name = _name;
        scope = _scope;
        declaration = _declaration;
    }

    public String getName() {
        return name;
    }

    public Integer getScope() {
        return scope;
    }

    public Declaration getDeclaration() {
        return this.declaration;
    }
}
