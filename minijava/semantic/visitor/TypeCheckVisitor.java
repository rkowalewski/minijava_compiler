package minijava.semantic.visitor;

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

        if (indexType == null || rhsType == null) {
            return null;
        }

        if (!(indexType instanceof TyInt)) {
            String msg = "invalid type of index for array access (found: %s, required: int)";
            reportError(String.format(msg, indexType.toString()));
        }

        if (!(rhsType instanceof TyInt)) {
            String msg = "invalid type of (found: %s, required: int)";
            reportError(String.format(msg, indexType.toString()));
        }

        Ty expArray = new ExpId(stm.id).accept(this);

        if (expArray == null) {
            reportError(String.format("StmArrayAssign: variable %s is not defined!", stm.id));
        } else if (!(expArray instanceof TyArr)) {
            String msg = "invalid type of statement id for array access (found: %s, required: int[])";
            reportError(String.format(msg, expArray.toString()));
        }

        return null;
    }

    @Override
    public Ty visit(StmAssign stm) {
        Ty idType = new ExpId(stm.id).accept(this);

        if (idType == null) {
            return null;
        }

        Ty rhsType = stm.rhs.accept(this);

        if (!idType.getClass().isAssignableFrom(rhsType.getClass())) {
            reportError(String.format("incompatible types for assign statement (lhs: %s, rhs: %s)", idType, rhsType));
        }

        return null;
    }

    @Override
    public Ty visit(StmIf stmIf) {
        Ty condType = stmIf.cond.accept(this);

        if (condType == null) {
            return null;
        }

        if (!(condType instanceof TyBool)) {
            reportError(String.format("StmIf: Invalid type of condition (expected: boolean, actual: %s", condType.toString()));
            return null;
        }

        stmIf.bodyTrue.accept(this);
        stmIf.bodyFalse.accept(this);

        return null;
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
        Ty argType = stmPrintChar.arg.accept(this);

        if (argType == null) {
            return null;
        }

        if (!(argType instanceof TyInt)) {
            reportError(String.format("StmPrintChar: invalid arg type (expected: int, actual: %s", argType));
        }

        return null;
    }

    @Override
    public Ty visit(StmPrintlnInt stmPrintlnInt) {
        Ty argType = stmPrintlnInt.arg.accept(this);

        if (argType == null) {
            return null;
        }

        if (!(argType instanceof TyInt)) {
            reportError(String.format("StmPrintlnInt: invalid arg type (expected: int, actual: %s", argType));
        }

        return null;
    }

    @Override
    public Ty visit(StmWhile stmWhile) {
        Ty condType = stmWhile.cond.accept(this);

        if (condType == null) {
            return null;
        }

        if (!(condType instanceof TyBool)) {
            reportError(String.format("StmWhile: Invalid type of condition (expected: boolean, actual: %s", condType.toString()));
            return null;
        }

        stmWhile.body.accept(this);

        return null;
    }

    @Override
    public Ty visit(ExpThis expThis) {
        Declaration parentClass = symbolTable.getCurrentClass();

        if (parentClass == null) {
            reportError("cannot find parent class in current scope level!");
            return null;
        }

        return parentClass.getType();
    }

    @Override
    public Ty visit(ExpTrue expTrue) {
        return new TyBool();
    }

    @Override
    public Ty visit(ExpInvoke expInvoke) {
        Ty objTy = expInvoke.obj.accept(this);

        if (objTy == null) {
            return null;
        }

        if (objTy.isPrimitive()) {
            reportError("cannot call a method on objects of primitive types!");
            return null;
        }

        MethodDeclaration method = symbolTable.lookupMethodInClass(Symbol.get(objTy.toString()), Symbol.get(expInvoke.method));

        if (method == null) {
            reportError(String.format("cannot find method %s on class %s", expInvoke.method, objTy.toString()));
            return null;
        }

        List<Ty> actualArgTypes = new ArrayList<Ty>(expInvoke.args.size());

        for (Exp argExp : expInvoke.args) {
            actualArgTypes.add(argExp.accept(this));
        }

        List<Ty> expectedArgTypes = method.getArgTypes();
        for (int i=0; i < expectedArgTypes.size(); i++) {
            if (!(expectedArgTypes.get(i).getClass().isAssignableFrom(actualArgTypes.get(i).getClass()))) {
                reportError(String.format("arguments of method call %s.%s does not match expected arguments!", objTy.toString(), expInvoke.method));
                return null;
            }
        }

        return method.getType().accept(this);
    }

    @Override
    public Ty visit(ExpNewIntArray expNewIntArray) {
        Ty sizeExp = expNewIntArray.size.accept(this);

        if (sizeExp == null) {
            reportError("ExpNewIntArray: The size expression must not be null!");
            return null;
        }

        if (!(sizeExp instanceof TyInt)) {
            reportError(String.format("ExpNewIntArray: invalid type of size expression (expected: int, actual %s)", sizeExp.toString()));
            return null;
        }

        return new TyArr(new TyInt());
    }

    @Override
    public Ty visit(ExpBinOp expBinOp) {
        Ty lhsType = expBinOp.left.accept(this);
        Ty rhsType = expBinOp.right.accept(this);

        if (lhsType == null || rhsType == null) {
            return null;
        }

        if (expBinOp.op == ExpBinOp.Op.AND) {
            if (!(lhsType instanceof TyBool)) {
                reportError(String.format("ExpBinOp Error (%s): invalid type of left expression (expected: boolean, actual: %s", expBinOp.op.toString(), lhsType.toString()));
                return null;
            }

            if (!(rhsType instanceof TyBool)) {
                reportError(String.format("ExpBinOp Error (%s): invalid type of right expression (expected: boolean, actual: %s", expBinOp.op.toString(), rhsType.toString()));
                return null;
            }

            return new TyBool();
        }

        if (!(lhsType instanceof TyInt)) {
            reportError(String.format("ExpBinOp Error (%s): invalid type of left expression (expected: int, actual: %s", expBinOp.op.toString(), lhsType.toString()));
            return null;
        }

        if (!(rhsType instanceof TyInt)) {
            reportError(String.format("ExpBinOp Error (%s): invalid type of right expression (expected: int, actual: %s", expBinOp.op.toString(), rhsType.toString()));
            return null;
        }

        if (expBinOp.op == ExpBinOp.Op.LT) {
            return new TyBool();
        }

        return new TyInt();
    }

    @Override
    public Ty visit(ExpNew expNew) {
        return new TyClass(expNew.className);
    }

    @Override
    public Ty visit(ExpNeg expNeg) {
        Ty expNegType = expNeg.body.accept(this);

        if (expNegType == null) {
            return null;
        }

        if (!(expNegType instanceof TyBool)) {
            reportError(String.format("ExpNeg: invalid type of expression (expected: boolean, actual: %s)", expNegType.toString()));
            return null;
        }

        return new TyBool();
    }

    @Override
    public Ty visit(ExpArrayLength expArrayLength) {
        Ty arrayLengthType = expArrayLength.array.accept(this);

        if (arrayLengthType == null) {
            return null;
        }

        if (!(arrayLengthType instanceof TyArr)) {
            reportError(String.format("ExpArrayLength: method 'length' is only available on arrays (actual: %s)", arrayLengthType.toString()));
        }

        return new TyInt();
    }

    @Override
    public Ty visit(ExpArrayGet expArrayGet) {
        Ty arrayType = expArrayGet.array.accept(this);
        Ty indexType = expArrayGet.index.accept(this);

        if (arrayType == null || indexType == null) {
            return null;
        }

        if (!(arrayType instanceof TyArr)) {
            reportError(String.format("ExpArrayGet: Incompatible type of array expression (found: %s, expected: int[])", arrayType));
            return null;
        }

        if (!(indexType instanceof TyInt)) {
            reportError(String.format("ExpArrayGet: incompatible type of array lookup index (found: %s, expected: int)", indexType));
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
        return new TyBool();
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
