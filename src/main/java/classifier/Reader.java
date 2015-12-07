package main.java.classifier;
/**
 * This Class reads a file and tokenizes it.
 * @author C. Visscher
 */
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class Reader{
	private String fullInput;
	private String[] tokenizedInput;
	private HashMap<String, Integer> wordCount;
	public Reader() {
		fullInput = "";
	} 
	
	/**
	 * 
	 * @return a string of the compleet text that was read from a file
	 */
	public String getFullInput() {
		return fullInput;
	}
	
	/**
	 * 
	 * @return a tokenized text
	 */
	public String[] getTokenizedInput() {
		return tokenizedInput;
	}
	
	/**
	 * Reads the text from a file 
	 */
	public void read(String file) throws FileNotFoundException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String input;
		try {
			while ((input = reader.readLine()) != null) {
				fullInput = fullInput + input;		
				}
		} catch(IOException e) {
			System.out.println(e.getMessage() + "Error reading file");
		}
//		System.out.println(fullInput);
	}
	
	/**
	 * Tokenizes a text
	 */
	public void tokenizer() {
		tokenizedInput = fullInput.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");
		for(int i = 0; i < tokenizedInput.length; i++) {
			System.out.println(tokenizedInput[i]);
		}
	}
	
	public void removeAndCountDoubbles () {
	}
	
	public static void main(String[] args) throws IOException {
		Reader read = new Reader();
		read.read("C:/MOD6AI/files/blogs/M/M-test3.txt");
		read.tokenizer();
	}
}