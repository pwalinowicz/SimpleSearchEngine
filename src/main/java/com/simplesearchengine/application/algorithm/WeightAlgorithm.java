package com.simplesearchengine.application.algorithm;

import com.simplesearchengine.application.base.Document;
import java.util.List;

public interface WeightAlgorithm {
    double calculateWeight(String searchedWord, Document document, List<Document> allDocuments);
}
