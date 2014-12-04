
class SimpleArrayAccess {
    public static void main(String[] a){
	System.out.println((new Arr()).do_it(3));
    }
}

class Arr {
    int[] theArr;
    int mx;
    
    public int do_it(int m) {
      int i;
	theArr = new int[m];
  mx = m;
  i = 1;

  while( i < mx+1) {
    theArr[i-1] = i + 10;
    i = i + 1;
  }

  return this.sum();
	
    }

public int sum() {
return mx;
}
}
