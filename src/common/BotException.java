package common;

public class BotException extends Exception{
	
	public BotException(String info){
		message = info;
	}
	
	public String toString(){
		return message;
	}

	String message;	
}