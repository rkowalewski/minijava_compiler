package minijava.backend.regalloc;

import minijava.backend.Assem;
import minijava.backend.MachineSpecifics;
import minijava.intermediate.FragmentProc;
import minijava.intermediate.Frame;
import minijava.intermediate.Temp;
import minijava.util.FiniteFunction;
import minijava.util.Function;

import java.util.*;

/**
 * User: kowa
 * Date: 12/18/14
 */
public class RegisterAllocator {
    private final MachineSpecifics machineSpecifics;
    private final Frame frame;
    private List<Assem> body;
    private AssemFlowGraph fg;
    private InterferenceGraph ig;
    private Deque<InterferenceGraph.Node> stack = new ArrayDeque<>();
    private Set<InterferenceGraph.Node> coloredNodes = new HashSet<>();
    private Map<InterferenceGraph.Node, Integer> degrees = new HashMap<>();
    private List<InterferenceGraph.Node> spillWorklist = new ArrayList<>();
    private List<InterferenceGraph.Node> freezeWorklist = new ArrayList<>();
    private List<InterferenceGraph.Node> simplifyWorklist = new ArrayList<>();
    private List<InterferenceGraph.Node> coalescedNodes = new ArrayList<>();
    private List<InterferenceGraph.Node> spilledNodes = new ArrayList<>();

    private List<Assem> workListMoves = new ArrayList<>();
    private List<Assem> activeMoves = new ArrayList<>();
    private List<Assem> coalescedMoves = new ArrayList<>();
    private List<Assem> constrainedMoves = new ArrayList<>();
    private List<Assem> frozenMoves = new ArrayList<>();

    private Map<InterferenceGraph.Node, InterferenceGraph.Node> alias = new HashMap<>();
    private final NodeInstructionsMap moveNodes = new NodeInstructionsMap();
    private final Map<Temp, Temp> registerMappings = new HashMap<>();


    public RegisterAllocator(List<Assem> body, MachineSpecifics machineSpecifics, Frame frame) {
        this.machineSpecifics = machineSpecifics;
        this.body = body;
        this.frame = frame;
    }

    public FragmentProc<List<Assem>> doRegAlloc() {

        do {
            this.fg = new AssemFlowGraph(body);
            this.ig = fg.getInterenceGraph();

            makeLists();

            do {
                if (!simplifyWorklist.isEmpty()) {
                    simplify();
                } else if (!workListMoves.isEmpty()) {
                    coalesce();
                } else if (!freezeWorklist.isEmpty()) {
                    freeze();
                } else if (!spillWorklist.isEmpty()) {
                    selectSpill();
                }

            } while (!simplifyWorklist.isEmpty() || !workListMoves.isEmpty() ||
                    !freezeWorklist.isEmpty() || !spillWorklist.isEmpty());

            assignColors();
            if (!spilledNodes.isEmpty()) {
               this.body = machineSpecifics.spill(frame, body, spills());
            }
        } while (!spilledNodes.isEmpty());

        Function<Temp, Temp> sigma = new FiniteFunction<>(registerMappings);

        List<Assem> finalBody = new ArrayList<>();

        for (Assem assem : body) {

            Assem renamed = assem.rename(sigma);

            finalBody.add(renamed);
        }

        FragmentProc<List<Assem>> assemFrag = new FragmentProc<>(frame, finalBody);

        return assemFrag;
    }

    private void assignColors() {
        while (!stack.isEmpty()) {
            InterferenceGraph.Node n = stack.pop();
            Set<Temp> okColors = new HashSet<>(Arrays.asList(machineSpecifics.getGeneralPurposeRegisters()));

            if (getAlias(n).info.isFixedColor()) {
                continue;
            }

            for (InterferenceGraph.Node w : n.neighbours()) {
                HashSet<InterferenceGraph.Node> used = new HashSet<>(coloredNodes);

                if (used.contains(getAlias(w))) {
                    okColors.remove(getAlias(w).info.temp);
                }
            }

            if (okColors.isEmpty()) {
                spilledNodes.add(n);
            } else {
                coloredNodes.add(n);

                registerMappings.put(n.info.temp, okColors.iterator().next());
            }
        }
    }

