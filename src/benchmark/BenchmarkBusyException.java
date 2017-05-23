package benchmark;

public class BenchmarkBusyException extends Exception{

	private static final long serialVersionUID = 1L;
	public BenchmarkBusyException(String msg){
		super(msg);
	}
}