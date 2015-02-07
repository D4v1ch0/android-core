package rp3.data;

import java.util.List;

import rp3.runtime.Session;

import android.text.TextUtils;

public class SimpleDictionary extends Dictionary<Long, String> {
	
	public SimpleDictionary() {
		super();
	}		
	
	public static SimpleDictionary getFromIdentifiables(List<? extends Identifiable> list){
		return getFromIdentifiables(list, false, null, null);
	}
	
	public static SimpleDictionary getFromIdentifiables(List<? extends Identifiable> list, boolean includeNullItem, String nullItemText){
		return getFromIdentifiables(list, includeNullItem, nullItemText, null);
	}
	
	public static SimpleDictionary getFromIdentifiables(List<? extends Identifiable> list, boolean includeNullItem, int resIDNullItemText){
		return getFromIdentifiables(list, includeNullItem, Session.getContext().getText(resIDNullItemText).toString(), null);
	}
	
	public static SimpleDictionary getFromIdentifiables(List<? extends Identifiable> list, String displayField){
		return getFromIdentifiables(list, false, null, displayField);
	}
	
	public static SimpleDictionary getFromIdentifiables(List<? extends Identifiable> list, boolean includeNullItem, int resIDNullItemText, String displayField){
		return getFromIdentifiables(list, includeNullItem, Session.getContext().getText(resIDNullItemText).toString(), displayField);
	}
	
	public static SimpleDictionary getFromIdentifiables(List<? extends Identifiable> list, boolean includeNullItem, String nullItemText, String displayField){
		SimpleDictionary dic = new SimpleDictionary();
		
		if(includeNullItem)
			dic.set((long)-1, nullItemText);
		
		for(Identifiable i : list){
			String desc = "";
			if(!TextUtils.isEmpty(displayField))
				desc = String.valueOf(i.getValue(displayField));
			else
				desc = i.getDescription();
			
			dic.set(i.getID(), desc);
		}
		
		return dic;
	}
}
