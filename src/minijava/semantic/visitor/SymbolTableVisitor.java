package minijava.semantic.visitor;

import minijava.semantic.node.ClassDeclaration;
import minijava.semantic.node.Declaration;
import minijava.semantic.node.MethodDeclaration;
import minijava.semantic.node.VarDeclaration;
import minijava.semantic.symbol.DuplicateSymbolException;
import minijava.semantic.symbol.Symbol;
import minijava.semantic.symbol.SymbolTable;
import minijava.syntax.*;

import java.util.List;

/**
 * User: kowa
 * Date: 10/28/14
 */
public class SymbolTableVisitor extends DepthFirstVisitor<Void> {
    private final SymbolTable symbolTable;
    private final List<ErrorMsg> errors;

    public SymbolTableVisitor(SymbolTable _symbolTable, List<ErrorMsg> _errors)  {
        this.symbolTable = _symbolTable;
        this.errors = _errors;
    }

    public void addSymtabEntry(Symbol s, Declaration d) {
        try {
            symbolTable.put(s, d);
        } catch(DuplicateSymbolException ex) {
            errors.add(new ErrorMsg(ex.getMessage()));
        }
    }

    @Override
    public Void visit(Prg prg) {
        symbolTable.enterScope();

        super.visit(prg);

        symbolTable.exitScope();
        return null;
    }

    @Override
    public Void visit(DeclMain declMain) {
        ClassDeclaration clazz = new ClassDeclaration(declMain.className, new TyClass(declMain.className));
        Symbol s = Symbol.get("c:" + declMain.className);
        addSymtabEntry(s, clazz);

        symbolTable.enterScope();

        MethodDeclaration method = new MethodDeclaration(new TyVoid());
        addSymtabEntry(Symbol.get("m:mainMethod"), method);

        //For the main method we must not enter the method scope

        symbolTable.exitScope();

        return null;
    }

    @Override
    public Void visit(DeclClass declClass) {
        ClassDeclaration clazz = new ClassDeclaration(declClass.className, new TyClass(declClass.className));
        Symbol s = Symbol.get("c:" + declClass.className);
        addSymtabEntry(s, clazz);

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
    public Void visit(DeclVar declVar) {
        Symbol symbol = Symbol.get("v:" + declVar.name);
        VarDeclaration d = new VarDeclaration(declVar.ty, Declaration.Kind.VARIABLE);
        addSymtabEntry(symbol, d);
        return null;
    }

    @Override
    public Void visit(Parameter parameter) {
        Symbol symbol = Symbol.get("v:" + parameter.id);
        VarDeclaration d = new VarDeclaration(parameter.ty, Declaration.Kind.PARAMETER);
        addSymtabEntry(symbol, d);
        return null;
    }

    @Override
    public Void visit(DeclMeth declMeth) {
        // Push the method declaration into the symbol table
        MethodDeclaration declaration = new MethodDeclaration(declMeth.ty);
        addSymtabEntry(Symbol.get("m:" + declMeth.methodName), declaration);


        // Increment the scope
        symbolTable.enterScope();

        for (Parameter param : declMeth.parameters) {
            param.accept(this);
        }

        for(DeclVar declVar : declMeth.localVars) {
            declVar.accept(this);
        }

        symbolTable.exitScope();
        return null;
    }

    private void reportError() {

    }
}
