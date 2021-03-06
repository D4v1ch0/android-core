package rp3.content;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import rp3.core.R;
import rp3.data.Identifiable;

public class SimpleIdentifiableAdapter extends BaseAdapter {

	private List<? extends Identifiable> data;
	private LayoutInflater inflater = null;
	private Context context;
	private String contentKey;
	private int layoutResID;
	
	static class ViewHolder {
		TextView textView_content;
	}
	
	public SimpleIdentifiableAdapter(Context c, List<? extends Identifiable> data){
		this.data = data;
		this.context = c;
		this.inflater = LayoutInflater.from(context);
		layoutResID = R.layout.base_rowlist_simple_spinner;
	}	
	
	
	public SimpleIdentifiableAdapter(Context c, List<? extends Identifiable> data, String contentKey){
		this.data = data;
		this.context = c;
		this.contentKey = contentKey;
		this.inflater = LayoutInflater.from(context);
		layoutResID = R.layout.base_rowlist_simple_spinner; 
	}
	
	public SimpleIdentifiableAdapter(Context c, List<? extends Identifiable> data, int layoutResID){
		this.data = data;
		this.context = c;
		this.layoutResID = layoutResID;
		this.inflater = LayoutInflater.from(context);
	}
	
	public SimpleIdentifiableAdapter(Context c, List<Identifiable> data, int layoutResID, String contentKey){
		this.data = data;
		this.context = c;
		this.layoutResID = layoutResID;
		this.contentKey = contentKey;
		this.inflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {		
		return data.size();
	}

	@Override
	public Object getItem(int position) {		
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {		
		return data.get(position).getID();
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
		
		Identifiable current = (Identifiable)getItem(position);
		if(current!=null){
			String value = current.getDescription();
			if(!TextUtils.isEmpty(contentKey))
				value = (String) current.getValue(contentKey);
				
			holder.textView_content.setText(value);
		}
		
		return convertView;
	}

	
	
}
