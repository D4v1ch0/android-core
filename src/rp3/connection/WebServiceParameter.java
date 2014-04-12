package rp3.connection;

public class WebServiceParameter {

	private String name;
	private Object value;
	private Object valueType;
	private boolean isComplexType;
	
	public WebServiceParameter(String name, Object value, Object valueType){		
		this.name = name;
		this.value = value;
		this.valueType = valueType;
	}
	
	public WebServiceParameter(String name, Object value, Object valueType, boolean isComplexType){		
		this.name = name;
		this.value = value;
		this.valueType = valueType;
		this.isComplexType = isComplexType;
	}
	
	public boolean isComplexType(){
		return isComplexType;
	}
	
	public void setComplexType(boolean isComplexType){
		this.isComplexType = isComplexType;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public Object getValueType() {
		return valueType;
	}
	public void setValueType(Object valueType) {
		this.valueType = valueType;
	}
		
}
