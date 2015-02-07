package rp3.configuration;

import java.util.ArrayList;
import java.util.List;

public class WebServiceData {
	
	public static final String WEBSERVICE_TYPE_NET = "NET";
	
	public static final String KEY_NAME = "name";
	public static final String KEY_TYPE = "type";
	public static final String KEY_URL = "url";	
	public static final String KEY_NAMESPACE = "namespace";
	public static final String KEY_SOAPACTION = "soapAction";	
	public static final String KEY_DOTNET = "dotNet";
	public static final String KEY_SOAPVERSION = "soapVersion";
	public static final String KEY_OAUTH_CLIENT_SECRET = "oAuthClientSecret";
	public static final String KEY_OAUTH_CLIENT_ID = "oAuthClientId";
		
	private String name;
	private String namespace;
	private String url;	
	private String type;
	private String soapVersion;
	private boolean dotNet;
	private List<WebServiceDataMethod> methods; 
	private String oAuthClientId;
	private String oAuthClientSecret;	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNamespace() {
		return namespace;
	}
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSoapVersion() {
		return soapVersion;
	}
	public void setSoapVersion(String soapVersion) {
		this.soapVersion = soapVersion;
	}		
	public boolean isDotNet() {
		return dotNet;
	}
	public void setDotNet(boolean dotNet) {
		this.dotNet = dotNet;
	}
	
	public String getOAuthClientId() {
		return oAuthClientId;
	}
	
	public void setOAuthClientId(String oAuthClientId) {
		this.oAuthClientId = oAuthClientId;
	}
	public String getOAuthClientSecret() {
		return oAuthClientSecret;
	}
	public void setOAuthClientSecret(String oAuthClientSecret) {
		this.oAuthClientSecret = oAuthClientSecret;
	}
	
	public WebServiceDataMethod getMethod(String name){
		for(WebServiceDataMethod m : this.getMethods()){			
			if(m.getName().equalsIgnoreCase(name))
				return m;
		}
		return null;
	}
	
	public List<WebServiceDataMethod> getMethods() {
		if(methods==null) methods = new ArrayList<WebServiceDataMethod>();
		return methods;
	}
}