package config;

import java.util.Set;

import enums.Relation;
import enums.Strategy;


public class CrawlerConfiguration {
	
	/**
	 * The screen name of user or keyword from which crawler starts crawling
	 */
	private String seed;
	
	/**
	 * Depth of crawling
	 */
	private int depth;
	
	/**
	 * Due to rate limiting on Twitter, this property restricts number of
	 * calls to API per hour (roughly estimating)
	 */
	private int hitsPerHour;
	
	/**
	 * How many seconds the crawling will last
	 */
	private int crawlTime;
	
	/**
	 * What kind of relations are used to crawl Twitter
	 * @see Relation
	 */
	private Set<Relation> relations;
	
	/**
	 * One of three mains strategies to crawl Twitter:
	 * Breadth-First search strategy, Depth-First search strategy, Keyword search
	 */
	private Strategy strategy;

	public String getSeed() {
		return seed;
	}

	public void setSeed(String seed) {
		this.seed = seed;
	}

	public int getHitsPerHour() {
		return hitsPerHour;
	}

	public void setHitsPerHour(int hitsPerHour) {
		this.hitsPerHour = hitsPerHour;
	}

	public int getCrawlTime() {
		return crawlTime;
	}

	public void setCrawlTime(int crawlTime) {
		this.crawlTime = crawlTime;
	}

	public Set<Relation> getRelations() {
		return relations;
	}

	public void setRelations(Set<Relation> relations) {
		this.relations = relations;
	}

	public Strategy getStrategy() {
		return strategy;
	}

	public void setStrategy(Strategy strategy) {
		this.strategy = strategy;
	}
	
	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Crawler configuration is: \n");
		sb.append("seed : " + seed + "\n");
		sb.append("depth : " + depth + "\n");
		sb.append("hitsPerHour : " + hitsPerHour + "\n");
		sb.append("crawlTime : " + crawlTime + "\n");
		sb.append("strategy : " + strategy.toString() +"\n");
		sb.append("relations : " + "\n");
		for (Relation r : relations) {
			sb.append("\t" + r.toString() + "\n");
		}
		return sb.toString();
	}
	
}
