package logic;

import java.util.Set;

import dto.NodeDto;
import dto.TweetDto;
import dto.UserDto;
import enums.Relation;
import events.CrawlingEvent;
import listeners.IListener;
import postgresDB.Persistor;
import publishers.DownloadTimer;
import publishers.IPublisher;
import twitter4j.PagableResponseList;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

public class DepthFirstStrategy implements IStrategy, IListener {

	private TwitterDownloader context;
	private boolean isCrawling = true;
	private IPublisher downloadTimer;
	private Twitter twitter;
	private String screenName;
	private int depth;
	private int hitsPerHour;
	private int crawlTime;
	private Set<Relation> relations;
	
	public DepthFirstStrategy(TwitterDownloader context) {
		this.context = context;
		twitter = context.getTwitter();
		screenName = context.getConfig().getSeed();
		depth = context.getConfig().getDepth();
		hitsPerHour = context.getConfig().getHitsPerHour();
		crawlTime = context.getConfig().getCrawlTime();
		relations = context.getConfig().getRelations();
	}
	
	@Override
	public void execute() {
		downloadTimer = new DownloadTimer(crawlTime);
		downloadTimer.register(this);
		
		System.out.println("Before Crawling...");
		
		UserDto user = new UserDto();
		user.setName(screenName);
		
		while (isCrawling) {
			visit(user, 1);
		}
		
		((DownloadTimer) downloadTimer).getTimer().cancel();
		System.out.println("After Crawling...");
		
		System.out.println("Saving to database...");
		Persistor.save(user);
		System.out.println("Saving to database finished.");
	}

	public void visit(NodeDto node, int currDepth) {
		if (currDepth <= depth && isCrawling) {			
			try {
				// For rate limit policy of Twitter
				Thread.sleep(3600000 / hitsPerHour);
				
				if (isCrawling && node instanceof TweetDto && relations.contains(Relation.RETWEETS)) {
					System.out.println("Accessing API");
					ResponseList<Status> statuses = twitter.getRetweets(((TweetDto) node).getTweetId());
					for (Status s : statuses) {
						if (isCrawling) {
							TweetDto tweet = new TweetDto();
							tweet.setTweetId(s.getId());
							((TweetDto) node).getRetweets().add(tweet);
							System.out.println("Obtaining retweet : \n" + tweet.toString() + "at level " + currDepth + "\n");
							visit(tweet, currDepth + 1);
						}
					}
				} else if (node instanceof UserDto) {
					if (relations.contains(Relation.FOLLOWED_BY)) {
						if (isCrawling) {
							System.out.println("Accessing API");
							PagableResponseList<User> users = twitter.getFollowersList(((UserDto) node).getName(), -1);
							for (User u : users) {
								if (isCrawling) {
									UserDto user = new UserDto();
									user.setName(u.getScreenName());
									user.setLang(u.getLang());
									((UserDto) node).getFollowers().add(user);
									System.out.println("Obtaining follower : \n" + user.toString() + 
													   "at level " + currDepth + "\n");
									visit(user, currDepth + 1);
								}
							}
						}
					} else if (relations.contains(Relation.FOLLOWS)) {
						if (isCrawling) {
							Thread.sleep(3600000 / hitsPerHour);
							System.out.println("Accessing API");
							PagableResponseList<User> users = twitter.getFriendsList(((UserDto) node).getName(), -1);
							for (User u : users) {
								if (isCrawling) {
									UserDto user = new UserDto();
									user.setName(u.getScreenName());
									user.setLang(u.getLang());
									((UserDto) node).getFriends().add(user);
									System.out.println("Obtaining friend : \n" + user.toString() +
													   "at level " + currDepth + "\n");
									visit(user, currDepth + 1);
								}
							}
						}
					} else if (relations.contains(Relation.MENTIONS)) {
						if (isCrawling) {
							// Thread.sleep(3600000 / hitsPerHour);
							//TODO: write some nasty code here
						}
					} else if (relations.contains(Relation.REPLIES_TO)) {
						if (isCrawling) {
							// Thread.sleep(3600000 / hitsPerHour);
							//TODO: write some nasty code here
						}
					} else if (relations.contains(Relation.HAS_TWEETS)) {
						if (isCrawling) {
							// Thread.sleep(3600000 / hitsPerHour);
							//TODO: write some nasty code here
						}
					} 			
				} 
				
			} catch (TwitterException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void publish(CrawlingEvent event) {
		if (event.getSource() == this.downloadTimer) {
			System.out.println(event.getInfo());
			isCrawling = false;
		}
	}

}
