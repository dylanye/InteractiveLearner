package main.java.classifier;

import java.util.Scanner;

public class Classifier {
	private Reader read;
	public Classifier() {
		read = new Reader(askFileLocation());
	}
	
	public String askFileLocation() {
		String fileloc = sendQuestion("Please, enter the location of the file.");
		return fileloc;
	}
	
	public String sendQuestion(String message) {
		System.out.println(message);
		String answer = "";
		do {
			Scanner scanner = new Scanner(System.in);
			answer = scanner.nextLine();
		} while (answer == "");
		return answer;
	}
	
	public static void main(String[] args) {
		Classifier classifier = new Classifier();
	}
}
