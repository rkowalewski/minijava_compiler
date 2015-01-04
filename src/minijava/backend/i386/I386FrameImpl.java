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
public class I386FrameImpl implements I386Frame {
    private final Label name;
    private final List<Temp> locals;
    private final int paramCount;
    private int frameSize = 0;

    public I386FrameImpl(I386FrameImpl frame) {
        this.name = frame.name;
        this.paramCount = 0;
        this.locals = new ArrayList<Temp>(frame.locals);
    }

    public I386FrameImpl(Label name, int paramCount) {
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
        if (number >= paramCount || number < 0) {
            throw new IllegalArgumentException("cannot access param " + number);
        }

        //return value
        return new TreeExpMEM(new TreeExpOP(TreeExpOP.Op.PLUS, new TreeExpCONST(4 * number + 8), new TreeExpTEMP(ebp)));
    }

    @Override
    public TreeExp addLocal(Location l) {
        if (l == Location.IN_MEMORY) {
            //Increment the frame size when a variable has to be spilled
            frameSize += 4;
            return new TreeExpCONST(frameSize);
        } else {
            Temp t = new Temp();
            locals.add(t);
            return new TreeExpTEMP(t);
        }
    }

    @Override
    public TreeStm makeProc(TreeStm body, TreeExp returnValue) {
        return new TreeStmSEQ(
                body,
                new TreeStmMOVE(new TreeExpTEMP(eax), returnValue));
    }

    @Override
    public int size() {
        return frameSize;
    }

    @Override
    public Frame clone() {
        return new I386FrameImpl(this);
    }
}
