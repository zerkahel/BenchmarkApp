/**
 * 
 */
package timing;

/**
 * @author alxtsa
 *
 */
public class Timer implements ITimer {
	private long time,tempTime;

	/**
	 * Init timer
	 */
	public Timer() {
		time=0;
		tempTime=0;
	}
	
	/* (non-Javadoc)
	 * @see timing.ITimer#start()
	 */
	@Override
	public void start() {
		time = System.nanoTime();
	}

	/* (non-Javadoc)
	 * @see timing.ITimer#stop()
	 */
	@Override
	public long stop() {
		return System.nanoTime()-time;
	}

	/* (non-Javadoc)
	 * @see timing.ITimer#pause()
	 */
	@Override
	public long pause() {
		tempTime=System.nanoTime();
		return System.nanoTime()-time;
	}

	/* (non-Javadoc)
	 * @see timing.ITimer#resume()
	 */
	@Override
	public void resume() {
		time += System.nanoTime()-tempTime;
	}

}
