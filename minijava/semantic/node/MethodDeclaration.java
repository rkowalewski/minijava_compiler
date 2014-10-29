package minijava.semantic.node;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: kowa
 * Date: 10/28/14
 */
public class MethodDeclaration extends Declaration {
    private Map<String, VarDeclaration> localVariables = new HashMap<String, VarDeclaration>();
    private List<Type> argTypes;
    private ClassDeclaration declaringClass;

    public MethodDeclaration(Type type, List<Type> argTypes, ClassDeclaration declaringClass) {
        super(type);
        this.argTypes = argTypes;
        this.declaringClass = declaringClass;
    }

    public Map<String, VarDeclaration> getLocalVariables() {
        return localVariables;
    }

    public void addLocalVariable(String name, VarDeclaration declVar) {
        localVariables.put(name, declVar);
    }

    public List<Type> getArgTypes() {
        return argTypes;
    }

    public ClassDeclaration getDeclaringClass() {
        return declaringClass;
    }
}
