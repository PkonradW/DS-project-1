import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
public class Main {

	public static void main(String[] args) throws IOException {
		
		File stateFile = new File(getInput());
		BufferedReader fileReader = new BufferedReader(new FileReader(stateFile));
		State[] stateList = new State[50];
		
		String firstLine[] =fileReader.readLine().split(",");
		
		for(int i = 0; i < 50; i++) {	// load array of States with entities constructed from CSV data
			String[] elementArray = fileReader.readLine().split(",");
			stateList[i] = new State(elementArray);
		}
		for (int i = 0; i < 50; i++) {
			System.out.println("state: " + i);
			State.statePrinter(stateList[i]);
		}
	}
	public static String getInput() {
		Scanner inputScanner = new Scanner(System.in);
		System.out.print("File name: ");
		String userInput = inputScanner.nextLine();
		inputScanner.close();
		return userInput;
	}

}
