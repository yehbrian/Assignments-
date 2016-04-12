
public class Taxes {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Please enter your taxable income.");
		int x = IO.readInt();
		if(x<=0){
			IO.reportBadInput();
			return;
		}
		double amount;
		amount=0;
		int y=x;
		int w=x;
		int z=x;
		if(x<=8000)
			amount=x*0.1;

		else if(x>8000 && x<=26000){
			amount=(((z-8000)*0.15)+800);

		}
		else if(x>34000 && x<80000){
			amount = ((y-34000)*0.25)+3900+800;

		}
		else if(x>80000){
			amount = ((w-80000)*0.35)+800+3900+12000;


		}
		IO.outputDoubleAnswer(amount);

	}	

}
