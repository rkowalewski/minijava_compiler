package minijava.backend.i386;

import minijava.backend.Assem;
import minijava.intermediate.Label;
import minijava.intermediate.Temp;
import minijava.util.Function;
import minijava.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

final class AssemJump implements Assem {

    enum Kind {

        JMP, J, CALL
    }

    enum Cond {

        E, NE, L, LE, G, GE, Z
    }

    private final Kind kind;
    private final Label label;
    private final Operand dest;
    private final Cond cond;

    AssemJump(Kind kind, Label label) {
        this(kind, label, null, null);
    }

    AssemJump(Kind kind, Operand dest) {
        this(kind, null, dest, null);
    }

    AssemJump(Kind kind, Label label, Cond cond) {
        this(kind, label, null, cond);
    }

    AssemJump(Kind kind, Label label, Operand dest, Cond cond) {
        assert (kind != Kind.J || cond != null) : "J needs condition argument";
        assert (kind == Kind.CALL || label != null) : "J and JMP need label as destination";
        assert (dest == null || dest instanceof Operand.Reg) : "dynamic destination of CALL must be Reg";
        this.kind = kind;
        this.label = label;
        this.dest = dest;
        this.cond = cond;
    }

    public List<Temp> use() {
        return Collections.emptyList();
    }

    public List<Temp> def() {
        if (kind == Kind.CALL) {
            List<Temp> callerSaves = new ArrayList<>(I386Frame.CALLER_SAVED);
            callerSaves.add(I386Frame.eax);
            return callerSaves;
        }

        return Collections.emptyList();
    }

    public List<Label> jumps() {
       if (kind != Kind.CALL && label != null) {
           return Arrays.asList(label);
       }

        return Collections.emptyList();
    }

    public boolean isFallThrough() {
        return kind != Kind.JMP;
    }

    public Pair<Temp, Temp> isMoveBetweenTemps() {
        return null;
    }

    public Label isLabel() {
        return null;
    }

    public String toString() {
        if (kind == Kind.J) {
            return "\t" + kind.toString().toLowerCase() + cond.toString().toLowerCase() + " " + label;
        } else {
            return "\t" + kind.toString().toLowerCase() + " " + label;
        }
    }

    public Assem rename(Function<Temp, Temp> sigma) {
        return this;
    }
}
