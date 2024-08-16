package utilities;

import file.ExcelReader;
import org.testng.annotations.DataProvider;
import java.io.File;

public class DataProviderUtil {

	private static final String data_path = System.getProperty("user.dir") + File.separator + "src" + File.separator
			+ "main" + File.separator + "resources" + File.separator + "testdata.xlsx";
	private static ExcelReader reader;

	// Initialize the ExcelReader once in the constructor
	public DataProviderUtil() {
		reader = new ExcelReader(data_path);
	}

	/**
	 * DataProvider method that reads search data from an Excel file.
	 *
	 * @return A 2D Object array containing search queries from the "search data"
	 *         sheet.
	 */
	@DataProvider(name = "searchData")
	public Object[][] getSearchData() {

		String sheetName = "search data";
		// Get the total number of rows in the sheet
		int rowCount = reader.getRowCount(sheetName) - 1;

		// Create a 2D array to hold the query name, search value, and expected result
		Object[][] data = new Object[rowCount][3];

		// Loop through each row and retrieve the query name, search value, and expected
		// result
		for (int i = 0; i < rowCount; i++) {

			data[i][0] = reader.getCellValue(sheetName, i + 1, 0); // Query Name
			data[i][1] = reader.getCellValue(sheetName, i + 1, 1); // Search Value
			data[i][2] = reader.getCellValue(sheetName, i + 1, 2); // Expected
		}

		return data;
	}

	@DataProvider(name = "registrationData")
	public Object[][] getRegistrationData() {

		String sheetName = "registration form";
		int rowCount = reader.getRowCount(sheetName);
		int colCount = 8;
		// Create a 2D array to hold the data
		Object[][] data = new Object[rowCount][colCount]; 

		// Loop through each row and retrieve data from the 2nd column onwards
		for (int i = 0; i < rowCount; i++) { // Start from 1 to skip header row
			for (int j = 0; j < colCount; j++) { 
				data[i][j] = reader.getCellValue(sheetName, i + 1, j);
			}
		}

		return data;
	}

	@DataProvider(name = "loginData")
	public Object[][] getLoginData() {

		String sheetName = "login form";
		int rowCount = reader.getRowCount(sheetName);
		int colCount = 6;
		// Create a 2D array to hold the data
		Object[][] data = new Object[rowCount][colCount-1]; // not staring the description column

		// Loop through each row and retrieve data from the 2nd column onwards
		for (int i = 0; i < rowCount; i++) { // Start from 1 to skip header row
			for (int j = 0; j < colCount-1; j++) { 
				data[i][j] = reader.getCellValue(sheetName, i + 1, j);
			}
		}

		return data;
	}
	
	@DataProvider(name = "productSearchData")
	public Object[][] getProductSearchData() {

		String sheetName = "products";
		// Get the total number of rows in the sheet
		int rowCount = reader.getRowCount(sheetName);
		int colCount = 9; // Assuming there are 9 columns in total

		// Create a 2D array to hold the data
		Object[][] data = new Object[rowCount][colCount - 2]; // -1 to exclude header row, -2 to start from the 2nd
																// column

		// Loop through each row and retrieve data from the 2nd column onwards
		for (int i = 0; i < rowCount; i++) { // Start from 1 to skip header row
			for (int j = 2; j < colCount; j++) { // Start from 2 to skip first two columns
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
	 * @return The value corresponding to the key in the second column, or null if
	 *         not found.
	 */
	public String getValue(String sheetName, String key) {

		// Use the existing getValue method from ExcelReader
		String value = reader.getValue(sheetName, key);
		return value;
	}
}
