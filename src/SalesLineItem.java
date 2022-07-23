import java.math.BigDecimal;

public class SalesLineItem {

    private String productCode, productName;
    private BigDecimal productTotal;

    SalesLineItem() {
        // Create new sale line item
        productCode = "";
        productName = "";
        productTotal = BigDecimal.valueOf(0.00);
    }

    SalesLineItem(ProductSpecification specification) {
        productCode = specification.getProductCode();
        productName = specification.getProductName();
        productTotal = specification.getProductPrice();
    }


}
