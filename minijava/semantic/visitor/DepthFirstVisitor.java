package minijava.semantic.visitor;

import minijava.syntax.*;

/**
 * User: kowa
 * Date: 10/29/14
 */
public abstract class DepthFirstVisitor<V> implements Visitor<V> {
    @Override
    public V visit(Prg prg) {
        prg.mainClass.accept(this);
        for (DeclClass declClass : prg.classes) {
            declClass.accept(this);
        }
        return null;
    }

    @Override
    public V visit(DeclClass declClass) {
        return null;
    }

    @Override
    public V visit(DeclVar declVar) {
        return null;
    }

    @Override
    public V visit(DeclMeth declMeth) {
        return null;
    }

    @Override
    public V visit(DeclMain declMain) {
        return null;
    }

    @Override
    public V visit(Parameter parameter) {
        return null;
    }

    @Override
    public V visit(StmArrayAssign stm) {
        return null;
    }

    @Override
    public V visit(StmAssign stm) {
        return null;
    }

    @Override
    public V visit(StmIf stmIf) {
        return null;
    }

    @Override
    public V visit(StmList stmList) {
        return null;
    }

    @Override
    public V visit(StmPrintChar stmPrintChar) {
        return null;
    }

    @Override
    public V visit(StmPrintlnInt stmPrintlnInt) {
        return null;
    }

    @Override
    public V visit(StmWhile stmWhile) {
        return null;
    }

    @Override
    public V visit(ExpThis expThis) {
        return null;
    }

    @Override
    public V visit(ExpTrue expTrue) {
        return null;
    }

    @Override
    public V visit(ExpInvoke expInvoke) {
        return null;
    }

    @Override
    public V visit(ExpNewIntArray expNewIntArray) {
        return null;
    }

    @Override
    public V visit(ExpBinOp expBinOp) {
        return null;
    }

    @Override
    public V visit(ExpNew expNew) {
        return null;
    }

    @Override
    public V visit(ExpNeg expNeg) {
        return null;
    }

    @Override
    public V visit(ExpArrayLength expArrayLength) {
        return null;
    }

    @Override
    public V visit(ExpArrayGet expArrayGet) {
        return null;
    }

    @Override
    public V visit(ExpIntConst expIntConst) {
        return null;
    }

    @Override
    public V visit(ExpFalse expFalse) {
        return null;
    }

    @Override
    public V visit(ExpId expId) {
        return null;
    }

    @Override
    public V visit(Ty ty) {
        return null;
    }
}
