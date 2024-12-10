package com.group11.classicmodels;
/**
 * @author Christian Douglas Farnes Fancy
 * @author Even Hjerpseth Unneberg
 */
public class officeData {
    private String officeCode;
    private String city;
    private String phone;
    private String addressLine1;
    private String addressLine2;
    private String state;
    private String country;
    private String postalCode;
    private String territory;

    // Define a class representing customer data with various attributes
    public officeData(String officeCode, String city, String phone,
                        String addressLine1, String addressLine2, String state,
                        String country, String postalCode, String territory) {
        // Initialize the object with provided customer data
        this.officeCode = officeCode;
        this.city = city;
        this.phone = phone;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.state = state;
        this.country = country;
        this.postalCode = postalCode;
        this.territory = territory;
    }

    // Add getters and setters as needed for retrieving the customer's data.

    public String getofficeCode() {
        return officeCode;
    }

    public String getcity() {
        return city;
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

    public String getstate() {
        return state;
    }

    public String getcountry() {
        return country;
    }

    public String getpostalCode() {
        return postalCode;
    }
    public String getterritory() {
        return territory;
    }
}
