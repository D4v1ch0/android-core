package rp3.util;

import java.util.Date;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public abstract class ViewUtils {
	
	public static final void setTextViewText(View rootView,int id, String text)
	{
		((TextView) rootView.findViewById(id)).setText(text);
	}
	
	public static final void setTextViewDefaultDateText(View rootView,int id, Date date)
	{
		((TextView) rootView.findViewById(id)).setText( Format.getDefaultDateFormat(date) );
	}
	
	public static final void setTextViewCurrencyText(View rootView,int id, double value)
	{
		((TextView) rootView.findViewById(id)).setText( Format.getDefaultCurrencyFormat(value) );
	}
	
	public static final void setTextViewNumberText(View rootView,int id, double value)
	{
		((TextView) rootView.findViewById(id)).setText( Format.getDefaultNumberFormat(value) );
	}
	
	public static final void setButtonClickListener(View rootView, int id, OnClickListener l){
		((Button) rootView.findViewById(id)).setOnClickListener(l);		
	}
	
	public static final void setImageButtonClickListener(View rootView, int id, OnClickListener l){
		((ImageButton) rootView.findViewById(id)).setOnClickListener(l);
	}
	
	public static final void setListViewAdapter(View rootView,int id, ListAdapter adapter)
	{		
		((ListView) rootView.findViewById(id)).setAdapter(adapter);
	}
	
	public static final void setListViewOnClickListener(View rootView,int id, AdapterView.OnItemClickListener l)
	{		
		((ListView) rootView.findViewById(id)).setOnItemClickListener(l);
	}
	
	public static final String getTextViewString(View rootView, int id){
		return ((TextView) rootView.findViewById(id)).getText().toString();
	}
	
	public static final double getTextViewDouble(View rootView, int id){
		return Convert.getDouble(getTextViewString(rootView,id));			
	}
	
	public static final int getTextViewInt(View rootView, int id){
		return Convert.getInt(getTextViewString(rootView,id));		
	}
	
	public static final void setViewVisibility(View rootView, int id, int visibility){
		rootView.findViewById(id).setVisibility(visibility);
	}
	
	public static final int getViewVisibility(View rootView, int id){
		return rootView.findViewById(id).getVisibility();
	}
	
}
