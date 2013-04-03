package metier;

public class Contact 
{
	private int num;
	private String nom;
	private String mail;
	
	public String getNom() 
	{
		return nom;
	}
	
	public void setNom(String nom) 
	{
		this.nom = nom;
	}
	
	public String getMail() 
	{
		return mail;
	}
	
	public void setMail(String mail) 
	{
		this.mail = mail;
	}
	
	public void setNum(int num) 
	{
		if (this.num != -1)
			throw new RuntimeException("Cannot change DB ID");
		this.num = num;
	}
	
	public int getNum() 
	{
		return num;
	}

	public Contact(int num, String nom, String mail) 
	{
		this.num = num;
		this.nom = nom;
		this.mail = mail;
	}
	
	public Contact(String nom, String mail) 
	{
		this(-1, nom, mail);
	}
}
