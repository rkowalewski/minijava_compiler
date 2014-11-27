package minijava.intermediate.visitor;

import minijava.backend.MachineSpecifics;
import minijava.intermediate.Label;
import minijava.intermediate.Temp;
import minijava.intermediate.tree.*;
import minijava.semantic.node.ClassDeclaration;
import minijava.semantic.node.Declaration;
import minijava.semantic.node.MethodDeclaration;
import minijava.semantic.node.VarDeclaration;
import minijava.semantic.symbol.Symbol;
import minijava.semantic.symbol.SymbolTable;
import minijava.syntax.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: kowa
 * Date: 11/7/14
 */
public class ExpTranslationVisitor implements ExpVisitor<TreeExp, RuntimeException> {
    private final SymbolTable symbolTable;
    private final MachineSpecifics machineSpecifics;

    public ExpTranslationVisitor(SymbolTable symbolTable, MachineSpecifics machineSpecifics) {
        this.symbolTable = symbolTable;
        this.machineSpecifics = machineSpecifics;
    }

    @Override
    public TreeExp visit(ExpTrue e) throws RuntimeException {
        return new TreeExpCONST(1);
    }

    @Override
    public TreeExp visit(ExpFalse e) throws RuntimeException {
        return new TreeExpCONST(0);
    }

    @Override
    public TreeExp visit(ExpThis e) throws RuntimeException {
        return ((MethodDeclaration) symbolTable.getCur().getParentElement()).getFrame().getParameter(0);
    }

    @Override
    public TreeExp visit(ExpNewIntArray e) throws RuntimeException {
        TreeExp size = e.size.accept(this);

        TreeExp sizePlusOne = plus(size, new TreeExpCONST(1), true);
        TreeExp nBytes = mul(sizePlusOne, new TreeExpCONST(machineSpecifics.getWordSize()), true);

        Temp t = new Temp();
        TreeExp l_halloc = new TreeExpCALL(new TreeExpNAME(new Label("_halloc")), Arrays.asList(nBytes));

        return new TreeExpESEQ(
                new TreeStmSEQ(
                        new TreeStmMOVE(new TreeExpTEMP(t), l_halloc),
                        new TreeStmMOVE(new TreeExpMEM(new TreeExpTEMP(t)), size)),
                new TreeExpTEMP(t)
        );
    }

    @Override
    public TreeExp visit(ExpNew e) throws RuntimeException {
        ClassDeclaration clazz = symbolTable.getClassByName(Symbol.get("c:" + e.className));
        TreeExp nBytes = mul(new TreeExpCONST(clazz.getFieldsCount()), new TreeExpCONST(machineSpecifics.getWordSize()), true);
        TreeExp l_halloc = new TreeExpCALL(new TreeExpNAME(new Label("_halloc")), Arrays.asList(nBytes));
        return l_halloc;
    }

    @Override
    public TreeExp visit(ExpNeg e) throws RuntimeException {
        //True
        Label t = new Label();
        //False
        Label f = new Label();
        //Join
        Label j = new Label();

        Temp r = new Temp();

        TreeExp val = e.body.accept(this);

        if (val instanceof TreeExpCONST) {
            TreeExpCONST constVal = (TreeExpCONST) val;

            if (constVal.value == 0) {
                return new TreeExpCONST(1);
            }
            return new TreeExpCONST(0);
        }


        return getRelExp(TreeStmCJUMP.Rel.EQ, val, new TreeExpCONST(0), new TreeExpCONST(1), new TreeExpCONST(0));
    }

