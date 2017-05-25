package benchmark.hdd;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;

import GUI.UpdateChart;
import logging.ILogger;

import timing.ITimer;
import timing.Timer;
import benchmark.IBenchmark;

public class HDDRandomAccess implements IBenchmark {

	private final static String PATH = "somefile.dat";
	private String result;

	public void initialize(long fileSizeInBytes) {
		RandomAccessFile file;

		// Create a temporary file with random content to be used for
		// reading/writing
		try {
			file = new RandomAccessFile(PATH, "rw");
			Random rand = new Random();
			int bufferSize = 4 * 1024;
			long toWrite = fileSizeInBytes / (long)bufferSize;
			byte[] buffer = new byte[bufferSize];
			int counter = 0;

			while (counter++ < toWrite) {
				rand.nextBytes(buffer);
				file.write(buffer);
			}

			file.close();

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}


	public void warmUp() {
		// have a Mountain Dew or Red Bull
	}

	@Override
	public void run() {
		throw new UnsupportedOperationException("Use run(Object[]) instead");
	}

	public void run(Object... option) {
		// ex. {"r", "fs", 4*1024}
		Object[] param = (Object[]) option;
		UpdateChart uc = (UpdateChart)param[3];

		try {
			// read benchmark
			if(param[0] != null){
				// buffer size given as parameter
				int bufferSize = Integer.parseInt(String.valueOf(param[2]));
				
				// read a fixed size and measure time
				if (String.valueOf(param[1]).toLowerCase().equals("fs")) {
					int steps = 10000;
					if (String.valueOf(param[0]).toLowerCase().equals("r")) {
						long time = new RandomAccess().randomReadFixedSize(PATH,
								bufferSize, steps,"r",uc);

						result = steps + " random reads in " + time + " ms ["
								+ (steps * bufferSize / 1024 / 1024) + " MB] " + (steps * bufferSize / 1024 / 1024)/(time/1000) + "MB/s";
					}
					else if (String.valueOf(param[0]).toLowerCase().equals("w")) {
						long time = new RandomAccess().randomReadFixedSize(PATH,
								bufferSize, steps,"w",uc);
						result = steps + " random writes in " + time + " ms ["
								+ (steps * bufferSize / 1024 / 1024) + " MB] " + (steps * bufferSize / 1024 / 1024)/(time/1000) + "MB/s";
					}
				}
				// read a fixed time amount and measure time
				else if (String.valueOf(param[1]).toLowerCase().equals("ft")) {
					int time = 1000;
					int ios=0;
					if (String.valueOf(param[0]).toLowerCase().equals("r")) 
						ios = new RandomAccess().randomReadFixedTime(PATH,bufferSize, time,"r");
					else if(String.valueOf(param[0]).toLowerCase().equals("w")) 
						ios = new RandomAccess().randomReadFixedTime(PATH,bufferSize, time,"w");
					result = ios + " I/Os per second ["
							+ (ios * bufferSize / 1024 / 1024) + " MB]";

				} else
					throw new UnsupportedOperationException("Read option \""
							+ String.valueOf(param[1])
							+ "\" is not implemented");

			} else
				throw new UnsupportedOperationException("Benchmark option \""
						+ String.valueOf(param[0]) + "\" is not implemented");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void clean() {
		// remove created file perhaps?
	}

	public String getResult() {
		return String.valueOf(result);
	}

	class RandomAccess {
		private Random random;

		RandomAccess() {
			random = new Random();
		}

		/**
		 * Reads data from random positions into a fixed size buffer from a
		 * given file using RandomAccessFile
		 * 
		 * @param filePath
		 *            Full path to file on disk
		 * @param bufferSize
		 *            Size of byte buffer to read at each step
		 * @param toRead
		 *            Number of steps to repeat random read
		 * @return Amount of time needed to finish given reads
		 * @throws IOException
		 */
		public long randomReadFixedSize(String filePath, int bufferSize,
				int toRead,String operation,UpdateChart uc) throws IOException {
			// file to read from
			RandomAccessFile file = new RandomAccessFile(filePath, "rw");
			// size of file
			int fileSize = (int) (file.getChannel().size() % Integer.MAX_VALUE);
			// counter for number of reads
			int counter = 0;
			// buffer for reading
			byte[] bytes = new byte[bufferSize];
			// timer
			Timer timer = new Timer();
			int pos = 0;
			Random rd = new Random();
			long updateGraphCounter=1;
			final long modmask=0x00000000000000FF;
			Timer tm = new Timer();
			double mseconds = 1;
			double megabytes = 1;
			long totalBytes=0;
			long time=0;

			byte[] data = new byte[bufferSize];
			tm.start();
			while (counter++ < toRead) {
				rd.nextBytes(data);
				// go to random spot in file
				pos = rd.nextInt(fileSize);
				totalBytes+=bufferSize;
				if((updateGraphCounter&modmask)==0){
					time=tm.pause() - time;
					mseconds = time / 1000000d;
					megabytes = (totalBytes / 1024) - megabytes;
					uc.updateData(megabytes/mseconds,totalBytes/1024/1024);
					tm.resume();
				}
				updateGraphCounter++;
				if(operation == "r")
					readFromFile(filePath,pos,bufferSize);			
				else if (operation == "w")
					writeToFile(filePath,data,pos);	
			}

			file.close();
			return timer.stop() / 1000000;
		}

		/**
		 * Reads data from random positions into a fixed size buffer from a
		 * given file using RandomAccessFile for one second, or any other given
		 * time
		 * 
		 * @param filePath
		 *            Full path to file on disk
		 * @param bufferSize
		 *            Size of byte buffer to read at each step
		 * @param millis
		 *            Total time to read from file
		 * @return Number of reads in the given amount of time
		 * @throws IOException
		 */
		public int randomReadFixedTime(String filePath, int bufferSize,
				int millis,String operation) throws IOException {
			// file to read from
			RandomAccessFile file = new RandomAccessFile(filePath, "rw");
			// size of file
			int fileSize = (int) (file.getChannel().size() % Integer.MAX_VALUE);
			// counter for number of reads
			int counter = 0;
			// buffer for reading
			byte[] bytes = new byte[bufferSize];
			int pos = 0;
			Random rd = new Random();
			byte[] data = new byte[bufferSize];
			long now = System.nanoTime();
			long time = System.nanoTime()-now;
			// read for a fixed amount of time
			while (time < millis * 1000000) {
				rd.nextBytes(data);
				// go to random spot in file
				pos = rd.nextInt(fileSize);
				if(operation == "r")
					readFromFile(filePath,pos,bufferSize);			
				else if (operation == "w"){
					//writeToFile(filePath,new String(data),pos);	
				}
				time = System.nanoTime()-now;
				counter++;
			}

			file.close();
			return counter;
		}

		/**
		 * Read data from a file at a specific position
		 * 
		 * @param filePath
		 *            Path to file
		 * @param position
		 *            Position in file
		 * @param size
		 *            Number of bytes to reads from the given position
		 * @return Data that was read
		 * @throws IOException
		 */
		public byte[] readFromFile(String filePath, int position, int size)
				throws IOException {

			RandomAccessFile file = new RandomAccessFile(filePath, "rw");
			file.seek(position);
			byte[] bytes = new byte[size];
			file.read(bytes);
			file.close();
			return bytes;
		}

		/**
		 * Write data to a file at a specific position
		 * 
		 * @param filePath
		 *            Path to file
		 * @param data
		 *            Data to be written
		 * @param position
		 *            Start position in file
		 * @throws IOException
		 */
		public void writeToFile(String filePath, byte[] data, int position)
				throws IOException {

			RandomAccessFile file = new RandomAccessFile(filePath, "rw");
			file.seek(position);
			file.write(data);
			file.close();
		}
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
