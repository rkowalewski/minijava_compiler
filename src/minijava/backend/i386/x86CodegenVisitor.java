package minijava.backend.i386;

import minijava.backend.Assem;
import minijava.intermediate.*;
import minijava.intermediate.tree.*;

import java.util.ArrayList;
import java.util.List;

/**
 * User: kowa
 * Date: 11/25/14
 */
public class x86CodegenVisitor implements FragmentVisitor<List<TreeStm>, Fragment<List<Assem>>> {
    private static List<Assem> prologue = new ArrayList<>();
    private static List<Assem> epilogue = new ArrayList<>();

    static {
        prologue.add(new AssemUnaryOp(AssemUnaryOp.Kind.PUSH, new Operand.Reg(x86Frame.ebp)));
        prologue.add(new AssemBinaryOp(AssemBinaryOp.Kind.MOV, new Operand.Reg(x86Frame.ebp), new Operand.Reg(x86Frame.esp)));
        prologue.add(new AssemUnaryOp(AssemUnaryOp.Kind.PUSH, new Operand.Reg(x86Frame.ebx)));
        prologue.add(new AssemUnaryOp(AssemUnaryOp.Kind.PUSH, new Operand.Reg(x86Frame.edi)));
        prologue.add(new AssemUnaryOp(AssemUnaryOp.Kind.PUSH, new Operand.Reg(x86Frame.esi)));

        epilogue.add(new AssemUnaryOp(AssemUnaryOp.Kind.POP, new Operand.Reg(x86Frame.esi)));
        epilogue.add(new AssemUnaryOp(AssemUnaryOp.Kind.POP, new Operand.Reg(x86Frame.edi)));
        epilogue.add(new AssemUnaryOp(AssemUnaryOp.Kind.POP, new Operand.Reg(x86Frame.ebx)));
        epilogue.add(new AssemBinaryOp(AssemBinaryOp.Kind.MOV, new Operand.Reg(x86Frame.esp), new Operand.Reg(x86Frame.ebp)));
        epilogue.add(new AssemUnaryOp(AssemUnaryOp.Kind.POP, new Operand.Reg(x86Frame.ebp)));
        epilogue.add(new AssemInstr(AssemInstr.Kind.RET));
    }

    @Override
    public Fragment<List<Assem>> visit(FragmentProc<List<TreeStm>> fragProc) {

        List<Assem> assemList = new ArrayList<>();

        ArrayList<TreeStm> treeStms = new ArrayList<>(fragProc.body);

        AssemProcessor assemProcessor = new AssemProcessor((x86Frame) fragProc.frame);

        for (TreeStm stm : treeStms) {
            assemList.addAll(assemProcessor.munchStm(stm));
        }

        assemList.addAll(0, prologue);
        assemList.addAll(epilogue);

        assemList.add(0, new AssemLabel(fragProc.frame.getName()));

        return new FragmentProc<>(fragProc.frame, assemList);
    }

    private class AssemProcessor {
        private final x86Frame x86Frame;
        private List<Assem> resultList = new ArrayList<>();

        private AssemProcessor(x86Frame x86Frame) {
            this.x86Frame = x86Frame;
        }

        public List<Assem> munchStm(TreeStm stm) {
            this.resultList = new ArrayList<>();

            if (stm instanceof TreeStmLABEL) {
                munchStm((TreeStmLABEL)stm);
            }
            else if (stm instanceof TreeStmJUMP) {
                munchStm((TreeStmJUMP)stm);
            }
            else if (stm instanceof TreeStmCJUMP) {
                munchStm((TreeStmCJUMP)stm);
            }
            else if (stm instanceof TreeStmMOVE) {
                munchStm((TreeStmMOVE)stm);
            }
            else if (stm instanceof TreeStmEXP) {
                munchStm((TreeStmEXP)stm);
            }

            return resultList;
        }

        private void munchStm(TreeStmMOVE move) {
            if (move.dest instanceof TreeExpTEMP) {
                if (move.src instanceof TreeExpCONST) {
                    emit(new AssemBinaryOp(AssemBinaryOp.Kind.MOV, new Operand.Reg(((TreeExpTEMP) move.dest).temp), new Operand.Imm(((TreeExpCONST) move.src).value)));
                } else {
                    emit(new AssemBinaryOp(AssemBinaryOp.Kind.MOV, new Operand.Reg(((TreeExpTEMP) move.dest).temp), new Operand.Reg(munchExp(move.src))));
                }

            } else if (move.dest instanceof TreeExpMEM) {
                TreeExpMEM mem = (TreeExpMEM) move.dest;

                if (move.src instanceof TreeExpCONST) {
                    emit(new AssemBinaryOp(AssemBinaryOp.Kind.MOV, new Operand.Reg(munchExp(move.dest)), new Operand.Imm(((TreeExpCONST) move.src).value)));
                } else {
                    emit(new AssemBinaryOp(AssemBinaryOp.Kind.MOV, new Operand.Reg(munchExp(move.dest)), new Operand.Reg(munchExp(move.src))));
                }
            } else {
                throw new IllegalArgumentException("the target of a MOV instruction must be either a register or a Memory Address!");
            }
        }

