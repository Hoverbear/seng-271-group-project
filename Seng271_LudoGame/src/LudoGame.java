import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * This is the main runner class for the Ludo Game. It currently prints the
 * board background image, and adds the pawns to the board.
 * 
 * @author Daniel Faulkner
 */
public class LudoGame extends JPanel {

	// The height and width of the application
	private final static int APPHEIGHT = 540;
	private final static int APPWIDTH = 750;

	// The offsets from the walls of the window
	private final static int BOARDLEFTOFFSET = 5;
	private final static int BOARDTOPOFFSET = 5;

	// The grids on the game board are 48 pixels across
	private final static int GRIDSIZE = 48;
	// There are 11x11 grids (including ones the pawns can't occupy)
	private final static int GRIDNUM = 11;
	// This 2D array holds the top left point of each matching grid spot
	private final static Point[][] THEGRID = new Point[GRIDNUM][GRIDNUM];
	// The grid offsets for putting pawns in the correct home field
	private final static int LEFT = 1, RIGHT = 8;
	private final static int TOP = 1, BOTTOM = 8;

	// The array lists to keep track of the pawns in the game
	private final ArrayList<Pawn> redPawns = new ArrayList<Pawn>();
	private final ArrayList<Pawn> bluePawns = new ArrayList<Pawn>();
	private final ArrayList<Pawn> yellowPawns = new ArrayList<Pawn>();
	private final ArrayList<Pawn> greenPawns = new ArrayList<Pawn>();

	// The swing panel for layered objects
	private final JLayeredPane boardPane;

	private final Die theDie;

	private HomeField redHome;
	private HomeField blueHome;
	private HomeField greenHome;
	private HomeField yellowHome;

	private final ArrayList<GoalField> redGoal = new ArrayList<GoalField>();
	private final ArrayList<GoalField> blueGoal = new ArrayList<GoalField>();
	private final ArrayList<GoalField> yellowGoal = new ArrayList<GoalField>();
	private final ArrayList<GoalField> greenGoal = new ArrayList<GoalField>();

	private final ArrayList<Player> players = new ArrayList<Player>();

	// private final Strategy redStrategy;
	// private final Strategy blueStrategy;
	// private final Strategy greenStrategy;
	// private final Strategy yellowStrategy;
	//
	// private final Player redPlayer;
	// private final Player bluePlayer;
	// private final Player yellowPlayer;
	// private final Player greenPlayer;

	/**
	 * Constructor for the game board. Adds layered images and game pieces.
	 */
	private LudoGame(int numberOfHumans) {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		final ImageIcon boardBackground = createImageIcon("src/game_board.png");
		final ImageIcon redPawnImg = createImageIcon("src/red_pawn.png");
		final ImageIcon bluePawnImg = createImageIcon("src/blue_pawn.png");
		final ImageIcon yellowPawnImg = createImageIcon("src/yellow_pawn.png");
		final ImageIcon greenPawnImg = createImageIcon("src/green_pawn.png");

		setupTheGrid();

		boardPane = new JLayeredPane();
		boardPane.setPreferredSize(new Dimension(APPWIDTH, APPHEIGHT));

		JLabel board = new JLabel(boardBackground);
		boardPane.add(board, new Integer(0));
		Dimension boardSize = board.getPreferredSize();
		board.setBounds(BOARDLEFTOFFSET, BOARDTOPOFFSET, boardSize.width,
				boardSize.height);

		setupTheFields();

		setupThePlayers(numberOfHumans);

		addPawns(redPawnImg, redPawns, redHome);
		addPawns(bluePawnImg, bluePawns, blueHome);
		addPawns(yellowPawnImg, yellowPawns, yellowHome);
		addPawns(greenPawnImg, greenPawns, greenHome);

		// Get an instance of the singleton Die.
		theDie = Die.getInstance();
		// TODO: Add graphics for Die.

		// Finally, boardPane is added to the game frame.
		add(boardPane);
	}

	/**
	 * Start of a test method. Anything that should happen after board setup
	 * goes here.
	 */
	protected void startTheGame() {
		testGame();
	}

	private void testGame() {
		boolean theShowMustGoOn = true;
		while (theShowMustGoOn) {
			for (Player pl : players) {
				System.out.println("Player " + players.indexOf(pl)
						+ " starts turn ...");
				int roll = 0;
				do {
					roll = testRollDie();
					pl.planMove(roll);
					sleep(100);
				} while (roll == 6);
				System.out.println("Turn done!\n");
				if (pl.checkIfGoalFull()) {
					System.err.println("We have a winner!!!");
					theShowMustGoOn = false;
					break;
				}
			}
			if (theShowMustGoOn) {
				System.out.println("Round done! Next round starting...\n");
			}
		}
	}

