import java.math.BigDecimal;

public class SalesLineItem implements Comparable<SalesLineItem>{

    private BigDecimal productTotal;
    private String productCode, productName;
    private int productQuantity;
    private boolean productTaxable;

    SalesLineItem() {
        // Create new sale line item
        productCode = "";
        productName = "";
        productTotal = BigDecimal.valueOf(0.00);
        productQuantity = 0;
        productTaxable = false;
    }

    SalesLineItem(ProductSpecification specification, int quantity, BigDecimal price) {
        productCode = specification.getProductCode();
        productName = specification.getProductName();
        productTotal = price;
        productQuantity = quantity;
        productTaxable = specification.getProductTaxable();
    }

    public String getProductCode() {
        return productCode;
    }

    public String getProductName() {
        return productName;
    }

    public BigDecimal getProductTotal() {
        return productTotal;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public boolean isProductTaxable() {
        return productTaxable;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductTotal(BigDecimal productTotal) {
        this.productTotal = productTotal;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    @Override
    public int compareTo(SalesLineItem o) {
        return (this.getProductName().compareTo(o.getProductName()));
    }
}