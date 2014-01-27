package rp3.app;

import rp3.core.R;
import rp3.data.Constants;
import rp3.db.sqlite.DataBase;
import rp3.db.sqlite.DataBaseService;
import rp3.db.sqlite.DataBaseServiceHelper;
import rp3.util.ViewUtils;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ProgressBar;

public class BaseListFragment extends ListFragment implements DataBaseService, LoaderCallbacks<Cursor> {
	
	public BaseListFragment()	
	{				
	}
	
	private Class<? extends SQLiteOpenHelper> dataBaseClass;
	private Context context;
	private DataBase db;		
	private int closeResourceOn = Constants.CLOSE_RESOURCES_ON_STOP;
	private View rootView;
	private View textEmptyView;
	private Integer contentViewResource;
	private ProgressBar loadingView;
	
	public View getRootView(){
		return rootView;
	}	
	
	public void showDialogFragment(DialogFragment f, String tagName){
		FragmentTransaction ft = getFragmentManager().beginTransaction();
	    Fragment prev = getFragmentManager().findFragmentByTag(tagName);
	    if (prev != null) {
	        ft.remove(prev);
	    }
	    ft.addToBackStack(null);
		f.show(ft,tagName);
	}
	
	public View getTextEmptyView(){
		if(textEmptyView==null){
			if(getRootView()!=null){
				textEmptyView = getRootView().findViewById(android.R.id.empty);
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
	
	public void setContentView(int resourceId) {
		this.contentViewResource = resourceId;
	}
	
	public void setRootView(View rootView){
		this.rootView = rootView;
		this.textEmptyView = null;
		this.loadingView = null;
	}
	
	public void setDataBaseParams(Class<? extends SQLiteOpenHelper> dataBase){
		dataBaseClass = dataBase;
	}
	
	public void setDataBaseParams(Class<? extends SQLiteOpenHelper> dataBase, int closeResourceOn){
		dataBaseClass = dataBase;
		this.closeResourceOn = closeResourceOn;
	}
	
	@Override
	public void setDataBaseParams(Context c, Class<? extends SQLiteOpenHelper> dataBase) {	
		dataBaseClass = dataBase;
		context = c;
	}
		
	public DataBase getDataBase(){
		if(db == null)
			db = DataBaseServiceHelper.getWritableDatabase(context,  dataBaseClass);
		return db;
	}	
	
	public void closeDataBase(){
		db.close();
		db = null;
	}
	
	public void closeDataBaseResources(){
		DataBaseServiceHelper.closeResources(this);
	}	

	@Override
	public boolean IsActiveDataBase() {
		return db != null;
	}
		
	@Override
	public Class<? extends SQLiteOpenHelper> getDataBaseClass() {
		return dataBaseClass;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.base_simple_listview);
	}
	
	@Override
	public void onAttach(Activity activity) {	
		super.onAttach(activity);
		this.context = activity;		
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(closeResourceOn == Constants.CLOSE_RESOURCES_ON_PAUSE)
			closeDataBaseResources();
	}
	
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if(closeResourceOn == Constants.CLOSE_RESOURCES_ON_STOP)
			closeDataBaseResources();
	}	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {			
		View result;
		if(contentViewResource!=null){
			setRootView(inflater.inflate(contentViewResource, container, false));
			onFragmentCreateView(getRootView(), savedInstanceState);
			result = getRootView();				
		}
		else
			result = super.onCreateView(inflater, container, savedInstanceState);
		
		return result;
	}
	
	@Override
	public void onResume() {	
		super.onResume();
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
	
	public void onFragmentCreateView(View rootView, Bundle savedInstanceState){		
	}
	
	public void setTextViewText(int id, String value){
		ViewUtils.setTextViewText(getRootView(), id, value);
	}	
	
	public void setTextViewCurrencyText(int id, double value)
	{
		ViewUtils.setTextViewCurrencyText(getRootView(), id, value);
	}
	
	public void setTextViewNumberText(int id, double value)
	{
		ViewUtils.setTextViewNumberText(getRootView(), id, value);
	}
	
	public void setButtonClickListener(int id, OnClickListener l)
	{
		ViewUtils.setButtonClickListener(getRootView(), id, l);
	}
	
	public void setImageButtonClickListener(int id, OnClickListener l)
	{
		ViewUtils.setImageButtonClickListener(getRootView(), id, l);
	}
	
	public void setListViewAdapter(int id, ListAdapter adapter)
	{		
		ViewUtils.setListViewAdapter(getRootView(), id, adapter);
	}
	
	public void setListViewOnItemClickListener(int id, AdapterView.OnItemClickListener l){
		ViewUtils.setListViewOnClickListener(getRootView(), id, l);
	}
	
	public String getTextViewString(int id){
		return ViewUtils.getTextViewString(getRootView(), id);
	}
	
	public int getTextViewInt(int id){
		return ViewUtils.getTextViewInt(getRootView(), id);
	}
	
	public double getTextViewDouble(int id){
		return ViewUtils.getTextViewDouble(getRootView(), id);
	}
	
	public void setViewVisibility(int id, int visibility){
		ViewUtils.setViewVisibility(getRootView(), id, visibility);
	}
	
	public int getViewVisibility(int id){
		return ViewUtils.getViewVisibility(getRootView(), id);
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {		
		showDefaultLoading();
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
		hideDefaultLoading();
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
	}
}
