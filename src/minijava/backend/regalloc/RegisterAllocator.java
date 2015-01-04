package minijava.backend.regalloc;

import minijava.backend.Assem;
import minijava.intermediate.Temp;

import java.util.*;

/**
 * User: kowa
 * Date: 12/18/14
 */
public class RegisterAllocator {
    private final InterferenceGraph graph;
    private final Deque<InterferenceGraph.Node> stack = new ArrayDeque<>();
    private final int numberOfColors = 8;
    private final List<Temp> generalPurposeRegisters;
    private final Map<Temp, Temp> registerMappings = new HashMap<>();
    private boolean isFinished = false;

    public RegisterAllocator(List<Assem> body, List<Temp> generalPurposeRegisters) {
        AssemFlowGraph flowGraph = new AssemFlowGraph(body);
        this.graph = flowGraph.getInterenceGraph();
        this.generalPurposeRegisters = generalPurposeRegisters;
    }

    public List<Temp> doRegAlloc() {
        simplify(graph.nodeSet());
        return select();
    }

    private List<Temp> select() {
        List<Temp> spilledTemps = new ArrayList<>();
        while (!stack.isEmpty()) {
            InterferenceGraph.Node graphEl = stack.pop();

            if (!graphEl.info.isFixedColor()) {
                List<Temp> useableRegs = new ArrayList<>(generalPurposeRegisters);


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
                    spilledTemps.add(graphEl.info);
                }

                graphEl.toggleActive();
            }
        }

        this.isFinished = spilledTemps.isEmpty();
        return spilledTemps;
    }

    private void simplify(Collection<InterferenceGraph.Node> nodesToColor) {
        Set<InterferenceGraph.Node> nodes = new HashSet<>(nodesToColor);
        Iterator<InterferenceGraph.Node> itGraphNodes = nodes.iterator();

        while (itGraphNodes.hasNext()) {
            InterferenceGraph.Node cur = itGraphNodes.next();

            if (cur.info.isFixedColor()) {
                continue;
            }

            if (cur.degree() < numberOfColors) {
                stack.push(cur);
                cur.toggleActive();
                itGraphNodes.remove();
            }
        }

        if (nodes.size() > 0) {
            //sort in descending order by nodes degree
            List<InterferenceGraph.Node> significantDegreeNodes = new ArrayList<>(nodes);
            Collections.sort(significantDegreeNodes, new NeighboursDegreeComparator());

            boolean modified = false;

            for (InterferenceGraph.Node node : significantDegreeNodes) {
                if (!node.info.isFixedColor()) {
                    stack.push(node);
                    node.toggleActive();
                    nodes.remove(node);
                    modified = true;
                    simplify(nodes);
                    break;
                }
            }

            if (!modified) {
                for (InterferenceGraph.Node preColored : significantDegreeNodes) {
                    stack.push(preColored);
                }
            }
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
        return isFinished ? Collections.unmodifiableMap(registerMappings): Collections.<Temp, Temp>emptyMap();
    }
}
