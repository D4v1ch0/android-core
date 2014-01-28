package rp3.db.sqlite;

import rp3.util.Convert;
import rp3.util.CursorUtils;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

public class DataBase {

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
	
	public DataBase(Context c, SQLiteOpenHelper helper){
		dbOpenHelper = helper;		
		this.c = c;
	}	
	
	private SQLiteDatabase getDb(){
		if(db==null){
			db = dbOpenHelper.getWritableDatabase();
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
	
	public Cursor query(String table, String[] columns)
	{
		return query(table, columns, null, null, null, null,null);
	}
	
	public Cursor query(String table, String[] columns, String selection, String[] selectionArgs)
	{
		return query(table, columns, selection, selectionArgs, null, null,null);
	}
	
	public Cursor query(String table, String[] columns, String selection, String selectionArg)
	{
		return query(table, columns, selection, Convert.getStringArrayFromScalar(selectionArg), null, null,null);
	}
	
	public Cursor query(String table, String column, String selection, String selectionArg)
	{
		return query(table, Convert.getStringArrayFromScalar(column), 
				selection, Convert.getStringArrayFromScalar(selectionArg), null, null,null);
	}
	
	public Cursor query(String table, String column, String selection, String[] selectionArgs)
	{
		return query(table, Convert.getStringArrayFromScalar(column), 
				selection, selectionArgs, null, null,null);
	}
	
	public Cursor query(String table, String[] columns, String selection, boolean selectionArg)
	{
		return query(table, columns, selection, Convert.getStringArrayFromScalar(selectionArg), null, null,null);
	}
	
	public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, 
			String having, String orderBy){
		return getDb().query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
	}
	
	public long queryMaxLong(String table, String column, String selection, String[] selectionArgs){
		Cursor c = query(table, column, selection, selectionArgs);
		return CursorUtils.getLong(c);
	}
	
	public int queryMaxInt(String table, String column, String selection, String[] selectionArgs){
		Cursor c = query(table, column, selection, selectionArgs);
		return CursorUtils.getInt(c);
	}
	
	public String queryMaxString(String table, String column, String selection, String[] selectionArgs){
		Cursor c = query(table, column, selection, selectionArgs);
		return CursorUtils.getString(c);
	}
	
	public double queryMaxDouble(String table, String column, String selection, String[] selectionArgs){
		Cursor c = query(table, column, selection, selectionArgs);
		return CursorUtils.getDouble(c);
	}
	
	public long queryMaxLong(String table, String column, String selection, String selectionArg){
		Cursor c = query(table, column, selection, selectionArg);
		return CursorUtils.getLong(c);
	}
	
	public int queryMaxInt(String table, String column, String selection, String selectionArg){
		Cursor c = query(table, column, selection, selectionArg);
		return CursorUtils.getInt(c);
	}
	
	public String queryMaxString(String table, String column, String selection, String selectionArg){
		Cursor c = query(table, column, selection, selectionArg);
		return CursorUtils.getString(c);
	}
	
	public double queryMaxDouble(String table, String column, String selection, String selectionArg){
		Cursor c = query(table, column, selection, selectionArg);
		return CursorUtils.getDouble(c);
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
	
	public long queryMaxLong(String table, String column){
		Cursor c = query(table, column);
		return CursorUtils.getLong(c);
	}
	
	public int queryMaxInt(String table, String column){
		Cursor c = query(table, column);
		return CursorUtils.getInt(c);
	}
	
	public String queryMaxString(String table, String column){
		Cursor c = query(table, column);
		return CursorUtils.getString(c);
	}
	
	public double queryMaxDouble(String table, String column){
		Cursor c = query(table, column);
		return CursorUtils.getDouble(c);
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
}
