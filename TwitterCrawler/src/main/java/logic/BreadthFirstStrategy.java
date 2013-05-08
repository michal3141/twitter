package logic;

import publishers.DownloadTimer;
import publishers.IPublisher;
import events.CrawlingEvent;
import listeners.IListener;

public class BreadthFirstStrategy implements IStrategy, IListener {

	private TwitterDownloader context;
	private boolean isCrawling = true;
	private IPublisher downloadTimer;
	
	public BreadthFirstStrategy(TwitterDownloader context) {
		this.context = context;
	}
		
	@Override
	public void execute() {
		downloadTimer = new DownloadTimer(context.getConfig().getCrawlTime());
		downloadTimer.register(this);
		
		System.out.println("Before Crawling...");
		
		while (isCrawling) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(isCrawling);
		}
		
		((DownloadTimer) downloadTimer).getTimer().cancel();
		System.out.println("After Crawling...");
	}
	
	@Override
	public void publish(CrawlingEvent event) {
		if (event.getSource() == this.downloadTimer) {
			System.out.println(event.getInfo());
			isCrawling = false;
		}
	}

}
