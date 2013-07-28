import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Die {
	/* This is a singleton! */
	private static Die instance = new Die();
	private int value;
	private static JLabel imgSrc;

	/**
	 * 
	 */
	private Die() {
	}

	/**
	 * 
	 * @return The singleton of the die.
	 */
	public static Die getInstance(final JLabel img) {
		if (instance == null) {
			synchronized (Die.class) {
				if (instance == null) {
					instance = new Die();
				}
			}
		}
		imgSrc = img;
		return instance;
	}

	/**
	 * 
	 * @return The value of the die.
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Rolls the die.
	 * 
	 * @return A newly generated die roll.
	 */
	public int roll() {
		value = (int) Math.ceil(Math.random() * 6);
		return value;
	}

	public void setImage(final ImageIcon img) {
		imgSrc.setIcon(img);
	}
}
