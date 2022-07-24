import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Scanner;

public class Register {

    /**
     *
     */
    ProductCatalog productCatalog;

    /**
     *
     */
    Sale sale;

    private static final String
            WELCOME_MESSAGE                     = "\nWelcome to Peter's cash register system!\n",
            FILENAME_MESSAGE                    = "\nInput file : ",
            FILE_INPUT_ERROR_MESSAGE            = "!!! Invalid input",
            BEGINNING_SALE_MESSAGE              = "\nBeginning a new sale? (Y/N) ",
            SALE_INPUT_ERROR_MESSAGE            = "!!! Invalid input\nShould be (Y/N)",
            CODE_INPUT_INCORRECT_MESSAGE        = "!!! Invalid product code\nShould be A[###] or B[###], 0000 = item list, -1 = quit\n",
            QUANTITY_INPUT_INCORRECT_MESSAGE    = "!!! Invalid quantity\nShould be [1-100]",
            BREAK_LINE                          = "--------------------",
            ENTER_CODE_MESSAGE                  = "\nEnter product code : ",
            ITEM_NAME_MESSAGE                   = "         item name : ",
            ENTER_QUANTITY_MESSAGE              = "\n\tEnter quantity : ",
            ITEM_TOTAL_MESSAGE                  = "        item total : $",
            RECEIPT_LINE                        = "----------------------------",
            RECEIPT_TOP                         = "Items list:\n",
            TENDERED_AMOUNT_RECEIPT             = "Tendered amount",
            TENDER_AMOUNT_WRONG                 = "\nAmount entered is invalid",
            TENDER_AMOUNT_TOO_SMALL             = "\nAmount entered is too small",
            CHANGE_AMOUNT                       = "Change",
            EOD_MESSAGE                         = "\nThe total sale for the day is  $",

    UPDATE_PROMPT_MESSAGE               = "\nDo you want to update the items data? (A/D/M/Q): ",
            UPDATE_ERROR_MESSAGE                = "!!! Invalid input\nShould be (A/D/M/Q)",
            UPDATE_CODE_PROMPT                  = "item code: ",
            UPDATE_NAME_PROMPT                  = "item name: ",
            UPDATE_PRICE_PROMPT                 = "item price: ",

    UPDATE_ITEM_ALREADY_ADDED           = "!!! item already created",
            UPDATE_ITEM_NOT_FOUND               = "!!! item not found",


    UPDATE_CODE_ERROR_MESSAGE           = "!!! Invalid input\nShould be A[###] or B[###]\n",
            UPDATE_NAME_ERROR_MESSAGE           = "!!! Invalid input\nName shouldn't be only digits\n",

    UPDATE_NAME_OLD_MESSAGE             = "!!! Invalid input\nName already used\n",

    UPDATE_PRICE_ERROR_MESSAGE          = "!!! Invalid input\nShould be greater than 0\n",

    UPDATE_ADD_SUCCESSFUL               = "Item add successful!\n",
            UPDATE_DELETE_SUCCESSFUL            = "Item delete successful!\n",
            UPDATE_MODIFY_SUCCESSFUL            = "Item modify successful!\n",

    THANK_YOU                           = "Thanks for using POST system. Goodbye.",

    FILE_NAME_KEY                       = "item.txt";

    /**
     * Format for currency
     */
    private static final DecimalFormat currencyFormat = new DecimalFormat("#,###.00");

    Register() {
        // Initialize ProductCatalog
        productCatalog = new ProductCatalog();
    }

    // object driver class
    void run() {
        boolean saleQuitFlag = false, alterQuitFlag = false;

        // Create systemIn Scanner
        Scanner systemInScanner = new Scanner(System.in);

        // Sale loop
        do {
            // Welcome message
            System.out.print(WELCOME_MESSAGE);

            // Initialize Sale
            sale = new Sale();

            // Prompt for sale
            if (checkOut(systemInScanner) == 1) {
                // Quit sale
                saleQuitFlag = true;
            }

        } while (!saleQuitFlag);

        // Alter loop
        do {
            // Prompt for altering product catalog
            alterQuitFlag = alterCatalog(systemInScanner);

        } while (!alterQuitFlag);

        // Close scanner
        systemInScanner.close();
    }

