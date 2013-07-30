import java.util.ArrayList;

/** 
 *
 */
public class LonePawnStrategy implements Strategy {

	private HomeField theHome;
	private ArrayList<Pawn> thePawns;

	/*
	 * (non-Javadoc)
	 * 
	 * @see Strategy#doMove()
	 */
	@Override
	public void chooseMove(final Player player, final int dieRoll) {
		theHome = player.getHomeField();
		thePawns = player.getPawns();

		// If there are no pawns on the field and you can play a pawn to the
		// field.
		if ((theHome.getPawnCount() + player.getGoalOccupiedCount() == 4)
				&& dieRoll == 6) {
			// If there are pawns in the home field.
			if (theHome.hasPawn()) {
				// Play a pawn onto the field.
				sendMoveToPlayer(player,
						new Move(theHome.getPawn(), theHome.getNextField()));
				return;
			} else {
				// Since there must be 4 pawns in Goal, the player has won.
				System.err.println("A player who won is trying to move...");
			}
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
					Field moveField = player.checkMovePawnBasic(movePawn,
							(BasicField) movePawn.getField(), dieRoll);
					if (moveField != null) {
						sendMoveToPlayer(player, new Move(movePawn, moveField));
						return;
					} else {
						for (Pawn p : thePawns) {
							if (p.isAtGoal()) {
								moveField = player.checkMovePawnGoal(p,
										(GoalField) p.getField(), dieRoll);
								if (moveField != null) {
									if (player.checkValidMove(moveField)) {
										sendMoveToPlayer(player, new Move(p,
												moveField));
										return;
									}
								}
							}
						}
					}
				}
				sendMoveToPlayer(player, null);
				return;
			}
		} else if (player.checkIfGoalOccupied()) {
			for (Pawn p : thePawns) {
				if (p.isAtGoal()) {
					Field moveField = player.checkMovePawnGoal(p,
							(GoalField) p.getField(), dieRoll);
					if (moveField != null) {
						if (player.checkValidMove(moveField)) {
							sendMoveToPlayer(player, new Move(p, moveField));
							return;
						}
					}
				}
			}
			sendMoveToPlayer(player, null);
			return;
		}

		// If they win
		if (player.checkIfGoalFull()) {
			System.out.println("This player is done!\n");
		}

		// If the player has no pawns to play, and doesn't have a 6, he passes.
		sendMoveToPlayer(player, null);
		return;
	}

	@Override
	public void sendMoveToPlayer(final Player player, final Move move) {
		player.takeMove(move);
	}
}
