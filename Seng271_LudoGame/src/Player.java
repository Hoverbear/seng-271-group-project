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
	 * 
	 */
	public final void planMove(final int dieRoll) {
		// TODO The Strategy should take care of this
		if (homeField.isFull() && dieRoll == 6) {
			movePawnFromHome();
		} else if (!homeField.isFull()) {
			if (checkIfGoalOccupied()) {
				// TODO
			} else {
				movePawnNormal(dieRoll);
			}
		}

		if (checkIfGoalOccupied()) {
			System.out.println("This player is done!\n");
		} else {
			System.out.println("Turn done!\n");
		}
	}

	/**
	 * 
	 */
	public void doMove() {

	}

	/**
	 * Moves a pawn out of its homefield.
	 */
	private void movePawnFromHome() {
		Pawn p = homeField.getPawn();
		p.moveToField(homeField.getNextField());
		System.out.println("Moved the pawn to "
				+ homeField.getNextField().getPoint().toString());
		sleep(50);
	}

	/**
	 * Moves a pawn along the normal fields. First finds an available pawn, then
	 * moves it.
	 * 
	 * @param distance
	 *            The distance to move
	 */
	private void movePawnNormal(final int distance) {
		Pawn thePawn = null;
		for (Pawn p : pawns) {
			if (p.getField() != homeField) {
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
	private void movePawnSpaces(final Pawn pawn, final BasicField field,
			final int distance) {
		if (distance == 0) {
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
			pawn.moveToField(goal);
			System.out.println("Moved the pawn to goal! At "
					+ goal.getPoint().toString());
			return true;
		} else if (!goal.hasNextField()) {
			System.err
					.println("Oops, invalid move attempted! Goal runway is too short");
			return false;
		} else {
			return movePawnGoal(pawn, (GoalField) goal.getNextField(),
					distance - 1);
		}
	}

	private boolean checkIfGoalFull() {
		boolean isFull = true;
		for (GoalField g : goalField) {
			isFull &= g.hasPawn();
		}
		return isFull;
	}

	private boolean checkIfGoalOccupied() {
		boolean hasPawn = false;
		for (GoalField g : goalField) {
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

}
