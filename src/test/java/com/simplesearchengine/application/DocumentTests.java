package com.simplesearchengine.application;

import com.simplesearchengine.application.base.Document;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;

public class DocumentTests {

	private String text;
	private Document document;

	@Before
	public void setup(){
		text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris non.";
		document = new Document("Test Document", text);
	}

	@Test
	public void shouldReturnListOf10Strings() {
		List<String> listOfWords = new ArrayList<>();

		Assert.assertEquals(document.getListOfWords().getClass(), listOfWords.getClass());
		Assert.assertEquals(document.getListOfWords().size(),10);
	}

	@Test
	public void shouldExcludeNonAlphanumericalChars(){
		List<String> listOfWords = document.getListOfWords();

		listOfWords.stream()
				.forEach(word -> Assert.assertTrue(!word.contains(".") &&
						!word.contains(",") && !word.contains(" ")));
	}
}


