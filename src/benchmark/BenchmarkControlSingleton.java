package benchmark;

class SideThread extends Thread{
	private IBenchmark ttype;
	Object[] bopt;
	public SideThread(IBenchmark testType, Object... opt){
		this.ttype = testType;
		this.bopt = opt;
	}
	public void run(){
		ttype.run(bopt);
	}
}


public class BenchmarkControlSingleton {
	//singleton
	private static BenchmarkControlSingleton instance=null;
	private SideThread cthread=null;
	public static String[] getFileSizes(){
		String[] opt = {"1024M","2048M","4096M"};
		return opt;
	}

	public static String[] getBufferSizes(){
		String[] opt = {"4K","16K","64K"};
		return opt;
	}
	
	public static int sizeStringToInt(String s){
		if(s.length()<1)
			return 0;
		int mul=0;
		switch(s.charAt(s.length()-1)){
		case 'M':
			mul=1024*1024;//MB
			break;
		case 'K':
			mul=1024;
			break;
		}
		return Integer.parseInt(s.substring(0, s.length()-1))*mul;
	}
	
	public static long sizeStringToLong(String s){
		if(s.length()<1)
			return 0;
		long mul=0;
		switch(s.charAt(s.length()-1)){
		case 'M':
			mul=1024*1024;//MB
			break;
		case 'K':
			mul=1024;
			break;
		}
		return Long.parseLong(s.substring(0, s.length()-1))*mul;
	}
	
	public BenchmarkControlSingleton(){
		
	}
	
	public static BenchmarkControlSingleton getInstance(){
		if(instance==null){
			instance = new BenchmarkControlSingleton();
			return instance;
		}
		
		return instance;
	}
	
	public void runBenchmark(IBenchmark testType, Object... opt) throws BenchmarkBusyException{
		if(cthread!=null && cthread.isAlive()==true)
			throw new BenchmarkBusyException("Wait until the current I/O operation is finished");
		
		cthread = new SideThread(testType,opt);
		cthread.start();
	}
	
	public boolean busyBench(){
		return cthread.isAlive();
	}
}
