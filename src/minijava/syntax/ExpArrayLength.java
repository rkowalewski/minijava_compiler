package minijava.syntax;


import minijava.semantic.visitor.Visitor;

public class ExpArrayLength extends Exp {

    final public Exp array;

    public ExpArrayLength(Exp body) {
        this.array = body;
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
