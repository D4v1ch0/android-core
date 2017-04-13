package rp3.app;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import rp3.configuration.Configuration;
import rp3.configuration.PreferenceManager;
import rp3.content.SimpleCallback;
import rp3.content.SyncAdapter;
import rp3.core.R;
import rp3.data.Constants;
import rp3.data.Message;
import rp3.data.MessageCollection;
import rp3.data.entity.OnEntityCheckerListener;
import rp3.db.sqlite.DataBase;
import rp3.db.sqlite.DataBaseService;
import rp3.db.sqlite.DataBaseServiceHelper;
import rp3.runtime.Session;
import rp3.sync.SyncUtils;
import rp3.util.ConnectionUtils;
import rp3.util.GooglePlayServicesUtils;
import rp3.util.Screen;
import rp3.util.ViewUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ExpandableListAdapter;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.SpinnerAdapter;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.ExpandableListView.OnChildClickListener;

public class BaseActivity extends FragmentActivity implements DataBaseService,
		LoaderCallbacks<Cursor>, OnEntityCheckerListener<Object>,
		DialogDatePickerChangeListener, DialogTimePickerChangeListener, FragmentResultListener {

	protected Class<? extends SQLiteOpenHelper> dataBaseClass;
	protected Context context;
	private DataBase db;
	private int closeResourceOn = Constants.CLOSE_RESOURCES_ON_STOP;
	private boolean isRestoreInstance = false;
	private ProgressBar loadingView;
	private View rootView;
	private int currentDialogId;
	private int menuResource;
	private FragmentTransaction fragmentTransaction;
	private boolean inFragmentTransaction;
	private boolean finishOnHomeUpButton = false;
	private Integer backupRequestOrientation = 0;
	
	private FragmentResultListener fragmentResultCallback;

	private ProgressDialog progressDialog;

	public BaseActivity() {
		this.context = this;
	}

	
	public void requestSync(Bundle settingsBundle) {
		if (ConnectionUtils.isNetAvailable(this)) {
			PreferenceManager.close();
			SyncUtils.requestSync(settingsBundle);
			//lockRotation();
		} else {
			MessageCollection mc = new MessageCollection();
			mc.addErrorMessage(getText(
					R.string.message_error_sync_no_net_available).toString());
			onSyncComplete(new Bundle(), mc);
		}
	}	
	
	public void lockRotation(){
		backupRequestOrientation = getRequestedOrientation();		
		setRequestedOrientation(Screen.getRequestOrientationFromCurrentScreen());
	}
	
	public void resetRotation(){
		setRequestedOrientation(backupRequestOrientation);
	}

	public boolean isRestoreInstance() {
		return isRestoreInstance;
	}

	public void setContentView(int layoutResID, int menuResID) {
		setContentView(layoutResID);
		menuResource = menuResID;
	}

	public void setHomeButtonEnabled(boolean enable) {
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	public void setHomeAsUpEnabled(boolean enable, boolean finishOnHomeUpButton) {
		getActionBar().setDisplayHomeAsUpEnabled(true);
		this.finishOnHomeUpButton = finishOnHomeUpButton;
	}

	public FragmentManager getCurrentFragmentManager() {
		return getSupportFragmentManager();
	}

	public LoaderManager getCurrentLoaderManager() {
		return getSupportLoaderManager();
	}

	public boolean hasFragment(int id) {
		return getCurrentFragmentManager().findFragmentById(id) != null;
	}

	public Fragment getFragment(int id) {
		return getCurrentFragmentManager().findFragmentById(id);
	}

	public boolean hasFragment(String tag) {
		return getCurrentFragmentManager().findFragmentByTag(tag) != null;
	}

	public Fragment getFragment(String tag) {
		return getCurrentFragmentManager().findFragmentByTag(tag);
	}

	public void setFragment(int id, Fragment fragment) {
		if (inFragmentTransaction) {
			fragmentTransaction.replace(id, fragment);
		} else {
			getCurrentFragmentManager().beginTransaction()
					.replace(id, fragment).commit();
		}
	}

	public void beginSetFragment() {
		inFragmentTransaction = true;
		fragmentTransaction = getCurrentFragmentManager().beginTransaction();
	}

	public void endSetFragment() {
		inFragmentTransaction = false;
		fragmentTransaction.commit();
		fragmentTransaction = null;
	}

	public void setFragments(int[] ids, Fragment[] fragments) {
		FragmentTransaction ft = getCurrentFragmentManager().beginTransaction();
		for (int i = 0; i < ids.length; i++)
			ft.replace(ids[i], fragments[0]);
		ft.commit();
	}

	public View getRootView() {
		if (rootView == null)
			rootView = getWindow().getDecorView();
		return rootView;
	}

	public void showDialogDatePicker(int id, Calendar c) {
		DialogDatePickerFragment f = new DialogDatePickerFragment(id, c, this,
				false);
		showDialogFragment(f, "datepicker");
	}

	public void showDialogDatePicker(int id) {
		DialogDatePickerFragment f = new DialogDatePickerFragment(id, this,
				false);
		showDialogFragment(f, "datepicker");
	}

	public void showDialogDatePicker(int id, Date d) {
		DialogDatePickerFragment f = new DialogDatePickerFragment(id, d, this,
				false);
		showDialogFragment(f, "datepicker");
	}

	public void showDialogDatePicker(int id, Calendar c, boolean asYearMonth) {
		DialogDatePickerFragment f = new DialogDatePickerFragment(id, c, this,
				asYearMonth);
		showDialogFragment(f, "datepicker");
	}

	public void showDialogDatePicker(int id, boolean asYearMonth) {
		DialogDatePickerFragment f = new DialogDatePickerFragment(id, this,
				asYearMonth);
		showDialogFragment(f, "datepicker");
	}

	public void showDialogDatePicker(int id, Date d, boolean asYearMonth) {
		DialogDatePickerFragment f = new DialogDatePickerFragment(id, d, this,
				asYearMonth);
		showDialogFragment(f, "datepicker");
	}

    public void showDialogTimePicker(int id){
        DialogTimePickerFragment f = new DialogTimePickerFragment(id, this);
        showDialogFragment(f,"timepicker");
    }

    public void showDialogTimePicker(int id, int hour, int minute){
        DialogTimePickerFragment f = new DialogTimePickerFragment(id, this, hour, minute);
        showDialogFragment(f,"timepicker");
    }

    public void showDialogTimePicker(int id, int interval){
        DialogTimePickerFragment f = new DialogTimePickerFragment(id, this, interval);
        showDialogFragment(f,"timepicker");
    }

    public void showDialogTimePicker(int id, int hour, int minute, int interval){
        DialogTimePickerFragment f = new DialogTimePickerFragment(id, this, hour, minute, interval);
        showDialogFragment(f,"timepicker");
    }

	public void showDialogFragment(DialogFragment f, String tagName) {
		showDialogFragment(f, tagName, null);
	}

	public void showDialogFragment(DialogFragment f, String tagName,
			String title) {
		showDialogFragment(f, tagName, title, null);
	}

	public void showDialogFragment(DialogFragment f, String tagName, int title) {
		showDialogFragment(f, tagName, Session.getContext().getText(title)
				.toString(), null);
	}

	public void showDialogFragment(DialogFragment f, String tagName, int title,
			FragmentResultListener l) {
		showDialogFragment(f, tagName, Session.getContext().getText(title)
				.toString(), l);
	}

	public void showDialogFragment(DialogFragment f, String tagName,
			String title, FragmentResultListener l) {
		fragmentResultCallback = l;
		if (fragmentResultCallback == null)
			fragmentResultCallback = this;

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Fragment prev = getSupportFragmentManager().findFragmentByTag(tagName);
		if (prev != null) {
			ft.remove(prev);
		}
		if (f instanceof BaseFragment) {
			((BaseFragment) f).setIsDialog(true);
			if (!TextUtils.isEmpty(title))
				((BaseFragment) f).setDialogTitle(title);
			((BaseFragment) f).setFragmentTagName(tagName);
		}

		ft.addToBackStack(null);
		f.show(ft, tagName);
	}

	private ProgressBar getLoadingView() {
		if (loadingView == null) {
			if (getRootView() != null) {
				loadingView = (ProgressBar) getRootView().findViewById(
						R.id.loading);
			}
		}
		return loadingView;
	}

	public void showDefaultLoading() {
		if (getLoadingView() != null) {
			getLoadingView().setVisibility(View.VISIBLE);
		}
	}

	public void hideDefaultLoading() {
		if (getLoadingView() != null) {
			getLoadingView().setVisibility(View.GONE);
		}
	}

	public void setDataBaseParameters(Class<? extends SQLiteOpenHelper> dataBase) {
		dataBaseClass = dataBase;
	}

	public void setDataBaseParameters(
			Class<? extends SQLiteOpenHelper> dataBase, int closeResourceOn) {
		dataBaseClass = dataBase;
		this.closeResourceOn = closeResourceOn;
	}

	@Override
	public void setDataBaseParameters(Context c,
			Class<? extends SQLiteOpenHelper> dataBase) {
		dataBaseClass = dataBase;
		context = c;
	}

	public DataBase getDataBase() {
		try {
			if(db != null)
				return db;
			else {
				Session.Start(this);
				rp3.configuration.Configuration.TryInitializeConfiguration(this);
				Configuration.reinitializeConfiguration(this, Configuration.getDataBaseConfiguration()
						.getDataBaseClass());
				if (context == null) {
					context = this;
				}
				if (dataBaseClass == null)
					dataBaseClass = Configuration.getDataBaseConfiguration()
							.getDataBaseClass();
				db = DataBaseServiceHelper.getWritableDatabase(context,
						dataBaseClass);
				return db;
			}
		}
		catch (Exception ex)
		{
			return DataBaseServiceHelper.getWritableDatabase(this,
					Configuration.getDataBaseConfiguration()
							.getDataBaseClass());
		}
	}

	public void closeDataBase() {
		db.close();
		db = null;
	}

	public void setDataBase(DataBase dataBase) {
		db = dataBase;
	}

	public void closeDataBaseResources() {
		DataBaseServiceHelper.closeResources(this);
	}

	@Override
	public boolean IsActiveDataBase() {
		return db != null;
	}

	@Override
	public Class<? extends SQLiteOpenHelper> getDataBaseClass() {
		if (dataBaseClass == null
				&& rp3.configuration.Configuration.getDataBaseConfiguration() != null)
			dataBaseClass = rp3.configuration.Configuration
					.getDataBaseConfiguration().getDataBaseClass();
		return dataBaseClass;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(context == null)
			context = this;
		if (savedInstanceState != null)
			isRestoreInstance = true;
	}
		
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {	
		super.onCreateOptionsMenu(menu);
		if (menuResource != 0)
			getMenuInflater().inflate(menuResource, menu);		
		return true;
	}	

	@Override
	public View onCreateView(View parent, String name, Context context,
			AttributeSet attrs) {
		View r = super.onCreateView(parent, name, context, attrs);

		return r;
	}

	@Override
	protected void onPause() {
		super.onPause();
		// Sync Handler		

		if (closeResourceOn == Constants.CLOSE_RESOURCES_ON_PAUSE)
			closeDataBaseResources();
	}
	
	@Override
	protected void onDestroy() {		
		super.onDestroy();
		try {
			unregisterReceiver(syncFinishedReceiver);
		} catch (IllegalArgumentException e) {
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		try {
			if(context == null)
				context = this;
			registerReceiver(syncFinishedReceiver, new IntentFilter(
					SyncAdapter.SYNC_FINISHED));
		} catch (IllegalArgumentException e) {
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (closeResourceOn == Constants.CLOSE_RESOURCES_ON_STOP)
			closeDataBaseResources();
	}

	/*
	 * Handle results returned to the FragmentActivity by Google Play services
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Decide what to do based on the original request code
		switch (requestCode) {

		case GooglePlayServicesUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST:
			/*
			 * If the result code is Activity.RESULT_OK, try to connect again
			 */
			switch (resultCode) {
			case Activity.RESULT_OK:

				// Log the result
				Log.d("BaseActivity", getString(R.string.message_connected));

			
				break;

			// If any other result was returned by Google Play services
			default:
				// Log the result
				Log.d("BaseActicvity", getString(R.string.message_unresolved));
				
				break;
			}
			default:
				super.onActivityResult(requestCode, resultCode, data);
				break;
		}			
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == android.R.id.home) {
			if (finishOnHomeUpButton) {
				finish();
				return true;
			}
		}

		return super.onOptionsItemSelected(item);
	}

	/* Inline Dialog Confirmation */

	private void setInlineDialog() {
		if (getRootView().findViewById(R.id.base_confirmation_dialog) != null) {

			setButtonClickListener(R.id.button_positive_confirmation,
					new View.OnClickListener() {

						@Override
						public void onClick(View arg0) {
							onPositiveConfirmation(currentDialogId);
						}
					});

			setButtonClickListener(R.id.button_negative_confirmation,
					new View.OnClickListener() {

						@Override
						public void onClick(View arg0) {
							onNegativeConfirmation(currentDialogId);
						}
					});

		}
	}

	public void showDialogProgress(int title, int message) {
		showDialogProgress(getText(title).toString(), getText(message)
				.toString(), false);
	}

	public void showDialogProgress(int title, int message, boolean cancelable) {
		showDialogProgress(getText(title).toString(), getText(message)
				.toString(), cancelable);
	}

	public void showDialogProgress(String title, String message) {
		showDialogProgress(title, message, false);
	}

	public void showDialogProgress(String title, String message,
			boolean cancelable) {
		if (progressDialog == null)
			progressDialog = new ProgressDialog(this);

		//progressDialog.setTitle(title);
		progressDialog.setMessage(message);
		progressDialog.setCancelable(cancelable);
		progressDialog.show();
	}

	public void closeDialogProgress() {
		if(progressDialog != null)
			progressDialog.dismiss();
	}

	public void showDialogConfirmation(int id, int message) {
		showDialogConfirmation(id, message, message);
	}

	public void showDialogConfirmation(final int id, int message, int title) {
		currentDialogId = id;

		if (getRootView().findViewById(R.id.action_group) != null)
			setViewVisibility(R.id.action_group, View.GONE);

		if (getRootView().findViewById(R.id.base_confirmation_dialog) != null) {
			setInlineDialog();
			setViewVisibility(R.id.base_confirmation_dialog, View.VISIBLE);
			setTextViewText(R.id.textView_dialog_message, getText(message)
					.toString());
		} else {
			Builder dialog = new AlertDialog.Builder(this)
					.setTitle(title)
					.setMessage(message)
					.setPositiveButton(R.string.confirmation_positive,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									onPositiveConfirmation(id);
								}
							})
					.setNegativeButton(R.string.confirmation_negative,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									onNegativeConfirmation(id);
								}
							}).setCancelable(false);
			dialog.show();
		}
	}

	public void showDialogConfirmation(final int id, int message, int title, boolean cancelable) {
		currentDialogId = id;

		if (getRootView().findViewById(R.id.action_group) != null)
			setViewVisibility(R.id.action_group, View.GONE);

		if (getRootView().findViewById(R.id.base_confirmation_dialog) != null) {
			setInlineDialog();
			setViewVisibility(R.id.base_confirmation_dialog, View.VISIBLE);
			setTextViewText(R.id.textView_dialog_message, getText(message)
					.toString());
		} else {
			Builder dialog = new AlertDialog.Builder(this)
					.setTitle(title)
					.setMessage(message)
					.setPositiveButton(R.string.confirmation_positive,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0,
													int arg1) {
									onPositiveConfirmation(id);
								}
							})
					.setNegativeButton(R.string.confirmation_negative,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0,
													int arg1) {
									onNegativeConfirmation(id);
								}
							}).setCancelable(cancelable);
			dialog.show();
		}
	}

	public void showDialogMessage(int messageResID) {
		showDialogMessage(new Message(getText(messageResID).toString()), null);
	}

	public void showDialogMessage(String message) {
		showDialogMessage(new Message(message), null);
	}

	public void showDialogMessage(Message message) {
		showDialogMessage(message, null);
	}

	public void showDialogMessage(Message message, final SimpleCallback callback) {
		MessageCollection mc = new MessageCollection();
		mc.addMessage(message);
		showDialogMessage(mc, callback);
	}

	public void showDialogMessage(MessageCollection messages) {
		showDialogMessage(messages, null);
	}

	public void showDialogMessage(MessageCollection messages,
			final SimpleCallback callback) {
		CharSequence[] items = new CharSequence[messages.getCuount()];
		int i = 0;
		for (Message m : messages.getMessages()) {
			items[i++] = m.getText();
		}
		Builder dialog = new AlertDialog.Builder(this)
				.setTitle(R.string.app_name)
				.setItems(items, null)
				.setPositiveButton(R.string.action_accept,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								if (callback != null)
									callback.onExecute();
							}
						}).setCancelable(true);
		dialog.show();
	}

	public void hideDialogConfirmation() {
		if (getRootView().findViewById(R.id.base_confirmation_dialog) != null)
			setViewVisibility(R.id.base_confirmation_dialog, View.GONE);
		if (getRootView().findViewById(R.id.action_group) != null)
			setViewVisibility(R.id.action_group, View.VISIBLE);
	}

	public void onPositiveConfirmation(int id) {
		hideDialogConfirmation();
	}

	public void onNegativeConfirmation(int id) {
		hideDialogConfirmation();
	}

	public void cancelAnimationTransition() {
		this.overridePendingTransition(0, 0);
	}

	/* View Set Extensions */

	public void setViewText(int id, String text) {
		ViewUtils.setViewText(getRootView(), id, text);
	}

	public void setViewDefaultDateText(int id, Date date) {
		ViewUtils.setViewDefaultDateText(getRootView(), id, date);
	}

	public void setViewDefaultDateText(int id, Calendar date) {
		ViewUtils.setViewDefaultDateText(getRootView(), id, date);
	}

	public void setViewDefaultCurrencyText(int id, double value) {
		ViewUtils.setViewDefaultCurrencyText(getRootView(), id, value);
	}

	public void setViewTextDefaultNumberText(int id, double value) {
		ViewUtils.setViewDefaultNumberText(getRootView(), id, value);
	}

	public void setTextViewText(int id, String value) {
		ViewUtils.setTextViewText(getRootView(), id, value);
	}

	public void setTextViewCurrencyText(int id, double value) {
		ViewUtils.setTextViewCurrencyText(getRootView(), id, value);
	}

	public void setTextViewNumberText(int id, double value) {
		ViewUtils.setTextViewNumberText(getRootView(), id, value);
	}

	public void setButtonClickListener(int id, OnClickListener l) {
		ViewUtils.setButtonClickListener(getRootView(), id, l);
	}

	public void setImageButtonClickListener(int id, OnClickListener l) {
		ViewUtils.setImageButtonClickListener(getRootView(), id, l);
	}

	public void setViewAdapter(int id, ListAdapter adapter) {
		ViewUtils.setViewAdapter(getRootView(), id, adapter);
	}
	
	public void setViewAdapter(int id, ExpandableListAdapter adapter) {
		ViewUtils.setViewAdapter(getRootView(), id, adapter);
	}	 

	public void setListViewHeader(int id, int resHeaderID) {
		ViewUtils.setListViewHeader(getRootView(), id, resHeaderID);
	}

	public void setSpinnerAdapter(int id, SpinnerAdapter adapter) {
		ViewUtils.setSpinnerAdapter(getRootView(), id, adapter);
	}

	public int getSpinnerSelectedIntID(int id) {
		return ViewUtils.getSpinnerSelectedIntID(getRootView(), id);
	}

	public long getSpinnerSelectedLongID(int id) {
		return ViewUtils.getSpinnerSelectedLongID(getRootView(), id);
	}

	public int getSpinnerSelectedPosition(int id) {
		return ViewUtils.getSpinnerSelectedPosition(getRootView(), id);
	}

	public String getSpinnerSelectedFieldCursor(int id, String fieldaName) {
		return ViewUtils.getSpinnerSelectedFieldCursor(getRootView(), id,
				fieldaName);
	}

	public String getSpinnerGeneralValueSelectedCode(int id) {
		return ViewUtils.getSpinnerGeneralValueSelectedCode(getRootView(), id);
	}

	public void setSpinnerSimpleAdapter(int id, String columnName, Cursor c) {
		ViewUtils.setSpinnerSimpleAdapter(getRootView(), this, id, columnName,
				c);
	}

	public void setSpinnerSimpleAdapter(int id, List<Object> objects) {
		ViewUtils.setSpinnerSimpleAdapter(getRootView(), this, id, objects);
	}

	public void setSpinnerSimpleAdapter(int id, Object[] objects) {
		ViewUtils.setSpinnerSimpleAdapter(getRootView(), this, id, objects);
	}

	public void setSpinnerSimpleAdapter(int id, int arrayResourceID) {
		ViewUtils.setSpinnerSimpleAdapter(getRootView(), this, id,
				arrayResourceID);
	}

	public SpinnerAdapter getSpinnerAdapter(int id) {
		return ViewUtils.getSpinnerAdapter(getRootView(), id);
	}

	public void setSpinnerSelectionByPosition(int id, int position) {
		ViewUtils.setSpinnerSelectionByPosition(getRootView(), id, position);
	}

	public void setSpinnerSelectionByFieldCursor(int id, String fieldaName,
			Object selectedValue) {
		ViewUtils.setSpinnerSelectionByFieldCursor(getRootView(), id,
				fieldaName, selectedValue);
	}

	public void setSpinnerSelectionById(int id, long itemId) {
		ViewUtils.setSpinnerSelectionById(getRootView(), id, itemId);
	}

	public void setSpinnerGeneralValueSelection(int id, String code) {
		ViewUtils.setSpinnerGeneralValueSelection(getRootView(), id, code);
	}

	public void setViewOnItemClickListener(int id,
			AdapterView.OnItemClickListener l) {
		ViewUtils.setViewOnItemClickListener(getRootView(), id, l);
	}

	public void setViewOnItemSelectedListener(int id,
			AdapterView.OnItemSelectedListener l) {
		ViewUtils.setViewOnItemSelectedListener(getRootView(), id, l);
	}
	
	public void setTextViewDateText(int id,Date date){
		ViewUtils.setTextViewDefaultDateText(getRootView(), id, date);
	}	

	public void setListViewChoiceMode(int id, int choiceMode) {
		ViewUtils.setListViewChoiceMode(getRootView(), id, choiceMode);
	}
	
	public void setViewOnChildClickListener(int id, OnChildClickListener l) {
		ViewUtils.setViewOnChildClickListener(getRootView(), id, l);
	}		

	public void setDatePicker(int id, Date value) {
		ViewUtils.setDatePicker(getRootView(), id, value);
	}

	public void setDatePicker(int id, Calendar value) {
		ViewUtils.setDatePicker(getRootView(), id, value);
	}

	public void setDatePicker(int id, Date value, OnDateChangedListener l) {
		ViewUtils.setDatePicker(getRootView(), id, value, l);
	}

	public void setDatePicker(int id, Calendar value, OnDateChangedListener l) {
		ViewUtils.setDatePicker(getRootView(), id, value, l);
	}

	public void setDatePickerAsYearMonth(int id) {
		ViewUtils.setDatePickerAsYearMonth(getRootView(), id);
	}

	public Date getDatePickerDate(int id) {
		return ViewUtils.getDatePickerDate(getRootView(), id);
	}

	public Calendar getDatePickerCalendar(int id) {
		return ViewUtils.getDatePickerCalendar(getRootView(), id);
	}

	public String getTextViewString(int id) {
		return ViewUtils.getTextViewString(getRootView(), id);
	}

	public int getTextViewInt(int id) {
		return ViewUtils.getTextViewInt(getRootView(), id);
	}

	public double getTextViewDouble(int id) {
		return ViewUtils.getTextViewDouble(getRootView(), id);
	}

	public void setViewVisibility(int id, int visibility) {
		ViewUtils.setViewVisibility(getRootView(), id, visibility);
	}

	public int getViewVisibility(int id) {
		return ViewUtils.getViewVisibility(getRootView(), id);
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

	public final void setViewPagerAdapter(int id, PagerAdapter adapter) {
		ViewUtils.setViewPagerAdapter(getRootView(), id, adapter);
	}

	public void setImageViewBitmapFromInternalStorageAsync(int id,
			String fileName) {
		ViewUtils.setImageViewBitmapFromInternalStorageAsync(getRootView(), id,
				fileName);
	}

	public void setImageViewBitmapFromInternalStorageAsync(View rootView,
			int id, String fileName, int reqWidth, int reqHeight) {
		ViewUtils.setImageViewBitmapFromInternalStorageAsync(getRootView(), id,
				fileName, reqWidth, reqHeight);
	}

	public void setImageViewBitmapFromResourcesAsync(View rootView, int id,
			int resID) {
		ViewUtils
				.setImageViewBitmapFromResourcesAsync(getRootView(), id, resID);
	}

	public void setImageViewBitmapFromResourcesAsync(View rootView, int id,
			int resID, int reqWidth, int reqHeight) {
		ViewUtils.setImageViewBitmapFromResourcesAsync(getRootView(), id,
				resID, reqWidth, reqHeight);
	}

	// public String getTextEditText(int id){
	// return ViewUtils.getTextViewText(getRootView(), id);
	// }

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
	public void onBackPressed() {
		if (getRootView().findViewById(R.id.base_confirmation_dialog) != null
				&& getRootView().findViewById(R.id.base_confirmation_dialog)
						.getVisibility() == View.VISIBLE) {
			hideDialogConfirmation();
		} else
			super.onBackPressed();
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

	public void onSyncComplete(Bundle data, MessageCollection messages) {		
		
	}

	private BroadcastReceiver syncFinishedReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			//resetRotation();
			MessageCollection messages = (MessageCollection) intent.getExtras()
					.getParcelable(SyncAdapter.NOTIFY_EXTRA_MESSAGES);
			Bundle bundle = intent.getExtras().getBundle(
					SyncAdapter.NOTIFY_EXTRA_DATA);
			onSyncComplete(bundle, messages);
		}
	};

	@Override
	public void onDailogDatePickerChange(int id, Calendar c) {
	}

	@Override
	public void onFragmentResult(String tagName, int resultCode, Bundle data) {
	}

    @Override
    public void onDailogTimePickerChange(int id, int hours, int minutes) {

    }
}
