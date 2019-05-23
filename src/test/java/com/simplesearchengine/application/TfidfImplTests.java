package com.simplesearchengine.application;

import com.simplesearchengine.application.algorithm.TfidfImpl;
import com.simplesearchengine.application.algorithm.WeightAlgorithm;
import com.simplesearchengine.application.base.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TfidfImplTests {

    private WeightAlgorithm tfidf = new TfidfImpl();
    private List<Document> allDocuments;

    @Before
    public void setup(){
        allDocuments = new ArrayList<>();
        allDocuments.add(new Document("Test Document1", "this is a a sample"));
        allDocuments.add(new Document("Test Document2", "this is another another example example example"));
    }

    @Test
    public void tfidfShouldBeEqualToZero(){
        Assert.assertEquals(tfidf.calculateWeight("this", allDocuments.get(0),allDocuments), 0, 0.001);
        Assert.assertEquals(tfidf.calculateWeight("this", allDocuments.get(1),allDocuments), 0, 0.001);
        Assert.assertEquals(tfidf.calculateWeight("example", allDocuments.get(0),allDocuments), 0, 0.001);
    }

    @Test
    public void tfidfShouldBeCalulatedCorrectly(){
        Assert.assertEquals(tfidf.calculateWeight("example", allDocuments.get(1),allDocuments), 0.129, 0.001);
    }

    @Test
    public void tfidfShouldReturnNullForInvalidInput(){
        Assert.assertEquals(tfidf.calculateWeight(null, allDocuments.get(1), allDocuments), null);
        Assert.assertEquals(tfidf.calculateWeight("example", null, allDocuments), null);
        Assert.assertEquals(tfidf.calculateWeight("example", allDocuments.get(1),null), null);
    }
}
