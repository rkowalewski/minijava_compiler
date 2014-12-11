package minijava.backend.i386;

import minijava.backend.Assem;
import minijava.intermediate.Label;
import minijava.intermediate.Temp;
import minijava.util.Function;
import minijava.util.Pair;

import java.util.*;

final class AssemBinaryOp implements Assem {

    enum Kind {

        MOV, //Singleton list dst and singleton list src
        ADD,
        SUB,
        SHL,
        SHR,
        SAL,
        SAR,
        AND,
        OR,
        XOR,
        TEST,
        CMP,
        LEA
    }

    private final Operand src;
    private final Operand dst;
    private final Kind kind;

    AssemBinaryOp(Kind kind, Operand dst, Operand src) {
        assert (kind != null && src != null && dst != null);
        assert (!((src instanceof Operand.Mem) && (dst instanceof Operand.Mem)));
        assert (kind != Kind.LEA || ((src instanceof Operand.Mem) && (dst instanceof Operand.Reg)));
        this.src = src;
        this.dst = dst;
        this.kind = kind;
    }

    public List<Temp> use() {

        List<Temp> useList = src.getRelevantRegsAlloc();

        if (dst instanceof Operand.Mem) {
            Set<Temp> useListSet = new HashSet<>(useList);

            useListSet.addAll(dst.getRelevantRegsAlloc());

            return new ArrayList<>(useListSet);
        }

        return useList;
    }

    public List<Temp> def() {
        if (dst instanceof Operand.Reg) {
            return Collections.singletonList(((Operand.Reg) dst).reg);
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
        return String.format("\t%s %s, %s", kind.toString().toLowerCase(), dst, src);
    }

    public Assem rename(Function<Temp, Temp> sigma) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
