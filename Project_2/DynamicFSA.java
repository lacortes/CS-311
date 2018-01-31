/**
*	CS 311
*	DynamicFSA.java
*	Purpose: Through the use of three arrays, maintain a list of words, that 
*			 can be transitioned to by the relation of the arrays. It processes
*			 one identfier at a time.
*
*	Once an identifer is processed, it is categorized either as a new identifer,
*	reserved identifer, or a repeated identifer.
*
*	@author Luis Cortes
*	02/21/17		 
*/

public class DynamicFSA {
	private char[] symbolArray;
	private int[]  nextArray;
	private final int INITIAL_SIZE = 1000000;
	private int nextAvailSymbol; // Index to next available index in symbol array
	private char[] tokens; // Array holding identifier being processed
	private int tokenIndex = 0; 


	private int[] switchArray; 
	private final byte SWTICH_SIZE = 64;

	///**********************************///
	// indexes of where values are seperated in switch array
	private final byte DIGIT_INDEX = 26; 
	private final byte UPPER_INDEX = 36;
	private final byte UNDER_INDEX = 62;
	private final byte DOLLAR_INDEX = 63;
	///**********************************///


	///************************************///
	// Values from ascii table
	private final byte DIG_START = 48;
	private final byte UPPERCASE_START = 65;
	private final byte LOWERCASE_START = 97;
	private final byte UNDERSCORE = 95;
	private final byte DOLLARSIGN = 36;
	///************************************///

	///***************************///
	// Every processed identifier is categorized into either a:
	private boolean newIdentifier;
	private boolean reserved;
	private boolean repeat;
	///***************************///
	private boolean processReserved;
	
	/**
	*	Constructor initializes arrays
	*/
	public DynamicFSA() {
		this.switchArray = new int[SWTICH_SIZE];
		this.symbolArray = new char[INITIAL_SIZE];
		this.nextArray   = new int[INITIAL_SIZE];
		this.initArrays();

		this.nextAvailSymbol = 0;
		this.newIdentifier = false;
		this.reserved = false;
		this.repeat = false; 
		this.processReserved = false;
	}

	/**
	*	Process reservied identifiers
	*/
	public void reservedWord(String identifier) {
		this.processReserved = true;
		this.getIdentifier(identifier);
	}

	/**
	*	Return true if the processed idetifier is new
	*/
	public boolean isNew() {
		return this.newIdentifier;
	}

	/**
	*	Return true if the processed identifier is reserved
	*/
	public boolean isReserved() {
		return this.reserved;
	}

	/**
	*	Return true if the processed identifer is a dupliacate
	*/
	public boolean isDuplicate() {
		return this.repeat;
	}

	/**
	*	Turn string identifier to char arry and store. 
	*/
	public void getIdentifier(String identifier) {
		this.tokenIndex = 0; // Restart tokens index
		this.newIdentifier = false;
		this.reserved = false;
		this.repeat = false;
		this.tokens = identifier.toCharArray();
	}

	/**
	*	Transition algorithm
	*/
	public void transition() {
		char symbol = this.getNextSymbol();
		// System.out.println(symbol);

		int indexOfSymbol = this.findIndex(symbol); // Get index for switch array
		int ptr = this.switchArray[indexOfSymbol]; // Get value of switchArray

		if (ptr == -1) { // Value at switchArray[i] has no pointer
			this.switchArray[indexOfSymbol] = nextAvailSymbol;
			this.create(); // Make a new identifer			
		} else {
			symbol = this.getNextSymbol();
			boolean exit = false;

			while (!exit) {
				// System.out.println("\tchecking: "+symbol);
				// System.out.println(this.symbolArray[ptr]+" == "+symbol);
				if (this.symbolArray[ptr] == symbol || !tokenAvailabe()) {

					// System.out.println("EQUAL");
					// Symbol not end marker
					if (symbol != '?') { // tokens available
						// System.out.println("\tNOT ?");
						symbol = this.getNextSymbol();
						ptr += 1;
					} else { // No more tokens
						// System.out.println("\tELSE");
						char end = this.symbolArray[ptr];
						if (end == '?') {
							// found duplicate
							// System.out.println("\t   DUPLICATE");
							this.repeat = true;
						} else if (end == '*') {
							// found reserve
							// System.out.println("\t   RESERVED");
							this.reserved = true;
						}
						exit = true;
					} // end else
					
				} else if (this.nextArray[ptr] != -1){ // nextArray[ptr] is defined
					// System.out.println("DEFINED");
					ptr = this.nextArray[ptr]; 
				} else { // not defined
					// System.out.println("UNDEFINED");
					this.nextArray[ptr] = this.nextAvailSymbol; // METHOD TO CHECK
					this.symbolArray[nextAvailSymbol++] = symbol;
					// System.out.println("SymbolArray-> index: "+
					// 					  (nextAvailSymbol-1)+" = "+symbol);
					this.create();
					exit = true;				
				}
			} // end while
		}// end else
	}// end transtitio()

