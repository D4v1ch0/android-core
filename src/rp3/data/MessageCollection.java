package rp3.data;

import java.util.ArrayList;
import java.util.List;

public class MessageCollection {

	private List<Message> messages;
	
	public List<Message> getMessages(){
		if(messages==null)messages = new ArrayList<Message>();
		return messages;
	}
	
	public int getCuount(){
		return getMessages().size();
	}
	
	public boolean hasErrorMessage(){
		for(Message m: getMessages()){
			if(m.getMessageType() == Message.ERROR_TYPE)
				return true;
		}
		return false;
	}
	
	public void clear(){
		getMessages().clear();
	}
	
	public void addMessage(Message m){
		getMessages().add(m);
	}
	
	public void addMessage(String text){
		getMessages().add( new Message(text) );
	}
	
	public void addMessage(String text, String title){
		getMessages().add( new Message(text, title) );
	}
	
	public void addMessage(String text, String title, int type){
		getMessages().add( new Message(text, title, type) );
	}
	
	public void addMessage(String text, String title, int type, int key){
		getMessages().add( new Message(text, title, type, key) );
	}
	
	public Message getMessage(int key){
		for(Message m: getMessages()){
			if(key == m.getKey())
				return m;
		}
		return null;
	}
	
	public void removeMessage(int key){
		Message remove = null;
		for(Message m: getMessages()){
			if(key == m.getKey()){
				remove = m;
				break;
			}
		}
		if(remove!=null) getMessages().remove(remove);
	}
}
