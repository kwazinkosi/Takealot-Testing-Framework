package tests;

import org.openqa.selenium.WebDriver;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.openqa.selenium.support.events.WebDriverListener;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.Assert;

import config.ConfigReader;
import logging.LoggingManager;
import pages.HomePage;
import reporting.ReportManager;
import utilities.AdOverlayListener;
import utilities.DataProviderUtil;

public class HomePageTest {
    private WebDriver driver;
    private HomePage homePage;

    @BeforeClass
    public void setUp() {
    	
        //LoggingManager.configureLogging();
        
        WebDriver originalDriver = new ChromeDriver();

        // Create an instance of your custom WebDriverListener
        WebDriverListener listener = new AdOverlayListener(originalDriver);
        // Wrap the original WebDriver with the EventFiringDecorator
        WebDriver decoratedDriver = new EventFiringDecorator<>(listener).decorate(originalDriver);

        driver = decoratedDriver;
        driver.get(ConfigReader.getProperty("base_url"));
        homePage = new HomePage(driver);
    }
    
    @Test(dataProvider = "searchData", dataProviderClass = DataProviderUtil.class)
    public void verifySearchFunctionality(String queryName, String searchValue, String expected) {
        LoggingManager.info("Testing search functionality with query: " + searchValue);
        
        boolean results_found = false;
        if(expected.equals("results-page")) {
        	results_found = homePage.searchValidFor(searchValue);
        }else {
        	results_found = homePage.searchForInvalidInput(searchValue, expected);
        }
        Assert.assertTrue(results_found, "Could not handle query: " + searchValue);
        LoggingManager.info("Testing search functionality {search value = "+searchValue+" -- PASSED \n");
    }


    @AfterClass
    public void tearDown() {
        driver.quit();
    }
}
