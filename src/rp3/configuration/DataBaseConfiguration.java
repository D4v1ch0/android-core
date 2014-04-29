package rp3.configuration;

import rp3.db.sqlite.DataBase;
import rp3.db.sqlite.DataBaseServiceHelper;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseConfiguration {

	private String name;
	private int version;
	
	public static final String KEY_NAME = "name";
	public static final String KEY_VERSION = "version";
	private Class<? extends SQLiteOpenHelper> dbClass;
	private Context context;		
	
	public DataBase getDataBase(){
		return DataBaseServiceHelper.getDataBaseInstance(
				context,
				getDataBaseClass());
	}
	
	public Class<? extends SQLiteOpenHelper> getDataBaseClass(){		
		return dbClass;
	}
	
	public DataBaseConfiguration(Context c, Class<? extends SQLiteOpenHelper> dbClass) {		
		this.context = c;
		this.dbClass = dbClass; 
	}

	public String getName() {
		return name;
	}

	void setName(String dbName) {
		name = dbName;
	}

	public int getVersion() {
		return version;
	}

	void setVersion(int version) {
		this.version = version;
	}

}
