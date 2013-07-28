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
	public Pawn doMove(final Player player, final int dieRoll) {
		theHome = player.getHomeField();
		theGoal = player.getEntryGoalField();
		thePawns = player.getPawns();

		if ((theHome.getPawnCount() + player.getGoalOccupiedCount() == 4)
				&& dieRoll == 6) {
			if (theHome.hasPawn()) {
				player.movePawnFromHome();
			} else {
				System.err.println("A player who won is trying to move...");
			}
		} else if (!(theHome.getPawnCount() + player.getGoalOccupiedCount() == 4)) {
			if (player.checkIfGoalFull()) {
				// TODO
			} else {
				player.movePawnNormal(dieRoll);
			}
		}

		if (player.checkIfGoalFull()) {
			System.out.println("This player is done!\n");
		} else {
			//
		}

		return null;
	}
}
