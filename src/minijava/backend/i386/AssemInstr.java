package minijava.backend.i386;

import minijava.backend.Assem;
import minijava.intermediate.Label;
import minijava.intermediate.Temp;
import minijava.util.Function;
import minijava.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

final class AssemInstr implements Assem {

    enum Kind {

        RET, LEAVE, NOP
    }

    private final Kind kind;

    AssemInstr(Kind kind) {
        this.kind = kind;
    }

    public List<Temp> use() {
        if (kind == Kind.RET || kind == Kind.LEAVE) {
            List<Temp> uses = new ArrayList<>(I386Frame.CALLEE_SAVED);
            uses.add(I386Frame.eax);
            return uses;
        }
        return Collections.emptyList();
    }

    public List<Temp> def() {
        return Collections.emptyList();
    }

    public List<Label> jumps() {
        return Collections.emptyList();
    }

    public boolean isFallThrough() {
        return false;
    }

    public Pair<Temp, Temp> isMoveBetweenTemps() {
        return null;
    }

    public Label isLabel() {
        return null;
    }

    public String toString() {
        return "\t" + kind.toString().toLowerCase();
    }

    public Assem rename(Function<Temp, Temp> sigma) {
        return this;
    }
}
