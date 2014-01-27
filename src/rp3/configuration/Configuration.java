package rp3.configuration;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import rp3.xml.XmlPull;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.XmlResourceParser;

public final class Configuration {	
	
	private static DataBaseConfiguration dataBaseConfiguration;
	private static AppConfiguration appConfiguration;
	
	private static boolean loaded;	
	
	public static boolean isLoaded(){
		return loaded;
	}
	
	public static void TryInitializeConfiguration(Context c){
		if(!isLoaded())
		{
			initConfiguration(c);
		}
	}
	

	public static DataBaseConfiguration getDataBaseConfiguration() {
		return dataBaseConfiguration;
	}
	
	private static void initConfiguration(Context c) {	
		
		appConfiguration = new AppConfiguration();
		
		int resourceId = c.getResources().getIdentifier("config", "xml",c.getApplicationContext().getPackageName());
		XmlResourceParser parser = c.getResources().getXml(resourceId);

		dataBaseConfiguration = new DataBaseConfiguration();

		try {
			parser.next();
			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {

				switch (eventType) {
				case XmlPullParser.START_TAG:
					
					String name = parser.getName();
															
					if(name.equalsIgnoreCase(DataBaseConfiguration.class.getSimpleName())){
						ContentValues values = XmlPull.getContentValues(parser,name);
						
						if(values.containsKey(DataBaseConfiguration.KEY_NAME))
							dataBaseConfiguration.setName(values.getAsString(DataBaseConfiguration.KEY_NAME));
						if(values.containsKey(DataBaseConfiguration.KEY_VERSION))
							dataBaseConfiguration.setVersion(values.getAsInteger(DataBaseConfiguration.KEY_VERSION));
					}
					else if(name.equalsIgnoreCase(AppConfiguration.class.getSimpleName())){
						ContentValues values = XmlPull.getContentValues(parser,name);
						appConfiguration.setValues(values);						
					}
					break;
//				case XmlPullParser.TEXT:
//
//					break;
//				case XmlPullParser.END_TAG:
//					if (parser.getName().equalsIgnoreCase(Statement.TAG_Name)) {
//					}
//					break;
				}

				parser.next();
				eventType = parser.getEventType();
			}
		} catch (XmlPullParserException e) {			
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		}

		loaded = true;
	}	

}
