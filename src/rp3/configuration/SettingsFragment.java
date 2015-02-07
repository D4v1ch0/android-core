package rp3.configuration;

import rp3.db.sqlite.DataBase;
import rp3.db.sqlite.DataBaseService;
import rp3.db.sqlite.DataBaseServiceHelper;
import rp3.runtime.Session;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

public class SettingsFragment extends PreferenceFragment implements DataBaseService, OnSharedPreferenceChangeListener {
		
	protected Class<? extends SQLiteOpenHelper> dataBaseClass;
	private DataBase db;
	
	public static Intent newIntent(Context c){
		Intent i  = new Intent(c, SettingsActivity.class);
		return i;
	}
	
	public DataBase getDataBase() {
		if (db == null)
			db = DataBaseServiceHelper.getWritableDatabase(this.getActivity(),
					getDataBaseClass());
		return db;
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
	public void setDataBaseParameters(Context c,
			Class<? extends SQLiteOpenHelper> dataBase) {
		dataBaseClass = dataBase;		
	}

	@Override
	public Class<? extends SQLiteOpenHelper> getDataBaseClass() {
		if(dataBaseClass == null && rp3.configuration.Configuration.getDataBaseConfiguration() != null)
			dataBaseClass = rp3.configuration.Configuration.getDataBaseConfiguration().getDataBaseClass();		
		return dataBaseClass;
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);		
		
		android.preference.PreferenceManager prefMgr = getPreferenceManager();
        prefMgr.setSharedPreferencesName(Session.getContext().getApplicationContext().getPackageName());
        prefMgr.setSharedPreferencesMode(Context.MODE_MULTI_PROCESS | Context.MODE_PRIVATE);
	}

	@Override
	public void onResume() {
	    super.onResume();
	    PreferenceManager.getPreferences().registerOnSharedPreferenceChangeListener(this);
	}
	
	@Override
	public void onPause() {
	    super.onPause();
	    PreferenceManager.getPreferences().unregisterOnSharedPreferenceChangeListener(this);
	}	
	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {        
		Preference pref = findPreference(key);
        onPreferenceChanged(pref);       
    }
	
	public void onPreferenceChanged(Preference pref){		
	}
		
	
	
}
