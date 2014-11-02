package minijava.semantic.visitor;

import minijava.semantic.node.ClassDeclaration;
import minijava.semantic.node.Declaration;
import minijava.semantic.node.MethodDeclaration;
import minijava.semantic.symbol.Symbol;
import minijava.semantic.symbol.SymbolTable;
import minijava.syntax.*;

import java.util.ArrayList;
import java.util.List;

/**
 * User: kowa
 * Date: 10/30/14
 */
public class TypeCheckVisitor extends DepthFirstVisitor<Ty> {
    private SymbolTable symbolTable;
    private List<ErrorMsg> errors;
    private ClassDeclaration currentClass;

    public TypeCheckVisitor(SymbolTable symbolTable, List<ErrorMsg> errors) {
        this.symbolTable = symbolTable;
        this.errors = errors;
    }

    @Override
    public Ty visit(Prg prg) {
        symbolTable.resetTable();
        symbolTable.enterScope();

        super.visit(prg);

        symbolTable.exitScope();
        return null;
    }

    @Override
    public Ty visit(DeclMain declMain) {
        //Enter main class
        symbolTable.enterScope();
        //Enter main method
        symbolTable.enterScope();

        declMain.mainBody.accept(this);

        symbolTable.exitScope();
        symbolTable.exitScope();

        return null;
    }

    @Override
    public Ty visit(DeclClass declClass) {
        symbolTable.enterScope();

        for (DeclVar field : declClass.fields) {
            field.accept(this);
        }

        for (DeclMeth method : declClass.methods) {
            method.accept(this);
        }

        symbolTable.exitScope();
        return null;
    }

    @Override
    public Ty visit(DeclVar declVar) {
        return declVar.ty.accept(this);
    }

    @Override
    public Ty visit(DeclMeth declMeth) {
        Ty expectedReturn = declMeth.ty.accept(this);

        symbolTable.enterScope();

        for (Parameter param : declMeth.parameters) {
            param.accept(this);
        }

        for (DeclVar var : declMeth.localVars) {
            var.accept(this);
        }

        declMeth.body.accept(this);

        Ty actualReturn = declMeth.returnExp.accept(this);

        //Checks if the expectedReturn Class is the same or a super class (interface) of actualReturn class
        if (!(expectedReturn.getClass().isAssignableFrom(actualReturn.getClass()))) {
            String msg = "Incompatible Types (expected: %s, actual: %s)";
            reportError(String.format(msg, expectedReturn.toString(), actualReturn.toString()));
        }

        symbolTable.exitScope();

        return null;
    }

    @Override
    public Ty visit(StmArrayAssign stm) {
        Ty indexType = stm.index.accept(this);
        Ty rhsType = stm.rhs.accept(this);

        if (!(indexType instanceof TyInt)) {
            String msg = "invalid type of index for array access (found: %s, required: int)";
            reportError(String.format(msg, indexType.toString()));
        }

        if (!(rhsType instanceof TyInt)) {
            String msg = "invalid type of (found: %s, required: int)";
            reportError(String.format(msg, indexType.toString()));
        }

        Declaration decl = symbolTable.lookup(Symbol.get(stm.id));

        if (decl == null) {
            reportError("Statement id for array access is not defined!");
        }

        if (!(decl.getType() instanceof TyArr)) {
            String msg = "invalid type of statement id for array access (found: %s, required: int[])";
            reportError(String.format(msg, decl.getType()));
        }

        return null;
    }

    @Override
    public Ty visit(StmAssign stm) {
        Ty idType = new ExpId(stm.id).accept(this);

        Ty rhsType = stm.rhs.accept(this);

        if (!idType.getClass().isAssignableFrom(rhsType.getClass())) {
            reportError(String.format("incompatible types for assign statement (lhs: %s, rhs: %s)", idType, rhsType));
        }

        return null;
    }

    @Override
    public Ty visit(StmIf stmIf) {
        return super.visit(stmIf);
    }

    @Override
    public Ty visit(StmList stmList) {
        for (Stm stm : stmList.stms) {
            stm.accept(this);
        }

        return null;
    }

    @Override
    public Ty visit(StmPrintChar stmPrintChar) {
        return super.visit(stmPrintChar);
    }

    @Override
    public Ty visit(StmPrintlnInt stmPrintlnInt) {
        Ty argType = stmPrintlnInt.arg.accept(this);

        if (!(argType instanceof TyInt)) {
            reportError(String.format("invalid arg type for System.out.println (expected: int, actual: %s", argType));
        }

        return null;
    }

    @Override
    public Ty visit(StmWhile stmWhile) {
        return super.visit(stmWhile);
    }

    @Override
    public Ty visit(ExpThis expThis) {
        return super.visit(expThis);
    }

    @Override
    public Ty visit(ExpTrue expTrue) {
        return super.visit(expTrue);
    }

    @Override
    public Ty visit(ExpInvoke expInvoke) {
        Ty objTy = expInvoke.obj.accept(this);

        if (objTy.isPrimitive()) {
            //error
            return null;
        }

        MethodDeclaration method = symbolTable.lookupMethodInClass(Symbol.get(objTy.toString()), Symbol.get(expInvoke.method));

        if (method == null) {
            //error
            return null;
        }

        List<Ty> actualArgTypes = new ArrayList<Ty>(expInvoke.args.size());

        for (Exp argExp : expInvoke.args) {
            actualArgTypes.add(argExp.accept(this));
        }

        List<Ty> expectedArgTypes = method.getArgTypes();
        for (int i=0; i < expectedArgTypes.size(); i++) {
            if (!(expectedArgTypes.get(i).equals(actualArgTypes.get(i)))) {
                //error
                return null;
            }
        }

        return method.getType().accept(this);
    }

    @Override
    public Ty visit(ExpNewIntArray expNewIntArray) {
        return new TyArr(new TyInt());
    }

    @Override
    public Ty visit(ExpBinOp expBinOp) {
        return super.visit(expBinOp);
    }

    @Override
    public Ty visit(ExpNew expNew) {
        return new TyClass(expNew.className);
    }

    @Override
    public Ty visit(ExpNeg expNeg) {
        return super.visit(expNeg);
    }

    @Override
    public Ty visit(ExpArrayLength expArrayLength) {
        return super.visit(expArrayLength);
    }

    @Override
    public Ty visit(ExpArrayGet expArrayGet) {
        Ty arrayType = expArrayGet.array.accept(this);
        Ty indexType = expArrayGet.index.accept(this);

        if (arrayType == null || indexType == null) {
            return null;
        }

        if (!(arrayType instanceof TyArr)) {
            reportError(String.format("incompatible type of array (found: %s, expected: int[])", arrayType));
            return null;
        }

        if (!(indexType instanceof TyInt)) {
            reportError(String.format("incompatible type of array lookup index (found: %s, expected: int)", indexType));
            return null;
        }

        return new TyInt();
    }

    @Override
    public Ty visit(ExpIntConst expIntConst) {
        return new TyInt();
    }

    @Override
    public Ty visit(ExpFalse expFalse) {
        return super.visit(expFalse);
    }

    @Override
    public Ty visit(ExpId expId) {
        Declaration decl = symbolTable.lookup(Symbol.get(expId.id));

        if (decl == null) {
            reportError(String.format("Variable with id %s is not defined", expId.id));
            return null;
        }

        return decl.getType().accept(this);
    }

    @Override
    public Ty visit(Ty ty) {
        return ty;
    }

    private void reportError(String msg) {
        errors.add(new ErrorMsg(msg));
    }
}
