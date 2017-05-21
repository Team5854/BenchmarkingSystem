import java.util.Scanner;

public class ScannerThread extends Thread {
	//private String lastPut = "";
	private String input = "";
	private boolean newInput = false;
	boolean pause = false;
	private Scanner scan = new Scanner(System.in);
	public void run() {
		while(true) {
			if (!pause) {
				input = scan.nextLine();
				if (input != null) {
					newInput = true;
				}
			}
		}
	}
	public String getInput() {
		if (pause) {
			return null;
		} else if (newInput) {
			newInput = false;
			return input;
		} else {
			return null;
		}
	}
	
	public void pause() {
		pause = true;
	}
	public void unPause() {
		pause = false;
	}
}
