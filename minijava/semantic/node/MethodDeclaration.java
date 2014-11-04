package minijava.semantic.node;

import minijava.syntax.Ty;

import java.util.List;

/**
 * User: kowa
 * Date: 10/28/14
 */
public class MethodDeclaration extends Declaration {
    private List<Ty> argTypes;
    private ClassDeclaration declaringClass;

    public MethodDeclaration(Ty type, List<Ty> argTypes) {
        super(type);
        this.argTypes = argTypes;
    }

    public List<Ty> getArgTypes() {
        return argTypes;
    }

    @Override
    public Kind getKind() {
        return Kind.METHOD;
    }

    public ClassDeclaration getDeclaringClass() {
        return declaringClass;
    }

    public void setDeclaringClass(ClassDeclaration declaringClass) {
        this.declaringClass = declaringClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        MethodDeclaration that = (MethodDeclaration) o;

        if (declaringClass != null ? !declaringClass.equals(that.declaringClass) : that.declaringClass != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (declaringClass != null ? declaringClass.hashCode() : 0);
        return result;
    }
}
