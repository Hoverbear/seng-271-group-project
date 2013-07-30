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
	public void chooseMove(Player player, int dieRoll) {
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

		sendMoveToPlayer(player, null);
		// Field f = theHome.getNextField();
		// for (int i = 0; i < 4; i++) {
		// while (!FieldHasPlayerPawn(f)) {
		// f = f.getNextField();
		// }
		// f will now contain the rearmost player Pawn
		// if (player.movePawnSpaces(f.getPawn(), (BasicField) f, dieRoll))
		// {
		// break;
		// }
		// }
		// Print some message saying there is no legal move
	}

	private boolean FieldHasPlayerPawn(Field f) {
		Pawn p = f.getPawn();
		for (int i = 0; i < 4; i++) {
			if (thePawns.get(i) == p) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void sendMoveToPlayer(final Player player, final Move move) {
		player.takeMove(move);
	}
}
