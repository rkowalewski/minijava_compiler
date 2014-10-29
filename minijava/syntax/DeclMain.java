package minijava.syntax;

import minijava.semantic.visitor.Visitor;

public class DeclMain {

    final public String className;
    final public String mainArg;
    final public Stm mainBody;

    public DeclMain(String className, String mainArg, Stm mainBody) {
        this.className = className;
        this.mainArg = mainArg;
        this.mainBody = mainBody;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }
}