    public int checkOut(Scanner systemInScanner) {
        // Declare and Initialization
        String eodFormat = "%1$8s";
        String userInput = "";
        int returnInt;

        // Prompt and check input for 'Y'/'y' or 'N'/'n'
        try {
            // Reset quit flag and return int
            returnInt = 0;

            // Loop input and check till correct
            do {
                // Output Beginning Message
                System.out.print(BEGINNING_SALE_MESSAGE);

                // User input
                userInput = systemInScanner.next();

                // Test if input is 1 char long and == Y or y
                if (userInput.matches("[Yy]{1}")) {

                    // Input Correct, user = yes
                    returnInt = 2;

                    // Print text break
                    System.out.print(BREAK_LINE);

                    // New sale
                    findCode(systemInScanner);
                }
                // Test if input is 1 char long and == N or n
                else if (userInput.matches("[Nn]{1}")){

                    // Input Correct, user = no
                    returnInt = 1;

                    // EOD earnings
                    if (sale == null) {
                        System.out.print(EOD_MESSAGE + "0.00");
                    } else {
                        System.out.print(EOD_MESSAGE + String.format(eodFormat, currencyFormat.format(sale.getEodTotal())));
                    }

                    // Prompt for update
                    updateProducts(systemInScanner);

                    // Output thank you message
                    System.out.print("\n" + THANK_YOU);
                }
                else {
                    // Input incorrect
                    // Print incorrect message
                    System.out.print(SALE_INPUT_ERROR_MESSAGE);
                }

            } while (returnInt == 0 || returnInt == 2);

            // Return returnInt
            return returnInt;

        } catch (Exception e) {
            // Input incorrect
            e.printStackTrace();

            return 0;
        }
    }

    public void findCode(Scanner systemInScanner) {
        // Declare and Initialization
        ProductSpecification specification;
        String userInput;
        boolean codeInputFlag, quitFlag = false, tenderCorrectFlag = false;

        // Loop till code input is valid
        do {
            // Reset code flag
            codeInputFlag = false;

            // Prompt for code input
            System.out.print(ENTER_CODE_MESSAGE);

            // User input
            userInput = systemInScanner.next();

            // Check if code input is A or B + [###]
            if (userInput.matches("[AB]\\d\\d\\d")) {
                // Find item
                specification = productCatalog.getProductSpecification(userInput);

                if (specification == null) {
                    // Item not found
                    // Output incorrect code message
                    System.out.print(CODE_INPUT_INCORRECT_MESSAGE);

                } else {
                    // Code input valid
                    // Set input flag to true
                    codeInputFlag = true;

                    // Item found
                    // Output Item name message + item name from itemArray
                    System.out.print(ITEM_NAME_MESSAGE + specification.getProductName());

                    // Run findQuantity
                    findQuantity(systemInScanner, specification);
                }
            } else if (userInput.equals("-1")) {
                // Change flags for quiting and code input to true
                quitFlag = true;
                codeInputFlag = true;

                // Print receipt top
                System.out.println(sale.createReceipt(currencyFormat));

                // Loop till tendered amount is larger than total with tax
                do {
                    try {
                        // Prompt for tender amount
                        System.out.printf("\n%-21s$ ",TENDERED_AMOUNT_RECEIPT);

                        // User input
                        BigDecimal tenderAmount = BigDecimal.valueOf(Double.parseDouble(systemInScanner.next()));
                        BigDecimal subtotalTax = sale.getSubtotalTax();

                        // Tender amount is correct
                        if (tenderAmount.compareTo(subtotalTax) >= 0) {
                            tenderCorrectFlag = true;

                            // Change
                            // Find change by subtracting tenderAmount by Total with tax
                            System.out.printf("\n%-21s$%7s\n%s\n",
                                    CHANGE_AMOUNT,
                                    currencyFormat.format(tenderAmount.subtract(subtotalTax)),
                                    RECEIPT_LINE
                            );

                            // Reset sale
                            sale.resetSale();
                        }
                        else {
                            // Tender is wrong
                            System.out.print(TENDER_AMOUNT_TOO_SMALL);
                        }
                    } catch (Exception e) {
                        // Tender is wrong
                        System.out.print(TENDER_AMOUNT_WRONG);
                    }

                } while (!tenderCorrectFlag);

            } else if (userInput.equals("0000")) {
                // Print item list
                listItems();

            } else {
                // Output incorrect code message
                System.out.print(CODE_INPUT_INCORRECT_MESSAGE);
            }

        } while (!codeInputFlag || !quitFlag);
    }

