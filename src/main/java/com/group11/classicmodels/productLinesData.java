package com.group11.classicmodels;
/**
 * @author Christian Douglas Farnes Fancy
 * @author Even Hjerpseth Unneberg
 */
public class productLinesData {
    private String productLine;
    private String textDescription;
    private String htmlDescription;
    private String image;

    // Define a class representing customer data with various attributes
    public productLinesData(String productLine, String textDescription, String htmlDescription,
                        String image) {
    	// Initialize the object with provided customer data
        this.productLine = productLine;
        this.textDescription = textDescription;
        this.htmlDescription = htmlDescription;
        this.image = image;

    }

    // Add getters and setters as needed

    public String getproductLine() {
        return productLine;
    }

    public String gettextDescription() {
        return textDescription;
    }

    public String gethtmlDescription() {
        return htmlDescription;
    }

    public String getimage() {
        return image;
    }
}