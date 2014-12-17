package minijava.backend.regalloc;

import minijava.util.SimpleGraph;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/**
 * User: kowa
 * Date: 12/17/14
 */
public abstract class PrintableGraph<T> extends SimpleGraph<T> {

    private static final String BASE = "/Users/kowa/Develop/projects/praktikum_compiler/temp";

    public void printGraph(String name) {
        if (name != null && !name.isEmpty()) {
            if (!String.valueOf(name.charAt(0)).equals("/")) {
                name = "/" + name;
            }

            File file = new File(BASE + name + ".dot");

            try (FileOutputStream fop = new FileOutputStream(file)) {

                // if file doesn't exists, then create it
                if (!file.exists()) {
                    file.createNewFile();
                }

                // get the content in bytes
                PrintStream ps = new PrintStream(fop);
                this.printDot(ps);
                ps.flush();
                ps.close();

            } catch (IOException e) {
                throw new RuntimeException("cannot print graph", e);
            }
        }
    }
}
