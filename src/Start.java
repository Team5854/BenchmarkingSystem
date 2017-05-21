import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;
import java.util.Scanner;

public class Start {
	
	//keyboard inputs to call menus.
	private static String userCode = "5854";
	private static String adminCode = "112";//"15201027";
	
	
	
	
	private static boolean debugging = false;// whether or not to run debugging printouts.
	private static boolean coderDebug = false;
	private static Scanner scan = new Scanner(System.in);
	private static RFIDThread noder; // thread for scanning the rfid
	private static ScannerThread inputer; //thread for getting input from user.
	private static UsersController users; //handles saving, adding,editing, and holding users.
	private static boolean firstTime = true;
	private static double logoutTime = 14.6;
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		inputer = new ScannerThread(); // initalize thread.
		noder = new RFIDThread(); // initalize thread.
		users = new UsersController(); // initalize user object.
		
		inputer.start(); //start the thread
		noder.start(); //start the thread
		
		String data = ""; //stores current Time.
		String number = ""; // current Id
		while (true) { //forever (until program closed)
			if ((data = inputer.getInput()) != null) { //get data from scanner, if it is not null do
				if (adminCode.equals(data)) { // if data is the admin code then
					inputer.pause(); // pause the thread for getting input.
					noder.pause(); //pause thread for getting rfid
					adminMenu(number); // call admin menu and pass current ID Card.
					//inputer.unPause();
					//noder.unPause();
				}
				if (userCode.equals(data)) {//if data is user code then
					inputer.pause();// pause thread
					noder.pause(); // pause thread.
					menu(number); // call user menu and pass current ID.
					//inputer.unPause();
					//noder.unPause();
				}
			}
			
			//This part deals with getting the data, then formatting it into the needed format.
			DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm"); //format date
			Calendar calobj = Calendar.getInstance(); // get a calendar
			String DateTime = df.format(calobj.getTime());
			int dayOfWeek = calobj.get(Calendar.DAY_OF_WEEK);
			//System.out.println(DateTime);
			String[] Time = DateTime.split(" ")[1].split(":");
			double Hours = Double.parseDouble(Time[0]);
			double Minutes = Double.parseDouble(Time[1]);
			DecimalFormat df1 = new DecimalFormat("###.##");
			double dec = Double.parseDouble(df1.format(Minutes / 60));
			double curTime = Hours + dec;
			printDebug(DateTime + "  --  " + curTime, "date");
			//System.out.print("ID: ");
			
			//get the RFID if scanned without a menu option. Log in the user.
			if ((number = noder.getNumber()) != null) {
				printCoderDebug(number);
				//find the user in the saved user files.
				String PIN = users.getPin(number);
				int index = users.findUserByID(PIN);
				//if the user was found then update the time logged in to current time.
				if (index != -1) {
					users.users.get(index).LastCheckInTime = curTime;
					users.saveUsers(); //save the user to the file.
				}
			}
			
			
			/*
			 * If the current time is equal to logout time then logout 
			 * all users on the system and update their total hours
			 */
			if (curTime == logoutTime) {
				//System.out.println(curTime);
				if (firstTime) {
					firstTime = false;
					System.out.println("Logging Out All Users");
					for (User u : users.users) { // for all users
						if (u.LastCheckInTime != -1) { // if they did log in today
							DecimalFormat df2 = new DecimalFormat("###.##");
							double tht = Double.parseDouble(df2.format(curTime - u.LastCheckInTime));
							u.TotalHours += tht; //update their total hours
							u.LastCheckInTime = -1; // and set them to logged out.
						}
					}
					users.saveUsers();// save all users.
				}
			}
			//if current time is a minute after the logout then set the variable to allow for logout tommorrow.
			//makes it so it doesnt keep loggin out for a minute. (only logs out once)
			if (curTime == logoutTime+0.1) {
				firstTime = true;
			}
		}
	}


	/*
	 * the user menu... when input is user code then output menu.
	 */
	private static void menu(String number) {
		System.out.println("<<MENU>>");
		System.out.println("- Logout [0]");
		System.out.println("- Show Current ID Card [1]");
		String answer = scan.nextLine();
		printDebug("Your Choice: " + answer);
		printDebug("ID : " + number);
		switch (answer) {
			case "0": //if they choose to logout then log them out.
				checkOut();
				break;
			case "1":// display users if they ask.
				displayCurrentUsers(); 
				break;
				
			default: // if it is not a normal choice output from them to do it again.
				System.out.println("That is not a choice");
				menu(number);
				break;
		}
	}
	
	
	/*
	 * menu for admins to edit users and debug system.
	 */
	private static void adminMenu(String number) {
		System.out.println("<<MENU>>");
		System.out.println("- Add new User [0]");
		System.out.println("- Fix Time For User [1]");
		System.out.println("- Toggle Debugging [2]");
		System.out.println("- Show Current ID Card [3]");
		System.out.println("- Display Current Users [4]");
		String answer = scan.nextLine();
		printDebug("Your Choice: " + answer);
		printDebug("ID : " + number);
		switch (answer) {
			case "0": //choice 0 is for adding a new user.
				addNewUser();
				break;
			case "1": //add or remove time from a user.
				FixTime();
				break;
			case "2": // enable or disable debugging.
				debugging = !debugging;
				noder.unPause(); //unpause RFID thread.
				inputer.unPause(); //unpause input thread.
				break;
			case "3": //display current ID
				System.out.println(number);
				noder.unPause(); //unpause
				inputer.unPause();
				break;
			case "4": //display all users in the system
				displayCurrentUsers();
				break;
				
			default: //ask them to input a diffrent choice.
				System.out.println("That is not a choice");
				adminMenu(number);
				break;
		}
	}
	
	/*
	 * logging out users method.
	 */
	private static void checkOut() {
		System.out.println("Checking Out?");
		
		//get the current date for totalling today's hours.
		DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
		Calendar calobj = Calendar.getInstance();
		String DateTime = df.format(calobj.getTime());
		printDebug(DateTime);
		String[] Time = DateTime.split(" ")[1].split(":");
		double Hours = Double.parseDouble(Time[0]);
		double Minutes = Double.parseDouble(Time[1]);
		DecimalFormat df1 = new DecimalFormat("###.##");
		double dec = Double.parseDouble(df1.format(Minutes / 60));
		double curTime = Hours + dec;
		
		
		String number = null;
		if ((number = noder.getNewNumber()) != null) { //have them swipe their card to logout.
			//find the user in the system.
			String PIN = users.getPin(number);
			int index = users.findUserByID(PIN);
			//if user found then
			if (index != -1) {
				//format the total time
				DecimalFormat df11 = new DecimalFormat("###.##");
				double dec1 = Double.parseDouble(df11.format(curTime - users.users.get(index).LastCheckInTime));
				//add time to total time
				users.users.get(index).TotalHours += dec1;
				//set them to logged out
				users.users.get(index).LastCheckInTime = -1;
				//save all users (just in case)
				users.saveUsers();
			}
		}
		System.out.println("Time Logged");
		
		noder.unPause(); //unpause threads.
		inputer.unPause();
	}
	
	
	/*
	 * add or remove threads.
	 */
	private static void FixTime() {
		String number = null;
		if ((number = noder.getNewNumber()) != null) { //have them scan their card
			//ask them to use plus or minus the amount of time to fix.
			System.out.print("Hours to change? + / -   >>");
			String answer = scan.nextLine(); 
			
			System.out.println(number);
			//find user
			String PIN = users.getPin(number);
			System.out.println(PIN);
			int index = users.findUserByID(PIN);
			//if user found then
			if (index != -1) {
				//add the fixed time to the total time.
				users.users.get(index).TotalHours += Double.parseDouble(answer);
				System.out.println("Time Fixed! Press any Button to continue.");
			} else {
				//else say the card could not be found
				System.out.println("Couldn't Find Card Number In Database!");
			}
			//this is for allowing a pause before continuing.
			answer = scan.nextLine();
		} else {
			//if card scanned incorrectly then rescan
			System.out.println("Card Not Scanned");
			FixTime();
		}
		noder.unPause();//unpause threads.
		inputer.unPause();
	}
	
	/**
	 * add a new user to the system.
	 */
	private static void addNewUser() {
		NodeJS writer = new NodeJS();
		
		//create a randomly generated pin
		int[] randomPin = new int[8];
		Random generator = new Random(); 
		for (int i = 0; i < randomPin.length; i++) {
			randomPin[i] = generator.nextInt(256);

		}
		//combine the bin into a single string.
		String PIN = "";
		for (int i = 0; i < randomPin.length-1; i++) {
			PIN += randomPin[i] + ",";
		}
		PIN += randomPin[randomPin.length-1];
		
		//search for if the random pin is already taken.
		boolean taken = false;
		for (int i = 0; i < users.users.size(); i++) {
			if (users.users.get(i).getID().equalsIgnoreCase(PIN)) {
				taken = true;
			}
		}
		//if the pin is not taken then
		if (!taken) {
			//write the pin to the card.
			writer.write(PIN);
			
			//ask for a name for the user.
			System.out.print("Name: ");
			String name = scan.nextLine();
			
			//test to see if the name is already in the database (don't have two users)
			boolean continueCreation = true;
			for (int i = 0; i < users.users.size(); i++) {
				System.out.println(users.users.get(i).getID());
				if (users.users.get(i).Name.equalsIgnoreCase(name)) {
					System.out.println("Name is already in database. Create User anyway? y/n");
					String yn = scan.nextLine();
					if (yn.equalsIgnoreCase("y")) {
						continueCreation = true;
						break;
					} else {
						continueCreation = false;
						break;
					}
				}
			}
			
			//if the name was not in the database.
			if (continueCreation) {
				//add the new user to the system,
				users.addNewUser(PIN, name);
				//then save all users.
				users.saveUsers();
				System.out.println("Users Saved");
			} else {
				System.out.println("User creation cancelled");
			}
			noder.unPause(); //unpause threads.
			inputer.unPause();
		}
				
	}
	
	/**
	 * displays all users in the system.
	 */
	public static void displayCurrentUsers() {
		for (int i = 0; i < users.users.size(); i++) {
			System.out.println(users.users.get(i).toWriter());
		}
		noder.unPause();
		inputer.unPause();
	}
	
	//if debugging is enabled then print.
	public static void printDebug(String print) {
		if (debugging)  {
			System.out.println(print);
		}
	}
	
	//used to not continue printing. checks if it is about to double an output.
	static String prevDate = "";
	private static void printDebug(String print, String var) {
		if (debugging)  {
			if ("date".equalsIgnoreCase(var)) {
				if (!prevDate.equalsIgnoreCase(print)) {
					prevDate = print;
					System.out.println(print);
				}
			}
		}
	}
	
	//allows for the coder (me) to make outputs that I use for debugging when writing the code.
	private static void printCoderDebug(String print) {
		if (coderDebug) {
			System.out.println(print);
		}
	}
}
