package p2048;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.UIManager;

/**
 * The main window being displayed.
 * 
 * @author Satia
 */
@SuppressWarnings("serial")
public class MainWindow extends JFrame {

	/** ActionMap key for up key pressed. */
	public static final String UP_AM_KEY = "upPressed";
	/** ActionMap key for down key pressed. */
	public static final String DOWN_AM_KEY = "downPressed";
	/** ActionMap key for right key pressed. */
	public static final String RIGHT_AM_KEY = "rightPressed";
	/** ActionMap key for left key pressed. */
	public static final String LEFT_AM_KEY = "leftPressed";

	/**
	 * Program entry point.
	 * 
	 * @param args
	 *            ignored
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		new MainWindow();
	}

	/**
	 * Action performed when UP is pressed.
	 */
	private Action upKeyPressedAction = new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
			score += Logic2048.upArrowPressed(numbers);
			update();
		}
	};

	/**
	 * Action performed when DOWN is pressed.
	 */
	private Action downKeyPressedAction = new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
			score += Logic2048.downArrowPressed(numbers);
			update();
		}
	};

	/**
	 * Action performed when RIGHT is pressed.
	 */
	private Action rightKeyPressedAction = new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
			score += Logic2048.rightArrowPressed(numbers);
			update();
		}
	};

	/**
	 * Action performed when LEFT is pressed.
	 */
	private Action leftKeyPressedAction = new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
			score += Logic2048.leftArrowPressed(numbers);
			update();
		}
	};

	/**
	 * The numbers used during the game.
	 */
	private int[][] numbers;

	/**
	 * The current score;
	 */
	private int score;

	/**
	 * The panel drawing the numbers.
	 */
	private MainPanel panel;

	/**
	 * The statusBar.
	 */
	private JTextField statusBar;

	/**
	 * Initialize the main window.
	 */
	public MainWindow() {
		super("2048");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		Container contentPane = getContentPane();

		createMenuBar();

		// Fill content pane
		contentPane.setLayout(new BorderLayout());
		contentPane.add(panel = new MainPanel(), BorderLayout.NORTH);
		contentPane.add(statusBar = new JTextField(), BorderLayout.SOUTH);

		createKeyBindings();

		// Arrange, center, show
		pack();
		setLocationRelativeTo(null);
		setVisible(true);

		// Initialize GUI
		panel.initializeGui();
	}

	/**
	 * Creates the manu bar.
	 */
	private void createMenuBar() {
		JMenuBar menubar = new JMenuBar();
		setJMenuBar(menubar);

		JMenu menu;
		JMenuItem entry;

		menu = new JMenu("Game");

		entry = new JMenuItem("New Game");
		entry.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newGame();
			}
		});
		menu.add(entry);

		entry = new JMenuItem("Exit");
		entry.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		menu.add(entry);
		menubar.add(menu);
	}

	/**
	 * Creates key bindings to be added and removed later.
	 */
	private void createKeyBindings() {
		panel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0),
				UP_AM_KEY);
		panel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0),
				DOWN_AM_KEY);
		panel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0),
				RIGHT_AM_KEY);
		panel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0),
				LEFT_AM_KEY);
	}

	/**
	 * Adds the key bindings.
	 */
	private void addKeyBindings() {
		panel.getActionMap().put(UP_AM_KEY, upKeyPressedAction);
		panel.getActionMap().put(DOWN_AM_KEY, downKeyPressedAction);
		panel.getActionMap().put(RIGHT_AM_KEY, rightKeyPressedAction);
		panel.getActionMap().put(LEFT_AM_KEY, leftKeyPressedAction);
	}

	/**
	 * Removes the key bindings.
	 */
	private void removeKeyBindings() {
		panel.getActionMap().remove(UP_AM_KEY);
		panel.getActionMap().remove(DOWN_AM_KEY);
		panel.getActionMap().remove(RIGHT_AM_KEY);
		panel.getActionMap().remove(LEFT_AM_KEY);
	}

	/**
	 * Updates the visual representation of the numbers. Also checks if the game
	 * is over and acts correspondingly in that case
	 */
	private void update() {
		panel.paintNumbers(numbers);
		statusBar.setText("Score: " + score);
		if (Logic2048.isGameOver(numbers)) {
			gameOver();
		}
	}

	/**
	 * Starts a new game.
	 */
	private void newGame() {
		numbers = Logic2048.newGame();
		score = 0;
		update();
		addKeyBindings();
	}

	/**
	 * Ends the current game.
	 */
	private void gameOver() {
		// Remove Key Bindings
		removeKeyBindings();
		// Show Popup
		JOptionPane.showMessageDialog(null, "Score: " + score, "Game over",
				JOptionPane.PLAIN_MESSAGE);
	}
}
