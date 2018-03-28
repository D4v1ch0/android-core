package rp3.db.sqlite;

import java.util.Date;

import rp3.db.QueryDir;
import rp3.runtime.Session;
import rp3.util.Convert;
import rp3.util.CursorUtils;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

public class DataBase {

	private static final String TAG = DataBase.class.getSimpleName();
	public static final String TEXT_TYPE = " TEXT";
	public static final String NUMERIC_TYPE = " NUM";
	public static final String INTEGER_TYPE = " INTEGER";
	public static final String UNSIGNED_BIG_INTEGER_TYPE = " UNSIGNED BIG INTEGER";
    public static final String DATETIME_TYPE = " TEXT";
    public static final String MONEY_TYPE = " REAL";
    public static final String REAL_TYPE = " REAL";
    public static final String COMMA_SEP = ",";
    
    public static final String DATABASE_TRUE_VALUE = "1";
    public static final String DATABASE_FALSE_VALUE = "0";
    
    public static final String WHERE_CLAUSE = " WHERE ";
    
    public static final String REFERENCE_CONSTRAINT = " REFERENCES ";
    
    public static final String DEFAULT_WHERE_UPDATE = " _ID = ? ";
    
	Context c;
	private SQLiteOpenHelper dbOpenHelper;
	private SQLiteDatabase db;		
	
	public static DataBase newDataBase(Class<? extends SQLiteOpenHelper> dbClass){
		return DataBaseServiceHelper.getDataBaseInstance(Session.getContext(), dbClass);
	}
	
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public DataBase(Context c, SQLiteOpenHelper helper){
		dbOpenHelper = helper;
		dbOpenHelper.setWriteAheadLoggingEnabled(true);
		this.c = c;
	}	
	
	private SQLiteDatabase getDb(){
		//Log.d(TAG,"getDb...");
		if(db==null){
			Log.d(TAG,"db==null...");
			db = dbOpenHelper.getWritableDatabase();
            db.enableWriteAheadLogging();
		}
		return db;
	}
	
	public boolean isOpen(){		
		return getDb().isOpen();
	}	
	
	public void close(){	
		if(db!=null){
			db.close();
			db = null;
		}
	}	
	
	public void beginTransaction(){
		getDb().beginTransaction();
	}

	public void beginTransactionNonExclusive()
	{
		getDb().beginTransactionNonExclusive();
	}

	public SQLiteStatement compileStatement(String sql)
	{
		return getDb().compileStatement(sql);
	}

	public void setTransactionSuccessful()
	{
		getDb().setTransactionSuccessful();
	}
	
	public void commitTransaction(){
		getDb().setTransactionSuccessful();
	}
	
	public void rollBackTransaction(){
		if(getDb().inTransaction())
			getDb().endTransaction();
	}
	
	public void endTransaction(){
		if(getDb().inTransaction())
			getDb().endTransaction();
	}
	
	public Cursor rawQueryDir(String queryName){		
		return getDb().rawQuery(QueryDir.getQuery(queryName), null);
	}
	
	public Cursor rawQueryDir(String queryName,String[] selectionArgs){		
		return getDb().rawQuery(QueryDir.getQuery(queryName), selectionArgs);
	}
	
	public Cursor rawQueryDir(String queryName,String selectionArg){		
		return rawQueryDir(queryName, Convert.getStringArrayFromScalar(selectionArg));
	}
	
	public Cursor rawQueryDir(String queryName,long selectionArg){		
		return rawQueryDir(queryName, Convert.getStringArrayFromScalar(selectionArg));
	}
	
	public Cursor rawQueryDir(String queryName,int selectionArg){		
		return rawQueryDir(queryName, Convert.getStringArrayFromScalar(selectionArg));
	}
	
	public Cursor rawQuery(String sql){		
		return getDb().rawQuery(sql, null);
	}
	
	public Cursor rawQuery(String sql,String[] selectionArgs){		
		return getDb().rawQuery(sql, selectionArgs);
	}
	
	public Cursor rawQuery(String sql,String selectionArg){		
		return getDb().rawQuery(sql, Convert.getStringArrayFromScalar(selectionArg));
	}
	
	public Cursor rawQuery(String sql,long selectionArg){		
		return getDb().rawQuery(sql, Convert.getStringArrayFromScalar(selectionArg));
	}
	
	public Cursor rawQuery(String sql,int selectionArg){		
		return getDb().rawQuery(sql, Convert.getStringArrayFromScalar(selectionArg));
	}
	
