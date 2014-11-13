package minijava.semantic.symbol;

import minijava.semantic.node.ClassDeclaration;
import minijava.semantic.node.Declaration;
import minijava.semantic.node.MethodDeclaration;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * User: kowa
 * Date: 10/29/14
 */
public class Scope {
    private Scope parentScope;
    private Declaration parentElement;
    private List<Scope> children;
    private final int level;
    private int nxtPtr;
    private Map<Symbol, Declaration> records;

    public Scope(Scope parentScope) {
        this.parentScope = parentScope;
        this.children = new ArrayList<Scope>();
        this.nxtPtr = 0;
        this.level = parentScope != null ? parentScope.getLevel() + 1 : -1;
        records = new LinkedHashMap<Symbol, Declaration>();
    }

    private Scope(Scope parentScope, Declaration parentElement) {
        this(parentScope);
        this.parentElement = parentElement;
    }

    public List<Scope> getChildren() {
        return children;
    }

    public Scope getParentScope() {
        return parentScope;
    }

    public void put(Symbol key, Declaration item) {

        if (records.containsKey(key) && records.get(key).getKind() == item.getKind()) {
            throw new DuplicateSymbolException(String.format("Duplicate %s: %s is already defined in current Scope!", item.getKind(), key.toString()));
        }

        if (item.getKind() == Declaration.Kind.METHOD) {
            ((MethodDeclaration) item).setDeclaringClass((ClassDeclaration) parentElement);
        }

        records.put(key, item);
    }

    public Scope nextChild() {
        Scope nxt;
        if (nxtPtr >= children.size()) {
            Declaration parentElement = null;

            if (records.size() > 0) {
                //Keep also track of the parent element in the tree
                List<Declaration> entries = new ArrayList<Declaration>(records.values());
                parentElement = entries.get(entries.size() - 1);
            }

            nxt = new Scope(this, parentElement);
            children.add(nxt);
        } else {
            nxt = children.get(nxtPtr);
        }

        nxtPtr++;

        return nxt;
    }

    public Declaration lookup(Symbol key, boolean lookupRecursive) {
        if (records.containsKey(key)) {
            return records.get(key);
        } else {
            if (lookupRecursive) {
                if (parentScope == null) {
                    return null;
                } else {
                    return parentScope.lookup(key, lookupRecursive);
                }
            }
            return null;
        }
    }

    public Declaration getParentElementByKind(Declaration.Kind kind) {
        if (parentElement == null) {
            return null;
        }

        if (parentElement.getKind() == kind) {
            return parentElement;
        }

        return parentScope.getParentElementByKind(kind);
    }

    public Declaration getParentElement() {
        return parentElement;
    }

    public void resetScope() {
        nxtPtr = 0;
        for (int i = 0; i < children.size(); i++) {
            children.get(i).resetScope();
        }
    }

    public int getLevel() {
        return level;
    }

    public void dump() {
        dump(0);
    }

    public void dump(int level) {
        // pre-order traverse
        System.out.println("* Scope Level " + level + ":");
        for (Map.Entry<Symbol, Declaration> entry : records.entrySet()) {
            String traceLine = String.format("Symbol %s (%s) --> %s", entry.getKey(), entry.getValue().getKind(), entry.getValue().getType());
            System.out.print(traceLine + ", ");
        }
        System.out.println();

        for (Scope s : children) {
            s.dump(level + 1);
        }
    }
}
