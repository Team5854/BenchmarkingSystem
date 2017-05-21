import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;
import java.util.Scanner;

public class Start {
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
			if ((number = noder.getNumber()) != null) {
				printCoderDebug(number);
				String PIN = users.getPin(number);
				int index = users.findUserByID(PIN);
				if (index != -1) {
					users.users.get(index).LastCheckInTime = curTime;
					users.saveUsers();
				}
			}
			
			if (curTime == logoutTime) {
				//System.out.println(curTime);
				if (firstTime) {
					firstTime = false;
					System.out.println("Logging Out All Users");
					for (User u : users.users) {
						if (u.LastCheckInTime != -1) {
							DecimalFormat df2 = new DecimalFormat("###.##");
							double tht = Double.parseDouble(df2.format(curTime - u.LastCheckInTime));
							u.TotalHours += tht;
							u.LastCheckInTime = -1;
						}
					}
					users.saveUsers();
				}
			}
			if (curTime == logoutTime+0.1) {
				firstTime = true;
			}
		}
	}



	private static void menu(String number) {
		System.out.println("<<MENU>>");
		System.out.println("- Logout [0]");
		System.out.println("- Show Current ID Card [1]");
		String answer = scan.nextLine();
		printDebug("Your Choice: " + answer);
		printDebug("ID : " + number);
		switch (answer) {
			case "0":
				checkOut();
				break;
			case "1":
				displayCurrentUsers();
				break;
				
			default:
				System.out.println("That is not a choice");
				menu(number);
				break;
		}
	}
	
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
			case "0":
				addNewUser();
				break;
			case "1":
				FixTime();
				break;
			case "2":
				debugging = !debugging;
				noder.unPause();
				inputer.unPause();
				break;
			case "3":
				System.out.println(number);
				noder.unPause();
				inputer.unPause();
				break;
			case "4":
				displayCurrentUsers();
				break;
				
			default:
				System.out.println("That is not a choice");
				adminMenu(number);
				break;
		}
	}
	
	private static void checkOut() {
		System.out.println("Checking Out?");
		
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
		if ((number = noder.getNewNumber()) != null) {
			String PIN = users.getPin(number);
			int index = users.findUserByID(PIN);
			if (index != -1) {
				DecimalFormat df11 = new DecimalFormat("###.##");
				double dec1 = Double.parseDouble(df11.format(curTime - users.users.get(index).LastCheckInTime));
				users.users.get(index).TotalHours += dec1;
				users.users.get(index).LastCheckInTime = -1;
				users.saveUsers();
			}
		}
		System.out.println("Time Logged");
		
		noder.unPause();
		inputer.unPause();
	}
	
	private static void FixTime() {
		String number = null;
		if ((number = noder.getNewNumber()) != null) {
			System.out.print("Hours to change? + / -   >>");
			String answer = scan.nextLine();
			
			System.out.println(number);
			String PIN = users.getPin(number);
			System.out.println(PIN);
			int index = users.findUserByID(PIN);
			if (index != -1) {
				users.users.get(index).TotalHours += Double.parseDouble(answer);
				System.out.println("Time Fixed! Press any Button to continue.");
			} else {
				System.out.println("Couldn't Find Card Number In Database!");
			}
			answer = scan.nextLine();
		} else {
			System.out.println("Card Not Scanned");
			FixTime();
		}
		noder.unPause();
		inputer.unPause();
	}
	private static void addNewUser() {
		NodeJS writer = new NodeJS();
		int[] randomPin = new int[8];
		Random generator = new Random(); 
		for (int i = 0; i < randomPin.length; i++) {
			randomPin[i] = generator.nextInt(256);

		}
		String PIN = "";
		for (int i = 0; i < randomPin.length-1; i++) {
			PIN += randomPin[i] + ",";
		}
		PIN += randomPin[randomPin.length-1];
		boolean taken = false;
		for (int i = 0; i < users.users.size(); i++) {
			if (users.users.get(i).getID().equalsIgnoreCase(PIN)) {
				taken = true;
			}
		}
		
		if (!taken) {
			writer.write(PIN);
			
			System.out.print("Name: ");
			String name = scan.nextLine();
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
			if (continueCreation) {
				users.addNewUser(PIN, name);
			
				users.saveUsers();
				System.out.println("Users Saved");
			} else {
				System.out.println("User creation cancelled");
			}
			noder.unPause();
			inputer.unPause();
		}
				
	}
	
	public static void displayCurrentUsers() {
		for (int i = 0; i < users.users.size(); i++) {
			System.out.println(users.users.get(i).toWriter());
		}
		noder.unPause();
		inputer.unPause();
	}
	
	
	public static void printDebug(String print) {
		if (debugging)  {
			System.out.println(print);
		}
	}
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
	
	private static void printCoderDebug(String print) {
		if (coderDebug) {
			System.out.println(print);
		}
	}
}
