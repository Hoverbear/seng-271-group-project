import java.awt.Point;

import javax.swing.JLabel;

public class Pawn {

	private final JLabel imgSrc;
	private Point position; // TODO Update this to relate to a Field tile.
	
	public Pawn(final JLabel source, final Point pos) {
		this.imgSrc = source;
		setPosition(pos);
	}
	
	protected JLabel getImgSrc() {
		return imgSrc;
	}
	
	public final Point getPosition() {
		return position;
	}
	
	private void setPosition(final Point pos) {
		this.position = pos;
	}
}
