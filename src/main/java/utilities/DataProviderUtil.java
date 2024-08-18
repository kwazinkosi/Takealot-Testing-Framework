package utilities;

import file.ExcelReader;
import org.testng.annotations.DataProvider;
import java.io.File;

/**
 * DataProviderUtil class provides DataProvider methods to supply test data
 * from Excel sheets to TestNG test methods.
 */
public class DataProviderUtil {

    private static final String DATA_PATH = System.getProperty("user.dir") + File.separator + "src" + File.separator
            + "test" + File.separator + "resources" + File.separator + "testdata.xlsx";
    private static ExcelReader reader;

    // Initialize the ExcelReader once in the constructor
    public DataProviderUtil() {
        reader = new ExcelReader(DATA_PATH);
    }

    /**
     * DataProvider method that reads search data from an Excel file.
     *
     * @return A 2D Object array containing search queries from the "search data" sheet.
     */
    @DataProvider(name = "searchData")
    public Object[][] getSearchData() {
        String sheetName = "search data";
        int rowCount = reader.getRowCount(sheetName) - 1; // Adjusted for header row

        // Create a 2D array to hold the query name, search value, and expected result
        Object[][] data = new Object[rowCount][3];

        // Loop through each row and retrieve the query name, search value, and expected result
        for (int i = 0; i < rowCount; i++) {
            data[i][0] = reader.getCellValue(sheetName, i + 1, 0); // Query Name
            data[i][1] = reader.getCellValue(sheetName, i + 1, 1); // Search Value
            data[i][2] = reader.getCellValue(sheetName, i + 1, 2); // Expected Result
        }

        return data;
    }

    /**
     * DataProvider method that reads registration data from an Excel file.
     *
     * @return A 2D Object array containing registration form data from the "registration form" sheet.
     */
    @DataProvider(name = "registrationData")
    public Object[][] getRegistrationData() {
        String sheetName = "registration form";
        int rowCount = reader.getRowCount(sheetName);
        int colCount = 8; // Number of columns in the registration form

        // Create a 2D array to hold the registration data
        Object[][] data = new Object[rowCount][colCount];

        // Loop through each row and retrieve data from the columns
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < colCount; j++) {
                data[i][j] = reader.getCellValue(sheetName, i + 1, j);
            }
        }

        return data;
    }

    /**
     * DataProvider method that reads login data from an Excel file.
     *
     * @return A 2D Object array containing login form data from the "login form" sheet.
     */
    @DataProvider(name = "loginData")
    public Object[][] getLoginData() {
        String sheetName = "login form";
        int rowCount = reader.getRowCount(sheetName);
        int colCount = 6; // Number of columns in the login form

        // Create a 2D array to hold the login data
        Object[][] data = new Object[rowCount][colCount - 1]; // Exclude description column

        // Loop through each row and retrieve data from the columns
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < colCount - 1; j++) {
                data[i][j] = reader.getCellValue(sheetName, i + 1, j);
            }
        }

        return data;
    }

    /**
     * DataProvider method that reads product search data from an Excel file.
     *
     * @return A 2D Object array containing product search data from the "products" sheet.
     */
    @DataProvider(name = "productSearchData")
    public Object[][] getProductSearchData() {
        String sheetName = "products";
        int rowCount = reader.getRowCount(sheetName);
        int colCount = 9; // Assuming there are 9 columns in total

        // Create a 2D array to hold the product search data
        Object[][] data = new Object[rowCount][colCount - 2]; // Exclude header row and first two columns

        // Loop through each row and retrieve data from the columns
        for (int i = 0; i < rowCount; i++) {
            for (int j = 2; j < colCount; j++) {
                data[i][j - 2] = reader.getCellValue(sheetName, i + 1, j);
            }
        }

        return data;
    }

    /**
     * Retrieves the value from the specified sheet and key.
     *
     * @param sheetName The name of the sheet to read from.
     * @param key       The key to search for in the first column of the sheet.
     * @return The value corresponding to the key in the second column, or null if not found.
     */
    public String getValue(String sheetName, String key) {
        // Use the existing getValue method from ExcelReader
        return reader.getValue(sheetName, key);
    }
}
