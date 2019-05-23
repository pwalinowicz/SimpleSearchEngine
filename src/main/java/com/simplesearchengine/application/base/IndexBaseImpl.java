package com.simplesearchengine.application.base;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Class respresenting the base of inverted indexes kept in a HashMap.
 * HashMap keeps the relation between the words (indexes) and the documents in which the word occured.
 */
@Component
public class IndexBaseImpl implements IndexBase{

    //===== Fields =====
    private HashMap<String, ArrayList<Document>> indexMap = new HashMap<>();

//===== Methods =====
    /**
     * Method adds all words (indexes) from the given document into the map
     * @param document
     * @return true if operation is successful or false if it's not
     */
    public synchronized boolean addAllIndexesFromDocument(Document document){

        List<String> listOfWords = document.getListOfWords();

        if(listOfWords != null) {
            for (String word : listOfWords) {
                //check if word is valid
                //if a word is already in the map then add given document into a documents list under the key: word.
                //otherwise add new key and create new empty list of documents for the new key.
                if(word != null && !word.equals("")) {
                    if (indexMap.containsKey(word)) {
                        if (!indexMap.get(word).contains(document)) {
                            indexMap.get(word).add(document);
                        }
                    } else {
                        ArrayList<Document> newList = new ArrayList<>();
                        newList.add(document);
                        indexMap.put(word, newList);
                    }
                } else {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Searches for a word (index) in the map.
     * @param index
     * @return List of corresponding documents if the index was found or null if it was not found.
     */
    public synchronized List<Document> getListOfDocumentsForSearchedIndex(String index){

        if (indexMap.containsKey(index)) {
            return indexMap.get(index);
        } else {
            return null;
        }
    }

    /**
     * Getter used for testing
     * @return map
     */
    public HashMap<String, ArrayList<Document>> getIndexMap() {
        return indexMap;
    }
}
