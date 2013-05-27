package logic;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import listeners.IListener;
import postgresDB.Persistor;
import postgresDB.SQLBuilder;
import publishers.DownloadTimer;
import publishers.IPublisher;
import twitter4j.PagableResponseList;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import dto.NodeDto;
import dto.TweetDto;
import dto.UserDto;
import enums.Relation;
import events.CrawlingEvent;

public class BreadthFirstStrategy implements IStrategy, IListener {

	private TwitterDownloader context;
	private boolean isCrawling = true;
	private IPublisher downloadTimer;
	private String screenName;
	private Twitter twitter;
	private Queue<NodeDto> queue;
	private int depth;
	private int hitsPerHour;
	private int crawlTime;
	private Set<Relation> relations;
	private SQLBuilder builder;
	
	public BreadthFirstStrategy(TwitterDownloader context) {
		this.context = context;
		twitter = context.getTwitter();
		screenName = context.getConfig().getSeed();
		queue= new LinkedList<NodeDto>();
		depth = context.getConfig().getDepth();
		hitsPerHour = context.getConfig().getHitsPerHour();
		crawlTime = context.getConfig().getCrawlTime();
		relations = context.getConfig().getRelations();
		builder= new SQLBuilder();
	}
		
	@Override
	public void execute() {
		downloadTimer = new DownloadTimer(crawlTime);
		downloadTimer.register(this);
		
		System.out.println("Before Crawling...");
		UserDto user= new UserDto();
		user.setName(screenName);
		builder.generateTables();
		builder.appendOne(user.getName());
		queue.add(user);
		
		if (isCrawling){
			bfs_twit();
		}
		
		((DownloadTimer) downloadTimer).getTimer().cancel();
		System.out.println("After Crawling...");
		
		System.out.println("Saving to database...");
	//	Persistor.save(user);
		Persistor.save1(builder.getSQL());
		System.out.println("Saving to database finished.");
	}
	
	public void bfs_twit(){
		
		int currDepth= 0;

		while(!queue.isEmpty()){
			if(currDepth>depth){
				System.out.println("In your head zombie zombie you know;p");
				break;
			}
			try {
				Thread.sleep(3600000 / hitsPerHour);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			NodeDto node = queue.remove();			
			if (node instanceof UserDto) {
				if (relations.contains(Relation.FOLLOWED_BY)) {
					if(isCrawling){
						System.out.println("Accessing API");
						try {
							PagableResponseList<User> users = twitter.getFollowersList(((UserDto) node).getName(), -1);
							for(User u: users){
								if(isCrawling){
									UserDto user = new UserDto();
									user.setName(u.getScreenName());
									user.setLang(u.getLang());
									((UserDto) node).getFollowers().add(user);
									System.out.println("Obtaining follower : \n" + user.toString() + 
											"at level " + currDepth + "\n");
									queue.add(user);
									builder.prepareSQL(user,((UserDto) node).getId(),"followers_id");
								}
							}
						} catch (TwitterException e) {
							e.printStackTrace();
						}
					}
				}
				else if(relations.contains(Relation.FOLLOWS)){
					if(isCrawling){
						System.out.println("Accessing API");
						try {
							Thread.sleep(3600000 / hitsPerHour);
							
							PagableResponseList<User> users = twitter.getFriendsList(((UserDto) node).getName(), -1);
							for(User u: users){
								if(isCrawling){
									UserDto user = new UserDto();
									user.setName(u.getScreenName());
									user.setLang(u.getLang());
									((UserDto) node).getFriends().add(user);
									System.out.println("Obtaining friend : \n" + user.toString() +
											   "at level " + currDepth + "\n");
									queue.add(user);
									builder.prepareSQL(user,((UserDto) node).getId(),"friends_id");
								}
							}
						} catch (TwitterException e) {
							e.printStackTrace();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}					
				}
				else if (relations.contains(Relation.MENTIONS)) {
				if (isCrawling) {
					System.out.println("Accessing API");
					try {
						ResponseList<Status> mentions = twitter.getMentionsTimeline();
						for (Status s : mentions) {
							if (isCrawling) {
								TweetDto tweet = new TweetDto();
								tweet.setTweetId(s.getId());
								tweet.setParentId(((UserDto) node).getId());
								System.out.println("Obtaining mention : \n" + tweet.toString() + "at level " + currDepth + "\n");
								queue.add(tweet);
								builder.prepareSQL(tweet,0,"mentioned_id");
							}
						}
					} catch (TwitterException e) {
						e.printStackTrace();
					}
				}
			}
			else if (relations.contains(Relation.REPLIES_TO)) {
				if (isCrawling) {
					try {
						Thread.sleep(3600000 / hitsPerHour);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("Afraid it's not implementable in any way...");
				}
			}
			else if (relations.contains(Relation.HAS_TWEETS)) {
				if (isCrawling) {
					System.out.println("Accessing API");
					try {
						ResponseList<Status> tweets = twitter.getUserTimeline(((UserDto) node).getName());
						for (Status s : tweets) {
							if (isCrawling) {
								TweetDto tweet = new TweetDto();
								tweet.setTweetId(s.getId());
								tweet.setParentId(((UserDto) node).getId());
								System.out.println("Obtaining tweet : \n" + tweet.toString() + "at level " + currDepth + "\n");
								queue.add(tweet);
								builder.prepareSQL(tweet,0,"has_tweets_id");
							}
						}
					} catch (TwitterException e) {
						e.printStackTrace();
					}
				}
			}	
			}
			else if(node instanceof TweetDto){
				if (relations.contains(Relation.RETWEETS)) {
					if(isCrawling){
						System.out.println("Accessing API");
						try {
							ResponseList<Status> statuses = twitter.getRetweets(((TweetDto) node).getTweetId());
							for(Status s: statuses){
								if(isCrawling){
									TweetDto tweet = new TweetDto();
									tweet.setTweetId(s.getId());
									tweet.setParentId(((TweetDto) node).getTweetId());
									((TweetDto) node).getRetweets().add(tweet);
									System.out.println("Obtaining retweet : \n" + tweet.toString() + "at level " + currDepth + "\n");
									queue.add(tweet);
									builder.prepareSQL(tweet,0,"retweeted_id");
								}
							}
						} catch (TwitterException e) {
							e.printStackTrace();
						}
					}
				}
			}
			++currDepth;
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
