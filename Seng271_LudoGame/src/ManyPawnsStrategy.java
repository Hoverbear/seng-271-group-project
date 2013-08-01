import java.util.ArrayList;

/** 
 *
 */
public class ManyPawnsStrategy implements Strategy {

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

		// Find all the pawns on the field.
		// Map them into the "Chosen" list.
		ArrayList<Pawn> chosen = new ArrayList<Pawn>();
		Field field = theHome.getNextField();
		// Traverse
		do {
			if (field.hasPawn()) {
				if (thePawns.contains(field.getPawn())) {
					chosen.add(field.getPawn());
				}
			}
			field = field.getNextField();
		} while (field != theHome.getNextField());

		// By now we have a list of pawns on the field.

		// Can do a basic move?
		for (Pawn p : chosen) {
			Field f = player.checkMovePawnBasic(p, (BasicField) p.getField(),
					dieRoll);
			if (f != null) {
				sendMoveToPlayer(player, new Move(p, f));
				return; // This is key! Once we find one, stop!
			}
		}

		// Can score?
		for (Pawn p : thePawns) {
			if (p.isAtGoal()) {
				Field f = player.checkMovePawnGoal(p, (GoalField) p.getField(),
						dieRoll);
				if (f != null) {
					sendMoveToPlayer(player, new Move(p, f));
					return; // This is key! Once we find one, stop!
				}
			}
		}

		// If the player has no moves, he passes.
		sendMoveToPlayer(player, null);
	}

	@Override
	public void sendMoveToPlayer(final Player player, final Move move) {
		player.takeMove(move);
	}

}
