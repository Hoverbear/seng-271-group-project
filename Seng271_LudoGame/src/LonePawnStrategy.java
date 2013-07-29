import java.util.ArrayList;

/** 
 *
 */
public class LonePawnStrategy implements Strategy {

	private HomeField theHome;
	private GoalField theGoal;
	private ArrayList<Pawn> thePawns;

	/*
	 * (non-Javadoc)
	 * 
	 * @see Strategy#doMove()
	 */
	@Override
	public void doMove(final Player player, final int dieRoll) {
		theHome = player.getHomeField();
		theGoal = player.getEntryGoalField();
		thePawns = player.getPawns();

		// If there are no pawns on the field and you can play a pawn to the
		// field.
		if ((theHome.getPawnCount() + player.getGoalOccupiedCount() == 4)
				&& dieRoll == 6) {
			// If there are pawns in the home field.
			if (theHome.hasPawn()) {
				// Play a pawn onto the field.
				player.movePawnFromHome();
			} else {
				// Since there must be 4 pawns in Goal, the player has won.
				System.err.println("A player who won is trying to move...");
			}
			// If the player shouldn't add a new pawn to the field.
		} else if (!(theHome.getPawnCount() + player.getGoalOccupiedCount() == 4)) {
			if (player.checkIfGoalFull()) {
				// TODO
			} else {
				player.movePawnNormal(dieRoll);
			}
		}
		// If the player has no pawns to play, and doesn't have a 6, he passes.

		// If they win
		if (player.checkIfGoalFull()) {
			System.out.println("This player is done!\n");
		} else {
			// TODO: Please fill this out!
		}
	}
}
