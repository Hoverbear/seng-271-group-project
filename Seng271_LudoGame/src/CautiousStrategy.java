/** 
 *
 */
public class CautiousStrategy implements Strategy {
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
		// Normal Move.
		else if (!(canAddNew && player.getHomeField().getPawnCount()
				+ player.getGoalOccupiedCount() == 4)) {
			// Get a pawn from the home field and move it!
			return player.getPawns().get(0);
			// TODO: Select one intelligently.
		}
		// Add a new pawn.
		else {
			// TODO: Get this right.
			return player.getHomeField().getPawn();
		}
	}

}