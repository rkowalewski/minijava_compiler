package minijava.backend.i386;

import minijava.intermediate.Frame;
import minijava.intermediate.Label;
import minijava.intermediate.Temp;
import minijava.intermediate.tree.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * User: kowa
 * Date: 11/26/14
 */
public class x86Frame implements Frame {
    private final Label name;
    private final List<Temp> params;
    private final List<Temp> locals;
    private final static Temp returnReg = new Temp();


    public x86Frame(x86Frame frame) {
        this.name = frame.name;
        this.params = new ArrayList<Temp>(frame.params);
        this.locals = new ArrayList<Temp>(frame.locals);
    }

    public x86Frame(Label name, int paramCount) {
        this.name = name;
        this.params = new ArrayList<Temp>();
        this.locals = new LinkedList<Temp>();
        for (int i = 0; i < paramCount; i++) {
            this.params.add(new Temp());
        }
    }

    @Override
    public Label getName() {
        return name;
    }

    @Override
    public int getParameterCount() {
        return params.size();
    }

    @Override
    public TreeExp getParameter(int number) {
        Temp t = params.get(number);
        return (t == null) ? null : new TreeExpTEMP(t);
    }

    @Override
    public TreeExp addLocal(Location l) {
        Temp t = new Temp();
        locals.add(t);
        return new TreeExpTEMP(t);
    }

    @Override
    public TreeStm makeProc(TreeStm body, TreeExp returnValue) {
        return new TreeStmSEQ(
                body,
                new TreeStmMOVE(new TreeExpTEMP(returnReg), returnValue));
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Frame clone() {
        return new x86Frame(this);
    }
}
