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
       
		if (deckRear == null){
                return;
       
		}
        CardNode card = deckRear;
        while (card.cardValue != 27){
        	card = card.next;
        }      
        int temp = card.cardValue;
        card.cardValue = card.next.cardValue;  
        card.next.cardValue = temp;
       
        return;
	}

	/**
	 * Implements Step 2 - Joker B - on the deck.
	 */
	void jokerB() {
	   
		if (deckRear == null){
	        return;
	   
		}
	    CardNode card = deckRear;
	   
	    while(card.cardValue != 28){
	    	card = card.next;
	    }
	    
	    for (int i = 0; i < 2 ;i++){
	        int temp = card.cardValue;
	        card.cardValue = card.next.cardValue;
	        card.next.cardValue = temp;
	        card = card.next;
	    }
	   
	    return;
	}
	
	/**
	 * Implements Step 3 - Triple Cut - on the deck.
	 */
	void tripleCut() {
		 if(deckRear == null){
             return;
		 }
     CardNode last = deckRear;
     CardNode first = last.next;
     if (first.cardValue == 27 || first.cardValue == 28){
             CardNode jokerB = first.next;
             while(jokerB.cardValue != 27 && jokerB.cardValue != 28){
                     jokerB = jokerB.next;
             }
             deckRear = jokerB;
             return;
     }
     CardNode tail = deckRear;
     if(tail.cardValue == 27 || tail.cardValue == 28){
             CardNode jokerA = tail.next;
             CardNode previous = null;
             while(jokerA.cardValue != 27 && jokerA.cardValue != 28){
                     previous = jokerA;
                     jokerA = jokerA.next;
             }
             deckRear = previous;
             return;
     }
    
     CardNode card = deckRear.next;
     CardNode prevcard = null;
     while(card.cardValue != 27 && card.cardValue != 28){
             prevcard = card;
             card = card.next;
     }
     CardNode end = prevcard;
     CardNode firstJoker = card;
     card = card.next;
     while(card.cardValue != 27 && card.cardValue != 28){
    	 card = card.next;
     }
     CardNode front = card.next;
    
     card.next = deckRear.next;
     deckRear.next = firstJoker;
     deckRear = end;
     end.next = front;
     return;
		}
		
	
	
	/**
	 * Implements Step 4 - Count Cut - on the deck.
	 */
	void countCut() {		
		CardNode secondlast = deckRear.next;
		while(secondlast.next != deckRear){
			secondlast = secondlast.next;
		}
		
		int count = deckRear.cardValue;
		if (count == 28){
			count = 27;
		}
		
		CardNode card = deckRear;
		for(int i = 0; i < count; i++){
			card = card.next;
		}
		
		secondlast.next = deckRear.next;
		CardNode first = card.next;
		card.next = deckRear;
		deckRear.next = first;
		card = deckRear;
		
		for(int j = 0; j < 28; j++){
			card = card.next;
		}
		
		return;
	}
	
        /**
         * Gets a key. Calls the four steps - Joker A, Joker B, Triple Cut, Count Cut, then
         * counts down based on the value of the first card and extracts the next card value 
         * as key, but if that value is 27 or 28, repeats the whole process.
         * 
         * @return Key between 1 and 26
         */
	int getKey() {
		while(true){
			jokerA();
			jokerB();
			tripleCut();
			countCut();
			int count = deckRear.next.cardValue;
			if(count == 28){
				count = 27;
			}
			
			CardNode card = deckRear;
			for(int i = 0; i < count; i++){
				card = card.next;
			}
			
			card = card.next;
			if(card.cardValue != 27 || card.cardValue != 28){
				return card.cardValue;
			}
		}
	    
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
		String message2 = message.toUpperCase();
		String enc = "";
		int length = message2.length();
		for(int i = 0; i < length; i++){
			char c = message2.charAt(i);
			if(Character.isLetter(c) == true){
				int key = getKey();
				int letter = c - 'A' + 1;
				int sum = letter + key;
				if(sum > 26){
					sum = sum - 26;
				}
				char c2 = (char)(sum + 'A' - 1);
				enc = enc + c2;
			}
		}
	    return enc;
	}
	
	/**
	 * Decrypts a message, which consists of upper case letters only
	 * 
	 * @param message Message to be decrypted
	 * @return Decrypted message, a sequence of upper case letters only
	 */
	public String decrypt(String message) {	
		String message2 = message.toUpperCase();
		String dec = "";
		int length = message2.length();
		for(int i = 0; i < length; i++){
			char c = message2.charAt(i);
			if(Character.isLetter(c) == true){
				int key = getKey();
				int letter = c - 'A' + 1;
				if(letter <= key){
					letter = letter + 26;
				}
				int sum = letter - key;
				char c2 = (char)(sum + 'A' - 1);
				dec = dec + c2;
			}
		}
	    return dec;
	}
}
