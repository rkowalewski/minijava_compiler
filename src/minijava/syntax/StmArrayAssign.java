package minijava.syntax;

import minijava.semantic.visitor.Visitor;

public class StmArrayAssign extends Stm {

    final public String id;
    final public Exp index;
    final public Exp rhs;

    public StmArrayAssign(String id, Exp index, Exp rhs) {
        this.id = id;
        this.index = index;
        this.rhs = rhs;
    }

    @Override
    public <A, T extends Throwable> A accept(StmVisitor<A, T> v) throws T {
        return v.visit(this);
    }

    @Override
    public <V> V accept(Visitor<V> v) {
        return v.visit(this);
    }
}
