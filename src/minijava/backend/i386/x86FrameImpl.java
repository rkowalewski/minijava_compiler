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
public class x86FrameImpl implements x86Frame {
    private final Label name;
    private final List<Temp> locals;
    private final int paramCount;

    private static int WORD_SIZE = 4;
    private static int PARAM_EBP_OFFSET = 8;

    public x86FrameImpl(x86FrameImpl frame) {
        this.name = frame.name;
        this.paramCount = 0;
        this.locals = new ArrayList<Temp>(frame.locals);
    }

    public x86FrameImpl(Label name, int paramCount) {
        this.name = name;
        this.locals = new LinkedList<Temp>();
        this.paramCount = paramCount;
    }

    @Override
    public Label getName() {
        return name;
    }

    @Override
    public int getParameterCount() {
        return paramCount;
    }

    @Override
    public TreeExp getParameter(int number) {
        if (number >= paramCount) {
            throw new IllegalArgumentException("cannot access param " + number);
        }

        return new TreeExpMEM(new TreeExpOP(TreeExpOP.Op.PLUS, new TreeExpCONST(4 * number + 8), new TreeExpTEMP(ebp)));
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
                new TreeStmMOVE(new TreeExpTEMP(eax), returnValue));
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Frame clone() {
        return new x86FrameImpl(this);
    }
}
