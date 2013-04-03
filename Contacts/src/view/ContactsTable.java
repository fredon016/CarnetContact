package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import metier.*;

public class ContactsTable extends JFrame implements KeyListener, ListSelectionListener
{
	private JTable contactsTable;
	private CarnetContacts carnetContacts;
	private JTextField nouveauNom, nouveauMail;
	private JButton ajouterBouton = new JButton("Ajouter");
	private JButton supprimerBouton = new JButton("Supprimer");
	
	public ContactsTable(CarnetContacts carnetContacts)
	{
		this.carnetContacts = carnetContacts;
		contactsTable = new JTable(getTableModel());
		getContentPane().setLayout(new BorderLayout());
		JPanel tablePanel = new JPanel();
		getContentPane().add(tablePanel, BorderLayout.NORTH);
		tablePanel.add(new JScrollPane(contactsTable));
		contactsTable.getSelectionModel().addListSelectionListener(this);
		JPanel controlPanel = new JPanel();
		getContentPane().add(controlPanel, BorderLayout.SOUTH);
		controlPanel.setLayout(new FlowLayout());
		ajouterBouton.setEnabled(false);
		supprimerBouton.setEnabled(false);
		ajouterBouton.addActionListener(getAjouter());
		supprimerBouton.addActionListener(getSupprimer());
		nouveauNom = new JTextField();
		nouveauMail = new JTextField();
		nouveauNom.setColumns(20);
		nouveauMail.setColumns(20);
		nouveauNom.setToolTipText("Saisissez le nom du nouveau contact");
		nouveauMail.setToolTipText("Saisissez le mail du nouveau contact");
		nouveauNom.addKeyListener(this);
		nouveauMail.addKeyListener(this);
		controlPanel.add(nouveauNom);
		controlPanel.add(nouveauMail);
		controlPanel.add(ajouterBouton);
		controlPanel.add(supprimerBouton);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setVisible(true);

	}

	private ActionListener getAjouter()
	{
		return new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				Contact c = new Contact(nouveauNom.getText(), nouveauMail.getText());
				try
				{
					carnetContacts.add(c);
					((AbstractTableModel) contactsTable.getModel()).
					fireTableStructureChanged();
				}
				catch (SQLException e)
				{
					System.out.println("Impossible d'ajouter le contact");;
				}
			}
		};
	}

	private ActionListener getSupprimer()
	{
		return new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				int[] selectedRows = contactsTable.getSelectedRows();
				TableModel tableModel = contactsTable.getModel();
				for (int i = 0 ; i < selectedRows.length ; i++)
					{
						Contact toDelete = (Contact)tableModel.getValueAt(selectedRows[i], 2);
						try
						{
							carnetContacts.delete(toDelete);
						} 
						catch (SQLException e)
						{
							System.out.println("Impossible de supprimer le contact");
						}
					}
				((AbstractTableModel) contactsTable.getModel()).
				fireTableStructureChanged();
			}
		};
	}
	
	private TableModel getTableModel()
	{
		return new AbstractTableModel()
		{
			@Override
			public int getColumnCount()
			{
				return 2;
			}

			@Override
			public String getColumnName(int columnIndex)
			{
				switch (columnIndex)
				{
				case 0:
					return "nom";
				case 1:
					return "mail";
				}
				return null;
			}

			@Override
			public int getRowCount()
			{
				try
				{
					return carnetContacts.getNbContacts();
				} catch (SQLException e)
				{
					System.out.println("Impossible de déterminer le nombre de contacts");
				}
				return 0;
			}

			@Override
			public Object getValueAt(int rowIndex, int columnIndex)
			{
				try
				{
					Contact contact = carnetContacts
							.getContactByIndex(rowIndex);
					switch (columnIndex)
					{
					case 0:
						return contact.getNom();
					case 1:
						return contact.getMail();
					case 2:
						return contact;
					}
				} catch (SQLException e)
				{
					System.out.println("Impossible de charger l'objet numéro "
							+ rowIndex);
				}
				return null;
			}

			@Override
			public boolean isCellEditable(int rowIndex, int columnIndex)
			{
				return true;
			}

			@Override
			public void setValueAt(Object aValue, int rowIndex, int columnIndex)
			{
				Contact contact = (Contact)getValueAt(rowIndex, 2);
				switch (columnIndex)
				{
					case 0 : contact.setNom((String)aValue); break;
					case 1 : contact.setMail((String)aValue); break;
				}
				try
				{
					carnetContacts.update(contact);
					fireTableRowsUpdated(rowIndex, rowIndex);
				} 
				catch (SQLException e)
				{
					System.out.println("impossible de modifier le contact");
				}
			}
		};
	}

	@Override
	public void keyPressed(KeyEvent arg0)
	{
	}

	@Override
	public void keyReleased(KeyEvent arg0)
	{
		ajouterBouton.setEnabled(!nouveauMail.getText().equals("") && !nouveauNom.getText().equals("") );
	}

	@Override
	public void keyTyped(KeyEvent arg0)
	{
	}

	@Override
	public void valueChanged(ListSelectionEvent arg0)
	{
		supprimerBouton.setEnabled(contactsTable.getSelectedRowCount() != 0);	
	}
}
