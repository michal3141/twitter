package postgresDB;

import dto.NodeDto;
import dto.TweetDto;
import dto.UserDto;

public class SQLBuilder {

	private StringBuilder builder;
	
	public SQLBuilder(){
		builder= new StringBuilder("");
	}
	
	public void appendOne(String name){
		builder.append("INSERT INTO users(name) VALUES ('"+name+"');\n");
	}
	
	public void generateTables(){
		
		builder.append("DROP TABLE IF EXISTS users;\n");
		builder.append("CREATE TABLE users ("
        + "id bigserial NOT NULL PRIMARY KEY,"
        + "data VARCHAR(64), " 
        + "name VARCHAR(256),"
        + "replied_id integer,"
        + "mentioned_id integer,"
        + "friends_id integer,"
        + "followers_id integer);\n");
		
		builder.append("DROP TABLE IF EXISTS tweets;\n");
		builder.append("CREATE TABLE tweets ("
		+ "id bigserial NOT NULL PRIMARY KEY,"
		+ "data VARCHAR(64),"
		+ "tweet_id integer,"
		+ "mentioned_id integer,"
		+ "has_tweets_id integer,"
		+ "retweeted_id integer);\n");
	}
	
	public void prepareSQL(NodeDto node, long k, String relation){
		if(node instanceof UserDto){
			UserDto u=(UserDto)node;
			builder.append("INSERT INTO users(data,name,"+relation+") VALUES ("); 
			builder.append("'"+u.getLang()+"', '"+u.getName()+"',"+k+");\n");
		}
		else{
			TweetDto t = (TweetDto) node;
			builder.append("INSERT INTO tweets(data,tweet_id,"+relation+") VALUES (");
			builder.append("'"+"t"+"', '"+t.getTweetId()+"',"+t.getParentId()+");\n");
		}
	}
	
	public String getSQL(){
		return builder.toString();
	}
}
