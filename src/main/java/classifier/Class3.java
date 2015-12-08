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
	
	public void run() throws FileNotFoundException {
		while(askAddAnotherCat()) {
		String cat = askCategory();
		String folder = askFolderLocation();
		storeCatFiles(cat, folder);
		}
		printMap();
		countWord();
	}
	
	public String askCategory() {
		String answer = sendQuestion("What is the Categorie?");
		return answer;
	}
	
	public String askFolderLocation() {
		String answer = sendQuestion("Were are your files located?");
		return answer;
	}
	
	public boolean askAddAnotherCat() {
		String answer = "";
		boolean proceed = false;
		do {
			answer = sendQuestion("Do you want to add another Category?(Yes/No)");
			System.out.println(!answer.equals("Yes") + "|" + !answer.equals("No"));
			} while(!answer.equals("Yes") && !answer.equals("No"));
		if(answer.equals("Yes")) {
			proceed = true;
		}
		return proceed;
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
	
	public void countWord() throws FileNotFoundException {
		Set<String> keys = categorizedFolder.keySet();
		for(String key : keys) {
			File[] files = categorizedFolder.get(key);
			for (int i = 0; i < files.length; i++) {
				read(files[i]);
			}
		}
	}
	
	public String read(File file) throws FileNotFoundException {
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
		System.out.println(fullInput);
		return fullInput;
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
	
	public static void main(String[]args) throws FileNotFoundException {
		Class3 classifier = new Class3();
		classifier.run();
	}
}
