package minijava.backend.i386;

import minijava.backend.Assem;
import minijava.intermediate.Label;
import minijava.intermediate.Temp;
import minijava.util.Function;
import minijava.util.Pair;

import java.util.List;

final class AssemUnaryOp implements Assem {

  enum Kind {

    PUSH, POP, NEG, NOT, INC, DEC, IMUL, IDIV, ENTER
  }
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
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public List<Temp> def() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public List<Label> jumps() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public boolean isFallThrough() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Pair<Temp, Temp> isMoveBetweenTemps() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Label isLabel() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public String toString() {
      return "\t" + kind.toString().toLowerCase() + " " + op;
  }

  public Assem rename(Function<Temp, Temp> sigma) {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}