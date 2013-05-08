package listeners;

import events.CrawlingEvent;

public interface IListener {
	public void publish(CrawlingEvent event);
}
