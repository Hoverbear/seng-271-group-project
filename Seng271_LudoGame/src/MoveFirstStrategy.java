import java.util.ArrayList;

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
			HomeField home = player.getHomeField();
			Field field = home;
			ArrayList<Pawn> pawns = player.getPawns();
			Pawn chosen = null;
			// Number of pawns on the board.
			int needed = 4 - player.getGoalOccupiedCount()
					- player.getHomeField().getPawnCount();

			// Loop around the board until we find the Pawn farthest distance
			// from home.
			// TODO: Does this fail if there are no pawns on the board?
			while (field.getNextField() != home && chosen == null) {
				if (field.hasPawn()) {
					Pawn thePawn = field.getPawn();
					if (pawns.contains(thePawn)) {
						if (needed > 0) {
							needed--;
						} else {
							chosen = thePawn;
						}
					}
				}
			}

			return chosen;
		}
		// Add a new pawn.
		else {
			// TODO: Select the farthest along pawn.
			return player.getHomeField().getPawn();
		}
	}

}
