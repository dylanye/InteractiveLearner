package main.java.classifier;

import java.util.Scanner;

public class Classifier {
	private Reader read;
	public Classifier() {
		read = new Reader(askFileLocation());
	}
	
	public String askFileLocation() {
		String fileloc = "";
		System.out.println("What is the location of the file?");
		do {
			Scanner scanner = new Scanner(System.in);
			fileloc = scanner.nextLine();
		} while (fileloc == "");
		return fileloc;
	}
	
	public static void main(String[] args) {
		Classifier classifier = new Classifier();
	}
}