	public Cursor query(String table, String column)
	{
		return query(table, Convert.getStringArrayFromScalar(column), null, null, null, null,null);
	}
	
	public Cursor query(boolean dictinct, String table, String column)
	{
		return query(dictinct, table, Convert.getStringArrayFromScalar(column), null, null, null, null,null);
	}
	
	public Cursor query(String table, String[] columns)
	{
		return query(table, columns, null, null, null, null,null);
	}
	
	public Cursor query(boolean dictinct, String table, String[] columns)
	{
		return query(dictinct, table, columns, null, null, null, null,null);
	}
	
	public Cursor query(String table, String[] columns, String selection, String[] selectionArgs)
	{
		return query(table, columns, selection, selectionArgs, null, null,null);
	}
	
	public Cursor query(boolean distinct, String table, String[] columns, String selection, String[] selectionArgs)
	{
		return query(distinct, table, columns, selection, selectionArgs, null, null,null);
	}
	
	public Cursor query(String table, String[] columns, String selection, String selectionArg)
	{
		return query(table, columns, selection, Convert.getStringArrayFromScalar(selectionArg), null, null,null);
	}
	
	public Cursor query(boolean distinct, String table, String[] columns, String selection, String selectionArg)
	{
		return query(distinct, table, columns, selection, Convert.getStringArrayFromScalar(selectionArg), null, null,null);
	}
	
	public Cursor query(String table, String column, String selection, String selectionArg)
	{
		return query(table, Convert.getStringArrayFromScalar(column), 
				selection, Convert.getStringArrayFromScalar(selectionArg), null, null,null);
	}
	
	public Cursor query(boolean distinct, String table, String column, String selection, String selectionArg)
	{
		return query(distinct, table, Convert.getStringArrayFromScalar(column), 
				selection, Convert.getStringArrayFromScalar(selectionArg), null, null,null);
	}
	
	public Cursor query(String table, String column, String selection, String[] selectionArgs)
	{
		return query(table, Convert.getStringArrayFromScalar(column), 
				selection, selectionArgs, null, null,null);
	}
	
	public Cursor query(boolean distinct, String table, String column, String selection, String[] selectionArgs)
	{
		return query(distinct, table, Convert.getStringArrayFromScalar(column), 
				selection, selectionArgs, null, null,null);
	}
	
	public Cursor query(String table, String[] columns, String selection, boolean selectionArg)
	{
		return query(table, columns, selection, Convert.getStringArrayFromScalar(selectionArg), null, null,null);
	}
	
	public Cursor query(boolean distinct, String table, String[] columns, String selection, boolean selectionArg)
	{
		return query(distinct, table, columns, selection, Convert.getStringArrayFromScalar(selectionArg), null, null,null);
	}
	
	public Cursor query(String table, String[] columns, String selection, Long selectionArg)
	{
		return query(table, columns, selection, Convert.getStringArrayFromScalar(selectionArg), null, null,null);
	}
	
	public Cursor query(boolean distinct, String table, String[] columns, String selection, Long selectionArg)
	{
		return query(distinct, table, columns, selection, Convert.getStringArrayFromScalar(selectionArg), null, null,null);
	}
	
	public Cursor query(String table, String[] columns, String selection, int selectionArg)
	{
		return query(table, columns, selection, Convert.getStringArrayFromScalar(selectionArg), null, null,null);
	}
	
	public Cursor query(boolean distinct, String table, String[] columns, String selection, int selectionArg)
	{
		return query(distinct, table, columns, selection, Convert.getStringArrayFromScalar(selectionArg), null, null,null);
	}
	
