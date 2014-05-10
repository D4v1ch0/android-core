package rp3.data;

import java.util.ArrayList;
import java.util.List;

public class Dictionary<K,V> {

	List<K> keys;
	List<V> values;
	List<DictionaryEntry<K, V>> entries;
	
	public Dictionary(){	
		keys = new ArrayList<K>();
		values = new ArrayList<V>();
		entries = new ArrayList<DictionaryEntry<K,V>>();
	}
	
	public int getCount(){
		return keys.size();
	}
	
	public List<DictionaryEntry<K,V>> getEntries(){
		return entries;
	}
	
	public void set(K key, V value){
		if(!keys.contains(key)) {
			keys.add(key);		
			values.add(value);
			entries.add(new DictionaryEntry<K,V>(key, value));
			
		}else{
			int index = keys.indexOf(key);
			if(index>=0)
				values.set(index,value);
		}
	}
	
	public V get(K key){
		if(keys.contains(key)){
			int index = keys.indexOf(key);
			if(index>=0)
				return values.get(index);
		}
		return null;
	}
	
	public void remove(K key){
		if(keys.contains(key)){
			int location = keys.indexOf(key);
			keys.remove(location);
			values.remove(location);
			entries.remove(location);
		}		
	}
}
