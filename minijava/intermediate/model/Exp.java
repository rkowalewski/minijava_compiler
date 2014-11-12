package minijava.intermediate.model;

import minijava.intermediate.Label;
import minijava.intermediate.tree.TreeExp;
import minijava.intermediate.tree.TreeStm;

/**
 * User: kowa
 * Date: 11/7/14
 */
public abstract class Exp {
    public abstract TreeExp unEx();
    public abstract TreeStm unNx();
    public abstract TreeStm unCx(Label t, Label f);

}
