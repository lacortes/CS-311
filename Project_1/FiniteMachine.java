import java.util.*;

/**
 * CS-311
 * FiniteMachine.java
 * Purpose: Implement a universal finite machine, that can process one string at a time
 * 
 * @author Luis Cortes
 * 01/19/17
 */


public class FiniteMachine {
	private ArrayList<Character> alphabet; // Refer to alphabet symbol by index 
	private ArrayList<Boolean> finalStates;
	private LinkedList<Character> string; // Store expression
	private Iterator<Character> stringIter; 
	private int[][] nextState; 
	private int numOfStates; 

	public FiniteMachine() {
		this.alphabet = new ArrayList<>();
		this.finalStates = new ArrayList<>();
		this.nextState = new int[100][100]; // [state][symbol] = next state
		this.string = new LinkedList<>();
		this.numOfStates = 0; 
	}

	/**
	 * Add a symbol as part of the alphabet.
	 * @param char Symbol to be added.  
	 */
	public void addAlpha(char symbol) {
		// Add symbol to specified index
		this.alphabet.add(symbol); 
	}

	/**
	 * Return the size of the alphabet
	 */
	public int alphaSize() {
		return alphabet.size();
	}

	public char alphaSym(int index) {
		return alphabet.get(index);
	}

	public ArrayList<Character> getAlpha() {
		return this.alphabet;
	}

	/**
	 * Set size of total number of states.
	 */
	public void numOfStates(int size) {
		this.numOfStates = size; 

		for (int i = 0; i < this.numOfStates-1; i++) 
			finalStates.add(false);
	}

	public int getNumStates() {
		return this.numOfStates;
	}

	/**
	 * Mark a symbol as a final state.  
	 * @param int Symbol to be marked as a final state.
	 */
	public void addFinalState(int index) { 
		finalStates.add(index, true);
	}

	/**
	 * Return array list of final states
	 */
	public ArrayList<Boolean> getFinalStates() {
		return this.finalStates;
	}

	public int sizeFinal() {
		return this.finalStates.size();
	}

	public void printNextState() {
		for (int i = 0; i < this.numOfStates; i++) {
			for (int j = 0; j < this.alphabet.size(); j++) {
				System.out.print(nextState[i][j] + " ");
			}
			System.out.println();
		}
	}
	
	/**
	 * Add a character symbol to the string representation
	 * @param char a symbol to be added to make a string
	 */
	public void addStringSymb(char symbol) {
		string.add(symbol);
	}

	/**
	 * Add the next state to the corresponding given index and input
	 */
	public void transition(int state, char inputSymbol, int nextState) {
		int symbolIndex = alphabet.indexOf(inputSymbol);  
		this.nextState[state][symbolIndex] = nextState;
	}

	/**
	 * Take in a string of symbols to process.
	 * @param LinkedList<Character> A string to be processed by machine
	 */
	public void acceptString(LinkedList<Character> sequence) {
		this.string = sequence;
		this.stringIter = this.string.iterator();
	}

	/**
	 * Return index of corresponding symbol.
	 */
	private char getNextSymbol() {
		if (stringIter.hasNext()) {
			char symbol = stringIter.next();
			return symbol;
			// return this.alphabet.indexOf(symbol); 
		} return '\n';
	}

	public int getNextState(int state, int input) {
		return this.nextState[state][input];
	}

	/**
	 * Check to see if the corresponding index (state) is a final state
	 * @param int index used to find corresponding state status
	 */
	public boolean isFinal(int index) {
		if (finalStates.get(index).equals(true))
			return true;
		return false;
	}

	public boolean perform() {
		boolean accept = false;
		while (stringIter.hasNext()) {
			int state = 0; // initial state
			boolean exit = false;

			while(!exit) {
				char symbol = getNextSymbol();

				if (alphabet.contains(symbol)) {
					int symbolIndex = alphabet.indexOf(symbol);
					state = this.nextState[state][symbolIndex];

					if (finalStates.get(state).equals(true)) {
						exit = true;
						accept = false;
					}
				} else {
					exit = true;
					if (stringIter.hasNext())
						accept = false;
					else if (finalStates.get(state).equals(true))
						accept = true;
					else 
						accept = false;
				}
			}
		}
		return accept;
	}

}