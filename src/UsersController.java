import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Calendar;

public class UsersController {
	public ArrayList<User> users = new ArrayList<User>();
	int weekOfMonth = 0;
	int month = 0;
	int year = 0;
	String[] lastDataFile;
	boolean FileDidNotExist = false;
	public UsersController() {
		Calendar calobj = Calendar.getInstance(); // get a calendar
		weekOfMonth = calobj.get(Calendar.WEEK_OF_MONTH);
		month = calobj.get(Calendar.MONTH)+1;
		year = calobj.get(Calendar.YEAR);
		BufferedReader br1 = null;

		try {
			// Create new file
			String path="/home/pi/SavedData/Users"+ weekOfMonth + month + year +".txt";
			File file = new File(path);

			// If file doesn't exists, then create it
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
			    	lastDataFile = line0.split(" ");
			    }			    
		    	br.close();
		    	
			    path = "/home/pi/SavedData/Users"+ lastDataFile[0] + lastDataFile[1] + lastDataFile[2] + ".txt";
			    file = new File(path);
			    

				FileWriter fw = new FileWriter(saverfile.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);

				// Write in file
				bw.write(weekOfMonth + " " + month+ " " + year);

				// Close connection
				bw.close();
			}

			br1 = new BufferedReader(new FileReader(file.getAbsoluteFile()));

			String line = "";
		    while ((line = br1.readLine()) != null) {
		    	System.out.println(line);
		    	User newUser = new User(line);
		        users.add(newUser);
		    }
		    if  (FileDidNotExist) {
		    	for (User u : users) {
		    		u.TotalHours = 0;
		    		u.LastCheckInTime = -1;
		    	}
		    }
		    br1.close();
		    saveUsers();
		} catch(Exception e) {
			
		} 
	}
	
	
	public int findUserByID(String ID) {
		for (int i = 0; i < users.size(); i++) {
			if (users.get(i).getID().equalsIgnoreCase(ID)) {
				return i;
			}
		}
		return -1;
	}
	
	public void addNewUser(String ID, String name) {
		users.add(new User(ID, name));
	}
	
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
