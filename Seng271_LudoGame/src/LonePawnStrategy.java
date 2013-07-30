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
	public void chooseMove(final Player player, final int dieRoll) {
		theHome = player.getHomeField();
		theGoal = player.getEntryGoalField();
		thePawns = player.getPawns();

		// If there are no pawns on the field and you can play a pawn to the
		// field.
		if ((theHome.getPawnCount() + player.getGoalOccupiedCount() == 4)
				&& dieRoll == 6) {
			// If there are pawns in the home field.
			if (theHome.hasPawn()) {
				// Play a pawn onto the field.
				// player.movePawnFromHome();
				// return new Move(theHome.getPawn(), theHome.getNextField());
				sendMoveToPlayer(player,
						new Move(theHome.getPawn(), theHome.getNextField()),
						dieRoll);
				return;
			} else {
				// Since there must be 4 pawns in Goal, the player has won.
				System.err.println("A player who won is trying to move...");
			}
			// If the player has a pawn on the field.
		} else if (!(theHome.getPawnCount() + player.getGoalOccupiedCount() == 4)) {
			if (player.checkIfGoalFull()) {
				// Shouldn't reach here.
				System.err
						.println("LonePawn shouldn't reach here, you say? WRONG!");
			} else {
				// If a pawn is on a BasicField, it's the one we want to move.
				Pawn movePawn = null;
				for (Pawn p : thePawns) {
					if (p.isAtBasic()) {
						movePawn = p;
						break;
					}
				}
				if (movePawn != null) {
					Field moveField = player.movePawnSpaces(movePawn,
							(BasicField) movePawn.getField(), dieRoll);
					if (moveField != null) {
						// return new Move(movePawn, moveField);
						sendMoveToPlayer(player, new Move(movePawn, moveField),
								dieRoll);
						return;
					} else {
						for (Pawn p : thePawns) {
							if (p.isAtGoal()) {
								moveField = player.movePawnGoal(p,
										(GoalField) p.getField(), dieRoll);
								if (moveField != null) {
									if (player.checkValidMove(moveField)) {
										sendMoveToPlayer(player, new Move(p,
												moveField), dieRoll);
										return;
									}
								}
							}
						}
					}
				}
				// return null;
				sendMoveToPlayer(player, null, dieRoll);
				return;
				// player.movePawnNormal(dieRoll);
			}
			// If the player has no pawns on the field, but at least one pawn is
			// in goal.
		} else if (player.checkIfGoalOccupied()) {
			for (Pawn p : thePawns) {
				if (p.isAtGoal()) {
					Field moveField = player.movePawnGoal(p,
							(GoalField) p.getField(), dieRoll);
					if (moveField != null) {
						if (player.checkValidMove(moveField)) {
							sendMoveToPlayer(player, new Move(p, moveField),
									dieRoll);
							return;
						}
					}
				}
			}
			sendMoveToPlayer(player, null, dieRoll);
			return;
		}
		// If the player has no pawns to play, and doesn't have a 6, he passes.

		// If they win
		if (player.checkIfGoalFull()) {
			System.out.println("This player is done!\n");
		} else {
			// TODO: Please fill this out!
		}
		// return null;
		sendMoveToPlayer(player, null, dieRoll);
		return;
	}

	@Override
	public void sendMoveToPlayer(final Player player, final Move move,
			final int dieRoll) {
		player.takeMove(move, dieRoll);
	}
}
