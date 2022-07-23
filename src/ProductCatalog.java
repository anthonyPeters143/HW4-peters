import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

public class ProductCatalog {

    private HashMap<String, ProductSpecification> productCatalogHashMap;

    ProductCatalog() {
        // Initialize file names
        String fileName = "item.txt";

        // Initialize Product HashMap
        productCatalogHashMap = new HashMap<>();

        // Input data from file
        try {
            // Create file
            File catalogFile = new File(fileName);

            // Set up scanner
            Scanner productCatalogScanner = new Scanner(catalogFile);

            // Scan in input and split by new line commas
            String productCatalogLine = productCatalogScanner.nextLine();
            String[] productCatalogSplitImportArray = productCatalogLine.split(Pattern.quote(","));

            // For loop to index and input file data into itemArray
            // Input code, name, price into Items objects in the array, advance index by 1, split index by 4
            for (int index = 0, splitIndex = 0; index < 10; index+=1, splitIndex+=3) {
                // Create ProductSpecification object and place it in productCatalogHashMap
                productCatalogHashMap.put(productCatalogSplitImportArray[splitIndex],
                        new ProductSpecification(productCatalogSplitImportArray[splitIndex],
                                String.valueOf(productCatalogSplitImportArray[splitIndex+1]),
                                BigDecimal.valueOf(Double.parseDouble(productCatalogSplitImportArray[splitIndex+2]))));
            }
            // Close scanner
            productCatalogScanner.close();

        } catch (Exception exception) {
            // File input failed
            exception.printStackTrace();
        }
    }

    public void addProductSpecification(String codeInput, String nameInput, BigDecimal priceInput) {
        productCatalogHashMap.put(codeInput,new ProductSpecification(codeInput,nameInput,priceInput));
    }

    public void deleteProductSpecification(String codeInput) {
        productCatalogHashMap.remove(codeInput);
    }

    public String getProductsStrings(DecimalFormat currentFormat) {
        String codeNamePriceLabelFormat = "%-11s %-15s %-12s", codeNamePriceListFormat = "%-11s %-15s %-8s";
        String returnString = "";

        // Add top of list labels
        returnString = returnString.concat(
                String.format(codeNamePriceLabelFormat,
                        "item code","item name","unit price")
                        + "\n");

        // Loop through productCatalog for code, name, and price of each product
        for (Map.Entry<String, ProductSpecification> productCatalogTracker : productCatalogHashMap.entrySet()) {
            returnString = returnString.concat(
                    String.format(codeNamePriceListFormat,
                            productCatalogTracker.getValue().getProductCode(),
                            productCatalogTracker.getValue().getProductName(),
                            currentFormat.format(productCatalogTracker.getValue().getProductPrice()))
                            + "\n");
        }

        return returnString;
    }

    /**
     * Searches for a ProductSpecification object using a giving key, which is equal to the item's code. If found will
     * return object else will return null
     *
     * @param key String, item's code
     * @return ProductSpecification if found, else null
     */
    public ProductSpecification getProductSpecification(String key) {
        return productCatalogHashMap.getOrDefault(key, null);
    }

}
