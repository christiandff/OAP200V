package com.group11.classicmodels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Christian Douglas Fanres Fancy
 */
public class Overview extends ApplicationMenu {
	private static final long serialVersionUID = 1L;
    private JFrame frame;
    private DatabaseConnection dbConnection;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Overview window = new Overview();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Overview() {
        super(); // Invoke the constructor of the superclass (ApplicationMenu)
        initialize();
        dbConnection = new DatabaseConnection();
        frame.setLocationRelativeTo(null);
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 1000, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        // Set this menu to your JFrame
        frame.setJMenuBar(this);

        JLabel lblNewLabel = new JLabel("BRU AS - Menu");
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 21));
        lblNewLabel.setBounds(436, 14, 192, 33);
        frame.getContentPane().add(lblNewLabel);

        JButton btnOrders = new JButton("Orders");
        btnOrders.setFont(new Font("Tahoma", Font.BOLD, 15));
        btnOrders.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Orders();
                com.group11.classicmodels.Orders.main(null);// Load data into the table when Orders2 window is shown
                frame.dispose();
            }
        });

        btnOrders.setBounds(280, 83, 147, 42);
        frame.getContentPane().add(btnOrders);
        
        JButton btncustomer = new JButton("Customers");
        btncustomer.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		new Customers();
        		com.group11.classicmodels.Customers.main(null);// Load data into the table when Orders2 window is shown
                frame.dispose();
        	}
        });
        btncustomer.setFont(new Font("Tahoma", Font.BOLD, 15));
        btncustomer.setBounds(280, 164, 147, 42);
        frame.getContentPane().add(btncustomer);
        
        JButton btnEmployees = new JButton("Employees");
        btnEmployees.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		new Employee();
        		com.group11.classicmodels.Employee.main(null);// Load data into the table when Orders2 window is shown
                frame.dispose();
        	}
        });
        btnEmployees.setFont(new Font("Tahoma", Font.BOLD, 15));
        btnEmployees.setBounds(280, 240, 147, 42);
        frame.getContentPane().add(btnEmployees);
        
        JButton btnpayments = new JButton("Payments");
        btnpayments.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		new payments();
        		com.group11.classicmodels.payments.main(null);// Load data into the table when Orders2 window is shown
                frame.dispose();
        	}
        });
        btnpayments.setFont(new Font("Tahoma", Font.BOLD, 15));
        btnpayments.setBounds(280, 323, 147, 42);
        frame.getContentPane().add(btnpayments);
        
        JButton btnoffices = new JButton("Offices");
        btnoffices.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		new offices();
        		com.group11.classicmodels.offices.main(null);// Load data into the table when Orders2 window is shown
                frame.dispose();
        	}
        });
        btnoffices.setFont(new Font("Tahoma", Font.BOLD, 15));
        btnoffices.setBounds(608, 323, 147, 42);
        frame.getContentPane().add(btnoffices);
        
        JButton btnproductline = new JButton("Productline");
        btnproductline.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		new productLines();
        		com.group11.classicmodels.productLines.main(null);// Load data into the table when Orders2 window is shown
                frame.dispose();
        	}
        });
        btnproductline.setFont(new Font("Tahoma", Font.BOLD, 15));
        btnproductline.setBounds(608, 240, 147, 42);
        frame.getContentPane().add(btnproductline);
        
        JButton btnOrders_1_1 = new JButton("Products");
        btnOrders_1_1.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		new products();
        		products.main(null);// Load data into the table when Orders2 window is shown
                frame.dispose();
        	}
        });
        btnOrders_1_1.setFont(new Font("Tahoma", Font.BOLD, 15));
        btnOrders_1_1.setBounds(608, 164, 147, 42);
        frame.getContentPane().add(btnOrders_1_1);
        
        JButton btnOrderDetails = new JButton("Order Details");
        btnOrderDetails.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		new OrderDetails();
        		OrderDetails.main(null);// Load data into the table when Orders2 window is shown
                frame.dispose();
        	}
        });
        btnOrderDetails.setFont(new Font("Tahoma", Font.BOLD, 15));
        btnOrderDetails.setBounds(608, 83, 147, 42);
        frame.getContentPane().add(btnOrderDetails);
        
        JButton btnExitApplication = new JButton("Exit Application");
        btnExitApplication.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		frame.dispose();
        	}
        });
        btnExitApplication.setFont(new Font("Tahoma", Font.BOLD, 15));
        btnExitApplication.setBounds(110, 11, 155, 42);
        frame.getContentPane().add(btnExitApplication);
        
        JButton btnExport = new JButton("Export");
        btnExport.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		new Export();
        		com.group11.classicmodels.Export.main(null);
                frame.dispose();
        	}
        });
        btnExport.setFont(new Font("Tahoma", Font.BOLD, 15));
        btnExport.setBounds(352, 403, 147, 42);
        frame.getContentPane().add(btnExport);
        
        JButton btnSqlExecution = new JButton("SQL execution");
        btnSqlExecution.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		SqlExecutionWindow sqlWindow = new SqlExecutionWindow(dbConnection);
                sqlWindow.setVisible(true);
        	}
        });
        btnSqlExecution.setFont(new Font("Tahoma", Font.BOLD, 15));
        btnSqlExecution.setBounds(523, 403, 147, 42);
        frame.getContentPane().add(btnSqlExecution);
        
        

    }
}
