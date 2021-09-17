import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
public class Project1 {

	public static void main(String[] args) throws IOException {

		int choice = 0;
		int index;
		boolean isAlphabetical = false;
		//File stateFile = getFile();
		File stateFile = new File("States1.csv"); // hardcoded for now, don't leave me like this
		BufferedReader fileReader = new BufferedReader(new FileReader(stateFile));
		State[] stateList = new State[50]; // assumes that there are a maximum of 50 records in file
		fileReader.readLine().split(","); // ignore top line of CSV file
		
		for(int i = 0; i < 50; i++) {	// load array of States with entities constructed from CSV data
			String[] elementArray = fileReader.readLine().split(",");
			stateList[i] = new State(elementArray);
		}
		do {
			choice = printMenu();
			if (choice == 1) stateReport(stateList); // prints out a state report (all states)
			else if (choice == 2) {		// sorts alphabetically
				stateList = bubbleSort(stateList);
				isAlphabetical = true;
			}
			else if (choice == 3) {		// sorts by fatality rate
				stateList = selectionSort(stateList);
				isAlphabetical = false;
			}
			else if (choice == 4) {		// sorts by MHI
				stateList = insertionSort(stateList);
				isAlphabetical = false;
			}
			else if (choice == 5) {		// find state for given name
				Scanner scan = new Scanner(System.in);
				System.out.print("enter state name: ");
				if (isAlphabetical) {
					index = binarySearch(scan.nextLine(), stateList);
					singleStateReport(stateList[index]);
				}
				else {
					index = selectionSearch(scan.nextLine(), stateList);
					if (index!= -1) {
						singleStateReport(stateList[index]);
					}
				}
			}
			else if (choice == 6) {
				spearmanMatrix(stateList);
			}

		} while (choice != 7);

	}

