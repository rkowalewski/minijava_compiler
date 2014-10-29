package minijava.syntax;

import minijava.semantic.visitor.Visitor;

public class StmAssign extends Stm {

    final public String id;
    final public Exp rhs;

    public StmAssign(String id, Exp rhs) {
        this.id = id;
        this.rhs = rhs;
    }

    @Override
    public <A, T extends Throwable> A accept(StmVisitor<A, T> v) throws T {
        return v.visit(this);
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
