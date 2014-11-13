package minijava.syntax;

import minijava.semantic.visitor.Visitor;

import java.util.List;

public class DeclClass {

    final public String className;
    final public String superName; // null if no superclass
    final public List<DeclVar> fields;
    final public List<DeclMeth> methods;

    public DeclClass(String className, String superName,
                     List<DeclVar> fields, List<DeclMeth> methods) {
        this.className = className;
        this.superName = superName;
        this.fields = fields;
        this.methods = methods;
    }

    public <V> V accept(Visitor<V> v) {
        return v.visit(this);
    }

}
