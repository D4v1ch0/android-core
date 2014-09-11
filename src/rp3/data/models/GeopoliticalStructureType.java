package rp3.data.models;

import java.util.ArrayList;
import java.util.List;

import rp3.data.entity.EntityBase;
import rp3.db.sqlite.DataBase;
import rp3.util.CursorUtils;
import android.database.Cursor;

public class GeopoliticalStructureType extends EntityBase<GeopoliticalStructureType> {

	private long id;
	private int geopoliticalStructureTypeId;
	private String name;
	private int levelStructure;
	
	@Override
	public long getID() {		
		return id;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	@Override
	public boolean isAutoGeneratedId() {		
		return false;
	}

	@Override
	public String getTableName() {		
		return Contract.GeopoliticalStructureType.TABLE_NAME;
	}

	@Override
	public void setValues() {
		setValue(Contract.GeopoliticalStructureType._ID, this.id);
		setValue(Contract.GeopoliticalStructureType.COLUMN_GEOPOLITICAL_STRUCTURE_TYPE_ID, this.geopoliticalStructureTypeId);
		setValue(Contract.GeopoliticalStructureType.COLUMN_NAME, this.name);
		setValue(Contract.GeopoliticalStructureType.COLUMN_LEVEL_STRUCTURE, this.levelStructure);
	}

	@Override
	public Object getValue(String key) {
		return null;
	}

	@Override
	public String getDescription() {		
		return this.name;
	}
	
	public int getGeopoliticalStructureTypeId() {
		return geopoliticalStructureTypeId;
	}

	public void setGeopoliticalStructureTypeId(int geopoliticalStructureTypeId) {
		this.geopoliticalStructureTypeId = geopoliticalStructureTypeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLevelStructure() {
		return levelStructure;
	}

		public void setLevelStructure(int levelStructure) {
			this.levelStructure = levelStructure;
		}

	public static List<GeopoliticalStructureType> getGeopoliticalStructureType(DataBase db, String code){
		Cursor c = db.query(Contract.GeopoliticalStructureType.TABLE_NAME, new String[]{
			Contract.GeopoliticalStructureType.COLUMN_GEOPOLITICAL_STRUCTURE_TYPE_ID,
			Contract.GeopoliticalStructureType.COLUMN_NAME,
			Contract.GeopoliticalStructureType.COLUMN_LEVEL_STRUCTURE
		});
		
		List<GeopoliticalStructureType> list = new ArrayList<GeopoliticalStructureType>();
		while(c.moveToNext()){
			GeopoliticalStructureType geo = new GeopoliticalStructureType();
			geo.setGeopoliticalStructureTypeId(CursorUtils.getInt(c, Contract.GeopoliticalStructureType.FIELD_GEOPOLITICAL_STRUCTURE_TYPE_ID));
			geo.setName(CursorUtils.getString(c, Contract.GeopoliticalStructureType.FIELD_NAME));
			geo.setLevelStructure(CursorUtils.getInt(c, Contract.GeopoliticalStructureType.FIELD_LEVEL_STRUCTURE));
		
			list.add(geo);
		}
		return list;
	}
	
}
