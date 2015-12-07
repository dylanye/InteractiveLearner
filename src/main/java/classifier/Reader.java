package main.java.classifier;
/**
 * @author C. Visscher
 */
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Reader {
	private String fullInput;
	private String[] tokenizedInput;
	public Reader() {
	} 
	
	public String getFullInput() {
		return fullInput;
	}
	
	public String[] getTokenizedInput() {
		return tokenizedInput;
	}
	
	public void read(String file) throws FileNotFoundException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String input;
		fullInput = "";
		try {
			while ((input = reader.readLine()) != null) {
				fullInput = fullInput + input;		
				}
		} catch(IOException e) {
			System.out.println(e.getMessage() + "Error reading file");
		}
//		System.out.println(fullInput);
	}
	
	public void tokenizer() {
		tokenizedInput = fullInput.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");
		for(int i = 0; i < tokenizedInput.length; i++) {
			System.out.println(tokenizedInput[i]);
		}
	}
	
	public static void main(String[] args) throws IOException {
		Reader read = new Reader();
		read.read("C:/MOD6AI/files/blogs/M/M-test3.txt");
		read.tokenizer();
	}
}