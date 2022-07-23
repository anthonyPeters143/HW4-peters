import java.math.BigDecimal;

public class ProductSpecification {

    private String productCode, productName;
    private BigDecimal productPrice;

    private boolean productTaxable;


    ProductSpecification() {
        productCode = "";
        productName = "";
        productPrice = BigDecimal.valueOf(0.00);
        productTaxable = false;
    }

    ProductSpecification(String code, String name, BigDecimal price) {
        productCode = code;
        productName = name;
        productPrice = price;
        productTaxable = this.isTaxable(code);
    }

    public String getProductCode() {
        return productCode;
    }

    public String getProductName() {
        return productName;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public boolean getProductTaxable() {
        return productTaxable;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    /**
     * Returns item taxable boolean, based on if first char. A = taxable (true), B = nontaxable (false)
     *
     * @param importItemCode, String
     * @return boolean, itemTaxable
     */
    private boolean isTaxable(String importItemCode) {
        // returns taxable state
        return importItemCode.charAt(0) == 'A';
    }

}
