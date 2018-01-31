import java.util.regex.*;
import java.util.Scanner;
import java.io.*;

/**
*	CS 311
*	project2.java
*	Purpose: Read in file of reserved words, and regular source code file, 
*		extract identifiers only and insert them to a transition table.
*
*	Program compiled and run via command line: 
*					'javac project2.java' then 'java project2'
*
*	@author Luis Cortes
*	02/21/17
*/

public class project2 {        /*regular expresseion to find java identifiers*/
	public static final String reg = "(\\s*([A-Za-z_$])\\w*|\"[^\"]*\"\\s*)";
	private static DynamicFSA transition; // Hold the transitions
	private static boolean wordIsReserved = false;

	public static void  main(String[] args) {
		String file1 = "Proj2_Input1.txt"; // Reserved words
		String file2 = "Proj2_Input2.txt"; // Source code
		
		transition = new DynamicFSA(); 
		read(file1, true); 
		System.out.println("****************************************");
		read(file2, false);
		System.out.println("****************************************");

		System.out.println();
		System.out.println(transition.toString());

	}

	/**
	*	Uses a regular expression to find identifiers. 
	*	It further rejects a string enclosed in " " or ' '
	*/
	public static String regexChecker(String theRegex, String check) {
		Pattern checkRegex = Pattern.compile(theRegex); // regular expr pattern
		Matcher regexMatcher = checkRegex.matcher(check); 

		StringBuilder line = new StringBuilder();
		String identifier; 

		while(regexMatcher.find()) { // 
			if (regexMatcher.group().length() != 0) { 
				// System.out.print(regexMatcher.group().trim());
				identifier = regexMatcher.group().trim();
				identifier += "?";

				if (identifier.charAt(0)=='"'||identifier.charAt(0)=='\'') {
					// System.out.println("\t****nope****");	
				} else { // A valid identifier
					// System.out.println("\tSAVE");
					line.append(identifier);
					line.append(" ");
					// Add identifier to database of words
				}
			}
		}
		return line.toString();
	}

	/**
	*	Take in a reserved word to process in transition list.
	*	Return the identifier with correct ending character
	*/
	public static String readInReserved(String identifier) {
		transition.reservedWord(identifier);
		transition.transition();
		return(check(identifier));
	}

	/**
	*	Take in identifier (not a reserved word) to process in transition 
	*	list. Return the identifier with the correct ending character
	*/
	public static String readInWord(String identifier) {
		transition.getIdentifier(identifier);
		transition.transition();
		return(check(identifier));
	}

	/**
	*	Takes in a string and determines what ending character to append to it,
	*	depending on the status of it when processed by the transition table
	*	Return with appendened character 
	*/
	public static String check(String word) {
		if (transition.isNew()) {
			// System.out.println("NEW");
			return word.replace('?', '?');
		} else if (transition.isReserved()) {
			// System.out.println("RESERVED WORD");
			return word.replace('?', '*');
		} else if (transition.isDuplicate()) {
			// System.out.println("DUPLICATE");
			return word.replace('?', '@');
		}
		return word;
	}

	/**
	*	Read in data from a file. Take in flag to determine if reading reserved
	*	words. Skip over comments and potential empty lines.
	*/
	public static void read(String fileName, boolean flag) {
		int lineCount =0; 
		if (flag) wordIsReserved = true; // Process reserved words if true
		try {
			File file = new File(fileName);
			Scanner readFile = new Scanner(file);

			String line;
			String out; 
			String tokens[];
			while(readFile.hasNextLine()) { // Read while line available
				line = readFile.nextLine().trim();
				
				// skip comments
				if (line.startsWith("/") || line.startsWith("*")) continue;
				out = regexChecker(reg,line);
				tokens = out.split(" "); // Break line to get individual word

				//**** SKIP EMPTY LINES ****//
				if (out.length() <= 0) {
					System.out.println();
					continue;
				}

				// Process one word at a time 
				for (String token : tokens) {
					// System.out.println(token);
					if (wordIsReserved) {
						System.out.print(readInReserved(token.trim()) + " ");
					} else {
						System.out.print(readInWord(token.trim())+ " ");
					}
				}
				System.out.println();


				lineCount++;
			}
			wordIsReserved = false;
			readFile.close();
		} catch (IOException e) {
			// Do nothing
		}
	}


}