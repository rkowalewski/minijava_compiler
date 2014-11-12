package minijava.semantic.node;

import minijava.intermediate.model.IntermediateMemOffset;
import minijava.intermediate.tree.TreeExp;
import minijava.semantic.symbol.Symbol;
import minijava.syntax.Ty;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * User: kowa
 * Date: 10/28/14
 */
public class ClassDeclaration extends Declaration {
    private final String className;
    private final Map<Symbol, VarDeclaration> fields = new LinkedHashMap<Symbol, VarDeclaration>();
    private final Map<Symbol, MethodDeclaration> methods = new LinkedHashMap<Symbol, MethodDeclaration>();

    private IntermediateMemOffset intermediateMemOffset;

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

    public void addField(Symbol symbol, VarDeclaration field) {
        fields.put(symbol, field);
    }

    public void addMethod(Symbol symbol, MethodDeclaration method) {
        methods.put(symbol, method);
    }

    public VarDeclaration getField(Symbol key) {
        return fields.get(key);
    }

    public MethodDeclaration getMethod(Symbol key) {
        return methods.get(key);
    }

    public int getFieldsCount() {
        return fields.size();
    }

    public int getMethodsCount() {
        return methods.size();
    }

    public void setAccess(IntermediateMemOffset intermediateMemOffset) {
        this.intermediateMemOffset = intermediateMemOffset;
    }

    public TreeExp exp() {
        if (intermediateMemOffset == null) {
            throw new RuntimeException("cannot access field without intermediateMemOffset!!");
        }

        return intermediateMemOffset.exp(null);
    }
}
