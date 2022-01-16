package com.simplesearchengine.application;

import com.simplesearchengine.application.algorithm.TfidfImpl;
import com.simplesearchengine.application.algorithm.WeightAlgorithm;
import com.simplesearchengine.application.base.IndexBaseImpl;
import com.simplesearchengine.application.engine.SearchEngine;
import com.simplesearchengine.application.engine.SearchEngineImpl;
import org.junit.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

public class ApplicationTests {

    private Application app;
    private SearchEngine searchEngine;
    private WeightAlgorithm tdidf;
    private IndexBaseImpl indexBase;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream out = System.out;

    @Before
    public void setup(){
        tdidf = new TfidfImpl();
        indexBase = new IndexBaseImpl();
        searchEngine = new SearchEngineImpl(indexBase, tdidf);
        app = new Application(searchEngine);
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void cleanup(){
        System.setOut(out);
    }

    @Ignore
    @Test
    public void successfulPopulationShouldReturnCorrectMessage() {
        app.populateBaseOfIndexesFromResourceFolder();
        Assert.assertEquals("Populating from folder - SUCCESSFUL\r\n", outContent.toString());
    }

    @Ignore
    @Test
    public void shouldDisplayMenuAndExitMessageForMenuOptionHigherThan1() {
        String input = "2";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        app.run();
        Assert.assertEquals("................................................\r\n" +
                        "What would you like to do?\r\n"+
                        "................................................\r\n"+
                        "[1] - Search for a word in the document base\r\n"+
                        "[other] - exit\r\n"+
                        "................................................\r\n"+
                        "\r\n"+
                        "Exiting\r\n"
                , outContent.toString());
    }

    @Ignore
    @Test
    public void shouldDisplayMenuAndExitMessageForMenuOptionNotInt() {
        String input = "a";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        app.run();

        Assert.assertEquals("................................................\r\n" +
                        "What would you like to do?\r\n"+
                        "................................................\r\n"+
                        "[1] - Search for a word in the document base\r\n"+
                        "[other] - exit\r\n"+
                        "................................................\r\n"+
                        "\r\n"+
                        "Exiting\r\n"
                , outContent.toString());
    }
}

