import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Time;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.text.ParseException;

public class ParkingSystem 
{
	private ArrayList<String> allCarsIn;
	private Map<String, ArrayList<Car>> byFloor;
	private int maxFloors;
	private int[][] carsPerFloor;
	private double profits;
	private double charge;
	private double[] rates;
	private boolean[] IDs;
	private int maxSpots;
	
	public ParkingSystem()
	{
		allCarsIn = new ArrayList<>();
		byFloor = new TreeMap<>();
		profits = charge = 0;
		rates = new double[3];
		maxSpots = 0;
		readFile();

	}
	public static void main(String[] args)
	{
		ParkingSystem ps = new ParkingSystem();
		ps.testProgram();
	}
	public void testProgram()
	{
		
		Scanner input = new Scanner(System.in);
		
		int decision = 0;
		
		while(decision != 4)
		{
			System.out.println("click 1 for park, 2 for unpark, 3 for profit, "
					+ "and 4 to quit");
			decision = input.nextInt();
			input.nextLine();
			
			if(decision == 1)
			{
				assignCarToFloor();
			}
			else if(decision == 2)
			{
				int ID = 0;
				System.out.println("Enter ID of the car to unpark");
				ID = input.nextInt();
				System.out.println("\n" + calculateCharge(ID));
				unparkCar(ID);
			}
			else if(decision == 3)
			{
				System.out.println(calculateProfits());
			}
			else if(decision == 4)
			{
				writeCarsToFile();
			}
		}
	}
	public void unparkCar(int ID)
	{
		IDs[ID] = false;
		
		String floor = getFloorByPlan(ID) + "";
		Car temp = null;
		int floorSize = byFloor.get(floor).size();
			
		for(int i = 0; i < floorSize; i++)
		{
			//System.out.println(i + " " + floorSize);
			temp = byFloor.get(floor).get(i);
			if(temp.getID() == ID)
			{
				byFloor.get(floor).remove(i);
				i += floorSize;
			}
		}		
	}
	public boolean doesCarExist(int ID)
	{
		String floor = getFloorByPlan(ID) + "";
		Car temp = null;
		int floorSize = byFloor.get(floor).size();
		
		for(int i = 0; i < floorSize; i++)
		{
			temp = byFloor.get(floor).get(i);
			if(temp.getID() == ID)
				return true;
			
		}	
		
		return false;
	}
	public Car assignCarToFloor()
	{		
		//fix random spot generator
		int randomSpot = 0;
		boolean isFull = true;
		do
		{
			randomSpot = (int)(Math.random()*maxSpots);		
		
			isFull = true;
			for(int i = 0; i < IDs.length; i++)
			{
				if(!IDs[i])
					isFull = false;
			//System.out.println(i + " " + isFull);

			}
		}while(IDs[randomSpot] && !isFull);

		if(isFull)
			return null;
		
		IDs[randomSpot] = true;
		
		String floor = getFloorByPlan(randomSpot) + "";
		//System.out.println(floor);
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		Date date = new Date(System.currentTimeMillis());
		//System.out.println(formatter.format(date));
		
		IDs[randomSpot] = true;
		carsPerFloor[0][Integer.parseInt(floor)]++;
		
		Car newCar = new Car(randomSpot, 
					formatter.format(date));
					
		byFloor.get(floor).add(newCar);
		
		return newCar;
	}
	private int getFloorByPlan(int spotNumber)
	{
		int floorNum = byFloor.size();
		int counter = byFloor.size()-1;
		int spaceFinder = maxSpots;
		
		do
		{
			spaceFinder = spaceFinder - carsPerFloor[1][counter];
			counter--;
			floorNum--;
		}while(spaceFinder > spotNumber);
		
		return floorNum;
	}
	private void readFile()
	{
		try
		{
			Scanner readIn = new 
					Scanner(new File("CarsInBuilding.txt"));
			
			int floors = Integer.parseInt(readIn.nextLine());
			maxFloors = floors;
			carsPerFloor = new int[2][floors];
			
			profits = Double.parseDouble(readIn.nextLine());
			
			for(int i = 0; i < rates.length; i++)
			{
				rates[i] = Double.parseDouble(readIn.nextLine());
			}
			
			for(int i = 0; i < floors; i++)
			{
				String line = readIn.nextLine();
				
				
				int spacesTaken = Integer.parseInt(
						line.substring(0, line.indexOf("\t")));
				int spaces = Integer.parseInt(line.substring(line.indexOf("\t")+1));
				carsPerFloor[0][i] = spacesTaken;
				carsPerFloor[1][i] = spaces;
				maxSpots += spaces;
			}
			
			IDs = new boolean[maxSpots];
			
			//System.out.println(IDs.length);
			/*for(int i = 0; i < carsPerFloor.length; i++)
			{
				for(int j = 0; j < carsPerFloor[i].length; j++)
				{
					System.out.print(carsPerFloor[i][j] + " ");
				}
				System.out.println();
			}*/
			for(int i = 0; i < maxFloors; i++)
			{
				byFloor.put(i+"", new ArrayList<>());
			}
			while(readIn.hasNextLine())
			{
				String line = readIn.nextLine();
				allCarsIn.add(line);
				String[] parts = line.split("\t");
				
				//System.out.println(parts[2] + " " + parts[0] + " " + parts[1]);
				
				int IDOfThisCar = Integer.parseInt(parts[0]);
				
				byFloor.get(parts[2]).add(
							new Car(IDOfThisCar, parts[1]));
				IDs[IDOfThisCar] = true;	
				
			}
			
			readIn.close();
		}
		catch(FileNotFoundException e)
		{
			System.out.println("File not found");
		}
	}
	public void writeCarsToFile()
	{
		try
		{
			PrintWriter writer = new PrintWriter("CarsInBuilding.txt");
			
			writer.println(maxFloors);
			writer.println(profits);
			
			for(int i = 0; i < rates.length; i++)
			{
				writer.println(rates[i]);
			}
			for(int i = 0; i < maxFloors; i++)
			{
				writer.println(carsPerFloor[0][i]
						+ "\t" + carsPerFloor[1][i]);
			}
			
			for(int i = 0; i < IDs.length; i++)
			{
				if(IDs[i])
				{
					String floor = getFloorByPlan(i) + "";
					int floorSize = byFloor.get(floor).size(); 
					
					for(int j = 0; j < floorSize; j++)
					{
						Car temp = byFloor.get(floor).get(j);
						if(temp.getID() == i)
						{
							writer.println(i + "\t" + temp.getTime()
							  + "\t" + floor);
							j += floorSize; 
						}
					}
				}
			}
			writer.close();
		}
		catch(FileNotFoundException e)
		{
			System.out.println("File Not Found");
		}
	}
	
