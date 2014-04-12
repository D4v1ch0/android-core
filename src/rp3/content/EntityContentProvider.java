package rp3.content;

import rp3.data.entity.EntityBase;
import rp3.db.sqlite.DataBase;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class EntityContentProvider<T extends EntityBase<?>> extends ContentProvider {

	DataBase db;
	
	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		
		return 0;
	}

	@Override
	public String getType(Uri arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri arg0, ContentValues arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onCreate() {
		//db = DataBaseServiceHelper.getDataBaseInstance(this.getContext(), );
		return false;
	}

	@Override
	public Cursor query(Uri arg0, String[] arg1, String arg2, String[] arg3,
			String arg4) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {		
		return 0;
	}

	
	
}
