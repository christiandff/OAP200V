package com.group11.classicmodels;
/**
 * @author Christian Douglas Farnes Fancy
 * @author Even Hjerpseth Unneberg
 */
public class EmployeeData {
    private String employeeNumber;
    private String firstName;
    private String lastName;
    private String extension;
    private String email;
    private String officeCode;
    private String reportsTo;
    private String jobTitle;

    // Define a class representing customer data with various attributes
    public EmployeeData(String employeeNumber, String firstName, String lastName,
                        String extension, String email, String officeCode,
                        String reportsTo, String jobTitle) {
        // Initialize the object with provided customer data
        this.employeeNumber = employeeNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.extension = extension;
        this.email = email;
        this.officeCode = officeCode;
        this.reportsTo = reportsTo;
        this.jobTitle = jobTitle;
    }

    // Add getters and setters as needed for retrieving the customer's data.

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getExtension() {
        return extension;
    }

    public String getEmail() {
        return email;
    }

    public String getOfficeCode() {
        return officeCode;
    }

    public String getReportsTo() {
        return reportsTo;
    }

    public String getJobTitle() {
        return jobTitle;
    }
}