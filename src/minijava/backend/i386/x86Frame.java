package minijava.backend.i386;

import minijava.intermediate.Frame;
import minijava.intermediate.Temp;

/**
 * User: kowa
 * Date: 12/2/14
 */
public interface x86Frame extends Frame {
    //Caller-Save Registers
    static final Temp eax = new Temp("eax");
    static final Temp ecx = new Temp("ecx");
    static final Temp edx = new Temp("edx");

    //Callee-Save Registers
    static final Temp ebx = new Temp("ebx");
    static final Temp edi = new Temp("edi");
    static final Temp esi = new Temp("esi");

    static final Temp ebp = new Temp("ebp");
    static final Temp esp = new Temp("esp");
}
