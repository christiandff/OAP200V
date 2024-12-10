package com.group11.classicmodels.app;

import javax.swing.SwingUtilities;
import com.group11.classicmodels.login_system;

/**
 * @author Christian Fancy
 */

public class App {

	public static void main(String[] args) {
	    SwingUtilities.invokeLater(new Runnable() {
	        public void run() {
	        	login_system window = new login_system(); //Opens the login system class
	            window.frame.setVisible(true);
	        }
	    });
	}


}