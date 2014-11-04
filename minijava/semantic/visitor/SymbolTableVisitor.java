package minijava.semantic.visitor;

import minijava.semantic.node.*;
import minijava.semantic.symbol.DuplicateSymbolException;
import minijava.semantic.symbol.Symbol;
import minijava.semantic.symbol.SymbolTable;
import minijava.syntax.*;

import java.util.ArrayList;
import java.util.Collections;
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

        MethodDeclaration method = new MethodDeclaration(new TyVoid(), Collections.<Ty>emptyList());
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
        VarDeclaration d = new VarDeclaration(declVar.ty);
        addSymtabEntry(symbol, d);
        return null;
    }

    @Override
    public Void visit(DeclMeth declMeth) {
        // Get the parameter types
        ArrayList<Ty> argumentTypes = new ArrayList<Ty>();

        for (Parameter param : declMeth.parameters) {
            argumentTypes.add(param.ty);
        }

        // Push the method declaration onto the symbol table
        MethodDeclaration declaration = new MethodDeclaration(declMeth.ty, argumentTypes);
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
