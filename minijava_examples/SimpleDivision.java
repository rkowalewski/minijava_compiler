
class SimpleDivision {
    public static void main(String[] a){
	System.out.println((new Div()).do_it(30));
    }
}

class Div {
    int div;
    int neg;
    int negMul;
    int negDiv;
    
    public int do_it(int m) {
      int x;
      int y;
      int z;
      x = 0 - 32;
      div = m/6;
  neg = x - 10;
  negMul = x * 6;
  negDiv = x / 4;
  System.out.println(div);
  System.out.println(neg);
  System.out.println(negMul);
  System.out.println(negDiv);

  return div;
}


}
