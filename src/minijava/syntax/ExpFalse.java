package minijava.syntax;

import minijava.semantic.visitor.Visitor;

public class ExpFalse extends Exp {

    public ExpFalse() {
    }

    @Override
    public <A, T extends Throwable> A accept(ExpVisitor<A, T> v) throws T {
        return v.visit(this);
    }

    @Override
    public <V> V accept(Visitor<V> v) {
        return v.visit(this);
    }
}
