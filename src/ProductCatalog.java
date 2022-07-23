import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

public class ProductCatalog {

    private final String FILENAME = "item.txt";

    private HashMap<String, ProductSpecification> productCatalogHashMap;

    ProductCatalog() {
        // Initialize Product HashMap
        productCatalogHashMap = new HashMap<>();

        // Input data from file
        try {
            // Create file
            File productCatalogFile = new File(FILENAME);

            // Set up scanner
            Scanner productCatalogScanner = new Scanner(productCatalogFile);

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
