package minijava.intermediate;

public final class Temp implements Comparable<Temp> {

    private static int nextId = 0;

    private final int id;
    private String name = null;
    private boolean fixedColor;

    public Temp() {
        this.id = nextId++;
        this.fixedColor = false;
    }

    public Temp(final String regName) {
        this();
        if (regName == null) throw new IllegalArgumentException("The name of a temp must not be null");
        this.name = regName;
        this.fixedColor = true;
    }

    public static void resetCounter() {
        nextId = 0;
    }

    @Override
    public String toString() {
        return name == null ? "t" + id : "%" + name;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Temp && ((Temp) obj).id == id);
    }

    @Override
    public int hashCode() {
        return id;
    }

    public boolean isFixedColor() {
        return fixedColor;
    }

    @Override
    public int compareTo(Temp o) {
        int oid = o.id;
        return (id < oid ? -1 : (id == oid ? 0 : 1));
    }
}
