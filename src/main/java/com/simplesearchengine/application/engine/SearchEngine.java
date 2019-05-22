package com.simplesearchengine.application.engine;

import com.simplesearchengine.application.base.Document;

import java.io.IOException;
import java.util.List;

public interface SearchEngine {
    List<Document> getDocumentListWithGivenIndex(String index);
    void printDocumentListWithGivenIndex(String searched);
    boolean populateBaseOfIndexesFromResourceFolder(String folderPath) throws IOException;
}
