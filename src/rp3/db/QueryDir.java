package rp3.db;

import java.util.ArrayList;
import java.util.List;

import rp3.xml.XmlPull;

import android.content.Context;
import android.content.res.XmlResourceParser;

public class QueryDir {

	public static final String QUERY_DATABASE_STATEMENT_FILE = "sql_db_query";
	public static final String QUERY_DATABASE_STATEMENT_FILE_CORE = "sql_db_query_ext";
	
	static ArrayList<String> queryNames;
	static ArrayList<String> queryContents;
	
	private static Context context;
	
	public static boolean isLoaded() {
		return queryNames != null;
	}
	
	public static String getQuery(String queryName){
		return queryContents.get( queryNames.indexOf(queryName) );
	}
	
	public static void PrepareQueries(Context c){		
		context = c;
		
		queryNames = new ArrayList<String>(); 
		queryContents = new ArrayList<String>();
		
		int resourceId = context.getResources().getIdentifier(QUERY_DATABASE_STATEMENT_FILE, "xml", context.getApplicationContext().getPackageName());		
		XmlResourceParser parser = context.getResources().getXml(resourceId);
		List<Statement> statements = XmlPull.getStatements(parser);
		
		for(Statement s: statements){
			queryNames.add(s.getName());
			queryContents.add(s.getQuery());
		}
		
		resourceId = context.getResources().getIdentifier(QUERY_DATABASE_STATEMENT_FILE_CORE, "xml", context.getApplicationContext().getPackageName());		
		parser = context.getResources().getXml(resourceId);
		statements = XmlPull.getStatements(parser);
		
		for(Statement s: statements){
			queryNames.add(s.getName());
			queryContents.add(s.getQuery());
		}
		
		
	}	
}
