package minijava.semantic.node;

import java.util.HashMap;
import java.util.Map;

/**
 * User: kowa
 * Date: 10/28/14
 */
public class ClassDeclaration extends Declaration {
    private final Map<String, VarDeclaration> fields;
    private final Map<String, MethodDeclaration> methods;
    private final String className;

    public ClassDeclaration(String className, Type type) {
        super(type);
        this.className = className;
        this.fields = new HashMap<String, VarDeclaration>();
        this.methods = new HashMap<String, MethodDeclaration>();
    }

    public boolean addMethod(String name, MethodDeclaration methodNode) {
        if (methods.containsKey(name)) {
            return false;
        }

        methods.put(name, methodNode);
        return true;
    }

    public boolean addField(String name, VarDeclaration varNode) {
        if (fields.containsKey(name)) {
            return false;
        }

        fields.put(name, varNode);
        return true;
    }

    public Map<String, VarDeclaration> getFields() {
        return fields;
    }

    public Map<String, MethodDeclaration> getMethods() {
        return methods;
    }

    public String getClassName() {
        return className;
    }

    public MethodDeclaration getMethod(String name) {
        return methods.get(name);
    }
}
