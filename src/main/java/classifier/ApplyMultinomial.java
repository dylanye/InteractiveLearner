package main.java.classifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Christiaan Visscher en Dylan Ye
 */
public class ApplyMultinomial {

    private List<String> vocabulary = new ArrayList<String>();
    private Map<String, Double> priorCMap = new HashMap<String, Double>();
    private Map<String, Map<String, Double>> probMap = new HashMap<String, Map<String, Double>>();
    private List<String> docWords;
    protected String maxCategory;

    /**
     *
     * @param vocabulary All the words, including doubles.
     * @param priorCMap A map containing all the categories and its probability.
     * @param probMap A map containing all the words and its probability.
     * @param list A list array of all the words of the applying document. Contains doubles.
     */
    public ApplyMultinomial(List<String> vocabulary, Map<String, Double> priorCMap, Map<String, Map<String, Double>> probMap, List<String> list){
        this.vocabulary = vocabulary;
        this.priorCMap = priorCMap;
        this.probMap = probMap;
        this.docWords = list;
        this.run();
    }

    /**
     * Checks if the words in the list are present in the vocabulary.
     * @return a list of words both present in the applying document AND the vocabulary.
     */
    public List<String> extractTokensFromDoc(){
        List<String> result = new ArrayList<String>();
        int index = 0;
        for (int i = 0; i < docWords.size(); i++ ){
            if (vocabulary.contains(docWords.get(i))){
                result.add(index, docWords.get(i));
                index++;
            }
        }
        return result;
    }

    public String getBestCategory(){
        return maxCategory;
    }

    /**
     * Gives the highest probability over all the categories.
     * @param map A map with all the categories and its probability
     * @return The category with highest probability. If probability is equal of two categories, first one will be returned.
     */
    public String argMax(Map<String, Double> map){
        maxCategory =  null;
        double maxProbability = 0.0;
        for (String s : map.keySet()){
            if(maxProbability == 0.0){
                maxProbability = map.get(s);
                maxCategory = s;
            }
            if (maxProbability < map.get(s)){
                maxProbability = map.get(s);
                maxCategory = s;
            }
        }
        return "Highest probability for category: "+ maxCategory;
    }

    /**
     * Removes words that do not occur very often in the document. If the occurrence is less than 0,5% of the text, the word will be removed.
     */
    public void removeRareWords(){
        String currentWord;
        //If the word has a occurrence of less than "percentage" then disregard the word in the calculation of the probability.
        double percentage = 0.005;
        Map<String, Integer> rareWordCountMap = new HashMap<String, Integer>();
        for(int i = 0; i < docWords.size(); i++) {
            if(!rareWordCountMap.containsKey(docWords.get(i))) {
                currentWord = docWords.get(i);
                int counter = 0;
                for(int j = 0; j < docWords.size(); j++) {
                    if (currentWord.equals(docWords.get(j))) {
                        counter++;
                    }
                }
                if ( (double)counter/(double)docWords.size() > percentage ) {
                    rareWordCountMap.put(currentWord, counter);
                }
            }
        }
        docWords = new ArrayList<String>(rareWordCountMap.keySet());
    }

    /**
     * This method will check for the best fitting category of the provided file.
     * If the word occurs in the vocabulary it will add its probability to the score. Reoccurring words will be added multiple times depending on the occurrence with the same probability.
     */
    public void run(){
        Map<String, Double> scoreMap = new HashMap<String, Double>();
        removeRareWords();
        for (String s : probMap.keySet()){
            double score = 0.0;
            List<String> tokens = extractTokensFromDoc();
            for (int i = 0; i < tokens.size(); i++){
                score += Math.log10(probMap.get(s).get(tokens.get(i)))/Math.log10(2);
            }
            score = score + Math.log10(priorCMap.get(s))/Math.log10(2);
            scoreMap.put(s, score);
        }
        System.out.println(argMax(scoreMap));
    }
}
