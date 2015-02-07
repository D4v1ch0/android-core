package rp3.app;


import rp3.core.R;
import rp3.data.entity.OnEntityCheckerListener;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

public abstract class BaseListFragment extends BaseFragment implements LoaderCallbacks<Cursor>,
	OnEntityCheckerListener<Object> {
	
	public BaseListFragment()	
	{				
	}
		
	private View textEmptyView;
	private ProgressBar loadingView;
	private ListView listView;
	private ListAdapter listAdapter;
	
	public View getTextEmptyView(){
		if(textEmptyView==null){
			if(getRootView()!=null){
				textEmptyView = getRootView().findViewById(R.id.empty);
			}
		}
		return textEmptyView;
	}
	
	private ProgressBar getLoadingView(){
		if(loadingView==null){
			if(getRootView()!=null){
				loadingView = (ProgressBar)getRootView().findViewById(R.id.loading);
			}
		}
		return loadingView;
	}
	
	public ListView getListView(){
		if(listView == null && getRootView()!=null){
			listView = (ListView)getRootView().findViewById(R.id.listView);
			
		}
		return listView;
	}
	
	public ListAdapter getListAdapter(){
		return getListView().getAdapter();
	}
	
	public void setListAdapter(ListAdapter adapter){
		listAdapter = adapter;
		if(getListView()!=null) getListView().setAdapter(adapter);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {		
		View rView = super.onCreateView(inflater, container, savedInstanceState);
		getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long id) {
				onListItemClick(getListView(), view, position, id);
			}
		});
		getListView().setAdapter(listAdapter);
		return rView;
	}
	
	public void onListItemClick(ListView listView, View view, int position, long id) {                        
    }
	
	public void showDefaultLoading(){
		if(getLoadingView()!=null){
			getLoadingView().setVisibility(View.VISIBLE);
			if(getTextEmptyView()!=null)
				getTextEmptyView().setVisibility(View.GONE);
			getListView().setVisibility(View.GONE);
		}
	}
	
	public void hideDefaultLoading(){
		if(getLoadingView()!=null){
			getLoadingView().setVisibility(View.GONE);						
		}	
		getListView().setVisibility(View.VISIBLE);
	}			
	
	public void notifyListChanged(){
		evaluateEmpty();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.base_simple_listview);
	}					
	
	@Override
	public void onResume() {	
		super.onResume();
		evaluateEmpty();
	}
	
	private void evaluateEmpty(){
		if(getListAdapter()==null || getListAdapter().getCount() ==0){
			if(getTextEmptyView()!=null)
				getTextEmptyView().setVisibility(View.VISIBLE);
			getListView().setVisibility(View.GONE);				
		}
		else{
			if(getTextEmptyView()!=null)
				getTextEmptyView().setVisibility(View.GONE);
			getListView().setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		showDefaultLoading();
		notifyListChanged();
		return super.onCreateLoader(id, args);
	}
	
	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
		hideDefaultLoading();		
		notifyListChanged();
		super.onLoadFinished(loader, c);		
	}
}
