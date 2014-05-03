package rp3.configuration;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import rp3.xml.XmlPull;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.XmlResourceParser;
import android.database.sqlite.SQLiteOpenHelper;

public final class Configuration {	
	
	private static DataBaseConfiguration dataBaseConfiguration;
	private static AppConfiguration appConfiguration;
	private static WebServiceConfiguration webServiceConfiguration;			
	//private static 
	private static boolean loaded;	
	
	public synchronized static boolean isLoaded(){
		return loaded;
	}
	
	public static void TryInitializeConfiguration(Context c, Class<? extends SQLiteOpenHelper> dbClass){
		if(!isLoaded())
		{
			initConfiguration(c, dbClass);
		}
	}	
	
	public static void TryInitializeConfiguration(Context c){
		if(!isLoaded()){
			initConfiguration(c, null);
		}
	}	

	public static PreferenceManager getParameters(){
		return null;
	}
	
	public synchronized static AppConfiguration getAppConfiguration() {	
		return appConfiguration;		
	}
	
	public synchronized static DataBaseConfiguration getDataBaseConfiguration() {	
		return dataBaseConfiguration;		
	}
	
	public synchronized static WebServiceConfiguration getWebServiceConfiguration() {		
		return webServiceConfiguration;			
	}
//	// Object to use as a thread-safe lock
//    private static final Object dataBaseConfigurationLock = new Object();
//    private static final Object webServiceConfigurationLock = new Object();
//    
	
	private static void initConfiguration(Context c,Class<? extends SQLiteOpenHelper> dbClass) {					
		
		synchronized (Configuration.class) {
			appConfiguration = new AppConfiguration();
		}
		
		synchronized (Configuration.class) {
			dataBaseConfiguration = new DataBaseConfiguration(c,dbClass);	
		}
		
		synchronized (Configuration.class) {
			webServiceConfiguration = new WebServiceConfiguration();	
		}		
		
		int resourceId = c.getResources().getIdentifier("config", "xml",c.getApplicationContext().getPackageName());
		XmlResourceParser parser = c.getResources().getXml(resourceId);
		

		try {
			boolean exitWebServiceC = false;
			
			parser.next();
			int eventType = parser.getEventType();
			String element = null;
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
					else if(name.equalsIgnoreCase(WebServiceConfiguration.class.getSimpleName())){						
						parser.nextTag();//childs	
						
						String webServideDataName = WebServiceData.class.getSimpleName();
						WebServiceData wsData = null;
						
						eventType = parser.getEventType();
						
						while(!exitWebServiceC && eventType != XmlPullParser.END_DOCUMENT){																								    						
																																																				
							switch (eventType) {
								case XmlPullParser.START_TAG:
									if(parser.getName().equalsIgnoreCase(webServideDataName))
										wsData = new WebServiceData();
									else if(parser.getName().equalsIgnoreCase("methods"))
									{
										parser.nextTag();//method
										while(parser.getName().equals("method")){
											ContentValues methodValues = XmlPull.getContentValues(parser,"method");
											WebServiceDataMethod method = new WebServiceDataMethod();
											method.setName(methodValues.getAsString("name"));
											method.setAction(methodValues.getAsString("action"));
											if(methodValues.containsKey("webMethod"))
												method.setWebMethod(methodValues.getAsString("webMethod"));
											else
												method.setWebMethod("GET");
											if(methodValues.containsKey("methodId"))
												method.setMethodId(methodValues.getAsString("methodId"));
											
											wsData.getMethods().add(method);
											
											parser.next();
										}
									}
								break;
								case XmlPullParser.TEXT:
									element = parser.getText();
									break;
								case XmlPullParser.END_TAG:
									String key = parser.getName();										
									
									if(key.equalsIgnoreCase(WebServiceData.KEY_TYPE))
										wsData.setType(element);
									else if(key.equalsIgnoreCase(WebServiceData.KEY_NAME))
										wsData.setName(element);
									else if(key.equalsIgnoreCase(WebServiceData.KEY_NAMESPACE))
										wsData.setNamespace(element);
									else if(key.equalsIgnoreCase(WebServiceData.KEY_SOAPVERSION))
										wsData.setSoapVersion(element);
									else if(key.equalsIgnoreCase(WebServiceData.KEY_URL))
										wsData.setUrl(element);
									else if(key.equalsIgnoreCase(WebServiceData.KEY_DOTNET))
										wsData.setDotNet("TRUE".equalsIgnoreCase(element));
									else if(key.equalsIgnoreCase(webServideDataName))
										webServiceConfiguration.addWebServiceData(wsData);
									else if(key.equalsIgnoreCase(WebServiceConfiguration.class.getSimpleName()))
										exitWebServiceC = true;								
									break;
							}
							
							if(exitWebServiceC) break;
							
							parser.next();
							eventType = parser.getEventType();
							
//							ContentValues wsValues = XmlPull.getContentValues(parser,webServideDataName);		
//														
//							final WebServiceData wsData = new WebServiceData();
//							wsData.setType(wsValues.getAsString(WebServiceData.KEY_TYPE));								
//							wsData.setName(wsValues.getAsString(WebServiceData.KEY_NAME));
//							
//							if(wsValues.containsKey(WebServiceData.KEY_NAMESPACE))
//								wsData.setNamespace(wsValues.getAsString(WebServiceData.KEY_NAMESPACE));
//							if(wsValues.containsKey(WebServiceData.KEY_URL))
//								wsData.setUrl(wsValues.getAsString(WebServiceData.KEY_URL));
//							if(wsValues.containsKey(WebServiceData.KEY_TYPE))
//								wsData.setType(wsValues.getAsString(WebServiceData.KEY_TYPE));
//							if(wsValues.containsKey(WebServiceData.KEY_DOTNET))
//								wsData.setDotNet(wsValues.getAsString(WebServiceData.KEY_DOTNET).equalsIgnoreCase("YES"));
//							if(wsValues.containsKey(WebServiceData.KEY_SOAPVERSION))
//								wsData.setSoapVersion(wsValues.getAsString(WebServiceData.KEY_SOAPVERSION));
//															
//																																																															
						}					
						
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
