package rp3.configuration;

import android.content.ContentValues;

public class AppConfiguration {		
	
	public static final String KEY_HELP = "help";
	public static final String KEY_ALLOW_SETTINGS_ON_AUT = "allowSettingsOnAut";
	public static final String KEY_ALLOW_HELP_ON_AUT = "allowHelpOnAut";	
	
	private ContentValues configurations;	
	
	public AppConfiguration(){		
	}
	
	public ContentValues getValues(){
		return configurations;
	}
	
	void setValues(ContentValues v){
		configurations = v;
	}
	
	public String get(String key){
		return configurations.getAsString(key);
	}
	
	public String getHelpUrl(){
		return get(KEY_HELP);
	}
	
	public boolean allowSettingsOnAut(){
		return "TRUE".equals(get(KEY_ALLOW_SETTINGS_ON_AUT));
	}
	
	public boolean allowHelpOnAut(){
		return "TRUE".equals(get(KEY_ALLOW_HELP_ON_AUT));
	}
	
}
