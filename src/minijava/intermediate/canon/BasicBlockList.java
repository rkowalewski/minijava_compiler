package minijava.intermediate.canon;

import minijava.intermediate.Label;
import minijava.intermediate.tree.TreeStm;

import java.util.LinkedList;
import java.util.List;

/**
 * User: kowa
 * Date: 11/19/14
 */
public class BasicBlockList {
    public final List<List<TreeStm>> blocks;
    public final Label done;

    public BasicBlockList(List<List<TreeStm>> blocks, Label done) {
        this.blocks = new LinkedList<>(blocks);
        this.done = done;
    }
}
