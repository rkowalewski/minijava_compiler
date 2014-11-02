package minijava.syntax;

import minijava.semantic.visitor.Visitor;

public class DeclVar {

    final public Ty ty;
    final public String name;

    public DeclVar(Ty ty, String name) {
        this.ty = ty;
        this.name = name;
    }

    public <V> V accept(Visitor<V> v) {
        return v.visit(this);
    }
}
