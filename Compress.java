
public class Compress {

	public static void main(String[] args) {
		System.out.println("Please enter your string.");
		String s = IO.readString();
		String good = "";
		char c;
		int length = 1;
		for(int i  = 0; i <s.length(); i++){
			length = 1;
			c= s.charAt(i);
			while(i+1<s.length() && s.charAt(i) == s.charAt(i+1)){
				length ++;
				i++;
			}
			if(length!=1)
				good = good + length + c; 
			else
				good = good + c;
		}
		IO.outputStringAnswer(good);
	}

}
