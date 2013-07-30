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

		if (dieRoll == 6 && theHome.getPawnCount() > 0
				&& player.checkMovePawnHome() != null) {
			sendMoveToPlayer(player,
					new Move(theHome.getPawn(), theHome.getNextField()));
			return;
		} else {
			ArrayList<Pawn> chosen = new ArrayList<Pawn>();
			Field field = theHome.getNextField();

			do {
				if (field.hasPawn()) {
					if (thePawns.contains(field.getPawn())) {
						chosen.add(field.getPawn());
					}
				}
				field = field.getNextField();
			} while (field != theHome.getNextField());

			for (Pawn p : chosen) {
				Field f = player.checkMovePawnBasic(p,
						(BasicField) p.getField(), dieRoll);
				if (f != null) {
					sendMoveToPlayer(player, new Move(p, f));
					return;
				}
			}

			for (Pawn p : thePawns) {
				if (p.isAtGoal()) {
					Field f = player.checkMovePawnGoal(p,
							(GoalField) p.getField(), dieRoll);
					if (f != null) {
						sendMoveToPlayer(player, new Move(p, f));
						return;
					}
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
