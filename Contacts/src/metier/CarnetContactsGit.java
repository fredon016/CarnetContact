package metier;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import view.identification1erlancement;
import db.Connexion;

public class CarnetContacts implements Runnable
{
	private static final String[] SCRIPT = new String[] {
			"if exists contact", 
			"create table contact" +
			"(numContact integer primary key auto_increment,\n" +
			"nomContact varchar(64) not null,\n" +
			"mailContact varchar(64) not null)"};
			
	Connexion connexion;//va cherche le fichier de la connexion 
	private ArrayList<Contact> contactsList;
	
	private boolean contactsLoaded = false;	
	
	public CarnetContacts() throws ClassNotFoundException, SQLException, FileNotFoundException, IOException, InterruptedException 
	{
		File f = new File("fichier.ser");
		if(f.exists())	
		connexion = new Connexion();
		else
		{	
			Thread t = new Thread(new identification1erlancement());
			t.start();
			 wait();
			String mdp = d.getMotDePasse().getText();
			if(mdp==null)
				mdp="";
			connexion = new Connexion(new Profil(d.getAdresseBDD().getText(), d.getLogin().getText(), mdp));
		}
		/*if(connexion == null)
		{
			identification1erlancement d = new  identification1erlancement();
			//identification1erlancement d = new  identification1erlancement();
			Thread.sleep(10000);
			connexion = deserialiser("fichier.ser");
		}
		else connexion = deserialiser("fichier.ser");
		*/
		
		contactsList = new ArrayList<Contact> ();
	}
	
	public void add(Contact contact) throws SQLException
	{
		int id = connexion.sqlInsert("insert into contact(nomContact, mailContact) values ('" +
				contact.getNom() + "', '" + contact.getMail() + "')");
		contact.setNum(id);
		contactsList.add(contact);
	}
	
	public void update(Contact contact) throws SQLException
	{
		connexion.sqlUpdate("update contact set nomContact = '" + contact.getNom() + 
				"', mailContact  = '" + contact.getMail() + "' " +
				"where numContact = " + contact.getNum());
	}
	
	public void delete(Contact contact) throws SQLException
	{
		contactsList.remove(contact);
		connexion.sqlUpdate("delete from contact where numContact = " + contact.getNum());		
	}
	
	public void reset() throws SQLException
	{
		connexion.sqlBatch(SCRIPT);
	}
	
	private void loadAllContacts() throws SQLException
	{
		if (! contactsLoaded)
		{
			ResultSet rs = connexion.select("select * from contact order by numContact");
			while(rs.next())
			{
				Contact contact = new Contact(
						rs.getInt("numContact"), 
						rs.getString("nomContact"), 
						rs.getString("mailContact"));
				contactsList.add(contact);
			}
			contactsLoaded = true;
		}
	}
	
	public int getNbContacts() throws SQLException
	{
		loadAllContacts();
		return contactsList.size();		
	}
	
	public Contact getContactByIndex(int index) throws SQLException
	{
		loadAllContacts();
		return contactsList.get(index);
	}	
	
	private <T extends Serializable> T deserialiser(String fichier) throws FileNotFoundException, IOException, ClassNotFoundException
	{
		ObjectInputStream ois;
		ois = new ObjectInputStream(new FileInputStream(fichier));
		return  (T)ois.readObject();
	}

	@Override
	public void run() {
		try {
			CarnetContacts c = new CarnetContacts();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
