package minijava.intermediate.model;

import minijava.intermediate.tree.TreeExp;
import minijava.intermediate.tree.TreeExpCONST;
import minijava.intermediate.tree.TreeExpMEM;
import minijava.intermediate.tree.TreeExpOP;

/**
 * User: kowa
 * Date: 11/11/14
 */
public class IntermediateMemOffset {
    private final int offset;

    public IntermediateMemOffset(int offset) {
        this.offset = offset;
    }

    public TreeExp exp(TreeExp thisPointer) {
        if (offset == 0) {
            return new TreeExpMEM(new TreeExpCONST(0));
        }

        return new TreeExpMEM(new TreeExpOP(TreeExpOP.Op.PLUS, thisPointer, new TreeExpCONST(offset)));
    }

}
