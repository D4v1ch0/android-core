package rp3.app;

import java.util.Date;
import java.util.List;

import rp3.core.R;
import rp3.db.sqlite.DataBase;
import rp3.util.ViewUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.DialogInterface;
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

public class BaseFragment extends DialogFragment implements LoaderCallbacks<Cursor> {
		
	public BaseFragment()	
	{		
	}
	
	private Context context;	
	private View rootView;
	private Integer contentViewResource;
	private ProgressBar loadingView;	
	private int currentDialogId;
	
	public View getRootView(){
		return rootView;
	}
	
	public Context getContext(){
		return context;
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
		}
	}
	
	public void hideDefaultLoading(){
		if(getLoadingView()!=null){
			getLoadingView().setVisibility(View.GONE);			
		}
	}
	public void setContentView(int resourceId) {
		this.contentViewResource = resourceId;
	}
	
	public void setRootView(View rootView){
		this.rootView = rootView;
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
		
	public DataBase getDataBase(){		
		return ((BaseActivity)getActivity()).getDataBase();  		
	}	
	

	@Override
	public void onAttach(Activity activity) {		
		super.onAttach(activity);
		this.context = activity;		
	}	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
				
		if(contentViewResource!=null){
			setRootView(inflater.inflate(contentViewResource, container, false));
			onFragmentCreateView(getRootView(), savedInstanceState);								
			
			return getRootView();
		}
		else
			return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	public void onFragmentCreateView(View rootView, Bundle savedInstanceState){		
	}
	
	/*Inline Dialog Confirmation*/
	
	private void setInlineDialog(){
		if(getRootView().findViewById(R.id.base_confirmation_dialog)!=null){
			setButtonClickListener(R.id.button_positive_confirmation, new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					onPositiveConfirmation(currentDialogId);
				}
			});
			
			setButtonClickListener(R.id.button_negative_confirmation, new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					onNegativeConfirmation(currentDialogId);
				}
			});
		}
	}
	
	public void showDialogConfirmation(int id, int message){
		showDialogConfirmation(id, message, message);
	}
	
	public void showDialogConfirmation(final int id, int message, int title){
		currentDialogId = id;
		
		if(getRootView().findViewById(R.id.action_group)!=null)
			setViewVisibility(R.id.action_group, View.GONE);
		
		if(getRootView().findViewById(R.id.base_confirmation_dialog)!=null){
			setInlineDialog();
			setViewVisibility(R.id.base_confirmation_dialog, View.VISIBLE);			
			setTextViewText(R.id.textView_dialog_message, getText(message).toString());
		}
		else{
			Builder dialog = new AlertDialog.Builder( getActivity() ).
					setTitle( title ).
					setMessage( message ).
					setPositiveButton( R.string.confirmation_positive , new DialogInterface.OnClickListener() {						
						@Override
						public void onClick(DialogInterface arg0, int arg1) {							
							onPositiveConfirmation(id);
						}
					}).setNegativeButton(R.string.confirmation_negative, new DialogInterface.OnClickListener() {						
						@Override
						public void onClick(DialogInterface arg0, int arg1) {							
							onNegativeConfirmation(id);
						}
					}).setCancelable(false);
			dialog.show();
		}				
	}
	
	public void hideDialogConfirmation(){
		if(getRootView().findViewById(R.id.base_confirmation_dialog)!=null)
			setViewVisibility(R.id.base_confirmation_dialog, View.GONE);
		if(getRootView().findViewById(R.id.action_group)!=null)
			setViewVisibility(R.id.action_group, View.VISIBLE);
	}
	
	public void onPositiveConfirmation(int id){
		hideDialogConfirmation();
	}
	
	public void onNegativeConfirmation(int id){
		hideDialogConfirmation();
	}
	
	/*View Set Extensions*/
	
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
	
	public void setTextViewDateText(int id,Date date){
		ViewUtils.setTextViewDefaultDateText(getRootView(), id, date);
	}	

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {		
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor c) {	
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
	}
	
}