    @Override
    public TreeExp visit(ExpBinOp e) throws RuntimeException {
        TreeExpOP.Op arithmeticOperator;

        switch (e.op) {
            case PLUS:
                arithmeticOperator = TreeExpOP.Op.PLUS;
                break;
            case MINUS:
                arithmeticOperator = TreeExpOP.Op.MINUS;
                break;
            case TIMES:
                arithmeticOperator = TreeExpOP.Op.MUL;
                break;
            case DIV:
                arithmeticOperator = TreeExpOP.Op.DIV;
                break;
            default:
                arithmeticOperator = null;
        }

        if (arithmeticOperator != null) {
            if (arithmeticOperator == TreeExpOP.Op.PLUS) {
                return plus(e.left.accept(this), e.right.accept(this), true);
            } else if (arithmeticOperator == TreeExpOP.Op.MINUS) {
                return plus(e.left.accept(this), e.right.accept(this), false);
            } else if (arithmeticOperator == TreeExpOP.Op.MUL) {
                return mul(e.left.accept(this), e.right.accept(this), true);
            } else if (arithmeticOperator == TreeExpOP.Op.DIV) {
                return mul(e.left.accept(this), e.right.accept(this), false);
            }

            return new TreeExpOP(arithmeticOperator, e.left.accept(this), e.right.accept(this));

        } else if (e.op == ExpBinOp.Op.LT) {
            //LessThan
            return getRelExp(TreeStmCJUMP.Rel.LT, e.left.accept(this), e.right.accept(this), new TreeExpCONST(1), new TreeExpCONST(0));
        } else {
            //And
            return getRelExp(TreeStmCJUMP.Rel.EQ, e.left.accept(this), new TreeExpCONST(0), new TreeExpCONST(0), e.right.accept(this));
        }
    }

    @Override
    public TreeExp visit(ExpArrayGet e) throws RuntimeException {
        TreeExp arrAddr = e.array.accept(this);
        TreeExp offset = e.index.accept(this);
        TreeExp lowerBoundCheck = getRelExp(TreeStmCJUMP.Rel.LE, new TreeExpCONST(0), offset, new TreeExpCONST(1), new TreeExpCONST(0));
        TreeExp upperBoundCheck = getRelExp(TreeStmCJUMP.Rel.LT, offset, new TreeExpMEM(arrAddr), new TreeExpCONST(1), new TreeExpCONST(0));
        TreeExp combined = getRelExp(TreeStmCJUMP.Rel.EQ, lowerBoundCheck, new TreeExpCONST(0), new TreeExpCONST(0), upperBoundCheck);

        //compensate array length
        TreeExp offsetPlusOne = plus(offset, new TreeExpCONST(1), true);
        TreeExp loc = mul(new TreeExpCONST(machineSpecifics.getWordSize()), offsetPlusOne, true);
        TreeExp arrayValue = new TreeExpMEM(plus(arrAddr, loc, true));

        return getRelExp(TreeStmCJUMP.Rel.EQ, combined, new TreeExpCONST(1),
                arrayValue,
                new TreeExpCALL(
                        new TreeExpNAME(new Label("_raise")),
                        Arrays.<TreeExp>asList(new TreeExpCONST(-1))
                )
        );
    }

    @Override
    public TreeExp visit(ExpArrayLength e) throws RuntimeException {
        return new TreeExpMEM(e.array.accept(this));
    }

    @Override
    public TreeExp visit(ExpInvoke e) throws RuntimeException {
        TreeExp caller = e.obj.accept(this);

        List<TreeExp> args = new ArrayList<>();

        args.add(caller);

        for (Exp arg : e.args) {
            args.add(arg.accept(this));
        }

        return new TreeExpCALL(new TreeExpNAME(new Label(e.fullname)), args);
    }

    @Override
    public TreeExp visit(ExpIntConst e) throws RuntimeException {
        return new TreeExpCONST(e.value);
    }

