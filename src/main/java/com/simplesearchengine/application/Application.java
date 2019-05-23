package com.simplesearchengine.application;

import com.simplesearchengine.application.base.Document;
import com.simplesearchengine.application.engine.SearchEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;


/**
 * Class handling the console behaviour.
 */
@SpringBootApplication
public class Application implements CommandLineRunner {

	//===== Fields =====
	private final SearchEngine searchEngine;

	/**
	 * Fixed location of the folder with the text files.
	 */
	private static final String resourceFolder = "./filesForIndexing";

	//===== Constructor =====
	@Autowired
	public Application(SearchEngine searchEngine) {
		this.searchEngine = searchEngine;
	}

	//===== Methods =====
	/**
	 * Method responsible for populating the search engine's document and index base from the folder.
	 * Takes place before the onStart method is called.
	 */
	@PostConstruct
	public void populateBaseOfIndexesFromResourceFolder(){
		if(searchEngine != null){
			try{
				if(searchEngine.populateBaseOfIndexesFromResourceFolder(resourceFolder)) {
					System.out.println("Populating from folder - SUCCESSFUL");
				} else {
					System.out.println("Populating from folder - UNSUCCESSFUL");
				}
			} catch(IOException e){
				System.out.println("Couldn't populate documents from the folder. Incorrect path.");
			}
		}
	}

	/**
	 * Method which handles the console behaviour
	 */
	@Override
	public void run(String... args)	{
		boolean hasExited = false;

		while (hasExited == false) {
			printMenuHeadlines();
			Scanner sc = new Scanner(System.in);
			Scanner innerSc = new Scanner(System.in);

			try {
				int choice = sc.nextInt();
				switch (choice) {
					case 1:
						String searchDecision = "Y";

						System.out.println("SEARCHING");
						while (searchDecision.trim().equalsIgnoreCase("Y")) {
							System.out.println("Write the word to be searched: ");
							String searched = innerSc.nextLine();

							searchEngine.printDocumentListWithGivenIndex(searched.trim());
							System.out.println();
							System.out.println("Do you want to make another search? Y/N");
							searchDecision = innerSc.nextLine();
						}
						break;
					default:
						System.out.println("Exiting");
						hasExited = true;
						break;
				}
			} catch (InputMismatchException e) {
				System.out.println("Exiting");
				hasExited = true;
			}
		}
	}

	/**
	 * Method printing generic headlines of the menu
	 */
	private void printMenuHeadlines() {
		System.out.println("................................................");
		System.out.println("What would you like to do?");
		System.out.println("................................................");
		System.out.println("[1] - Search for a word in the document base");
		System.out.println("[other] - exit");
		System.out.println("................................................");
		System.out.println();
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
