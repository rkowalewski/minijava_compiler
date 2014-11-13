package minijava.semantic.node;

import minijava.syntax.Ty;

/**
 * User: kowa
 * Date: 10/28/14
 */
public abstract class Declaration {
    protected Ty type;
    public Declaration(Ty type) {
        this.type = type;
    }
    public Ty getType() {
        return type;
    }
    public abstract Kind getKind();

    public enum Kind {
        VARIABLE, PARAMETER, METHOD, CLASS;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Declaration that = (Declaration) o;

        if (!type.equals(that.type)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return type.hashCode();
    }
}
