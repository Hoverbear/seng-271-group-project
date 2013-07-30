import java.util.ArrayList;

/** 
 *
 */
public class AggressiveStrategy implements Strategy {

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

		Pawn frontMostValid = null;
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

		// Now iterate through pawns on basic fields from most forward to least.
		for (Pawn p : basicFieldPawns) {
			Field f = player.checkMovePawnBasic(p, (BasicField) p.getField(),
					dieRoll);
			if (f != null) {
				if (frontMostValid == null) {
					frontMostValid = p;
				}
				if (f.getPawn() != null) {
					sendMoveToPlayer(player, new Move(p, f));
					return;
				}
			}
		}

		// If an aggressive move wasn't found, can a pawn be brought out?
		if (dieRoll == 6 && theHome.getPawnCount() > 0
				&& player.checkMovePawnHome() != null) {
			sendMoveToPlayer(player,
					new Move(theHome.getPawn(), theHome.getNextField()));
			return;
		}

		// If no pawns can be brought out, try to move front most valid pawn.
		if (frontMostValid != null) {
			Field f = player.checkMovePawnBasic(frontMostValid,
					(BasicField) frontMostValid.getField(), dieRoll);
			sendMoveToPlayer(player, new Move(frontMostValid, f));
			return;
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

	@Override
	public void sendMoveToPlayer(final Player player, final Move move) {
		player.takeMove(move);
	}

}
