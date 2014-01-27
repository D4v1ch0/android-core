package rp3.db;

public class Statement {
	
	public static final String TAG_Name = "statement";
	
	private String name;
	private String query;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
}