    private void freeze() {
        if (!freezeWorklist.isEmpty()) {
            InterferenceGraph.Node n = freezeWorklist.remove(0);
            simplifyWorklist.add(n);
            freezeMoves(n);
        }
    }

    public void selectSpill() {
        if (!spillWorklist.isEmpty()) {
            InterferenceGraph.Node less = spillWorklist.get(0);
            for (InterferenceGraph.Node n : spillWorklist) {
                if (degrees.get(n) > degrees.get(less))
                    less = n;
            }

            spillWorklist.remove(less);
            simplifyWorklist.add(less);
            freezeMoves(less);
        }
    }

    private void freezeMoves(InterferenceGraph.Node n) {
        for (Assem move : moveInstructionsForNode(n)) {
            InterferenceGraph.Node x = ig.getNodeByTemp(move.def().get(0));
            InterferenceGraph.Node y = ig.getNodeByTemp(move.use().get(0));

            InterferenceGraph.Node v;
            if (getAlias(x) == getAlias(n))
                v = getAlias(x);
            else v = getAlias(y);

            activeMoves.remove(move);
            frozenMoves.add(move);
            if (freezeWorklist.contains(v) && moveInstructionsForNode(v).isEmpty()) {
                freezeWorklist.remove(v);
                simplifyWorklist.add(v);
            }
        }
    }

    private void coalesce() {
        if (!workListMoves.isEmpty()) {
            InterferenceGraph.Node u, v;

            Assem move = workListMoves.get(0);
            InterferenceGraph.Node x = getAlias(ig.getNodeByTemp(move.def().get(0)));
            InterferenceGraph.Node y = getAlias(ig.getNodeByTemp(move.use().get(0)));

            if (y.info.isFixedColor()) {
                u = y;
                v = x;
            } else {
                u = x;
                v = y;
            }
            workListMoves.remove(0);
            List<InterferenceGraph.Node> list = new ArrayList<>(u.neighbours());
            list.addAll(v.neighbours());

            if (u == v) {
                coalescedMoves.add(move);
                addWorkList(u);
            } else if (v.info.isFixedColor() || u.neighbours().contains(v)) {
                constrainedMoves.add(move);
                addWorkList(u);
                addWorkList(v);
            } else if ((u.info.isFixedColor() && georgeTestOK(v, u)) || (!u.info.isFixedColor() && conservativeTest(list))) {
                coalescedMoves.add(move);
                combine(u, v);
                addWorkList(u);
            } else {
                activeMoves.add(move);
            }
        }
    }

    private void combine(InterferenceGraph.Node u, InterferenceGraph.Node v) {
        if (freezeWorklist.contains(v)) {
            freezeWorklist.remove(v);
        } else {
            spillWorklist.remove(v);
        }

        coalescedNodes.add(v);
        alias.put(v, u);

        // moveList[u] ← moveList[u] ∪ moveList[v]
        moveNodes.get(u).addAll(moveNodes.get(v));
        enableMoves(Collections.singletonList(v));

        for (InterferenceGraph.Node n : v.neighbours()) {
            ig.addEdge(n, u);
            decrementDegree(n);
        }

        if (degrees.get(u) >= machineSpecifics.getGeneralPurposeRegisters().length && freezeWorklist.contains(u)) {
            freezeWorklist.remove(u);
            spillWorklist.add(u);
        }

    }

    private boolean conservativeTest(List<InterferenceGraph.Node> list) {
        int k = 0;
        for (InterferenceGraph.Node n : list) {
            if (degrees.get(n) >= machineSpecifics.getGeneralPurposeRegisters().length)
                k++;
        }

        return k < machineSpecifics.getGeneralPurposeRegisters().length;
    }

