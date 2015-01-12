package minijava.backend.regalloc;

import minijava.intermediate.Temp;

/**
 * User: kowa
 * Date: 1/7/15
 */
public class NodeInfo {
    public final Temp temp;
    private InterferenceGraph.Node moveNode;
    private boolean mergeable;

    public NodeInfo(Temp temp) {
        this.temp = temp;
    }

    public boolean isFixedColor() {
        return temp.isFixedColor();
    }

    public InterferenceGraph.Node getMoveNode() {
        return moveNode;
    }

    public void setMoveNode(InterferenceGraph.Node moveNode) {
        this.moveNode = moveNode;
    }
}
