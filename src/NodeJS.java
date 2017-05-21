import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 
 *
 * @author Keegan
 *
 * This class deals with launching the NodeJS code from the pi and returning the input. 
 */
public class NodeJS {
	private static String FRCGlitchTeamID = "0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0";

	public NodeJS() {
	}
	
	/*
	 * returns just the ID number
	 */
	public String read() {
		//System.out.println("scan");
		String command = "node node_modules/mfrc522-rpi/test/read.js"; // command to launch reading of Id.
		
		int lineNum = 0;
		String[] cardInfo = new String[11];
		cardInfo[3] = "data : null";
		//read in RFID info until input equals finished.
		try {
			//Runtime.getRuntime().exec("cd node_modules/mfrc522-rpi/test");
		    Process process = Runtime.getRuntime().exec(command);
		 
		    BufferedReader reader = new BufferedReader(
		            new InputStreamReader(process.getInputStream()));
		    String line;
		    while (!"finsihed".equalsIgnoreCase(line = reader.readLine()) && lineNum < 10) {
		        cardInfo[lineNum] = line;
		        lineNum++;
		    }
		 
		    reader.close();
	        //System.out.println("finished Reading");

		} catch (IOException e) {
		    e.printStackTrace();
		}
		//split the string then add that data correctly into return array
		String data = null;
		if (cardInfo[3] != null) {
			String[] dataString = cardInfo[3].split(": ");
			if (dataString != null && dataString.length > 2) {
				data = cardInfo[3].split(": ")[2];
			}
		}
		String returner = data;
		return returner;
	}
	
	
	/*
	 * returns all info on the card.
	 */
	public String[] readFull() {
		String command = "node node_modules/mfrc522-rpi/test/read.js"; 
		int lineNum = 0;
		String[] cardInfo = new String[10];
		//read card info
		try {
			//Runtime.getRuntime().exec("cd node_modules/mfrc522-rpi/test");
		    Process process = Runtime.getRuntime().exec(command);
		 
		    BufferedReader reader = new BufferedReader(
		            new InputStreamReader(process.getInputStream()));
		    String line;
		    while (!(line = reader.readLine()).equalsIgnoreCase("finsihed")) {
		        cardInfo[lineNum] = line;
		        lineNum++;
		    }
		 
		    reader.close();
	        System.out.println("finished Reading");

		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		//split info recieved and add it to the correct var.
		String type = cardInfo[0].split(": ")[1];
		String UID = cardInfo[1].split(": ")[1];
		String MC = cardInfo[2].split(": ")[1];
		String block = cardInfo[3].split(": ")[1].split(" ")[0];
		String data = cardInfo[3].split(": ")[2];
		
		//add variables to an array to be returned.
		String[] returner = {type, UID, MC, block, data};
		return returner;
	}
	
	
	/*
	 * writes to the RFID card
	 */
	public void write(String PIN) {
		
		String key = FRCGlitchTeamID + ","+ PIN;
		String command = "node node_modules/mfrc522-rpi/test/write.js " + key;
		
		try {
		   Process process = Runtime.getRuntime().exec(command);
		   BufferedReader reader = new BufferedReader(
		            new InputStreamReader(process.getInputStream()));
		    String line;
		    while (!(line = reader.readLine()).equalsIgnoreCase("finished")) {
		        System.out.println(line);
		    }
		 
		    reader.close();
		}catch(Exception e) {}
	}
}
