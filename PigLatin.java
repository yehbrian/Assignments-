
public class PigLatin {

	public static void main(String[] args) {
		
		System.out.println("please enter one word");
		String s = IO.readString();
		s= s.replaceAll("[^a-zA-Z]", "");
		s = s.toLowerCase();
		char first = s.charAt(0);
		if(first == 'a' || first == 'e'||  first == 'i' ||  first == 'o' ||  first == 'u'){
			s = s + "way";
		}
		else{
			s = s+ first;
			s = s+ "ay";
			s=s.substring(1);
		}
		IO.outputStringAnswer(s);
	}

}
