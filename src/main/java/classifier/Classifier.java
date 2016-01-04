package main.java.classifier;
/**
 * 
 * @author Christiaan en Dylan
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Classifier {
	/**
	 * A map that where the key represents a category and the value an array of all the files associated with the category.
	 */
	private Map<String, File[]> categorizedFolder;
	/**
	 * A map where the key represent a category and the value the words in the category with their corresponding quantity.
	 */
	private Map<String, Map<String, Integer>> categorizedWordCount;

	public Classifier() {
		categorizedFolder = new HashMap<String, File[]>();
		categorizedWordCount = new HashMap<String, Map<String, Integer>>();
	}
	
	public void run() throws FileNotFoundException {
		while(askAddAnotherCat()) {
			String cat = askCategory();
			String folder = askFolderLocation();
			storeCatFiles(cat, folder);
		}
        countWord();
        TrainerMultinomial trainer = new TrainerMultinomial(categorizedWordCount, categorizedFolder);
        while(askApplyNow()) {
        	/*!!!!!!!~~~~ aanpassen voor complete folders!!!!!!~~~~
        	List<String> list = removeDoubles(tokenizer(read(askFileLocation())));
            ApplyMultinomial apply = new ApplyMultinomial(trainer.getVocabulary(), trainer.getPriorCMap(), trainer.getProbMap(), list);
            System.out.println(list.toString());
        	!!!!!!!~~~~ aanpassen voor complete folders!!!!!!~~~~*/
        }
	}
	
	/**
	 * Get the current catagorizedFolder
	 * @return the current catagorizedFolder
	 */
	public Map<String, File[]> getCategorizedFolder() {
		return categorizedFolder;
	}
	
	/**
	 * Get the current categorizedWordCount
	 * @return the current categorizedWordCount
	 */
	public Map<String, Map<String, Integer>> getCategorizedWordCount() {
		return categorizedWordCount;
	}
	
	/**
	 * Asks the user for the name of the category
	 * @return the string that represent the category
	 */
	public String askCategory() {
		String answer = sendQuestion("What is the Category?");
		return answer;
	}
	
	/**
	 * Asks the user for the location of the folder
	 * @return the string that represents the location of the folder
	 */
	public String askFolderLocation() {
		String answer = sendQuestion("Were are your files located?");
		return answer;
	}
	
	public File askFileLocation() {
		String fileloc = sendQuestion("Please, enter the location of the file.");
		File singleFile = new File(fileloc);
		return singleFile;
	}
	
	public boolean askAddAnotherCat() {
		String answer = "";
		boolean proceed = false;
		do {
			answer = sendQuestion("Do you want to add another Category?(Yes/No)");
			} while(!answer.equals("Yes") && !answer.equals("No"));
		if(answer.equals("Yes")) {
			proceed = true;
		}
		return proceed;
	}
	
	public boolean askApplyNow() {
		String answer = "";
		boolean proceed = false;
		do {
			answer = sendQuestion("Do you want to Apply?(Yes/No)");
		} while(!answer.equals("Yes") && !answer.equals("No"));
		if(answer.equals("Yes")) {
			proceed = true;
		}
		return proceed;
	}
	
	public void storeCatFiles(String Cat, String loc) {
		File location = new File(loc);
		categorizedFolder.put(Cat, location.listFiles());
	}
	
	public void countWord() throws FileNotFoundException {
		Set<String> keys = categorizedFolder.keySet();
		for(String key : keys) {
			File[] filesFromMap = categorizedFolder.get(key);
			Map<String, Integer> result = new HashMap<String, Integer>();
			for (int i = 0; i < filesFromMap.length; i++) {
				Map<String, Integer> wordCount = new HashMap<String, Integer>();
				String text = read(filesFromMap[i]);
				String[] tokenizedText = tokenizer(text);
				wordCount = removeAndCountDoubles(tokenizedText);
				wordCount.forEach((k, v) -> result.merge(k, v, (v1, v2) -> v1 + v2));
			}
			categorizedWordCount.put(key, result);				
		}
	}
	
	public String[] tokenizer(String text) {
		String[] tokenizedText = text.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");
		for(int i = 0; i < tokenizedText.length; i++) {
		}
		return tokenizedText;
	}

	public List<String> concatenateAllText(String cat) throws FileNotFoundException {
		List<String> result = new ArrayList<String>();
		File[] filesFromMap = categorizedFolder.get(cat);
		for (int i = 0; i < filesFromMap.length; i++) {
			List<String> temp = new ArrayList<String>();
			String text = read(filesFromMap[i]);
			String[] tokenizedText = tokenizer(text);
			for (int j = 0; j < tokenizedText.length; j++) {
				temp.add(tokenizedText[j]);
			}
			result.addAll(temp);
		}
		return result;
	}
	
	public List<String> removeDoubles(String[] tokenizedText) {
		String currentWord;
		List<String> result = new ArrayList<String>();
		for(int i = 0; i < tokenizedText.length; i++) {
			if(!result.contains(tokenizedText[i])) {
			result.add(tokenizedText[i]);
			}
		}
		return result;
	}

	public Map<String, Integer> removeAndCountDoubles(String[] tokenizedText) {
		String currentWord;
		Map<String, Integer> wordCount = new HashMap<String, Integer>();
		for(int i = 0; i < tokenizedText.length; i++) {
			if(wordCount.containsKey(tokenizedText[i]) != true) {
			currentWord = tokenizedText[i];
			int counter = 0;
			for(int j = 0; j < tokenizedText.length; j++) {
				if (currentWord.equals(tokenizedText[j])) {
					counter++;
				}
            }
			wordCount.put(currentWord, counter);
			}
		}
		return wordCount;
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
	
	public static void main(String[]args) throws FileNotFoundException, InterruptedException {
		Classifier classifier = new Classifier();
		classifier.run();
	}
}
