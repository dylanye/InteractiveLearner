package main.java.classifier;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Class3 {
	private File[] files;
	private Map<String, File[]> categorizedFolder;
	private Map<String, Map<String, Integer>> wordCount;
	
	public Class3() {
		categorizedFolder = new HashMap<String, File[]>();
		wordCount = new HashMap<String, Map<String, Integer>>();
	}
	
	public void run() {
		String cat = askCategory();
		String folder = askFolderLocation();
		storeCatFiles(cat, folder);
		printMap();
	}
	
	public String askCategory() {
		String answer = sendQuestion("What is the Categorie?");
		return answer;
	}
	
	public String askFolderLocation() {
		String answer = sendQuestion("Were are your files located?");
		return answer;
	}
	
	public void storeCatFiles(String Cat, String loc) {
		File location = new File(loc);
		files = location.listFiles();
		categorizedFolder.put(Cat, files);
	}
	
	public void printMap() {
		Set<String> keys = categorizedFolder.keySet();
		for(String key : keys) {
			File[] files = categorizedFolder.get(key);
			for (int i = 0; i < files.length; i++) {
				System.out.println(key + files[i].toString());
			}
		}
	}	
/*	
	public void countWord() {
		Set<String> keys = categorizedFolder.keySet();
		for(String key : keys) {
			File[] files = categorizedFolder.get(key);
			for (int i = 0; i < files.length; i++) {
				read(files[i]);
			}
		}
	}
*/	
	public void read(File file) throws FileNotFoundException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String fullInput = "";
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
	
	public String sendQuestion(String message) {
		System.out.println(message);
		String answer = "";
		do {
			Scanner scanner = new Scanner(System.in);
			answer = scanner.nextLine();
		} while (answer == "");
		return answer;
	}
	
	public static void main(String[]args) {
		Class3 classifier = new Class3();
		classifier.run();
	}
}
