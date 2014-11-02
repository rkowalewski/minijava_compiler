package minijava.semantic.node;

import minijava.syntax.Ty;

/**
 * User: kowa
 * Date: 10/28/14
 */
public class ClassDeclaration extends Declaration {
    private final String className;

    public ClassDeclaration(String className, Ty type) {
        super(type);
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    @Override
    public Kind getKind() {
        return Kind.CLASS;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
