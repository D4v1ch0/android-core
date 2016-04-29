package rp3.data.models;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;

import rp3.data.Dictionary;
import rp3.data.Identifiable;
import rp3.data.entity.EntityBase;
import rp3.db.sqlite.DataBase;
import rp3.util.Convert;
import rp3.util.CursorUtils;

public class GeneralValue extends EntityBase<GeneralValue> implements Identifiable {

	private long id;
	private String code;
	private String value;
	private long generalTableId;
	private String reference1;
	private String reference2;
	private String reference3;
	private String reference4;
	private String reference5;
	private int order;
	
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
		return true;
	}

	@Override
	public String getTableName() {		
		return Contract.GeneralValue.TABLE_NAME;
	}

	@Override
	public void setValues() {
		setValue(Contract.GeneralValue.COLUMN_CODE, this.code);
		setValue(Contract.GeneralValue.COLUMN_GENERAL_TABLE_ID, this.generalTableId);
		setValue(Contract.GeneralValue.COLUMN_VALUE, this.value);
		setValue(Contract.GeneralValue.COLUMN_REFERENCE1, this.reference1);
		setValue(Contract.GeneralValue.COLUMN_REFERENCE2, this.reference2);
		setValue(Contract.GeneralValue.COLUMN_REFERENCE3, this.reference3);
		setValue(Contract.GeneralValue.COLUMN_REFERENCE4, this.reference4);
		setValue(Contract.GeneralValue.COLUMN_REFERENCE5, this.reference5);
		setValue(Contract.GeneralValue.COLUMN_ORDER, this.order);
	}

	@Override
	public Object getValue(String key) {
		if(key.equals(Contract.GeneralValue.COLUMN_CODE))
			return this.code;
		else if(key.equals(Contract.GeneralValue.COLUMN_GENERAL_TABLE_ID))
			return this.generalTableId;
		else if(key.equals(Contract.GeneralValue.COLUMN_VALUE))
			return this.value;
		else if(key.equals(Contract.GeneralValue.COLUMN_REFERENCE1))
			return this.reference1;
		else if(key.equals(Contract.GeneralValue.COLUMN_REFERENCE2))
			return this.reference2;
		else if(key.equals(Contract.GeneralValue.COLUMN_REFERENCE3))
			return this.reference3;
		else if(key.equals(Contract.GeneralValue.COLUMN_REFERENCE4))
			return this.reference4;
		else if(key.equals(Contract.GeneralValue.COLUMN_REFERENCE5))
			return this.reference5;
		else if(key.equals(Contract.GeneralValue.COLUMN_ORDER))
			return this.order;
		return null;
	}

	@Override
	public String getDescription() {	
		return this.code;
	}	
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public long getGeneralTableId() {
		return generalTableId;
	}

	public void setGeneralTableId(long generalTableId) {
		this.generalTableId = generalTableId;
	}

	public String getReference1() {
		return reference1;
	}

	public void setReference1(String reference1) {
		this.reference1 = reference1;
	}

	public String getReference2() {
		return reference2;
	}

	public void setReference2(String reference2) {
		this.reference2 = reference2;
	}

	public String getReference3() {
		return reference3;
	}

	public void setReference3(String reference3) {
		this.reference3 = reference3;
	}

	public String getReference4() {
		return reference4;
	}

	public void setReference4(String reference4) {
		this.reference4 = reference4;
	}

	public String getReference5() {
		return reference5;
	}

	public void setReference5(String reference5) {
		this.reference5 = reference5;
	}
	
	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	private static GeneralValue getGeneralValue(Cursor c){
		GeneralValue gv = new GeneralValue();
		
		gv.setValue( CursorUtils.getString(c, Contract.GeneralValue.COLUMN_VALUE) );
		gv.setCode( CursorUtils.getString(c, Contract.GeneralValue.COLUMN_CODE) );
		gv.setGeneralTableId( CursorUtils.getLong(c, Contract.GeneralValue.COLUMN_GENERAL_TABLE_ID) );
		gv.setID( CursorUtils.getLong(c, Contract.GeneralValue._ID) );
		gv.setReference1( CursorUtils.getString(c, Contract.GeneralValue.COLUMN_REFERENCE1) );
		gv.setReference2( CursorUtils.getString(c, Contract.GeneralValue.COLUMN_REFERENCE2) );
		gv.setReference3( CursorUtils.getString(c, Contract.GeneralValue.COLUMN_REFERENCE3) );
		gv.setReference4( CursorUtils.getString(c, Contract.GeneralValue.COLUMN_REFERENCE4) );
		gv.setReference5( CursorUtils.getString(c, Contract.GeneralValue.COLUMN_REFERENCE5) );
		gv.setOrder( CursorUtils.getInt(c, Contract.GeneralValue.COLUMN_ORDER) );
		
		return gv;
	}
	
	public static GeneralValue getGeneralValue(DataBase db, String code){
		Cursor c = db.query(Contract.GeneralValue.TABLE_NAME, getSelectColumns()			
				,  Contract.GeneralValue.COLUMN_CODE + " = ? ", code);					
		
		if(c.moveToFirst()){
			return getGeneralValue(c);
		}
		
		return null;
	}

    public static GeneralValue getGeneralValue(DataBase db, long generalTableId, String code){
        Cursor c = db.query(Contract.GeneralValue.TABLE_NAME, getSelectColumns()
                ,  Contract.GeneralValue.COLUMN_CODE + " = ? AND " + Contract.GeneralValue.COLUMN_GENERAL_TABLE_ID + " = ?",
                new String[] {code + "", generalTableId + ""});

        if(c.moveToFirst()){
            return getGeneralValue(c);
        }

        return null;
    }
	
	public static Dictionary<String, String> getDictionary(DataBase db, long generalTableId){
		List<GeneralValue> gvs = getGeneralValues(db, generalTableId, false);
		Dictionary<String, String> dic = new Dictionary<String, String>();
		for(GeneralValue gv : gvs){
			dic.set(gv.getCode(), gv.getValue());
		}
		return dic;
	}
	
	public static List<GeneralValue> getGeneralValues(DataBase db, long generalTableId){
		return getGeneralValues(db, generalTableId, false);
	}
	
	public static List<GeneralValue> getGeneralValues(DataBase db, long generalTableId, boolean useOrderByColumn){
		List<GeneralValue> gv = new ArrayList<GeneralValue>();
		
		String orderQuery = "";
		if(useOrderByColumn)
			orderQuery = Contract.GeneralValue.COLUMN_ORDER;
		else
			orderQuery = Contract.GeneralValue.COLUMN_VALUE;
		
		Cursor c = db.query(Contract.GeneralValue.TABLE_NAME, getSelectColumns()			
		,  Contract.GeneralValue.COLUMN_GENERAL_TABLE_ID + " = ? ", 
			Convert.getStringArrayFromScalar(generalTableId),
			null,null,
			orderQuery);
		
		if(c.moveToFirst()){
			do{
				gv.add(getGeneralValue(c));
			}while(c.moveToNext());
		}
		
		return gv;
	}

	public static List<GeneralValue> getGeneralValuesOrder(DataBase db, long generalTableId, String orderColumn){
		List<GeneralValue> gv = new ArrayList<GeneralValue>();

		Cursor c = db.query(Contract.GeneralValue.TABLE_NAME, getSelectColumns()
				,  Contract.GeneralValue.COLUMN_GENERAL_TABLE_ID + " = ? ",
				Convert.getStringArrayFromScalar(generalTableId),
				null,null,
				orderColumn);

		if(c.moveToFirst()){
			do{
				gv.add(getGeneralValue(c));
			}while(c.moveToNext());
		}

		return gv;
	}

	public static List<GeneralValue> getGeneralValues(DataBase db, long generalTableId, String codeException){
		List<GeneralValue> gv = new ArrayList<GeneralValue>();


		Cursor c = db.query(Contract.GeneralValue.TABLE_NAME, getSelectColumns()
				,  Contract.GeneralValue.COLUMN_GENERAL_TABLE_ID + " = ? AND " + Contract.GeneralValue.COLUMN_CODE + " NOT IN (?)",
				new String[] {generalTableId + "", codeException},
				null,null,
				null);

		if(c.moveToFirst()){
			do{
				gv.add(getGeneralValue(c));
			}while(c.moveToNext());
		}

		return gv;
	}

	private static String[] getSelectColumns(){
		return new String[] {Contract.GeneralValue._ID,
		Contract.GeneralValue.COLUMN_CODE,
		Contract.GeneralValue.COLUMN_GENERAL_TABLE_ID,
		Contract.GeneralValue.COLUMN_VALUE,
		Contract.GeneralValue.COLUMN_REFERENCE1,
		Contract.GeneralValue.COLUMN_REFERENCE2,
		Contract.GeneralValue.COLUMN_REFERENCE3,
		Contract.GeneralValue.COLUMN_REFERENCE4,
		Contract.GeneralValue.COLUMN_REFERENCE5,
		Contract.GeneralValue.COLUMN_ORDER};
	}
	
	public static boolean delete(DataBase db, long generalTableId){
		return db.delete(Contract.GeneralValue.TABLE_NAME, Contract.GeneralValue.COLUMN_GENERAL_TABLE_ID + " = ? ", generalTableId) != 0;
	}
	
}
