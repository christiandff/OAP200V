package com.group11.classicmodels;

/**
 * Creates main menu of the application
 * @author Boban Vesin
 * @author Christian Douglas Farnes Fancy
 */

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
/**
 * @author Boban Vesin
 * @author Christian Douglas Farnes Fancy
 * @author Even Hjerpseth Unneberg
 */


public class ApplicationMenu extends JMenuBar implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JMenu menu_file = null;
	private JMenuItem dBconnectionItem = null;
	private JMenuItem exitItem = null;
	private JMenu menu_help = null;
	private JMenuItem option_tip = null;

	private Font bigFont = new Font("Calibri", Font.PLAIN, 28);
	private Font smallFont = new Font("Calibri", Font.PLAIN, 24);

	/**
	 * Method constructor Creates the following form of the menu bar: Menu Test
	 * database connection Exit ` Help About the application
	 */

	protected ApplicationMenu() {

		UIManager.put("Menu.font", bigFont);
		UIManager.put("MenuItem.font", smallFont);

		menu_file = new JMenu("File");

		dBconnectionItem = new JMenuItem("Test database connection");
		dBconnectionItem.addActionListener(this);

		exitItem = new JMenuItem("Exit");
		exitItem.addActionListener(this);

		menu_file.add(dBconnectionItem);
		menu_file.add(exitItem);

		menu_help = new JMenu("Help");

		option_tip = new JMenuItem("About the application");
		option_tip.addActionListener(this);
		menu_help.add(option_tip);

		this.add(menu_file);
		this.add(menu_help);
	}

	/**
	 * Actions that are performed upon the clicks on the main menu by the user
	 */
	public void actionPerformed(ActionEvent event) {
	    String arg = event.getActionCommand();
	    if (arg.equals("Test database connection")) {
	        DatabaseConnection db = null;
	        try {
	            db = new DatabaseConnection();
	            // Use a simple query to test the connection
	            if (db.getConnection() != null && db.getConnection().createStatement().execute("SELECT 1")) {
	                displayMessage("Connection successfull!");
	            } else {
	                displayMessage("\"Error with the connection: please check that you have the right; url, user and password in the DatabaseConnection.java file");
	            }
	        } catch (Exception e) {
	            displayMessage("Error with the connection: please check that you have the right; url, user and password in the DatabaseConnection.java file " + e.getMessage());
	        } finally {
	            if (db != null) {
	                db.closeConnection();
	            }
	        }
	    } else if (arg.equals("Exit")) {
	        System.exit(0);
	    } else if (arg.equals("About the application")) {
	        JTextArea helptext = new JTextArea(
	                "This is the application for BRU AS\n\n- It provided the user with a overview of the classicmodels database\n-"
	                + " The user is able to perform CRUD operations on the database\n-"
	                + " The application is made for a school exam for the subject OAP200V\n-"
	                + " The applcation is made by team 11.");
	        helptext.setEditable(false);
	        helptext.setOpaque(false);
	        helptext.setFont(bigFont);
	        JOptionPane.showMessageDialog(this, helptext, "About the application", JOptionPane.INFORMATION_MESSAGE);
	    }
	}


	/**
	 * Method that displays option pane with the provided message it simplifies
	 * presentation of the messages to the user
	 * 
	 * @param message message that is presented to the user
	 */
	private void displayMessage(String message) {
		UIManager.put("OptionPane.messageFont", bigFont);
		UIManager.put("OptionPane.buttonFont", bigFont);
		JOptionPane.showMessageDialog(this, message);
	}

}
