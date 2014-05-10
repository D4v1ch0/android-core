package rp3.data;

public interface Identifiable {

	long getID();
	String getDescription();
	Object getValue(String key);	
}
