package minijava.backend.i386;

import minijava.backend.Assem;
import minijava.intermediate.Fragment;
import minijava.intermediate.FragmentProc;
import minijava.intermediate.FragmentVisitor;
import minijava.intermediate.Temp;
import minijava.intermediate.tree.*;

import java.util.ArrayList;
import java.util.List;

/**
 * User: kowa
 * Date: 11/25/14
 */
public class CodegenVisitor implements FragmentVisitor<List<TreeStm>, Fragment<List<Assem>>> {

    @Override
    public Fragment<List<Assem>> visit(FragmentProc<List<TreeStm>> fragProc) {

        List<Assem> assemList = new ArrayList<>();

        ArrayList<TreeStm> treeStms = new ArrayList<>(fragProc.body);
        for (TreeStm stm : treeStms) {
            assemList.addAll(new AssemProcessor().munchStm(stm));
        }

        return new FragmentProc<>(fragProc.frame, assemList);
    }

    private static class AssemProcessor {
        private List<Assem> resultList = new ArrayList<>();

        public List<Assem> munchStm(TreeStm stm) {

            return munchStm(stm);
        }

        private void munchStm(TreeStmMOVE move) {
            if (move.dest instanceof TreeExpTEMP) {
                TreeExpTEMP dst = (TreeExpTEMP) move.dest;
                emit(new AssemBinaryOp(AssemBinaryOp.Kind.MOV, new Operand.Reg(dst.temp), munchExp(move.src, null)));
            } else if (move.dest instanceof TreeExpMEM) {
                TreeExpMEM mem = (TreeExpMEM) move.dest;

                //emit(new AssemBinaryOp(AssemBinaryOp.Kind.)munchExp(mem.addr));
            }
        }

        private void munchStm(TreeStmEXP move) {

        }

        private void munchStm(TreeStmJUMP move) {

        }

        private void munchStm(TreeStmCJUMP move) {

        }

        private void munchStm(TreeStmLABEL move) {
            resultList.add(new AssemLabel(move.label));
        }

        private Operand munchExp(TreeExp exp, Temp t) {
            return munchExp(exp, t);
        }

        private Operand munchExp(TreeExpNAME exp, Temp t) {
            return null;
        }

        private Operand munchExp(TreeExpOP exp, Temp t) {
            return null;
        }

        private Operand munchExp(TreeExpCALL exp, Temp t) {
            return null;
        }

        private Operand munchExp(TreeExpMEM exp, Temp t) {
            return null;
        }

        private Operand munchExp(TreeExpTEMP exp, Temp t) {
            return null;
        }

        private Operand munchExp(TreeExpCONST exp, Temp t) {
            return null;
        }



        private void emit(Assem assem) {
            resultList.add(assem);
        }
    }
}
