package rp3.db.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

public interface DataBaseService {

	boolean IsActiveDataBase();			
	
	Class<? extends SQLiteOpenHelper> getDataBaseClass();	
	
	public DataBase getDataBase();
	
	void closeDataBase();
	
	void setDataBaseParams(Context c,Class<? extends SQLiteOpenHelper> dataBase);		
	void closeDataBaseResources();

}
