/**
 * 
 */
package benchmark;
import timing.ITimer;
import logging.ILogger;

/**
 * @author alxtsa
 *
 */
public interface IBenchmark {
	
	/**
	 * Initialize the benchmark
	 * @param max the maximum number of operations
	 */
	
	public String[] getFileSizes();
	
	public String[] getBufferSizes();
	
	public void init(int max);
	/**
	 * Run the benchmark
	 */
	public void run();
	/**
	 * Run the benchmark , time it and log the results
	 * @param logger an instance of a class that extends the ILogger interface
	 * @param timer an instance of a class that extends the ITimer interface
	 */
	public void runAndLog(ILogger logger, ITimer timer);
	/**
	 * Clean any benchmark temporary files
	 */
	public void clean();
}
