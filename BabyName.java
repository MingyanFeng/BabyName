
/**
 * Data files gives information on the first names of people born in a particular year. 
 * All data from 1880 through 2014 on both boys and girls names are used in this project.
 * 
 * @Mingyan Feng
 * @Modify:01/02/2017
 */
import edu.duke.*;
import org.apache.commons.csv.*;
import java.io.*;

public class BabyName {
    public void printNames () {
		FileResource fr = new FileResource();
		for (CSVRecord rec : fr.getCSVParser(false)) {
			int numBorn = Integer.parseInt(rec.get(2));
			if (numBorn <= 100) {
				System.out.println("Name " + rec.get(0) +
						   " Gender " + rec.get(1) +
						   " Num Born " + rec.get(2));
			}
		}
	}

    public void totalBirths (FileResource fr) {
	    int totalBirths = 0;
		int totalBoys = 0;
		int totalGirls = 0;
		int numOfGirlsName = 0;
		int numOfBoysName = 0;
		int totalNames = 0;
    	for (CSVRecord rec : fr.getCSVParser(false)) {
			int numBorn = Integer.parseInt(rec.get(2));
			totalBirths += numBorn;
    		if (rec.get(1).equals("M")) {
    				totalBoys += numBorn;
    				numOfBoysName++;
    			}
    		else {
    				totalGirls += numBorn;
    				numOfGirlsName++;
    		}
        }
        totalNames = numOfGirlsName + numOfBoysName;
        System.out.println("Total births = " + totalBirths);
		System.out.println("Female girls = " + totalGirls);
		System.out.println("Male boys = " + totalBoys);
		System.out.println("The number of girls names = " + numOfGirlsName);
		System.out.println("The number of boys names = " + numOfBoysName);
		System.out.println("The number of total names = " + totalNames);
    }

	public void testTotalBirths () {
		//FileResource fr = new FileResource();
		String fileAddress = "C:\\Users\\xuyi1\\Desktop\\us_babynames\\us_babynames_by_year\\yob1905.csv";
		FileResource fr  = new FileResource(fileAddress);
		totalBirths(fr);
	}
	
	//This method returns the rank of the name in the file for the given gender
	public int getRank(int year, String name, String gender) {
		String fileAddress = "C:\\Users\\xuyi1\\Desktop\\us_babynames\\us_babynames_by_year\\yob" + Integer.toString(year) + ".csv";
		FileResource fr  = new FileResource(fileAddress);
		int numGivenNameGender = -1;
		int rank = 1;
		for (CSVRecord rec : fr.getCSVParser(false)) {
		//If rec.get(0).equals(name) && rec.get(1).equals(gender), rec.get(2)
		//Get the given gender's name number(int), sets numGivenNameGender
			if (rec.get(0).equals(name) && rec.get(1).equals(gender)) {
				numGivenNameGender = Integer.parseInt(rec.get(2));
				break;
			}
		}
		//If the name is not in the file, then -1 is returned. 
		if (numGivenNameGender < 0 ) {
			return -1;
		}
		
		for (CSVRecord rec : fr.getCSVParser(false)) {
			if (rec.get(1).equals(gender) && !rec.get(0).equals(name)) {
					rank++;
			}
			if (rec.get(1).equals(gender) && rec.get(0).equals(name)) {
				break;
			}
		}
		return rank;
	}
	
	public String getName (int year, int rank, String gender) {
		int tempRank = 1;
		String fileAddress = "C:\\Users\\xuyi1\\Desktop\\us_babynames\\us_babynames_by_year\\yob" + Integer.toString(year) + ".csv";
		FileResource fr  = new FileResource(fileAddress);
		for (CSVRecord rec : fr.getCSVParser(false)) {
			if (rec.get(1).equals(gender)) {
				if (tempRank == rank) {
					return rec.get(0);
				}
				else {
					tempRank++;
				}
			}
		}
		return new String("NO NAME");
	}
	
	public String whatIsNameInYear(String name, int year, int newYear, String gender) {
		int rankInBornYear = getRank(year, name, gender);
		String nameInBornYear = getName(year, rankInBornYear, gender);
		String nameInNewYear = getName(newYear, rankInBornYear, gender);
		if (gender.equals("F")) {
			return new String(nameInBornYear + " born in " + year + " would be " + nameInNewYear + " if she was born in " + newYear);
		}
		else{
			return new String(nameInBornYear + " born in " + year + " would be " + nameInNewYear + " if he was born in " + newYear);
		}
	}
	
