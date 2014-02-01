package rp3.data;

public class Message {
	
	public final static int INFORMATION_TYPE = 0;
	public final static int WARNING_TYPE = 1;
	public final static int ERROR_TYPE = 2;
	
	private int messageType = INFORMATION_TYPE;	
	
	private String text;
	private String title;
	private int key;
	
	public Message(){		
	}
	
	public Message(String text){
		setText(text);
	}	
	public Message(String text,String title){
		setText(text);
		setTitle(title);
	}	
	public Message(String text,String title,int type){
		setText(text);
		setTitle(title);
		setMessageType(type);
	}		
	public Message(String text,String title,int type,int key){
		setText(text);
		setTitle(title);
		setMessageType(type);
		setKey(key);
	}
	public Message(String text,int type){
		setText(text);		
		setMessageType(type);		
	}
	public int getKey(){
		return key;
	}
	public void setKey(int key){
		this.key = key;
	}	
	public int getMessageType() {
		return messageType;
	}
	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
}
