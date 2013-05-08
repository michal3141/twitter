package exceptions;

public class InvalidConfigException extends Exception {
	
	private String reason;
	public InvalidConfigException(String reason) {
		this.reason = reason;
	}
	
	public String getMessage() {
		return "InvalidConfigException during parsing. Reason: " + reason;	
	}
}
