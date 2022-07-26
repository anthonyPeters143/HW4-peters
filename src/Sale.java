
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.function.BiFunction;

public class Sale {

    /**
     * ArrayList of SalesLineItems
     */
    ArrayList<SalesLineItem> salesLineItemArrayList;

    BigDecimal eodTotal, subtotal, subtotalTax;

    Sale() {
        // Initialize Sale item array list
        salesLineItemArrayList = new ArrayList<SalesLineItem>();

        // Initialize end of day total
        eodTotal = BigDecimal.valueOf(0);
        subtotal = BigDecimal.valueOf(0);
        subtotalTax = BigDecimal.valueOf(0);
    }

    public void addSalesLineItem(ProductSpecification specification, int quantity, BigDecimal price){
        // Add total to end of day total
        eodTotal = eodTotal.add(price);

        // Loop array list to check if SalesLineItem is already created
        for (SalesLineItem SalesLineItemTracker : salesLineItemArrayList) {
            if (specification.getProductCode().equals(SalesLineItemTracker.getProductCode())){
                // SalesLineItem tracker already created
                // Set the salesLineItem's price to the new total plus the old total
                SalesLineItemTracker.setProductTotal((SalesLineItemTracker.getProductTotal()).add(price));

                // Set the salesLineItem's quantity to the new amount plus the old amount
                SalesLineItemTracker.setProductQuantity(quantity + SalesLineItemTracker.getProductQuantity());

                return;
            }
        }

        // Item not found, create new item tracker
        salesLineItemArrayList.add(new SalesLineItem(specification,quantity,price));
    }

    public String createReceipt(DecimalFormat currencyFormat) {
        BigDecimal taxableTotal = BigDecimal.valueOf(0), nontaxableTotal = BigDecimal.valueOf(0);
        String receiptString = "Items list:\n";
        String quantityNameTotalFormat = "%4s %-16s$%7s%n",
                    subtotalFormat = "%-21s$%7s%n%-21s$%7s";

        // Sort into alphabetical order by name
        Collections.sort(salesLineItemArrayList);

        // Loop through salesLineItemArrayList objects
        for (SalesLineItem salesLineItemTracker : salesLineItemArrayList) {
            // Check if item has quantity
            if (salesLineItemTracker.getProductQuantity() > 0) {
                // Check taxable flag
                if (salesLineItemTracker.isProductTaxable()) {
                    // Taxable, increment taxable total
                    taxableTotal = ((salesLineItemTracker.getProductTotal()).add(taxableTotal));

                } else {
                    // Nontaxable, increment non-taxable total
                    nontaxableTotal = ((salesLineItemTracker.getProductTotal()).add(nontaxableTotal));

                }

            }

            // Add product's name, quantity, total, and new line char to return string
            receiptString = receiptString.concat(
                    String.format(quantityNameTotalFormat,
                        salesLineItemTracker.getProductQuantity(),
                        salesLineItemTracker.getProductName(),
                        currencyFormat.format(salesLineItemTracker.getProductTotal())) );
        }

        subtotal = taxableTotal.add(nontaxableTotal);
        subtotalTax = (taxableTotal.multiply(BigDecimal.valueOf(.06))).add(taxableTotal.add(nontaxableTotal));

        // Compile subtotals then format strings and concat to receipt string
        receiptString = receiptString.concat(
                String.format(subtotalFormat,
                        "Subtotal",
                        currencyFormat.format(subtotal),
                        "Total with tax (6%)",
                        currencyFormat.format(subtotalTax)));


        return receiptString;
    }

    public void resetSale() {
        // Reset subtotal fields to 0
        subtotal = BigDecimal.valueOf(0);
        subtotalTax = BigDecimal.valueOf(0);

        // Reset product ArrayList
        salesLineItemArrayList = new ArrayList<SalesLineItem>();
    }

    public BigDecimal getEodTotal() {
        return eodTotal;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public BigDecimal getSubtotalTax() {
        return subtotalTax;
    }
}
