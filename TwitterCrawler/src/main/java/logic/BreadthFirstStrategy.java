package logic;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import listeners.IListener;
import publishers.DownloadTimer;
import publishers.IPublisher;
import twitter4j.PagableResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import dto.NodeDto;
import dto.UserDto;
import enums.Relation;
import events.CrawlingEvent;

public class BreadthFirstStrategy implements IStrategy, IListener {

	private TwitterDownloader context;
	private boolean isCrawling = true;
	private IPublisher downloadTimer;
	private String screenName;
	private Twitter twitter;
	private Queue queue;
	private int depth;
	private int hitsPerHour;
	private int crawlTime;
	private Set<Relation> relations;
	
	public BreadthFirstStrategy(TwitterDownloader context) {
		this.context = context;
		screenName = context.getConfig().getSeed();
		queue= new LinkedList();
		depth = context.getConfig().getDepth();
		hitsPerHour = context.getConfig().getHitsPerHour();
		crawlTime = context.getConfig().getCrawlTime();
		relations = context.getConfig().getRelations();
	}
		
	@Override
	public void execute() {
		downloadTimer = new DownloadTimer(context.getConfig().getCrawlTime());
		downloadTimer.register(this);
		
		System.out.println("Before Crawling...");
		UserDto user= new UserDto();
		user.setName(screenName);
		queue.add(user);
		
		if (isCrawling){
			bfs_twit();
		}
		
		((DownloadTimer) downloadTimer).getTimer().cancel();
		System.out.println("After Crawling...");
	}
	
	public void bfs_twit(){
		
		int currDepth= 0;

		while(!queue.isEmpty()){
			NodeDto node= (NodeDto)queue.remove();			
			if (node instanceof UserDto) {
				if (relations.contains(Relation.FOLLOWED_BY)) {
					System.out.println("Accessing API");
					try {
						PagableResponseList<User> users = twitter.getFollowersList(((UserDto) node).getName(), -1);
					} catch (TwitterException e) {
						e.printStackTrace();
					}
				}
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
