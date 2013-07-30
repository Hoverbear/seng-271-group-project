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
		// TODO Auto-generated method stub
		theHome = player.getHomeField();
		thePawns = player.getPawns();

		if (dieRoll == 6 && theHome.getPawnCount() > 0) {
			if (player.movePawnFromHome()) {
				// TODO ??
				// return null;
			}
		}
		Field f = theHome.getNextField();
		for (int i = 0; i < 4; i++) {
			while (!FieldHasPlayerPawn(f)) {
				f = f.getNextField();
			}
			// f will now contain the rearmost player Pawn
			// if (player.movePawnSpaces(f.getPawn(), (BasicField) f, dieRoll))
			// {
			// break;
			// }
		}
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
	public void sendMoveToPlayer(final Player player, final Move move,
			final int dieRoll) {
		// TODO Auto-generated method stub

	}
}