    public void findQuantity(Scanner systemInScanner, ProductSpecification specification) {
        // Declare and Initialization
        String outputFormat = "%1$8s";
        int productQuantity;
        BigDecimal productPrice;
        boolean quantityInputFlag = false;

        do {
            try {
                // Prompt for item quantity
                System.out.print(ENTER_QUANTITY_MESSAGE);

                // User input
                productQuantity = Integer.parseInt(systemInScanner.next());

                // Check if quantity is [1-100]
                if (productQuantity > 0 && productQuantity <= 100) {
                    // Quantity input valid
                    quantityInputFlag = true;

                    // Calc price
                    productPrice = specification.getProductPrice().multiply(BigDecimal.valueOf(productQuantity));

                    // Add Item price to Sale object
                    sale.addSalesLineItem(specification,productQuantity);

                    // Output item total
                    System.out.print(ITEM_TOTAL_MESSAGE +  String.format(outputFormat,
                            currencyFormat.format(productPrice) +
                                    "\n"));
                }
                else {
                    // Quantity Input Incorrect
                    // Print incorrect message
                    System.out.print(QUANTITY_INPUT_INCORRECT_MESSAGE);
                }

            } catch (Exception e) {
                // Quantity input is incorrect
                System.out.print(QUANTITY_INPUT_INCORRECT_MESSAGE);
            }

        } while (!quantityInputFlag);
    }

    public void updateProducts(Scanner systemInScanner) {

    }

    // Returns true if finished, else returns false
    public boolean alterCatalog(Scanner systemInScanner) {

        // Prompt for Add, Delete, Modify, or Quit Items
        System.out.print(UPDATE_PROMPT_MESSAGE);

        // User input
        String userInput = systemInScanner.next();

        switch (userInput) {
            case "A" -> {
                // Add
                addProductCatalogItem(systemInScanner);
            }
            case "D" -> {
                // Delete
                deleteProductCatalogItem(systemInScanner);
            }
            case "M" -> {
                // Modify
                modifyProductCatalogItem(systemInScanner);
            }
            case "Q" -> {
                // List products
                System.out.println(listItems());

                // Quit
                return true;
            }
            default -> {
                // Input invalid
                System.out.print(UPDATE_ERROR_MESSAGE);
            }
        }

        return false;
    }

    private void addProductCatalogItem(Scanner systemInScanner) {
        // Declare and Initialization
        BigDecimal priceInput = BigDecimal.valueOf(0);
        String userInput, codeInput = "", nameInput = "";
        boolean codeInputFlag = false, nameInputFlag = false, priceInputFlag = false;

        // prompt for code, name, price
        // Find code
        do {
            // Prompt for code input
            System.out.print(UPDATE_CODE_PROMPT);

            // User input
            userInput = systemInScanner.next();

            // Check if code input is A or B + [###]
            if (userInput.matches("[AB]\\d\\d\\d")) {
                // Code input valid, check if item already created
                if (productCatalog.getProductSpecification(userInput) == null) {
                    // Not created before
                    codeInputFlag = true;
                    codeInput = userInput;

                } else {
                    // Created before
                    System.out.print(UPDATE_ITEM_ALREADY_ADDED + "\n");
                }
            } else {
                // Code input invalid
                System.out.print(UPDATE_CODE_ERROR_MESSAGE);
            }

        } while (!codeInputFlag);

        // Find name
        do {
            // Prompt for code input
            System.out.print(UPDATE_NAME_PROMPT);

            // Clear scanner buffer
            systemInScanner.nextLine();

            // User input
            userInput = systemInScanner.nextLine();

            // Check name if created before
            if (!userInput.equals("")) {
                // Item name not created before
                nameInputFlag = true;
                nameInput = userInput;
            }
        } while (!nameInputFlag);

        // Find price
        do {
            // Prompt for code input
            System.out.print(UPDATE_PRICE_PROMPT);

            // User input
            userInput = systemInScanner.next();

            try {
                if (Double.parseDouble(userInput) > 0) {
                    // Input valid
                    priceInputFlag = true;
                    priceInput = BigDecimal.valueOf(Long.parseLong(userInput));

                } else {
                    // Input invalid
                    System.out.print(UPDATE_PRICE_ERROR_MESSAGE);

                }
            } catch (Exception e) {
                // Price input invalid
                System.out.print(UPDATE_PRICE_ERROR_MESSAGE);
            }
        } while (!priceInputFlag);

        // Add product specification to product catalog
        productCatalog.addProductSpecification(codeInput,nameInput,priceInput);

        // Output success message
        System.out.print(UPDATE_ADD_SUCCESSFUL);
    }

