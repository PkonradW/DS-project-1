import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;
public class Project1 {

	public static void main(String[] args) throws IOException {

		File stateFile;
		int choice = 0;
		boolean isAlphabetical = false;
		stateFile = new File("States1.csv"); // hardcoded for now, don't leave me like this
		Scanner scan = new Scanner(System.in);
		BufferedReader fileReader = new BufferedReader(new FileReader(stateFile));
		State[] stateList = new State[50]; // assumes that there are a maximum of 50 records in file
		String firstLine[] =fileReader.readLine().split(",");
		
		for(int i = 0; i < 50; i++) {	// load array of States with entities constructed from CSV data
			String[] elementArray = fileReader.readLine().split(",");
			stateList[i] = new State(elementArray);
		}
		do {
			choice = printMenu();
			if (choice == 1) stateReport(stateList); // prints out a state report (all states)
			else if (choice == 2) {		// sorts alphabetically
				bubbleSort(stateList);
				isAlphabetical = true;
			}
			else if (choice == 3) {		// sorts by fatality rate
				selectionSort(stateList);
				isAlphabetical = false;
			}
			else if (choice == 4) {		// sorts by MHI
				insertionSort(stateList);
				isAlphabetical = false;
			}
			else if (choice == 5) {		// find state for given name
				System.out.print("enter state name: ");
				if (isAlphabetical) {
					binarySearch(scan.nextLine(), stateList);
				}
				else {
					selectionSearch(scan.nextLine(), stateList);
				}
			}
			else if (choice == 6) {
				System.out.print(" cant do that yet chief");
			}

		} while (choice != 7);

	}

	/**
	 * Prompts user for the name of the CSV file to use
	 * @return userInput: string that user specified with keyboard input
	 */
	public static String getInput() {
		Scanner inputScanner = new Scanner(System.in);
		System.out.print("File name: ");
		String userInput = inputScanner.nextLine();
		inputScanner.close();
		return userInput;
	}
	public static int printMenu() {
		Scanner menuScanner = new Scanner(System.in);
		int choice;
		do {
			System.out.println();
			System.out.println("1. Print a states report");
			System.out.println("2. Sort alphabetically by name");
			System.out.println("3. Sort by case fatality rate (CFR)");
			System.out.println("4. Sort by median household income (MHI)");
			System.out.println("5. Find and print metrics for a given State");
			System.out.println("6. Print Spearman's rho matrix");
			System.out.println("7. quit");
			System.out.println();
			System.out.print("Enter selection and press return to continue: ");
			choice = Integer.parseInt(menuScanner.nextLine());
			if (choice > 7 || choice < 1) {
				System.out.println("Invalid input, please enter selections as an integer between 1 and 7 inclusive");
			}
		} while (choice > 7 && choice < 1);
		return choice;
	}
	public static void stateSwap(int s1, int s2, State[] states) {
		State temp = states[s1];
		states[s1] = states[s2];
		states[s2] = temp;
	}
	public static State[] selectionSort(State[] states) {
		int j;

		for (int i = 0; i < states.length - 1; i++) {
			// largest = states[i]
			for (j = i+1; j < states.length; j++) {
				if(states[j].getCaseFatalityRate() < states[i].getCaseFatalityRate()){
					stateSwap(j, i, states);
				}
			}
		}
		return states;
	}
	public static State[] insertionSort(State[] states) {
		int i = 0;
		int j = 0;
		State temp;
		for (i = 1; i < states.length; i++) {
			temp = states[i];
			for(j = i-1; j >= 0 && (states[j].getMedianIncome() > temp.getMedianIncome()); j--) {
				states[j+1] = states[j];
			}
			states[j+1] = temp;
		}
		return states;
	}
	public static State[] bubbleSort(State[] states) {
		/* sort by name
		 * 1) compare leftmost item with second leftmost
		 * 		if smaller, swap
		 * 		else compare with next element to left
		 * 		stop when front of array is reached
		 */
		int i;
		int j;
		State temp;

		for (i = 0; i < states.length - 1; i++) {
			for (j = states.length-1; j>i; j--) {
				if (states[j].getName().compareTo(states[j-1].getName()) < 0) { // compares names of states alphabetically
					stateSwap(j, (j-1), states);
				}
			}
		}
		return states;
	}
	public static void selectionSearch(String stateName, State[] states){
		for (State state : states){
			if (stateName.equals(state.getName())){
				singleStateReport(state);
				return;
			}
		}
		System.out.println("Search failed.");
		return;
	}
	public static void binarySearch(String stateName, State[] states){
		int low = 0;
		int high = states.length - 1;
		int mid = (high + low) / 2;

		while (low <= high) {
			if (stateName.equals(states[mid].getName())) {
				singleStateReport(states[mid]);
				return;
			} else if (stateName.compareTo(states[mid].getName()) < 0 ) {
				high = mid - 1;
				mid = (high + low) / 2;
			} else if (stateName.compareTo(states[mid].getName()) > 0) {
				low = mid + 1;
				mid = (high + low) / 2;
			}
		}
		System.out.println("search failed 😓");
	}
	public static void singleStateReport(State state) {
		System.out.println();
		System.out.println("State: " + state.getName());
		System.out.println("MHI: " + state.getMedianIncome());
		System.out.println("VCR: " + state.getViolentRate());
		System.out.println("CFR: " + state.getCaseFatalityRate());
		System.out.println("Case Rate: " + state.getCaseRate());
		System.out.println("Death Rate: " + state.getDeathRate());
		System.out.println();
	}
	public static void stateReport(State[] states){
		State s;
		System.out.println("Name           | MHI      | VCR      | CFR       | Case Rate | Death Rate");
		System.out.println("-------------------------------------------------------------------------");
		for (int i = 0; i < states.length; i++) {
			s = states[i];
			System.out.format("%-15s| %-9d| %-9.2f| %-10.6f| %-10.2f| %-10.2f%n",
					s.getName(),
					s.getMedianIncome(),
					s.getViolentRate(),
					s.getCaseFatalityRate(),
					s.getCaseRate(),
					s.getDeathRate());

		}
	}

}
