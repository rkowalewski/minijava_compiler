package minijava.backend.i386;

import minijava.backend.Assem;
import minijava.backend.MachineSpecifics;
import minijava.intermediate.*;
import minijava.intermediate.tree.TreeStm;

import java.util.List;

/**
 * User: kowa
 * Date: 11/26/14
 */
public class x86MachineSpecifics implements MachineSpecifics {

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
        return new x86FrameImpl(name, paramCount);
    }

    @Override
    public List<Assem> spill(Frame frame, List<Assem> instrs, List<Temp> toSpill) {
        throw new UnsupportedOperationException("Generic machine doesn't support assembly code!");
    }

    @Override
    public Fragment<List<Assem>> codeGen(Fragment<List<TreeStm>> frag) {
        return frag.accept(new x86CodegenVisitor());
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

            for (Assem asm : fragProc.body) {

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
}
