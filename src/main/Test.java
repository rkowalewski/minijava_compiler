package main;

import minijava.backend.Assem;
import minijava.backend.MachineSpecifics;
import minijava.backend.dummymachine.IntermediateToCmm;
import minijava.backend.i386.I386CodegenVisitor;
import minijava.backend.i386.I386MachineSpecifics;
import minijava.backend.regalloc.RegisterAllocator;
import minijava.intermediate.Fragment;
import minijava.intermediate.FragmentProc;
import minijava.intermediate.Temp;
import minijava.intermediate.canon.BasicBlock;
import minijava.intermediate.canon.Canon;
import minijava.intermediate.canon.TraceSchedule;
import minijava.intermediate.tree.TreeStm;
import minijava.intermediate.visitor.IntermediateTranslationVisitor;
import minijava.semantic.symbol.SymbolTable;
import minijava.semantic.visitor.ErrorMsg;
import minijava.semantic.visitor.SymbolTableVisitor;
import minijava.semantic.visitor.TypeCheckVisitor;
import minijava.syntax.Prg;
import minijava.util.FiniteFunction;
import minijava.util.Function;
import minijava.util.Pair;
import parser.Lexer;
import parser.ParseException;
import parser.Parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Test {

    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("First argument must be file name of straigtline program.");
            System.exit(-1);
        }

        String filename = args[0];
        java.io.InputStream inp;

        try {
            inp = new FileInputStream(filename);
        } catch (FileNotFoundException e) {
            throw new Error("File not found: " + filename);
        }

        Lexer lex = new Lexer(inp);
        Parser parser = new Parser(lex);

        java_cup.runtime.Symbol parseTree;
        SymbolTable symbolTable = new SymbolTable();
        List<ErrorMsg> errors = new ArrayList<ErrorMsg>();
        try {
            try {
                parseTree = parser.parse();
                Prg prg = (Prg) parseTree.value;

                SymbolTableVisitor symbolTableVisitor = new SymbolTableVisitor(symbolTable, errors);
                prg.accept(symbolTableVisitor);

                if (errors.size() > 0) {
                    System.out.println(String.format("#errors in building symbol table of file %s: %d", filename, errors.size()));
                    for (ErrorMsg msg : errors) {
                        System.out.println(msg.getMsg());
                    }
                    System.exit(1);
                } else {
                    //Type Checking
                    TypeCheckVisitor typeCheckVisitor = new TypeCheckVisitor(symbolTable, errors);
                    prg.accept(typeCheckVisitor);

                    if (errors.size() > 0) {
                        System.out.println(String.format("#errors in type checking of file %s: %d", filename, errors.size()));

                        for (ErrorMsg msg : errors) {
                            System.out.println(msg.getMsg());
                        }

                        System.exit(1);
                    }

                    //Intermediate Translation
                    MachineSpecifics machineSpecifics = new I386MachineSpecifics();
                    IntermediateTranslationVisitor intermediateTranslation = new IntermediateTranslationVisitor(symbolTable, machineSpecifics);
                    prg.accept(intermediateTranslation);

                    if (!intermediateTranslation.getFragmentList().isEmpty()) {
                        boolean doCanon = true;

                        if (doCanon) {
                            Canon canon = new Canon();
                            BasicBlock basicBlocksBuilder = new BasicBlock();
                            TraceSchedule scheduler = new TraceSchedule();

                            List<Fragment<List<minijava.backend.Assem>>> assemblyFrags = new ArrayList<>();

                            for (Fragment<TreeStm> frag : intermediateTranslation.getFragmentList()) {
                                //Canonicalize
                                Fragment<List<TreeStm>> canonicalized = frag.accept(canon);

                                //Build Basic Blocks
                                Fragment<BasicBlock.BasicBlockList> fragBasicBlockList = canonicalized.accept(basicBlocksBuilder);
                                //Trace the Basic Blocks
                                Fragment<List<TreeStm>> scheduledFrag = fragBasicBlockList.accept(scheduler);

                                //Instruction Preselection
                                FragmentProc<List<Assem>> assemFrag = (FragmentProc<List<Assem>>) machineSpecifics.codeGen(scheduledFrag);

                                machineSpecifics.printAssembly(Collections.<Fragment<List<Assem>>>singletonList(assemFrag));
//                                assemblyFrags.add(assemFrag);

                                //generate assembly code
                                RegisterAllocator registerAllocator = new RegisterAllocator(assemFrag.body, assemFrag.frame, machineSpecifics);
                                Fragment<List<Assem>> finalFrag = new FragmentProc<>(assemFrag.frame, registerAllocator.doRegAlloc());
                                assemblyFrags.add(finalFrag);
                            }

                            System.out.println(machineSpecifics.printAssembly(assemblyFrags));
                        } else {
                            System.out.println(IntermediateToCmm.stmFragmentsToCmm(intermediateTranslation.getFragmentList()));

                        }

                    } else {
                        System.out.println("Empty Program!!");
                    }
                }
            } finally {
                inp.close();
            }
        } catch (ParseException ex) {
            System.out.println("Parse error!\n" + ex.getMessage());
            System.exit(1);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
