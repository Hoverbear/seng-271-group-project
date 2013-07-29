import java.util.ArrayList;

/* 
 * 
 */
public class Player {
	private final Strategy strategy;
	private final ArrayList<GoalField> goalField;
	private final HomeField homeField;
	// Should be a set of 4.
	private final ArrayList<Pawn> pawns;

	/**
	 * 
	 * @param strategy
	 *            The strategy used by the player.
	 * @param goalField
	 *            The player's goal field. This should be determined by the game
	 *            itself.
	 * @param homeField
	 *            The player's home field. This should be determined by the game
	 *            itself.
	 * @param pawns
	 *            The set of the players pawns. These should be created by the
	 *            Ludo game initialization.
	 */
	public Player(final Strategy strategy,
			final ArrayList<GoalField> goalField, final HomeField homeField,
			final ArrayList<Pawn> pawns) {
		this.strategy = strategy;
		this.goalField = goalField;
		this.homeField = homeField;
		this.pawns = pawns;
	}

	/**
	 * Calls on the strategy to perform a move.
	 */
	public final void doMove(final int dieRoll) {
		// Won players skip their turn.
		if (getGoalOccupiedCount() == 4) {
			return;
		}

		// Locational variables. - TODO: THESE SHOULD ALREADY BE AVAILABLE
		HomeField theHome = getHomeField();
		GoalField theGoal = getEntryGoalField();
		ArrayList<Pawn> thePawns = getPawns();
		// Opportune move possibilities.
		Pawn canScore = canScore(dieRoll); // Store a pawn or null in each, as
											// to represent a move or not.
		Pawn canKnock = canKnock(dieRoll);
		boolean canAddNew = false;
		if (dieRoll == 6 && theHome.hasPawn()) {
			canAddNew = true;
		}
		// Choose a move. If it doesn't find a "opportune" move, it just picks
		// something else.
		Pawn move = strategy.chooseMove(canScore, canKnock, canAddNew, this);
		// We have our chosen move, now move the pawn!
		takeMove(move);
		return;
	}

	private void takeMove(Pawn move) {
		// TODO Auto-generated method stub

	}

	private Pawn canKnock(int dieRoll) {
		// TODO Auto-generated method stub
		return null;
	}

	private Pawn canScore(int dieRoll) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * Preserved temporarily in case needed. Will remove with next commit.
	 * -Daniel
	 */
	private void testMove(final int dieRoll) {
		// TODO The Strategy should take care of this
		if ((homeField.getPawnCount() + getGoalOccupiedCount() == 4)
				&& dieRoll == 6) {
			if (homeField.hasPawn()) {
				movePawnFromHome();
			} else {
				System.err
						.println("This player already won ... why must the game go on?");
			}
		} else if (!(homeField.getPawnCount() + getGoalOccupiedCount() == 4)) {
			if (checkIfGoalFull()) {
				// TODO
			} else {
				movePawnNormal(dieRoll);
			}
		}

		if (checkIfGoalFull()) {
			System.out.println("This player is done!\n");
		} else {
			//
		}
	}

	/**
	 * 
	 * @return The home field.
	 */
	public final HomeField getHomeField() {
		return homeField;
	}

	/**
	 * 
	 * @return The goal field.
	 */
	public final GoalField getEntryGoalField() {
		// The end of the GoalField array is where the entry point is.
		return goalField.get(goalField.size() - 1);
	}

	public final ArrayList<Pawn> getPawns() {
		return pawns;
	}

	/**
	 * Moves a pawn out of its homefield.
	 */
	public Pawn movePawnFromHome() {
		if (checkValidMove(homeField.getNextField())) {
			Pawn p = homeField.getPawn();
			p.moveToField(homeField.getNextField());
			System.out.println("Moved the pawn to "
					+ homeField.getNextField().getPoint().toString());
			sleep(50);
			return p;
		}
		sleep(50);
		return null;
	}

