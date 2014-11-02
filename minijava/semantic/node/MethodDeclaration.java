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
}
