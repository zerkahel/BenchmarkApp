/**
 * 
 */
package logging;

import java.io.*;
/**
 * @author alxtsa
 *
 */
public class Logger implements ILogger {

	/* (non-Javadoc)
	 * @see logging.ILogger#printTime(long, logging.TimeUnit)
	 */
	@Override
	public void consolePrintTime(long time, TimeUnit timeUnit) {
		System.out.println(TimeUnit.ConvertTime(time, timeUnit)+"");
	}

	/* (non-Javadoc)
	 * @see logging.ILogger#printToFile(java.lang.String)
	 */
	@Override
	public void printToFile(String str) {
		printToFile(str,"results.txt");
	}

	/* (non-Javadoc)
	 * @see logging.ILogger#printToFile(java.lang.String, java.io.File)
	 */
	@Override
	public void printToFile(String str, String fileName) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
			bw.write(str);

		} catch (IOException e) {
			e.printStackTrace();

		}
	}

}
