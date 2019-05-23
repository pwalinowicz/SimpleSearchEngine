package com.simplesearchengine.application;

import com.simplesearchengine.application.base.Document;
import com.simplesearchengine.application.base.IndexBaseImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.List;

public class IndexBaseImplTest {

    private String text;
    private Document document;
    private IndexBaseImpl indexBase;

    @Before
    public void setup(){
        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris non.";
        document = new Document("Test Document", text);
        indexBase = new IndexBaseImpl();
    }

    @Test
    public void baseShouldContain10EntriesAfterIndexing(){
        Document documentCopy = new Document("Document Copy", text);

        indexBase.addAllIndexesFromDocument(document);
        Assert.assertEquals(indexBase.getIndexMap().size(),10);

        indexBase.addAllIndexesFromDocument(documentCopy);
        //There should be still 10 entries as the second document was the exact copy
        Assert.assertEquals(indexBase.getIndexMap().size(),10);
    }

    @Test
    public void baseShouldContain10EntriesAfterIndexingACopy(){
        Document documentCopy = new Document("Document Copy", text);

        indexBase.addAllIndexesFromDocument(document);
        indexBase.addAllIndexesFromDocument(documentCopy);
        //There should be still 10 entries as the second document was the exact copy of the original
        Assert.assertEquals(indexBase.getIndexMap().size(),10);
    }




    @Test
    public void eachIndexShouldHaveTwoLinkedDocuments(){
        Document documentCopy = new Document("Document Copy", text);

        indexBase.addAllIndexesFromDocument(document);
        indexBase.addAllIndexesFromDocument(documentCopy);

        List<String> listOfWords = documentCopy.getListOfWords();

        //Each word should be linked to two documents "Test Document" and "Document Copy"
        listOfWords.stream()
                .forEach(word -> Assert.assertEquals(indexBase.getListOfDocumentsForSearchedIndex(word).size(), 2)
                );
    }

    @Test
    public void shouldReturnNullIfTheWordIsNotFound(){
        indexBase.addAllIndexesFromDocument(document);
        Assert.assertEquals(indexBase.getListOfDocumentsForSearchedIndex("Warszawa"),null);
    }

    @Test
    public void shouldReturnNullIfTheStringIsEmptyOrNull(){
        indexBase.addAllIndexesFromDocument(document);
        Assert.assertEquals(indexBase.getListOfDocumentsForSearchedIndex(""),null);
        Assert.assertEquals(indexBase.getListOfDocumentsForSearchedIndex(null),null);
    }

}