	public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, 
			String having, String orderBy){
		return getDb().query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
	}
		
	
	public Cursor query(boolean distinct, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, 
			String having, String orderBy){
		return getDb().query(distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, null);
	}
	
	public long queryMaxLong(String table, String column, String selection, String[] selectionArgs){
		Cursor c = query(table, getMaxClauseFrom(column, column), selection, selectionArgs);		
		if(!c.moveToFirst()) return 0;
		return CursorUtils.getLong(c);		
	}
	
	public int queryMaxInt(String table, String column, String selection, String[] selectionArgs){
		Cursor c = query(table, getMaxClauseFrom(column, column), selection, selectionArgs);
		if(!c.moveToFirst()) return 0;
		return CursorUtils.getInt(c);
	}
	
	public String queryMaxString(String table, String column, String selection, String[] selectionArgs){
		Cursor c = query(table, getMaxClauseFrom(column, column), selection, selectionArgs);
		if(!c.moveToFirst()) return null;
		return CursorUtils.getString(c);
	}
	
	public double queryMaxDouble(String table, String column, String selection, String[] selectionArgs){
		Cursor c = query(table, getMaxClauseFrom(column, column), selection, selectionArgs);
		if(!c.moveToFirst()) return 0;
		return CursorUtils.getDouble(c);
	}
	
	public Date queryMaxDate(String table, String column, String selection, String[] selectionArgs){
		Cursor c = query(table, getMaxClauseFrom(column, column), selection, selectionArgs);
		if(!c.moveToFirst()) return null;
		return CursorUtils.getDate(c, column);
	}
	
	public long queryMaxLong(String table, String column, String selection, String selectionArg){
		Cursor c = query(table, getMaxClauseFrom(column, column), selection, selectionArg);
		if(!c.moveToFirst()) return 0;
		return CursorUtils.getLong(c);
	}
	
	public int queryMaxInt(String table, String column, String selection, String selectionArg){
		Cursor c = query(table, getMaxClauseFrom(column, column), selection, selectionArg);
		if(!c.moveToFirst()) return 0;
		return CursorUtils.getInt(c);
	}
	
	public String queryMaxString(String table, String column, String selection, String selectionArg){
		Cursor c = query(table, getMaxClauseFrom(column, column), selection, selectionArg);
		if(!c.moveToFirst()) return null;
		return CursorUtils.getString(c);
	}
	
	public double queryMaxDouble(String table, String column, String selection, String selectionArg){
		Cursor c = query(table, getMaxClauseFrom(column, column), selection, selectionArg);
		if(!c.moveToFirst()) return 0;
		return CursorUtils.getDouble(c);
	}
	
	public Date queryMaxDate(String table, String column, String selection, String selectionArg){
		Cursor c = query(table, getMaxClauseFrom(column, column), selection, selectionArg);
		if(!c.moveToFirst()) return null;
		return CursorUtils.getDate(c, column);
	}
	
	public long queryMinLong(String table, String column, String selection, String[] selectionArgs){
		Cursor c = query(table, getMinClauseFrom(column, column), selection, selectionArgs);		
		if(!c.moveToFirst()) return 0;
		return CursorUtils.getLong(c);		
	}
	
	public int queryMinInt(String table, String column, String selection, String[] selectionArgs){
		Cursor c = query(table, getMinClauseFrom(column, column), selection, selectionArgs);
		if(!c.moveToFirst()) return 0;
		return CursorUtils.getInt(c);
	}
	
	public String queryMinString(String table, String column, String selection, String[] selectionArgs){
		Cursor c = query(table, getMinClauseFrom(column, column), selection, selectionArgs);
		if(!c.moveToFirst()) return null;
		return CursorUtils.getString(c);
	}
	
	public double queryMinDouble(String table, String column, String selection, String[] selectionArgs){
		Cursor c = query(table, getMinClauseFrom(column, column), selection, selectionArgs);
		if(!c.moveToFirst()) return 0;
		return CursorUtils.getDouble(c);
	}
	
	public Date queryMinDate(String table, String column, String selection, String[] selectionArgs){
		Cursor c = query(table, getMinClauseFrom(column, column), selection, selectionArgs);
		if(!c.moveToFirst()) return null;
		return CursorUtils.getDate(c, column);
	}
	
	public long queryMinLong(String table, String column, String selection, String selectionArg){
		Cursor c = query(table, getMinClauseFrom(column, column), selection, selectionArg);
		if(!c.moveToFirst()) return 0;
		return CursorUtils.getLong(c);
	}
	
	public int queryMinInt(String table, String column, String selection, String selectionArg){
		Cursor c = query(table, getMinClauseFrom(column, column), selection, selectionArg);
		if(!c.moveToFirst()) return 0;
		return CursorUtils.getInt(c);
	}
	
	public String queryMinString(String table, String column, String selection, String selectionArg){
		Cursor c = query(table, getMinClauseFrom(column, column), selection, selectionArg);
		if(!c.moveToFirst()) return null;
		return CursorUtils.getString(c);
	}
	
	public double queryMinDouble(String table, String column, String selection, String selectionArg){
		Cursor c = query(table, getMinClauseFrom(column, column), selection, selectionArg);
		if(!c.moveToFirst()) return 0;
		return CursorUtils.getDouble(c);
	}
	
	public Date queryMinDate(String table, String column, String selection, String selectionArg){
		Cursor c = query(table, getMinClauseFrom(column, column), selection, selectionArg);
		if(!c.moveToFirst()) return null;
		return CursorUtils.getDate(c, column);
	}
	
	public long queryMinLong(String table, String column, String selection, int selectionArg){
		return queryMinLong(table,column,selection,String.valueOf(selectionArg));
	}
	
	public int queryMinInt(String table, String column, String selection, int selectionArg){
		return queryMinInt(table,column,selection,String.valueOf(selectionArg));
	}
	
	public String queryMinString(String table, String column, String selection, int selectionArg){
		return queryMinString(table,column,selection,String.valueOf(selectionArg));
	}
	
	public double queryMinDouble(String table, String column, String selection, int selectionArg){
		return queryMinDouble(table,column,selection,String.valueOf(selectionArg));
	}
	
	public long queryMinLong(String table, String column, String selection, long selectionArg){
		return queryMinLong(table,column,selection,String.valueOf(selectionArg));
	}
	
	public int queryMinInt(String table, String column, String selection, long selectionArg){
		return queryMinInt(table,column,selection,String.valueOf(selectionArg));
	}
	
	public String queryMinString(String table, String column, String selection, long selectionArg){
		return queryMinString(table,column,selection,String.valueOf(selectionArg));
	}
	
	public double queryMinDouble(String table, String column, String selection, long selectionArg){
		return queryMaxDouble(table,column,selection,String.valueOf(selectionArg));
	}
	
	public Date queryMinDate(String table, String column, String selection, long selectionArg){
		return queryMinDate(table,column,selection,String.valueOf(selectionArg));
	}
	
	public long queryMaxLong(String table, String column, String selection, int selectionArg){
		return queryMaxLong(table,column,selection,String.valueOf(selectionArg));
	}
	
	public int queryMaxInt(String table, String column, String selection, int selectionArg){
		return queryMaxInt(table,column,selection,String.valueOf(selectionArg));
	}
	
	public String queryMaxString(String table, String column, String selection, int selectionArg){
		return queryMaxString(table,column,selection,String.valueOf(selectionArg));
	}
	
	public double queryMaxDouble(String table, String column, String selection, int selectionArg){
		return queryMaxDouble(table,column,selection,String.valueOf(selectionArg));
	}
	
	public long queryMaxLong(String table, String column, String selection, long selectionArg){
		return queryMaxLong(table,column,selection,String.valueOf(selectionArg));
	}
	
	public int queryMaxInt(String table, String column, String selection, long selectionArg){
		return queryMaxInt(table,column,selection,String.valueOf(selectionArg));
	}
	
	public String queryMaxString(String table, String column, String selection, long selectionArg){
		return queryMaxString(table,column,selection,String.valueOf(selectionArg));
	}
	
	public double queryMaxDouble(String table, String column, String selection, long selectionArg){
		return queryMaxDouble(table,column,selection,String.valueOf(selectionArg));
	}
	
	public Date queryMaxDate(String table, String column, String selection, long selectionArg){
		return queryMaxDate(table,column,selection,String.valueOf(selectionArg));
	}
	
	public long queryMaxLong(String table, String column){
		Cursor c = query(table, getMaxClauseFrom(column, column));
		return CursorUtils.getLong(c);
	}
	
	public int queryMaxInt(String table, String column){
		Cursor c = query(table, getMaxClauseFrom(column, column));
		if(!c.moveToFirst()) return 0;
		return CursorUtils.getInt(c);
	}
	
	public String queryMaxString(String table, String column){
		Cursor c = query(table, getMaxClauseFrom(column, column));
		if(!c.moveToFirst()) return null;
		return CursorUtils.getString(c);
	}
	
	public double queryMaxDouble(String table, String column){
		Cursor c = query(table, getMaxClauseFrom(column, column));
		if(!c.moveToFirst()) return 0;
		return CursorUtils.getDouble(c);
	}
	
	public Date queryMaxDate(String table, String column){
		Cursor c = query(table, getMaxClauseFrom(column, column));
		if(!c.moveToFirst()) return null;
		return CursorUtils.getDate(c, column);
	}
	
	public int queryMinInt(String table, String column){
		Cursor c = query(table, getMinClauseFrom(column, column));
		if(!c.moveToFirst()) return 0;
		return CursorUtils.getInt(c);
	}
	
	public String queryMinString(String table, String column){
		Cursor c = query(table, getMinClauseFrom(column, column));
		if(!c.moveToFirst()) return null;
		return CursorUtils.getString(c);
	}
	
	public double queryMinDouble(String table, String column){
		Cursor c = query(table, getMinClauseFrom(column, column));
		if(!c.moveToFirst()) return 0;
		return CursorUtils.getDouble(c);
	}
	
	public long queryMinLong(String table, String column){
		Cursor c = query(table, getMinClauseFrom(column, column));
		return CursorUtils.getLong(c);
	}
	
	public long insert(String table, ContentValues values){
		return insert(table, values, null);
	}
	
	public long insert(String table, ContentValues values, String nullColumnHack){
		return getDb().insert(table, nullColumnHack, values);
	}
	
	public long update(String table, ContentValues values, String whereArg) {
		return update(table, values, DEFAULT_WHERE_UPDATE, Convert.getStringArrayFromScalar(whereArg));
	}
	
	public long update(String table, ContentValues values, long whereArg) {
		return update(table, values, DEFAULT_WHERE_UPDATE, Convert.getStringArrayFromScalar(whereArg));
	}
	
	public long update(String table, ContentValues values, int whereArg) {
		return update(table, values, DEFAULT_WHERE_UPDATE, Convert.getStringArrayFromScalar(whereArg));
	}
	
	public long update(String table, ContentValues values, String whereClause, String whereArg) {		
		return getDb().update(table, values, whereClause, Convert.getStringArrayFromScalar(whereArg) );
	}
	
	public long update(String table, ContentValues values, String whereClause, String[] whereArgs) {
		return getDb().update(table, values, whereClause, whereArgs);
	}
	
	public long update(String table, ContentValues values, String whereClause, long whereArg) {		
		return getDb().update(table, values, whereClause, Convert.getStringArrayFromScalar(whereArg) );
	}
	
	public long update(String table, ContentValues values, String whereClause, int whereArg) {
		return getDb().update(table, values, whereClause, Convert.getStringArrayFromScalar(whereArg));
	}
	
	public long delete(String table){
		return delete(table, false);
	}
	
	public long delete(String table, boolean truncateAutIncrementId){		
		long returns = getDb().delete(table, null, null);
		if(truncateAutIncrementId)
			getDb().execSQL("UPDATE sqlite_sequence SET seq = 0 where name = \"" + table + "\"; ");
		return returns;
	}
	
	public long delete(String table, String whereClause, String[] whereArgs){
		return getDb().delete(table, whereClause, whereArgs);
	}
	
	public long delete(String table, String whereClause, String whereArg){
		return getDb().delete(table, whereClause, Convert.getStringArrayFromScalar(whereArg));
	}
	
	public long delete(String table, String whereClause, long whereArg){
		return getDb().delete(table, whereClause, Convert.getStringArrayFromScalar(whereArg));
	}
	
	public long delete(String table, String whereClause, int whereArg){
		return getDb().delete(table, whereClause, Convert.getStringArrayFromScalar(whereArg));
	}
	
	
	public long delete(String table, String whereArg){
		return delete(table, DEFAULT_WHERE_UPDATE ,whereArg);
	}
	
	public long delete(String table, long whereArg){
		return delete(table, String.valueOf(whereArg));
	}
	
	public long delete(String table, int whereArg){
		return delete(table, String.valueOf(whereArg));
	}
	
	
	public void execSQL(String sql){
		getDb().execSQL(sql);
	}
	
	public long getLongLastInsertRowId(){
		return SQLiteUtil.getLastInsertRowId(getDb());
	}
	
	public int getIntLastInsertRowId(){
		return Integer.parseInt( String.valueOf(SQLiteUtil.getLastInsertRowId(getDb())) );
	}
    
    public String getMaxClauseFrom(String columnName, String alias){
    	StringBuilder sb = new StringBuilder("MAX(" + columnName + ")");
    	if(alias != null && !TextUtils.isEmpty(alias)){
    		sb.append(" AS " +  alias);
    	}
    	return sb.toString();
    }
    
    public String getMinClauseFrom(String columnName, String alias){
    	StringBuilder sb = new StringBuilder("MIN(" + columnName + ")");
    	if(alias != null && !TextUtils.isEmpty(alias)){
    		sb.append(" AS " +  alias);
    	}
    	return sb.toString();
    }

    public int getVersion()
    {
        return db.getVersion();
    }

    public String getSQLiteVersion()
    {
        Cursor cursor = db.rawQuery("select sqlite_version() AS sqlite_version", null);
        String sqliteVersion = "";
        while(cursor.moveToNext()){
            sqliteVersion += cursor.getString(0);
        }
        Log.d(TAG,"SQLITE VERSION:"+sqliteVersion);
        return  sqliteVersion;
    }
}