	/**
	*	Place symbols in appropriate place, until identifier is complete
	*	Set newIdentifier to true, for being a new identifier, 
	*	or reserved to true
	*/
	private void create() {
		while (tokenAvailabe()) {
			char sym = this.getNextSymbol();
			// System.out.println("symbol: "+sym);
			if (sym != '?') { // ignore '?'
				this.symbolArray[nextAvailSymbol] = sym; // put symbol in symbolArray
				// System.out.println("SymbolArray-> index: "+nextAvailSymbol+" = "+sym);
				this.nextAvailSymbol++; // CREATE METHOD DO UPDATE ARRAYS IF NO SPACE
			}			
		}

		// Add the new identifier (?) after word
		if (processReserved) {
			this.symbolArray[nextAvailSymbol] = '*';
			// System.out.println("ADDING RESERVED");
			this.reserved = true;
			this.processReserved = false;
		} else {
			this.symbolArray[nextAvailSymbol] = '?';
			this.newIdentifier = true;	
		}
		// System.out.println("SymbolArray-> index: "+nextAvailSymbol
		// 					+" = "+symbolArray[nextAvailSymbol]);
		nextAvailSymbol++;
	}

	/**
	*	Return a char symbol from the tokens array (identifer).
	*	Increase counter by one;
	*/
	private char getNextSymbol() {
		return this.tokens[tokenIndex++];
	}	

	/**
	*	Check if symbol table array has avaliable space, if not then double 
	*	array for it and the 'next' array.
	*	Return the next available index 
	*/
	private int nextSymbolIndex() {
		// Do later
		return 0;
	}

	/**
	*	Check whether tokens array still has symbols remaining
	*/
	private boolean tokenAvailabe() {
		if (this.tokenIndex > this.tokens.length-1) { // out of bounds
			return false;
		}
		return true;
	}

	/**
	*	Determines the switch index for the corresponding symbol
	*/
	private int findIndex(char sym) {
		int index = -1; 
		int value = sym;

		// Determine in what group char falls under
		switch(determineType(value)) {
			case 'd': // digit
				index = (value - DIG_START) + DIGIT_INDEX;
				break;
			case 'u': // uppercase
				index = (value - UPPERCASE_START) + UPPER_INDEX;
				break;
			case 'l': // lowercase
				index = value - LOWERCASE_START;
				break;
			case '_': // underscore
				index = UNDER_INDEX;
				break;
			case '$': // dollarsign
				index = DOLLAR_INDEX; 
				break;
		}
		// System.out.println("switch array: "+index);
		return index;
	}

	/**
	*	Determine the type of character that is being processed.
	*	Return its type in char: d (digit), u (uppercase), l (lowercase)
	*	_ (underscore), or $ (dollar)
	*/
	private char determineType(int charValue) {

		/*	Digit ascii value range: 48 - 57
		*	Uppercase ascii value range: 65 - 90
		*	Lowercase ascii value range: 97 - 122 
		*	'_' ascii value: 95
		*	'$' ascii value: 36
		*/

		if (charValue >= 48 && charValue <= 57) {
			// Char is a digit
			// System.out.print(" DIGIT ");
			return 'd'; // digit
		} else if (charValue >= 65 && charValue <= 90) { 	
			// Char is an uppercase letter
			// System.out.print(" UPPER ");
			return 'u'; // upper case
		} else if (charValue >= 97 && charValue <= 122) {
			// System.out.print(" LOWER ");
			return 'l'; // lower case
		} else if (charValue == UNDERSCORE) {
			// System.out.print(" UNDERSCORE ");
			return '_'; // underscore
		} else {
			// System.out.print(" DOLLAR ");
			return '$'; // dollar sign
		}
	}

	/**
	*	Initialize all values in switch array with -1;
	*/ 
	private void initArrays() {
		for (int i = 0; i < SWTICH_SIZE; i++) {
			this.switchArray[i] = -1;
		}

		for (int i = 0; i < INITIAL_SIZE; i++) {
			this.symbolArray[i] = ' ';
			this.nextArray[i] = -1;
		}
	}

	/**
	*	Determines the valeu in ascii for the corresponding symbol
	*/
	private char findChar(int index) {
		char value;

		if (index >= 0 && index <= 25) { // lower case
			value =  (char)(index + 65);
		} else if (index >= 26 && index <= 35 ) { // number
			value = (char)(index + 22);
		} else if (index >= 36 && index <= 61) { // upper
			value = (char)(index + 61);
		} else if (index == 63) { // underscore
			value = '_';
		} else {
			value = '$';
		} return value;
	}

	private String printSymbol(int start, int end) {
		char sym;
		String str = ""; 
		for (int i = start; i < end; i++) {
			sym = findChar(i);
			str += String.format("%1$-4s", sym);
		}
		return str;
	}

	private String printValue(int start, int end) {
		String str = "";
		for (int i = start; i < end; i++) {
			if (i%19==0) {
				str += "\n\nswitch: ";
			}
			str += String.format("%1$-5d", switchArray[i]);
		}
		return str;
	}

	/**
	*	String representation of transition list arrays.
	*/
	public String toString() {
		String pretty ="";		
		String str= "";
		char sym;

		pretty += "        ";
		// for (int i = 0; i < 19; i++) {
		// 	sym = findChar(i);
		// 	pretty += String.format("%1$-4s",sym);
		// }
		// pretty += printSymbol(0,19);

		for (int i=0; i<this.switchArray.length; i++) {
			sym = findChar(i);
			if (i % 19 == 0) { 
				pretty += "\n\nswitch: ";
			}
			pretty += String.format("%1$-5d\b",switchArray[i]);
		}
		// pretty += printValue(0, 19);

		// for (int i = 0; i< this.nextAvailSymbol; i++) {
		// 	if (i %19 == 0) pretty += "\n\nsymbol: ";
		// 	pretty += String.format("%1$-5d", symbolArray[i]);
		// }


		return pretty;
	}
}