        private void munchStm(TreeStmEXP move) {
            munchExp(move.exp);
        }

        private void munchStm(TreeStmJUMP move) {
            if (move.poss.size() > 1) {
                throw new IllegalStateException("cannot handle a jump with multiple target labels");
            }

            emit(new AssemJump(AssemJump.Kind.JMP, move.poss.get(0)));
        }

        private void munchStm(TreeStmCJUMP cjump) {
            if (cjump.right instanceof TreeExpCONST) {
                int value = ((TreeExpCONST) cjump.right).value;
                emit(new AssemBinaryOp(AssemBinaryOp.Kind.CMP, new Operand.Reg(munchExp(cjump.left)), new Operand.Imm(value)));

                if (cjump.rel == TreeStmCJUMP.Rel.EQ && value == 0) {
                    emit(new AssemJump(AssemJump.Kind.J, cjump.ltrue, AssemJump.Cond.Z));
                    return;
                }
            } else {
                emit(new AssemBinaryOp(AssemBinaryOp.Kind.CMP, new Operand.Reg(munchExp(cjump.left)), new Operand.Reg(munchExp(cjump.right))));
            }

            AssemJump.Cond condition = null;

            switch(cjump.rel) {
                case EQ:
                    condition = AssemJump.Cond.E;
                    break;
                case NE:
                    condition = AssemJump.Cond.NE;
                    break;
                case LT:
                    condition = AssemJump.Cond.L;
                    break;
                case GT:
                    condition = AssemJump.Cond.G;
                    break;
                case LE:
                    condition = AssemJump.Cond.LE;
                    break;
                case GE:
                    condition = AssemJump.Cond.GE;
                    break;
                default:
                    break;
            }

            emit(new AssemJump(AssemJump.Kind.J, cjump.ltrue, condition));
        }

        private void munchStm(TreeStmLABEL move) {
            resultList.add(new AssemLabel(move.label));
        }

        private Temp munchExp(TreeExp exp) {
            if (exp instanceof TreeExpCONST) {
                return munchExp((TreeExpCONST) exp);
            } else if (exp instanceof TreeExpOP) {
                return munchExp((TreeExpOP) exp);
            }
            else if (exp instanceof TreeExpCALL) {
                return munchExp((TreeExpCALL) exp);
            }
            else if (exp instanceof TreeExpMEM) {
                return munchExp((TreeExpMEM) exp);
            }
            else if (exp instanceof TreeExpTEMP) {
                return munchExp((TreeExpTEMP) exp);
            }
            else if (exp instanceof TreeExpNAME) {
                return munchExp((TreeExpNAME) exp);
            }

            return null;
        }

        private Temp munchExp(TreeExpNAME exp) {
            return null;
        }

        private Temp munchExp(TreeExpOP exp) {
            AssemBinaryOp.Kind binaryOp = null;
            AssemUnaryOp.Kind unaryOp = null;

            switch (exp.op) {
                case PLUS:
                    binaryOp = AssemBinaryOp.Kind.ADD;
                    break;
                case MINUS:
                    binaryOp = AssemBinaryOp.Kind.SUB;
                    break;
                case MUL:
                    unaryOp = AssemUnaryOp.Kind.IMUL;
                    break;
                case DIV:
                    unaryOp = AssemUnaryOp.Kind.IDIV;
                    break;
                case AND:
                    binaryOp = AssemBinaryOp.Kind.AND;
                    break;
                case OR:
                    binaryOp = AssemBinaryOp.Kind.OR;
                    break;
                case LSHIFT:
                    binaryOp = AssemBinaryOp.Kind.SHL;
                    break;
                case RSHIFT:
                    binaryOp = AssemBinaryOp.Kind.SHR;
                    break;
                case XOR:
                    binaryOp = AssemBinaryOp.Kind.XOR;
                    break;

                default:
                    break;
            }

            Temp dest = null;

            if (exp.left instanceof TreeExpMEM && exp.right instanceof TreeExpMEM) {
                dest = new Temp();
                emit(new AssemBinaryOp(AssemBinaryOp.Kind.MOV, new Operand.Reg(dest), new Operand.Reg(munchExp(exp.left))));
            }

            Operand right;

            if (exp.right instanceof TreeExpCONST) {
                right = new Operand.Imm(((TreeExpCONST) exp.right).value);
            } else {
                right = new Operand.Reg(munchExp(exp.right));
            }

            if (binaryOp != null) {
                dest = dest != null ? dest : munchExp(exp.left);
                emit(new AssemBinaryOp(binaryOp, new Operand.Reg(dest), right));
                return dest;
            } else if (unaryOp != null) {
                dest = dest != null ? dest : munchExp(exp.left);
                emit(new AssemBinaryOp(AssemBinaryOp.Kind.MOV, new Operand.Reg(x86Frame.eax), new Operand.Reg(dest)));
                emit(new AssemUnaryOp(unaryOp, right));
                return x86Frame.eax;
            } else {
                throw new IllegalArgumentException("cannot process binop!");
            }
        }

