package rp3.content;


import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import rp3.core.R;
import rp3.data.DictionaryEntry;
import rp3.data.Identifiable;
import rp3.data.SimpleDictionary;

public class SimpleDictionaryAdapter extends BaseAdapter {

	private SimpleDictionary data;
	
	private LayoutInflater inflater = null;
	private Context context;
	private int layoutResID;
	
	static class ViewHolder {		
		TextView textView_content;
	}
	
	public SimpleDictionaryAdapter(Context c, List<? extends Identifiable> data){		
		this.data = SimpleDictionary.getFromIdentifiables(data);
		this.context = c;
		this.inflater = LayoutInflater.from(context);
		layoutResID = R.layout.base_rowlist_simple_spinner;
	}
	
	public SimpleDictionaryAdapter(Context c, SimpleDictionary data){		
		this.data = data;
		this.context = c;
		this.inflater = LayoutInflater.from(context);
		layoutResID = R.layout.base_rowlist_simple_spinner;
	}	
	
	public SimpleDictionaryAdapter(Context c, SimpleDictionary data, int layoutResID){
		this.data = data;
		this.context = c;
		this.layoutResID = layoutResID;
		this.inflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {		
		return data.getCount();
	}

	@Override
	public Object getItem(int position) {			
		return data.getEntries().get(position);
	}

	@Override
	public long getItemId(int position) {		
		return data.getEntries().get(position).getKey();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		
		if (convertView == null) {
			convertView = inflater.inflate(layoutResID, null);
			
			holder = new ViewHolder();			
			holder.textView_content = (TextView)convertView.findViewById(R.id.textView_content);
			
			convertView.setTag(holder);
			
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		
		@SuppressWarnings("unchecked")
		DictionaryEntry<Long, String> current = (DictionaryEntry<Long,String>)getItem(position);
		if(current!=null){
			String value = current.getValue();
			holder.textView_content.setText(value);
		}
		
		return convertView;
	}

	
	
}
