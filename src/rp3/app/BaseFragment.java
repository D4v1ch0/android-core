package rp3.app;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import rp3.configuration.PreferenceManager;
import rp3.content.SimpleCallback;
import rp3.content.SyncAdapter;
import rp3.core.R;
import rp3.data.Message;
import rp3.data.MessageCollection;
import rp3.data.entity.OnEntityCheckerListener;
import rp3.db.sqlite.DataBase;
import rp3.runtime.Session;
import rp3.sync.SyncUtils;
import rp3.util.ConnectionUtils;
import rp3.util.GooglePlayServicesUtils;
import rp3.util.LocationUtils;
import rp3.util.ViewUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ExpandableListAdapter;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.SpinnerAdapter;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.ExpandableListView.OnChildClickListener;

public class BaseFragment extends DialogFragment implements LoaderCallbacks<Cursor>, OnEntityCheckerListener<Object>, 
	DialogDatePickerChangeListener, FragmentResultListener {
	
	public static final int RESULT_OK = -1;
	public static final int RESULT_CANCELED = 0;
    private FragmentManager mRetainedChildFragmentManager;

    public BaseFragment()
	{		
	}
	
	private Context context;	
	private View rootView;
	private Integer contentViewResource;
	private ProgressBar loadingView;	
	private int currentDialogId;
	private int menuResource;
	private FragmentTransaction fragmentTransaction;
	private boolean inFragmentTransaction;
	private boolean isDialog;
	private String dialogTitle;
	private String currentFragmentTagName;
	private FragmentResultListener fragmentResultCallback;
	GoogleApiClient locationClient;
	private boolean enableGooglePlayServices = false;
	
	private ProgressDialog progressDialog;
	
	public View getRootView(){
		return rootView;
	}
	
	public void tryEnableGooglePlayServices(boolean enableGooglePlayServices){		
		this.enableGooglePlayServices = enableGooglePlayServices;
		if(this.enableGooglePlayServices){
			this.enableGooglePlayServices = 
					GooglePlayServicesUtil.isGooglePlayServicesAvailable(Session.getContext()) == ConnectionResult.SUCCESS;
		}
	}
	
	public Context getContext(){
		if(context == null)
			return Session.getContext();
		return context;
	}
	
	void setIsDialog(boolean isDialog){
		this.isDialog = isDialog;		
	}
	
	void setDialogTitle(String title){
		if(getDialog()!=null)
			getDialog().setTitle(title);
		dialogTitle = title;
	}	
	
	void setFragmentTagName(String tagName){
		currentFragmentTagName = tagName;
	}
	
	public String getFragmentTagName(){
		return currentFragmentTagName;
	}
	
	public boolean isDialog(){
		return isDialog;
	}
	
	public void requestSync(Bundle settingsBundle){
		if(ConnectionUtils.isNetAvailable(getActivity())){
			PreferenceManager.close();
			SyncUtils.requestSync(settingsBundle);
			//((BaseActivity)getActivity()).lockRotation();
		}else{
			MessageCollection mc = new MessageCollection();
			mc.addErrorMessage(getText(R.string.message_error_sync_no_net_available).toString());
			onSyncComplete(new Bundle(), mc);
		}
	}
	
	public <D> void executeLoader(int id, Bundle args, LoaderCallbacks<D> callback){
		Loader<Object> loader = this.getLoaderManager().getLoader(id); 
		if(loader==null){
			this.getLoaderManager().initLoader(id, args, callback);
		}
		else{
			this.getLoaderManager().restartLoader(id, args, callback);
		}
	}
	
	public boolean hasFragment(int id){
		return getCurrentChildFragmentManager().findFragmentById(id) != null;
	}
	
	public Fragment getFragment(int id){
		return getCurrentChildFragmentManager().findFragmentById(id);
	}
	
	public boolean hasFragment(String tag){
		return getCurrentChildFragmentManager().findFragmentByTag(tag)!=null;
	}
	
	public Fragment getFragment(String tag){
		return getCurrentChildFragmentManager().findFragmentByTag(tag);
	}
	
	public void setFragment(int id, Fragment fragment){
		if(inFragmentTransaction){
			fragmentTransaction.replace(id, fragment);
		}
		else{
            getCurrentChildFragmentManager().beginTransaction().replace(id, fragment).commit();
		}
	}
	
	public void beginSetFragment(){
		inFragmentTransaction = true;
		fragmentTransaction = getCurrentChildFragmentManager().beginTransaction();
	}	
	
	public void endSetFragment(){
		inFragmentTransaction = false;
		fragmentTransaction.commit();
		fragmentTransaction = null;
	}
	
	public void setFragments(int[] ids, Fragment[] fragments){
		FragmentTransaction ft = getCurrentChildFragmentManager().beginTransaction();
		for(int i = 0; i < ids.length; i ++)
			ft.replace(ids[i], fragments[0]);
		ft.commit();
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
	
	public void setContentView(int layoutResID, int menuResID){
		setContentView(layoutResID);
		menuResource = menuResID;
	}
	
	public void finishActivity(){
		this.getActivity().finish();
	}
	
	public void finish(){
		if(isDialog)
			dismiss();
		else			
			finishActivity();
	}
	
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		if(menuResource!=0) inflater.inflate(menuResource, menu);
		super.onCreateOptionsMenu(menu, inflater);
		onAfterCreateOptionsMenu(menu);				
	}
	
	public void onAfterCreateOptionsMenu(Menu menu){		
	}
	
	public void setRootView(View rootView){
		this.rootView = rootView;
	}
	
	public void setFragmentResultCallback(FragmentResultListener c){
		this.fragmentResultCallback = c;
	}
	
	public void showDialogDatePicker(int id, Calendar c){
		DialogDatePickerFragment f = new DialogDatePickerFragment(id, c, this, false);		
		showDialogFragment(f,"datepicker");
	}
	
	public void showDialogDatePicker(int id){
		DialogDatePickerFragment f = new DialogDatePickerFragment(id, this, false);		
		showDialogFragment(f,"datepicker");
	}
	
	public void showDialogDatePicker(int id, Date d){
		DialogDatePickerFragment f = new DialogDatePickerFragment(id, d, this, false);		
		showDialogFragment(f,"datepicker");
	}
	
	public void showDialogDatePicker(int id, Calendar c, boolean asYearMonth){
		DialogDatePickerFragment f = new DialogDatePickerFragment(id, c, this, asYearMonth);				
		showDialogFragment(f,"datepicker");
	}
	
	public void showDialogDatePicker(int id, boolean asYearMonth){
		DialogDatePickerFragment f = new DialogDatePickerFragment(id, this, asYearMonth);		
		showDialogFragment(f,"datepicker");
	}
		
	
	public void showDialogDatePicker(int id, Date d, boolean asYearMonth){
		DialogDatePickerFragment f = new DialogDatePickerFragment(id, d, this, asYearMonth);		
		showDialogFragment(f,"datepicker");
	}
	
	public void showDialogFragment(DialogFragment f, String tagName) {				
		showDialogFragment(f,tagName, null, null);
	}
	
	public void showDialogFragment(DialogFragment f, String tagName, String title) {				
		showDialogFragment(f,tagName, title, null);
	}
	
	public void showDialogFragment(DialogFragment f, String tagName, int title) {				
		showDialogFragment(f,tagName, Session.getContext().getText(title).toString(), null);
	}
	
	public void showDialogFragment(DialogFragment f, String tagName, int title, FragmentResultListener l) {
		showDialogFragment(f,tagName, Session.getContext().getText(title).toString(), l);
	}
	
	public void showDialogFragment(DialogFragment f, String tagName, String title, FragmentResultListener l) {		
		if(l == null) l = this;
		if(f instanceof BaseFragment) ((BaseFragment) f).setFragmentResultCallback(l);
		
		FragmentTransaction ft = getCurrentChildFragmentManager().beginTransaction();
		Fragment prev = getCurrentChildFragmentManager().findFragmentByTag(tagName);
		if (prev != null) {
			ft.remove(prev);
		}
		if(f instanceof BaseFragment){
			((BaseFragment)f).setIsDialog(true);
			if(!TextUtils.isEmpty(title))
				((BaseFragment)f).setDialogTitle(title);
			((BaseFragment)f).setFragmentTagName(tagName);
		}
		
		ft.addToBackStack(null);		
		f.show(ft, tagName);
	}
	
	public void setResult(int resultCode, Bundle data){
		if(fragmentResultCallback!=null)
			fragmentResultCallback.onFragmentResult(getFragmentTagName(), resultCode, data);
	}
		
	public DataBase getDataBase(){		
		return ((BaseActivity)getActivity()).getDataBase();  		
	}

    private FragmentManager getCurrentChildFragmentManager() {//!!!Use this instead of getFragmentManager, support library from 20+, has a bug that doesn't retain instance of nested fragments!!!!
        if(mRetainedChildFragmentManager == null) {
            mRetainedChildFragmentManager = getChildFragmentManager();
        }
        return mRetainedChildFragmentManager;
    }
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub		
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);	
		
		if(enableGooglePlayServices){
			locationClient = LocationUtils.getLocationClient((BaseActivity)this.getActivity());
		}
	}
	
	@Override
	public void onAttach(Activity activity) {		
		super.onAttach(activity);
        if (mRetainedChildFragmentManager != null) {
            //restore the last retained child fragment manager to the new
            //created fragment
            try {
                Field childFMField = Fragment.class.getDeclaredField("mChildFragmentManager");
                childFMField.setAccessible(true);
                childFMField.set(this, mRetainedChildFragmentManager);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
		this.context = activity;					
	}	
	
	@Override
	public void onResume() {		
		super.onResume();
		try {
			getActivity().registerReceiver(syncFinishedReceiver, new IntentFilter(SyncAdapter.SYNC_FINISHED));			
		} catch(IllegalArgumentException e) {			
		}
		if(enableGooglePlayServices){
			if(GooglePlayServicesUtils.servicesConnected((BaseActivity)this.getActivity()))
				locationClient.connect();
		}
	}
	
	@Override
	public void onStop() {		
		super.onStop();
		if(enableGooglePlayServices){
			if(GooglePlayServicesUtils.servicesConnected((BaseActivity)this.getActivity()))
				locationClient.disconnect();
		}
	}
	
	
	@Override
	public void onPause() {	
		super.onPause();			
	}
	
	@Override
	public void onDestroy() {		
		super.onDestroy();
		try {
			getActivity().unregisterReceiver(syncFinishedReceiver);			
		} catch(IllegalArgumentException e) {
			
		}
	}
	
	public Location getLastLocation(){		
		if(GooglePlayServicesUtils.servicesConnected((BaseActivity) this.getActivity())){
			Location location = LocationServices.FusedLocationApi.getLastLocation(locationClient);
			return location;
		}
		return null;
	}
	
//	private void setMenu(){
//		Menu menu = null;
//	    try {
//	        Class<?> menuBuilderClass = Class.forName("com.android.internal.view.menu.MenuBuilder");
//
//	        Constructor<?> constructor = menuBuilderClass.getDeclaredConstructor(Context.class);
//
//	        menu = (Menu) constructor.newInstance(this.getContext());
//
//	    } catch (Exception e) {e.printStackTrace();}
//	    
//		this.getActivity().getMenuInflater().inflate(R.menu.activity_search, menu);
//		
//		getRootView()
//	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
				
		if(contentViewResource!=null){
			setRootView(inflater.inflate(contentViewResource, container, false));
			
			if(isDialog() && !TextUtils.isEmpty(dialogTitle))
				setDialogTitle(dialogTitle);
			
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
	
	public void showDialogProgress(int title, int message){
		showDialogProgress(getText(title).toString(), getText(message).toString(), false);
	}
	
	public void showDialogProgress(int title, int message, boolean cancelable){
		showDialogProgress(getText(title).toString(), getText(message).toString(), cancelable);
	}
	
	public void showDialogProgress(String title, String message){
		showDialogProgress(title, message, false);
	}
	
	public void showDialogProgress(String title, String message, boolean cancelable){
		if(progressDialog==null) progressDialog = new ProgressDialog(getContext());
		
		progressDialog.setTitle(title);
		progressDialog.setMessage(message);				
		progressDialog.setCancelable(cancelable);
		progressDialog.show();
	}
	
	public void closeDialogProgress(){
		try{
			progressDialog.dismiss();
		}catch(Exception ex){}
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
	
	public void showDialogMessage(int messageResID){
		showDialogMessage(new Message(getText(messageResID).toString()), null);
	}
	
	public void showDialogMessage(String message){
		showDialogMessage(new Message(message), null);
	}
	
	public void showDialogMessage(Message message){
		showDialogMessage(message, null);
	}
	
	public void showDialogMessage(Message message, final SimpleCallback callback){
		MessageCollection mc = new MessageCollection();
		mc.addMessage(message);
		showDialogMessage(mc, callback);
	}
	
	public void showDialogMessage(MessageCollection messages){
		showDialogMessage(messages, null);
	}

	public void showDialogMessage(MessageCollection messages, final SimpleCallback callback){		 
		CharSequence[] items = new CharSequence[messages.getCuount()];
		int i = 0;
		for(Message m : messages.getMessages()){
			items[i++] = m.getText();
		}
		Builder dialog = new AlertDialog.Builder(getContext())
		.setTitle(R.string.app_name)
		.setItems(items, null)
		.setPositiveButton(R.string.action_accept, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				if(callback!=null)
					callback.onExecute();				
			}
		})
		.setCancelable(true);
		dialog.show();
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
	
	public void cancelAnimationTransition(){
		this.getActivity().overridePendingTransition(0, 0);
	}
	
	/*View Set Extensions*/
	
	public void setViewText(int id, String text){
		ViewUtils.setViewText(getRootView(), id, text);
	}
	
	public void setViewDefaultDateText(int id, Date date){
		ViewUtils.setViewDefaultDateText(getRootView(), id, date);
	}
	
	public void setViewDefaultDateText(int id, Calendar date){
		ViewUtils.setViewDefaultDateText(getRootView(), id, date);
	}
	
	public void setViewDefaultCurrencyText(int id, double value){
		ViewUtils.setViewDefaultCurrencyText(getRootView(), id, value);
	}
	
	public void setViewDefaultNumberText(int id, double value){
		ViewUtils.setViewDefaultNumberText(getRootView(), id, value);
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
	
	public void setViewAdapter(int id, ListAdapter adapter){
		ViewUtils.setViewAdapter(getRootView(), id, adapter);
	}
	
	public void setViewAdapter(int id, ExpandableListAdapter adapter) {
		ViewUtils.setViewAdapter(getRootView(), id, adapter);
	}
	
	public void setListViewHeader(int id, int resHeaderID){
		ViewUtils.setListViewHeader(getRootView(), id, resHeaderID);
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
	
	public int getSpinnerSelectedPosition(int id){		
		return ViewUtils.getSpinnerSelectedPosition(getRootView(), id);
	}
	
	public String getSpinnerSelectedFieldCursor(int id, String fieldaName){		
		return ViewUtils.getSpinnerSelectedFieldCursor(getRootView(), id, fieldaName);
	}
	
	public String getSpinnerGeneralValueSelectedCode(int id){
		return ViewUtils.getSpinnerGeneralValueSelectedCode(getRootView(), id);
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
	
	public void setSpinnerSimpleAdapter(int id, int arrayResourceID) {
		ViewUtils.setSpinnerSimpleAdapter(getRootView(), getActivity(), id, arrayResourceID);
	}
	
	public SpinnerAdapter getSpinnerAdapter(int id) {
		return ViewUtils.getSpinnerAdapter(getRootView(), id);
	}
	
	public void setSpinnerSelectionByPosition(int id, int position) {
		ViewUtils.setSpinnerSelectionByPosition(getRootView(), id, position);
	}
	
	public void setSpinnerSelectionByFieldCursor(int id, String fieldaName, Object selectedValue) {
		ViewUtils.setSpinnerSelectionByFieldCursor(getRootView(), id, fieldaName, selectedValue);
	}
	
	public void setSpinnerSelectionById(int id, long itemId) {
		ViewUtils.setSpinnerSelectionById(getRootView(), id, itemId);
	}		
	
	public void setSpinnerGeneralValueSelection(int id, String code){
		ViewUtils.setSpinnerGeneralValueSelection(getRootView(), id, code);
	}
	
	public void setViewOnItemClickListener(int id, AdapterView.OnItemClickListener l){
		ViewUtils.setViewOnItemClickListener(getRootView(), id, l);
	}
	
	public void setViewOnItemSelectedListener(int id, AdapterView.OnItemSelectedListener l){
		ViewUtils.setViewOnItemSelectedListener(getRootView(), id, l);
	}
			
	public void setListViewChoiceMode(int id, int choiceMode){
		ViewUtils.setListViewChoiceMode(getRootView(), id, choiceMode);
	}
	
	public void setViewOnChildClickListener(int id, OnChildClickListener l) {
		ViewUtils.setViewOnChildClickListener(getRootView(), id, l);
	}
	
	public void setDatePicker(int id, Date value){
		ViewUtils.setDatePicker(getRootView(), id, value);
	}
	
	public void setDatePicker(int id, Calendar value){
		ViewUtils.setDatePicker(getRootView(), id, value);
	}
	
	public void setDatePicker(int id, Date value, OnDateChangedListener l){		
		ViewUtils.setDatePicker(getRootView(), id, value, l);		
	}
	
	public void setDatePicker(int id, Calendar value, OnDateChangedListener l){
		ViewUtils.setDatePicker(getRootView(), id, value, l);
	}
	
	public void setDatePickerAsYearMonth(int id){
		ViewUtils.setDatePickerAsYearMonth(getRootView(), id);
	}
	
	public Date getDatePickerDate(int id){		
		return ViewUtils.getDatePickerDate(getRootView(), id);
	}
	
	public Calendar getDatePickerCalendar(int id){
		return ViewUtils.getDatePickerCalendar(getRootView(), id);
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
	
	public void setViewError(int id, String text) {
		ViewUtils.setViewError(getRootView(), id, text);
	}
	
	public void setViewError(int id, String text, int innerViewId) {
		ViewUtils.setViewError(getRootView(), id, text, innerViewId);
	}

	public void setViewError(int id, Message m) {
		ViewUtils.setViewError(getRootView(), id, m);
	}
	
	public void setViewError(int id, Message m, int innerViewId) {
		ViewUtils.setViewError(getRootView(), id, m, innerViewId);
	}

	public final void setViewPagerAdapter(int id, PagerAdapter adapter){
		ViewUtils.setViewPagerAdapter(getRootView(), id, adapter);
	}
	
	public void setImageViewBitmapFromInternalStorageAsync(int id, String fileName){
		ViewUtils.setImageViewBitmapFromInternalStorageAsync(getRootView(),id, fileName);
	}
	
	public void setImageViewBitmapFromInternalStorageAsync(View rootView, int id, String fileName, 
			int reqWidth, int reqHeight){
		ViewUtils.setImageViewBitmapFromInternalStorageAsync(getRootView(), id, fileName, reqWidth, reqHeight);
	}
	
	public void setImageViewBitmapFromResourcesAsync(View rootView, int id, int resID){
		ViewUtils.setImageViewBitmapFromResourcesAsync(getRootView(), id, resID);
	}
	
	public void setImageViewBitmapFromResourcesAsync(View rootView, int id, int resID,
			int reqWidth, int reqHeight){
		ViewUtils.setImageViewBitmapFromResourcesAsync(getRootView(), id, resID, reqWidth, reqHeight);
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

	@Override
	public void onEntityValidationFailed(MessageCollection mc, Object e) {		
	}

	@Override
	public void onEntityItemValidationFailed(Message m, Object e) {
	}

	@Override
	public void onEntityValidationSuccess(Object e) {
	}
	
	public void onSyncComplete(Bundle data, MessageCollection messages){	
		//((BaseActivity)getActivity()).resetRotation();
	}
	
	private BroadcastReceiver syncFinishedReceiver = new BroadcastReceiver() {

	    @Override
	    public void onReceive(Context context, Intent intent) {
	    	//((BaseActivity)getActivity()).resetRotation();
	    	MessageCollection messages = (MessageCollection)intent.getExtras().getParcelable(SyncAdapter.NOTIFY_EXTRA_MESSAGES);	        
	        Bundle bundle = intent.getExtras().getBundle(SyncAdapter.NOTIFY_EXTRA_DATA);
	    	onSyncComplete(bundle, messages);
	    }
	};		
	
	public void onCancel(DialogInterface dialog) {
		setResult(RESULT_CANCELED, null);
	};

	@Override
	public void onDailogDatePickerChange(int id, Calendar c) {		
	}

	@Override
	public void onFragmentResult(String tagName, int resultCode, Bundle data) {		
	}
}
