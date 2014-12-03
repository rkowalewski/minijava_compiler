package minijava.intermediate;

public final class Temp implements Comparable<Temp> {

    private static int nextId = 0;

    private String name = null;
    private final int id;
    private boolean isSpecial = false;

    public Temp() {
        this.id = nextId++;
    }

    public Temp(String name) {
        this();
        this.name = name;
        this.isSpecial = true;
    }

    public static void resetCounter() {
        nextId = 0;
    }

    @Override
    public String toString() {
        return this.name == null ? "t" + id : "%" + this.name;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Temp && ((Temp) obj).id == id);
    }

    @Override
    public int hashCode() {
        return id;
    }

    public boolean isSpecial() {
        return isSpecial;
    }

    @Override
    public int compareTo(Temp o) {
        int oid = o.id;
        return (id < oid ? -1 : (id == oid ? 0 : 1));
    }
}
