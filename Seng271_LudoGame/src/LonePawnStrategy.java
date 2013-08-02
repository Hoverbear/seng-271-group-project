import java.util.ArrayList;

/**
 * A strategy that favours rushing a single Pawn to the GoalField before moving
 * any others.
 */
public class LonePawnStrategy implements Strategy {

	private HomeField theHome;
	private ArrayList<Pawn> thePawns;

	/*
	 * (non-Javadoc)
	 * 
	 * @see Strategy#chooseMove()
	 */
	@Override
	public void chooseMove(final Player player, final int dieRoll) {
		theHome = player.getHomeField();
		thePawns = player.getPawns();

		ArrayList<Pawn> basicFieldPawns = new ArrayList<Pawn>();

		// First iterate through fields to find own pawns.
		Field field = theHome.getNextField();
		do {
			if (field.hasPawn()) {
				if (thePawns.contains(field.getPawn())) {
					basicFieldPawns.add(0, field.getPawn());
				}
			}
			field = field.getNextField();
		} while (field != theHome.getNextField());

		// Iterate through pawns on basic fields and move the most forward one.
		for (Pawn p : basicFieldPawns) {
			Field f = player.checkMovePawnBasic(p, (BasicField) p.getField(),
					dieRoll);
			if (f != null) {
				sendMoveToPlayer(player, new Move(p, f));
				return;
			}
		}

		// Iterates through the goal pawns if no valid move found yet.
		for (Pawn p : thePawns) {
			if (p.isAtGoal()) {
				Field f = player.checkMovePawnGoal(p, (GoalField) p.getField(),
						dieRoll);
				if (f != null) {
					sendMoveToPlayer(player, new Move(p, f));
					return;
				}
			}
		}

		// If the player has no moves, he passes.
		sendMoveToPlayer(player, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Strategy#sendMoveToPlayer()
	 */
	@Override
	public void sendMoveToPlayer(final Player player, final Move move) {
		player.takeMove(move);
	}
}
