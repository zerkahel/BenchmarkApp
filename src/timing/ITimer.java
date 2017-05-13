package timing;

/**
 * @author alxtsa
 *
 */
public interface ITimer {
	/**
	 * Starts the timer in the current instance of the class
	 * 
	 */
	public void start();
	/**
	 * Stops the timer in the current instance of the class
	 *  
	 * @return number of nanoseconds
	 */
	public long stop();
	/**
	 * Pauses the timer in the current instance of the class
	 * 
	 * @return number of nanoseconds
	 */
	public long pause();
	/**
	 * Resumes the timer in the current instance of the class
	 */
	public void resume();
}
