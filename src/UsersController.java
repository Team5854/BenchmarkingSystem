import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Calendar;


/**
 * 
 * @author keegan
 *
 *
 *	does user controlling.
 */
public class UsersController {
	public ArrayList<User> users = new ArrayList<User>(); //scalable array of all users.
	int weekOfMonth = 0;
	int month = 0;
	int year = 0;
	String[] lastDataFile;
	boolean FileDidNotExist = false;
	public UsersController() {
		
		//instance of week, month, years for user file saving each week.
		Calendar calobj = Calendar.getInstance(); // get a calendar
		weekOfMonth = calobj.get(Calendar.WEEK_OF_MONTH);
		month = calobj.get(Calendar.MONTH)+1;
		year = calobj.get(Calendar.YEAR);
		BufferedReader br1 = null;

		try {
			// Create new file
			String path="/home/pi/SavedData/Users"+ weekOfMonth + month + year +".txt";
			File file = new File(path);

			/**
			 * If the file does not exist then it means it will be empty with no users. this will get the last file
			 * that contained users and then read them all and then write them with logged out users and no hours.
			 * then write into the file that saves last weeks file name the current file for next week.
			 */
			if (!file.exists()) {
				FileDidNotExist = true;
				file.createNewFile();
				
				String pathsaver="/home/pi/SavedData/UsersSaver.txt";
				File saverfile = new File(pathsaver);

				// If file doesn't exists, then create it
				if (!saverfile.exists()) {		
					saverfile.createNewFile();
				}
				BufferedReader br = new BufferedReader(new FileReader(saverfile.getAbsoluteFile()));

				String line0 = "";
			    while ((line0 = br.readLine()) != null) {
			    	lastDataFile = line0.split(" ");// get data
			    }			    
		    	br.close();
		    
		    	//change the path to be last weeks file
			    path = "/home/pi/SavedData/Users"+ lastDataFile[0] + lastDataFile[1] + lastDataFile[2] + ".txt";
			    file = new File(path);
			    

				FileWriter fw = new FileWriter(saverfile.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);

				// Write in file
				bw.write(weekOfMonth + " " + month+ " " + year);

				// Close connection
				bw.close();
			}
			//if file exists then read from this weeks file, if file did not exist read from last weeks file
			br1 = new BufferedReader(new FileReader(file.getAbsoluteFile()));

			String line = "";
		    while ((line = br1.readLine()) != null) {
		    	System.out.println(line);
		    	User newUser = new User(line);
		        users.add(newUser);
		    }
		    
		    //if the file from this week did not exist then reset all users to be default for a clean start for the week.
		    if  (FileDidNotExist) {
		    	for (User u : users) {
		    		u.TotalHours = 0;
		    		u.LastCheckInTime = -1;
		    	}
		    }
		    br1.close();
		    saveUsers();// then save all users to the file
		} catch(Exception e) {
			
		} 
	}
	
	
	public int findUserByID(String ID) { //this finds the user by Pin.
		for (int i = 0; i < users.size(); i++) {
			if (users.get(i).getID().equalsIgnoreCase(ID)) {
				return i;
			}
		}
		return -1;
	}
	
	public void addNewUser(String ID, String name) { //adds new user by id and name
		users.add(new User(ID, name));
	}
	
	/**
	 * (process of writing to file self explanitor already)
	 * saves all users in the system to the current weeks file.
	 */
	public void saveUsers() { 
		try {
			// Create new file
			String path="/home/pi/SavedData/Users"+ weekOfMonth + month + year +".txt";
			File file = new File(path);

			// If file doesn't exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			// Write in file
			for (int i = 0; i < users.size(); i++) {
				bw.write(users.get(i).toWriter()+"\n");
			}

			// Close connection
			bw.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * gets the pin of the user. seperates it from the glitch id.
	 */
	public String getPin(String number) {
		String[] inbetween = number.split(",");
		String PIN = "";
		for (int i = 8; i < inbetween.length-1; i++) {
			PIN += inbetween[i] + ",";
		}
		PIN += inbetween[inbetween.length-1];
		return PIN;
	}
}