    public boolean georgeTestOK(InterferenceGraph.Node v, InterferenceGraph.Node u) {
        for (InterferenceGraph.Node n : v.neighbours()) {
            if (!(degrees.get(n) < machineSpecifics.getGeneralPurposeRegisters().length || n.info.isFixedColor() || n.neighbours().contains(u))) {
                return false;
            }
        }
        return true;
    }

    private void addWorkList(InterferenceGraph.Node n) {
        if (!n.info.isFixedColor() && !isMoveRelated(n) && degrees.get(n) < machineSpecifics.getGeneralPurposeRegisters().length) {
            freezeWorklist.remove(n);
            simplifyWorklist.add(n);
        }
    }

    private void simplify() {
        if (simplifyWorklist.isEmpty()) return;

        InterferenceGraph.Node simpleNode = simplifyWorklist.remove(0);
        stack.push(simpleNode);
        //disable this node in graph to decrement the degree of all neighbours
        for (InterferenceGraph.Node neighbor : simpleNode.neighbours()) {
            decrementDegree(neighbor);
        }
    }

    private void decrementDegree(InterferenceGraph.Node n) {
        if (n.info.isFixedColor()) return;

        int d = degrees.get(n);

        degrees.put(n, d-1);

        if (d == machineSpecifics.getGeneralPurposeRegisters().length) {
            List<InterferenceGraph.Node> list = new ArrayList<>(n.neighbours());
            list.add(n);
            enableMoves(list);
            spillWorklist.remove(n);

            if (isMoveRelated(n))
                freezeWorklist.add(n);
            else
                simplifyWorklist.add(n);
        }
    }

    private void enableMoves(List<InterferenceGraph.Node> nodes) {
        for (InterferenceGraph.Node n : nodes) {
            for (Assem move : moveInstructionsForNode(n)) {
                if (activeMoves.remove(move))
                    workListMoves.add(move);
            }
        }
    }

    private List<Temp> spills() {
        List<Temp> spills = new ArrayList<>();

        for (InterferenceGraph.Node spilled : spilledNodes) {
            spills.add(spilled.info.temp);
        }

        return spills;
    }

    private void makeLists() {
        for (Assem move : fg.getMoves()) {
            workListMoves.add(move);

            moveNodes.addAssemToNode(ig.getNodeByTemp(move.use().get(0)), move);
            moveNodes.addAssemToNode(ig.getNodeByTemp(move.use().get(0)), move);
        }

        for (InterferenceGraph.Node node : ig.nodeSet()) {
            degrees.put(node, node.degree());
            if (node.degree() >= machineSpecifics.getGeneralPurposeRegisters().length) {
                spillWorklist.add(node);
            } else if (isMoveRelated(node)) {
                freezeWorklist.add(node);
            } else {
                simplifyWorklist.add(node);
            }
        }
    }

    private boolean isMoveRelated(InterferenceGraph.Node node) {
        return !moveInstructionsForNode(node).isEmpty();
    }

    private Set<Assem> moveInstructionsForNode(InterferenceGraph.Node n) {
        HashSet<Assem> s = new HashSet<>(activeMoves);

        s.addAll(workListMoves);
        s.retainAll(moveNodes.get(n));

        return s;
    }

    private InterferenceGraph.Node getAlias(InterferenceGraph.Node n) {
        if (coalescedNodes.contains(n)) {
            return getAlias(alias.get(n));
        } else {
            return n;
        }
    }

    private class NodeInstructionsMap extends HashMap<InterferenceGraph.Node, Set<Assem>> {
        public void addAssemToNode(InterferenceGraph.Node node, Assem assem) {
            if (moveNodes.containsKey(node)) {
                moveNodes.get(node).add(assem);
            } else {
                Set<Assem> initial = new HashSet<>();
                initial.add(assem);
                moveNodes.put(node, initial);
            }

        }
    }


}
