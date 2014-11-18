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

        TreeExp sizePlusOne = new TreeExpOP(TreeExpOP.Op.PLUS, size, new TreeExpCONST(1));
        TreeExp nBytes = new TreeExpOP(TreeExpOP.Op.MUL, sizePlusOne, new TreeExpCONST(machineSpecifics.getWordSize()));

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
        TreeExp nBytes = new TreeExpOP(TreeExpOP.Op.MUL, new TreeExpCONST(clazz.getFieldsCount()), new TreeExpCONST(machineSpecifics.getWordSize()));
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
        TreeExp offsetPlusOne = new TreeExpOP(TreeExpOP.Op.PLUS, offset, new TreeExpCONST(1));
        TreeExp loc = new TreeExpOP(TreeExpOP.Op.MUL, new TreeExpCONST(machineSpecifics.getWordSize()), offsetPlusOne);
        TreeExp arrayValue = new TreeExpMEM(new TreeExpOP(TreeExpOP.Op.PLUS, arrAddr, loc));

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

}