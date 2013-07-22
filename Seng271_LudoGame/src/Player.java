/* 
 * 
 */
public class Player {
	// Feel free to change these to public as you see fit.
	private Strategy strategy;
	private GoalField goalField;
	private HomeField homeField;
	private Pawn[] pawns;
	
	/*
	 * 
	 */
	public Player(Strategy strategy, GoalField goalField, HomeField homeField, Pawn[] pawns){
		this.strategy = strategy;
		this.goalField = goalField;
		this.homeField = homeField;
		// TODO: Verify correctness.
		this.pawns = pawns;
	}
	
	/*
	 * 
	 */
	public void planMove(){
		
	}
	
	/*
	 * 
	 */
	public void doMove() {
		
	}
}
