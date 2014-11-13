package minijava.syntax;

import minijava.semantic.visitor.Visitor;

public abstract class Stm {

    public abstract <A, T extends Throwable> A accept(StmVisitor<A, T> v) throws T;

    public abstract<V> V accept(Visitor<V> v);

    public String prettyPrint() {
        return accept(new PrettyPrint.PrettyPrintVisitorStm());
    }
}

