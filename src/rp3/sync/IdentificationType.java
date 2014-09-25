package rp3.sync;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.transport.HttpResponseException;
import org.xmlpull.v1.XmlPullParserException;

import rp3.connection.WebService;
import rp3.content.SyncAdapter;
import rp3.data.models.Contract;
import rp3.db.sqlite.DataBase;

public class IdentificationType {

	public static int executeSync(DataBase db){
		WebService webService = new WebService("Core","GetIdentificationTypes");
		try
		{			
			webService.addCurrentAuthToken();
			
			try {
				webService.invokeWebService();
			} catch (HttpResponseException e) {
				return SyncAdapter.SYNC_EVENT_HTTP_ERROR;
			} catch (IOException e) {
				return SyncAdapter.SYNC_EVENT_ERROR;
			} catch (XmlPullParserException e) {
				return SyncAdapter.SYNC_EVENT_ERROR;
			}		
			
			JSONArray gvs = webService.getJSONArrayResponse();			
			
			rp3.data.models.IdentificationType.deleteAll(db, Contract.IdentificationType.TABLE_NAME);			
			
			for(int i=0; i < gvs.length(); i++){
				
				try {
					JSONObject gv = gvs.getJSONObject(i);
					
					rp3.data.models.IdentificationType model = new rp3.data.models.IdentificationType();
					model.setID(gv.getLong("IdentificationTypeId"));
					model.setName(gv.getString("Name"));
					model.setApplyNaturalPersonOnly(gv.getBoolean("ApplyNaturalPersonOnly"));					
					
					rp3.data.models.IdentificationType.insert(db, model);
					
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
