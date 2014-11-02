package minijava.semantic.symbol;

import minijava.semantic.node.ClassDeclaration;
import minijava.semantic.node.Declaration;
import minijava.semantic.node.MethodDeclaration;

public class SymbolTable {

    private Scope root;		// root of the scope tree
    private Scope cur;		// current scopen

    public SymbolTable() {
        root = new Scope(null);
        cur = root;
    }

    public void enterScope() {
        cur = cur.nextChild();
    }

    public void exitScope() {
        cur = cur.getParentScope();
    }

    public void put(Symbol key, Declaration decl) {
        cur.put(key, decl);
    }

    public Declaration lookup(Symbol key) {
        return cur.lookup(key);
    }

    public ClassDeclaration getCurrentClass() {
        return (ClassDeclaration) cur.getParentElementByKind(Declaration.Kind.CLASS);
    }

    public MethodDeclaration lookupMethodInClass(Symbol clazz, Symbol method) {
        ClassDeclaration classDecl = (ClassDeclaration) root.lookupTopDownWithKind(clazz, Declaration.Kind.CLASS);
        MethodDeclaration methodDecl = (MethodDeclaration) root.lookupTopDownWithKind(method, Declaration.Kind.METHOD);

        if (methodDecl.getDeclaringClass().equals(classDecl)) {
            return methodDecl;
        }

        return null;
    }

    public Scope getRoot() {
        return root;
    }

    public Scope getCur() {
        return cur;
    }

    public void dump() {
        root.dump();
    }

    public void dumpCurrent() {
        System.err.println("=== DumpCurrent ===");
        cur.dump();
        System.err.println();
    }

    public void resetTable() {
        root.resetScope();
    }
}