	public int yearOfHighestRank(String name, String gender) {
		DirectoryResource dr = new DirectoryResource();
		int highestRank = Integer.MAX_VALUE;
		int fileYear = 0;
		for (File f : dr.selectedFiles()){
			FileResource fr = new FileResource(f);
			int currentRank = 1;
			int numGivenNameGender = -1;
			for (CSVRecord rec : fr.getCSVParser(false)) {
				//If rec.get(0).equals(name) && rec.get(1).equals(gender), rec.get(2)
				//Get the given gender's name number(int), sets numGivenNameGender
				if (rec.get(1).equals(gender) && rec.get(0).equals(name)) {
					numGivenNameGender = Integer.parseInt(rec.get(2));
					break;
				}
				if (rec.get(1).equals(gender) && !rec.get(0).equals(name)) {
					continue;
				}
			}
			//If the name is not in the file, then -1 is returned. 
			/*if (numGivenNameGender < 0 ) {
				return -1;
			}
			*/
			for (CSVRecord rec : fr.getCSVParser(false)) {
				if (rec.get(1).equals(gender) && !rec.get(0).equals(name)) {
					currentRank++;
				}
				if (rec.get(1).equals(gender) && rec.get(0).equals(name)) {
					break;
				}
			}
			if (currentRank < highestRank) {
				highestRank = currentRank;
				fileYear = Integer.parseInt(f.getName().substring(3, 7));
			}
		} 
		return fileYear;
	}
	
	public double getAverageRank(String name, String gender) {
		DirectoryResource dr = new DirectoryResource();
		double rankSum = 0;
		double yearSum = 0;
		for (File f : dr.selectedFiles()){
			FileResource fr = new FileResource(f);
			double currentRank = 1;
			double numGivenNameGender = -1;
			for (CSVRecord rec : fr.getCSVParser(false)) {
				//If rec.get(0).equals(name) && rec.get(1).equals(gender), rec.get(2)
				//Get the given gender's name number(int), sets numGivenNameGender
				if (rec.get(0).equals(name) && rec.get(1).equals(gender)) {
					numGivenNameGender = Integer.parseInt(rec.get(2));
					break;
				}
			}
			//If the name is not in the file, then -1 is returned. 
			if (numGivenNameGender < 0 ) {
				return -1.0;
			}
			for (CSVRecord rec : fr.getCSVParser(false)) {
				if (rec.get(1).equals(gender) && !rec.get(0).equals(name)) {
					currentRank++;
				}
				if (rec.get(1).equals(gender) && rec.get(0).equals(name)) {
					break;
				}
			}
			if (currentRank > 0) {
				rankSum += currentRank;
				yearSum++;
			}
		}
		return rankSum / yearSum;
	}

	public int getTotalBirthsRankedHigher(int year, String name, String gender) {
		DirectoryResource dr = new DirectoryResource();
		int rankedHigherSum = 0;
		for (File f : dr.selectedFiles()){
			FileResource fr = new FileResource(f);
			double currentRank = 1;
			double numGivenNameGender = -1;
			for (CSVRecord rec : fr.getCSVParser(false)) {
				//If rec.get(0).equals(name) && rec.get(1).equals(gender), rec.get(2)
				//Get the given gender's name number(int), sets numGivenNameGender
				if (rec.get(0).equals(name) && rec.get(1).equals(gender)) {
					numGivenNameGender = Integer.parseInt(rec.get(2));
					break;
				}
			}
			//If the name is not in the file, then -1 is returned. 
			if (numGivenNameGender < 0 ) {
				return -1;
			}
			for (CSVRecord rec : fr.getCSVParser(false)) {
				if (rec.get(1).equals(gender) && !rec.get(0).equals(name)) {
					currentRank++;
					rankedHigherSum += Integer.parseInt(rec.get(2));
				}
				if (rec.get(1).equals(gender) && rec.get(0).equals(name)) {
					break;
				}
			}
		}
		return rankedHigherSum;
	}
}
