package rp3.app;

import rp3.configuration.Configuration;
import rp3.core.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

public class SearchActivity extends BaseActivity {
	
	
	private ListView listView;	
	private TextView textView_no_search_result_message;
	public static final String RESULT_ARG_ID = "search_id";
	private SearchView searchView;
	private String queryHint;
	
	public SearchActivity(){
		Configuration.TryInitializeConfiguration(this, getDataBaseClass());
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		
		listView = (ListView)this.findViewById(rp3.core.R.id.listView_search_result);
		textView_no_search_result_message = (TextView)this.findViewById(rp3.core.R.id.textView_no_search_result_message);
		
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position,
					long id) {								
				SearchActivity.this.onItemClick(adapterView, view, position, id);
			}
		});
	}
	
	public ListView getListView(){
		return listView;
	}
	
	public ListAdapter getAdpater() {
		return this.listView.getAdapter();
	}

	public void setAdapter(ListAdapter adapter) {
		this.listView.setAdapter(adapter);				
	}
	
	public void setQueryHint(CharSequence queryHint){
		this.queryHint = String.valueOf(queryHint);
		if(searchView!=null)
			searchView.setQueryHint(queryHint);
	}
	
	public CharSequence getQueryHint(){
		return queryHint;
	}		
	
	@Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
    	getMenuInflater().inflate(rp3.core.R.menu.activity_search, menu);    	            	    
    	searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));                                 
        searchView.setIconifiedByDefault(false);
        
        searchView.setOnQueryTextListener(new OnQueryTextListener() {
			
			public boolean onQueryTextSubmit(String query) {				
				boolean result = SearchActivity.this.onQueryTextSubmit(query);
				evaluateShowMessage();
				return result;
			}
			
			public boolean onQueryTextChange(String newText) {				
				boolean result = SearchActivity.this.onQueryTextChange(newText);
				evaluateShowMessage();
				return result;
			}
		});
             
        searchView.setQueryHint(queryHint);
        searchView.requestFocus();
        
    	return super.onCreateOptionsMenu(menu);
    }
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		if(searchView!=null) searchView.requestFocus();
		return super.onPrepareOptionsMenu(menu);
	}
	
	private void evaluateShowMessage(){
		if(listView.getCount() > 0 )
			textView_no_search_result_message.setVisibility(View.GONE);
		else
			textView_no_search_result_message.setVisibility(View.VISIBLE);
	}
	
	protected boolean onQueryTextSubmit(String query){		
		return false;
	}
	
	protected boolean onQueryTextChange(String newText){		
		return false;
	}
	
	protected void onItemClick(AdapterView<?> adapterView, View view, int position,long id){
		Intent intent = new Intent();
		intent.putExtra(RESULT_ARG_ID, id );				
		setResult(RESULT_OK, intent);
		finish();
	}

	
}
