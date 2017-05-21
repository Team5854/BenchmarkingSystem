
public class User {
	String ID = "";
	String Name = "";
	double TotalHours = 0.0;
	double LastCheckInTime = 0.0;
	public User(String fullInfo) {
		String[] info = fullInfo.split("; ");
		ID = info[0];
		Name = info[1];
		TotalHours = Double.parseDouble(info[2]);
		LastCheckInTime = Double.parseDouble(info[3]);
	}
	
	public User(String iD2, String name2) {
		ID = iD2;
		Name = name2;
	}

	public String getID() {
		return ID;
	}
	public String toWriter() {
		return ID + "; " + Name + "; " + TotalHours + "; "+ LastCheckInTime;
	}
}
