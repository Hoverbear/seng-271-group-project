
public class Die {
	public Die(){
		
	}
	
	/*
	 * Return a random die roll between 1 and 6.
	 */
	public int roll(){
		return (int) (1 + Math.random() * 6);
	}
}
