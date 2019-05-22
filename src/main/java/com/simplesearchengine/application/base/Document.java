package com.simplesearchengine.application.base;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Document{

    //===== Fields =====
    private String documentName;
    private int id;
    private String text;
    private double statisticWeight;

    //===== Constructor =====
    public Document(String documentName, String text) {
        this.text = text;
        this.documentName = documentName;
    }

    //===== Methods =====
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public double getStatisticWeight() {
        return statisticWeight;
    }

    public void setStatisticWeight(double statisticWeight) {
        this.statisticWeight = statisticWeight;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getDocumentName() {
        return documentName;
    }

    /**
     * Method processing the text to a list of words.
     * @return List of Strings representing list of words from a document.
     */
    public List<String> getListOfWords() {

        //exclude non alpha-numerical characters in the words
        return Arrays.asList(this.getText().split("\\s"))
                .stream()
                .map(s -> s = s.replaceAll("[^a-zA-Z0-9_-]",""))
                .collect(Collectors.toList());
    }
}


