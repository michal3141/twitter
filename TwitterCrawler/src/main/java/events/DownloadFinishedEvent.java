package events;

import publishers.IPublisher;

public class DownloadFinishedEvent extends CrawlingEvent {
	
	public DownloadFinishedEvent(String info, IPublisher src) {
		super(info, src);
	}
}
