package rp3.app;

import java.util.List;

import rp3.core.R;
import rp3.data.Message;
import rp3.data.MessageCollection;
import rp3.data.entity.OnEntityCheckerListener;
import rp3.db.sqlite.DataBase;
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
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.SpinnerAdapter;

public abstract class BaseListFragment extends ListFragment implements LoaderCallbacks<Cursor>,
	OnEntityCheckerListener<Object> {
	
	public BaseListFragment()	
	{				
	}
	
	private Context context;
	private View rootView;
	private View textEmptyView;
	private Integer contentViewResource;
	private ProgressBar loadingView;
	
	public View getRootView(){
		return rootView;
	}	
	
	public Context getContext(){
		return context;
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
			
	public DataBase getDataBase(){		
		return ((BaseActivity)getActivity()).getDataBase();
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
	
	public void setSpinnerAdapter(int id, SpinnerAdapter adapter){
		ViewUtils.setSpinnerAdapter(getRootView(), id, adapter);
	}
	
	public int getSpinnerSelectedIntID(int id){
		return ViewUtils.getSpinnerSelectedIntID(getRootView(), id);
	}
	
	public long getSpinnerSelectedLongID(int id){
		return ViewUtils.getSpinnerSelectedLongID(getRootView(), id);
	}
	
	public void setSpinnerSimpleAdapter(int id, String columnName, Cursor c) {
		ViewUtils.setSpinnerSimpleAdapter(getRootView(), getActivity(), id, columnName, c);
	}
	
	public void setSpinnerSimpleAdapter(int id,List<Object> objects) {
		ViewUtils.setSpinnerSimpleAdapter(getRootView(), getActivity(), id, objects);
	}
	
	public void setSpinnerSimpleAdapter(int id,Object[] objects) {
		ViewUtils.setSpinnerSimpleAdapter(getRootView(), getActivity(), id, objects);
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
	
	public void setViewError(int id, String text){
		ViewUtils.setViewError(getRootView(),id,text);
	}
	
	public void setViewError(int id, Message m){
		ViewUtils.setViewError(getRootView(),id,m);
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
	
	@Override
	public void onEntityValidationFailed(MessageCollection m, Object e) {	
	}

	@Override
	public void onEntityItemValidationFailed(Message message, Object e) {
	}

	@Override
	public void onEntityValidationSuccess(Object e) {
	}
}
