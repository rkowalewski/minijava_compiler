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
public class AssemFlowGraph extends PrintableGraph<minijava.backend.Assem> {

    public AssemFlowGraph(List<minijava.backend.Assem> body) {
        SimpleGraph.Node prev = null;
        SimpleGraph.Node cur;

        //First: Put all instruction in graph add add edges
        for (minijava.backend.Assem instr : body) {
            Pair<Temp, Temp> moveBetweenTemps = instr.isMoveBetweenTemps();
            if (moveBetweenTemps != null && moveBetweenTemps.fst.equals(moveBetweenTemps.snd)) {
                continue;
            }

            cur = this.new Node(instr);

            if (prev != null && ((minijava.backend.Assem) prev.info).isFallThrough()) {
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
                //simple move instruction
                for (Temp out : livenessInfo.liveOut.get(node)) {
                    if (!out.equals(moveBetweenTemps.snd)) {
                        ig.addEdge(moveBetweenTemps.fst, out);
                        ig.addEdge(out, moveBetweenTemps.fst);
                    }
                }
            } else {
                //not a simple move instruction
                for (Temp def : node.info.def()) {
                    for (Temp out : livenessInfo.liveOut.get(node)) {
                        ig.addEdge(out, def);
                        ig.addEdge(def, out);
                    }
                }
            }
        }

        return ig;
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

            List<Node> nodes = new ArrayList<>(nodeSet());

            for (Node node : nodes) {
                liveIn.put(node, new HashSet<Temp>());
                liveOut.put(node, new HashSet<Temp>());
            }

            while (isChanged) {
                for (int idx = nodes.size() - 1; idx >= 0; idx--) {
                    Node node = nodes.get(idx);

                    Set<Temp> out = internalLiveOut(node);
                    Set<Temp> in = internalLiveIn(node, out);

                    isChanged = !(in.equals(liveIn.get(node)) && out.equals(liveOut.get(node)));

                    liveIn.put(node, in);
                    liveOut.put(node, out);
                }
            }
        }

        private Set<Temp> internalLiveOut(SimpleGraph.Node node) {
            // out[n] = U_{s in succ[n]} in[s]
            Set<Temp> liveOutTemps = new HashSet<>();

            for (Object successor : node.successors()) {
                liveOutTemps.addAll(this.liveIn.get(successor));
            }

            return liveOutTemps;

        }

        private Set<Temp> internalLiveIn(SimpleGraph.Node node, Set<Temp> liveOutList) {
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
