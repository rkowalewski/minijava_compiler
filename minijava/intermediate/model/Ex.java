package minijava.intermediate.model;

import minijava.intermediate.Label;
import minijava.intermediate.tree.TreeExp;
import minijava.intermediate.tree.TreeStm;

/**
 * User: kowa
 * Date: 11/7/14
 */
public class Ex extends Exp {
    @Override
    public TreeExp unEx() {
        return null;
    }

    @Override
    public TreeStm unNx() {
        return null;
    }

    @Override
    public TreeStm unCx(Label t, Label f) {
        return null;
    }
}
