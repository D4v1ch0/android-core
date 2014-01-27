package rp3.configuration;

public class DataBaseConfiguration {

	private String name;
	private int version;
	
	public static final String KEY_NAME = "name";
	public static final String KEY_VERSION = "version";

	public DataBaseConfiguration() {			
	}

	public String getName() {
		return name;
	}

	void setName(String dbName) {
		name = dbName;
	}

	public int getVersion() {
		return version;
	}

	void setVersion(int version) {
		this.version = version;
	}

}
