package solitaire;

import java.io.IOException;
import java.util.Scanner;
import java.util.Random;
import java.util.NoSuchElementException;

/**
 * This class implements a simplified version of Bruce Schneier's Solitaire Encryption algorithm.
 * 
 * @author RU NB CS112
 */
public class Solitaire {

	/**
	 * Circular linked list that is the deck of cards for encryption
	 */
	CardNode deckRear;

	/**
	 * Makes a shuffled deck of cards for encryption. The deck is stored in a circular
	 * linked list, whose last node is pointed to by the field deckRear
	 */
	public void makeDeck() {
		// start with an array of 1..28 for easy shuffling
		int[] cardValues = new int[28];
		// assign values from 1 to 28
		for (int i=0; i < cardValues.length; i++) {
			cardValues[i] = i+1;
		}

		// shuffle the cards
		Random randgen = new Random();
		for (int i = 0; i < cardValues.length; i++) {
			int other = randgen.nextInt(28);
			int temp = cardValues[i];
			cardValues[i] = cardValues[other];
			cardValues[other] = temp;
		}

		// create a circular linked list from this deck and make deckRear point to its last node
		CardNode cn = new CardNode();
		cn.cardValue = cardValues[0];
		cn.next = cn;
		deckRear = cn;
		for (int i=1; i < cardValues.length; i++) {
			cn = new CardNode();
			cn.cardValue = cardValues[i];
			cn.next = deckRear.next;
			deckRear.next = cn;
			deckRear = cn;
		}
	}

	/**
	 * Makes a circular linked list deck out of values read from scanner.
	 */
	public void makeDeck(Scanner scanner) 
			throws IOException {
		CardNode cn = null;
		if (scanner.hasNextInt()) {
			cn = new CardNode();
			cn.cardValue = scanner.nextInt();
			cn.next = cn;
			deckRear = cn;
		}
		while (scanner.hasNextInt()) {
			cn = new CardNode();
			cn.cardValue = scanner.nextInt();
			cn.next = deckRear.next;
			deckRear.next = cn;
			deckRear = cn;
		}
	}

	/**
	 * Implements Step 1 - Joker A - on the deck.
	 */
	void jokerA() {
		int temp;
		CardNode ptr = this.deckRear;
		while(ptr.cardValue != 27)
		{
			ptr = ptr.next;
		}
		temp = ptr.cardValue;
		ptr.cardValue = ptr.next.cardValue;
		ptr.next.cardValue = temp;
	}

	/**
	 * Implements Step 2 - Joker B - on the deck.
	 */
	void jokerB() {
		int temp;
		int temp2;
		CardNode ptr = this.deckRear;
		while(ptr.cardValue != 28)
		{
			ptr = ptr.next;
		}
		temp = ptr.next.cardValue;
		temp2 = ptr.next.next.cardValue;
		ptr.cardValue = temp;
		ptr.next.cardValue = temp2;
		ptr.next.next.cardValue = 28;  
	}

	/**
	 * Implements Step 3 - Triple Cut - on the deck.
	 */
	void tripleCut() {
		int beforeFirst = 0;
		int center = 0;
		int after = 0;
		CardNode last = deckRear;
		CardNode first = deckRear.next;
		CardNode ptr = deckRear.next;



		if(first.cardValue == 27 || first.cardValue == 28){

			if(first.cardValue == 27){
				CardNode Joker = new CardNode();
				Joker.cardValue = 28;
				while(ptr.cardValue != 28){
					ptr = ptr.next;
					deckRear = ptr;
				}

			}
			else if(first.cardValue == 28){
				CardNode Joker = new CardNode();
				Joker.cardValue = 27;
				while(ptr.cardValue != 27){
					ptr = ptr.next;
					deckRear = ptr;
				}
			}
			return;
		}


		CardNode temp = new CardNode();
		temp = deckRear;

		int one = 0;
		while (temp.next.cardValue != 27) {
			temp = temp.next;
			one++;
		}

		temp = deckRear;
		int two = 0;
		while (temp.next.cardValue != 28) {
			temp = temp.next;
			two++;
		}

		temp = deckRear;

		int firstJoker = 0;
		int secondJoker = 0;

		if (one > two) {
			firstJoker = 28;
			secondJoker = 27;

		}

		else if (two > one) {
			firstJoker = 27;
			secondJoker = 28;
		}

		while (temp.next.cardValue != firstJoker) {

			temp = temp.next;
			beforeFirst++;
		}

		CardNode beforeJoker = new CardNode();
		beforeJoker = temp;

		temp = temp.next.next;
		while (temp.cardValue != secondJoker) {

			center++;
			temp = temp.next;

		}

		CardNode afterJoker = new CardNode();
		afterJoker = temp;

		after = 26 - beforeFirst - center;

		CardNode firstPart = new CardNode();
		firstPart = deckRear.next;
		CardNode lastPart = new CardNode();
		lastPart = beforeJoker;
		CardNode firstPartRear = new CardNode();
		firstPartRear = afterJoker.next;
		CardNode lastPartRear = new CardNode();
		lastPartRear = deckRear;      

		if (after == 0 && beforeFirst == 0) {
			return;
			//fix
		}


		else if (after == 0 && beforeFirst != 0) {
			deckRear = lastPart;
			//fix
		}

		else if (after != 0 && beforeFirst == 0) {
			//fix
		}

		else {
			lastPartRear.next = lastPart.next;
			afterJoker.next = firstPart;
			lastPart.next = firstPartRear;
			deckRear = lastPart;
			return;

		}
	}



