package minijava.backend.regalloc;

import minijava.intermediate.Temp;
import minijava.util.Pair;
import minijava.util.SimpleGraph;

import java.util.*;

/**
 * User: kowa
 * Date: 12/15/14
 */
public class InterferenceGraph extends SimpleGraph<NodeInfo> {

//    private final Set<Pair<Node, Node>> movePairs = new HashSet<>();
    private final Set<Node> moveNodes = new HashSet<>();

    public InterferenceGraph() {
        super();
    }


    public Pair<Node, Node> addEdge(Temp src, Temp dst) {
        Node nodeSrc = nodeFor(src, true);
        Node nodeDst = nodeFor(dst, true);
        this.addEdge(nodeSrc, nodeDst);
        return new Pair<>(nodeSrc, nodeDst);
    }

    public void addMove(Pair<Temp, Temp> movePair) {
        Node dst = nodeFor(movePair.fst, true);
        Node src = nodeFor(movePair.snd, true);

        this.moveNodes.add(dst);
        this.moveNodes.add(src);
//        this.movePairs.add(new Pair(dst, src));
    }

    @Override
    public void addEdge(Node src, Node dst) {
        if (src == null || dst == null || src.successors().contains(dst)) {
            return;
        }

        super.addEdge(src, dst);
        super.addEdge(dst, src);
    }

    public Node getNodeByTemp(Temp temp) {
        return nodeFor(temp, false);
    }

    private Node nodeFor(Temp temp, boolean insert) {
        for (Node node : nodeSet()) {
            if (node.info.equals(temp)) {
                return node;
            }
        }

        if (insert) {
            return new Node(new NodeInfo(temp));
        }

        return null;
    }

    public Set<Node> getMoveNodes() {
        return Collections.unmodifiableSet(moveNodes);
    }
}
