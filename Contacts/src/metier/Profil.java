package metier;

import java.io.Serializable;

public class Profil implements Serializable{
	
	private String url = "";
	private String login = "";
	private String password = "";
	private boolean courant;
	
	public Profil(String url, String login, String password)
	{
		this.courant = false;
		this.url = url;
		this.login = login;
		this.password = password;
	}

	public String getUrl() 
	{
		return url;
	}

	public void setUrl(String url) 
	{
		this.url = url;
	}

	public String getLogin()
	{
		return login;
	}

	public void setLogin(String login) 
	{
		this.login = login;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password) 
	{
		this.password = password;
	}
	
	public boolean isCourant() 
	{
		return courant;
	}

	public void setCourant(boolean courant) 
	{
		this.courant = courant;
	}
}
