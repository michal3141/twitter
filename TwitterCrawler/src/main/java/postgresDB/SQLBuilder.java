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
        + "replied_id bigint,"
        + "mentioned_id bigint,"
        + "friends_id bigint,"
        + "followers_id bigint);\n");
		
		builder.append("DROP TABLE IF EXISTS tweets;\n");
		builder.append("CREATE TABLE tweets ("
		+ "id bigserial NOT NULL PRIMARY KEY,"
		+ "data VARCHAR(64),"
		+ "tweet_id bigint,"
		+ "mentioned_id bigint,"
		+ "has_tweets_id bigint,"
		+ "retweeted_id bigint);\n");
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
			builder.append("'"+t.getText()+"', '"+t.getTweetId()+"',"+t.getParentId()+");\n");
		}
	}
	
	public String getSQL(){
		return builder.toString();
	}
}
