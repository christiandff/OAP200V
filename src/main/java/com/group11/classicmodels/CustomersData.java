package com.group11.classicmodels;
/**
 * @author Christian Douglas Farnes Fancy
 * @author Even Hjerpseth Unneberg
 */
public class CustomersData {
    private String customerNumber;
    private String customerName;
    private String contactLastName;
    private String contactFirstName;
    private String phone;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private String salesRepEmployeeNumber;
    private String creditLimit;

 // Define a class representing customer data with various attributes
    public CustomersData(String customerNumber, String customerName, String contactLastName,
                        String contactFirstName, String phone, String addressLine1,
                        String addressLine2, String city, String state, String postalCode, 
                        String country, String salesRepEmployeeNumber, String creditLimit) {
        // Initialize the object with provided customer data
        this.customerNumber = customerNumber;
        this.customerName = customerName;
        this.contactLastName = contactLastName;
        this.contactFirstName = contactFirstName;
        this.phone = phone;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.country = country;
        this.salesRepEmployeeNumber = salesRepEmployeeNumber;
        this.creditLimit = creditLimit;
    }

    // Add getters and setters as needed for retrieving the customer's data.
    public String getcustomerNumber() {
        return customerNumber;
    }

    
    public String getcustomerName() {
        return customerName;
    }

    
    public String getcontactLastName() {
        return contactLastName;
    }

    
    public String getcontactFirstName() {
        return contactFirstName;
    }

    
    public String getphone() {
        return phone;
    }

    
    public String getaddressLine1() {
        return addressLine1;
    }

   
    public String getaddressLine2() {
        return addressLine2;
    }

    
    public String getcity() {
        return city;
    }

    
    public String getstate() {
        return state;
    }

    
    public String getpostalCode() {
        return postalCode;
    }

    
    public String getcountry() {
        return country;
    }

    
    public String getsalesRepEmployeeNumber() {
        return salesRepEmployeeNumber;
    }

    
    public String getcreditLimit() {
        return creditLimit;
    }
}