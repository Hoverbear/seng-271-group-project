import java.util.ArrayList;

/* 
 * 
 */
public class Player {
	private final Strategy strategy;
	private final GoalField goalField;
	private final HomeField homeField;
	// Should be a set of 4.
	private final ArrayList<Pawn> pawns = new ArrayList<Pawn>();

	/**
	 * 
	 * @param strategy
	 *            The strategy used by the player.
	 * @param goalField
	 *            The player's goal field. This should be determined by the game
	 *            itself.
	 * @param homeField
	 *            The player's home field. This should be determined by the game
	 *            itself.
	 * @param pawns
	 *            The set of the players pawns. These should be created by the
	 *            Ludo game initialization.
	 */
	public Player(final Strategy strategy, final GoalField goalField,
			final HomeField homeField, final ArrayList<Pawn> pawns) {
		this.strategy = strategy;
		this.goalField = goalField;
		this.homeField = homeField;
		for (Pawn p : pawns) {
			pawns.add(p);
		}
	}

	/**
	 * 
	 */
	public void planMove() {

	}

	/**
	 * 
	 */
	public void doMove() {

	}
}
