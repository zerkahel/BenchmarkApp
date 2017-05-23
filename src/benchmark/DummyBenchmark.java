/**
 * 
 */
package benchmark;

import benchmark.hdd.HDDRandomAccess;
import benchmark.hdd.HDDReadSpeed;
import benchmark.hdd.HDDReadSpeed.ReadOptions;
import benchmark.hdd.HDDWriteSpeed;
import logging.ILogger;
import timing.ITimer;
/**
 * @author alxtsa
 *
 */
public class DummyBenchmark implements IBenchmark {

	@SuppressWarnings("unused")
	private long x;
	int maxVal;
	public void init(int max) {
		maxVal = max;
	}

	/* (non-Javadoc)
	 * @see benchmark.IBenchmark#run()
	 */
	@Override
	public void run() {		
		x = 0;
		for(int i=0;i<maxVal;++i) {
			x*=i;
		}
	}

	/* (non-Javadoc)
	 * @see benchmark.IBenchmark#runAndLog(logging.ILogger, timing.ITimer)
	 */
	@Override
	public void runAndLog(ILogger logger, ITimer timer) {
		// TODO Auto-generated method stub

	}

	public void clean(){
		//nothing to clean
	}

	public static void main(String[] args) {

/*		HDDReadSpeed hrs = new HDDReadSpeed();		
		hrs.run(ReadOptions.STREAM);
		hrs.run(ReadOptions.NIO);
		
		HDDRandomAccess hra = new HDDRandomAccess();
		hra.initialize((long)3000*1024*1024);
		hra.run("r","fs",4*1024);
		System.out.println(hra.getResult());
		hra.run("r","ft",4*1024);
		System.out.println(hra.getResult());
		hra.run("w","fs",4*1024);
		System.out.println(hra.getResult());
		hra.run("w","ft",4*1024);
		System.out.println(hra.getResult());*/
		
		HDDWriteSpeed hws = new HDDWriteSpeed();
		hws.run("fs",true);
		hws.run("fb",true);
	}
}
