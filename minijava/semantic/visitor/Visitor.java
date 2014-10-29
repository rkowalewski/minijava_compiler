package minijava.semantic.visitor;

import minijava.syntax.*;

/**
 * User: kowa
 * Date: 10/28/14
 */
public interface Visitor {
    void visit(Prg prg);
    void visit(DeclClass declClass);
    void visit(DeclVar declVar);
    void visit(DeclMeth declMeth);
    void visit(DeclMain declMain);
    void visit(Parameter parameter);
    void visit(StmArrayAssign stm);
    void visit(StmAssign stm);
    void visit(StmIf stmIf);
    void visit(StmList stmList);
    void visit(StmPrintChar stmPrintChar);
    void visit(StmPrintlnInt stmPrintlnInt);
    void visit(StmWhile stmWhile);
    void visit(ExpThis expThis);
    void visit(ExpTrue expTrue);
    void visit(ExpInvoke expInvoke);
    void visit(ExpNewIntArray expNewIntArray);
    void visit(ExpBinOp expBinOp);
    void visit(ExpNew expNew);
    void visit(ExpNeg expNeg);
    void visit(ExpArrayLength expArrayLength);
    void visit(ExpArrayGet expArrayGet);
    void visit(ExpIntConst expIntConst);
    void visit(ExpFalse expFalse);
    void visit(ExpId expId);
}
