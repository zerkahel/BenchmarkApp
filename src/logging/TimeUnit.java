/**
 * 
 */
package logging;

/**
 * @author alxtsa
 *
 */
public enum TimeUnit {
	/**
	 * Nanosecond
	 */
	NANO,
	/**
	 * Microseconds
	 */
	MICRO,
	/**
	 * Miliseconds
	 */
	MILI,
	/**
	 * Seconds
	 */
	SECONDS;
	/**
	 * Converts time from nano seconds to desired time unit
	 * @param time time in nanoseconds 
	 * @param timeUnit desired time unit
	 * @return the time in the desired unit
	 */
	public static long ConvertTime(long time, TimeUnit timeUnit) {
		long retVal=0;
		
		switch(timeUnit) {
		case MICRO:
			retVal=time/1000;break;
		case MILI:
			retVal=time/1000000;break;
		case SECONDS:
			retVal=time/1000000000;break;
		default:
			retVal=time;break;
		}
		
		return retVal;
	}
}
