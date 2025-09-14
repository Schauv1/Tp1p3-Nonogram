package system;

public class GameState {
	private boolean _win;
	private int _remainingHelps;
	private int _errors;
	
	public GameState() {
		_win = false;
		_remainingHelps = 3;
	}
	
	public boolean allDone(ButtonState[][] states) {
		_win = true;
		for (int row = 0; row < states.length; row++) {
			for (int column = 0; column < states.length; column++) {
				if (!states[row][column].verify())
					_win = false;
				if (!states[row][column].correctlyChosen())
					_errors++;
			}
		}
		return _win;
	}
	
	public int getHelps() {
		return _remainingHelps;
	}
	
	public void usedHelp() {
		_remainingHelps--;
	}
	
	public int getErrors() {
		return _errors;
	}
}
