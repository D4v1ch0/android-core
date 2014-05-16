package rp3.configuration;


import java.util.List;

import rp3.db.sqlite.DataBase;
import rp3.db.sqlite.DataBaseService;
import rp3.db.sqlite.DataBaseServiceHelper;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity implements DataBaseService {

	protected Class<? extends SQLiteOpenHelper> dataBaseClass;
	private DataBase db;
	
	public static Intent newIntent(Context c){
		Intent i  = new Intent(c, SettingsActivity.class);
		return i;
	}
	
	public DataBase getDataBase() {
		if (db == null)
			db = DataBaseServiceHelper.getWritableDatabase(this,
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
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);							
	}
	
	@Override
	public void onBuildHeaders(List<Header> target) {
		int resID = this.getResources().getIdentifier("preference_headers", "xml", this.getApplicationContext().getPackageName());		
		loadHeadersFromResource(resID, target);
	}
	
}
