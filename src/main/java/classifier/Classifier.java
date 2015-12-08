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
	
	public String askTrainingOrApply() {
		String answer = sendQuestion("Do you want to train or apply the learner?(Enter: 'Train' or 'Apply'") ;
		if(!answer.equals("Train") || !answer.equals("Apply")) {
			System.out.println("Invalid input please try again.");
			askTrainingOrApply();
		}
		return answer;
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
