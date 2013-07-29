/** 
 *
 */
public class MoveFirstStrategy implements Strategy {
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
			return player.getPawns().get(0);
			// TODO: Select one intelligently.
		}
		// Add a new pawn.
		else {
			// TODO: Select the farthest along pawn.
			return player.getHomeField().getPawn();
		}
	}

}
