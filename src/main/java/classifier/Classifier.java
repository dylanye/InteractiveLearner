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
import java.util.*;

public class Classifier {
    /**
     * A map that where the key represents a category and the value an array of all the files associated with the category.
     */
    private Map<String, File[]> categorizedFolder;
    /**
     * A map where the key represent a category and the value the words in the category with their corresponding quantity.
     */
    private Map<String, Map<String, Integer>> categorizedWordCount;
    /**
     * A map where the key is the file and the value a list of all the words in the file.
     */
    private Map<File, String[]> fileWords;
    /**
     * List of all the categories
     */
    private List<String> categoryList;

    private String[] featureSelection;

    public Classifier() throws FileNotFoundException {
        categorizedFolder = new HashMap<String, File[]>();
        categorizedWordCount = new HashMap<String, Map<String, Integer>>();
        categoryList = new ArrayList<String>();
        File commonWordsFile = new File("./src/data/FeatureSelection.txt");
        try(BufferedReader br = new BufferedReader(new FileReader(commonWordsFile))) {
            List<String> temp = new ArrayList<String>();
            int index = 0;
            for(String line; (line = br.readLine()) != null; ) {
                temp.add(index, line);
                index++;
            }
            featureSelection = new String[temp.size()];
            featureSelection = temp.toArray(featureSelection);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() throws FileNotFoundException {
        while(askAddAnotherCat()) {
            String cat = askCategory();
            String folder = askFolderLocation();
            categoryList.add(cat);
            storeCatFiles(cat, folder);
        }
        countWord();
        featureSelectionTrainer();
        TrainerMultinomial trainer = new TrainerMultinomial(categorizedWordCount, categorizedFolder);
        do {
        	String applyOrACC = askApplyACC().toLowerCase();
            fileWords = new HashMap<File, String[]>();
            if(applyOrACC.toLowerCase().equals("apply")) {
            	File fileLoc = askFileLocation();
                String[] list = tokenizer(read(fileLoc));
                System.out.println(fileLoc.getPath());
                ApplyMultinomial apply = new ApplyMultinomial(trainer.getVocabulary(), trainer.getPriorCMap(), trainer.getProbMap(), featureSelectionApply(list));
                String correctCat = askCorrectCat(apply.getBestCategory());
                categorizedFolder = new HashMap<String, File[]>();
                categorizedWordCount = new HashMap<String, Map<String, Integer>>();
                File[] fileLocA = new File[1];
                fileLocA[0] = fileLoc;
                categorizedFolder.put(correctCat, fileLocA);
                countWord();
                featureSelectionTrainer();
                trainer.update(categorizedWordCount, categorizedFolder);
            }
            if(applyOrACC.toLowerCase().equals("acc")) {
                String folderACC = askFolderLocation();
                File loc = new File(folderACC);
                File[] fileArray = loc.listFiles();
                for(int i = 0; i < fileArray.length; i++) {
                    String[] wordArray= tokenizer(read(fileArray[i]));
                    fileWords.put(fileArray[i], wordArray);
                }

                //Create resultmap
                //Resultmap has the category and the number of files that the apply method has decided that it was the best category.
                Map<String, Integer> resultMap = new HashMap<String, Integer>();
                for (String category : categoryList) {
                    resultMap.put(category, 0);
                }

                //Apply all the files
                for (File file : fileWords.keySet()) {
                    System.out.println(file.getPath());
                    ApplyMultinomial apply = new ApplyMultinomial(trainer.getVocabulary(), trainer.getPriorCMap(), trainer.getProbMap(), featureSelectionApply(fileWords.get(file)));
                    //Fill resultmap with scores
                    for (String category : resultMap.keySet()) {
                        if (category.equals(apply.getBestCategory())) {
                            int temp = resultMap.get(category);
                            temp++;
                            resultMap.put(category, temp);
                        }
                    }
                }

                //Result of accuracy
                for (String category : resultMap.keySet()){
                    double categoryAccuracy;
                    int totalFiles = fileArray.length;
                    categoryAccuracy = (double) resultMap.get(category) / (double) totalFiles;
                    System.out.println("Accuracy for category: " + category + " is: " + categoryAccuracy);
                }
                System.out.println(resultMap.toString());

            }
        }while (askContinue());
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
    
    public List<String> getCategoryList() {
    	return categoryList;
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
    	} while(!answer.toLowerCase().equals("yes") && !answer.toLowerCase().equals("no"));
        if(answer.toLowerCase().equals("yes")) {
            proceed = true;
        }
        return proceed;
    }
    
    public boolean askContinue() {
    	String answer = "";
    	boolean proceed = false;
    	do{ 
    		answer = sendQuestion("Do you want to apply again?(Yes/No)");
    	} while(!answer.toLowerCase().equals("yes") && !answer.toLowerCase().equals("no"));
        if(answer.toLowerCase().equals("yes")) {
            proceed = true;
        }
    	return proceed;
    }
    
    public String askApplyACC() {
        String answer = "";
        do {
            answer = sendQuestion("Do you want to apply one file or determine the accurancy of a folder?(type: Apply/Acc)");
        } while(!answer.toLowerCase().equals("apply") && !answer.toLowerCase().equals("acc"));
        return answer;
    }
    
    public String askCorrectCat(String predictedCat) {
    	String answer = "";
    	String answerCorrectCat = "";
    	boolean proceed = false;
    	do {
    		answer = sendQuestion("Is the predicted class correct?(Yes/No)");
    	} while(!answer.toLowerCase().equals("yes") && !answer.toLowerCase().equals("no"));
    	if(answer.toLowerCase().equals("no")) {
    		do {
    			answerCorrectCat = sendQuestion("What is the correct category? (case sensitive)" + "\n" + "Choose from: " + categoryList.toString());
    			for (String category : categoryList) {
    				if(answerCorrectCat.equals(category)) {
    					proceed = true;
    				}
    			}
    		} while(!proceed);
    	} else {
    		answerCorrectCat = predictedCat;
    	}
    	return answerCorrectCat;
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
            if(!wordCount.containsKey(tokenizedText[i])) {
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

    public void featureSelectionTrainer() throws FileNotFoundException {

        Map<String, Map<String, Integer>> result = new HashMap<String, Map<String, Integer>>();
        for(String category : categorizedWordCount.keySet()) {
            Map<String, Integer> wordCountMap = new HashMap<String, Integer>();
            for (String word : categorizedWordCount.get(category).keySet()) {
                if (!Arrays.asList(featureSelection).contains(word)){
                    wordCountMap.put(word, categorizedWordCount.get(category).get(word));
                }
            }
            result.put(category, wordCountMap);
        }
        categorizedWordCount = result;
    }

    public List<String> featureSelectionApply(String[] words) throws FileNotFoundException {
        List<String> result = new ArrayList<String>();
        int index = 0;

        for (int i = 0; i < words.length; i++){
            if (!Arrays.asList(featureSelection).contains(words[i])) {
                result.add(index, words[i]);
                index++;
            }
        }
        return result;
    }

    public static void main(String[]args) throws FileNotFoundException, InterruptedException {
        Classifier classifier = new Classifier();
        classifier.run();
    }
}