	/**
	 * Moves a pawn along the normal fields. First finds an available pawn, then
	 * moves it.
	 * 
	 * @param distance
	 *            The distance to move
	 */
	public void movePawnNormal(final int distance) {
		Pawn thePawn = null;
		for (Pawn p : pawns) {
			if (p.isAtBasic()) {
				thePawn = p;
				break;
			}
		}
		if (thePawn != null) {
			movePawnSpaces(thePawn, (BasicField) thePawn.getField(), distance);
		} else {
			System.err.println("Unexpected error. Missing pawn!");
		}
		sleep(50);
	}

	/**
	 * Moves a pawn along the normal fields, checking for matching goal fields.
	 * Once distance left to travel is zero, settles on field to move to.
	 * 
	 * @param pawn
	 * @param field
	 * @param distance
	 */
	public void movePawnSpaces(final Pawn pawn, final BasicField field,
			final int distance) {
		if (distance == 0 && checkValidMove(field)) {
			pawn.moveToField(field);
			System.out.println("Moved the pawn to "
					+ field.getPoint().toString());
		} else {
			if (field.hasGoalField()) {
				if (field.getGoalField() == goalField.get(3)) {
					movePawnGoal(pawn, goalField.get(3), distance - 1);
				} else {
					System.out
							.println("Noticed a goal field ... failed to be interested");
					movePawnSpaces(pawn, (BasicField) field.getNextField(),
							distance - 1);
				}
			} else {
				movePawnSpaces(pawn, (BasicField) field.getNextField(),
						distance - 1);
			}
		}
	}

	/**
	 * Moves a pawn along the goal fields.
	 * 
	 * @param pawn
	 * @param goal
	 * @param distance
	 * @return
	 */
	private boolean movePawnGoal(final Pawn pawn, final GoalField goal,
			final int distance) {
		if (distance == 0) {
			if (checkValidMove(goal)) {
				pawn.moveToField(goal);
				System.out.println("Moved the pawn to goal! At "
						+ goal.getPoint().toString());
				return true;
			} else {
				return false;
			}
		} else if (!goal.hasNextField()) {
			System.err
					.println("Oops, invalid move attempted! Goal runway is too short");
			return false;
		} else {
			return movePawnGoal(pawn, (GoalField) goal.getNextField(),
					distance - 1);
		}
	}

	/**
	 * 
	 * @return True if the player's goal fields are full.
	 */
	public boolean checkIfGoalFull() {
		boolean isFull = true;
		for (GoalField g : goalField) {
			isFull &= g.hasPawn();
		}
		return isFull;
	}

	/**
	 * 
	 * @return This returns true if one or more goal field has a pawn.
	 */
	public boolean checkIfGoalOccupied() {
		boolean hasPawn = false;
		for (GoalField g : goalField) {
			hasPawn |= g.hasPawn();
		}
		return hasPawn;
	}

	/**
	 * 
	 * @return The number of pawns occupying the goal.
	 */
	public int getGoalOccupiedCount() {
		int numPawns = 0;
		for (GoalField g : goalField) {
			if (g.hasPawn()) {
				numPawns++;
			}
		}
		return numPawns;
	}

	/**
	 * 
	 * @param field
	 *            The field that the player wishes to move the pawn to.
	 * @return True if the move is valid.
	 */
	private boolean checkValidMove(final Field field) {
		if (field.hasPawn()) {
			if (isOwnPawn(field.getPawn())) {
				System.err
						.println("Oops, invalid move attempted! Own pawn at field location");
				return false;
			} else {
				return true;
			}
		} else {
			return true;
		}
	}

	/**
	 * 
	 * @param foundPawn
	 *            The pawn the player wishes to check.
	 * @return True if the foundPawn is the player's own pawn.
	 */
	private boolean isOwnPawn(final Pawn foundPawn) {
		boolean ownPawn = false;
		for (Pawn p : pawns) {
			ownPawn |= (p == foundPawn);
		}
		return ownPawn;
	}

	/**
	 * 
	 * @param milli
	 *            The amount of milliseconds to sleep.
	 */
	private void sleep(final long milli) {
		try {
			Thread.sleep(milli);
		} catch (InterruptedException ie) {
			System.err
					.println("Unexpected timing error. Aborting thread sleep");
		}
	}

}
