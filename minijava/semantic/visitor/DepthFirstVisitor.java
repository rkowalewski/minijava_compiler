package minijava.semantic.visitor;

import minijava.syntax.*;

/**
 * User: kowa
 * Date: 10/29/14
 */
public abstract class DepthFirstVisitor implements Visitor {
    @Override
    public void visit(Prg prg) {
        prg.mainClass.accept(this);
        for (DeclClass declClass : prg.classes) {
            declClass.accept(this);
        }
    }

    @Override
    public void visit(DeclMain declMain) {

    }

    @Override
    public void visit(DeclClass declClass) {

    }

    @Override
    public void visit(DeclVar declVar) {

    }

    @Override
    public void visit(DeclMeth declMeth) {

    }

    @Override
    public void visit(Parameter parameter) {

    }

    @Override
    public void visit(StmArrayAssign stm) {

    }

    @Override
    public void visit(StmAssign stm) {

    }

    @Override
    public void visit(StmIf stmIf) {

    }

    @Override
    public void visit(StmList stmList) {

    }

    @Override
    public void visit(StmPrintChar stmPrintChar) {

    }

    @Override
    public void visit(StmPrintlnInt stmPrintlnInt) {

    }

    @Override
    public void visit(StmWhile stmWhile) {

    }

    @Override
    public void visit(ExpThis expThis) {

    }

    @Override
    public void visit(ExpTrue expTrue) {

    }

    @Override
    public void visit(ExpInvoke expInvoke) {

    }

    @Override
    public void visit(ExpNewIntArray expNewIntArray) {

    }

    @Override
    public void visit(ExpBinOp expBinOp) {

    }

    @Override
    public void visit(ExpNew expNew) {

    }

    @Override
    public void visit(ExpNeg expNeg) {

    }

    @Override
    public void visit(ExpArrayLength expArrayLength) {

    }

    @Override
    public void visit(ExpArrayGet expArrayGet) {

    }

    @Override
    public void visit(ExpIntConst expIntConst) {

    }

    @Override
    public void visit(ExpFalse expFalse) {

    }

    @Override
    public void visit(ExpId expId) {

    }
}
