package main.java.classifier;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by dylan on 6-12-15.
 */
public class TrainerMultinomial {
    private Map<String, File[]> categorizedFolder;
    private Map<String, Map<String, Integer>> categorizedWordCount;
    private List<String> vocabulary = new ArrayList<String>();

    public TrainerMultinomial(Map<String, Map<String, Integer>> wordcount, Map<String, File[]> categorizedFolder){
        this.categorizedWordCount = wordcount;
        this.categorizedFolder = categorizedFolder;
    }

    public void extractVocabulary(){
        for (Map<String, Integer> m : categorizedWordCount.values()){
            for (String s : m.keySet()){
                System.out.println("Print string s " + s);
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

    public List<String> concatenateAllText(String s) throws FileNotFoundException {
        List<String> result = new ArrayList<String>();
        for (int i = 0; i < categorizedFolder.get(s).length; i++){
        File[] files = categorizedFolder.get(s);
            for (int j = 0; j < files.length; j++){
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(files[j])));
                while (br.){

                }
            }

        }
        return result;
    }

    public List<String> getVocabulary(){
        return vocabulary;
    }

    public void run(){
        int countDoc = countDocuments();
        for (String s : categorizedWordCount.keySet()){
            int countDocPerCat = countDocuments(s);
            int priorC = countDocPerCat/countDoc;

        }

    }
    //todo V extract all words of one doc

    //todo N count number of docs

    //todo Nc count docs in a class

}
