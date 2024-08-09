package utilities;

import file.ExcelReader;
import org.testng.annotations.DataProvider;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DataProviderUtil {

    private static final String data_path = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "testdata.xlsx";

    /**
     * DataProvider method that reads search data from an Excel file.
     *
     * @return A 2D Object array containing search queries from the "search data" sheet.
     */
    @DataProvider(name = "searchData")
    public Object[][] getSearchData() {
    	ExcelReader reader = new ExcelReader(data_path);
        String sheetName = "search data";

        // Get the total number of rows in the sheet
        int rowCount = reader.getRowCount(sheetName);

        // Create a 2D array to hold the query name, search value, and expected result
        Object[][] data = new Object[rowCount][3];

        // Loop through each row and retrieve the query name, search value, and expected result
        for (int i = 0; i < rowCount; i++) {
            data[i][0] = reader.getCellValue(sheetName, i + 1, 0); // Query Name
            data[i][1] = reader.getCellValue(sheetName, i + 1, 1); // Search Value
            data[i][2] = reader.getCellValue(sheetName, i + 1, 2); // Expected
        }

        return data;
    }
}