    private void deleteProductCatalogItem(Scanner systemInScanner) {
        // Declare and Initialization
        String userInput, codeInput = "";
        boolean codeInputFlag = false;

        // prompt for code, name, price
        // Find code
        do {
            // Prompt for code input
            System.out.print(UPDATE_CODE_PROMPT);

            // User input
            userInput = systemInScanner.next();

            // Check if code input is A or B + [###]
            if (userInput.matches("[AB]\\d\\d\\d")) {
                // Code input valid, check if item already created
                if (productCatalog.getProductSpecification(userInput) == null) {
                    // Not created before
                    codeInputFlag = true;
                    codeInput = userInput;

                } else {
                    // Created before
                    System.out.print(UPDATE_ITEM_ALREADY_ADDED);
                }
            } else {
                // Code input invalid
                System.out.print(UPDATE_CODE_ERROR_MESSAGE);
            }

        } while (!codeInputFlag);

        // Delete item from item list
        productCatalog.deleteProductSpecification(codeInput);

        // Output success message
        System.out.print(UPDATE_DELETE_SUCCESSFUL);
    }

    private void modifyProductCatalogItem(Scanner systemInScanner) {
        // Declare and Initialization
        BigDecimal priceInput = BigDecimal.valueOf(0);
        String userInput, codeInput = "", nameInput = "";
        boolean codeInputFlag = false, nameInputFlag = false, priceInputFlag = false;

        // prompt for code, name, price
        // Find code
        do {
            // Prompt for code input
            System.out.print(UPDATE_CODE_PROMPT);

            // User input
            userInput = systemInScanner.next();

            // Check if code input is A or B + [###]
            if (userInput.matches("[AB]\\d\\d\\d")) {
                // Code input valid, check if item already created
                if (productCatalog.getProductSpecification(userInput) == null) {
                    // Not created before
                    System.out.print(UPDATE_ITEM_NOT_FOUND);

                } else {
                    // Created before
                    codeInputFlag = true;
                    codeInput = userInput;

                }
            } else {
                // Code input invalid
                System.out.print(UPDATE_CODE_ERROR_MESSAGE);
            }

        } while (!codeInputFlag);

        // Find name
        do {
            // Prompt for code input
            System.out.print(UPDATE_NAME_PROMPT);

            // Clear scanner buffer
            systemInScanner.nextLine();

            // User input
            userInput = systemInScanner.nextLine();

            // Check name if created before
            if (!userInput.equals("")) {
                // Item name not created before
                nameInputFlag = true;
                nameInput = userInput;
            }
        } while (!nameInputFlag);

        // Find price
        do {
            // Prompt for code input
            System.out.print(UPDATE_PRICE_PROMPT);

            // User input
            userInput = systemInScanner.next();

            try {
                if (Double.parseDouble(userInput) > 0) {
                    // Input valid
                    priceInputFlag = true;
                    priceInput = BigDecimal.valueOf(Long.parseLong(userInput));

                } else {
                    // Input invalid
                    System.out.print(UPDATE_PRICE_ERROR_MESSAGE);

                }
            } catch (Exception e) {
                // Price input invalid
                System.out.print(UPDATE_PRICE_ERROR_MESSAGE);
            }
        } while (!priceInputFlag);

        // Remove old productSpecification
        productCatalog.deleteProductSpecification(codeInput);

        // Add new productSpecification
        productCatalog.addProductSpecification(codeInput,nameInput,priceInput);

        // Output success message
        System.out.print(UPDATE_MODIFY_SUCCESSFUL);
    }

    // Calls for a list of all products stored in ProductCatalog
    private String listItems() {
        return productCatalog.getProductsStrings(currencyFormat);
    }

}
