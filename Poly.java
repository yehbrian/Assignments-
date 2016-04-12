
public class Poly {

	public static void main(String[] args) {
				System.out.println("Please enter the first root");
				int x,y,z;
				x=-(IO.readInt());
				System.out.println("Please enter the second root");
				y=-(IO.readInt());
				System.out.println("Please enter the third root");
				z=-(IO.readInt());
				int second,first, third;
				first = x+y+z;
				third = x*y*z;
				second=(x*y)+(y*z)+(x*z);
				
				System.out.println("x^3 + " + first + "x^2 + " + second + "x + " + third );
	}

}
	