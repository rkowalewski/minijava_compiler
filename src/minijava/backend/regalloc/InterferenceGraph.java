package minijava.backend.regalloc;

import minijava.intermediate.Temp;

/**
 * User: kowa
 * Date: 12/15/14
 */
public class InterferenceGraph  extends PrintableGraph<Temp> {

    public InterferenceGraph() {
        super();
    }


    public void addEdge(Temp src, Temp dst) {

        Node nodeSrc = lookupNodeByTemp(src);

        Node nodeDst = lookupNodeByTemp(dst);

        this.addEdge(nodeSrc, nodeDst);
    }

    @Override
    public void addEdge(Node src, Node dst) {
        if (!(src != null && dst != null && src.successors().contains(dst))) {
            super.addEdge(src, dst);
        }
    }

    private Node lookupNodeByTemp(Temp temp) {
        for (Node node : nodeSet()) {
            if (node.info.equals(temp)) {
                return node;
            }
        }

        return new Node(temp);
    }


}
