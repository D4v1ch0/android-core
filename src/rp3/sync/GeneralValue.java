package rp3.sync;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.transport.HttpResponseException;

import rp3.connection.HttpConnection;
import rp3.connection.WebService;
import rp3.content.SyncAdapter;
import rp3.data.models.Contract;
import rp3.db.sqlite.DataBase;

public class GeneralValue {	
	
	public static int executeSync(DataBase db){
		WebService webService = new WebService("Core","GetGeneralValues");
		try
		{			
			webService.addCurrentAuthToken();
			
			try {
				webService.invokeWebService();
			} catch (HttpResponseException e) {
				if(e.getStatusCode() == HttpConnection.HTTP_STATUS_UNAUTHORIZED)
					return SyncAdapter.SYNC_EVENT_AUTH_ERROR;
				return SyncAdapter.SYNC_EVENT_HTTP_ERROR;
			} catch (Exception e) {
				return SyncAdapter.SYNC_EVENT_ERROR;
			}
			
			JSONArray gvs = webService.getJSONArrayResponse();			
			
			rp3.data.models.GeneralValue.deleteAll(db, Contract.GeneralValue.TABLE_NAME);			
			
			for(int i=0; i < gvs.length(); i++){
				
				try {
					JSONObject gv = gvs.getJSONObject(i);
					
					rp3.data.models.GeneralValue model = new rp3.data.models.GeneralValue();
					model.setGeneralTableId(gv.getLong("Id"));
					model.setCode(gv.getString("Code"));
					model.setValue(gv.getString("Content"));
					model.setReference1(gv.getString("Reference01"));
					model.setReference2(gv.getString("Reference02"));
					model.setReference3(gv.getString("Reference03"));
					model.setReference4(gv.getString("Reference04"));
					model.setReference5(gv.getString("Reference05"));
					
					rp3.data.models.GeneralValue.insert(db, model);
					
				} catch (JSONException e) {
					
					return SyncAdapter.SYNC_EVENT_ERROR;
				}
				
			}
		}finally{
			webService.close();
		}
		
		return SyncAdapter.SYNC_EVENT_SUCCESS;		
	}
}