	/**
	 * Prompts user for the name of the CSV file to use
	 * @return userInput: string that user specified with keyboard input
	 */
	public static File getFile() {
		Scanner inputScanner = new Scanner(System.in);
		System.out.print("File name: ");
		File userFile = new File(inputScanner.nextLine());
		while (!userFile.exists()) {
			System.out.println("File does not exist");
			System.out.println("File name: ");
			userFile = new File(inputScanner.nextLine());
		}
		return userFile;
	}
	public static int printMenu() {
		Scanner menuScanner = new Scanner(System.in);
		int choice = 0;
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
			try {
				choice = Integer.parseInt(menuScanner.nextLine());
			} catch (Exception NumberFormatException){
				System.out.println("  --Your input could not be processed, please only use integers as input.");
			}
			if (choice > 7 || choice < 1) {
				System.out.println("  --Invalid input, enter an integer between 1 and 7 inclusive.");
			}
		} while (choice > 7 && choice < 1);
		return choice;
	}
	public static void spearmanMatrix (State[] states) {
		/* 
		 * For each intersection
		 * 		1) calculate di for each state
		 * 			a. get difference between ranking of element for both data points
		 * 				  i. start with alphabetically sorted array, get String name of element n
		 * 				 ii. get rankings of that element by finding index of that element in both arrays
		 * 				iii. (ranking.array1 - ranking.array2) = difference
		 * 			b. square that value
		 * 			
		 * 		2) sum all di of each state (sum += currentstate.di)
		 * 		3) plug into formula
		 * 		4) save as variable for that intersection
		*/
		/*		
		 * 	For Median Household Income, Violent Crime Rate, Covid Case Rate, Covid Death Rate
		 * 		------------------------------
		 * 		|        |   MHI   |   VCR   |
		 * 		------------------------------          		  
	     *		|  CCR   |   x1    |   x2    |
	     *		------------------------------
		 *      |  CDR   |   x3    |   x4    |
		 *      ------------------------------
		 */
		float[] spearmanVal = new float[4];
		State[] caseRateList = states.clone();
		State[] deathRateList = states.clone();
		State[] violentRateList = states.clone(); 
		State[] medianIncomeList = states.clone();
		caseRateList = caseRateSort(caseRateList);
		deathRateList = deathRateSort(deathRateList);
		violentRateList = violentRateSort(violentRateList);
		medianIncomeList = insertionSort(medianIncomeList);
		
		spearmanVal[0] = spearmanRho(sumRankDiff(caseRateList, medianIncomeList), states.length);
		spearmanVal[1] = spearmanRho(sumRankDiff(caseRateList, violentRateList), states.length);
		spearmanVal[2] = spearmanRho(sumRankDiff(deathRateList, medianIncomeList), states.length);
		spearmanVal[3] = spearmanRho(sumRankDiff(deathRateList, violentRateList), states.length);
		
		System.out.println(" -------------------------------------");
		System.out.println( "|           |    MHI     |     VCR    |");
		System.out.println(" -------------------------------------");
		System.out.format( "|Case Rate  |  %7.4f   |  %7.4f   |%n", spearmanVal[0], spearmanVal[1]);
		System.out.println(" -------------------------------------");
		System.out.format( "|Death Rate |  %7.4f   |  %7.4f   |%n", spearmanVal[2], spearmanVal[3]);
		System.out.println(" -------------------------------------");
		
	}
	public static float spearmanRho(int sum, int size) {
		float top = (6 * (float) sum);
		float bottom = (size * ((size*size) - 1));
		float result = top/bottom;
		return result;
	}
	/**
	 * calculates the sum of d<sub>i</sub><sup>2</sup> for <code>State[] list1</code> and <code>State[] list2</code>
	 * @param list1 array of <code>State</code> objects
	 * @param list2 array of <code>State</code> objects
	 */
	public static int sumRankDiff(State[] list1, State[] list2) {
		int sum = 0;
		int diff = 0;
		int rank1;
		int rank2;
		
		for (State state : list1) {
			// index of state[i] in list1 - index of that same state in list 2
			rank1 = selectionSearch(state.getName(), list1);
			rank2 = selectionSearch(state.getName(), list2);
			diff = rank1 - rank2;
			sum += (diff * diff);
		}
		return sum;
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
				if(states[j].getCaseFatalityRate() 
						< states[i].getCaseFatalityRate()){
					stateSwap(j, i, states);
				}
			}
		}
		System.out.println("State list sorted by Case Fatality Rate (CFR)");
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
		System.out.println("State list sorted by Median Household Income (MHI)");
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
		System.out.println("State list sorted Alphabetically");
		return states;
	}
	/**
	 * Uses an optimized bubble sort with a boolean variable to sort array of State objects by COVID case rate
	 * @param Array of State objects
	 * @return array of State objects sorted by case rate ascending
	 */	
	public static State[] caseRateSort (State[] states ) {
		int i;
		boolean hasSwapped = true;
		while (hasSwapped) {
			hasSwapped = false;
			for (i = 1; i < states.length; i++) {
				if (states[i].getCaseRate() < states[i-1].getCaseRate()) {
					hasSwapped = true;
					stateSwap(i, (i-1) , states);
				}
			}
		}
		System.out.println("Sorted by Case Rate");
		return states;
	}
	/**
	 * Uses an optimized bubble sort with a boolean variable to sort array of State objects by Violent Crime Rate
	 * @param Array of State objects
	 * @return Array of State objects sorted by violent crime rate ascending
	 */	
	public static State[] violentRateSort (State[] states ) {
		int i;
		boolean hasSwapped = true;
		while (hasSwapped) {
			hasSwapped = false;
			for (i = 1; i < states.length; i++) {
				if (states[i].getViolentRate() < states[i-1].getViolentRate()) {
					hasSwapped = true;
					stateSwap(i, (i-1) , states);
				}
			}
		}
		System.out.println("Sorted by Violent Crime Rate");
		return states;
	}
	/**
	 * Uses an optimized bubble sort with a boolean variable to sort array of State objects by COVID death rate (note: <b>not</b> the same as COVID case death rate)
	 * @param Array of State objects
	 * @return Array of State objects sorted by COVID death rate  ascending
	 */	
	public static State[] deathRateSort (State[] states ) {
		int i;
		boolean hasSwapped = true;
		while (hasSwapped) {
			hasSwapped = false;
			for (i = 1; i < states.length; i++) {
				if (states[i].getDeathRate() < states[i-1].getDeathRate()) {
					hasSwapped = true;
					stateSwap(i, (i-1) , states);
				}
			}
		}
		System.out.println("Sorted by Death Rate");
		return states;
	}
	/**
	 * 
	 * @param stateName
	 * @param State[] states
	 * @return index of State with name stateName in State[] states
	 */
	public static int selectionSearch(String stateName, State[] states){
		for (int i = 0; i < states.length; i++){
			if (stateName.equals(states[i].getName())){
				//singleStateReport(state);
				return i;
			}
		}
		System.out.println("  --Search failed.");
		return -1;
	}
	public static int binarySearch(String stateName, State[] states){
		int low = 0;
		int high = states.length - 1;
		int mid = (high + low) / 2;

		while (low <= high) {
			if (stateName.equals(states[mid].getName())) {
				singleStateReport(states[mid]);
				return mid;
			} else if (stateName.compareTo(states[mid].getName()) < 0 ) {
				high = mid - 1;
				mid = (high + low) / 2;
			} else if (stateName.compareTo(states[mid].getName()) > 0) {
				low = mid + 1;
				mid = (high + low) / 2;
			}
		}
		System.out.println("  --search failed");
		return -1;
	}
	public static void singleStateReport(State state) {
		System.out.println();
		System.out.println("State: " + state.getName());
		System.out.println("Median Household Income: " + state.getMedianIncome());
		System.out.println("Violent Crime Rate: " + state.getViolentRate());
		System.out.println("Covid case Fatality Rate: " + state.getCaseFatalityRate());
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
