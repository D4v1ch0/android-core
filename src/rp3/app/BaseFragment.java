package rp3.app;

import java.util.Date;

import rp3.core.R;
import rp3.data.Constants;
import rp3.db.sqlite.DataBase;
import rp3.db.sqlite.DataBaseService;
import rp3.db.sqlite.DataBaseServiceHelper;
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
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ProgressBar;

public class BaseFragment extends DialogFragment implements DataBaseService, LoaderCallbacks<Cursor> {
		
	public BaseFragment()	
	{		
	}
	
	private Class<? extends SQLiteOpenHelper> dataBaseClass;
	private Context context;
	private DataBase db;		
	private int closeResourceOn = Constants.CLOSE_RESOURCES_ON_STOP;
	private View rootView;
	private Integer contentViewResource;
	private ProgressBar loadingView;	
	private int currentDialogId;
	
	public View getRootView(){
		return rootView;
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
