package rp3.configuration;

public class WebServiceDataMethod {
	
	private String name;
	
	private String action;
	
	private String methodId;
	
	private String webMethod;

    private String contentEncoding;

    public String getContentEncoding() {
        return contentEncoding;
    }

    public void setContentEncoding(String contentEncoding) {
        this.contentEncoding = contentEncoding;
    }
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getMethodId() {
		return methodId;
	}
	public void setMethodId(String methodId) {
		this.methodId = methodId;
	}
	public String getWebMethod() {
		return webMethod;
	}
	public void setWebMethod(String webMethod) {
		this.webMethod = webMethod;
	}
}
