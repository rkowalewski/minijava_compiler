// Should return 5

class ScopeTwo {
    public static void main (String[] argv) {
	System.out.println (new SS().run(5, 10));
    }
}

class SS {

    int n;

    public int Init (int m) {
        n = m;
        return 0;
    }

    public int run (int n, int m) {
        int k;
        k = this.Init (m);
        return n;
    }

}
