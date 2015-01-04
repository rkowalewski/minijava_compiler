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
public class I386CodegenVisitor implements FragmentVisitor<List<TreeStm>, Fragment<List<Assem>>> {

    @Override
    public Fragment<List<Assem>> visit(FragmentProc<List<TreeStm>> fragProc) {

        List<Assem> assemList = new ArrayList<>();

        ArrayList<TreeStm> treeStms = new ArrayList<>(fragProc.body);

        AssemProcessor assemProcessor = new AssemProcessor();

        for (int i = 0; i < treeStms.size(); i++) {
            assemList.addAll(assemProcessor.munchStm(treeStms.get(i)));
        }

        List<Assem> completeBody = saveAndRestoreCalleeSaves(assemList);
        completeBody.add(0, new AssemLabel(fragProc.frame.getName()));
        completeBody.add(new AssemInstr(AssemInstr.Kind.RET));


        return new FragmentProc<>(fragProc.frame, completeBody);
    }

    private List<Assem> saveAndRestoreCalleeSaves(List<Assem> body) {
        List<Assem> completeBody = new ArrayList<>(body);
        List<Assem> prologue = new ArrayList<>();

        Temp ebx = new Temp();
        Temp edi = new Temp();
        Temp esi = new Temp();

        prologue.add(new AssemBinaryOp(AssemBinaryOp.Kind.MOV, new Operand.Reg(ebx), new Operand.Reg(I386Frame.ebx)));
        prologue.add(new AssemBinaryOp(AssemBinaryOp.Kind.MOV, new Operand.Reg(edi), new Operand.Reg(I386Frame.edi)));
        prologue.add(new AssemBinaryOp(AssemBinaryOp.Kind.MOV, new Operand.Reg(esi), new Operand.Reg(I386Frame.esi)));

        List<Assem> epilogue = new ArrayList<>();

        epilogue.add(new AssemBinaryOp(AssemBinaryOp.Kind.MOV, new Operand.Reg(I386Frame.ebx), new Operand.Reg(ebx)));
        epilogue.add(new AssemBinaryOp(AssemBinaryOp.Kind.MOV, new Operand.Reg(I386Frame.edi), new Operand.Reg(edi)));
        epilogue.add(new AssemBinaryOp(AssemBinaryOp.Kind.MOV, new Operand.Reg(I386Frame.esi), new Operand.Reg(esi)));


        completeBody.addAll(0, prologue);
        completeBody.addAll(epilogue);

        return completeBody;
    }

    private static class AssemProcessor {
        private List<Assem> resultList = new ArrayList<>();

        public List<Assem> munchStm(TreeStm stm) {
            this.resultList = new ArrayList<>();

            if (stm instanceof TreeStmLABEL) {
                munchStm((TreeStmLABEL) stm);
            } else if (stm instanceof TreeStmJUMP) {
                munchStm((TreeStmJUMP) stm);
            } else if (stm instanceof TreeStmCJUMP) {
                munchStm((TreeStmCJUMP) stm);
            } else if (stm instanceof TreeStmMOVE) {
                munchStm((TreeStmMOVE) stm);
            } else if (stm instanceof TreeStmEXP) {
                munchStm((TreeStmEXP) stm);
            }

            return resultList;
        }

