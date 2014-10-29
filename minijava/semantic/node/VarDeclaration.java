package minijava.semantic.node;

/**
 * User: kowa
 * Date: 10/29/14
 */
public class VarDeclaration extends Declaration {
    private ClassDeclaration declaringClass = null;

    public VarDeclaration(Type type) {
        super(type);
    }

    public ClassDeclaration getDeclaringClass() {
        return declaringClass;
    }

    public void setDeclaringClass(ClassDeclaration declaringClass) {
        this.declaringClass = declaringClass;
    }
}
