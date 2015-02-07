package rp3.sync;

import java.util.Date;

import rp3.data.models.SyncHistory;
import rp3.db.sqlite.Contract;
import rp3.db.sqlite.DataBase;
import rp3.db.sqlite.DbMasterOpenHelper;
import rp3.runtime.Session;
import rp3.util.DateTime;


public abstract class SyncAudit {

	public static Long getDaysOfLastSync(String category, int event){
		Date date = getLastSyncDate(category, event);
		if(date == null) return null;
		
		return DateTime.getDaysDiff(date,DateTime.getCurrentDateTime());
	}
	
	public static Date getLastSyncDate(){
		DataBase db = getDataBaseNewInstance();
		Date date = SyncHistory.getLastSyncDate(db);
		db.close();
		return date;
	}
	
	public static Date getLastSyncDate(int event){
		DataBase db = getDataBaseNewInstance();
		Date date = SyncHistory.getLastSyncDate(db, event);
		db.close();
		return date;
	}
	
	public static Date getLastSyncDate(String category, int event){
		DataBase db = getDataBaseNewInstance();
		Date date = SyncHistory.getLastSyncDate(db, category, event);
		db.close();
		return date;
	}
	
	public static Date getLastSyncDate(String category){
		DataBase db = getDataBaseNewInstance();
		Date date = SyncHistory.getLastSyncDate(db, category);
		db.close();
		return date;
	}
	
	public static void insert(String category, int event){
		insert(category, event, null);
	}
	
	public static void insert(String category, int event, String notes){
		SyncHistory d = new SyncHistory();
		d.setEvent(event);
		d.setCategory(category);
		d.setNotes(notes);
		
		d.setUser(Session.getUser().getLogonName());
		d.setSyncDate(DateTime.getCurrentDateTime());
		
		DataBase db = getDataBaseNewInstance();
		SyncHistory.insert(db, d);
		db.close();
	}
	
	public static void clearAudit(){
		DataBase db = getDataBaseNewInstance();
		SyncHistory.deleteAll(db, Contract.SyncHistory.TABLE_NAME, true);
		db.close();
	}
	
	private static DataBase getDataBaseNewInstance(){
		return DataBase.newDataBase(DbMasterOpenHelper.class);
	}
	
}
