/* 
 * 
 */
public class Player {
	// Feel free to change these to public as you see fit.
	private Strategy strategy;
	private GoalField goalField;
	private HomeField homeField;
	private Pawn[] pawns; // Should be a set of 4.
	
	/**
	 * 
	 * @param strategy
	 * 			The strategy used by the player.
	 * @param goalField
	 * 			The player's goal field. This should be determined by the game itself.
	 * @param homeField
	 * 			The player's home field. This should be determined by the game itself.
	 * @param pawns
	 * 			The set of the players pawns. These should be created by the Ludo game initialization.
	 */
	public Player(Strategy strategy, GoalField goalField, HomeField homeField, Pawn[] pawns){
		this.strategy = strategy;
		this.goalField = goalField;
		this.homeField = homeField;
		// TODO: Verify correctness.
		this.pawns = pawns;
	}
	
	/**
	 * 
	 */
	public void planMove(){
		
	}
	
	/**
	 * 
	 */
	public void doMove() {
		
	}
}
