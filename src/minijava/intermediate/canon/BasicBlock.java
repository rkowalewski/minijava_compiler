package minijava.intermediate.canon;

import minijava.intermediate.Fragment;
import minijava.intermediate.FragmentProc;
import minijava.intermediate.FragmentVisitor;
import minijava.intermediate.Label;
import minijava.intermediate.tree.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * User: kowa
 * Date: 11/19/14
 */
public class BasicBlock implements FragmentVisitor<List<TreeStm>, Fragment<BasicBlockList>> {

    @Override
    public Fragment<BasicBlockList> visit(FragmentProc<List<TreeStm>> fragProc) {
        BasicBlocksProcessor proc = new BasicBlocksProcessor();
        return new FragmentProc<>(fragProc.frame, proc.buildBasicBlocks(new LinkedList(fragProc.body)));
    }

    private static class BasicBlocksProcessor {
        private final List<List<TreeStm>> blocks = new LinkedList<>();
        private final Label done = new Label();
        private LinkedList<TreeStm> currentBlock;

        public BasicBlockList buildBasicBlocks(LinkedList<TreeStm> stms) {
            if (!stms.isEmpty()) {
                if (stms.getFirst() instanceof TreeStmLABEL) {
                    if (currentBlock == null) {
                        currentBlock = new LinkedList<>();
                        currentBlock.add(stms.removeFirst());
                        blocks.add(currentBlock);
                    }  else {
                        LinkedList<TreeStm> temp = new LinkedList<>();
                        temp.add(stms.removeFirst());
                        blocks.add(temp);
                        currentBlock = temp;
                    }

                    processStms(stms);
                } else {
                    LinkedList<TreeStm> stmsWithLabel = new LinkedList(stms);
                    stmsWithLabel.addFirst(new TreeStmLABEL(new Label()));
                    buildBasicBlocks(stmsWithLabel);
                }
            }

            return new BasicBlockList(blocks, done);
        }

        private void processStms(LinkedList<TreeStm> l) {
            if (l.isEmpty()) {
                l.add(new TreeStmJUMP(new TreeExpNAME(done), Arrays.asList(done)));
                processStms(l);
            } else if (l.getFirst() instanceof TreeStmJUMP || l.getFirst() instanceof TreeStmCJUMP) {
                currentBlock.add(l.removeFirst());
                buildBasicBlocks(l);
            } else if (l.getFirst() instanceof TreeStmLABEL) {
                Label label = ((TreeStmLABEL) l.getFirst()).label;
                l.addFirst(new TreeStmJUMP(new TreeExpNAME(label), Arrays.asList(label)));
                processStms(new LinkedList<>(l));
            } else {
                currentBlock.add(l.removeFirst());
                processStms(l);
            }
        }

    }
}
