import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/** 
 *
 */
public class HumanStrategy implements Strategy, MouseListener {

	private boolean waiting = true;
	private Pawn movePawn = null;
	private Player thePlayer = null;
	private int theRoll = 0;

	/*
	 * (non-Javadoc)
	 * 
	 * @see Strategy#doMove()
	 */
	@Override
	public void chooseMove(final Player player, final int dieRoll) {
		this.thePlayer = player;
		this.theRoll = dieRoll;
		for (Pawn p : thePlayer.getPawns()) {
			p.getImgSrc().addMouseListener(this);
		}
		// System.err.println("*whistles*");
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			Pawn thePawn = null;
			Object clickSource = e.getSource();
			for (Pawn p : thePlayer.getPawns()) {
				if (p.getImgSrc() == clickSource) {
					thePawn = p;
					break;
				}
			}
			// Take down the listeners.
			for (Pawn p : thePlayer.getPawns()) {
				while (p.getImgSrc().getMouseListeners().length > 0) {
					p.getImgSrc().removeMouseListener(
							p.getImgSrc().getMouseListeners()[0]);
				}
			}
			sendMoveToPlayer(thePlayer, new Move(thePawn, thePlayer
					.getHomeField().getNextField()), theRoll);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void sendMoveToPlayer(final Player player, final Move move,
			final int dieRoll) {
		System.err.println("Sending move...");
		player.takeMove(move, dieRoll);
	}
}
