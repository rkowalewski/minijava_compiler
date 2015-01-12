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

    private Set<Assem> moves = new HashSet<>();

    public AssemFlowGraph(List<minijava.backend.Assem> body) {
        SimpleGraph<Assem>.Node prev = null;
        SimpleGraph<Assem>.Node cur;

        //First: Put all instruction in graph add add edges
        for (Assem instr : body) {
            Pair<Temp, Temp> moveBetweenTemps = instr.isMoveBetweenTemps();

            if (moveBetweenTemps != null) {
                //Discard nodes where source equals dest
                if (moveBetweenTemps.fst.equals(moveBetweenTemps.snd)) {
                    continue;
                } else {
                    this.moves.add(instr);
                }
            }

            cur = this.new Node(instr);

            if (prev != null && prev.info.isFallThrough()) {
                this.addEdge(prev, cur);
            }

            prev = cur;
        }

        //Second: add edges from all nodes with jumps to their targets
        for (Node src : this.nodeSet()) {
            if (!src.info.jumps().isEmpty()) {
                for (Label targetLabel : src.info.jumps()) {
                    for (Node targetNode : this.nodeSet()) {
                        if (targetLabel.equals(targetNode.info.isLabel())) {
                            this.addEdge(src, targetNode);
                        }
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

    public Set<Assem> getMoves() {
        return Collections.unmodifiableSet(moves);
    }

    protected List<Temp> defList(Node node) {
        return node.info.def();
    }

    protected List<Temp> useList(Node node) {
        return node.info.use();
    }


    private class LivenessInfo {
        private Map<Node, Set<Temp>> liveIn = new LinkedHashMap<>();
        private Map<Node, Set<Temp>> liveOut = new LinkedHashMap<>();

        public void analyze() {

            boolean isChanged = true;

            for (Node node : nodeSet()) {
                liveIn.put(node, new HashSet<Temp>());
                liveOut.put(node, new HashSet<Temp>());
            }

            reverse();

            while (isChanged) {
                for (Node node : nodeSet()) {

                    Set<Temp> out = internalLiveOut(node);
                    Set<Temp> in = internalLiveIn(node, out);

                    isChanged = !(in.equals(liveIn.get(node)) && out.equals(liveOut.get(node)));

                    liveIn.put(node, in);
                    liveOut.put(node, out);
                }
            }

            reverse();
        }

        private Set<Temp> internalLiveOut(SimpleGraph<Assem>.Node node) {
            // out[n] = U_{s in succ[n]} in[s]
            Set<Temp> liveOutTemps = new HashSet<>();

            for (Node successor : node.successors()) {
                liveOutTemps.addAll(this.liveIn.get(successor));
            }

            return liveOutTemps;

        }

        private Set<Temp> internalLiveIn(SimpleGraph<Assem>.Node node, Set<Temp> liveOutList) {
            // (out[n] - def[n])

            Set<Temp> liveIn = new HashSet<>(liveOutList);

            for (Temp temp : defList(node)) {
                liveIn.remove(temp);
            }

            // use[n] U (out[n] - def[n])
            for (Temp temp : useList(node)) {
                liveIn.add(temp);
            }

            return liveIn;
        }
    }
}
