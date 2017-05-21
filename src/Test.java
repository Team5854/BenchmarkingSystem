import java.util.Scanner;

public class Test {
	private static Scanner scan = new Scanner(System.in);
	public static void main(String[] args) {
		while (true) {
			NodeJS noder = new NodeJS();
			String choice = scan.nextLine();
			switch (choice) {
				case "read":
					String[] cardInfo = noder.readFull();
					String type = cardInfo[0];
					String UID = cardInfo[1];
					String MC = cardInfo[2];
					String block = cardInfo[3];
					String data = cardInfo[4];
		
					System.out.println(type);
					System.out.println(UID);
					System.out.println(MC);
					System.out.println(block);
					System.out.println(data);
		
					if (block.equals("false")) {
						System.out.println("Error");
					}	
					break;
			
				case "write":
					System.out.println("Enter 11 numbers seperated by commas.");
					String newPIN = scan.nextLine();
					//String PIN = "0xAA, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00";
					noder.write(newPIN);
					break;
				case "exit":
					System.exit(0);
			}
		}
	}
	

}
