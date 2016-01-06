package main.java.classifier;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dylan on 6-12-15.
 */
public class TrainerMultinomial {

    /**
     * categorizedFolder Contains a map of the category as the key and an array of Files as value.
     * categorizedWordCount A map with the class as key and a map<string, integer> as value. The map contains the word and the count of the word in the category.
     * priorCMap Is a map with the category as key and its according priorC as value.
     * probMap Is a map with all the probabilities of the words per category as the key. So every key (category) will contain values of maps<word, probability>
     * vocabulary All the words, including doubles.
     */
    private Map<String, File[]> categorizedFolder;
    private Map<String, Map<String, Integer>> categorizedWordCount;
    private List<String> vocabulary = new ArrayList<String>();
    private Map<String, Map<String, Double>> probMap = new HashMap<String, Map<String, Double>>();
    private Map<String, Double> priorCMap = new HashMap<String, Double>();

    /**
     * @param categorizedWordCount A map with the class as key and a map<string, integer> as value. The map contains the word and the count of the word in the category.
     * @param categorizedFolder Contains a map of the category as the key and an array of Files as value.
     */
    public TrainerMultinomial(Map<String, Map<String, Integer>> categorizedWordCount, Map<String, File[]> categorizedFolder){
        this.categorizedWordCount = categorizedWordCount;
        this.categorizedFolder = categorizedFolder;
        try {
            this.run();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Extract all the words, including doubles.
     */
    public void extractVocabulary(){
        for (Map<String, Integer> m : categorizedWordCount.values()){
            for (String s : m.keySet()){
                vocabulary.add(s);
            }
        }
    }

    /**
     * Counts all the documents.
     * @return Integer of all the documents.
     */
    public int countDocuments(){
        int count = 0;
        for (String s : categorizedFolder.keySet()) {
            for (int i = 0; i < categorizedFolder.get(s).length; i++){
                count++;
            }
        }
        return count;
    }

    /**
     * Counts all the documents of the category.
     * @param s The category
     * @return Integer of all the documents of the category
     */
    public int countDocuments(String s){
        int count = 0;
        for (int i = 0; i < categorizedFolder.get(s).length; i++){
            count++;
        }
        return count;
    }

    /**
     *
     * @param cat the category
     * @return return a list of strings of all the documents of the category.
     * @throws FileNotFoundException
     */
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

    /**
     *
     * @param file A file. This can be a blog text or something else.
     * @return Returns the whole text of the file as a string.
     * @throws FileNotFoundException
     */
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

    /**
     * Tokenizes the provided text.
     * @param text The text of a document.
     * @return An array of tokenized strings.
     */
    public String[] tokenizer(String text) {
        String[] tokenizedText = text.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");
        return tokenizedText;
    }

    public List<String> getVocabulary(){
        return vocabulary;
    }

    public Map<String, Map<String, Double>> getProbMap(){
        return probMap;
    }

    public Map<String, Double> getPriorCMap(){
        return priorCMap;
    }

    /**
     * This method will create a baseline for the interactive learner. It will create a probMap with all the words and its probability, per category
     * @throws FileNotFoundException
     */
    public void run() throws FileNotFoundException{
        extractVocabulary();
        int countDoc = countDocuments();
        //Calculate per category
        for (String s : categorizedWordCount.keySet()){
            int countDocPerCat = countDocuments(s);
            //priorC is the probability of the total amount of documents of class C divided by total documents
            double priorC = (double)countDocPerCat/(double)countDoc;
            //priorCMap is a map with the category as key and its according priorC as value.
            priorCMap.put(s, priorC);
            //Calculate the probability of the word and add them to the probMap.
            List<String> concatText = concatenateAllText(s);
            //tempProbMap contains a word and the probability
            Map<String, Double> tempProbMap = new HashMap<String, Double>();
            for (int i = 0; i < vocabulary.size(); i++){
                String vocabWord = vocabulary.get(i);
                double probability = 0;
                if (categorizedWordCount.get(s).get(vocabWord) == null) {
                    probability = ((1.0) / ((double) concatText.size() + 1.0));
                }
                else {
                    probability = ((double) categorizedWordCount.get(s).get(vocabWord) + 1.0) / ((double) concatText.size() + 1.0);
                }
                Map<String, Double> temp = new HashMap<String, Double>();
                temp.put(vocabWord, probability);
                temp.forEach((k, v) -> tempProbMap.merge(k, v, (v1, v2) -> v1 + v2));
            }
            probMap.put(s, tempProbMap);
        }
    }

    public void update(Map<String, Map<String, Integer>> updateWordCountMap, Map<String, File[]> updateFileMap){
        //First update the categorizedWordCount.
        //Go over the category of the to be added map. This should only be 1 category.
        for (String cat : updateWordCountMap.keySet()){
            //Go over every word of the map.
            for(String word : updateWordCountMap.get(cat).keySet()) {
                //If the existing categorizedWordCount map contains the "words" of the to be added map, then increase the count of the "word"
                if (categorizedWordCount.get(cat).containsKey(word)) {
                    int oldCount = categorizedWordCount.get(cat).get(word);
                    int addCount = updateWordCountMap.get(cat).get(word);
                    int newCount = oldCount + addCount;
                    categorizedWordCount.get(cat).put(word, newCount);
                }
                //If the "word" does not exist in the categorizedWordCount map, then add the "word" to the map with its count.
                else {
                    categorizedWordCount.get(cat).put(word, updateWordCountMap.get(cat).get(word));
                }
            }
        }
        //Second update the categorizedFiles.
        //Go over the category of the to be added map. This should only be 1 category.
        for (String cat : updateFileMap.keySet()){
            //Create a temp file array of
            File[] tempFileArray = new File[categorizedFolder.get(cat).length];
            File[] oldFileArray = categorizedFolder.get(cat);
            File[] newFile = updateFileMap.get(cat);
            for (int index = 0; index < tempFileArray.length; index++) {
                System.out.println("index = "+index);
                if (index < tempFileArray.length) {
                    tempFileArray[index] = oldFileArray[index];
                }
                else {
                    //newFile should only have 1 file in the array, therefore index 0 can be used.
                    tempFileArray[index] = newFile[0];
                }
            }
            categorizedFolder.put(cat, tempFileArray);
        }
        try {
            this.run();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Update function for the trainer failed");
        }
    }
}
