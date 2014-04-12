package rp3.configuration;

import java.util.ArrayList;
import java.util.List;

public class WebServiceConfiguration {
	
	private List<WebServiceData> webServiceDataCollection;		

	public synchronized void addWebServiceData(WebServiceData data) {
		getWebServiceDataCollection().add(data);
	}

	public WebServiceData get(String name) {
		List<WebServiceData> collection = getWebServiceDataCollection();
		for (WebServiceData data : collection) {
			if (data.getName().equalsIgnoreCase(name))
				return data;
		}
		return null;
	}

	public List<WebServiceData> getWebServiceDataCollection() {
		if (webServiceDataCollection == null)
			webServiceDataCollection = new ArrayList<WebServiceData>();
		return webServiceDataCollection;
	}

}
