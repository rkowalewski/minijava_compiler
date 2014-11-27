package minijava.backend;

import minijava.intermediate.Label;
import minijava.intermediate.Temp;
import minijava.util.Function;
import minijava.util.Pair;

import java.util.List;

// Assumption: is immutable!
public interface Assem {

    //src list
    public List<Temp> use();

    //dest list
    public List<Temp> def();

    //list of possible dest labels: if empty --> fall through
    public List<Label> jumps();

    public boolean isFallThrough();

    public Pair<Temp, Temp> isMoveBetweenTemps();

    public Label isLabel();

    public Assem rename(Function<Temp, Temp> sigma);
}
