public class Die {
	/* This is a singleton! */
	private static Die instance = new Die();
	private int value;

	/**
	 * 
	 */
	private Die() {
	}

	/**
	 * 
	 * @return The singleton of the die.
	 */
	public static Die getInstance() {
		if (instance == null) {
			synchronized (Die.class) {
				if (instance == null) {
					instance = new Die();
				}
			}
		}
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
		value = (int) (1 + Math.random() * 6);
		return value;
	}
}
