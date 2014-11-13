package minijava.semantic.symbol;

import minijava.semantic.node.ClassDeclaration;
import minijava.semantic.node.Declaration;
import minijava.semantic.node.MethodDeclaration;
import minijava.semantic.node.VarDeclaration;

import java.util.LinkedHashMap;
import java.util.Map;

public class SymbolTable {

    private Scope root;		// root of the scope tree
    private Scope cur;		// current scope
    private final Map<Symbol, ClassDeclaration> classTable = new LinkedHashMap<Symbol, ClassDeclaration>();

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

        postProcessSymbolAdded(key, decl);
    }

    public Declaration lookup(Symbol key) {
        return cur.lookup(key, true);
    }


    public ClassDeclaration getCurrentClass() {
        return (ClassDeclaration) cur.getParentElementByKind(Declaration.Kind.CLASS);
    }

    public ClassDeclaration getClassByName(Symbol key) {
        return classTable.get(key);
    }

    public MethodDeclaration lookupMethodInClass(Symbol clazz, Symbol method) {
        ClassDeclaration classDecl = classTable.get(clazz);

        if (classDecl == null) {
            return null;
        }

        MethodDeclaration methodDecl = classDecl.getMethod(method);

        return methodDecl;
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

    private void postProcessSymbolAdded(Symbol key, Declaration decl) {

        if (decl == null) {
            return;
        }

        switch (cur.getLevel()) {
            case 0:
                ClassDeclaration clazz = (ClassDeclaration) decl;
                classTable.put(key, clazz);
                break;
            case 1:
                Declaration.Kind kind = decl.getKind();
                ClassDeclaration parentClass = (ClassDeclaration) cur.getParentElementByKind(Declaration.Kind.CLASS);
                if (kind == Declaration.Kind.METHOD) {
                    parentClass.addMethod(key, (MethodDeclaration)decl);
                } else {
                    parentClass.addField(key, (VarDeclaration) decl);
                }
                break;
            case 2:
                kind = decl.getKind();
                if (kind == Declaration.Kind.PARAMETER) {
                    MethodDeclaration methodDeclaration = (MethodDeclaration) cur.getParentElement();
                    methodDeclaration.addParameterInfo(key, (VarDeclaration) decl);
                }
            default:
                break;
        }
    }
}
