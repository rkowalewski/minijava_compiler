package minijava.syntax;

import minijava.semantic.visitor.Visitor;

public abstract class Exp {

    public abstract <A, T extends Throwable> A accept(ExpVisitor<A, T> v) throws T;

    public abstract<V> V accept(Visitor<V> v);

    public String prettyPrint() {
        return accept(new PrettyPrint.PrettyPrintVisitorExp());
    }
}



