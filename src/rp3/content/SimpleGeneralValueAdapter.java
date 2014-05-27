package rp3.content;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import rp3.core.R;
import rp3.data.Identifiable;
import rp3.data.models.GeneralValue;
import rp3.db.sqlite.DataBase;

public class SimpleGeneralValueAdapter extends BaseAdapter {

	private List<GeneralValue> data;
	private LayoutInflater inflater = null;
	private Context context;	
	private int layoutResID;
	
	static class ViewHolder {
		TextView textView_content;
	}
	
	public SimpleGeneralValueAdapter(Context c, DataBase db, long generalTableId){
		this.data = GeneralValue.getGeneralValues(db, generalTableId);
		this.context = c;
		this.inflater = LayoutInflater.from(context);
		layoutResID = R.layout.base_rowlist_simple_spinner;
	}	
	
	public SimpleGeneralValueAdapter(Context c, List<GeneralValue> data){
		this.data = data;
		this.context = c;
		this.inflater = LayoutInflater.from(context);
		layoutResID = R.layout.base_rowlist_simple_spinner;
	}	
		
	
	public SimpleGeneralValueAdapter(Context c, List<GeneralValue> data, int layoutResID){
		this.data = data;
		this.context = c;
		this.layoutResID = layoutResID;
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
	
	public GeneralValue getGeneralValue(int position) {		
		return (GeneralValue)data.get(position);
	}
	
	public String getCode(int position) {		
		return getGeneralValue(position).getCode();
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
		
		Identifiable current = (GeneralValue)getItem(position);
		if(current!=null){
			String value = current.getDescription();			
				
			holder.textView_content.setText(value);
		}
		
		return convertView;
	}

	
	
}
