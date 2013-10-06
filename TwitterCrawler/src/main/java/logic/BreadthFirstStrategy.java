package logic;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import listeners.IListener;

import org.apache.log4j.PropertyConfigurator;

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
import utils.Text;
import ytharvest.factory.EntryFactoryFacade;
import ytharvest.factory.entities.ExtractedEntity;
import ytharvest.factory.entities.ExtractedUser;
import ytharvest.factory.entities.ExtractedVideo;
import ytharvest.factory.exceptions.HarvestException;
import ytharvest.properties.Properties;
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
	private static SQLBuilder builder;
	private static final int LEN = 63;
	private static EntryFactoryFacade facade;
	private static int currDepth=0;

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
			download_yt();
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
									builder.prepareSQL(user,null,((UserDto) node).getId(),"followers_id");
								}
							}
						} catch (TwitterException e) {
							e.printStackTrace();
						}
					}
				}
				if(relations.contains(Relation.FOLLOWS)){
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
									builder.prepareSQL(user,null,((UserDto) node).getId(),"friends_id");
								}
							}
						} catch (TwitterException e) {
							e.printStackTrace();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}					
				}
				if (relations.contains(Relation.MENTIONS)) {
					if (isCrawling) {
						System.out.println("Accessing API");
						try {
							ResponseList<Status> mentions = twitter.getMentionsTimeline();
							for (Status s : mentions) {
								if (isCrawling) {
									TweetDto tweet = new TweetDto();
									tweet.setTweetId(s.getId());
									tweet.setParentId(((UserDto) node).getId());
									String text = s.getText();
									text = Text.processText(text);
									tweet.setText(text);
									System.out.println("Obtaining mention : \n" + tweet.toString() + "at level " + currDepth + "\n");
									queue.add(tweet);
									builder.prepareSQL(tweet,null,0,"mentioned_id");
								}
							}
						} catch (TwitterException e) {
							e.printStackTrace();
						}
					}
				}
				if (relations.contains(Relation.REPLIES_TO)) {
					if (isCrawling) {
						try {
							Thread.sleep(3600000 / hitsPerHour);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						System.out.println("Afraid it's not implementable in any way...");
					}
				}
				if (relations.contains(Relation.HAS_TWEETS)) {
					if (isCrawling) {
						System.out.println("Accessing API");
						try {
							ResponseList<Status> tweets = twitter.getUserTimeline(((UserDto) node).getName());
							for (Status s : tweets) {
								if (isCrawling) {
									TweetDto tweet = new TweetDto();
									tweet.setTweetId(s.getId());
									tweet.setParentId(((UserDto) node).getId());
									String text = s.getText();
									text = Text.processText(text);
									tweet.setText(text);
									System.out.println("Obtaining tweet : \n" + tweet.toString() + "at level " + currDepth + "\n");
									queue.add(tweet);
									builder.prepareSQL(tweet,null,0,"has_tweets_id");
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
									String text = s.getText();
									text = Text.processText(text);
									tweet.setText(text);
									((TweetDto) node).getRetweets().add(tweet);
									System.out.println("Obtaining retweet : \n" + tweet.toString() + "at level " + currDepth + "\n");
									queue.add(tweet);
									builder.prepareSQL(tweet,null,0,"retweeted_id");
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
	
	
	public void download_yt(){
		try{
			init();

			System.out.println("Starting yt downloader..");

			String userName = "cris776";
			System.out.println("Before YT Crawling...");
		
//		UserYtDto user = new UserYtDto();
//		user.setName(userName);

	    	userWithUploads(userName);

	    	currDepth=0;
			userWithSubs(userName);
			currDepth=0;
			userWithFavs(userName);
			System.out.println("After YT Crawling...");
		}
		catch(Exception e){
			System.out.println("Exception download yt");
			e.printStackTrace();
		}
	}
	
	private static void init()
	{
		// log4jInit();
		PropertyConfigurator.configure("target/classes/log4j.properties");

		// YtHarvester
		Properties.configure("target/classes/harvest.properties");
		facade = new EntryFactoryFacade();
	}
	
	// Wypisanie upload'ów danego u¿ytkownika
		private static void userWithUploads(String userId) throws HarvestException, IOException
		{
			ExtractedUser user = facade.getUserEntry(userId);

//			Collection<ExtractedVideo> ups = facade.castEntities(
//					facade.getVideoFeed(user.getUploadedVideosLink()), ExtractedVideo.class);
			Collection<ExtractedVideo> ups = facade.getCastedVideoFeed(user.getUploadedVideosLink());

//			countUniques(ups);

			for (ExtractedEntity vid : ups){
				if(currDepth>10)
					break;
				++currDepth;
				builder.prepareSQL(user,vid,0,"uploaded");
			}
		}
		

		private static void userWithFavs(String userId) throws HarvestException, IOException
		{
			ExtractedUser user = facade.getUserEntry(userId);
			Collection<ExtractedVideo> ups = facade.castEntities(
					facade.getVideoFeed(user.getFavedVideosLink()), ExtractedVideo.class);
		
//			countUniques(ups);

			for (ExtractedVideo vid : ups){
				if(currDepth>10)
					break;
				++currDepth;
				builder.prepareSQL(user, vid ,0,"favs");
			}
		}


		private static void userWithSubs(String userId) throws HarvestException, IOException
		{	
			if(currDepth+7<10){
			++currDepth;
			ExtractedUser user = facade.getUserEntry(userId);
		//	NodeDto use= new UserYtDto();
	//		((UserYtDto) use).setName(user.getUserName());
	//		logEntity(user);
			
			Collection<ExtractedUser> subs = facade.castEntities(facade.getUserFeed(user.getSubscriptionsLink()), ExtractedUser.class);
			for (ExtractedUser u : subs){
				builder.prepareSQL(user, u,0, "users");
				userWithSubs(u.getUserName());
			}}
		}
	
	
	@Override
	public void publish(CrawlingEvent event) {
		if (event.getSource() == this.downloadTimer) {
			System.out.println(event.getInfo());
			isCrawling = false;
		}
	}

}
