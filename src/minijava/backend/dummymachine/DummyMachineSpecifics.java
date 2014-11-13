package minijava.backend.dummymachine;

import minijava.backend.Assem;
import minijava.backend.MachineSpecifics;
import minijava.intermediate.Fragment;
import minijava.intermediate.Frame;
import minijava.intermediate.Label;
import minijava.intermediate.Temp;
import minijava.intermediate.tree.TreeStm;

import java.util.List;

public class DummyMachineSpecifics implements MachineSpecifics {

  @Override
  public Frame newFrame(Label name, int params) {
    return new DummyMachineFrame(name, params);
  }

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
    throw new UnsupportedOperationException("Register allocation not supported.");
  }

  @Override
  public String printAssembly(List<Fragment<List<Assem>>> frags) {
    throw new UnsupportedOperationException("Generic machine doesn't support assembly code!");
  }

  @Override
  public Fragment<List<Assem>> codeGen(Fragment<List<TreeStm>> frag) {
    throw new UnsupportedOperationException("Generic machine doesn't support assembly code!");
  }

  @Override
  public List<Assem> spill(Frame frame, List<Assem> instrs, List<Temp> toSpill) {
    throw new UnsupportedOperationException("Generic machine doesn't support assembly code!");
  }

}
