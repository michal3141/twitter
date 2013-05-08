package publishers;

import listeners.IListener;
import events.CrawlingEvent;

public interface IPublisher {
	public void publishAll(CrawlingEvent event);
	public void register(IListener listener);
	public void unregister(IListener listener);
}