	/**
	 * Implements Step 4 - Count Cut - on the deck.
	 */
	void countCut() {
		CardNode beforeRear = deckRear;
		CardNode front = deckRear.next;
		while(beforeRear.next!= deckRear){
			beforeRear = beforeRear.next;
		}
		CardNode temp = deckRear.next;

		int counter = 0;
		int value = deckRear.cardValue;

		if(deckRear.cardValue == 28){
			value = 27;
		}
		while(counter < value && temp != deckRear){
			CardNode toAdd = new CardNode();
			toAdd.cardValue = front.cardValue; //set value to first node
			toAdd.next = deckRear; //sets new node before rear
			beforeRear.next = toAdd; // sets new node after second to last node
			beforeRear = toAdd; //sets beforerear to new before rear
			counter++; //increment
			temp = temp.next;
			front = temp;
			front.next = temp.next;
			deckRear.next = front;

		}
		if(counter == deckRear.cardValue){
			deckRear.next = temp;
		}

	}

	/**
	 * Gets a key. Calls the four steps - Joker A, Joker B, Triple Cut, Count Cut, then
	 * counts down based on the value of the first card and extracts the next card value 
	 * as key. But if that value is 27 or 28, repeats the whole process (Joker A through Count Cut)
	 * on the latest (current) deck, until a value less than or equal to 26 is found, which is then returned.
	 * 
	 * @return Key between 1 and 26
	 */
	int getKey() {
		CardNode use = null;
		int counter =0;
		int key = 0;
		do{
			jokerA();
			jokerB();
			tripleCut();
			countCut();
			printList(deckRear);
			use = deckRear.next;
			counter = use.cardValue;
			if(counter == 28)
				counter = 27;
			while(counter > 0){
				use = use.next;
				counter--;
			}
			key = use.cardValue;
		}while(key == 27 || key == 28);
		System.out.println(key);
		return key;
	}


	/**
	 * Utility method that prints a circular linked list, given its rear pointer
	 * 
	 * @param rear Rear pointer
	 */
	private static void printList(CardNode rear) {
		if (rear == null) { 
			return;
		}
		System.out.print(rear.next.cardValue);
		CardNode ptr = rear.next;
		do {
			ptr = ptr.next;
			System.out.print("," + ptr.cardValue);
		} while (ptr != rear);
		System.out.println("\n");
	}

	/**
	 * Encrypts a message, ignores all characters except upper case letters
	 * 
	 * @param message Message to be encrypted
	 * @return Encrypted message, a sequence of upper case letters only
	 */
	public String encrypt(String message) {	

		String upper = message.toUpperCase();
		String x = "";
		int encryption;

		for(int i = 0; i < upper.length(); i++){
			if(Character.isLetter(upper.charAt(i)) == true){
				int word =upper.charAt(i) - 'A' +1;
				int key = getKey();
				if((word + key) > 26){
					encryption = word +key - 26;
				}
				else {
					encryption = word + key;
				}
				x = x + (char)(encryption - 1 + 'A');
			}
		}
		return x;

	}



	/**
	 * Decrypts a message, which consists of upper case letters only
	 * 
	 * @param message Message to be decrypted
	 * @return Decrypted message, a sequence of upper case letters only
	 */
	public String decrypt(String message) {	
		String upper = message.toUpperCase();
		String x = "";
		int encryption;

		for(int i = 0; i < upper.length(); i++){
			if(Character.isLetter(upper.charAt(i)) == true){
				int word =upper.charAt(i) - 'A' +1;
				int key = getKey();

				if((word - key) < 0){
					encryption = word - key + 26;
				}
				else {
					encryption = word - key;

				}
				x = x + (char)(encryption - 1 + 'A');
			}
		}
		return x;
	}
}