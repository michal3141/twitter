package logic;

import twitter4j.Twitter;
import config.CrawlerConfiguration;
import enums.Strategy;

public class TwitterDownloader extends Thread {
	
	private CrawlerConfiguration config;
	private Twitter twitter;
	
	public TwitterDownloader(CrawlerConfiguration config, Twitter twitter) {
		this.config = config;
		this.twitter = twitter;
	}
	
	public void run() {
		if (config.getStrategy() == Strategy.BREADTH_FIRST) {
			new BreadthFirstStrategy(this).execute();
		} else if (config.getStrategy() == Strategy.DEPTH_FIRST) {
			new DepthFirstStrategy(this).execute();
		} else if (config.getStrategy() == Strategy.KEYWORDS) {
			new KeywordsStrategy(this).execute();
		}
	}

	public CrawlerConfiguration getConfig() {
		return config;
	}

	public void setConfig(CrawlerConfiguration config) {
		this.config = config;
	}

	public Twitter getTwitter() {
		return twitter;
	}

	public void setTwitter(Twitter twitter) {
		this.twitter = twitter;
	}
	
	
}