	private Car getCar(int ID)
	{
		String floor = getFloorByPlan(ID) + "";
		Car temp = null;
		int floorSize = byFloor.get(floor).size();
		
		for(int i = 0; i < floorSize; i++)
		{
			temp = byFloor.get(floor).get(i);
			if(temp.getID() == ID)
				i += floorSize;
		}

		return temp;
	}
	public double calculateProfits()
	{
		return profits;
	}
	public double calculateCharge(int ID)
	{
		charge = 0;
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

		if(!doesCarExist(ID))
		{
			return 0;
		}
		try
		{
			Date d1 = formatter.parse(getCar(ID).getTime());
			Date d2 = new Date(System.currentTimeMillis());
			
			//in milliseconds
			long diff = d2.getTime() - d1.getTime();
			long diffSeconds = diff / 1000 % 60;
			long diffMinutes = diff / (60 * 1000) % 60;
			long diffHours = diff / (60 * 60 * 1000) % 24;
			long diffDays = diff / (24 * 60 * 60 * 1000);
	
			/*System.out.print(diffDays + " days, ");
			System.out.print(diffHours + " hours, ");
			System.out.print(diffMinutes + " minutes, ");	
			System.out.print(diffSeconds + " seconds.");*/
			
			if(diffHours < 1 && diffDays == 0)
				charge = rates[0];
			else if(diffHours < 5 && diffDays == 0)
				charge = diffHours*rates[0];
			else if(diffHours < 24 && diffDays == 0)
				charge = diffHours*rates[1];
			else
				charge = diffDays*rates[2];
		}
		catch(ParseException e)
		{
			System.out.println("Did not calculate charges");
		}

		profits += charge;
		return charge;
	}
	public void setFloors(int floors)
	{
		maxFloors = floors;
	}
	public int getFloors()
	{
		return maxFloors;
	}
	public Map<String, ArrayList<Car>> getCarsInFloors()
	{
		return byFloor;
	}
	public void setCarsPerFloor(int[][] carsInFloors)
	{
		carsPerFloor = carsInFloors;
	}
	public void setRates(double perHour, double per5Hours, double perDay)
	{
		rates[0] = perHour;
		rates[1] = per5Hours;
		rates[2] = perDay;
	}
	
	public void setPlan(int totalFloors, int [][] copyCarsFloor)
	{		
		maxSpots = 0;
		carsPerFloor = new int[2][totalFloors];
		for(int i = 0; i < totalFloors; i++)
		{
			carsPerFloor[0][i] = copyCarsFloor[0][i];
			carsPerFloor[1][i] = copyCarsFloor[1][i];
			maxSpots += copyCarsFloor[1][i];
		}
		
		maxFloors = totalFloors;
		byFloor = new TreeMap<>();
		for(int i = 0; i < maxFloors; i++)
		{
			byFloor.put(i+"", new ArrayList<>());
		}
		allCarsIn = new ArrayList<>();
		IDs = new boolean[maxSpots];
		for(int i = 0; i < IDs.length; i++)
		{
			IDs[i] = false;
		}
	}
	public double[] getRates()
	{
		return rates;
	}
	public int[][] getCarsPerFloor()
	{
		return carsPerFloor;
	}
	public double getProfits()
	{
		return profits;
	}
	public double getCharge()
	{
		return charge;
	}
}
