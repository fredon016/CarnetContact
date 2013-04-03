import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.JFrame;

import metier.CarnetContacts;
import view.ContactsTable;


public class Main {
	
	public static void main(String[] args) throws FileNotFoundException, IOException
	{
		try
		{
			try {
				JFrame jf = new ContactsTable(new CarnetContacts());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
		catch (ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
