package minijava.syntax;


import minijava.semantic.visitor.Visitor;

public class ExpId extends Exp {

    final public String id;

    public ExpId(String id) {
        this.id = id;
    }

    @Override
    public <A, T extends Throwable> A accept(ExpVisitor<A, T> v) throws T {
        return v.visit(this);
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
