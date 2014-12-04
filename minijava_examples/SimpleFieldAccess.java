
class SimpleFieldAccess {
    public static void main(String[] a){
	System.out.println((new Arr()).do_it(3));
    }
}

class Arr {
    int mx;
    
    public int do_it(int m) {
  mx = m;

  return this.sum();
	
    }

public int sum() {
return mx;
}
}
