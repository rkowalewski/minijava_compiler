package minijava.backend.i386;

import minijava.backend.Assem;
import minijava.backend.MachineSpecifics;
import minijava.backend.dummymachine.DummyMachineFrame;
import minijava.intermediate.Fragment;
import minijava.intermediate.Frame;
import minijava.intermediate.Label;
import minijava.intermediate.Temp;
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
        return new DummyMachineFrame(name, paramCount);
    }

    @Override
    public List<Assem> spill(Frame frame, List<Assem> instrs, List<Temp> toSpill) {
        throw new UnsupportedOperationException("Generic machine doesn't support assembly code!");
    }

    @Override
    public Fragment<List<Assem>> codeGen(Fragment<List<TreeStm>> frag) {
        return frag.accept(new CodegenVisitor());
    }

    @Override
    public String printAssembly(List<Fragment<List<Assem>>> frags) {
        //Prologue

        //Body

        //Epilogue

        return "";
    }
}
