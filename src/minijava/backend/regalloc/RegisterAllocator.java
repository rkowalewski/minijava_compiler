package minijava.backend.regalloc;

import minijava.backend.Assem;
import minijava.backend.MachineSpecifics;
import minijava.intermediate.Frame;
import minijava.intermediate.Temp;
import minijava.util.FiniteFunction;
import minijava.util.Function;
import minijava.util.Pair;
import minijava.util.SimpleGraph;

import java.util.*;

/**
 * User: kowa
 * Date: 12/18/14
 */
public class RegisterAllocator {
    private final Deque<InterferenceGraph.Node> stack = new ArrayDeque<>();
    private final Set<InterferenceGraph.Node> simplifyWorklist = new HashSet<>();
    private final Set<InterferenceGraph.Node> spillCandidates = new HashSet<>();
    private final Set<InterferenceGraph.Node> spilledNodes = new HashSet<>();
    private final Frame frame;
    private final MachineSpecifics machineSpecifics;
    private List<Assem> instructions;
    private InterferenceGraph graph;
    private final Map<Temp, Temp> registerMappings = new HashMap<>();
    private boolean isFinished = false;

    public RegisterAllocator(List<Assem> instructions, Frame frame, MachineSpecifics machineSpecifics) {
        this.instructions = instructions;
        this.frame = frame;
        this.machineSpecifics = machineSpecifics;
    }


    public List<Assem> doRegAlloc() {
        do {
            build();
            makeWorkLists();

            do {
                simplify();
                checkSpillCandidates();
            } while (!simplifyWorklist.isEmpty() || !spillCandidates.isEmpty());

            select();

            if (!spilledNodes.isEmpty()) {
                this.instructions = machineSpecifics.spill(frame, this.instructions, spills());
            }

        } while (!spilledNodes.isEmpty());

        List<Assem> finalBody = new ArrayList<>();
        Function<Temp, Temp> sigma = new FiniteFunction<>(registerMappings);

        for (Assem instr : instructions) {
            Assem renamed = instr.rename(sigma);

            Pair<Temp, Temp> move = renamed.isMoveBetweenTemps();

            if (move == null || !move.fst.equals(move.snd)) {
                finalBody.add(renamed);
            }
        }

        return Collections.unmodifiableList(finalBody);
    }

    private List<Temp> spills() {
        List<Temp> spills = new ArrayList<>();
        for (InterferenceGraph.Node spilled : spilledNodes) {
            spills.add(spilled.info);
        }

        return spills;
    }

    private void simplify() {
        for (InterferenceGraph.Node simple : simplifyWorklist) {
            addToStack(simple);
        }

        simplifyWorklist.clear();
    }

    private void addToStack(SimpleGraph<Temp>.Node node) {
        node.toggleActive();
        stack.push(node);
    }


    private void checkSpillCandidates() {
        if (!spillCandidates.isEmpty()) {
            List<InterferenceGraph.Node> listToSort = new ArrayList<>(spillCandidates);
            Collections.sort(listToSort, new NeighboursDegreeComparator());

            //Push Node with highest Degree on Stack
            InterferenceGraph.Node highestDegree = listToSort.get(0);
            addToStack(highestDegree);
            spillCandidates.remove(highestDegree);

            Iterator<InterferenceGraph.Node> itCandidates = spillCandidates.iterator();

            //Check if others can be simplified
            while (itCandidates.hasNext()) {
                InterferenceGraph.Node node = itCandidates.next();
                if (node.degree() < this.machineSpecifics.getGeneralPurposeRegisters().length) {
                    //add to worklist for simplify
                    simplifyWorklist.add(node);
                    //remove from spillCandidates
                    itCandidates.remove();
                }
            }
        }

    }

    private void makeWorkLists() {
        this.stack.clear();
        this.spilledNodes.clear();
        this.spillCandidates.clear();

        for (InterferenceGraph.Node node : this.graph.nodeSet()) {
            if (!node.info.isFixedColor()) {
                if (node.degree() < machineSpecifics.getGeneralPurposeRegisters().length) {
                    simplifyWorklist.add(node);
                } else {
                    spillCandidates.add(node);
                }
            }
        }
    }

    private void build() {
        AssemFlowGraph flowGraph = new AssemFlowGraph(instructions);
        this.graph = flowGraph.getInterenceGraph();
    }

    private void select() {
        while (!stack.isEmpty()) {
            InterferenceGraph.Node graphEl = stack.pop();

            List<Temp> useableRegs = new ArrayList<>(Arrays.asList(this.machineSpecifics.getGeneralPurposeRegisters()));

            for (InterferenceGraph.Node neighbour : graphEl.neighbours()) {
                if (neighbour.info.isFixedColor()) {
                    useableRegs.remove(neighbour.info);
                } else if (registerMappings.containsKey(neighbour.info)) {
                    useableRegs.remove(registerMappings.get(neighbour.info));
                }
            }

            if (!useableRegs.isEmpty()) {
                registerMappings.put(graphEl.info, useableRegs.get(0));
            } else {
                spilledNodes.add(graphEl);
            }

            graphEl.toggleActive();
        }
    }


    private class NeighboursDegreeComparator implements Comparator<InterferenceGraph.Node> {
        @Override
        public int compare(InterferenceGraph.Node o1, InterferenceGraph.Node o2) {
            if (o1.info.isFixedColor() && o2.info.isFixedColor()) {
                return 0;
            }

            if (o2.info.isFixedColor()) {
                return Integer.MAX_VALUE;
            }

            if (o1.info.isFixedColor()) {
                return Integer.MIN_VALUE;
            }

            return o2.degree() - o1.degree();
        }
    }

    public Map<Temp, Temp> getRegisterMappings() {
        return isFinished ? Collections.unmodifiableMap(registerMappings) : Collections.<Temp, Temp>emptyMap();
    }
}
