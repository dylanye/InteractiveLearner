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
    private Map<String, File[]> categorizedFolder;
    private Map<String, Map<String, Integer>> categorizedWordCount;
    private List<String> vocabulary = new ArrayList<String>();
    private double priorC = 0.0 ;
    private Map<String, Map<String, Double>> probMap = new HashMap<String, Map<String, Double>>();


    public TrainerMultinomial(Map<String, Map<String, Integer>> categorizedWordCount, Map<String, File[]> categorizedFolder){
        this.categorizedWordCount = categorizedWordCount;
        this.categorizedFolder = categorizedFolder;
        try {
            this.run();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void extractVocabulary(){
        for (Map<String, Integer> m : categorizedWordCount.values()){
            for (String s : m.keySet()){
                vocabulary.add(s);
            }
        }
    }

    public int countDocuments(){
        int count = 0;
        for (String s : categorizedFolder.keySet()) {
            for (int i = 0; i < categorizedFolder.get(s).length; i++){
                count++;
            }
        }
        return count;
    }

    public int countDocuments(String s){
        int count = 0;
        for (int i = 0; i < categorizedFolder.get(s).length; i++){
            count++;
        }
        return count;
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

    public String[] tokenizer(String text) {
        String[] tokenizedText = text.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");
        for(int i = 0; i < tokenizedText.length; i++) {
        }
        return tokenizedText;
    }

    public List<String> getVocabulary(){
        return vocabulary;
    }

    public void run() throws FileNotFoundException{
        extractVocabulary();
        int countDoc = countDocuments();
        for (String s : categorizedWordCount.keySet()){
            int countDocPerCat = countDocuments(s);
            priorC = (double)countDocPerCat/(double)countDoc;
            List<String> concatText = concatenateAllText(s);
            Map<String, Double> tempProbMap = new HashMap<String, Double>();
            for (int i = 0; i < vocabulary.size() - 1; i++){
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
}
