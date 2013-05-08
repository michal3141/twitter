package events;

import publishers.IPublisher;

public abstract class CrawlingEvent {
	
	private String info;
	private IPublisher source;
	
	public CrawlingEvent(String info, IPublisher source) {
		this.info = info;
		this.source = source;
	}
	
	public IPublisher getSource() {
		return source;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
	
	
}
