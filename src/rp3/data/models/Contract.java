package rp3.data.models;

import rp3.db.sqlite.DataBase;
import android.provider.BaseColumns;

public final class Contract {
	
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
