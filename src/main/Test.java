package main;

import minijava.backend.Assem;
import minijava.backend.MachineSpecifics;
import minijava.backend.dummymachine.IntermediateToCmm;
import minijava.backend.i386.I386MachineSpecifics;
import minijava.backend.regalloc.AssemFlowGraph;
import minijava.intermediate.Fragment;
import minijava.intermediate.FragmentProc;
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
import parser.Lexer;
import parser.ParseException;
import parser.Parser;

import java.io.*;
import java.util.ArrayList;
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

                            List<Fragment<List<TreeStm>>> scheduledFrags = new ArrayList<>();
                            List<Fragment<List<TreeStm>>> canonedFrags = new ArrayList<>();

                            List<Fragment<List<Assem>>> assemblyFrags = new ArrayList<>();

                            for (Fragment<TreeStm> frag : intermediateTranslation.getFragmentList()) {
                                //Canonicalize
                                Fragment<List<TreeStm>> canonicalized = frag.accept(canon);
                                canonedFrags.add(canonicalized);

                                //Build Basic Blocks
                                Fragment<BasicBlock.BasicBlockList> fragBasicBlockList = canonicalized.accept(basicBlocksBuilder);
                                //Trace the Basic Blocks
                                Fragment<List<TreeStm>> fragScheduled = fragBasicBlockList.accept(scheduler);

                                scheduledFrags.add(fragScheduled);

                                Fragment<List<Assem>> assemList = machineSpecifics.codeGen(fragScheduled);
                                assemblyFrags.add(assemList);

                                doRegAlloc(assemList, false);
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

    private static void doRegAlloc(Fragment<List<Assem>> frag, boolean trace) {
        FragmentProc<List<Assem>> fragProc = (FragmentProc<List<Assem>>) frag;
        AssemFlowGraph graph = new AssemFlowGraph(fragProc.body);
        graph.getInterenceGraph();

        if (trace) {

            File file = new File("/Users/kowa/Develop/projects/praktikum_compiler/temp/" + fragProc.frame.getName() + ".dot");

            try (FileOutputStream fop = new FileOutputStream(file)) {

                // if file doesn't exists, then create it
                if (!file.exists()) {
                    file.createNewFile();
                }

                // get the content in bytes
                PrintStream ps = new PrintStream(fop);
                graph.printDot(ps);
                ps.flush();
                ps.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
