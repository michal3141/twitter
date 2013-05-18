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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="Users")
public class UserDto extends NodeDto {
	
	
	/**
	 * Users which are followed by this user
	 */
	@JoinColumn(nullable=true)
	@OneToMany(cascade=CascadeType.ALL)
	private Collection<UserDto> friends = new ArrayList<>();
	
	/**
	 * Users which follows this user
	 */
	@JoinColumn(nullable=true)
	@OneToMany(cascade=CascadeType.ALL)
	private Collection<UserDto> followers = new ArrayList<>();
	
	/**
	 * Users which are replied by this user in recent tweets
	 */
	@JoinColumn(nullable=true)
	@OneToMany(cascade=CascadeType.ALL)
	private Collection<UserDto> replied = new ArrayList<>();
	
	/**
	 * Users which are mentioned by this user in recent tweets
	 */
	@JoinColumn(nullable=true)
	@OneToMany(cascade=CascadeType.ALL)
	private Collection<UserDto> mentioned = new ArrayList<>();
	
	/**
	 * Tweets that are tweeted by this user
	 */
	@JoinColumn(nullable=true)
	@OneToMany(cascade=CascadeType.ALL)
	private Collection<TweetDto> tweets = new ArrayList<>();
	
	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY) 
	@Column
	private long id;
	/**
	 * Screen name for this user
	 */
	private String name;
	
	/**
	 * Preferred language for this user
	 */
	private String lang;

	public Long getId() {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserDto other = (UserDto) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	
		
}
