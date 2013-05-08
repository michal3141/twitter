package dto;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="Users")
public class UserDto extends NodeDto {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	/**
	 * Users which are followed by this user
	 */
	@OneToMany(cascade=CascadeType.ALL)
	private Collection<UserDto> friends = new ArrayList<>();
	
	/**
	 * Users which follows this user
	 */
	@OneToMany(cascade=CascadeType.ALL)
	private Collection<UserDto> followers = new ArrayList<>();
	
	/**
	 * Users which are replied by this user in recent tweets
	 */
	@OneToMany(cascade=CascadeType.ALL)
	private Collection<UserDto> replied = new ArrayList<>();
	
	/**
	 * Users which are mentioned by this user in recent tweets
	 */
	@OneToMany(cascade=CascadeType.ALL)
	private Collection<UserDto> mentioned = new ArrayList<>();
	
	/**
	 * Tweets that are tweeted by this user
	 */
	@OneToMany(cascade=CascadeType.ALL)
	private Collection<TweetDto> tweets = new ArrayList<>();
	
	/**
	 * Screen name for this user
	 */
	private String name;
	
	/**
	 * Preferred language for this user
	 */
	private String lang;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Collection<UserDto> getFriends() {
		return friends;
	}

	public void setFriends(Collection<UserDto> friends) {
		this.friends = friends;
	}

	public Collection<UserDto> getFollowers() {
		return followers;
	}

	public void setFollowers(Collection<UserDto> followers) {
		this.followers = followers;
	}

	public Collection<UserDto> getReplied() {
		return replied;
	}

	public void setReplied(Collection<UserDto> replied) {
		this.replied = replied;
	}

	public Collection<UserDto> getMentioned() {
		return mentioned;
	}

	public void setMentioned(Collection<UserDto> mentioned) {
		this.mentioned = mentioned;
	}

	public Collection<TweetDto> getTweets() {
		return tweets;
	}

	public void setTweets(Collection<TweetDto> tweets) {
		this.tweets = tweets;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Name : " + name + "\n");
		sb.append("Lang : " + lang + "\n");
		return sb.toString();
	}
		
}
