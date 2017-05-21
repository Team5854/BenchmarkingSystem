import java.util.Scanner;

public class ScannerThread extends Thread {
	//private String lastPut = "";
	private String input = "";
	private boolean newInput = false;
	boolean pause = false;
	private Scanner scan = new Scanner(System.in);
	
	
	public void run() { //start of new thread.
		while(true) { //while the program is running. or thread is not paused.
			if (!pause) { //if the thread is not paused.
				input = scan.nextLine(); //scan for input and set input to var.
				if (input != null) { //if their is input.
					newInput = true; // say that their is input
				}
			}
		}
	}
	public String getInput() {
		if (pause) { // if not paused
			return null;
		} else if (newInput) { // and the input is not a repeat.
			newInput = false; //set to not repeat input
			return input; // return input to main class.
		} else {
			return null; //if none of that then return null.
		}
	}
	
	public void pause() { //pause sets all variables to not allow continuing of thread.
		pause = true;
	}
	public void unPause() { //allow thread to continue to work.
		pause = false;
	}
}
