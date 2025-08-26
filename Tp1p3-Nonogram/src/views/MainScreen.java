package views;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import system.ButtonState;
import system.GameState;
import system.GridBuilder;
import system.SelectionState;

import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.border.BevelBorder;

public class MainScreen extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel _frame;
	private JPanel _grid;
	private JButton[][] _buttonGrid;
	private ButtonState[][] _buttonState;
	private GridBuilder _gridBuilder;
	private JPanel _rowHints;
	private JPanel _columnHints;
	private GameState _gameState;
	private JButton endGameButton;
	private JButton btnNewButton;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainScreen frame = new MainScreen();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainScreen() {
		setTitle("Nonogram");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		_frame = new JPanel();
		_frame.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(_frame);
		_frame.setLayout(null);
		
		_buttonGrid = new JButton[5][5];
		initializeLogic();
		initializeVisuals();
	}
	
	private void initializeLogic() {
		_gridBuilder = new GridBuilder(5);
		_gridBuilder.generateNew();
		_buttonState = new ButtonState[5][5];
		boolean[][] temporalGrid = _gridBuilder.getGrid();
		for(int row = 0; row < temporalGrid.length; row++) {
			for(int column = 0; column < temporalGrid[0].length; column++) {
				_buttonState[row][column] = new ButtonState(temporalGrid[row][column]);
			}	
		}
		_gameState = new GameState();
	}
	
	private void initializeVisuals() {		
		_rowHints = new JPanel();
		_rowHints.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		_rowHints.setBounds(0, 49, 50, 184);
		_frame.add(_rowHints);
		_rowHints.setLayout(new GridLayout(5, 0, 0, 0));
		
		String[] rHintsTemp = _gridBuilder.getRowHints(); 
		for (int y = 0; y < _buttonGrid.length; y++) {
			JLabel hintTxt = new JLabel();
			if (!rHintsTemp[y].isEmpty())
				hintTxt.setText(rHintsTemp[y]);
			else
				hintTxt.setText("0");
			hintTxt.setHorizontalAlignment(SwingConstants.CENTER);
			_rowHints.add(hintTxt);
		}
		
		_columnHints = new JPanel();
		_columnHints.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		_columnHints.setBounds(47, 11, 331, 37);
		_frame.add(_columnHints);
		_columnHints.setLayout(new GridLayout(0, 5, 0, 0));
		
		rHintsTemp = _gridBuilder.getColumnHints(); 
		for (int x = 0; x < _buttonGrid.length; x++) {
			JLabel hintTxt = new JLabel();
			if (!rHintsTemp[x].isEmpty())
				hintTxt.setText(rHintsTemp[x]);
			else
				hintTxt.setText("0");
			hintTxt.setHorizontalAlignment(SwingConstants.CENTER);
			_columnHints.add(hintTxt);
		}
		
		_grid = new JPanel();
		_grid.setBounds(47, 49, 331, 184);
		_frame.add(_grid);
		_grid.setLayout(new GridLayout(5, 5, 0, 0));
		
		endGameButton = new JButton("End");
		endGameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_gameState.endGame(_buttonState);
			}
		});
		endGameButton.setBounds(377, 59, 57, 23);
		_frame.add(endGameButton);
		
		btnNewButton = new JButton("Help");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		btnNewButton.setBounds(380, 185, 54, 23);
		_frame.add(btnNewButton);
		
		for(int row = 0; row < _buttonGrid.length; row++) {
			for(int column = 0; column < _buttonGrid[0].length; column++) {
			JButton button = new JButton("");
			int r = row;
			int c = column;
			button.setFont(new Font("Tahoma", Font.PLAIN, 37));
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					_buttonState[r][c].cycleState();
					if (_buttonState[r][c].getState() == SelectionState.Selected)
						button.setBackground(new Color(50, 50, 0));
					else {
						button.setBackground(new Color(255, 255, 255));
						if (_buttonState[r][c].getState() == SelectionState.Crossed) {
							button.setText("X");
						}
						if (_buttonState[r][c].getState() == SelectionState.Clear) {
							button.setText("");
						}
					}
				}
			});
			button.setBackground(new Color(255,255,255));
			_grid.add(button);
			_buttonGrid[row][column] = button;
			}
		}
	}
}
