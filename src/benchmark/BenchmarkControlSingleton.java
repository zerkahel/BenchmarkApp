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
		String[] opt = {"16M","256M","4096M"};
		return opt;
	}

	public static String[] getBufferSizes(){
		String[] opt = {"4K","16K","512K"};
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
	
	public BenchmarkControlSingleton(){
		
	}
	
	public static BenchmarkControlSingleton getInstance(){
		if(instance==null)
			return new BenchmarkControlSingleton();
		
		return instance;
	}
	
	public void runBenchmark(IBenchmark testType, Object... opt) throws BenchmarkBusyException{
		if(cthread!=null && cthread.isAlive()==true)
			throw new BenchmarkBusyException("Wait a sec");
		
		cthread = new SideThread(testType,opt);
		cthread.start();
	}
}
