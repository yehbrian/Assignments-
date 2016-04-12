package search;

import java.io.*;
import java.util.*;

/**
 * This class encapsulates an occurrence of a keyword in a document. It stores the
 * document name, and the frequency of occurrence in that document. Occurrences are
 * associated with keywords in an index hash table.
 * 
 * @author Sesh Venugopal
 * 
 */
class Occurrence {
	/**
	 * Document in which a keyword occurs.
	 */
	String document;

	/**
	 * The frequency (number of times) the keyword occurs in the above document.
	 */
	int frequency;

	/**
	 * Initializes this occurrence with the given document,frequency pair.
	 * 
	 * @param doc Document name
	 * @param freq Frequency
	 */
	public Occurrence(String doc, int freq) {
		document = doc;
		frequency = freq;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "(" + document + "," + frequency + ")";
	}
}

/**
 * This class builds an index of keywords. Each keyword maps to a set of documents in
 * which it occurs, with frequency of occurrence in each document. Once the index is built,
 * the documents can searched on for keywords.
 *
 */
public class LittleSearchEngine {

	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the associated value is
	 * an array list of all occurrences of the keyword in documents. The array list is maintained in descending
	 * order of occurrence frequencies.
	 */
	HashMap<String,ArrayList<Occurrence>> keywordsIndex;

	/**
	 * The hash table of all noise words - mapping is from word to itself.
	 */
	HashMap<String,String> noiseWords;

	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine() {
		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashMap<String,String>(100,2.0f);
	}

	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all keywords,
	 * each of which is associated with an array list of Occurrence objects, arranged
	 * in decreasing frequencies of occurrence.
	 * 
	 * @param docsFile Name of file that has a list of all the document file names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input files on disk
	 */
	public void makeIndex(String docsFile, String noiseWordsFile) 
			throws FileNotFoundException {
		// load noise words to hash table
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			String word = sc.next();
			noiseWords.put(word,word);
		}

