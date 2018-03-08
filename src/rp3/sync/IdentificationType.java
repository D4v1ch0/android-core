package rp3.sync;

import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.transport.HttpResponseException;

import rp3.connection.HttpConnection;
import rp3.connection.WebService;
import rp3.content.SyncAdapter;
import rp3.data.models.Contract;
import rp3.db.sqlite.DataBase;

public class IdentificationType {

	public static final String TAG = IdentificationType.class.getSimpleName();
	public static int executeSync(DataBase db){
		WebService webService = new WebService("Core","GetIdentificationTypes");
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
			Log.d(TAG,"Cantidad de IdentificationType:"+gvs.length());
			if(gvs==null){
				return SyncAdapter.SYNC_EVENT_AUTH_ERROR;
			}
			if(gvs.length()==0){
				return SyncAdapter.SYNC_EVENT_AUTH_ERROR;
			}
			rp3.data.models.IdentificationType.deleteAll(db, Contract.IdentificationType.TABLE_NAME);
			
			for(int i=0; i < gvs.length(); i++){
				
				try {
					JSONObject gv = gvs.getJSONObject(i);
					
					rp3.data.models.IdentificationType model = new rp3.data.models.IdentificationType();
					model.setID(gv.getLong("IdentificationTypeId"));
					model.setName(gv.getString("Name"));
					model.setApplyNaturalPersonOnly(gv.getBoolean("ApplyNaturalPersonOnly"));
					if (!gv.isNull("UseCustomValidator")) {
						model.setUseCustomValidator(gv.getBoolean("UseCustomValidator"));
					} else {
						model.setUseCustomValidator(false);
					}
					if (!gv.isNull("Length")) {
						model.setLenght(gv.getInt("Length"));
					} else {
						model.setLenght(0);
					}
					if (!gv.isNull("MaskType")) {
						model.setMaskType(gv.getInt("MaskType"));
					} else {
						model.setMaskType(0);
					}
					if (!gv.isNull("Mask")) {
						model.setMask(gv.getString("Mask"));
					} else {
						model.setMask(null);
					}
					if (!gv.isNull("RegExValidator")) {
						model.setRegExValidator(gv.getString("RegExValidator"));
					} else {
						model.setRegExValidator(null);
					}

					Log.d(TAG,model.toString());
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
