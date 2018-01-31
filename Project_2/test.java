public class test {
	public static void main(String[] args) {
		// Test Dynamic FSA
		DynamicFSA fsa = new DynamicFSA();
		String word = "import?";
		String word2 = "akz09AZ_$?";
		String word3 = "absolute?";
		String word4 = "absolutely?";
		String word5 = "abs?";
		String word6 = "absolutely?";
		String word7 = "abs?";
		String word8 = "akz09AZ_$?";
		String word9 = "import?";
		String word10 = "important?";
		String word11 ="importanButNotLegal?";

		// fsa.getIdentifier(word);
		// fsa.transition();
		// if (fsa.isNew()) {
		// 	word += "?";
		// 	System.out.println(word);
		// }
		// fsa.getIdentifier(word3);
		// fsa.transition();
		// System.out.println(check(fsa, word3));

		// fsa.getIdentifier(word4);
		// fsa.transition();
		// System.out.println(check(fsa, word4));

		// fsa.getIdentifier(word5);
		// fsa.transition();
		// System.out.println(check(fsa, word5));

		// fsa.getIdentifier(word6);
		// fsa.transition();
		// System.out.println(check(fsa, word6));

		// fsa.getIdentifier(word7);
		// fsa.transition();
		// System.out.println(check(fsa, word7));

		fsa.reservedWord(word);
		fsa.transition();
		System.out.println(check(fsa, word));

		fsa.getIdentifier(word2);
		fsa.transition();
		System.out.println(check(fsa, word2));

		fsa.getIdentifier(word8);
		fsa.transition();
		System.out.println(check(fsa, word8));

		fsa.getIdentifier(word9);
		fsa.transition();
		System.out.println(check(fsa, word9));

		fsa.getIdentifier(word10);
		fsa.transition();
		System.out.println(check(fsa, word10));

		fsa.getIdentifier(word11);
		fsa.transition();
		System.out.println(check(fsa, word11));




		// fsa.getIdentifier(word6);
		// fsa.transition();


	}

	public static String check(DynamicFSA machine, String word) {
		if (machine.isNew()) {
			return word.replace('?', '?');
		} else if (machine.isReserved()) {
			return word.replace('?', '*');
		} else if (machine.isDuplicate()) {
			return word.replace('?', '@');
		}
		return word;
	}
}