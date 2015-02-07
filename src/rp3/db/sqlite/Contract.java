package rp3.db.sqlite;

import android.provider.BaseColumns;

public final class Contract {
	
	public static abstract class SyncHistory implements BaseColumns{
		public static final String TABLE_NAME = "tbSyncHistory";
		
		public static final String COLUMN_SYNC_DATE = "SyncDate";
		public static final String COLUMN_CATEGORY = "Category";
		public static final String COLUMN_EVENT = "Event";
		public static final String COLUMN_NOTES = "Notes";
		public static final String COLUMN_USER = "User";
		
		public static final String FIELD_DATE = COLUMN_SYNC_DATE;
		public static final String FIELD_CATEGORY = COLUMN_CATEGORY;
		public static final String FIELD_EVENT = COLUMN_EVENT;
		public static final String FIELD_NOTES = COLUMN_NOTES;
		public static final String FIELD_USER = COLUMN_USER;
	}
	
	public static abstract class User implements BaseColumns {
        public static final String TABLE_NAME = "tbUser";
        public static final String COLUMN_LOGONNAME = "LogonName";
        public static final String COLUMN_FULLNAME = "FullName";
        public static final String COLUMN_ISLOGGED = "IsLogged";
        public static final String COLUMN_CURRENT = "IsCurrent";
        
        public static final String FIELD_LOGONNAME = COLUMN_LOGONNAME;
        public static final String FIELD_FULLNAME = COLUMN_FULLNAME;
        public static final String FIELD_ISLOGGED = COLUMN_ISLOGGED;
        public static final String FIELD_CURRENT = COLUMN_CURRENT;
        
        public static final String CREATE_TABLE = "CREATE TABLE tbUser " +
        	_ID + DataBase.INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT " + DataBase.COMMA_SEP +
        	COLUMN_LOGONNAME + DataBase.TEXT_TYPE + DataBase.COMMA_SEP +
        	COLUMN_FULLNAME + DataBase.TEXT_TYPE + DataBase.COMMA_SEP +
        	COLUMN_ISLOGGED + DataBase.INTEGER_TYPE + DataBase.COMMA_SEP +
        	COLUMN_CURRENT + DataBase.INTEGER_TYPE + ");";
        	
    }		
	
}
