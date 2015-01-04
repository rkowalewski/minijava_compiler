package minijava.backend.i386;

import minijava.backend.Assem;
import minijava.intermediate.Label;
import minijava.intermediate.Temp;
import minijava.util.Function;
import minijava.util.Pair;

import java.util.*;

final class AssemUnaryOp implements Assem {

    enum Kind {

        PUSH, POP, NEG, NOT, INC, DEC, IMUL, IDIV, ENTER
    }

    private final Operand op;
    private final Kind kind;

    AssemUnaryOp(Kind kind, Operand op) {
        if (kind == Kind.ENTER) {
            throw new IllegalArgumentException("Do no use ENTER in assembly");
        }

        assert ((kind == Kind.POP || kind == Kind.NEG || kind == Kind.NEG
                || kind == Kind.INC || kind == Kind.DEC || kind == Kind.IDIV)
                ? !(op instanceof Operand.Imm) : true);
        assert ((kind == Kind.ENTER) ? (op instanceof Operand.Imm) : true);
        this.op = op;
        this.kind = kind;
    }

    public List<Temp> use() {
        List<Temp> uses = new ArrayList<>();

        if (kind == Kind.IMUL || kind == Kind.IDIV) {
            uses.add(I386Frame.eax);
            uses.add(I386Frame.edx);
        }

        if (kind != null && kind != Kind.POP) {
            uses.addAll(op.getRelevantRegsAlloc());
        }

        return uses;
    }

    public List<Temp> def() {
        if (this.kind == Kind.IMUL || this.kind == Kind.IDIV) {
            return Arrays.asList(I386Frame.edx, I386Frame.eax);
        } else if (kind != null && kind != Kind.PUSH) {
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
        return null;
    }

    public Label isLabel() {
        return null;
    }

    public String toString() {
        return "\t" + kind.toString().toLowerCase() + " " + op;
    }

    public Assem rename(Function<Temp, Temp> sigma) {
        return new AssemUnaryOp(this.kind, op.rename(sigma));
    }
}
