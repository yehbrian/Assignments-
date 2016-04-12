
public class WordCount {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		System.out.println("please enter a sentence");
		String s = IO.readString();
		System.out.println("Please enter minimum length of words");
		int length = IO.readInt();
		int counter =0 ;
		s = s.toLowerCase();
		s = s+" ";
		s= s.replaceAll("[^a-zA-Z ]", "");
		while(s.length()!=0){
			if(s.substring(0, s.indexOf(" ")).length()>=length){
				counter++;
			}
			s = s.substring(s.indexOf(" ")+1);

		}
		IO.outputIntAnswer(counter);
	}
}