		// index all keywords
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next();
			HashMap<String,Occurrence> kws = loadKeyWords(docFile);
			mergeKeyWords(kws);
		}

	}

	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword occurrences
	 * in the document. Uses the getKeyWord method to separate keywords from other words.
	 * 
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String,Occurrence> loadKeyWords(String docFile) 
			throws FileNotFoundException {
		File file = new File(docFile);
		HashMap<String,Occurrence> hash = new HashMap<String,Occurrence>();
		Scanner reader = new Scanner(file);
		while(reader.hasNext()){
			String word = getKeyWord(reader.next());
			if(word != null){
				if(hash.containsKey(word)){
					hash.get(word).frequency++;
				}
				else{
					Occurrence wordFound = new Occurrence(docFile, 1);
					hash.put(word, wordFound);
				}
			}
		}
		return hash;
	}

	/**
	 * Merges the keywords for a single document into the master keywordsIndex
	 * hash table. For each keyword, its Occurrence in the current document
	 * must be inserted in the correct place (according to descending order of
	 * frequency) in the same keyword's Occurrence list in the master hash table. 
	 * This is done by calling the insertLastOccurrence method.
	 * 
	 * @param kws Keywords hash table for a document
	 */
	public void mergeKeyWords(HashMap<String,Occurrence> kws) {
		HashMap<String,ArrayList <Occurrence>> hashmap = keywordsIndex;
		Iterator<String> keyWords = kws.keySet().iterator();
		while(keyWords.hasNext()){
			String wordFound = keyWords.next().toString();
			if(keywordsIndex.containsKey(wordFound)){
				Occurrence wordOccurence = new Occurrence (kws.get(wordFound).document, kws.get(wordFound).frequency);
				ArrayList<Occurrence> listOfWords = keywordsIndex.get(wordFound);
				listOfWords.add(wordOccurence);
				insertLastOccurrence(listOfWords);
				keywordsIndex.put(wordFound, listOfWords);
			}
			else{
				Occurrence wordOccurence = new Occurrence (kws.get(wordFound).document, kws.get(wordFound).frequency);
				ArrayList<Occurrence> listOfWords = new ArrayList<Occurrence>();
				listOfWords.add(wordOccurence);
				keywordsIndex.put(wordFound, listOfWords);
			}
		}
	}
	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of any
	 * TRAILING punctuation, consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
	 * 
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 */
	public String getKeyWord(String word) {
		if (word == null)
			return null;
		String fixedWord = word;
		fixedWord = fixedWord.trim();
		fixedWord = fixedWord.toLowerCase();
		for (int count = 0; count < fixedWord.length(); count++){
			char character = fixedWord.charAt(fixedWord.length()-1);
			if(punctuation(character)){
				if (fixedWord.length() == 1){
					return null;
				}
				fixedWord = fixedWord.substring(0, fixedWord.length()-1);
			}
			else
				break;
		}
		for (int counter = 0; counter < fixedWord.length(); counter++){
			char c = fixedWord.charAt(counter);
			if (!Character.isLetter(c)){
				return null;
			}
		}
		if (noiseWords.containsValue(fixedWord)){
			return null;
		}
		return fixedWord;
	}
	private static boolean punctuation(char character){
		char[] punctuations = {'.', ',', '?', ':', ';', '!'};
		for (int count = 0; count < punctuations.length; count++){
			if (character == punctuations[count]){
				return true;
			}
		}
		return false;
	}

	/**
	 * Inserts the last occurrence in the parameter list in the correct position in the
	 * same list, based on ordering occurrences on descending frequencies. The elements
	 * 0..n-2 in the list are already in the correct order. Insertion of the last element
	 * (the one at index n-1) is done by first finding the correct spot using binary search, 
	 * then inserting at that spot.
	 * 
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary search process,
	 *         null if the size of the input list is 1. This returned array list is only used to test
	 *         your code - it is not used elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
		ArrayList<Integer> listOfWords = new ArrayList<Integer>(5);
		int length = occs.size();
		if(length==1){
			return null;
		}
		else{
			Occurrence temp = occs.get(occs.size()-1);
			occs.remove(occs.size()-1);
			int highestValue = 0;
			int lowestValue = occs.size()-1;
			int mid = 0;
			int count = temp.frequency;
			while(highestValue <= lowestValue) {
				mid = (lowestValue + highestValue) / 2;
				Occurrence toFind = occs.get(mid);
				int last = toFind.frequency;
				if(last == count){
					listOfWords.add(mid);
					break;
				}
				if(last < count){
					lowestValue = mid -1;
					listOfWords.add(mid);
				}
				if(last > count){
					highestValue = mid + 1;
					listOfWords.add(mid);
					mid = mid+1;
				}
			}
			occs.add(mid, temp);
			return listOfWords;
		}
	}
	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
	 * document. Result set is arranged in desclasting order of occurrence frequencies. (Note that a
	 * matching document will only appear once in the result.) Ties in frequency values are broken
	 * in favor of the first keyword. (That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2
	 * also with the same frequency f1, then doc1 will appear before doc2 in the result. 
	 * The result set is limited to 5 entries. If there are no matching documents, the result is null.
	 * 
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of NAMES of documents in which either kw1 or kw2 occurs, arranged in desclasting order of
	 *         frequencies. The result size is limited to 5 documents. If there are no matching documents,
	 *         the result is null.
	 */
	public ArrayList<String> top5search(String kw1, String kw2) {
		kw1 = kw1.toLowerCase();
		kw2 = kw2.toLowerCase();
		ArrayList<String> toReturn = new ArrayList<String>();
		ArrayList<Occurrence> first = new ArrayList<Occurrence>();
		if(!(keywordsIndex.get(kw1) == null))
			first = keywordsIndex.get(kw1);
		ArrayList<Occurrence> second = new ArrayList<Occurrence>();
		if(keywordsIndex.get(kw2) != null)
			second = keywordsIndex.get(kw2);
		for(int counter = 0; counter < first.size(); counter++){
			if(toReturn.size() <= 4){
				String documentOne = first.get(counter).document;
				int LIST1 = first.get(counter).frequency;
				for(int i = 0; i < second.size(); i++){
					String documentTwo = second.get(i).document;
					int LIST2 = second.get(i).frequency;
					if(LIST2 <= LIST1){
						if(!toReturn.contains(documentOne) && toReturn.size() <= 4){
							toReturn.add(documentOne);
						}
					}
					if(LIST2 >= LIST1){
						if(!toReturn.contains(documentTwo) && toReturn.size() <= 4){
							toReturn.add(documentTwo);
						}
					}
				}
			}
		}
		if(toReturn.size() == 0){
			return null;
		}
		return toReturn;
	}
}