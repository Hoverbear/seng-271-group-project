public interface Strategy {

	/**
	 * 
	 * @param player
	 *            The Player the strategy belongs to. For determining the Pawns
	 *            and associated Fields to decide a move.
	 * @param dieRoll
	 *            The die roll, so the strategy can determine the appropriate
	 *            move.
	 * @return The Pawn to move
	 */
	public void doMove(final Player player, final int dieRoll);
}
