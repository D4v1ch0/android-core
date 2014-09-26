package rp3.sync;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.transport.HttpResponseException;

import rp3.connection.HttpConnection;
import rp3.connection.WebService;
import rp3.content.SyncAdapter;
import rp3.data.models.Contract;
import rp3.data.models.GeopoliticalStructureType;
import rp3.db.sqlite.DataBase;

public class GeopoliticalStructure {

	public static int executeSync(DataBase db){
		WebService webService = new WebService("Core","GetGeopoliticalStructures");
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
			
			JSONArray types = webService.getJSONArrayResponse();			
			
			rp3.data.models.GeopoliticalStructureType.deleteAll(db, Contract.GeopoliticalStructureType.TABLE_NAME);
			rp3.data.models.GeopoliticalStructure.deleteAll(db, Contract.GeopoliticalStructure.TABLE_NAME);
			
			for(int i=0; i < types.length(); i++){
				
				try {
					JSONObject type = types.getJSONObject(i);
					rp3.data.models.GeopoliticalStructureType modelType = new GeopoliticalStructureType();
					modelType.setName(type.getString("Name"));
					modelType.setID(type.getLong("GeopoliticalStructureTypeId"));
					modelType.setLevelStructure(type.getInt("LevelStructure"));
					
					rp3.data.models.GeopoliticalStructureType.insert(db, modelType);
					
					JSONArray strs = type.getJSONArray("GeopoliticalStructures");
					
					for(int j=0; j < strs.length(); j++){
						JSONObject str = strs.getJSONObject(j);
						rp3.data.models.GeopoliticalStructure modelStr = new rp3.data.models.GeopoliticalStructure();						
						modelStr.setID(str.getLong("GeopoliticalStructureId"));
						modelStr.setIsoCode(str.getString("IsoCode"));
						modelStr.setLatitude( str.isNull("Latitude") ? null : Double.parseDouble(str.getString("Latitude")));
						modelStr.setLongitude( str.isNull("Longitude") ? null : Double.parseDouble(str.getString("Longitude")));
						modelStr.setName(str.getString("Name"));
						modelStr.setParentGeopoliticalStructureId(str.isNull("ParentGeopoliticalStructureId")? null : str.getLong("ParentGeopoliticalStructureId") );
						
						rp3.data.models.GeopoliticalStructure.insert(db, modelStr);
					}
					
					
					
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
