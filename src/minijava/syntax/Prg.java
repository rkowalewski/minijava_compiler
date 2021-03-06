package minijava.syntax;

import minijava.semantic.visitor.Visitor;

import java.util.List;

public class Prg {

    final public DeclMain mainClass;
    final public List<DeclClass> classes;

    public Prg(DeclMain mainClass, List<DeclClass> classes) {
        this.mainClass = mainClass;
        this.classes = classes;
    }

    public String prettyPrint() {
        return PrettyPrint.prettyPrint(this);
    }

    public void translateIntermediate() {

    }

    public <V> V accept(Visitor<V> v) {
        return v.visit(this);
    }

}
