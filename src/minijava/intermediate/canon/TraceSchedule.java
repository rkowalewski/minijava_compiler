package minijava.intermediate.canon;

import minijava.intermediate.Fragment;
import minijava.intermediate.FragmentProc;
import minijava.intermediate.FragmentVisitor;
import minijava.intermediate.Label;
import minijava.intermediate.tree.*;

import java.util.*;

/**
 * User: kowa
 * Date: 11/19/14
 */
public class TraceSchedule implements FragmentVisitor<BasicBlock.BasicBlockList, Fragment<List<TreeStm>>> {
    @Override
    public Fragment<List<TreeStm>> visit(FragmentProc<BasicBlock.BasicBlockList> fragProc) {
        TraceScheduleProcessor proc = new TraceScheduleProcessor(fragProc.body);
        return new FragmentProc<>(fragProc.frame, proc.scheduleBasicBlocks());
    }

    private static class TraceScheduleProcessor {
        private Map<Label, List<TreeStm>> labelBlockDict;
        private LinkedList<TreeStm> statements = new LinkedList();
        private LinkedList<List<TreeStm>> basicBlocks;
        private BasicBlock.BasicBlockList basicBlockList;

        private TraceScheduleProcessor(BasicBlock.BasicBlockList basicBlockList) {
            this.basicBlockList = basicBlockList;
            this.labelBlockDict = new HashMap<>();
            this.basicBlocks = new LinkedList<>(basicBlockList.blocks);
        }

        public List<TreeStm> scheduleBasicBlocks() {
            for (List<TreeStm> block : basicBlocks) {
                labelBlockDict.put(((TreeStmLABEL) block.get(0)).label, block);
            }

            List<TreeStm> statementList = processNextBlock();

            omitJumps(statementList);

            return statementList;
        }

        private void omitJumps(List<TreeStm> processedBlocks) {
            if (processedBlocks.size() > 1) {
                for (int i = 1; i < processedBlocks.size(); i++) {
                    TreeStm current = processedBlocks.get(i);
                    TreeStm last = processedBlocks.get(i-1);
                    if (current instanceof TreeStmLABEL && last instanceof TreeStmJUMP) {
                        TreeStmLABEL stmLabel = (TreeStmLABEL) current;
                        TreeStmJUMP stmJump = (TreeStmJUMP) last;

                        if (stmJump.poss.size() == 1 && stmLabel.label.equals(stmJump.poss.get(0))) {
                            processedBlocks.remove(i-1);
                            i -= 1;
                        }
                    }
                }
            }
        }

        private List<TreeStm> processNextBlock() {

            if (basicBlocks.isEmpty()) {
                statements.add(new TreeStmLABEL(basicBlockList.done));
            } else {
                List<TreeStm> block = basicBlocks.removeFirst();
                TreeStmLABEL stmLabel = (TreeStmLABEL) block.get(0);
                if (labelBlockDict.containsKey(stmLabel.label)) {
                    trace(block);
                } else {
                    processNextBlock();
                }
            }

            return statements;
        }

        private void trace(List<TreeStm> block) {
            labelBlockDict.remove(((TreeStmLABEL) block.get(0)).label);

            this.statements.addAll(block);

            TreeStm last = this.statements.getLast();

            if (last instanceof TreeStmJUMP) {
                TreeStmJUMP jump = (TreeStmJUMP) last;

                Label label = jump.poss.get(0);
                List<TreeStm> targetBlock = labelBlockDict.get(label);

                if (jump.poss.size() == 1 && targetBlock != null) {
                     trace(targetBlock);
                } else {
                    processNextBlock();
                }
            } else if (last instanceof TreeStmCJUMP) {
                TreeStmCJUMP cJump = (TreeStmCJUMP) last;
                List<TreeStm> blockTrue = labelBlockDict.get(cJump.ltrue);
                List<TreeStm> blockFalse = labelBlockDict.get(cJump.lfalse);

                if (!blockFalse.isEmpty()) {
                    trace(blockFalse);
                } else if (!blockTrue.isEmpty()) {
                    TreeStm newCJump = new TreeStmCJUMP(cJump.rel.neg(), cJump.left, cJump.right, cJump.lfalse, cJump.ltrue);
                    statements.set(statements.size() - 1, newCJump);
                    trace(blockTrue);
                } else {
                    Label ff = new Label();
                    //replace last statement with new CJUMP
                    statements.set(statements.size() - 1, new TreeStmCJUMP(cJump.rel, cJump.left, cJump.right, cJump.ltrue, ff));
                    statements.add(new TreeStmLABEL(ff));
                    statements.add(new TreeStmJUMP(new TreeExpNAME(cJump.lfalse), Arrays.asList(cJump.lfalse)));
                    processNextBlock();
                }
            } else {
                throw new IllegalStateException("Bad Basic Block in Trace Schedule!");
            }
        }
    }
}
