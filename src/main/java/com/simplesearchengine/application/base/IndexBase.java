package com.simplesearchengine.application.base;

import java.util.List;

public interface IndexBase {
    boolean addAllIndexesFromDocument(Document document);
    List<Document> getListOfDocumentsForSearchedIndex(String index);
}
