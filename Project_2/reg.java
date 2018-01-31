import java.util.regex.*;
public class reg {
	public static void main(String[] args) {
		String comment ="/** This is a comment";
		String comment2 = "* This is part of a comment";
		String comment3 = "// Comment again";
		String word = "This should not be seen";

		String[] strings = {comment, comment2, comment3, word};

		for (int i = 0; i < strings.length; i++) {
			String line = strings[i];

			if (line.startsWith("/") || line.startsWith("*")) {
				System.out.println("COMMENT: "+line);
			}
		}

	}
}