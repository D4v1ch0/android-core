package rp3.db.sqlite;

import java.util.List;

import rp3.configuration.Configuration;
import rp3.db.QueryDir;
import rp3.xml.XmlPull;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public abstract class DataBaseOpenHelper extends SQLiteOpenHelper  {
	
	Context context;
	private static final String TAG = DataBaseOpenHelper.class.getSimpleName();
	public static final String CREATE_DATABASE_EXT_STATEMENT_FILE = "sql_db_create_ext";
	public static final String CREATE_DATABASE_STATEMENT_FILE = "sql_db_create";
	public static final String UPGRADE_VERSION_DATABASE_STATEMENT_FILE = "sql_db_upv%s";
	public static final String DROP_DATABASE_STATEMENT_FILE = "sql_db_drop";

    public static final String TO_VERSION = "ToVersion";
	
	protected void executeListStatements(SQLiteDatabase db, List<String> statements) {
		try
		{
			db.beginTransaction();
			for(String statement: statements){
				db.execSQL(statement);			
			}
			db.setTransactionSuccessful();			
		}finally{
			db.endTransaction();
		}
	}
	
	public DataBaseOpenHelper(Context context) {
		super(context, Configuration.getDataBaseConfiguration().getName(), null, 
				Configuration.getDataBaseConfiguration().getVersion());		
		initialize(context);
	}
	
	public DataBaseOpenHelper(Context context, String dataBaseName, int version) {
		super(context, dataBaseName, null, 
				version);
	}
	
	private void initialize(Context context){
		this.context = context;
		if(!QueryDir.isLoaded()) QueryDir.PrepareQueries(this.context);
	}
    
	public Context getContext(){
		return context;
	}
	
	public String getCreateDataBaseFileName(){
		return CREATE_DATABASE_STATEMENT_FILE;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		int resourceId = getContext().getResources().getIdentifier(getCreateDataBaseFileName(), "xml", getContext().getApplicationContext().getPackageName());		
		XmlResourceParser parser = getContext().getResources().getXml(resourceId);
		List<String> statements = XmlPull.getTextElements(parser, "statement");
		try{

			Log.d(TAG,"STATEMENTS NORMAL...");
			for (String state:statements){
				Log.d(TAG,state);
			}
		}catch (Exception e){
			e.printStackTrace();
		}

		try{
			int resourseExtId = getContext().getResources().getIdentifier(CREATE_DATABASE_EXT_STATEMENT_FILE, "xml", getContext().getApplicationContext().getPackageName());
			XmlResourceParser parserExt = getContext().getResources().getXml(resourseExtId);

			List<String> statementsExt = XmlPull.getTextElements(parserExt, "statement");
			Log.d(TAG,"statementsExt...");
			if(statementsExt.size()>0){
				for (String s:statementsExt){
					Log.d(TAG,s);
				}
				statements.addAll(statementsExt);
			}


			executeListStatements(db, statements);
		}catch (Exception e){
			e.printStackTrace();
		}

	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {		
	}
	
	public void drop(SQLiteDatabase db){
		int resourceId = getContext().getResources().getIdentifier(DROP_DATABASE_STATEMENT_FILE, "xml", getContext().getApplicationContext().getPackageName());		
		XmlResourceParser parser = getContext().getResources().getXml(resourceId);

		List<String> statements = XmlPull.getTextElements(parser, "statement");
		
		executeListStatements(db, statements);		
	}
	
}
