package com.simplesearchengine.application;

import com.simplesearchengine.application.algorithm.TfidfImpl;
import com.simplesearchengine.application.algorithm.WeightAlgorithm;
import com.simplesearchengine.application.base.Document;
import com.simplesearchengine.application.base.IndexBaseImpl;
import com.simplesearchengine.application.engine.SearchEngine;
import com.simplesearchengine.application.engine.SearchEngineImpl;
import org.junit.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class SearchEngineImplTests {

    private List<Document> allDocuments;
    private SearchEngine searchEngine;
    private WeightAlgorithm tdidf;
    private IndexBaseImpl indexBase;
    private String resourceFolderPath = "./filesForTesting";
    private static String exampleText1 = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris non. this.";
    private static String exampleText2 = "this is another example";
    private static File newFolder = new File("./filesForTesting");
    private static File emptyFilesFolder = new File("./emptyFilesTestingFolder");
    private static File emptyFolder = new File("./emptyTestingFolder");

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream out = System.out;

    @BeforeClass
    public static void createTestFiles(){
        try {
            newFolder.mkdir();
            List<String> linesDocument1 = Arrays.asList(exampleText1);
            Path file = Paths.get("./filesForTesting/TestDocument1.txt");
            Files.write(file, linesDocument1, StandardCharsets.UTF_8);

            List<String> linesDocument2 = Arrays.asList(exampleText2);
            file = Paths.get("./filesForTesting/TestDocument2.txt");
            Files.write(file, linesDocument2, StandardCharsets.UTF_8);

            emptyFilesFolder.mkdir();
            List<String> linesDocument3 = Arrays.asList("");
            file = Paths.get("./emptyFilesTestingFolder/TestDocument1.txt");
            Files.write(file, linesDocument3, StandardCharsets.UTF_8);

            List<String> linesDocument4 = Arrays.asList("");
            file = Paths.get("./emptyFilesTestingFolder/TestDocument2.txt");
            Files.write(file, linesDocument4, StandardCharsets.UTF_8);

            emptyFolder.mkdir();

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void eraseTestFiles(){
        try {
            Files.deleteIfExists(Paths.get("./filesForTesting/TestDocument1.txt"));
            Files.deleteIfExists(Paths.get("./filesForTesting/TestDocument2.txt"));
            Files.deleteIfExists(Paths.get("./emptyFilesTestingFolder/TestDocument1.txt"));
            Files.deleteIfExists(Paths.get("./emptyFilesTestingFolder/TestDocument2.txt"));
            if(newFolder.exists()) {
                newFolder.delete();
            }
            if(emptyFilesFolder.exists()) {
                emptyFilesFolder.delete();
            }
            if(emptyFolder.exists()) {
                emptyFolder.delete();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Before
    public void setup(){
        tdidf = new TfidfImpl();
        indexBase = new IndexBaseImpl();
        searchEngine = new SearchEngineImpl(indexBase, tdidf);
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void cleanup(){
        System.setOut(out);
    }

    @Test
    public void shouldThrowExceptionForWrongFolderPath(){
        resourceFolderPath = ".\\filesForIndexingWrongPath";
        try {
            Assert.assertFalse(searchEngine.populateBaseOfIndexesFromResourceFolder(resourceFolderPath));
            Assert.fail("Exception wasn't thrown");
        } catch (IOException e) {
            Assert.assertEquals(resourceFolderPath, e.getMessage());
        }
    }

    @Test
    public void shouldAddAllIndexesFromTheFolderAndReturnTrue(){
        resourceFolderPath = "./filesForTesting";
        try {
            Assert.assertTrue(searchEngine.populateBaseOfIndexesFromResourceFolder(resourceFolderPath));
        } catch (IOException e) {
            Assert.fail("Exception was thrown");
        }
    }

    @Test
    public void shouldNotAddWordsFromEmptyFiles(){
        try{
            searchEngine.populateBaseOfIndexesFromResourceFolder("./emptyFilesTestingFolder");
            Assert.assertEquals(indexBase.getIndexMap().size(),0);
        } catch (IOException e) {
            Assert.fail("Exception was thrown");
        }
    }

    @Test
    public void shouldNotAddWordsFromEmptyFolder(){
        try{
            searchEngine.populateBaseOfIndexesFromResourceFolder("./emptyTestingFolder");
            Assert.assertEquals(indexBase.getIndexMap().size(),0);
        } catch (IOException e) {
            Assert.fail("Exception was thrown");
        }
    }

    @Test
    public void shouldReturnListOfTwoDocumentsForStringPresentInTwoDocuments(){
        resourceFolderPath = "./filesForTesting";
        try {
            searchEngine.populateBaseOfIndexesFromResourceFolder(resourceFolderPath);
            Assert.assertEquals(searchEngine.getDocumentListWithGivenIndex("this").size(),2);
        } catch (IOException e) {
            Assert.fail("Exception was thrown");
        }
    }

    @Test
    public void shouldReturnListWithOneDocumentForStringPresentInOnlyOneOfTheDocuments(){
        resourceFolderPath = "./filesForTesting";
        try {
            searchEngine.populateBaseOfIndexesFromResourceFolder(resourceFolderPath);
            Assert.assertEquals(searchEngine.getDocumentListWithGivenIndex("Lorem").size(),1);
        } catch (IOException e) {
            Assert.fail("Exception was thrown");
        }
    }

    @Test
    public void shouldReturnNullWhenSearchedIndexIsNull(){
        resourceFolderPath = "./filesForTesting";
        try {
            searchEngine.populateBaseOfIndexesFromResourceFolder(resourceFolderPath);
            Assert.assertEquals(searchEngine.getDocumentListWithGivenIndex(null), null);
        } catch (IOException e) {
            Assert.fail("Exception was thrown");
        }
    }

    @Test
    public void shouldReturnNullWhenSearchedIndexIsEmptyString(){
        resourceFolderPath = "./filesForTesting";
        try {
            searchEngine.populateBaseOfIndexesFromResourceFolder(resourceFolderPath);
            Assert.assertEquals(searchEngine.getDocumentListWithGivenIndex(""), null);
        } catch (IOException e) {
            Assert.fail("Exception was thrown");
        }
    }

    @Ignore
    @Test
    public void shouldPrintTwoDocuments(){
        resourceFolderPath = "./filesForTesting";
        try {
            searchEngine.populateBaseOfIndexesFromResourceFolder(resourceFolderPath);
            searchEngine.printDocumentListWithGivenIndex("this");
            Assert.assertEquals("The word: this can be found in the following documents:\r\n" +
                    "TestDocument1\r\n" +
                    "TestDocument2\r\n",
                    outContent.toString());
            //watch out for the line separators!
        } catch (IOException e) {
            Assert.fail("Exception was thrown");
        }
    }

    @Test
    public void shouldMessageThatNoDocumentsWereFound(){
        resourceFolderPath = "./filesForTesting";
        try {
            searchEngine.populateBaseOfIndexesFromResourceFolder(resourceFolderPath);
            searchEngine.printDocumentListWithGivenIndex("Warszawa");
            Assert.assertEquals(    "No such word is present in the Document Base\r\n",
                    outContent.toString());
            //watch out for the line separators!
        } catch (IOException e) {
            Assert.fail("Exception was thrown");
        }
    }



}
