/**
 * Create a class named State that will store information about a State and provide methods to 
 * get, and set the data, and compare the States by several fields. 
 * 	a. Fields: name, capitol, region, US House seats, population, COVID-19 cases, COVID-
 * 	   19 deaths, median household income, and violent crime rate. 
 * 	b. Constructor 
 * 	c. Get and set methods for each field 
 *	d. Method to print a State object
 * @author M A N
 *
 */
public class State {
	private String name;
	private String capitol;
	private String region;
	private int houseSeats;
	private int population;
	private int covidCases;
	private int covidDeaths;
	private int medianIncome;
	private float violentRate;
	
	/**
	 * Constructor for State objects
	 */
	public State(String[] elements) {
		setName(elements[0]);
		setCapitol(elements[1]);
		setRegion(elements[2]);
		setHouseSeats(Integer.parseInt(elements[3]));
		setPopulation(Integer.parseInt(elements[4]));
		setCovidCases(Integer.parseInt(elements[5]));
		setCovidDeaths(Integer.parseInt(elements[6]));
		setMedianIncome(Integer.parseInt(elements[7]));
		setViolentRate(Float.parseFloat(elements[8]));
	}
	public static void statePrinter(State state) {
		System.out.println("State: " + state.getName() );
		System.out.println("Capitol: " + state.getCapitol() );
		System.out.println("Region: " + state.getRegion() );
		System.out.println("House Seats: " + state.getHouseSeats() );
		System.out.println("Population: " + state.getPopulation() );
		System.out.println("Covid Cases: " + state.getCovidCases()	 );
		System.out.println("Covid Deaths: " + state.getCovidDeaths() );
		System.out.println("Median Household Income: " + state.getMedianIncome() );
		System.out.println("Violent Crime Rate: " + state.getViolentRate() );
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCapitol() {
		return capitol;
	}
	public void setCapitol(String capitol) {
		this.capitol = capitol;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public int getHouseSeats() {
		return houseSeats;
	}
	public void setHouseSeats(int houseSeats) {
		this.houseSeats = houseSeats;
	}
	public int getPopulation() {
		return population;
	}
	public void setPopulation(int population) {
		this.population = population;
	}
	public int getCovidCases() {
		return covidCases;
	}
	public void setCovidCases(int covidCases) {
		this.covidCases = covidCases;
	}
	public int getCovidDeaths() {
		return covidDeaths;
	}
	public void setCovidDeaths(int covidDeaths) {
		this.covidDeaths = covidDeaths;
	}
	public int getMedianIncome() {
		return medianIncome;
	}
	public void setMedianIncome(int medianIncome) {
		this.medianIncome = medianIncome;
	}
	public float getViolentRate() {
		return violentRate;
	}
	public void setViolentRate(float violentRate) {
		this.violentRate = violentRate;
	}
}
