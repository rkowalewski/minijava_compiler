package minijava.backend.i386;

import minijava.backend.Assem;
import minijava.backend.MachineSpecifics;
import minijava.intermediate.*;
import minijava.intermediate.tree.TreeStm;

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
        throw new UnsupportedOperationException("Registers allocation not supported.");
    }

    @Override
    public Temp[] getGeneralPurposeRegisters() {
        throw new UnsupportedOperationException("Registers allocation not supported.");
    }

    @Override
    public Frame newFrame(Label name, int paramCount) {
        return new I386FrameImpl(name, paramCount);
    }

    @Override
    public List<Assem> spill(Frame frame, List<Assem> instrs, List<Temp> toSpill) {
        throw new UnsupportedOperationException("Generic machine doesn't support assembly code!");
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

            List<Assem> completeBody = addPrologueEpilogue(fragProc.body);

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

    private List<Assem> addPrologueEpilogue(List<Assem> body) {
        List<Assem> completeBody = new ArrayList<>(body);

        //Prologue
        ArrayList<Temp> calleeSaved = new ArrayList<>(I386Frame.CALLEE_SAVED);
        Collections.reverse(calleeSaved);

        for (Temp temp : calleeSaved) {
            completeBody.add(1, new AssemUnaryOp(AssemUnaryOp.Kind.PUSH, new Operand.Reg(temp)));
        }

        completeBody.add(1, new AssemBinaryOp(AssemBinaryOp.Kind.MOV, new Operand.Reg(I386Frame.ebp), new Operand.Reg(I386Frame.esp)));
        completeBody.add(1, new AssemUnaryOp(AssemUnaryOp.Kind.PUSH, new Operand.Reg(I386Frame.ebp)));

        List<Assem> epilogueList = new ArrayList<>();

        //Eplogue
        for (Temp temp : calleeSaved) {
            epilogueList.add(new AssemUnaryOp(AssemUnaryOp.Kind.POP, new Operand.Reg(temp)));
        }

        epilogueList.add(new AssemBinaryOp(AssemBinaryOp.Kind.MOV, new Operand.Reg(I386Frame.esp), new Operand.Reg(I386Frame.ebp)));
        epilogueList.add(new AssemUnaryOp(AssemUnaryOp.Kind.POP, new Operand.Reg(I386Frame.ebp)));

        completeBody.addAll(completeBody.size()-1, epilogueList);

        return completeBody;
    }
}
