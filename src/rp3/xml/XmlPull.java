package rp3.xml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import rp3.db.Statement;

import android.content.ContentValues;
import android.content.res.XmlResourceParser;

public class XmlPull {

	public static List<Statement> getStatements(XmlResourceParser parser){
		List<Statement> elements = new ArrayList<Statement>();
							
		try {
			String name = null;
			String content = null;
			parser.next();
			int eventType = parser.getEventType();
			
			while(eventType != XmlPullParser.END_DOCUMENT){				
				
				switch (eventType) {
					case XmlPullParser.START_TAG:
						name = parser.getAttributeValue(null, "name");
						break;
					case XmlPullParser.TEXT:
						content = parser.getText();
						break;
					case XmlPullParser.END_TAG:
						if(parser.getName().equalsIgnoreCase(Statement.TAG_Name)){
							if(content!=null && name !=null){
								Statement s = new Statement();
								s.setName(name);
								s.setQuery(content);
								elements.add(s);
							}																
						}
						break;				
				}
				
				parser.next();
				eventType = parser.getEventType();
			}
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return elements;
	}
	
	public static List<String> getTextElements(XmlResourceParser parser, String elementName){
		List<String> elements = new ArrayList<String>();
							
		try {
			String element = null;
			parser.next();
			int eventType = parser.getEventType();
			
			while(eventType != XmlPullParser.END_DOCUMENT){				
				
				switch (eventType) {
					case XmlPullParser.TEXT:
						element = parser.getText();
						break;
					case XmlPullParser.END_TAG:
						if(parser.getName().equalsIgnoreCase(elementName)){
							if(element!=null)
								elements.add(element);
						}
						break;				
				}
				
				parser.next();
				eventType = parser.getEventType();
			}
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return elements;
	}
	
	
	public static ContentValues getContentValues(XmlResourceParser parser, String stopElementName){
		ContentValues values = new ContentValues();
		boolean exit = false;			
		try {
			String element = null;
			parser.next();
			int eventType = parser.getEventType();
			
			while(eventType != XmlPullParser.END_DOCUMENT){				
				
				switch (eventType) {
					case XmlPullParser.TEXT:
						element = parser.getText();
						break;
					case XmlPullParser.END_TAG:
						
						if(parser.getName().equalsIgnoreCase(stopElementName)) {
							exit = true;
						}
						else{
							values.put(parser.getName(), element);
						}
						break;				
				}
				
				if(exit) break;
				
				parser.next();
				eventType = parser.getEventType();
			}
		} catch (XmlPullParserException e) {			
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		}
		
		return values;
	}
	
}
