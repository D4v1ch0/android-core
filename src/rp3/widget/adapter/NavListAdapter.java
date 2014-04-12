package rp3.widget.adapter;

import java.util.List;

import rp3.app.nav.NavItem;
import rp3.core.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NavListAdapter extends BaseAdapter {

	private Context context;
    private List<NavItem> navItems;

    static class ViewHolder {
		TextView textView_title;
		TextView textView_count;
		ImageView imageView_Icon;
	}
    
    static class SeparatorViewHolder {
		TextView textView_title;
	}
    
    public NavListAdapter(Context context, List<NavItem> resultNavItems){
        this.context = context;
        this.navItems = resultNavItems;
    }
 
    @Override
    public int getCount() {
        return navItems.size();
    }
 
    @Override
    public Object getItem(int position) {       
        return navItems.get(position);
    }
    
    public NavItem getNavItem(int position){
    	return navItems.get(position);
    }
 
    @Override
    public long getItemId(int position) {
        return getNavItem(position).getId();
    }
    
    @Override
    public int getItemViewType(int position) {    	
    	return  getNavItem(position).isCategory() ? 1: 0;
    }
    
    @Override
    public int getViewTypeCount() {    	
    	return 2;
    }
 
    @Override
    public boolean isEnabled(int position) {
    	return !getNavItem(position).isCategory();
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        
    	NavItem item = getNavItem(position);
    	
    	
    	if(!item.isCategory())
    	{
    		ViewHolder viewHolder;
    		if (convertView == null) {    		
                LayoutInflater mInflater = (LayoutInflater)
                        context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                         
	            convertView = mInflater.inflate(R.layout.base_rowlist_navitem, null);
	            
	            viewHolder = new ViewHolder();
	            viewHolder.imageView_Icon = (ImageView) convertView.findViewById(R.id.imageView_icon);
	            viewHolder.textView_title = (TextView) convertView.findViewById(R.id.textView_title);
	            viewHolder.textView_count = (TextView) convertView.findViewById(R.id.textView_counter);
	            
	            convertView.setTag(viewHolder);               
            }
    		else
    			viewHolder = (ViewHolder)convertView.getTag();
    		
    		viewHolder.imageView_Icon.setImageResource(navItems.get(position).getIcon());        
    		viewHolder.textView_title.setText(navItems.get(position).getTitle());
            
    		if(item.getIcon() == 0){
    			viewHolder.imageView_Icon.setVisibility(View.GONE);    			
    		}
    		else
    		{
    			viewHolder.imageView_Icon.setVisibility(View.VISIBLE);
    			viewHolder.imageView_Icon.setImageResource(navItems.get(position).getIcon());                		
    		}
    		
            // displaying count
            // check whether it set visible or not
            if(navItems.get(position).getCounterVisibility()){
            	viewHolder.textView_count.setText(navItems.get(position).getCount());
            	viewHolder.textView_count.setVisibility(View.VISIBLE);
            }else{
                // hide the counter view
            	viewHolder.textView_count.setVisibility(View.GONE);
            }             
    	}
    	else
    	{
    		SeparatorViewHolder separatorViewHolder;
    		
    		if (convertView == null) {    		
                LayoutInflater mInflater = (LayoutInflater)
                        context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                         
	            convertView = mInflater.inflate(R.layout.base_rowlist_category_navitem, null);
	            
	            separatorViewHolder = new SeparatorViewHolder();
	            separatorViewHolder.textView_title = (TextView) convertView.findViewById(R.id.textView_title);	            
	            
	            convertView.setTag(separatorViewHolder);
            }
    		else
    			separatorViewHolder = (SeparatorViewHolder)convertView.getTag();
    		    		        		
    		
    		separatorViewHolder.textView_title.setText(navItems.get(position).getTitle());
    	}
    	
    	return convertView;
    }
    
}
