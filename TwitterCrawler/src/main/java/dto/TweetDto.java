package dto;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="Tweets")
public class TweetDto extends NodeDto {
	
	/**
	 * Tweets that are responses to this tweet - possibly lack of thereof
	 */
	@OneToMany(cascade=CascadeType.ALL)
	private Collection<TweetDto> retweets = new ArrayList<>();
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	private long tweetId;

	public Collection<TweetDto> getRetweets() {
		return retweets;
	}

	public void setRetweets(Collection<TweetDto> retweets) {
		this.retweets = retweets;
	}

	public long getTweetId() {
		return tweetId;
	}

	public void setTweetId(long tweetId) {
		this.tweetId = tweetId;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("TweetId : " + tweetId + "\n");
		return sb.toString();
	}
}