    @Override
    public TreeExp visit(ExpId e) throws RuntimeException {
        //lookup variable / field
        Symbol key = Symbol.get("v:" + e.id);
        VarDeclaration var = (VarDeclaration) symbolTable.getCur().lookup(key, false);

        if (var != null) {
            //inside method
            MethodDeclaration method = (MethodDeclaration) symbolTable.getCur().getParentElement();

            if (var.getKind() == Declaration.Kind.PARAMETER) {

                int paramIndex = method.getParameterIndexByKey(key);

                if (paramIndex > -1) {
                    paramIndex += 1;
                }

                return method.getFrame().getParameter(paramIndex);
            } else {
                return var.localAccess();
            }
        } else {
            //field of class
            var = (VarDeclaration) symbolTable.lookup(key);
            TreeExp thisPointer = new ExpThis().accept(this);
            return var.fieldAccess(thisPointer);
        }
    }

    private TreeExp getRelExp(TreeStmCJUMP.Rel operator, TreeExp lhs, TreeExp rhs, TreeExp expTrue, TreeExp expFalse) {
        Label t = new Label();
        Label f = new Label();
        Label j = new Label();

        Temp r = new Temp();

        return new TreeExpESEQ(
                new TreeStmSEQ(
                        new TreeStmCJUMP(operator, lhs, rhs, t, f),
                        new TreeStmSEQ(
                                new TreeStmLABEL(t),
                                new TreeStmSEQ(
                                        new TreeStmMOVE(new TreeExpTEMP(r), expTrue),
                                        new TreeStmSEQ(
                                                new TreeStmJUMP(new TreeExpNAME(j), Arrays.asList(j)),
                                                new TreeStmSEQ(
                                                        new TreeStmLABEL(f),
                                                        new TreeStmSEQ(
                                                                new TreeStmMOVE(new TreeExpTEMP(r), expFalse),
                                                                new TreeStmLABEL(j)
                                                        )
                                                )
                                        )
                                )
                        )
                ),
                new TreeExpTEMP(r)
        );
    }

    TreeExp plus(TreeExp left, TreeExp right, boolean plus) {
        if (left instanceof TreeExpCONST && right instanceof TreeExpCONST) {
            TreeExpCONST cLeft = (TreeExpCONST) left;
            TreeExpCONST cRight = (TreeExpCONST) right;

            if (!plus) {
                return new TreeExpCONST(cLeft.value - cRight.value);
            }

            return new TreeExpCONST(cLeft.value + cRight.value);
        }

        if (!plus) {
            return new TreeExpOP(TreeExpOP.Op.MINUS, left, right);
        }

        return new TreeExpOP(TreeExpOP.Op.PLUS, left, right);
    }

    TreeExp mul(TreeExp left, TreeExp right, boolean mul) {
        if (left instanceof TreeExpCONST && right instanceof TreeExpCONST) {
            TreeExpCONST c1 = (TreeExpCONST) left;
            TreeExpCONST c2 = (TreeExpCONST) right;

            if (mul) {
                return new TreeExpCONST(c1.value * c2.value);
            }
            return new TreeExpCONST(c1.value / c2.value);
        } else if (left instanceof TreeExpCONST && ((TreeExpCONST) left).value % 2 == 0) {
            int val = log2(((TreeExpCONST) left).value);

            if (mul) {
                return new TreeExpOP(TreeExpOP.Op.LSHIFT, right, new TreeExpCONST(val));
            }

            return new TreeExpOP(TreeExpOP.Op.RSHIFT, right, new TreeExpCONST(val));
        } else if (right instanceof TreeExpCONST && ((TreeExpCONST) right).value % 2 == 0) {
            int val = log2(((TreeExpCONST) right).value);

            if (mul) {
                return new TreeExpOP(TreeExpOP.Op.LSHIFT, left, new TreeExpCONST(val));
            }

            return new TreeExpOP(TreeExpOP.Op.RSHIFT, left, new TreeExpCONST(val));
        }

        if (mul) {
            return new TreeExpOP(TreeExpOP.Op.MUL, left, right);
        }

        return new TreeExpOP(TreeExpOP.Op.DIV, left, right);
    }

    private int log2(int n) {
        return (int) (Math.log10(n) / Math.log10(2));
    }
}
