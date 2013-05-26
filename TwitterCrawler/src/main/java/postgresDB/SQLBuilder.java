package postgresDB;

import dto.NodeDto;
import dto.UserDto;

public class SQLBuilder {

	private StringBuilder builder;
	
	public SQLBuilder(){
		builder= new StringBuilder("");
	}
	
	public void appendOne(String name){
		builder.append("INSERT INTO twit(name) VALUES ('"+name+"');\n");
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
	}
	
	public void prepareSQL(NodeDto node, long k, String relation){
		if(node instanceof UserDto){
			UserDto u=(UserDto)node;
			builder.append("INSERT INTO users(data,name,"+relation+") VALUES ("); 
			builder.append("'"+u.getLang()+"', '"+u.getName()+"',"+k+");\n");
		}
		else{
			System.out.println("Not implemented yet");
		}
	}
	
	public String getSQL(){
		return builder.toString();
	}
}
