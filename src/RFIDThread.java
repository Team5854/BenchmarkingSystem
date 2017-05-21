
public class RFIDThread extends Thread {
	NodeJS noder = new NodeJS();
	String number = null;
	boolean pause = false;
	public void run() {
		while (true) { //while running
			if (!pause) { //and not paused
				number = noder.read(); //then read in the ID of card.
			}
		}
	}
	public String getNumber() { //if not paused then return ID
		if (pause) { 
			return null;
		}
		return number;
	}
	public String getNewNumber() { //read in from a not thread.
		if ((number = noder.read()) != null) {
			return number;
		}
		return null;
	}
	public void pause() {//pause thread.
		pause = true;
	}
	public void unPause() { // unpause thread.
		pause = false;
	}

}
