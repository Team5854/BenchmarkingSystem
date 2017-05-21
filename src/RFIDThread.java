
public class RFIDThread extends Thread {
	NodeJS noder = new NodeJS();
	String number = null;
	boolean pause = false;
	public void run() {
		while (true) {
			if (!pause) {
				number = noder.read();
			}
		}
	}
	public String getNumber() {
		if (pause) {
			return null;
		}
		return number;
	}
	public String getNewNumber() {
		if ((number = noder.read()) != null) {
			return number;
		}
		return null;
	}
	public void pause() {
		pause = true;
	}
	public void unPause() {
		pause = false;
	}

}
