package benchmark.hdd;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import logging.ILogger;
import logging.TimeUnit;
import timing.ITimer;
import timing.Timer;

import benchmark.IBenchmark;

public class HDDReadSpeed implements IBenchmark {


	public void initialize(int size) {
	}


	public void warmUp() {
	}


	public void run() {
	}

	public void writeNewRandFile(String fn1, String fn2){
		try{
			File f = new File(fn1);
			FileWriter fw = new FileWriter(fn1);			

			Random rd = new Random();
			for(int i=0;i<210000000;++i) {
				String tmp;
				tmp = new Integer(rd.nextInt()).toString();
				fw.write(tmp);
			}
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run(Object... options) {
		/***
		 * important:
		 * [0] -> type ( stream or nio)
		 * [1] -> buffersize
		 */
		try {
			ReadOptions option = (ReadOptions) options[0];
			int buffsize = (int)options[1];
			FileReader reader = new FileReader();

			String sourcePath = "somefile.dat";
			String sourcePath2 = "someotherfile.dat";
			switch (option) {
			case STREAM:
				writeNewRandFile(sourcePath,sourcePath2);			
				reader.compareWithBufferSize(sourcePath, sourcePath2, 1024);
				break;
			case NIO:
				writeNewRandFile(sourcePath,sourcePath2);
				reader.readNio(sourcePath, buffsize);
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void clean() {
	}

	public String getResult() {
		return null;
	}

	public enum ReadOptions {
		STREAM
		, NIO
	}

	@Override
	public void init(int max) {
		// TODO Auto-generated method stub

	}

	@Override
	public void runAndLog(ILogger logger, ITimer timer) {
		// TODO Auto-generated method stub

	}

}
