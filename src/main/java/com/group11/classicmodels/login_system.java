package com.group11.classicmodels;

import java.awt.EventQueue;
import javax.swing.JPasswordField;

import javax.swing.JOptionPane;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;

/**
 * @author Christian Douglas Farnes Fancy
 */

public class login_system {

	
	// IS not very secure, but works as an ispiration for contuiation
	
	public JFrame frame; // Main frame for the login interface
	private JTextField txtusername; // Text field for username input
	private JPasswordField passwordField; // Password field for password input


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					login_system window = new login_system();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public login_system() {
		initialize();
        frame.setLocationRelativeTo(null);
	}

	/**
	 * Creates the frame and everything you see.
	 */
	private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 699, 489);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        
        JLabel lblNewLabel = new JLabel("Login");
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 21));
        lblNewLabel.setBounds(280, 45, 77, 24);
        frame.getContentPane().add(lblNewLabel);
        
        JLabel lblUsername = new JLabel("Username");
        lblUsername.setFont(new Font("Tahoma", Font.BOLD, 11));
        lblUsername.setBounds(150, 135, 64, 14);
        frame.getContentPane().add(lblUsername);
        
        JLabel lblPassword = new JLabel("Password");
        lblPassword.setFont(new Font("Tahoma", Font.BOLD, 11));
        lblPassword.setBounds(150, 202, 64, 14);
        frame.getContentPane().add(lblPassword);
        
     // Text field for entering the username
        txtusername = new JTextField();
        txtusername.setBounds(280, 132, 191, 20);
        frame.getContentPane().add(txtusername);
        txtusername.setColumns(10);
        
     // Password field for entering the password (hidden input)
        passwordField = new JPasswordField();
        passwordField.setBounds(280, 199, 191, 20);
        frame.getContentPane().add(passwordField);
        
        JButton btnlogin = new JButton("Login");
        btnlogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String password = new String(passwordField.getPassword());
                String username = txtusername.getText();
                
                // Check if username and password exactly match "student"
                if ("student".equals(username) && "student".equals(password)) {
                    passwordField.setText(null);
                    txtusername.setText(null);
                    com.group11.classicmodels.Overview.main(null);
                    frame.dispose();
                } else {
                    //If the input is invalid, show the message, "Invalid login details", "try again".
                    JOptionPane.showMessageDialog(null, "Invalid login details", "try again", JOptionPane.ERROR_MESSAGE);

                    passwordField.setText(null);
                    txtusername.setText(null);
                }
            }
        });
        btnlogin.setBounds(243, 293, 89, 23);
        frame.getContentPane().add(btnlogin);

        // Button for exiting the application
        JButton btnExit = new JButton("Exit");
        btnExit.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		 frame.dispose();
        	}
        });
        btnExit.setBounds(382, 293, 89, 23);
        frame.getContentPane().add(btnExit);
	}
}
