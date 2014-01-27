package rp3.db.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

public final class DataBaseServiceHelper {

	public static DataBase getDataBaseInstance(Context c,Class<? extends SQLiteOpenHelper> dbClass) {		
		try {			
			SQLiteOpenHelper openHelper = ((SQLiteOpenHelper) dbClass.getDeclaredConstructor(Context.class).
					newInstance(c));	
			
			DataBase db = new DataBase(c,openHelper);
			return db;
		} catch (Exception e) {
			return null;
		}
	}
	
	public static DataBase getReadableDatabase(Context c,Class<? extends SQLiteOpenHelper> dbClass){
		if(c!=null && dbClass != null){
			return DataBaseServiceHelper.getDataBaseInstance(c, 
					dbClass);
		}			
		return null;
	}	
	
	public static DataBase getWritableDatabase(Context c,Class<? extends SQLiteOpenHelper> dbClass){		
		if(c!=null && dbClass != null)
			return DataBaseServiceHelper.getDataBaseInstance(c, dbClass);
		return null;
	}
	
	public static void closeResources(DataBaseService dataBaseService)
	{
		if(dataBaseService.IsActiveDataBase())
		{
			dataBaseService.closeDataBase();
		}
	}
	
}
