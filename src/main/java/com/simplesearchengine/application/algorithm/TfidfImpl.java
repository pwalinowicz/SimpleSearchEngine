package com.simplesearchengine.application.algorithm;

import com.simplesearchengine.application.base.Document;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Class implementing the tf-idf algorithm
 */
@Component
public class TfidfImpl implements WeightAlgorithm {

    //===== Methods =====
    /**
     * Calculates the value of tf of the searched word in relation to a given document.
     * @param searchedWord - String which is searched in the documents
     * @param document - given document
     * @return value of calculated tf
     */
    private double tfWordInSingleDocument(String searchedWord, Document document){
        double tf = 0;
        List<String> listOfWords = document.getListOfWords();

        for(String word : listOfWords){
            if(searchedWord.equalsIgnoreCase(word)){
                tf++;
            }
        }
        return tf / listOfWords.size();
    }

    /**
     * Calculates to value of idf of the searched word in the relation to all documents.
     * @param searchedWord - String which is searched in the documents
     * @param allDocuments - all documents in the base.
     * @return
     */
    private double idfWordInAllDocuments(String searchedWord, List<Document> allDocuments) {
        double count = 0;

        for (Document document : allDocuments) {
            for (String word : document.getListOfWords()) {
                if (searchedWord.equalsIgnoreCase(word)) {
                    count++;
                    break;
                }
            }
        }
        return Math.log10(allDocuments.size()/count);
    }

    /**
     * Calculates the value of tf-idf.
     * @param searchedWord - String which is searched in the documents
     * @param document - given document
     * @param allDocuments - all documents in the base
     * @return the value of tf-idf.
     */
    private double tfidfWordInDocument(String searchedWord, Document document, List<Document> allDocuments){
        return tfWordInSingleDocument(searchedWord, document) * idfWordInAllDocuments(searchedWord, allDocuments);
    }

    /**
     * Calculates the value of weight according to the implementation of the tf-idf algorithm.
     * @param searchedWord - String which is searched in the documents
     * @param document - given document
     * @param allDocuments - all documents in the base
     * @return value of the weight
     */
    @Override
    public Double calculateWeight(String searchedWord, Document document, List<Document> allDocuments) {
        if(searchedWord != null && document != null && allDocuments != null){
            return tfidfWordInDocument(searchedWord, document, allDocuments);
        } else {
            return null;
        }
    }
}

