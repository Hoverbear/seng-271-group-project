import java.util.ArrayList;

public interface Strategy {
	
	/**
	 * 
	 * @param Pawns
	 * 			A list of pawns that the player owns.
	 * @param die
	 * 			The die, so the strategy can determine the appropriate move.
	 * @return
	 * 			The new pawn arraylist.
	 */
	public ArrayList<Pawn> doMove(ArrayList<Pawn> Pawns, Die die);
}
