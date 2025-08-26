package system;

public class GameState {
	boolean _win;
	int _remainingHelps;
	
	public GameState() {
		_win = false;
		_remainingHelps = 3;
	}
	
	public boolean endGame(ButtonState[][] states) {
		_win = true;
		for (int row = 0; row < states.length; row++) {
			for (int column = 0; column < states.length; column++) {
				if (!states[row][column].verify())
					_win = false;
			}
		}
		return _win;
	}
}
