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

/**
 * Created by magno_000 on 18/03/2015.
 */
public class TestConnection {
    public static boolean executeSync(){
        WebService webService = new WebService("Core","GetConnection");
        boolean gvs = false;
        try
        {
            //webService.addCurrentAuthToken();
            webService.setTimeOut(15000);

            try {
                webService.invokeWebService();
            } catch (HttpResponseException e) {
                if(e.getStatusCode() == HttpConnection.HTTP_STATUS_UNAUTHORIZED)
                    return false;
                return false;
            } catch (Exception e) {
                return false;
            }

            gvs = webService.getBooleanResponse();

        }finally{
            webService.close();
        }

        return gvs;
    }

}