        private void munchStm(TreeStmMOVE move) {
            if (move.dest instanceof TreeExpTEMP) {
                TreeExpTEMP dst = (TreeExpTEMP) move.dest;
                if (move.src instanceof TreeExpCONST) {
                    emit(new AssemBinaryOp(AssemBinaryOp.Kind.MOV, new Operand.Reg(dst.temp), new Operand.Imm(((TreeExpCONST) move.src).value)));
                } else if (move.src instanceof TreeExpMEM) {
                    TreeExpMEM mem = (TreeExpMEM) move.src;

                    Operand src;

                    if (mem.addr instanceof TreeExpOP) {
                        TreeExpOP binOp = (TreeExpOP) mem.addr;

                        if (binOp.op == TreeExpOP.Op.PLUS) {
                            if (binOp.left instanceof TreeExpCONST) {
                                src = new Operand.Mem(munchExp(binOp.right), null, null, ((TreeExpCONST) binOp.left).value);
                            } else if (binOp.right instanceof TreeExpCONST) {
                                src = new Operand.Mem(munchExp(binOp.left), null, null, ((TreeExpCONST) binOp.right).value);
                            } else {
                                src = new Operand.Mem(munchExp(binOp.left), null, munchExp(binOp.right), 0);
                            }
                        } else {
                            throw new IllegalArgumentException("Invalid Access Mode");
                        }
                    } else {
                        src = new Operand.Mem(munchExp(mem.addr));
                    }

                    emit(new AssemBinaryOp(AssemBinaryOp.Kind.MOV, new Operand.Reg(dst.temp), src));

                } else {
                    emit(new AssemBinaryOp(AssemBinaryOp.Kind.MOV, new Operand.Reg(dst.temp), new Operand.Reg(munchExp(move.src))));
                }
            } else if (move.dest instanceof TreeExpMEM) {
                TreeExpMEM mem = (TreeExpMEM) move.dest;
                Operand dest;

                if (mem.addr instanceof TreeExpOP) {
                    TreeExpOP binOp = (TreeExpOP) mem.addr;

                    if (binOp.op == TreeExpOP.Op.PLUS) {
                        if (binOp.left instanceof TreeExpCONST) {
                            dest = new Operand.Mem(munchExp(binOp.right), null, null, ((TreeExpCONST) binOp.left).value);
                        } else if (binOp.right instanceof TreeExpCONST) {
                            dest = new Operand.Mem(munchExp(binOp.left), null, null, ((TreeExpCONST) binOp.right).value);
                        } else {
                            dest = new Operand.Mem(munchExp(binOp.left), null, munchExp(binOp.right), 0);
                        }
                    } else {
                        throw new IllegalArgumentException("Invalid Access Mode");
                    }

                } else {
                    dest = new Operand.Mem(munchExp(mem.addr));
                }

                emit(new AssemBinaryOp(AssemBinaryOp.Kind.MOV, dest, new Operand.Reg(munchExp(move.src))));
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
            } else {
                emit(new AssemBinaryOp(AssemBinaryOp.Kind.CMP, new Operand.Reg(munchExp(cjump.left)), new Operand.Reg(munchExp(cjump.right))));
            }

            AssemJump.Cond condition = null;

            switch (cjump.rel) {
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
            } else if (exp instanceof TreeExpCALL) {
                return munchExp((TreeExpCALL) exp);
            } else if (exp instanceof TreeExpMEM) {
                return munchExp((TreeExpMEM) exp);
            } else if (exp instanceof TreeExpTEMP) {
                return munchExp((TreeExpTEMP) exp);
            } else if (exp instanceof TreeExpNAME) {
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
                case ARSHIFT:
                    binaryOp = AssemBinaryOp.Kind.SAR;
                    break;
                default:
                    throw new IllegalArgumentException("cannot handle operator: " + exp.op.toString());
            }

            Temp leftReg;

            if (exp.left instanceof TreeExpMEM && exp.right instanceof TreeExpMEM) {
                leftReg = new Temp();
                emit(new AssemBinaryOp(AssemBinaryOp.Kind.MOV, new Operand.Reg(leftReg), new Operand.Reg(munchExp(exp.left))));
            } else {
                leftReg = munchExp(exp.left);
            }

            Operand right;

            if (exp.right instanceof TreeExpCONST && unaryOp == null) {
                right = new Operand.Imm(((TreeExpCONST) exp.right).value);
            } else {
                right = new Operand.Reg(munchExp(exp.right));
            }

            if (binaryOp != null) {
                if (binaryOp == AssemBinaryOp.Kind.SUB || binaryOp == AssemBinaryOp.Kind.ADD) {
                    if (right instanceof Operand.Imm && ((Operand.Imm) right).imm == 1) {
                        unaryOp = binaryOp == AssemBinaryOp.Kind.ADD ? AssemUnaryOp.Kind.INC : AssemUnaryOp.Kind.DEC;
                        emit(new AssemUnaryOp(unaryOp, new Operand.Reg(leftReg)));
                        return leftReg;
                    }
                }
                emit(new AssemBinaryOp(binaryOp, new Operand.Reg(leftReg), right));
                return leftReg;
            } else {
                //must be mul or div
                Temp target = new Temp();
                emit(new AssemBinaryOp(AssemBinaryOp.Kind.MOV, new Operand.Reg(I386Frame.eax), new Operand.Reg(leftReg)));

                if (unaryOp == AssemUnaryOp.Kind.IDIV) {
                    //cdq command
                    emit(new AssemBinaryOp(AssemBinaryOp.Kind.MOV, new Operand.Reg(I386Frame.edx), new Operand.Reg(I386Frame.eax)));
                    emit(new AssemBinaryOp(AssemBinaryOp.Kind.SAR, new Operand.Reg(I386Frame.edx), new Operand.Imm(31)));
                }
                emit(new AssemUnaryOp(unaryOp, right));
                emit(new AssemBinaryOp(AssemBinaryOp.Kind.MOV, new Operand.Reg((target)), new Operand.Reg(I386Frame.eax)));
                return target;
            }
        }

