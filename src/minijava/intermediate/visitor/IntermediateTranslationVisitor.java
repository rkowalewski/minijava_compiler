package minijava.intermediate.visitor;

import minijava.backend.MachineSpecifics;
import minijava.intermediate.*;
import minijava.intermediate.model.IntermediateMemOffset;
import minijava.intermediate.tree.*;
import minijava.semantic.node.ClassDeclaration;
import minijava.semantic.node.MethodDeclaration;
import minijava.semantic.node.VarDeclaration;
import minijava.semantic.symbol.Symbol;
import minijava.semantic.symbol.SymbolTable;
import minijava.semantic.visitor.DepthFirstVisitor;
import minijava.syntax.*;

import java.util.ArrayList;
import java.util.List;

/**
 * User: kowa
 * Date: 11/7/14
 */
public class IntermediateTranslationVisitor extends DepthFirstVisitor<Void>{
    private final SymbolTable symbolTable;
    private final MachineSpecifics machineSpecifics;
    private final List<Fragment<TreeStm>> fragmentList = new ArrayList<>();

    public IntermediateTranslationVisitor(SymbolTable symbolTable, MachineSpecifics machineSpecifics) {
        this.symbolTable = symbolTable;
        this.machineSpecifics = machineSpecifics;
    }

    @Override
    public Void visit(Prg prg) {
        symbolTable.resetTable();
        symbolTable.enterScope();
        super.visit(prg);
        symbolTable.exitScope();

        return null;
    }

    @Override
    public Void visit(DeclClass declClass) {
        symbolTable.enterScope();

        int offset = 0;
        int wSize = machineSpecifics.getWordSize();

        for (DeclVar field : declClass.fields) {
            VarDeclaration fieldDecl = (VarDeclaration) symbolTable.lookup(Symbol.get("v:" + field.name));
            fieldDecl.setAccess(new IntermediateMemOffset(offset));
            offset += wSize;
        }

        for (DeclMeth method : declClass.methods) {
            method.accept(this);
        }

        symbolTable.exitScope();

        return null;
    }

    @Override
    public Void visit(DeclVar declVar) {
        return super.visit(declVar);
    }

    @Override
    public Void visit(DeclMeth declMeth) {
        symbolTable.enterScope();

        ClassDeclaration klass = symbolTable.getCurrentClass();

        MethodDeclaration methodDecl = symbolTable.lookupMethodInClass(Symbol.get("c:" + klass.getClassName()), Symbol.get("m:" + declMeth.methodName));
        String fullName = klass.getClassName() + "$" + declMeth.methodName;

        Frame frame = machineSpecifics.newFrame(new Label(fullName), declMeth.parameters.size() + 1);
        methodDecl.setFrame(frame);

        for (Parameter p: declMeth.parameters) {
            //Do parameter stuff
        }

        for (DeclVar var : declMeth.localVars) {
            VarDeclaration varInfo = (VarDeclaration) symbolTable.getCur().lookup(Symbol.get("v:" + var.name), false);
            varInfo.setAccess(frame.addLocal(Frame.Location.ANYWHERE));
        }

        TreeStm bodySeq = declMeth.body.accept(new StmTranslationVisitor(symbolTable, machineSpecifics));
        TreeExp returnExp = declMeth.returnExp.accept(new ExpTranslationVisitor(symbolTable, machineSpecifics));

        createFrameProc(frame, frame.makeProc(bodySeq, returnExp));

        symbolTable.exitScope();

        return null;
    }

    @Override
    public Void visit(DeclMain declMain) {
        symbolTable.enterScope();
        symbolTable.enterScope();

        Frame frame = machineSpecifics.newFrame(new Label("main"), 0);
        TreeStm body = declMain.mainBody.accept(new StmTranslationVisitor(symbolTable, machineSpecifics));
        Temp temp = new Temp();
        TreeExp retExp = new TreeExpESEQ(
                new TreeStmMOVE(new TreeExpTEMP(temp), new TreeExpCONST(0)),
                new TreeExpTEMP(temp)
        );

        createFrameProc(frame, frame.makeProc(body, retExp));

        symbolTable.exitScope();
        symbolTable.exitScope();

        return null;
    }

    private void createFrameProc(Frame frame, TreeStm body) {
        Fragment fragmentProc = new FragmentProc(frame, body);
        fragmentList.add(fragmentProc);
    }

    public List<Fragment<TreeStm>> getFragmentList() {
        return fragmentList;
    }
}
