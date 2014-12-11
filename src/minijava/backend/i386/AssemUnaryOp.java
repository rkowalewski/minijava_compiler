package minijava.backend.i386;

import minijava.backend.Assem;
import minijava.intermediate.Label;
import minijava.intermediate.Temp;
import minijava.util.Function;
import minijava.util.Pair;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

final class AssemUnaryOp implements Assem {

    enum Kind {

        PUSH, POP, NEG, NOT, INC, DEC, IMUL, IDIV, ENTER
    }

    private static final Collection<Kind> relevantUseDef = Arrays.asList(
            Kind.NEG, Kind.NOT, Kind.INC, Kind.DEC, Kind.IMUL, Kind.IDIV
    );


    private final Operand op;
    private final Kind kind;

    AssemUnaryOp(Kind kind, Operand op) {
        assert ((kind == Kind.POP || kind == Kind.NEG || kind == Kind.NEG
                || kind == Kind.INC || kind == Kind.DEC || kind == Kind.IDIV)
                ? !(op instanceof Operand.Imm) : true);
        assert ((kind == Kind.ENTER) ? (op instanceof Operand.Imm) : true);
        this.op = op;
        this.kind = kind;
    }

    public List<Temp> use() {
        if (relevantUseDef.contains(this.kind) || this.kind == Kind.PUSH) {
            return op.getRelevantRegsAlloc();
        }

        return Collections.emptyList();
    }

    public List<Temp> def() {
        if (relevantUseDef.contains(this.kind) || this.kind == Kind.POP) {
            return op.getRelevantRegsAlloc();
        }

        return Collections.emptyList();
    }

    public List<Label> jumps() {
        return Collections.emptyList();
    }

    public boolean isFallThrough() {
        return true;
    }

    public Pair<Temp, Temp> isMoveBetweenTemps() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Label isLabel() {
        return null;
    }

    public String toString() {
        return "\t" + kind.toString().toLowerCase() + " " + op;
    }

    public Assem rename(Function<Temp, Temp> sigma) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