	private int testRollDie() {
		int playerRoll = theDie.roll();
		System.out.println("Roll: " + playerRoll);
		sleep(500);
		return playerRoll;
	}

	private void sleep(final long milli) {
		try {
			Thread.sleep(milli);
		} catch (InterruptedException ie) {
			System.err
					.println("Unexpected timing error. Aborting thread sleep");
		}
	}

	/**
	 * Sets up the 2D array that holds the 11 x 11 grid imagining of the board
	 * and assigns each location the relevant Point of the top left corner (for
	 * a Point p, p.x and p.y give you the x and y coordinates of that point,
	 * respectively).
	 */
	private void setupTheGrid() {
		for (int i = 0; i < GRIDNUM; i++) {
			for (int j = 0; j < GRIDNUM; j++) {
				THEGRID[i][j] = new Point(BOARDLEFTOFFSET + (i * GRIDSIZE),
						BOARDTOPOFFSET + (j * GRIDSIZE));
			}
		}
	}

	/**
	 * Hideous method that sets up all the Field instances and lines these up
	 * with the appropriate spot on THEGRID, assigning a Point value to each
	 * field and establishing links. If you don't want to cry, try to avoid
	 * looking at this method too much. It may seem perplexing, which is because
	 * it directly channels misery into text and wants to devour your soul. But
	 * don't worry, it works! You can now navigate the track by starting from
	 * any HomeField!
	 */
	private void setupTheFields() {

		final int[] gridJ = { 10, 10, 9, 8, 7, 6, 6, 6, 6, 6, 5, 4, 4, 4, 4, 4,
				3, 2, 1, 0, 0, 0, 1, 2, 3, 4, 4, 4, 4, 4, 5, 6, 6, 6, 6, 6, 7,
				8, 9, 10 };
		final int[] gridI = { 5, 4, 4, 4, 4, 4, 3, 2, 1, 0, 0, 0, 1, 2, 3, 4,
				4, 4, 4, 4, 5, 6, 6, 6, 6, 6, 7, 8, 9, 10, 10, 10, 9, 8, 7, 6,
				6, 6, 6, 6 };
		Field lastField = null;
		BasicField firstField = null;
		for (int i = 0; i < 40; i++) {
			BasicField theTrack = new BasicField(THEGRID[gridI[i]][gridJ[i]]);
			if (i % 10 == 0) {
				if (i == 0) {
					firstField = theTrack;
					int[] goalJ = { 9, 8, 7, 6 };
					int[] goalI = { 5, 5, 5, 5 };
					setupTheGoals(redGoal, goalI, goalJ, theTrack);
				} else if (i == 10) {
					int[] goalJ = { 5, 5, 5, 5 };
					int[] goalI = { 1, 2, 3, 4 };
					setupTheGoals(blueGoal, goalI, goalJ, theTrack);
				} else if (i == 20) {
					int[] goalJ = { 1, 2, 3, 4 };
					int[] goalI = { 5, 5, 5, 5 };
					setupTheGoals(yellowGoal, goalI, goalJ, theTrack);
				} else if (i == 30) {
					int[] goalJ = { 5, 5, 5, 5 };
					int[] goalI = { 9, 8, 7, 6 };
					setupTheGoals(greenGoal, goalI, goalJ, theTrack);
				}
			} else if ((i - 1) % 10 == 0) {
				if (i == 1) {
					int[] homeJ = { 8, 9, 8, 9 };
					int[] homeI = { 1, 1, 2, 2 };
					redHome = setupTheHome(homeI, homeJ, theTrack);
				} else if (i == 11) {
					int[] homeJ = { 1, 1, 2, 2 };
					int[] homeI = { 2, 1, 2, 1 };
					blueHome = setupTheHome(homeI, homeJ, theTrack);
				} else if (i == 21) {
					int[] homeJ = { 2, 1, 2, 1 };
					int[] homeI = { 9, 9, 8, 8 };
					yellowHome = setupTheHome(homeI, homeJ, theTrack);
				} else if (i == 31) {
					int[] homeJ = { 9, 9, 8, 8 };
					int[] homeI = { 8, 9, 8, 9 };
					greenHome = setupTheHome(homeI, homeJ, theTrack);
				}
			}
			if (lastField != null) {
				lastField.setNextField(theTrack);
			}
			lastField = theTrack;
		}
		lastField.setNextField(firstField);
	}

