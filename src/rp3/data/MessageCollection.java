package rp3.data;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;


public class MessageCollection implements Parcelable {

	private List<Message> messages;
	
	public MessageCollection() {		
	}
	
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
	
	public void addMessage(String text, int type){
		getMessages().add( new Message(text, type) );
	}
	
	public void addErrorMessage(String text){
		getMessages().add( new Message(text, Message.ERROR_TYPE) );
	}
	
	public void addErrorMessage(String text, String title){
		getMessages().add( new Message(text, title, Message.ERROR_TYPE) );
	}
	
	public void addWarningMessage(String text){
		getMessages().add( new Message(text, Message.WARNING_TYPE) );
	}
	
	public void addWarningMessage(String text, String title){
		getMessages().add( new Message(text, title, Message.WARNING_TYPE) );
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

	@Override
	public int describeContents() {		
		return 0;
	}

	@Override
	public void writeToParcel(Parcel in, int arg1) {		
		in.writeList(messages);
	}
	
	public MessageCollection(Parcel in){
		this.messages = new ArrayList<Message>();
		in.readList(this.messages, getClass().getClassLoader());
	}
	
	

	public static final Parcelable.Creator<MessageCollection> CREATOR = new Parcelable.Creator<MessageCollection>() {
		 public MessageCollection createFromParcel(Parcel in) {
			 return new MessageCollection(in);
		 }

		 public MessageCollection[] newArray(int size) {
			 return new MessageCollection[size];
		 }
	 };
}
