package minijava.semantic.node;

import minijava.syntax.Ty;

/**
 * User: kowa
 * Date: 10/29/14
 */
public class VarDeclaration extends Declaration {
    public VarDeclaration(Ty type) {
        super(type);
    }

    @Override
    public Kind getKind() {
        return Kind.VARIABLE;
    }


}
