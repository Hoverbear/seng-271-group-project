import java.awt.Point;

import javax.swing.JLabel;

public class Pawn {
	private final JLabel imgSrc;
	private Point position; // TODO Update this to relate to a Field tile.
	
	/**
	 * 
	 * @param source
	 * @param pos
	 */
	public Pawn(final JLabel source, final Point pos) {
		this.imgSrc = source;
		setPosition(pos);
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
	public final Point getPosition() {
		return position;
	}
	
	/**
	 * 
	 * @param pos
	 */
	private void setPosition(final Point pos) {
		this.position = pos;
	}
	
	/**
	 * 
	 * @param field
	 */
	public void moveToField(Field field){
		
	}
}
