package minijava.backend.i386;

import minijava.intermediate.Temp;
import minijava.util.Function;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

abstract class Operand {

    final static class Imm extends Operand {

        public final int imm;

        public Imm(Integer imm) {
            assert (imm != null);
            this.imm = imm;
        }

        @Override
        public Operand rename(Function<Temp, Temp> sigma) {
            return this;
        }

        @Override
        public String toString() {
            return Integer.toString(imm);
        }

        @Override
        public List<Temp> getRelevantRegsAlloc() {
            return Collections.emptyList();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Imm imm1 = (Imm) o;

            if (imm != imm1.imm) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return imm;
        }
    }

    final static class Reg extends Operand {

        public Temp reg;

        public Reg(Temp reg) {
            assert (reg != null);
            this.reg = reg;
        }

        @Override
        public String toString() {
            return reg.toString();
        }

        @Override
        public Operand rename(Function<Temp, Temp> sigma) {
            return new Reg(sigma.apply(reg));
        }

        @Override
        public List<Temp> getRelevantRegsAlloc() {
            if (I386Frame.esp.equals(reg) || I386Frame.ebp.equals(reg)) {
                return Collections.emptyList();
            }

            return Collections.singletonList(reg);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Reg reg1 = (Reg) o;

            if (!reg.equals(reg1.reg)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return reg.hashCode();
        }
    }

    final static class Mem extends Operand {

        public final Temp base;  // base adress (for example of array)
        public final Integer scale; // null or 1, 2, 4 or 8; --> usually 4 Bytes (DWORD) in 32 bit address mode
        public final Temp index;  // index (for example the index of array item)
        public final int displacement; //usually 0

        public Mem(Temp base, Integer scale, Temp index, int displacement) {
            assert (scale == null || (scale == 1 || scale == 2 || scale == 4 || scale == 8));
            this.base = base;
            this.scale = scale;
            this.index = index;
            this.displacement = displacement;
        }

        public Mem(Temp base) {
            this(base, null, null, 0);
        }

        @Override
        public Operand rename(Function<Temp, Temp> sigma) {
            return new Mem(base != null ? sigma.apply(base) : null, scale,
                    index != null ? sigma.apply(index) : null, displacement);
        }

        private boolean isRelevantForRegAlloc(Temp t) {
            return t != null && !(I386Frame.esp.equals(t) || I386Frame.ebp.equals(t));
        }

        @Override
        public List<Temp> getRelevantRegsAlloc() {
            List<Temp> list = new ArrayList<>();

            if (isRelevantForRegAlloc(base)) {
                list.add(base);
            }

            if (isRelevantForRegAlloc(index)) {
                list.add(index);
            }

            return list;
        }

        @Override
        public String toString() {
            if (scale != null && index != null) {
                return String.format("DWORD PTR [%s + %s * %s + %s]", base, scale, index, displacement);
            } else if (index != null) {
                return String.format("DWORD PTR [%s + %s + %s]", base, index, displacement);
            } else if (displacement != 0) {
                return String.format("DWORD PTR [%s %s %s]", base, displacement > 0 ? "+" : "-", Math.abs(displacement));
            } else {
                return String.format("DWORD PTR [%s]", base);
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Mem mem = (Mem) o;

            if (displacement != mem.displacement) return false;
            if (base != null ? !base.equals(mem.base) : mem.base != null) return false;
            if (index != null ? !index.equals(mem.index) : mem.index != null) return false;
            if (scale != null ? !scale.equals(mem.scale) : mem.scale != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = base != null ? base.hashCode() : 0;
            result = 31 * result + (scale != null ? scale.hashCode() : 0);
            result = 31 * result + (index != null ? index.hashCode() : 0);
            result = 31 * result + displacement;
            return result;
        }
    }

    public abstract Operand rename(Function<Temp, Temp> sigma);

    public abstract List<Temp> getRelevantRegsAlloc();
}
