import java.util.*;
import java.io.*;
public class Books {
	private static ArrayList<Books> books;
	private static int numOfBooks;
	private String title; 
	private int id; 

	public Books() {
		this.books = new ArrayList<>(); 
		this.title = "no title";
		this.id = 3412;
	}

	public void readInfo(String data) {
		try {
		File file = new File(data);
		Scanner readFile = new Scanner(file);


		int index = 0; 
			Books book; 
			String border = "====";
			while (readFile.hasNext()) {
				String check = nextLine(); 
				
				if (check.equals(border)) {
					book = new Books();
					book.title = readFile.nextLine(); 
					book.id = readFile.nextInt();
					books.add(index++, book); 
					numOfBooks++; 
				}
				
			}
			readFile.close(); 
		} catch (IOException e) {
			System.out.println("error");
		} 
	}

	public void show() {
		for (int i = 0; i < books.size(); i++)
			System.out.println(books.get(i).title);
	}
}