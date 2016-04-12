public class StringRec  
{  
	public static void main(String[] args){
		// TODO Auto-generated method stub
		System.out.println(decompress("q9w5e2rt5y4qw2Er3T"));
	}
	public static String decompress(String compressedText)  
	{  
		if(compressedText.length()<=0){
			return compressedText;
		}
		else if(Character.isLetter(compressedText.charAt(0)) == true){  
			String first = compressedText.substring(0,1);  
			String last = compressedText.substring(1);  
			return first + decompress(last);
		}  
		else{
			char start = compressedText.charAt(0);
			int i = start-'0';
			i--;
			String first = compressedText.substring(1,2);  
			String last = compressedText.substring(1);
			if (i<2){
				compressedText = first+last;
				return decompress(compressedText);
			}
			else{
				compressedText = i+first+last;
			}
			return decompress(compressedText);  
		}
	}
}