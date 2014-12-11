package minijava.backend.regalloc;

import minijava.backend.Assem;
import minijava.intermediate.Label;
import minijava.intermediate.Temp;
import minijava.util.SimpleGraph;

import java.util.*;

/**
 * User: kowa
 * Date: 12/11/14
 */
public class AssemFlowGraph extends SimpleGraph<Assem> {

    public AssemFlowGraph(List<Assem> body) {
        SimpleGraph.Node prev = null;
        SimpleGraph.Node cur;

        //First: Put all instruction in graph add add edges
        for (Assem instr : body) {
            cur = this.new Node(instr);

            if (prev != null && ((Assem) prev.info).isFallThrough()) {
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

    public SimpleGraph<Assem> getInterenceGraph() {
        LivenessInfo livenessInfo = new LivenessInfo();
        livenessInfo.analyze();

        return this;
    }

    protected List<Temp> defList(Node node) {
        return node.info.def();
    }

    protected List<Temp> useList(Node node) {
        return node.info.use();
    }


    private class LivenessInfo {
        private Map<Node, Set<Temp>> liveIn = new HashMap<>();
        private Map<Node, Set<Temp>> liveOut = new HashMap<>();

        public void analyze() {

            boolean isChanged = true;

            while(isChanged) {
                for (SimpleGraph.Node node : nodeSet()) {
                    Set<Temp> in = internalLiveIn(node);
                    Set<Temp> out = internalLiveOut(node);

                    isChanged = !(in.equals(liveIn.get(node)) && out.equals(liveOut.get(node)));

                    liveIn.put(node, in);
                    liveOut.put(node, out);
                }
            }

        }

        private Set<Temp> internalLiveOut(SimpleGraph.Node node) {
            // out[n] = U_{s in succ[n]} in[s]
            Set<Temp> liveOut = new HashSet<>();

            for (Object successor : node.successors()) {
                liveOut.addAll(internalLiveIn((SimpleGraph.Node) successor));
            }

            return liveOut;

        }

        private Set<Temp> internalLiveIn(SimpleGraph.Node node) {
            // (out[n] - def[n])
            Set<Temp> liveOut = internalLiveOut(node);
            for (Temp temp : defList(node)) {
                liveOut.remove(temp);
            }

            // use[n] U (out[n] - def[n])
            for (Temp temp : useList(node)) {
                liveOut.add(temp);
            }

            return liveOut;
        }

    }
}
