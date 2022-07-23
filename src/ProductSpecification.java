import java.math.BigDecimal;

public class ProductSpecification {

    private String productCode, productName;
    private BigDecimal productPrice;


    ProductSpecification() {
        productCode = "";
        productName = "";
        productPrice = BigDecimal.valueOf(0.00);
    }

    ProductSpecification(String code, String name, BigDecimal price) {
        productCode = code;
        productName = name;
        productPrice = price;
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

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

}
