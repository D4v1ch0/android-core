package rp3.security;

public class User {
		
	private String user = "";
	@SuppressWarnings("unused")
	private String password = "";				
	
	public String getUser()
	{
		return this.user;
	}
	
	public void setUser(String user)
	{
		this.user = user;
	}
	
	public void setPassword(String password)
	{
		this.password = password;
	}
	
}
