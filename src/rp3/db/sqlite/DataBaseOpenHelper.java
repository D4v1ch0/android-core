package rp3.db.sqlite;

import java.util.List;

import rp3.configuration.Configuration;
import rp3.data.models.Contract;
import rp3.db.QueryDir;
import rp3.xml.XmlPull;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public abstract class DataBaseOpenHelper extends SQLiteOpenHelper  {
	
	private Context context;
	
	public static final String CREATE_DATABASE_STATEMENT_FILE = "sql_db_create";
	public static final String UPGRADE_VERSION_DATABASE_STATEMENT_FILE = "sql_db_upv%s";
	public static final String DROP_DATABASE_STATEMENT_FILE = "sql_db_drop";
	
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
	
	private void initialize(Context context){
		this.context = context;
		if(!QueryDir.isLoaded()) QueryDir.PrepareQueries(this.context);
	}
    
	public Context getContext(){
		return context;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {		
		int resourceId = getContext().getResources().getIdentifier(CREATE_DATABASE_STATEMENT_FILE, "xml", getContext().getApplicationContext().getPackageName());
		
		XmlResourceParser parser = getContext().getResources().getXml(resourceId);

		List<String> statements = XmlPull.getTextElements(parser, "statement");
		statements.add(Contract.User.CREATE_TABLE);
		
		executeListStatements(db, statements);
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
