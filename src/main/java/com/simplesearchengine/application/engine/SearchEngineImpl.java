package com.simplesearchengine.application.engine;

import com.simplesearchengine.application.algorithm.WeightAlgorithm;
import com.simplesearchengine.application.base.Document;
import com.simplesearchengine.application.base.IndexBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Class implementing the search engine's functionality
 */
@Service
public class SearchEngineImpl implements SearchEngine {

    //===== Fields =====
    private final IndexBase indexBase;
    private WeightAlgorithm algorithmImpl;
    private List<Document> allDocuments;
    private Comparator<Document> comparator;

    //===== Constructor =====
    @Autowired
    public SearchEngineImpl(IndexBase indexBase, WeightAlgorithm algorithmImpl) {
        this.indexBase = indexBase;
        this.algorithmImpl = algorithmImpl;
        this.allDocuments = new ArrayList<>();

        //comparator sorting the documents descending based on the weight
        this.comparator = Comparator.comparingDouble(Document::getStatisticWeight).reversed();
    }

    //===== Methods =====
    /**
     * Setter enabling usage of different comparator than the default one.
     * @param comparator
     */
    public void setComparator(Comparator<Document> comparator) {
        this.comparator = comparator;
    }

    /**
     * Setter enabling usage of different algorithm for calculation of weight than the default tf-idf.
     * @param algorithmImpl
     */
    public void setAlgorithmImpl(WeightAlgorithm algorithmImpl) {
        this.algorithmImpl = algorithmImpl;
    }

    /**
     * Adds a given document to a list of all documents and calls method which fills the base of indexes.
     * @param document
     * @return true if adding operation is successful, false if not.
     */
    private boolean addAllIndexesFromSingleDocument(Document document){
        if(!allDocuments.contains(document)) {
            allDocuments.add(document);
            return indexBase.addAllIndexesFromDocument(document);
        } else {
            return false;
        }
    }

    /**
     * Takes a list of documents and calls addAllIndexesFromSingleDocument method for each and every document.
     * @param documentList
     * @return true if operation is successful or false if it's not
     */
    private boolean addAllIndexesFromListOfDocuments(List<Document> documentList){
        if(documentList != null){
            for (Document doc : documentList){
                addAllIndexesFromSingleDocument(doc);
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Method searches for a given String in the base and returns list of Documents in which the searched String occurs.
     * List is sorted basing on the value of the weight for each document in the list.
     * @param index
     * @return List of Documents in which the searched String occurs
     */
    @Override
    public List<Document> getDocumentListWithGivenIndex(String index){
        //get the document list for the searched String
        List<Document> documentList = indexBase.getListOfDocumentsForSearchedIndex(index);

        //if the index and list is valid then set weight for each document in the list and sort the list

        if (documentList != null && index != null){
            for (Document document : documentList){
                document.setStatisticWeight(algorithmImpl.calculateWeight(index, document, allDocuments));
                //setting the weight in accordance to the implemented algorithm
            }
            return documentList.stream()
                    .sorted(comparator)
                    .collect(Collectors.toList());
        } else {
            return null;
        }
    }

    /**
     * Method prints to the console the names of files (documents) which the searched word is present in.
     * @param searched
     */
    @Override
    public void printDocumentListWithGivenIndex(String searched){
        List<Document> result = getDocumentListWithGivenIndex(searched);

        //if the result list is valid then print the names of the files to the console
        if (result != null) {
            System.out.println("The word: " + searched + " can be found in the following documents:");
            result.stream().forEach(document -> System.out.println(document.getDocumentName()));
        } else {
            System.out.println("No such word is present in the Document Base");
        }
    }

    /**
     * Method uses the text files in the folder located under the given path to fill the base of indexes by calling the
     * addAllIndexesFromListOfDocuments method.
     * Catches exception in case of problems with reading of the files
     * @param folderPath - path to a folder with text files
     * @return true if the operation is successful or false if it's not
     * @throws IOException in case of the incorrect file path
     */
    @Override
    public boolean populateBaseOfIndexesFromResourceFolder(String folderPath) throws IOException {
        List<Document> documentList = new ArrayList<>();
        Stream<Path> filePaths = Files.walk(Paths.get(folderPath));
        // getting stream of paths of every file in the folder

        filePaths.forEach(filePath -> {
            //reads only files which are valid
            if (Files.isRegularFile(filePath) && filePath.getFileName().toString().endsWith("txt")) {
                try {
                    //if file is correct then read all the content, convert it to a Document object and add it to a list of documents to be added later
                    byte[] allBytes = Files.readAllBytes(filePath);
                    String fileContent = new String(allBytes, StandardCharsets.UTF_8).trim();

                    documentList.add(
                            new Document(
                                    filePath.getFileName().toString().substring(0, filePath.getFileName().toString().indexOf('.')).trim(),
                                    fileContent
                            )
                    );
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        //if the document list is valid then add indexes form all the documents in the list
        if (!documentList.isEmpty()) {
            if(addAllIndexesFromListOfDocuments(documentList)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

}

