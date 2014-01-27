package rp3.configuration;

import android.content.ContentValues;

public class AppConfiguration {		
	
	private ContentValues configurations;
	
	public AppConfiguration(){		
	}
	
	public ContentValues getValues(){
		return configurations;
	}
	
	void setValues(ContentValues v){
		configurations = v;
	}
}
