
import java.util.ArrayList;

public class Sale {

    ArrayList<SalesLineItem> salesLineItemArrayList;

    Sale() {
        // Initialize Sale item array list
        salesLineItemArrayList = new ArrayList<SalesLineItem>();

    }

    public void addSalesLineItem(ProductSpecification specification, int quantity){

        // loop array list to check if SalesLineItem is already created
        for (SalesLineItem SalesLineItemTracker : salesLineItemArrayList) {
            if (specification.getProductCode().equals(SalesLineItemTracker.getItemIDTrack())){
                // Item tracker already created
                // Update saleItemTracker quantity and price
                saleItemTracker.addItemQuantity(itemQuantity);
                saleItemTracker.addItemTotal(itemPrice);

                return;
            }
        }

        // Item not found, create new item tracker
        saleItemTrackerArrayList.add(new SaleItemTracker(saleItem,itemQuantity,itemPrice));


        // Check if created before, if so then add quantity to object
        if (salesLineItemArrayList.contains(specification)) {
            // Set quantity and price from SaleLineItemObject to new totals


        } else {
            // Create new SalesLineItem object
        }
        salesLineItemArrayList.add(
                new SalesLineItem()
        );
    }



}
