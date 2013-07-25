import java.awt.Point;

import javax.swing.JLabel;

public class Pawn {
	private final JLabel imgSrc;
	private Point position;
	private Field location;

	/**
	 * 
	 * @param source
	 * @param pos
	 */
	public Pawn(final JLabel source, final Field loc) {
		this.imgSrc = source;
		moveToField(loc);
	}

	/**
	 * 
	 * @return
	 */
	protected JLabel getImgSrc() {
		return imgSrc;
	}

	/**
	 * 
	 * @return
	 */
	private Point getPosition() {
		return position;
	}

	/**
	 * 
	 * @param pos
	 */
	private void setPosition(final Point pos) {
		this.position = pos;
		imgSrc.setLocation(pos);
	}

	public final Field getField() {
		return location;
	}

	/**
	 * 
	 * @param field
	 */
	public void moveToField(Field field) {
		this.location = field;
		setPosition(location.getPoint());
		location.setPawn(this);
	}
}
