package view;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

import db.Connexion;

public class identification1erlancement extends JFrame implements Runnable {
	
	private JTextField adresseBDD;
	private JTextField login;
	private JTextField motDePasse;
	private Connexion connexion;
	
	public identification1erlancement() throws HeadlessException
	{
		setTitle("Identification login et mot de passe base de donnee");
		setSize(320,320);
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//mettre par defaut un DISPOSE_ON_CLOSE(et utiliser la methode dispose() pour fermer la JFrame en cour) parce qu'avec un EXIT_ON_CLOSE ca ferme tout les JFrame lancé
		setLocationRelativeTo(null);		
		getContentPane().setLayout(null);
		
		login = new JTextField();
		login.setBounds(113, 83, 65, 27);
		getContentPane().add(login);
		
		JTextField motDePasse = new JTextField();
		motDePasse.setBounds(113, 141, 67, 27);
		getContentPane().add(motDePasse);
		
		JButton ok = new JButton("OK");
		ok.setBounds(113, 199, 80, 27);
		getContentPane().add(ok);
		
		adresseBDD = new JTextField();
		adresseBDD.setBounds(65, 30, 161, 27);
		getContentPane().add(adresseBDD);
		ok.addActionListener(new ok());
		repaint();
		validate();
	}
	
	public JTextField getAdresseBDD() 
	{
		return adresseBDD;
	}

	public JTextField getLogin() 
	{
		return login;
	}

	public JTextField getMotDePasse() 
	{
		return motDePasse;
	}
	
	class ok implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
				notify();
		}
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

	@Override
	public void run()
	{
		identification1erlancement d = new identification1erlancement();
	}
}