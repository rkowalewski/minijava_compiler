package minijava.semantic.symbol;

import minijava.semantic.node.ClassDeclaration;
import minijava.semantic.node.Declaration;
import minijava.semantic.node.MethodDeclaration;

import java.util.Iterator;

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

    public Declaration lookupField(Symbol key) {
        return cur.lookup(key, true);
    }

    public ClassDeclaration getCurrentClass() {
        return (ClassDeclaration) cur.getParentElementByKind(Declaration.Kind.CLASS);
    }

    public MethodDeclaration lookupMethodInClass(Symbol clazz, Symbol method) {
        ClassDeclaration classDecl = (ClassDeclaration) lookupTopDownWithKind(root, clazz, null);

        if (classDecl == null) {
            return null;
        }

        MethodDeclaration methodDecl = (MethodDeclaration) lookupTopDownWithKind(root, method, classDecl);

        return methodDecl;
    }

    private Declaration lookupTopDownWithKind(Scope scope, Symbol key, Declaration parent) {
        Declaration decl = scope.lookup(key, false);
        if (decl != null && (parent == null || parent.equals(scope.getParentElement()))) {
            return decl;
        } else {
            Iterator<Scope> itChildren = scope.getChildren().iterator();
            Declaration result = null;

            while(itChildren.hasNext() && result == null) {
                Scope child = itChildren.next();
                result = lookupTopDownWithKind(child, key, parent);
            }

            return result;
        }
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
