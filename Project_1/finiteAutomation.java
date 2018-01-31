/**
 * CS-311
 * FiniteMachine.java
 * Purpose: Implement a universal finite machine, that can process one string at a time
 * 
 * @author Luis Cortes
 * 01/19/17
 */

import java.util.*;
import java.io.*;
public class finiteAutomation {
	public static ArrayList<LinkedList<Character>> strings = new ArrayList<>(); 
	public static ArrayList<ArrayList<LinkedList<Character>>> allStrings = new ArrayList<>();
	public static ArrayList<FiniteMachine> machines = new ArrayList<>();
	public static LinkedList<Character> exp;

	public static void main(String[] args) {
		System.out.println("Driver program for state machine.");
		exp = new LinkedList<>();
		read("test.txt");


		
		for (int i = 0; i < machines.size(); i ++) { // Iterate through machines
			FiniteMachine machine = machines.get(i);

			System.out.println("Finite State Automata: "+(i+1));
			System.out.println("(1) Number of states: "+machine.getNumStates());
			ArrayList<Boolean> finalStates = machine.getFinalStates();

			System.out.print("(2) Final States: "); 
			for (int index = 0; index < finalStates.size(); index++) {
				if (finalStates.get(index).equals(true))
					System.out.print(index);
			}
			System.out.println();

			System.out.print("(3) Alaphabet: ");
			ArrayList<Character> letters = machine.getAlpha();
			for (int j = 0; j < letters.size(); j++) {
				System.out.print(letters.get(j) + " ");
			}
			System.out.println();

			System.out.println("(4) Transitions: ");
			for (int state = 0; state < machine.getNumStates(); state++) {
				for (int sym = 0; sym < machine.alphaSize(); sym++) {
					char input = machine.alphaSym(sym);
					int endState = machine.getNextState(state, sym);
					System.out.println("\t"+state+" "+input+" "+endState);
				}
			}

			System.out.println("(5) Strings: "); 
				ArrayList<LinkedList<Character>> coll = allStrings.get(i);
				for (int k = 0; k < coll.size(); k ++) {
					// System.out.println("\touter loop: "+k);
					LinkedList<Character> list = coll.get(k);
					machine.acceptString(list);

					System.out.print("\t");
					for (int a = 0; a < list.size(); a++) {
						// System.out.println("\t\tInner inner loop: "+a);
						System.out.print(list.get(a));

					}

					if (machine.perform()) 
						System.out.print("\tAccept");
					else
						System.out.print("\tReject");
					System.out.println();
				}

			System.out.println();
		}
		

	}

	public static void read(String data) {
		try {
			File file = new File(data);
			Scanner inputFile = new Scanner(file);

			boolean recording = false; // Keep track of every block of different machines
			String numStates, numFinalStates, alphabet, transition, string;
			String line;

			String check = inputFile.nextLine(); 
			boolean again = true;
			int count =0;
			while (again) {
				// System.out.println("Entering loop"+count);

				FiniteMachine machine = new FiniteMachine();
				
				if (check.equals("====")) {
					// System.out.println("checking: "+check);
					recording = true; // Start recording
					numStates = inputFile.nextLine();
					numFinalStates = inputFile.nextLine();
					alphabet = inputFile.nextLine();

					addNumStates(machine, Integer.parseInt(numStates));
					putFinalStates(machine, numFinalStates);
					putAlpha(machine, alphabet);

					// System.out.println("number of states: "+numStates);
					// System.out.println("number of final: "+numFinalStates);
					// System.out.println("Alpha: "+ alphabet);

					line = inputFile.nextLine();
					// System.out.println("Checking: "+line);

					// process transitions
					while ( line.substring(0,1).equals("(") && line.substring(4).equals(")") ) {
						// System.out.println(line.substring(1,4));
						transition = line.substring(1,4);
						putTransition(machine, transition);
						line = inputFile.nextLine();
					}

					// System.out.println("checking again: "+line);
					string = line; // Strings are followed after reading all transitions

					// Array to hold collection of linked list
					ArrayList<LinkedList<Character>> strings = new ArrayList<>(); 
					// process strings
					while (!line.equals("====")) {
						// System.out.println(line);
						string = line;
						// System.out.println("count : "+count);
						// System.out.println("entering: "+string);
						addString(strings, string);

						line = inputFile.nextLine();
					}

					if (!inputFile.hasNext()) {
						again = false;
					}


					machines.add(machine);
					allStrings.add(strings); // Add list of linked lists
					count ++;
				}
			}
			inputFile.close();

			

		} catch (IOException e) {

		}
	}

	// Set num of states to machine
	public static void addNumStates(FiniteMachine m, int size) {
		m.numOfStates(size);
	}

	// Add final states to machine
	public static void putFinalStates(FiniteMachine m, String states) {
		for (int index = 0; index < states.length(); index ++) {
			if (index % 2 == 0) {
				int sym = Integer.parseInt(states.substring(index, index +1));
				m.addFinalState(sym);
			}
		}
	} 

	// 
	public static void putAlpha(FiniteMachine m, String alphabet) {
		for (int index = 0; index < alphabet.length(); index++) {
			if (index % 2 == 0) {
				char sym = alphabet.charAt(index);
				m.addAlpha(sym);
			}
		}
	}

	public static void putTransition(FiniteMachine m, String transition) {
		int state = Integer.parseInt(transition.substring(0,1));
		int endState = Integer.parseInt(transition.substring(2));
		char sym = transition.charAt(1);

		m.transition(state, sym, endState);
	}

	public static void addString(ArrayList<LinkedList<Character>> list, String string) {
		
		LinkedList<Character> holdStr = new LinkedList<>();
		for (int index = 0; index < string.length(); index++) {
			// System.out.print("\tAdding:: "+string.charAt(index));
			holdStr.add(string.charAt(index));
		}
		// System.out.println();
		list.add(holdStr);

		// System.out.print("list: ");
		// for (int i = 0; i < holdStr.size(); i++) {
		// 	System.out.print(holdStr.get(i));
		// }
		// System.out.println();
	}


}