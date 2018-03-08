package rp3.sync;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.transport.HttpResponseException;

import java.util.ArrayList;
import java.util.List;

import rp3.connection.HttpConnection;
import rp3.connection.WebService;
import rp3.content.SyncAdapter;
import rp3.data.models.Contract;
import rp3.db.sqlite.DataBase;

public class GeneralValue {

    private static final String TAG = GeneralValue.class.getSimpleName();
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
            Log.d(TAG,"Cantidad de values:"+gvs.length());
            try {
                if(gvs.length()>0){
                    rp3.data.models.GeneralValue.deleteAll(db, Contract.GeneralValue.TABLE_NAME);
                    TypeToken<List<rp3.util.GeneralValue>> typeToken = new TypeToken<List<rp3.util.GeneralValue>>(){};
                    List<rp3.util.GeneralValue> list = new Gson().fromJson(gvs.toString(),typeToken.getType());
                    for(rp3.util.GeneralValue gv:list){
                        if(gv.getReference10()!=null){
                            if(gv.getReference10().equalsIgnoreCase("Rp3AunaApp")){
                                rp3.data.models.GeneralValue model = new rp3.data.models.GeneralValue();
                                model.setGeneralTableId(gv.getId());
                                model.setCode(gv.getCode());
                                model.setValue(gv.getContent());
                                if(gv.getReference01()!=null){
                                    model.setReference1(gv.getReference01());
                                }else{
                                    model.setReference1("");
                                }
                                if(gv.getReference02()!=null){
                                    model.setReference2(gv.getReference02());
                                }else{
                                    model.setReference2("");
                                }
                                if(gv.getReference03()!=null){
                                    model.setReference3(gv.getReference03());
                                }else{
                                    model.setReference3("");
                                }
                                if(gv.getReference04()!=null){
                                    model.setReference4(gv.getReference04());
                                }else{
                                    model.setReference4("");
                                }
                                if(gv.getReference05()!=null){
                                    model.setReference5(gv.getReference05());
                                }else{
                                    model.setReference5("");
                                }

                                rp3.data.models.GeneralValue.insert(db, model);
                            }
                        }

                    }
                }else{
                    return  SyncAdapter.SYNC_EVENT_AUTH_ERROR;
                }
            }catch (Exception e){
                e.printStackTrace();
                return SyncAdapter.SYNC_EVENT_ERROR;
            }

        }finally{
            webService.close();
        }

        return SyncAdapter.SYNC_EVENT_SUCCESS;
    }
}
