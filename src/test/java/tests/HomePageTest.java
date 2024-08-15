package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.openqa.selenium.support.events.WebDriverListener;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import config.ConfigReader;
import logging.LoggingManager;
import utilities.AdOverlayListener;
import pages.BasePage;
import pages.HomePage;
import reporting.ReportManager;
import utilities.DataProviderUtil;
import utilities.DriverFactory;

public class HomePageTest {

    private WebDriver driver;
    private HomePage homePage;

    @BeforeClass
    public void setUp() {
        
        driver = DriverFactory.initDriver();

        // Apply the WebDriverListener
        WebDriverListener listener = new AdOverlayListener(driver);
        driver = new EventFiringDecorator<>(listener).decorate(driver);

        driver.get(ConfigReader.getProperty("base_url"));
        homePage = new HomePage(driver);
        BasePage.reporter.setDriver(driver);
    }

    @Test(priority = 0)
    public void verifyTitle() {
    	
        LoggingManager.info("Testing home page title");
        String expectedTitle = homePage.dataUtil.getValue("common info", "home_page_title");
        String actualTitle = homePage.getHomeTitle();
        Assert.assertEquals(actualTitle, expectedTitle, "Home page title does not match the expected value.");
        LoggingManager.info("Testing home page title -- PASSED \n");
    }

    @Test(priority = 1, dataProvider = "searchData", dataProviderClass = DataProviderUtil.class)
    public void verifySearchFunctionality(String queryName, String searchValue, String expected) {
        LoggingManager.info("Testing search functionality with query: " + searchValue);
        
        boolean resultsFound = false;
        if (expected.equals("results-page")) {
            resultsFound = homePage.searchValidFor(searchValue).isResultsVisible();
        } else {
            resultsFound = homePage.searchForInvalidInput(searchValue, expected);
        }
        
        Assert.assertTrue(resultsFound, "Could not handle query: " + searchValue);
        LoggingManager.info("Testing search functionality {search value = " + searchValue + " -- PASSED \n");
    }
    
    
    
    @AfterClass
    public void tearDown() {
        DriverFactory.quitDriver(); // Quit the WebDriver instance
    }
}
