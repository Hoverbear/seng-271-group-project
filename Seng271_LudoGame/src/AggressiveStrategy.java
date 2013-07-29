/** 
 *
 */
public class AggressiveStrategy implements Strategy {
	public Pawn chooseMove(Pawn canScore, Pawn canKnock, boolean canAddNew,
			Player player) {
		// Knock someone out.
		if (canKnock != null) {
			return canKnock;
		}
		// Get in goal
		else if (canScore != null) {
			return canScore;
		}
		// Move a new pawn on
		else if (canAddNew
				&& player.getHomeField().getPawnCount()
						+ player.getGoalOccupiedCount() == 4) {
			// Get a pawn from the home field and move it!
			return player.movePawnFromHome();
			// TODO: Figure out how to do this right.
		}
		// Normal move
		else {
			// TODO: Choose the pawn on the field farthest along, see if that
			// move would work, otherwise choose the other.
			return player.getPawns().get(0);
		}
	}

}
