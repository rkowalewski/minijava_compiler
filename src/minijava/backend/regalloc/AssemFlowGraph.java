package minijava.backend.regalloc;

import minijava.backend.Assem;
import minijava.intermediate.Label;
import minijava.intermediate.Temp;
import minijava.util.Pair;
import minijava.util.SimpleGraph;

import java.util.*;

/**
 * User: kowa
 * Date: 12/11/14
 */
public class AssemFlowGraph extends PrintableGraph<Assem> {

    private final List<Assem> body;

    public AssemFlowGraph(List<Assem> body) {
        this.body = body;
        SimpleGraph<Assem>.Node prev = null;
        SimpleGraph<Assem>.Node cur;

        //First: Put all instruction in graph add add edges
        for (Assem instr : body) {
            Pair<Temp, Temp> moveBetweenTemps = instr.isMoveBetweenTemps();

            //Discard nodes where source and dest are the same
            if (moveBetweenTemps != null && moveBetweenTemps.fst.equals(moveBetweenTemps.snd)) {
                continue;
            }

            cur = this.new Node(instr);

            if (prev != null && prev.info.isFallThrough()) {
                this.addEdge(prev, cur);
            }

            prev = cur;
        }

        //Second: add edges from all nodes with jumps to their targets
        for (Node src : this.nodeSet()) {
            for (Label targetLabel : src.info.jumps()) {
                for (Node targetNode : this.nodeSet()) {
                    if (targetLabel.equals(targetNode.info.isLabel())) {
                        this.addEdge(src, targetNode);
                    }
                }
            }
        }
    }

    public InterferenceGraph getInterenceGraph() {
        LivenessInfo livenessInfo = new LivenessInfo();
        livenessInfo.analyze();

        InterferenceGraph ig = new InterferenceGraph();

        for (Node node : this.nodeSet()) {
            Pair<Temp, Temp> moveBetweenTemps = node.info.isMoveBetweenTemps();

            if (moveBetweenTemps != null) {
                //Track all move Nodes
                ig.addMove(moveBetweenTemps);
                //simple move instruction
                for (Temp out : livenessInfo.liveOut.get(node)) {
                    if (!out.equals(moveBetweenTemps.snd)) {
                        ig.addEdge(moveBetweenTemps.fst, out);
                    }
                }
            } else {
                //not a simple move instruction
                for (Temp def : node.info.def()) {
                    for (Temp out : livenessInfo.liveOut.get(node)) {
                        ig.addEdge(out, def);
                    }
                }
            }
        }

        return ig;
    }

    private class LivenessInfo {
        private Map<Node, Set<Temp>> liveIn = new LinkedHashMap<>();
        private Map<Node, Set<Temp>> liveOut = new LinkedHashMap<>();

        public void analyze() {

            Map<Node, Set<Temp>> liveInP = new HashMap<>();
            Map<Node, Set<Temp>> liveOutP = new HashMap<>();

            for (Node node : nodeSet()) {
                liveIn.put(node, new HashSet<>(node.info.use()));
                liveOut.put(node, new HashSet<Temp>());
                liveInP.put(node, new HashSet<Temp>());
                liveOutP.put(node, new HashSet<Temp>());
            }

            boolean isChanged = true;

            //reverse topological sort to speed up the liveness analysis
            ReverseTopOrder reverseTopOrder = new ReverseTopOrder(AssemFlowGraph.this);

            while (isChanged) {

                for (AssemFlowGraph.Node node : reverseTopOrder.getElements()) {

                    liveInP.get(node).addAll(liveIn.get(node));
                    liveOutP.get(node).addAll(liveOut.get(node));

                    liveOut.get(node).addAll(internalLiveOut(node));
                    liveIn.get(node).addAll(internalLiveIn(node));

                }

                isChanged = false;
                for (AssemFlowGraph.Node node : nodeSet()) {
                    if (!(liveInP.get(node).containsAll(liveIn.get(node)) && liveOutP.get(node).containsAll(liveOut.get(node)))) {
                        isChanged = true;
                        break;
                    }
                }
            }
        }

        private Set<Temp> internalLiveOut(SimpleGraph<Assem>.Node node) {
            // out[n] = U_{s in succ[n]} in[s]
            Set<Temp> liveOut = new HashSet<>();

            for (Node successor : node.successors()) {
                liveOut.addAll(liveIn.get(successor));
            }

            return liveOut;
        }

        private Set<Temp> internalLiveIn(SimpleGraph<Assem>.Node node) {
            // (use[n] U (out[n] - def[n]))
            Set<Temp> aux = new HashSet<>(liveOut.get(node));
            aux.removeAll(node.info.def());
            return aux;
        }
    }

    private class ReverseTopOrder {
        public Iterable<SimpleGraph<Assem>.Node> getElements() {
            return Collections.unmodifiableList(postOrder);
        }

        private LinkedList<SimpleGraph<Assem>.Node> postOrder;
        private final Map<Node, Boolean> marked;

        public ReverseTopOrder(SimpleGraph<Assem> graph) {
            postOrder = new LinkedList<>();
            marked = new HashMap<>();

            for (SimpleGraph<Assem>.Node node : graph.nodeSet()) {
                if (!isMarked(node)) {
                    dfs(node);
                }
            }
        }

        private boolean isMarked(Node node) {
            return marked.containsKey(node) && marked.get(node).booleanValue();
        }

        private void dfs(SimpleGraph<Assem>.Node node) {
            marked.put(node, true);

            for (SimpleGraph<Assem>.Node neigh : node.successors()) {
                if (!isMarked(neigh)) {
                    dfs(neigh);
                }
            }

            postOrder.add(node);
        }




    }
}
