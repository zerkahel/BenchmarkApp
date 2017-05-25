package benchmark.hdd;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;

import timing.Timer;

public class FileReader {

	private Timer timer = new Timer();
	private double benchScore;

	/**
	 * Read file benchmark using java.io
	 */
	public void streamReadFixedSize(String fileName, int minBufSize,
			int maxBufSize) throws IOException {

		System.out.println("Stream read benchmark");
		int currentBufferSize = minBufSize;

		int counter = 0;

		while (currentBufferSize <= maxBufSize) {
			readWithBufferSize(fileName, currentBufferSize);
			currentBufferSize *= 2;
			counter++;
		}

		benchScore /= counter;
		System.out.println("File read score : "
				+ String.format("%.2f", benchScore) + " MB/sec");
	}

	/**
	 * Read file benchmark using java.nio	
	 */
	public void nIOReadFixedSize(String fileName, int minBufSize, int maxBufSize)
			throws IOException {

		System.out.println("NIO read");
		int myBufferSize = minBufSize;
		while (myBufferSize <= maxBufSize) {
			timer.start();
			long totalBytes = 0;//readNio(fileName, myBufferSize);

			if (totalBytes != -1) {
				printStats(fileName, totalBytes, myBufferSize);
			} else {
				System.out.println("File are not equal");
			}

			myBufferSize *= 2;
		}
	}

	/**
	 * Read given file with a buffered stream (io)	
	 */
	public void readWithBufferSize(String fileName, int myBufferSize)
			throws IOException {

		if (!(new File(fileName).exists()))
			throw new IOException("Benchmark file " + fileName
					+ " was not found");

		final BufferedInputStream inputStream = new BufferedInputStream(
				new FileInputStream(new File(fileName)), myBufferSize);
		byte[] buffer = new byte[myBufferSize];
		int read;

		timer.start();
		long totalbytes = 0;
		while ((read = inputStream.read(buffer)) != -1) {
			totalbytes += read;
		}
		if (read == -1) {
			printStats(fileName, totalbytes, myBufferSize);
		} else {
			System.out.println("Error reading " + fileName);
		}
		inputStream.close();
	}

	public void compareWithBufferSize(String fileName1, String fileName2,
			int myBufferSize) throws IOException {
		final BufferedInputStream inputStream1 = new BufferedInputStream(
				new FileInputStream(new File(fileName1)), myBufferSize);
		byte[] buff1 = new byte[myBufferSize];
		final BufferedInputStream inputStream2 = new BufferedInputStream(
				new FileInputStream(new File(fileName2)), myBufferSize);
		byte[] buff2 = new byte[myBufferSize];
		int read1;

		System.out.println("File compare benchmark");

		timer.start();
		long totalBytes = 0;
		while ((read1 = inputStream1.read(buff1)) != -1) {
			totalBytes += 2 * read1;
			int read2 = inputStream2.read(buff2);
			if (read1 != read2) {
				break;
			}
			if (!Arrays.equals(buff1, buff2)) {
				break;
			}
		}
		if (read1 == -1) {
			printStats(fileName1, totalBytes, myBufferSize);
		} else {
			System.out.println("Files are not equal");
		}
		inputStream1.close();
		inputStream2.close();
	}
	
	/**
	 * Read given file using a file channel (nio)	
	 */
	public long readNio(String fileName, int myBufferSize,UpdateChart uc) throws IOException {
		FileChannel fChannel = null;
		FileInputStream fileInputStream=null;
		long totalBytes = 0;
		Timer tm = new Timer();
		double mseconds = 1;
		double megabytes = 1;
		long time=0;
		try {
			fileInputStream = new FileInputStream(fileName);
			fChannel = fileInputStream.getChannel();
			ByteBuffer buffer = ByteBuffer.allocateDirect(myBufferSize);
			int read;
			tm.start();
			long updateGraphCounter=1;
			final long modmask=0x00000000000000FF;
			while ((read = fChannel.read(buffer)) != -1) {
				if((updateGraphCounter&modmask)==0){
					time=tm.pause() - time;
					mseconds = time / 1000000d; //time in miliseconds (nano/10^6
					megabytes = (totalBytes / 1024) - megabytes;
					System.out.println(megabytes/mseconds);
					uc.updateData(megabytes/mseconds,updateGraphCounter);
					tm.resume();
				}
				buffer.clear();
				totalBytes += read;
				updateGraphCounter++;
			}
			return totalBytes;
		} finally {
			if (fChannel != null) {
				fChannel.close();
				fileInputStream.close();
			}
		}
	}
	
	public void compareNIO(String fileName1, String fileName2, int myBufferSize)
			throws IOException {
		FileChannel fChannel1 = null;
		FileChannel fChannel2 = null;
		long totalBytes = 0;
		timer.start();
		System.out.println("File compare benchmark using NIO");
		try {
			fChannel1 = new FileInputStream(fileName1).getChannel();
			fChannel2 = new FileInputStream(fileName2).getChannel();

			ByteBuffer buffer = ByteBuffer.allocateDirect(myBufferSize);
			ByteBuffer buffer2 = ByteBuffer.allocateDirect(myBufferSize);

			int read;
			int read2 = 0;
			while (	((read = fChannel1.read(buffer)) != -1)
					&& ((read2 = fChannel2.read(buffer2)) != -1)
					) {
				if(buffer.equals(buffer2) == false){
					System.out.println("files are not the same");
					break;
				}
				buffer.clear();
				buffer2.clear();
				totalBytes += 2*read;
			}
			if(read == -1 || read2 == -1){
				printStats(fileName1,totalBytes,myBufferSize);
			}
		} finally {
			if (fChannel1 != null && fChannel2 !=null) {
				fChannel1.close();
				fChannel2.close();
			}
		}

	}

	private void printStats(String fileName, long totalBytes, int myBufferSize) {
		NumberFormat nf = new DecimalFormat("#.00");
		final long time = timer.stop();
		double mseconds = time / 1000000d;
		double megabytes = totalBytes / 1024 / 1024;
		double rate = (megabytes) / mseconds * 1000;
		System.out.println("Done reading " + totalBytes + " bytes from file: "
				+ fileName + " in " + nf.format(mseconds) + " ms ("
				+ nf.format(rate) + "MB/sec)" + " with a buffer size of "
				+ myBufferSize / 1024 + " kB");

		// actual score (MBps)
		benchScore += rate;
	}
}