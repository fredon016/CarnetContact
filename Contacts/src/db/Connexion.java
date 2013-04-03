package db;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import metier.Profil;

public class Connexion implements Serializable//serialisation 
{
	
	private Profil profil;
	private static final boolean DEBUG_MODE = false;
	
	private transient Connection c;//transient veut dire que il n'est pas serialiaer
	
	 public Connexion(Profil profil) throws ClassNotFoundException, SQLException
	{
		profil = new Profil(profil.getUrl(), profil.getLogin(), profil.getPassword());
		profil.setCourant(true); 
		serialiser(profil);
		Class.forName("com.mysql.jdbc.Driver");
			this.c = DriverManager.getConnection(profil.getUrl(), profil.getLogin(), profil.getPassword());
	}
	 
	 public Connexion() throws ClassNotFoundException, SQLException, FileNotFoundException, IOException
		{
		 profil=deserialiser("fichier.ser");
		 Class.forName("com.mysql.jdbc.Driver");
			this.c = DriverManager.getConnection(profil.getUrl(), profil.getLogin(), profil.getPassword());
		}
	
	public void sqlBatch(String[] script) throws SQLException
	{
		Statement s = c.createStatement();
		for (String str : script)
		{
			if (DEBUG_MODE)
				System.out.println(str + ";");
			s.addBatch(str);
		}
		s.executeBatch();
	}
	
	public void sqlCreate(String req) throws SQLException
	{
		if (DEBUG_MODE)
			System.out.println(req);
		Statement s = c.createStatement();
		s.addBatch(req);
		s.executeBatch();
	}
	
	public void sqlUpdate(String req) throws SQLException
	{
		if (DEBUG_MODE)
			System.out.println(req);
		Statement s = c.createStatement();
		s.executeUpdate(req);
	}

	public int sqlInsert(String req) throws SQLException
	{
		if (DEBUG_MODE)
			System.out.println(req);
		Statement s = c.createStatement();
		s.executeUpdate(req, 1) ;
		ResultSet rs = s.getGeneratedKeys();
		if (rs.next())
			return rs.getInt(1);
		throw new SQLException("no value inserted");
	}
	
	public ResultSet select(String req) throws SQLException
	{
		if (DEBUG_MODE)
			System.out.println(req);
		Statement s = c.createStatement();
		return s.executeQuery(req);
	}
	
	private <T extends Serializable> void serialiser(T o)
	{
		try {
			FileOutputStream fichier = new FileOutputStream("fichier.ser");
			ObjectOutputStream oos;
			oos = new ObjectOutputStream(fichier);
			oos.writeObject(o);
			oos.flush();
			oos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
		private <T extends Serializable> T deserialiser(String fichier) throws FileNotFoundException, IOException, ClassNotFoundException
	{
		ObjectInputStream ois;
		ois = new ObjectInputStream(new FileInputStream(fichier));
		return  (T)ois.readObject();
	}
}
