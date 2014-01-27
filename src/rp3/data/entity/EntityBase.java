package rp3.data.entity;

import java.util.Date;

import rp3.db.sqlite.DataBase;
import rp3.util.Format;
import android.content.ContentValues;

public abstract class EntityBase<T extends Object> {
		
	
	public EntityBase(){		
		tableName = getTableName();
	}
	
	private static String tableName;
	
	private ContentValues contentValues;
	
	public abstract long getID();
	
	public abstract String getTableName();
	
	public abstract void setValues();
	
	protected ContentValues getValues(){
		if(contentValues==null) contentValues = new ContentValues();
		return contentValues;
	}	
	
	protected void clearValues(){
		getValues().clear();
	}
	
	protected void setValue(String key, Double value){
		getValues().put(key, value);
	}
	
	protected void setValue(String key, Integer value){
		getValues().put(key, value);
	}
	
	protected void setValue(String key, Short value){
		getValues().put(key, value);
	}
	
	protected void setValue(String key, Float value){
		getValues().put(key, value);
	}
	
	protected void setValue(String key, String value){		
		getValues().put(key, value);
	}
	
	protected void setValue(String key, Long value){
		getValues().put(key, value);
	}
	
	protected void setValue(String key, boolean value){
		getValues().put(key, Format.getDataBaseBoolean(value) );
	}
	
	protected void setValue(String key, Date value){
		getValues().put(key, Format.getDataBaseString(value) );
	}
	
	public static <T> T get(long id){
		return null;
	}
	
	public static <T> boolean insert(DataBase db, EntityBase<T> e){
		return e.insert(db);
	}
	
	public boolean insert(DataBase db){
		setValues();
		return db.insert(getTableName(), getValues()) != 0;
	}
	
	public static <T> boolean update(DataBase db, EntityBase<T> e){
		return e.update(db);
	}

	public boolean update(DataBase db){
		setValues();
		return db.update(getTableName(), getValues(), getID()) != 0;
	}	
	
	public static boolean delete(DataBase db,long id){
		return db.delete(tableName, id) != 0;
	}
}
