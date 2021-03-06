package rp3.connection;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.Document;
import org.w3c.dom.Entity;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import rp3.configuration.Configuration;
import rp3.configuration.WebServiceData;
import rp3.configuration.WebServiceDataMethod;
import rp3.data.Dictionary;
import rp3.data.DictionaryEntry;
import rp3.runtime.Session;
import rp3.util.DateTime;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;


public class WebService {
	private static final String TAG = WebService.class.getSimpleName();
	
	
	public static final String TYPE_SOAP = "SOAP";
	public static final String TYPE_REST = "REST";

	private String wsConfigurationName;
	private WebServiceData wsData;
	private WebServiceDataMethod wsMethod;
	private List<WebServiceParameter> parameters;
	private String respJSONString;
	private Object respSoap;
	private Integer timeOut;	
	private File responseFile;
	private boolean useFileResponse;	
	private Dictionary<String, String> headers;
	private boolean unAuthorized = false;  
	
	public WebService() {
		headers = new Dictionary<String, String>();
	}

	public WebService(String wsConfigurationName, String methodName) {
		headers = new Dictionary<String, String>();
		setConfigurationName(wsConfigurationName, methodName);
	}

	public void setConfigurationName(String wsConfigurationName,
			String methodName) {
		this.wsConfigurationName = wsConfigurationName;
		wsData = Configuration.getWebServiceConfiguration().get(
				this.wsConfigurationName);
		wsMethod = wsData.getMethod(methodName);
	}

	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}
	
	public void setHeader(String name, String value){
		headers.set(name, value);
	}
	
	public void addCurrentAuthToken(){
		setAuthToken(Session.getUser().getAuthToken());
		setAuthTokenType(Session.getUser().getAuthTokenType());
	}
	
	public void setAuthToken(String authToken){	
		setHeader("AuthToken", authToken);
	}
	
	public void setAuthTokenType(String authTokenType){		
		setHeader("AuthTypeToken", authTokenType);
	}	

	public List<WebServiceParameter> getParameters() {
		if (parameters == null)
			parameters = new ArrayList<WebServiceParameter>();
		return parameters;
	}

	public void addParameter(String name, Object value) {
		getParameters().add(
				new WebServiceParameter(name, value, value.getClass()));
	}

	public void addParameter(String name, Object value, Object valueType) {
		getParameters().add(
				new WebServiceParameter(name, value, valueType, true));
	}

	public void addStringParameter(String name, String value) {
		getParameters().add(new WebServiceParameter(name, value, String.class));
	}

	public void addBooleanParameter(String name, boolean value) {
		getParameters()
				.add(new WebServiceParameter(name, value, Boolean.class));
	}

	public void addIntParameter(String name, int value) {
		getParameters()
				.add(new WebServiceParameter(name, value, Integer.class));
	}

	public void addLongParameter(String name, long value) {
		getParameters().add(new WebServiceParameter(name, value, Long.class));
	}

	public void addDoubleParameter(String name, double value) {
		getParameters().add(new WebServiceParameter(name, value, Double.class));
	}

	public void addFloatParameter(String name, double value) {
		getParameters().add(new WebServiceParameter(name, value, Float.class));
	}

	public void addFloatParameter(String name, Object value, Object valueType) {
		getParameters().add(new WebServiceParameter(name, value, valueType));
	}
	
	public void setParameter(String name, Object value){
		boolean exists = false;
		for(WebServiceParameter p : getParameters()){
			if(p.getName().equals(name)){
				exists = true;
				p.setValue(value);
				break;
			}
		}
		if(!exists)
			addParameter(name, value);
	}

	public boolean useFileResponse() {
		return useFileResponse;
	}

	public void setUseFileResponse(boolean useFileResponse) {
		this.useFileResponse = useFileResponse;
	}
	
	public File getFileResponse() {
		return responseFile;
	}
	
	public boolean getBooleanResponse() {
		return Boolean.parseBoolean(getStringResponse());
	}

	public long getLongResponse() {
		return Long.parseLong(getStringResponse());
	}

	public double getDoubleResponse() {
		return Double.parseDouble(getStringResponse());
	}

	public double getFloatResponse() {
		return Float.parseFloat(getStringResponse());
	}

	public int getIntegerResponse() {
		return Integer.parseInt(getStringResponse());
	}

	public String getStringResponse() {
		if (wsData.getType().equalsIgnoreCase(TYPE_SOAP)) {
			return getSoapPrimitiveResponse().getValue().toString();
		} else if (wsData.getType().equalsIgnoreCase(TYPE_REST)) {
			return respJSONString;
		}
		return null;
	}

	public JSONArray getJSONArrayResponse() {
		try {
			return new JSONArray(respJSONString);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public JSONObject getJSONObjectResponse() {
		try {
			return new JSONObject(respJSONString);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public SoapPrimitive getSoapPrimitiveResponse() {
		return ((SoapPrimitive) respSoap);
	}

	public SoapObject getSoapObjectResponse() {
		return ((SoapObject) respSoap);
	}

	public XmlPullParser getXmlPullParserResponse() {
		XmlPullParser parser = Xml.newPullParser();
		try {
			parser.setInput(new StringReader(getStringResponse()));
			return parser;
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public Document getXmlDocument() {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			Document d1 = builder.parse(new InputSource(new StringReader(
					getStringResponse())));
			return d1;
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean isUnAuthorized(){
		return unAuthorized;
	}

	private void executeSoap() throws HttpResponseException, IOException,
			XmlPullParserException {
		SoapObject request = new SoapObject(wsData.getNamespace(),
				wsMethod.getMethodId());

		// Create envelope
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);

		for (WebServiceParameter p : this.getParameters()) {
			PropertyInfo paramPI = new PropertyInfo();
			paramPI.setName(p.getName());
			paramPI.setValue(p.getValue());
			paramPI.setType(p.getValueType());

			request.addProperty(paramPI);

			// envelope.encodingStyle = SoapSerializationEnvelope.XSD;

			if (p.isComplexType() && !(p.getValueType() instanceof SoapObject)) {
				envelope.addMapping(wsData.getNamespace(), p.getValue()
						.getClass().getSimpleName(),
						(Class<?>) p.getValueType());
			}
		}

		envelope.setAddAdornments(false);
		envelope.implicitTypes = true;
		if (wsData.isDotNet())
			envelope.dotNet = true;

		// Set output SOAP object
		envelope.setOutputSoapObject(request);
		// Create HTTP call object
		if (timeOut == null)
			timeOut = HttpConnection.NET_CONNECT_TIMEOUT_MILLIS;
								

		try {
			if(useFileResponse){
				String fileName = wsMethod.getName() + String.valueOf(DateTime.getCurrentDateTime().getTime());			
				this.responseFile = new File(Session.getContext().getFilesDir(),fileName);	
				
				HttpTransportFileOutStreamSE androidHttpTransport = new HttpTransportFileOutStreamSE(
						wsData.getUrl(), this.timeOut);
								
				// Invole web service			
				androidHttpTransport.call(wsMethod.getAction(), envelope, null, responseFile);
				
			}else{
				HttpTransportSE androidHttpTransport = new HttpTransportSE(
						wsData.getUrl(), this.timeOut);
				
				// Invole web service			
				androidHttpTransport.call(wsMethod.getAction(), envelope);
				
				// Get the response
				Object resultObject = envelope.getResponse();

				respSoap = resultObject;
			}			
						
		}finally {
			Log.d(TAG,"respSoap:"+respSoap.toString());
		}
	}
		

	
	private void executeRest() throws JSONException, ClientProtocolException, IOException {
        String urlString = wsData.getUrl() + "/" + wsMethod.getAction();
        boolean oAuthEnabled = true;

        if (timeOut == null)
            timeOut = HttpConnection.NET_CONNECT_TIMEOUT_MILLIS;

        HttpParams httpParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeOut);
        HttpConnectionParams.setSoTimeout(httpParameters, timeOut);

        DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);

        httpClient.addRequestInterceptor(new HttpRequestInterceptor() {

            public void process(
                    final HttpRequest request,
                    final HttpContext context) throws HttpException, IOException {
                if (!request.containsHeader("Accept-Encoding")) {
                    request.addHeader("Accept-Encoding", "gzip");
                }
            }
        });

        httpClient.addResponseInterceptor(new HttpResponseInterceptor() {

            public void process(
                    final HttpResponse response,
                    final HttpContext context) throws HttpException, IOException {
                HttpEntity entity = response.getEntity();
                Header ceheader = entity.getContentEncoding();
                if (ceheader != null) {
                    HeaderElement[] codecs = ceheader.getElements();
                    for (int i = 0; i < codecs.length; i++) {
                        if (codecs[i].getName().equalsIgnoreCase("gzip")) {
                            response.setEntity(
                                    new ClientGZipContentCompression.GzipDecompressingEntity(response.getEntity()));
                            return;
                        }
                    }
                }
            }

        });

			HttpResponse resp = null;

			if (wsMethod.getWebMethod().equalsIgnoreCase("POST")) {
				
				JSONObject dato = new JSONObject();
				JSONArray jArray = null;
				
				for (WebServiceParameter p : this.getParameters()) {
					if(p.getName().startsWith("@")){
						urlString = urlString.replace(p.getName(), p.getValue()
							.toString());
					}else{																
						if (p.getValue() instanceof JSONObject) {
							dato = (JSONObject) p.getValue();
							break;
						}else if(p.getValue() instanceof JSONArray){
							jArray = (JSONArray) p.getValue();
							break;
						}
						
						dato.put(p.getName(), p.getValue());
					}											
				}
				
				HttpPost post = new HttpPost(urlString);


                post.addHeader("Accept-Encoding", "gzip, deflate");
				post.addHeader("X-Requested-With", "XMLHttpRequest");
				post.addHeader("Accept-Language", "es-419,es;q=0.8");
				post.addHeader("Accept", "*/*");
				post.setHeader("content-type", "application/json");
				
				if(!TextUtils.isEmpty(wsData.getOAuthClientId()) && !TextUtils.isEmpty(wsData.getOAuthClientSecret())){
					post.setHeader("ClientId", wsData.getOAuthClientId());
					post.setHeader("ClientSecret", wsData.getOAuthClientSecret());
					
					oAuthEnabled = true;
				}
								
				for(DictionaryEntry<String, String> header: headers.getEntries()){
					post.setHeader(header.getKey(), header.getValue());
				}
				
				
												
//				if(array!=null){
//					List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
//					postParameters.add(new BasicNameValuePair("collection", array.toString()));
//					UrlEncodedFormEntity entityA = new UrlEncodedFormEntity(postParameters);					
//					post.setEntity(entityA);
//				}
//				else{

                AbstractHttpEntity entity = null;
                /*if(wsMethod.getContentEncoding().equalsIgnoreCase("gzip")) {
                    if (!post.containsHeader("Content-Encoding")) {
                        post.addHeader("Content-Encoding", "gzip");
                    }
                    if (jArray != null) {
                        entity = new ByteArrayEntity(ClientGZipContentCompression.CompressString(jArray.toString()));
                    } else {
                        entity = new ByteArrayEntity(ClientGZipContentCompression.CompressString(dato.toString()));
                    }
                }
                else
                {*/
                    if(jArray!=null){
                        entity = new StringEntity(jArray.toString(), HTTP.UTF_8);
                    }else{
                        entity = new StringEntity(dato.toString(), HTTP.UTF_8);
                    }
                //}
					
			    post.setEntity(entity);
//				}

				resp = httpClient.execute(post);
				
				evaluateStatusResponse(resp, httpClient, post, oAuthEnabled);
				
			} else {
				for (WebServiceParameter p : this.getParameters()) {
					urlString = urlString.replace(p.getName(), p.getValue()
							.toString());
				}

				HttpGet get = new HttpGet(urlString);
				get.setHeader("content-type", "application/json");

				if(!TextUtils.isEmpty(wsData.getOAuthClientId()) && !TextUtils.isEmpty(wsData.getOAuthClientSecret())){
					get.setHeader("ClientId", wsData.getOAuthClientId());
					get.setHeader("ClientSecret", wsData.getOAuthClientSecret());															
				}
				
				for(DictionaryEntry<String, String> header: headers.getEntries()){
					get.setHeader(header.getKey(), header.getValue());
				}
				
				resp = httpClient.execute(get);
				evaluateStatusResponse(resp, httpClient, get, oAuthEnabled);

			}

			respJSONString = EntityUtils.toString(resp.getEntity());
		Log.d(TAG,"respJSONString:"+respJSONString);
	}	
	
	private void evaluateStatusResponse(HttpResponse response,HttpClient httpClient, HttpUriRequest uri, boolean oAuthEnabled) throws ClientProtocolException, IOException {
		int status = response.getStatusLine().getStatusCode();
        if(status != 200)
            respJSONString = EntityUtils.toString(response.getEntity());
		switch (status) {
		case HttpConnection.HTTP_STATUS_BAD_REQUEST:
			throw new HttpResponseException(status);
		case HttpConnection.HTTP_STATUS_ERROR:
			throw new HttpResponseException(status);
		case HttpConnection.HTTP_STATUS_FORBIDDEN:
			throw new HttpResponseException(status);
		case HttpConnection.HTTP_STATUS_NOT_FOUND:
			throw new HttpResponseException(status);
		case HttpConnection.HTTP_STATUS_UNAUTHORIZED:
			if(oAuthEnabled){
                rp3.accounts.Authenticator.getServerAuthenticate().requestSignIn();
				String token = Session.getUser().getAuthToken();

				if(!TextUtils.isEmpty(token)){
					uri.setHeader("AuthToken",  token);
					response = httpClient.execute(uri);
				}else
					throw new HttpResponseException(status);
			}
			else			
				throw new HttpResponseException(status);
		default:
			break;
		}
	}
	
	public void invokeWebService() throws HttpResponseException, IOException,
			XmlPullParserException, JSONException {		
		if (wsData.getType().equalsIgnoreCase(TYPE_SOAP)) {
			Log.d(TAG,"TYPE_SOAP...");
			executeSoap();
		} else if (wsData.getType().equalsIgnoreCase(TYPE_REST)) {
			Log.d(TAG,"TYPE_REST...");
			executeRest();
		}
	}
	
	public void close(){
		if(responseFile!=null)
			responseFile.delete();
	}
}
