/**
 * 
 */
package logging;

import java.io.File;

/**
 * @author alxtsa
 *
 */
public interface ILogger {
	/**
	 * Prints time in the desired time unit
	 * @param time time in nanoseconds
	 * @param timeUnit desired time unit
	 */
	public void consolePrintTime(long time, TimeUnit timeUnit);
	/**
	 * Prints a string to the default file
	 * @param str string to be printed
	 */
	public void printToFile(String str);
	/**
	 * Prints a string to the desired file
	 * @param str string to be printed
	 * @param fileName desired file
	 */
	public void printToFile(String str, String fileName);
}
