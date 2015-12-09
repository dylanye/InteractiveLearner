package main.java.classifier;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dylan on 6-12-15.
 */
public class ApplyMultinomial {
    private List<String> vocabulary = new ArrayList<String>();
    private Map<String, Double> priorCMap = new HashMap<String, Double>();
    private Map<String, Map<String, Double>> probMap = new HashMap<String, Map<String, Double>>();
    private List<String> document;


    public ApplyMultinomial(List<String> vocabulary, Map<String, Double> priorCMap, Map<String, Map<String, Double>> probMap, List<String> document){
        this.vocabulary = vocabulary;
        this.priorCMap = priorCMap;
        this.probMap = probMap;
        this.document = document;
        this.run();
    }

    /**
     * Checks if the words in the document are present in the vocabulary. The provided document contains no duplicates.
     * @return a list of words both present in the document AND the vocabulary.
     */
    public List<String> extractTokensFromDoc(){
        List<String> result = new ArrayList<String>();
        int index = 0;
        for (int i = 0; i < document.size() - 1; i++ ){
            if (vocabulary.contains(document.get(i))){
                result.add(index, document.get(i));
                index++;
            }
        }
        return result;
    }

    /**
     * Gives the highest probability over all the categories.
     * @param map A map with all the categories and its probability
     * @return The category with highest probability. If probability is equal of two categories, first one will be returned.
     */
    public String argMax(Map<String, Double> map){
        String maxCategory =  null;
        Double maxProbability = 0.0;
        System.out.println("scoreMap result: "+ map.toString());
        for (String s : map.keySet()){
            if (maxProbability < map.get(s)){
                maxProbability = map.get(s);
                maxCategory = s;
            }
        }
        return "Highest probability for category: "+ maxCategory + " with " + maxProbability.toString();
    }

    public void run(){
        Map<String, Double> scoreMap = new HashMap<String, Double>();
        for (String s : probMap.keySet()){
            double score = 1.0;
            List<String> tokens = extractTokensFromDoc();
            for (int i = 0; i < tokens.size() - 1; i++){
                score = score * probMap.get(s).get(tokens.get(i));
            }
            score = score * priorCMap.get(s);
            scoreMap.put(s, score);
            System.out.println(score);
        }
        System.out.println(argMax(scoreMap));
    }
}
