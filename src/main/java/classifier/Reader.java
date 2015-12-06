package main.java.classifier;
/**
 * @author C. Visscher
 */
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileReader;
import java.io.IOException;

public class Reader {
	
	public Reader() {
	} 
	
	public static void read(BufferedReader reader) throws EOFException {
		String input;
		String fullInput = "";
		try {
			while ((input = reader.readLine()) != null) {
				fullInput = fullInput + input;		
				}
		} catch(IOException e) {
			System.out.println(e.getMessage() + "Error reading file");
		}
		System.out.println(fullInput);
		throw new EOFException();

	}
	
	public void splitter() {
	}
	
	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader("c:/Users/Christiaan/Desktop/project/index.txt"));
		Reader.read(reader);
		
	}
}