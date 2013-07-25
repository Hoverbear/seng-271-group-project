import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
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
	private LudoGame() {
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

		// redHome.addPawns(redPawns);
		// blueHome.addPawns(bluePawns);
		// yellowHome.addPawns(yellowPawns);
		// greenHome.addPawns(greenPawns);

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
		int roll = testRollDie();
		// Checks if any pawns are out.
		if (redHome.isFull() && roll == 6) {
			testMovePawnFromHome(redHome);
			roll = testRollDie();
		}

		// Checks if any pawns are out. Intentional redundancy.
		if (!redHome.isFull()) {
			// If there's a pawn in the goal, see if it can move.
			if (checkIfGoalOccupied(redGoal)) {
				// TODO
			} else {
				testMovePawnNormal(redPawns, redHome, redGoal.get(3), roll);
			}
		}

		// Escape/repeat conditions.
		// if (checkIfGoalFull(redGoal)) {
		if (checkIfGoalOccupied(redGoal)) {
			System.out.println("And you're done!");
		} else {
			System.out.println("Next round!\n");
			sleep(1000);
			testGame();
		}
	}

	private int testRollDie() {
		int playerRoll = theDie.roll();
		System.out.println("Roll: " + playerRoll);
		sleep(500);
		return playerRoll;
	}

	/**
	 * Tester method. Moves a pawn out of its homefield.
	 * 
	 * @param home
	 */
	private void testMovePawnFromHome(final HomeField home) {
		Pawn p = home.getPawn();
		p.moveToField(home.getNextField());
		System.out.println("Moved the pawn to "
				+ home.getNextField().getPoint().toString());
		sleep(1000);
	}

	/**
	 * Tester method. Moves a pawn along the normal fields. First finds an
	 * available pawn, then moves it.
	 * 
	 * @param pawns
	 *            The pawns
	 * @param home
	 *            The homefield
	 * @param goal
	 *            The associated goal
	 * @param distance
	 *            The distance to move
	 */
	private void testMovePawnNormal(final ArrayList<Pawn> pawns,
			final HomeField home, final GoalField goal, final int distance) {
		Pawn thePawn = null;
		for (Pawn p : pawns) {
			if (p.getField() != home) {
				thePawn = p;
				break;
			}
		}
		if (thePawn != null) {
			testMovePawnSpaces(thePawn, (BasicField) thePawn.getField(), goal,
					distance);
		} else {
			System.err.println("Unexpected error. Missing pawn!");
		}
		sleep(1000);
	}

	/**
	 * Tester method. Moves a pawn along the normal fields. Checks for matching
	 * goal fields. Once distance left to travel is zero, remains at field.
	 * 
	 * @param pawn
	 * @param field
	 * @param goal
	 * @param distance
	 */
	private void testMovePawnSpaces(final Pawn pawn, final BasicField field,
			final GoalField goal, final int distance) {
		if (distance == 0) {
			pawn.moveToField(field);
			System.out.println("Moved the pawn to "
					+ field.getPoint().toString());
		} else {
			if (field.hasGoalField()) {
				if (field.getGoalField() == goal) {
					testMovePawnGoal(pawn, goal, distance - 1);
				} else {
					System.out
							.println("Noticed a goal field ... failed to be interested");
					testMovePawnSpaces(pawn, (BasicField) field.getNextField(),
							goal, distance - 1);
				}
			} else {
				testMovePawnSpaces(pawn, (BasicField) field.getNextField(),
						goal, distance - 1);
			}
		}
	}

	/**
	 * Tester method. Moves a pawn along the goal fields.
	 * 
	 * @param pawn
	 * @param goal
	 * @param distance
	 */
	private boolean testMovePawnGoal(final Pawn pawn, final GoalField goal,
			final int distance) {
		if (distance == 0) {
			pawn.moveToField(goal);
			System.out.println("Moved the pawn to goal! At "
					+ goal.getPoint().toString());
			return true;
		} else if (!goal.hasNextField()) {
			System.err
					.println("Oops, invalid move attempted! Goal runway is too short.");
			return false;
		} else {
			return testMovePawnGoal(pawn, (GoalField) goal.getNextField(),
					distance - 1);
		}
	}

	private boolean checkIfGoalFull(final ArrayList<GoalField> goal) {
		boolean isFull = true;
		for (GoalField g : goal) {
			isFull &= g.hasPawn();
		}
		return isFull;
	}

	private boolean checkIfGoalOccupied(final ArrayList<GoalField> goal) {
		boolean hasPawn = false;
		for (GoalField g : goal) {
			hasPawn |= g.hasPawn();
		}
		return hasPawn;
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
					System.out.println(firstField.getPoint().toString());
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
					int[] goalJ = { 9, 8, 7, 6 };
					int[] goalI = { 5, 5, 5, 5 };
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
				JLabel jl = new JLabel(imgSrc);
				boardPane.add(jl, new Integer(1));
				Dimension size = jl.getPreferredSize();
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
		Integer[] possibilities = { 1, 2, 3, 4 };
		int result = (int) JOptionPane
				.showInputDialog(
						frame,
						"Please choose the number of players who may be playing this game.",
						"Player Asker Abouter", JOptionPane.DEFAULT_OPTION,
						null, possibilities, possibilities[0]);

		// TODO: Pass in number of humans!
		LudoGame contentPane = new LudoGame();
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
