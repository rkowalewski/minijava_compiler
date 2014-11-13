package minijava.syntax;

import minijava.semantic.visitor.Visitor;

public abstract class Ty {

    @Override
    public abstract String toString();

    public abstract <A> A accept(TyVisitor<A> v);

    public abstract boolean isPrimitive();

    public <V> V accept(Visitor<V> visitor) {
        return visitor.visit(this);
    }
}




