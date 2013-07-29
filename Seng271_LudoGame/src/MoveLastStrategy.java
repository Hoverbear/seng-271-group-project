/** 
 *
 */
public class MoveLastStrategy implements Strategy {
	public Pawn chooseMove(Pawn canScore, Pawn canKnock, boolean canAddNew,
			Player player) {
		// Get in goal
		if (canScore != null) {
			return canScore;
		}
		// Knock someone out.
		else if (canKnock != null) {
			return canKnock;
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
