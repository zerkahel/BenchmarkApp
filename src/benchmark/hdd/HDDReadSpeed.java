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

	public void writeNewRandFile(String fn1){
		try{
			File f = new File(fn1);
			if(f.exists())
				return;
			FileWriter fw = new FileWriter(fn1);			

			Random rd = new Random();
			for(int i=0;i<310000000;++i) {
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
		 * [3] -> update chart callback
		 */
		try {
			ReadOptions option = (ReadOptions) options[0];
			int buffsize = (int)options[1];
			UpdateChart uc = (UpdateChart)options[2];
			FileReader reader = new FileReader();

			String sourcePath = "somefile.dat";
			String sourcePath2 = "someotherfile.dat";
			switch (option) {
			case STREAM:
				writeNewRandFile(sourcePath);			
				break;
			case NIO:
				writeNewRandFile(sourcePath);
				reader.readNio(sourcePath, buffsize,uc);
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
