package rp3.configuration;

import android.content.ContentValues;

public class AppConfiguration {		
	
	public static final String KEY_HELP = "help";
    public static final String KEY_MAIL = "mail";
	public static final String KEY_ALLOW_SETTINGS_ON_AUT = "allowSettingsOnAut";
	public static final String KEY_ALLOW_HELP_ON_AUT = "allowHelpOnAut";
	public static final String KEY_WEB_KEY = "webkey";
	
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
		if(configurations.containsKey(key))
			return configurations.getAsString(key);
		else
			return null;
	}
	
	public String getHelpUrl(){
		return get(KEY_HELP);
	}

    public String getMail(){
        return get(KEY_MAIL);
    }
	
	public boolean allowSettingsOnAut(){
		return "TRUE".equals(get(KEY_ALLOW_SETTINGS_ON_AUT));
	}
	
	public boolean allowHelpOnAut(){
		return "TRUE".equals(get(KEY_ALLOW_HELP_ON_AUT));
	}

	public String getWebKey(){
		return get(KEY_WEB_KEY);
	}
	
}
