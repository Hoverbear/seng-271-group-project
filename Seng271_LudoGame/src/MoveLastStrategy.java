import java.util.ArrayList;

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
		// Normal Move.
		else if (!(canAddNew && player.getHomeField().getPawnCount()
				+ player.getGoalOccupiedCount() == 4)) {
			HomeField home = player.getHomeField();
			Field field = home;
			ArrayList<Pawn> pawns = player.getPawns();
			Pawn chosen = null;

			// Loop around the board until we find the pawn shortest distance
			// from home.
			// TODO: Does this fail if there are no pawns on the board?
			while (field.getNextField() != home && chosen == null) {
				if (field.hasPawn()) {
					Pawn thePawn = field.getPawn();
					if (pawns.contains(thePawn)) {
						chosen = thePawn;
					}
				}
			}

			return chosen;
		}
		// Add a new pawn.
		else {
			// TODO: Get this right.
			return player.getHomeField().getPawn();
		}
	}
}