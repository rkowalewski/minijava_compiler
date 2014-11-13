package minijava.semantic.visitor;

import minijava.syntax.*;

/**
 * User: kowa
 * Date: 10/28/14
 */
public interface Visitor<V> {
    V visit(Prg prg);
    V visit(DeclClass declClass);
    V visit(DeclVar declVar);
    V visit(DeclMeth declMeth);
    V visit(DeclMain declMain);
    V visit(Parameter parameter);
    V visit(StmArrayAssign stm);
    V visit(StmAssign stm);
    V visit(StmIf stmIf);
    V visit(StmList stmList);
    V visit(StmPrintChar stmPrintChar);
    V visit(StmPrintlnInt stmPrintlnInt);
    V visit(StmWhile stmWhile);
    V visit(ExpThis expThis);
    V visit(ExpTrue expTrue);
    V visit(ExpInvoke expInvoke);
    V visit(ExpNewIntArray expNewIntArray);
    V visit(ExpBinOp expBinOp);
    V visit(ExpNew expNew);
    V visit(ExpNeg expNeg);
    V visit(ExpArrayLength expArrayLength);
    V visit(ExpArrayGet expArrayGet);
    V visit(ExpIntConst expIntConst);
    V visit(ExpFalse expFalse);
    V visit(ExpId expId);
    V visit(Ty ty);
}
