package minijava.intermediate.visitor;

import minijava.backend.MachineSpecifics;
import minijava.intermediate.Label;
import minijava.intermediate.tree.*;
import minijava.semantic.symbol.SymbolTable;
import minijava.syntax.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: kowa
 * Date: 11/10/14
 */
public class StmTranslationVisitor implements StmVisitor<TreeStm, RuntimeException> {
    private final SymbolTable symbolTable;
    private final MachineSpecifics machineSpecifics;

    public StmTranslationVisitor(SymbolTable symbolTable, MachineSpecifics machineSpecifics) {
        this.symbolTable = symbolTable;
        this.machineSpecifics = machineSpecifics;
    }

    @Override
    public TreeStm visit(StmList s) throws RuntimeException {
        List<TreeStm> stms = new ArrayList<>(s.stms.size());

        for (Stm stm : s.stms) {
            stms.add(stm.accept(this));
        }

        return TreeStm.fromList(stms);
    }

    @Override
    public TreeStm visit(StmIf s) throws RuntimeException {
        ExpTranslationVisitor v = new ExpTranslationVisitor(symbolTable, machineSpecifics);
        Label t = new Label();
        Label f = new Label();
        Label j = new Label();

        TreeStmCJUMP cond = new TreeStmCJUMP(TreeStmCJUMP.Rel.EQ, s.cond.accept(v), new TreeExpCONST(1), t, f);

        return new TreeStmSEQ(
                cond,
                new TreeStmSEQ(
                        new TreeStmLABEL(t),
                        new TreeStmSEQ(
                                s.bodyTrue.accept(this),
                                new TreeStmSEQ(
                                        new TreeStmJUMP(new TreeExpNAME(j), Arrays.asList(j)),
                                        new TreeStmSEQ(
                                                new TreeStmLABEL(f),
                                                new TreeStmSEQ(
                                                        s.bodyFalse.accept(this),
                                                        new TreeStmLABEL(j)
                                                )
                                        )
                                )
                        )
                )
        );

    }

    @Override
    public TreeStm visit(StmWhile s) throws RuntimeException {
        Label test = new Label();
        Label done = new Label();

        ExpTranslationVisitor v = new ExpTranslationVisitor(symbolTable, machineSpecifics);

        TreeExp cond = s.cond.accept(v);

        TreeStmCJUMP stmTest;

        if (s.body == null) {
            stmTest = new TreeStmCJUMP(TreeStmCJUMP.Rel.EQ, cond, new TreeExpCONST(1), test, done);
            return new TreeStmSEQ(
                    new TreeStmLABEL(test),
                    new TreeStmSEQ(
                            stmTest,
                            new TreeStmLABEL(done)
                    )
            );
        } else {
            Label body = new Label();
            stmTest = new TreeStmCJUMP(TreeStmCJUMP.Rel.EQ, cond, new TreeExpCONST(1), body, done);
            TreeStm bodyStm = s.body.accept(this);
            return new TreeStmSEQ(
                    new TreeStmLABEL(test),
                    new TreeStmSEQ(
                            stmTest,
                            new TreeStmSEQ(
                                    new TreeStmLABEL(body),
                                    new TreeStmSEQ(
                                            bodyStm,
                                            new TreeStmSEQ(
                                                    new TreeStmJUMP(new TreeExpNAME(test), Arrays.asList(test)),
                                                    new TreeStmLABEL(done)
                                            )
                                    )
                            )
                    )
            );
        }
    }

    @Override
    public TreeStm visit(StmPrintlnInt s) throws RuntimeException {
        ExpTranslationVisitor v = new ExpTranslationVisitor(symbolTable, machineSpecifics);
        TreeExp arg = s.arg.accept(v);
        return new TreeStmEXP(new TreeExpCALL(new TreeExpNAME(new Label("_println_int")), Arrays.asList(arg)));
    }

    @Override
    public TreeStm visit(StmPrintChar s) throws RuntimeException {
        ExpTranslationVisitor v = new ExpTranslationVisitor(symbolTable, machineSpecifics);
        TreeExp arg = s.arg.accept(v);
        return new TreeStmEXP(new TreeExpCALL(new TreeExpNAME(new Label("_print_char")), Arrays.asList(arg)));
    }

    @Override
    public TreeStm visit(StmAssign s) throws RuntimeException {
        ExpTranslationVisitor v = new ExpTranslationVisitor(symbolTable, machineSpecifics);

        TreeExp id = new ExpId(s.id).accept(v);
        TreeExp rhs = s.rhs.accept(v);
        return new TreeStmMOVE(id, rhs);
    }

    @Override
    public TreeStm visit(StmArrayAssign s) throws RuntimeException {
        ExpTranslationVisitor v = new ExpTranslationVisitor(symbolTable, machineSpecifics);
        TreeExp arrAddr = new ExpId(s.id).accept(v);
        TreeExp offset = s.index.accept(v);
        offset = new TreeExpOP(TreeExpOP.Op.PLUS, offset, new TreeExpCONST(1));
        TreeExp loc = new TreeExpOP(TreeExpOP.Op.MUL, offset, new TreeExpCONST(machineSpecifics.getWordSize()));
        return new TreeStmMOVE(new TreeExpMEM(new TreeExpOP(TreeExpOP.Op.PLUS, arrAddr, loc)), s.rhs.accept(v));
    }
}
