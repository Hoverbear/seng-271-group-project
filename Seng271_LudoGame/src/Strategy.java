
public interface Strategy {
	public Pawn chooseMove(Pawn canScore, Pawn canKnock, boolean canAddNew,
			Player player);
}
