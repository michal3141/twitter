package consoleapp;

import logic.TwitterDownloader;
import config.ConfigParser;
import config.CrawlerConfiguration;
import twitter4j.PagableResponseList;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

public class Test {
	
	private static ConfigurationBuilder cb;
	
	public static void main(String[] args) {

		setUpAuthorization();
		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();
		CrawlerConfiguration config = ConfigParser.fromXML("configs.xml");
		ConfigParser.toXML(config, "configs.xml");
		System.out.println(config.toString());
		TwitterDownloader downloader = new TwitterDownloader(config, twitter);
		downloader.start();
		try {
			downloader.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// hackItUp(twitter);
	}

	private static void setUpAuthorization() {
		cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		  .setOAuthConsumerKey("WvPPmJPjNVWpDmcUimkvg")
		  .setOAuthConsumerSecret("zAp8jTbPNT5jSaoOm5rfMeE8uLXh4F4APdvAFitVVYo")
		  .setOAuthAccessToken("1172898295-Sji9i1YJ9iHhTSlsW0Gldj3JShPIp5G6hmNZaIW")
		  .setOAuthAccessTokenSecret("RdJxjRrVv41sGu4hMdQwk7AP7MllUoufyLdsPWMCKE");
	}
	
	private static void hackItUp(Twitter twitter) {
		try {
			ResponseList<Status> statuses = twitter.getUserTimeline("michal3141", new Paging(1,20));
			for (Status s : statuses) {
				System.out.println(s.getText());
			}
			
			PagableResponseList<User> followers;
			
			long cursor = -1;
			do {
				followers = twitter.getFollowersList("ChessExplained", cursor);
				for (User u : followers) {
					System.out.println(u.getScreenName());
				}
			} while ((cursor = followers.getNextCursor()) != 0);
			
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		
	}
}