	private void setupTheGoals(final ArrayList<GoalField> theGoal,
			final int[] gridI, final int[] gridJ, final BasicField linker) {
		GoalField lastField = null;
		GoalField currentField = null;
		for (int i = 3; i >= 0; i--) {
			currentField = new GoalField(THEGRID[gridI[i]][gridJ[i]]);
			theGoal.add(currentField);
			if (lastField != null) {
				currentField.setNextGoalField(lastField);
			}
			lastField = currentField;
		}
		linker.setGoalField(currentField);
	}

	private HomeField setupTheHome(final int[] gridI, final int[] gridJ,
			final BasicField entry) {
		final ArrayList<Point> points = new ArrayList<Point>();
		for (int i = 0; i < gridI.length; i++) {
			points.add(THEGRID[gridI[i]][gridJ[i]]);
		}
		HomeField hf = new HomeField(points);
		hf.setNextField(entry);
		return hf;
	}

	private void setupThePlayers(int numberOfHumans) {

		// Set up the GoalFields in order
		ArrayList<ArrayList<GoalField>> goalFields = new ArrayList<ArrayList<GoalField>>(
				Arrays.asList(redGoal, blueGoal, yellowGoal, greenGoal));

		// Same deal with Pawn lists
		ArrayList<ArrayList<Pawn>> pawns = new ArrayList<ArrayList<Pawn>>(
				Arrays.asList(redPawns, bluePawns, yellowPawns, greenPawns));

		// And homeFields
		HomeField[] homeFields = { redHome, blueHome, yellowHome, greenHome };

		// Loop through the four players.
		for (int i = 0; i < 4; i++) {
			// Do we need the next player to be a human?
			if (numberOfHumans > i) {
				players.add(new Player(new HumanStrategy(), goalFields.get(i),
						homeFields[i], pawns.get(i)));
			} else {
				// Choose an AI strategy.
				int choice = (int) (1 + Math.random() * 4);
				Strategy someStrategy;
				switch (choice) {
				case 1:
					someStrategy = new AggressiveStrategy();
					break;
				case 2:
					someStrategy = new DefensiveStrategy();
					break;
				case 3:
					someStrategy = new LonePawnStrategy();
					break;
				case 4:
					someStrategy = new ManyPawnsStrategy();
					break;
				default:
					someStrategy = new ManyPawnsStrategy();
					break;
				}
				// Create the AI.
				players.add(new Player(someStrategy, goalFields.get(i),
						homeFields[i], pawns.get(i)));
			}
		}
		// All the players are initialized now.
	}

	/**
	 * Adds a player's 4 pawns to the game board in the grid spot corresponding
	 * with the home field (once that's implemented).
	 * 
	 * @param imgSrc
	 *            the pawn's image
	 * @param pawnList
	 *            the list to add the pawn to after putting on board
	 * @param homeFieldX
	 *            the home field positioning (LEFT or RIGHT)
	 * @param homeFieldY
	 *            the home field positioning (TOP or BOTTOM)
	 */
	private void addPawns(final ImageIcon imgSrc,
			final ArrayList<Pawn> pawnList, final HomeField home) {
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				// JLabel jl = new JLabel(imgSrc);
				JButton jl = new JButton(imgSrc);
				jl.setBorderPainted(false);
				jl.setContentAreaFilled(false);
				boardPane.add(jl, new Integer(1));
				Dimension size = new Dimension(jl.getIcon().getIconWidth(), jl
						.getIcon().getIconHeight());
				jl.setBounds(0, 0, size.width, size.height);
				Pawn p = new Pawn(jl, home);
				pawnList.add(p);
			}
		}
	}

	/**
	 * Loads the image from the specified path.
	 * 
	 * @param src
	 *            the path of the image file
	 * @return an ImageIcon (or null, if the image is not found)
	 */
	private ImageIcon createImageIcon(final String src) {
		try {
			BufferedImage bluePawn = ImageIO.read(new File(src));
			ImageIcon icon = new ImageIcon(bluePawn);
			return icon;
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return null;
		}
	}

	/**
	 * Creates the general GUI frames and call the LudoGame constructor.
	 */
	private static void createGUI() {
		JFrame frame = new JFrame("Ludo Game");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Prompt for players
		Integer[] possibilities = { 0, 1, 2, 3, 4 };
		int numberOfHumans = (int) JOptionPane.showInputDialog(frame,
				"How many humans will be attempting to out-Ludo the computer?",
				"Player Asker Abouter", JOptionPane.DEFAULT_OPTION, null,
				possibilities, possibilities[0]);

		LudoGame contentPane = new LudoGame(numberOfHumans);
		contentPane.setOpaque(true);
		frame.setContentPane(contentPane);

		frame.pack();
		frame.setVisible(true);

		// And the game is started!
		contentPane.startTheGame();
	}

	/**
	 * The entry point for the program. Kicks things off by creating the GUI.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		createGUI();
	}
}
