package rp3.db.sqlite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public final class SQLiteUtil {

	public static long getLastInsertRowId(SQLiteDatabase db){
		long id = 0;
		Cursor c = db.rawQuery("SELECT last_insert_rowid();", null);
		if(c.moveToFirst()){
			id = c.getLong(0);
		}
		return id;
	}
	

}
