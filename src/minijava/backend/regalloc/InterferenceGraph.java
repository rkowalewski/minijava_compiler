package minijava.backend.regalloc;

import minijava.intermediate.Temp;
import minijava.util.Pair;

import java.util.*;

/**
 * User: kowa
 * Date: 12/15/14
 */
public class InterferenceGraph extends PrintableGraph<Temp> {

    private final Map<Node, Integer> colorMap = new HashMap<>();
    private final List<Move> moves = new ArrayList<>();

    public InterferenceGraph() {
        super();
    }


    public void addEdge(Temp src, Temp dst) {

        Node nodeSrc = nodeFor(src, true);

        Node nodeDst = nodeFor(dst, true);

        this.addEdge(nodeSrc, nodeDst);
    }

    public void addMove(Pair<Temp, Temp> movePair) {
        this.moves.add(new Move(nodeFor(movePair.fst, true), nodeFor(movePair.snd, true)));
    }

    @Override
    public void addEdge(Node src, Node dst) {
        super.addEdge(src, dst);
        super.addEdge(dst, src);
    }

    private Node nodeFor(Temp temp, boolean insert) {
        for (Node node : nodeSet()) {
            if (node.info.equals(temp)) {
                return node;
            }
        }

        if (insert) {
            return new Node(temp);
        }

        return null;
    }

    public List<Move> getMoves() {
        return Collections.unmodifiableList(moves);
    }

    public class Move {
        public final Node src, dst;

        public Move(Node dst, Node src) {
            this.src = src;
            this.dst = dst;
        }
    }



}
