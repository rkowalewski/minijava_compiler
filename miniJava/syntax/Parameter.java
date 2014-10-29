package minijava.syntax;

import minijava.semantic.visitor.Visitor;

public class Parameter {

    final public String id;
    final public Ty ty;

    public Parameter(String id, Ty ty) {
        this.id = id;
        this.ty = ty;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

}
