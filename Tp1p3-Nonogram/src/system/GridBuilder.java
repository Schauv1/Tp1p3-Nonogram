package system;

import java.util.Random;

public class GridBuilder {
	private boolean[][] _grid;
	private String[] _columnHints;
	private String[] _rowHints;
	private Random _rand;
	
	public GridBuilder(int size) {
		_grid = new boolean[size][size];
		_rowHints = new String[size];
		_columnHints = new String[size];
	}
	
	public void generateNew() {
		_rand = new Random();
		for (int row = 0; row < _grid.length; row++) {
			String rowHint = "";
			int consecutiveCounter = 0;
			for (int column = 0; column < _grid.length; column++) {
				if (_rand.nextInt(2) == 1) {
					_grid[row][column] = true;
					consecutiveCounter ++;
				}
				else {
					_grid[row][column] = false;
					if (consecutiveCounter != 0) {
						rowHint = rowHint + consecutiveCounter + " ";
						consecutiveCounter = 0;
					}
				}
			}
			if (consecutiveCounter != 0)
				rowHint = rowHint + consecutiveCounter + " ";
			_rowHints[row] = rowHint;
		}
		buildColumnHints();
	}
	
	private void buildColumnHints() {
		for (int column = 0; column < _grid.length; column++) {
			String columnHint = "";
			int consecutiveCounter = 0;
			for (int row = 0; row < _grid.length; row++) {
				if (_grid[row][column] == true)
					consecutiveCounter ++;
				else {
					if (consecutiveCounter != 0) {
						columnHint = columnHint + consecutiveCounter + " ";
						consecutiveCounter = 0;
					}
				}
			}
			if (consecutiveCounter != 0)
				columnHint = columnHint + consecutiveCounter + " ";
			_columnHints[column] = columnHint;
		}
	}

	public boolean[][] getGrid() {
		return _grid.clone();
	}
	
	public String[] getColumnHints() {
		return _columnHints.clone();
	}
	
	public String[] getRowHints() {
		return _rowHints.clone();
	}
}
