package minijava.semantic.node;

/**
 * User: kowa
 * Date: 10/28/14
 */
public abstract class Declaration {
    private Type type;
    public Declaration(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }
}
