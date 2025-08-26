package system;

public class ButtonState {
	private SelectionState _state;
	private SelectionState _correctState;
	
	public ButtonState(boolean correctState) {
		_state = SelectionState.Clear;
		if (correctState == true)
			_correctState = SelectionState.Selected;
		else
			_correctState = SelectionState.Crossed;
	}

	private void select() {
		_state = SelectionState.Selected;
	}

	private void cross() {
		_state = SelectionState.Crossed;
	}

	private void clearState() {
		_state = SelectionState.Clear;
	}

	public void cycleState() {
		switch(_state) {
		case Clear:
			select();
			break;
		case Crossed:
			clearState();
			break;
		case Selected:
			cross();
			break;
		}
	}
	
	public SelectionState getState() {
		return _state;
	}
	
	public boolean verify() {
		return _state == _correctState;
	}
}
