package rp3.data.models;

import android.provider.BaseColumns;

public class Contract {

	public abstract class GeneralTable implements BaseColumns{
		public static final String TABLE_NAME = "tbGeneralTable";
		
		public static final String COLUMN_NAME = "Name";
		
		public static final String FIELD_NAME = COLUMN_NAME;
	}
	
	public abstract class GeneralValue implements BaseColumns{
		public static final String TABLE_NAME = "tbGeneralValue";
		
		public static final String COLUMN_GENERAL_TABLE_ID = "GeneralTableId";		
		public static final String COLUMN_CODE = "Code";
		public static final String COLUMN_VALUE = "Value";
		public static final String COLUMN_REFERENCE1 = "Reference1";
		public static final String COLUMN_REFERENCE2 = "Reference2";
		public static final String COLUMN_REFERENCE3 = "Reference3";
		public static final String COLUMN_REFERENCE4 = "Reference4";
		public static final String COLUMN_REFERENCE5 = "Reference5";
		
		public static final String FIELD_GENERAL_TABLE_ID = COLUMN_GENERAL_TABLE_ID;		
		public static final String FIELD_CODE = COLUMN_CODE;
		public static final String FIELD_VALUE = COLUMN_VALUE;
		public static final String FIELD_REFERENCE1 = COLUMN_REFERENCE1;
		public static final String FIELD_REFERENCE2 = COLUMN_REFERENCE2;
		public static final String FIELD_REFERENCE3 = COLUMN_REFERENCE3;
		public static final String FIELD_REFERENCE4 = COLUMN_REFERENCE4;
		public static final String FIELD_REFERENCE5 = COLUMN_REFERENCE5;		
		
	}
	
}