        private Temp munchExp(TreeExpCALL exp) {
            Label fnLabel = ((TreeExpNAME) exp.func).label;

            //Push args in reverse order
            for (int i = exp.args.size() - 1; i > -1; i--) {
                emit(new AssemUnaryOp(AssemUnaryOp.Kind.PUSH, new Operand.Reg(munchExp(exp.args.get(i)))));
            }

            emit(new AssemJump(AssemJump.Kind.CALL, fnLabel));

            if (exp.args.size() > 0) {
                emit(new AssemBinaryOp(AssemBinaryOp.Kind.ADD, new Operand.Reg(x86Frame.esp), new Operand.Imm(exp.args.size() * 4)));
            }

            return x86Frame.eax;
        }

        private Temp munchExp(TreeExpMEM exp) {
            if (exp.addr instanceof TreeExpTEMP) {
                return ((TreeExpTEMP) exp.addr).temp;
            }

            Temp reg = new Temp();

            if (exp.addr instanceof TreeExpOP) {
                TreeExpOP binOp = (TreeExpOP) exp.addr;

                if (binOp.op == TreeExpOP.Op.PLUS) {
                    Operand src;
                    if (binOp.left instanceof TreeExpCONST) {
                        if (binOp.right instanceof TreeExpOP) {
                            TreeExpOP baseIndexOP = (TreeExpOP) binOp.right;
                            if (baseIndexOP.left instanceof TreeExpOP) {
                                src = calcIndexScaleOp((TreeExpOP) baseIndexOP.left, baseIndexOP.right);
                            } else if (baseIndexOP.right instanceof TreeExpOP) {
                                src = calcIndexScaleOp((TreeExpOP) baseIndexOP.right, baseIndexOP.left);
                            } else {
                                src = new Operand.Mem(munchExp(binOp.left), null, munchExp(binOp.right), 0);
                            }
                        } else {
                            src = new Operand.Mem(munchExp(binOp.right), null, null, ((TreeExpCONST) binOp.left).value);
                        }
                    } else if (binOp.right instanceof TreeExpCONST) {
                        if (binOp.left instanceof TreeExpOP) {
                            TreeExpOP baseIndexOP = (TreeExpOP) binOp.left;
                            if (baseIndexOP.left instanceof TreeExpOP) {
                                src = calcIndexScaleOp((TreeExpOP) baseIndexOP.left, baseIndexOP.right);
                            } else if (baseIndexOP.right instanceof TreeExpOP) {
                                src = calcIndexScaleOp((TreeExpOP) baseIndexOP.right, baseIndexOP.left);
                            } else {
                                src = new Operand.Mem(munchExp(binOp.left), null, munchExp(binOp.right), 0);
                            }
                        } else {
                            src = new Operand.Mem(munchExp(binOp.left), null, null, ((TreeExpCONST) binOp.right).value);
                        }
                    } else {
                        if (binOp.left instanceof TreeExpOP) {
                            src = calcIndexScaleOp((TreeExpOP) binOp.left, binOp.right);
                        } else if (binOp.right instanceof TreeExpOP) {
                            src = calcIndexScaleOp((TreeExpOP) binOp.right, binOp.left);
                        } else {
                            src = new Operand.Mem(munchExp(binOp.left), null, munchExp(binOp.right), 0);
                        }
                    }
                    emit(new AssemBinaryOp(AssemBinaryOp.Kind.MOV, new Operand.Reg(reg), src));
                } else {
                    throw new IllegalArgumentException("Invalid Memory Address mode");
                }
            }

            return reg;
        }

        private Operand calcIndexScaleOp(TreeExpOP indexScale, TreeExp base) {
            if (indexScale.op != TreeExpOP.Op.MUL) {
                throw new IllegalArgumentException("invalid MEMORY Address mode");
            }

            int scale;
            Temp index;

            if (indexScale.left instanceof TreeExpCONST) {
                scale = ((TreeExpCONST) indexScale.left).value;
                index = munchExp(indexScale.right);
            } else {
                scale = ((TreeExpCONST) indexScale.right).value;
                index = munchExp(indexScale.left);
            }

            return new Operand.Mem(munchExp(base), scale, index, 0);
        }

        private Temp munchExp(TreeExpTEMP exp) {
            return exp.temp;
        }

        private Temp munchExp(TreeExpCONST exp) {
            Temp reg = new Temp();
            emit(new AssemBinaryOp(AssemBinaryOp.Kind.MOV, new Operand.Reg(reg), new Operand.Imm(exp.value)));
            return reg;
        }

        private void emit(Assem assem) {
            resultList.add(assem);
        }
    }
}
