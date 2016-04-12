
public class NthPrime {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int n, bool = 1, count = 3;


		System.out.println("Enter the number that you want for prime numbers");
		n = IO.readInt();
		int x=2;
		if(n<=0){
			IO.reportBadInput();
			return;
		}
		while (x<=n)
		{
			for ( int j = 2 ; j <= (count/2) ; j++ ){
				if ( count%j == 0 )
				{
					bool = 0;
				}
			}
			if ( bool != 0 )
			{
				x++;
			}
			bool = 1;
			count++;
		}
		count--;
		IO.outputIntAnswer(count);
	}
}