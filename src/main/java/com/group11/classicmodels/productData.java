package com.group11.classicmodels;
/**
 * @author Christian Douglas Farnes Fancy
 * @author Even Hjerpseth Unneberg
 */
public class productData {
    private String productCode;
    private String productName;
    private String productLine;
    private String productScale;
    private String productVendor;
    private String productDescription;
    private String quantityInStock;
    private String buyPrice;
    private String MSRP;

    // Define a class representing customer data with various attributes
    public productData(String productCode, String productName, String productLine,
                        String productScale, String productVendor, String productDescription,
                        String quantityInStock, String buyPrice, String MSRP) {
    	// Initialize the object with provided customer data
        this.productCode = productCode;
        this.productName = productName;
        this.productLine = productLine;
        this.productScale = productScale;
        this.productVendor = productVendor;
        this.productDescription = productDescription;
        this.quantityInStock = quantityInStock;
        this.buyPrice = buyPrice;
        this.MSRP = MSRP;
    }

    // Add getters and setters as needed

    public String getproductCode() {
        return productCode;
    }

    public String getproductName() {
        return productName;
    }

    public String getproductLine() {
        return productLine;
    }

    public String getproductScale() {
        return productScale;
    }

    public String getproductVendor() {
        return productVendor;
    }

    public String getproductDescription() {
        return productDescription;
    }

    public String getquantityInStock() {
        return quantityInStock;
    }

    public String getbuyPrice() {
        return buyPrice;
    }
    public String getMSRP() {
        return MSRP;
    }
}