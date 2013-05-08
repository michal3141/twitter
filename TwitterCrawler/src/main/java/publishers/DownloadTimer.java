package publishers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;

import listeners.IListener;
import events.CrawlingEvent;
import events.DownloadFinishedEvent;

import utils.Sec;

public class DownloadTimer implements IPublisher {
	
	private Collection<IListener> listeners = new ArrayList<>();
	private final Sec secondsToFinish;
	private Timer timer;
	
	public DownloadTimer(int val) {
		this.secondsToFinish = new Sec(val);
		timer = new Timer();
		timer.schedule(new TimerTask() {		
			public void run() {
				if (secondsToFinish.isZero()) {
					publishAll(new DownloadFinishedEvent("Download Finished", DownloadTimer.this));
					secondsToFinish.decrement();
				} else if (secondsToFinish.getVal() > 0){
					System.out.println("Remained downloading time : " + secondsToFinish.getVal());
					secondsToFinish.decrement();
				}
			}			
		}, 0, 1000);
	}
	
	public Timer getTimer() {
		return timer;
	}
 
	@Override
	public void publishAll(CrawlingEvent event) {
		for (IListener listener : listeners) {
			listener.publish(event);
		}	
	}

	@Override
	public void register(IListener listener) {
		listeners.add(listener);	
	}

	@Override
	public void unregister(IListener listener) {
		listeners.remove(listener);
	}
}
