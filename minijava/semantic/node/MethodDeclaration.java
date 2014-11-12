package minijava.semantic.node;

import minijava.intermediate.Frame;
import minijava.semantic.symbol.Symbol;
import minijava.syntax.Ty;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * User: kowa
 * Date: 10/28/14
 */
public class MethodDeclaration extends Declaration {
    private ClassDeclaration declaringClass;
    private Frame frame;

    private Map<Symbol, VarDeclaration> args = new LinkedHashMap<>();

    public MethodDeclaration(Ty type) {
        super(type);
    }

    public Map<Symbol, VarDeclaration> getArgs() {
        return args;
    }

    public void addParameterInfo(Symbol key, VarDeclaration param) {
        args.put(key, param);
    }

    public int getParameterIndexByKey(Symbol key) {
        if (args.containsKey(key)) {
            List<Map.Entry<Symbol,VarDeclaration>> argsList = new ArrayList<>(args.entrySet());

            for (int i = 0; i < argsList.size(); i++) {
                if (argsList.get(i).getKey().equals(key)) {
                    return i;
                }
            }
        }

        return -1;
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

    public Frame getFrame() {
        return frame;
    }

    public void setFrame(Frame frame) {
        this.frame = frame;
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
