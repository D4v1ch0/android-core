package rp3.data;

public class DictionaryEntry<K,V> {

	public DictionaryEntry(){		
	}
	
	public DictionaryEntry(K key,V value){
		this.key = key;
		this.value = value;
	}
	
	private K key;
	private V value;
	
	public K getKey(){
		return key;
	}
	
	public void setKey(K key){
		this.key = key;
	}
	
	public V getValue(){
		return value;
	}
	
	public void setValue(V value){
		this.value = value;
	}
	
}
