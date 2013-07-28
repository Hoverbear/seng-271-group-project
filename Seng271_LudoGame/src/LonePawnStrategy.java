import java.util.ArrayList;

/** 
 *
 */
public class LonePawnStrategy implements Strategy {

	private HomeField theHome;
	private GoalField theGoal;
	private ArrayList<Pawn> thePawns;

	/*
	 * (non-Javadoc)
	 * 
	 * @see Strategy#doMove()
	 */
	@Override
	public Pawn doMove(final Player player, final int dieRoll) {
		theHome = player.getHomeField();
		theGoal = player.getEntryGoalField();
		thePawns = player.getPawns();

		if ((theHome.getPawnCount() + player.getGoalOccupiedCount() == 4)
				&& dieRoll == 6) {
			if (theHome.hasPawn()) {
				movePawnFromHome();
			} else {
				System.err.println("A player who won is trying to move...");
			}
		} else if (!(theHome.getPawnCount() + player.getGoalOccupiedCount() == 4)) {
			if (player.checkIfGoalFull()) {
				// TODO
			} else {
				movePawnNormal(dieRoll);
			}
		}

		if (player.checkIfGoalFull()) {
			System.out.println("This player is done!\n");
		} else {
			//
		}

		return null;
	}

	/**
	 * Moves a pawn out of its homefield.
	 */
	private void movePawnFromHome() {
		if (checkValidMove(theHome.getNextField())) {
			Pawn p = theHome.getPawn();
			p.moveToField(theHome.getNextField());
			System.out.println("Moved the pawn to "
					+ theHome.getNextField().getPoint().toString());
		}
		sleep(50);
	}

	/**
	 * Prepares a pawn to move along the normal fields. First finds an available
	 * pawn, then moves it.
	 * 
	 * @param distance
	 *            The distance to move
	 */
	private void movePawnNormal(final int distance) {
		Pawn thePawn = null;
		for (Pawn p : thePawns) {
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
	private void movePawnSpaces(final Pawn pawn, final BasicField field,
			final int distance) {
		if (distance == 0 && checkValidMove(field)) {
			pawn.moveToField(field);
			System.out.println("Moved the pawn to "
					+ field.getPoint().toString());
		} else {
			if (field.hasGoalField()) {
				if (field.getGoalField() == theGoal) {
					movePawnGoal(pawn, theGoal, distance - 1);
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
	 * Prevents landing on a Pawn of the same team by checking the destination
	 * Field.
	 * 
	 * @param field
	 *            the Field to check for Pawns on the same team
	 * @return
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
	 * Iterates through Pawns on the team to see if a particular Pawn is on the
	 * team.
	 * 
	 * @param foundPawn
	 *            the Pawn to check the loyalties of
	 * @return
	 */
	private boolean isOwnPawn(final Pawn foundPawn) {
		boolean ownPawn = false;
		for (Pawn p : thePawns) {
			ownPawn |= (p == foundPawn);
		}
		return ownPawn;
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
