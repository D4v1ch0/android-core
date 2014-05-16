package rp3.db.sqlite;

import android.content.Context;

public class DbMasterOpenHelper extends DataBaseOpenHelper {


	public static final String CREATE_DATABASE_MASTER_STATEMENT_FILE = "sql_master_db_create";
	public static final int VERSION = 1;
	
	public DbMasterOpenHelper(Context context) {
		super(context, "RP3MASTER",VERSION);	
		this.context = context;
	}
	
	
	@Override
	public String getCreateDataBaseFileName() {		
		return CREATE_DATABASE_MASTER_STATEMENT_FILE;
	}
}
