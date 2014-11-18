package minijava.semantic.node;

import minijava.intermediate.model.IntermediateMemOffset;
import minijava.intermediate.tree.TreeExp;
import minijava.syntax.Ty;

/**
 * User: kowa
 * Date: 10/29/14
 */
public class VarDeclaration extends Declaration {
    private IntermediateMemOffset intermediateMemOffset = null;
    private TreeExp accessExp = null;
    private Kind kind;

    public VarDeclaration(Ty type, Kind kind) {
        super(type);
        this.kind = kind;
    }

    @Override
    public Kind getKind() {
        return kind;
    }

    public void setAccess(IntermediateMemOffset intermediateMemOffset) {
        this.intermediateMemOffset = intermediateMemOffset;
    }

    public void setAccess(TreeExp exp) {
        this.accessExp = exp;
    }

    public TreeExp fieldAccess(TreeExp thisPointer) {
        if (accessExp != null) {
            //prefer local variables over field variables
            return accessExp;
        }
        if (intermediateMemOffset == null) {
            throw new RuntimeException("cannot access field without intermediateMemOffset!!");
        }

        return intermediateMemOffset.exp(thisPointer);
    }

    public TreeExp localAccess() {
        if (accessExp == null) {
            throw new RuntimeException("cannot access field!!");
        }

        return fieldAccess(null);
    }


}
