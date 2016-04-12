
public class TwoSmallest {
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		System.out.println("Please enter a terminating value. Enter this again when you want to end the program.");
		double x= IO.readDouble();
		System.out.println("Please enter your set of real numbers. enter at least 2 real numbers in the set.");
		double small = Double.MAX_VALUE;
		double smaller= Double.MAX_VALUE;
	

		double z;

		int count = 1;
		do{
			z=IO.readDouble();
			if(smaller>=z && z!=x){
				small=smaller;
				smaller=z;
			}
			else if(z<=small && z>=smaller && z!=x){
				small=z;
			}
			count++;
		}while(x!=z);
		
		if(count<=3){
		IO.reportBadInput();
		return;
		}
		IO.outputDoubleAnswer(smaller);
		IO.outputDoubleAnswer(small);


	}

}