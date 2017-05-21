

/**
 * 
 * @author keegan
 *
 * Class to hold info for each user.
 */
public class User {
	String ID = "";
	String Name = "";
	double TotalHours = 0.0;
	double LastCheckInTime = 0.0;
	
	//create varibale from complete string (read from file and make user)
	public User(String fullInfo) {
		String[] info = fullInfo.split("; ");
		ID = info[0];
		Name = info[1];
		TotalHours = Double.parseDouble(info[2]);
		LastCheckInTime = Double.parseDouble(info[3]);
	}
	
	public User(String iD2, String name2) { //create a user from varibales
		ID = iD2;
		Name = name2;
	}

	public String getID() { //returns the id of user.
		return ID;
	}
	public String toWriter() {//formats the User to print to file.
		return ID + "; " + Name + "; " + TotalHours + "; "+ LastCheckInTime;
	}
}
