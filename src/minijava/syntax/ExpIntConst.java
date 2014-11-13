package minijava.syntax;


import minijava.semantic.visitor.Visitor;

public class ExpIntConst extends Exp {

    final public int value;

    public ExpIntConst(int value) {
        this.value = value;
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
