package benchmark.hdd;

import java.io.IOException;

import benchmark.IBenchmark;
import logging.ILogger;
import timing.ITimer;

public class HDDWriteSpeed implements IBenchmark {

	
	public void initialize(int size) {
	}

	
	public void warmUp() {
	}

	
	public void run() {
		throw new UnsupportedOperationException(
				"Method not implemented. Use run(Object) instead");
	}

	
	public void run(Object... options) {
		FileWriter writer = new FileWriter();
		// either "fs" - fixed size, or "fb" - fixed buffer
		String option = (String) options[0];
		// true/false whether the written files should be deleted at the end
		Boolean clean = (Boolean) options[1];

		String prefix = "tempBenchData/data";
		String suffix = ".dat";
		int startIndex = 0;
		int endIndex = 8;
		
		try {
			if (option.equals("fs"))
				writer.streamWriteFixedSize(prefix, suffix, startIndex,
						endIndex, 256 * 1024 * 1024, clean);
			else if (option.equals("fb"))
				writer.streamWriteFixedBuffer(prefix, suffix, startIndex,
						endIndex, 4 * 1024, clean);
			else
				throw new IllegalArgumentException("Argument "
						+ options[0].toString() + " is undefined");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public void clean() {
	}

	
	public String getResult() {
		return null;
	}

	
	public void init(int max) {
		// TODO Auto-generated method stub
		
	}

	
	public void runAndLog(ILogger logger, ITimer timer) {
		// TODO Auto-generated method stub
		
	}
}
