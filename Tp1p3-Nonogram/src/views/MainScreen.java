package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import system.ButtonState;
import system.GameState;
import system.GridBuilder;
import system.SelectionState;

import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.util.Random;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.border.BevelBorder;

public class MainScreen extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel _grid;
	private JButton[][] _buttonGrid;
	private ButtonState[][] _buttonState;
	private GridBuilder _gridBuilder;
	private JPanel _rowHints;
	private JPanel _columnHints;
	private GameState _gameState;
	private JButton endGameButton;
	private ImageIcon _xIcon;
	private boolean _firstMove;
	private Random _rand;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			Object[] options = { "5x5", "10x10", "15x15", "20x20" };
			String sel = (String) JOptionPane.showInputDialog(null, "Elige dificultad", "Niveles",
					JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
			if (sel == null)
				return;
			int size = 5;
			if (sel.equals("5x5"))
				size = 5;
			else if (sel.equals("10x10"))
				size = 10;
			else if (sel.equals("15x15"))
				size = 15;
			else if (sel.equals("20x20"))
				size = 20;

			MainScreen ms = new MainScreen(size);
			ms.setVisible(true);
		});
	}

/////////////////////////////////////////////////////////////
	/**
	 * Create the frame.
	 */
	public MainScreen(int size) {
		super("Nonogram");
		_buttonGrid = new JButton[size][size];
		_buttonState = new ButtonState[size][size];

		initializeLogic(size);
		initializeVisuals(size);

		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private void initializeIcons() {
		Image reescaledIcon = _xIcon.getImage();
		reescaledIcon = reescaledIcon.getScaledInstance(_buttonGrid[0][0].getWidth(), _buttonGrid[0][0].getHeight(), Image.SCALE_DEFAULT);
		_xIcon = new ImageIcon(reescaledIcon);
		_firstMove = false;
	}

	private void initializeLogic(int size) {
		_rand = new Random();
		_firstMove = true;
		_gridBuilder = new GridBuilder(size);
		_gridBuilder.generateNew();

		boolean[][] temporalGrid = _gridBuilder.getGrid();
		for (int row = 0; row < size; row++) {
			for (int col = 0; col < size; col++) {
				_buttonState[row][col] = new ButtonState(temporalGrid[row][col]);
			}
		}

		_gameState = new GameState();
	}

	private void initializeVisuals(int size) {
		_xIcon = new ImageIcon(MainScreen.class.getResource("/resources/x.png"));
		JPanel content = new JPanel(new BorderLayout(8, 8));
		content.setBorder(new EmptyBorder(10, 10, 10, 10));
		setContentPane(content);

		// --- ROW HINTS ---
		_rowHints = new JPanel(new GridLayout(size, 1, 0, 2));
		_rowHints.setBorder(new BevelBorder(BevelBorder.LOWERED));
		String[] rowHints = _gridBuilder.getRowHints();
		int rowHintWidth = Math.max(50, size * 10);
		_rowHints.setPreferredSize(new Dimension(rowHintWidth, 0));
		for (int r = 0; r < size; r++) {
			JLabel hintTxt = new JLabel();
			hintTxt.setHorizontalAlignment(SwingConstants.CENTER);
			hintTxt.setFont(new Font("Tahoma", Font.PLAIN, Math.max(10, 18 - size / 2)));
			if (!rowHints[r].isEmpty())
				hintTxt.setText(rowHints[r].trim());
			else
				hintTxt.setText("0");
			_rowHints.add(hintTxt);
		}

		// --- COLUMN HINTS ---
		_columnHints = new JPanel(new GridLayout(1, size, 4, 0));
		_columnHints.setBorder(new BevelBorder(BevelBorder.LOWERED));
		String[] columnHints = _gridBuilder.getColumnHints();
		for (int c = 0; c < size; c++) {
			JPanel colPanel = new JPanel();
			colPanel.setLayout(new BoxLayout(colPanel, BoxLayout.Y_AXIS));
			colPanel.add(Box.createVerticalGlue()); // empuja números hacia abajo
			if (!columnHints[c].isEmpty()) {
				String[] nums = columnHints[c].trim().split("\\s+");
				for (String n : nums) {
					JLabel lbl = new JLabel(n);
					lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
					lbl.setFont(new Font("Tahoma", Font.PLAIN, Math.max(10, 18 - size / 2)));
					colPanel.add(lbl);
				}
			} else {
				JLabel lbl = new JLabel("0");
				lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
				lbl.setFont(new Font("Tahoma", Font.PLAIN, Math.max(10, 18 - size / 2)));
				colPanel.add(lbl);
			}
			_columnHints.add(colPanel);
		}

		// --- GRID DE BOTONES ---
		_grid = new JPanel(new GridLayout(size, size, 1, 1));
		_grid.setPreferredSize(new Dimension(Math.min(900, size * 36), Math.min(900, size * 36)));

		for (int row = 0; row < size; row++) {
			for (int col = 0; col < size; col++) {
				JButton button = new JButton("");
				final int r = row, c = col;
				button.setOpaque(true);
				button.setBorderPainted(true);
				button.setContentAreaFilled(true);
				button.setBackground(Color.WHITE);

				button.addActionListener(e -> {
					if (_firstMove)
						initializeIcons();
					_buttonState[r][c].cycleState();
					SelectionState st = _buttonState[r][c].getState();
					button.setIcon(null);
					button.setBackground(new Color(225, 225, 225));
					switch (st) {
					case Selected:
						button.setBackground(new Color(50, 50, 0));
						button.setText("");
						break;
					case Crossed:
						if (_buttonState[r][c].getState() == SelectionState.Crossed) {
							button.setIcon(_xIcon);
						}
						break;
					case Clear:
					default:
						button.setText("");
						break;
					}
				});
				_grid.add(button);
				_buttonGrid[row][col] = button;
			}
		}

		// --- HEADER PANEL ---
		JPanel headerPanel = new JPanel(new BorderLayout());
		JPanel leftFiller = new JPanel();
		leftFiller.setPreferredSize(new Dimension(rowHintWidth, 0));
		headerPanel.add(leftFiller, BorderLayout.WEST);
		headerPanel.add(_columnHints, BorderLayout.CENTER);

		// --- PANEL CENTRAL ---
		JPanel centerPanel = new JPanel(new BorderLayout());
		centerPanel.add(headerPanel, BorderLayout.NORTH);
		centerPanel.add(_rowHints, BorderLayout.WEST);
		centerPanel.add(_grid, BorderLayout.CENTER);

		content.add(centerPanel, BorderLayout.CENTER);

		// --- BOTONES DE CONTROL ---
		JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		endGameButton = new JButton("End");
		endGameButton.addActionListener(e -> {
			for (int r = 0; r < _buttonState.length; r++) {
				for (int c = 0; c < _buttonState[0].length; c++) {
					if (_buttonState[r][c].getState() == SelectionState.Clear) {
						JOptionPane.showMessageDialog(MainScreen.this, "Aún hay celdas sin completar.", "Faltan celdas",
								JOptionPane.INFORMATION_MESSAGE);
						return;
					}
				}
			}
			boolean win = _gameState.allDone(_buttonState);
			if (win)
				JOptionPane.showMessageDialog(MainScreen.this, "¡Ganaste! 🎉", "Resultado",
						JOptionPane.INFORMATION_MESSAGE);
			else {
				JOptionPane.showMessageDialog(MainScreen.this, "Hay " + _gameState.getErrors() +  " errores 😅", "Resultado",
						JOptionPane.WARNING_MESSAGE);
				highlightIncorrectCells();
			}
		});
		JButton help = new JButton("Help");
		help.addActionListener(ev -> JOptionPane.showMessageDialog(MainScreen.this,
				"Click: rellenar\nClick otra vez: marcar X\nClick otra vez: limpiar \nBoton Aid: Define una casilla como correcta", "Help",
				JOptionPane.INFORMATION_MESSAGE));

		controls.add(help);
		controls.add(endGameButton);
		content.add(controls, BorderLayout.SOUTH);
		
		JButton aid = new JButton("Aid");
		
		aid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!_gameState.allDone(_buttonState) && _gameState.getHelps() > 0) {
					_gameState.usedHelp();
					while (true) {
						int rand1 = _rand.nextInt(size);
						int rand2 = _rand.nextInt(size);
						if (!_buttonState[rand1][rand2].verify()) {
							_buttonState[rand1][rand2].help();
							updateButton(rand1,rand2);
							return;
						}
					}
				}
			}
		});
		controls.add(aid);
		
		pack();
	}

	private void highlightIncorrectCells() { // marcar las que están mal
		for (int r = 0; r < _buttonState.length; r++) {
			for (int c = 0; c < _buttonState[0].length; c++) {
				if (!_buttonState[r][c].verify()) {
					_buttonGrid[r][c].setBackground(new Color(255, 180, 180));
				} else {
					if (_buttonState[r][c].getState() == SelectionState.Selected) {
						_buttonGrid[r][c].setBackground(new Color(50, 50, 0));
					} else {
						_buttonGrid[r][c].setBackground(Color.WHITE);
					}
				}
			}
		}
	}
	
	private void updateButton (int row, int col) {
		_buttonGrid[row][col].setText("");
		if (_firstMove)
			initializeIcons();
		SelectionState selec = _buttonState[row][col].getState();
		_buttonGrid[row][col].setBackground(Color.white);
		_buttonGrid[row][col].setIcon(null);
		_buttonGrid[row][col].setText("");
		switch (selec) {
		case Selected:
			_buttonGrid[row][col].setBackground(new Color(50, 50, 0));
			break;
		case Crossed:
			_buttonGrid[row][col].setIcon(_xIcon);
			break;
		case Clear:
			_buttonGrid[row][col].setText("");
			break;
		}
	}

}
