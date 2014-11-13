package minijava.syntax;

import minijava.semantic.visitor.Visitor;

public class ExpArrayGet extends Exp {

    final public Exp array;
    final public Exp index;

    public ExpArrayGet(Exp array, Exp index) {
        this.array = array;
        this.index = index;
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