        private Temp munchExp(TreeExpCALL exp) {
            Label fnLabel = ((TreeExpNAME) exp.func).label;

            //Push args in reverse order
            for (int i = exp.args.size() - 1; i > -1; i--) {

                TreeExp arg = exp.args.get(i);
                Operand op;

                if (arg instanceof TreeExpCONST) {
                    op = new Operand.Imm(((TreeExpCONST) arg).value);
                } else {
                    op = new Operand.Reg(munchExp(arg));
                }

                emit(new AssemUnaryOp(AssemUnaryOp.Kind.PUSH, op));
            }

            emit(new AssemJump(AssemJump.Kind.CALL, fnLabel));

            if (exp.args.size() > 0) {
                emit(new AssemBinaryOp(AssemBinaryOp.Kind.ADD, new Operand.Reg(I386Frame.esp), new Operand.Imm(exp.args.size() * 4)));
            }

            Temp reg = new Temp();

            emit(new AssemBinaryOp(AssemBinaryOp.Kind.MOV, new Operand.Reg(reg), new Operand.Reg(I386Frame.eax)));

            return reg;
        }

        private Temp munchExp(TreeExpMEM exp) {
            Operand src;
            if (exp.addr instanceof TreeExpTEMP) {
                src = new Operand.Mem(((TreeExpTEMP) exp.addr).temp);
            } else if (exp.addr instanceof TreeExpOP) {
                TreeExpOP binOp = (TreeExpOP) exp.addr;

                if (binOp.op == TreeExpOP.Op.PLUS) {
                    if (binOp.left instanceof TreeExpCONST) {
                        src = new Operand.Mem(munchExp(binOp.right), null, null, ((TreeExpCONST) binOp.left).value);
                    } else if (binOp.right instanceof TreeExpCONST) {
                        src = new Operand.Mem(munchExp(binOp.left), null, null, ((TreeExpCONST) binOp.right).value);
                    } else {
                        src = new Operand.Mem(munchExp(binOp.left), null, munchExp(binOp.right), 0);
                    }
                } else {
                    throw new IllegalArgumentException("Invalid Access Mode");
                }
            } else {
                src = new Operand.Mem(munchExp(exp.addr));
            }

            Temp reg = new Temp();
            emit(new AssemBinaryOp(AssemBinaryOp.Kind.MOV, new Operand.Reg(reg), src));
            return reg;
        }

        private Temp munchExp(TreeExpTEMP exp) {
            Temp reg = new Temp();
            emit(new AssemBinaryOp(AssemBinaryOp.Kind.MOV, new Operand.Reg(reg), new Operand.Reg(exp.temp)));
            return reg;
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
