package minijava.semantic.visitor;

import minijava.semantic.node.ClassDeclaration;
import minijava.semantic.node.MethodDeclaration;
import minijava.semantic.node.Type;
import minijava.semantic.node.VarDeclaration;
import minijava.semantic.symbol.Symbol;
import minijava.semantic.symbol.SymbolTable;
import minijava.syntax.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * User: kowa
 * Date: 10/28/14
 */
public class SymbolTableVisitor extends DepthFirstVisitor {
    private final SymbolTable symbolTable;
    private final Map<String, ClassDeclaration> classTable = new HashMap<String, ClassDeclaration>();
    private ClassDeclaration currentClass;
    private MethodDeclaration currentMethod;
    private int errorCount = 0;

    public SymbolTableVisitor()  {
        this.symbolTable = new SymbolTable();
        currentClass = null;
        currentMethod = null;
        symbolTable.beginScope();
    }

    @Override
    public void visit(DeclClass declClass) {
        if (classTable.containsKey(declClass.className)) {
            reportError();
        }

        currentClass = new ClassDeclaration(declClass.className, Type.getType(declClass.className));;
        symbolTable.push(declClass.className, currentClass);

        //Begin Scope
        currentMethod = null;
        symbolTable.beginScope();

        for (DeclVar field : declClass.fields) {
            field.accept(this);
        }

        for (DeclMeth method : declClass.methods) {
            method.accept(this);
        }

        symbolTable.endScope();

        currentClass = null;
    }

    @Override
    public void visit(DeclVar declVar) {
        VarDeclaration varDeclaration = new VarDeclaration(Type.getType(declVar.ty.toString()));

        Symbol prevDecl = symbolTable.scopeContains(declVar.name);
        if (prevDecl != null && (prevDecl.getDeclaration() instanceof VarDeclaration) && ((VarDeclaration)prevDecl.getDeclaration()).getDeclaringClass() != null && ((VarDeclaration)prevDecl.getDeclaration()).getDeclaringClass() == currentClass) {
            reportError();
        }
        symbolTable.push(declVar.name, varDeclaration);

        if (currentMethod != null) {
            //currentMethod.incrementLocalCount();
            //decl.setLocalPosition(currentMethodDeclaration.getLocalCount());
            currentMethod.addLocalVariable(declVar.name, varDeclaration);
        } else {
//            decl.setInstancePosition(currentClassDeclaration.getInstanceVariableCount());
            currentClass.addField(declVar.name, varDeclaration);
        }

        varDeclaration.setDeclaringClass(currentClass);
    }

    @Override
    public void visit(DeclMeth declMeth) {
        // Get the parameter types
        ArrayList<Type> argumentTypes = new ArrayList<Type>();

        for (Parameter param : declMeth.parameters) {
            argumentTypes.add( Type.getType(param.ty.toString()) );
        }

        // Push the method declaration onto the symbol table
        MethodDeclaration declaration = new MethodDeclaration( Type.getType(declMeth.ty.toString()), argumentTypes, currentClass);
        MethodDeclaration previousDeclaration = currentClass.getMethod(declMeth.methodName);
        if (previousDeclaration != null && previousDeclaration.getDeclaringClass() == currentClass) {
            //Method already defined
            reportError();
        }
        symbolTable.push(declMeth.methodName, declaration);
        currentMethod = declaration;
        currentClass.addMethod(declMeth.methodName, declaration);

        // Increment the scope
        symbolTable.beginScope();

        for (Parameter param : declMeth.parameters) {
            param.accept(this);
        }

        for(DeclVar declVar : declMeth.localVars) {
            declVar.accept(this);
        }

        symbolTable.endScope();
    }

    @Override
    public void visit(Parameter parameter) {
        //Delegate to DeclVar
        DeclVar declVar = new DeclVar(parameter.ty, parameter.id);
        declVar.accept(this);
    }

    @Override
    public void visit(DeclMain declMain) {
        ClassDeclaration decl = new ClassDeclaration(declMain.className, Type.getType(declMain.className));
        classTable.put(declMain.className, decl);
    }

    private void reportError() {
        this.errorCount++;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }
}
