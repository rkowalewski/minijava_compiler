package minijava.backend.i386;

import minijava.backend.Assem;
import minijava.backend.MachineSpecifics;
import minijava.intermediate.*;
import minijava.intermediate.tree.TreeExpCONST;
import minijava.intermediate.tree.TreeStm;
import minijava.util.FiniteFunction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: kowa
 * Date: 11/26/14
 */
public class I386MachineSpecifics implements MachineSpecifics {

    @Override
    public int getWordSize() {
        return 4;
    }

    @Override
    public Temp[] getAllRegisters() {
        Temp[] allReg = new Temp[8];

        List<Temp> regList = new ArrayList<>();
        regList.addAll(I386Frame.CALLEE_SAVED);
        regList.addAll(I386Frame.CALLER_SAVED);
        regList.add(I386Frame.ebp);
        regList.add(I386Frame.esp);

        regList.toArray(allReg);

        return allReg;
    }

    @Override
    public Temp[] getGeneralPurposeRegisters() {
        Temp[] gpRegs = new Temp[6];

        List<Temp> regList = new ArrayList<>();
        regList.addAll(I386Frame.CALLEE_SAVED);
        regList.addAll(I386Frame.CALLER_SAVED);

        regList.toArray(gpRegs);

        return gpRegs;
    }

    @Override
    public Frame newFrame(Label name, int paramCount) {
        return new I386FrameImpl(name, paramCount);
    }

    @Override
    public List<Assem> spill(Frame frame, List<Assem> instrs, List<Temp> toSpill) {
        List<Assem> spilledBody = new ArrayList<>(instrs);
        for (Temp temp : toSpill) {
            int offset = ((TreeExpCONST) frame.addLocal(Frame.Location.IN_MEMORY)).value;

            for (int i = 0; i < spilledBody.size(); i++) {
                Assem instr = spilledBody.get(i);
                Temp newTemp = new Temp();

                Assem renamedInstr = instr.rename(new FiniteFunction<>(Collections.singletonMap(temp, newTemp)));

                spilledBody.set(i, renamedInstr);

                if (instr.use().contains(temp)) {
                    spilledBody.add(i, new AssemBinaryOp(AssemBinaryOp.Kind.MOV, new Operand.Reg(newTemp), new Operand.Mem(I386Frame.ebp, 0, null, 0 - offset)));
                    i++;
                }

                if (instr.def().contains(temp)) {
                    spilledBody.add(i + 1, new AssemBinaryOp(AssemBinaryOp.Kind.MOV, new Operand.Mem(I386Frame.ebp, 0, null, 0 - offset), new Operand.Reg(newTemp)));
                    i++;
                }
            }
        }

        return spilledBody;
    }

    @Override
    public Fragment<List<Assem>> codeGen(Fragment<List<TreeStm>> frag) {
        FragmentProc<List<Assem>> assemFrag = (FragmentProc<List<Assem>>) frag.accept(new I386CodegenVisitor());
        return assemFrag;
    }

    @Override
    public String printAssembly(List<Fragment<List<Assem>>> frags) {
        StringBuilder builder = new StringBuilder();

        builder.append(".intel_syntax\n");

        for (Fragment<List<Assem>> frag : frags) {

            FragmentProc<List<Assem>> fragProc = (FragmentProc<List<Assem>>) frag;

            String frameName = fragProc.frame.getName().toString();
            builder.append(".globl ").append(frameName).append("\n");
            builder.append(".type ").append(frameName).append(", ").append("@function\n");

            List<Assem> completeBody = addPrologueEpilogue(fragProc);

            for (Assem asm : completeBody) {

                String line = asm.toString();

                builder.append(line);

                char last = line.charAt(line.length() - 1);

                if (!(String.valueOf(last).equals("\n"))) {
                    builder.append("\n");
                }
            }

            builder.append("\n\n");
        }

        return builder.toString();
    }

    private List<Assem> addPrologueEpilogue(FragmentProc<List<Assem>> frag) {
        List<Assem> completeBody = new ArrayList<>(frag.body);
        List<Assem> prologue = new ArrayList<>();

        prologue.add(new AssemUnaryOp(AssemUnaryOp.Kind.PUSH, new Operand.Reg(I386Frame.ebp)));
        prologue.add(new AssemBinaryOp(AssemBinaryOp.Kind.MOV, new Operand.Reg(I386Frame.ebp), new Operand.Reg(I386Frame.esp)));
        if (frag.frame.size() > 0) {
            prologue.add(new AssemBinaryOp(AssemBinaryOp.Kind.SUB, new Operand.Reg(I386Frame.esp), new Operand.Imm(frag.frame.size())));
        }

        List<Assem> epilogue = new ArrayList<>();

        epilogue.add(new AssemBinaryOp(AssemBinaryOp.Kind.MOV, new Operand.Reg(I386Frame.esp), new Operand.Reg(I386Frame.ebp)));
        epilogue.add(new AssemUnaryOp(AssemUnaryOp.Kind.POP, new Operand.Reg(I386Frame.ebp)));

        completeBody.addAll(1, prologue);
        completeBody.addAll(completeBody.size() - 1, epilogue);

        return completeBody;
    }
}
