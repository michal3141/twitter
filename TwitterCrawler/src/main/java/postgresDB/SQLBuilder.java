package postgresDB;

import ytharvest.factory.entities.ExtractedEntity;
import ytharvest.factory.entities.ExtractedUser;
import ytharvest.factory.entities.ExtractedVideo;
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
		
		//yt
		builder.append("DROP TABLE IF EXISTS usersYt;\n");
		builder.append("CREATE TABLE usersYt ("
        + "id bigserial NOT NULL PRIMARY KEY,"
        + "name VARCHAR(64), " 
        + "subscribes VARCHAR(256));\n");
		
		builder.append("DROP TABLE IF EXISTS uploaded;\n");
		builder.append("CREATE TABLE uploaded ("
        + "id bigserial NOT NULL PRIMARY KEY,"
        + "name VARCHAR(256), " 
        + "upload VARCHAR(256), "
        + "category VARCHAR(256));\n");
		
		builder.append("DROP TABLE IF EXISTS favourite;\n");
		builder.append("CREATE TABLE favourite ("
        + "id bigserial NOT NULL PRIMARY KEY,"
        + "name VARCHAR(256), " 
        + "favs VARCHAR(256), "
        + "category VARCHAR(256));\n");
	}
	
	public void prepareSQL(NodeDto node, NodeDto node2, long k, String relation){
		if(node instanceof UserDto){
			UserDto u=(UserDto)node;
			builder.append("INSERT INTO users(data,name,"+relation+") VALUES ("); 
			builder.append("'"+u.getLang()+"', '"+u.getName()+"',"+k+");\n");
		}
		else if(node instanceof TweetDto){
			TweetDto t = (TweetDto) node;
			builder.append("INSERT INTO tweets(data,tweet_id,"+relation+") VALUES (");
			builder.append("'"+t.getText()+"', '"+t.getTweetId()+"',"+t.getParentId()+");\n");
		}
/*		else if(node instanceof ExtractedEntity){
			if(relation=="users" && node instanceof ExtractedUser){
				builder.append("INSERT INTO users(name,subscribes) VALUES ("); 
				builder.append("'"+((ExtractedUser)node).getUserName()+"', '"+((ExtractedUser)node2).getUserName()+"');\n");
			}else if(relation=="uploaded"){
				builder.append("INSERT INTO uploaded(name,upload,category) VALUES ("); 
				builder.append("'"+((ExtractedUser)node).getUserName()+"' ,'"+((ExtractedVideo)node2).getVideoId()+"', '"+((ExtractedVideo)node2).getCategory()+"');\n");
			}
			else if(relation=="favs"){
				builder.append("INSERT INTO favourite(name,favs,category) VALUES ("); 
				builder.append("'"+((ExtractedUser)node).getUserName()+"' ,'"+((ExtractedVideo)node2).getVideoId()+"', '"+((ExtractedVideo)node2).getCategory()+"');\n");
			}
		}*/
		else{
			System.out.println("Any oder??");
		}
	}
	
	public void prepareSQL(ExtractedEntity node, ExtractedEntity node2, long k, String relation){
		if(node instanceof ExtractedEntity){
			if(relation=="users" && node instanceof ExtractedUser){
				builder.append("INSERT INTO usersYt(name,subscribes) VALUES ("); 
				builder.append("'"+((ExtractedUser)node).getUserName()+"', '"+((ExtractedUser)node2).getUserName()+"');\n");
			}else if(relation=="uploaded"){
				builder.append("INSERT INTO uploaded(name,upload,category) VALUES ("); 
				builder.append("'"+((ExtractedUser)node).getUserName()+"' ,'"+((ExtractedVideo)node2).getVideoId()+"', '"+((ExtractedVideo)node2).getCategory()+"');\n");
			}
			else if(relation=="favs"){
				builder.append("INSERT INTO favourite(name,favs,category) VALUES ("); 
				builder.append("'"+((ExtractedUser)node).getUserName()+"' ,'"+((ExtractedVideo)node2).getVideoId()+"', '"+((ExtractedVideo)node2).getCategory()+"');\n");
			}
		}else{
			System.out.println("omg please yt");
		}
	}
	
	public String getSQL(){
		return builder.toString();
	}
}
