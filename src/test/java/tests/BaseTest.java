package tests;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import pages.BasePage;
import utilities.DriverFactory;

public class BaseTest {
    
    @BeforeMethod
    public void setUp() {
        DriverFactory.initDriver();  // Initialize WebDriver for the current thread
        BasePage.reporter.setDriver(DriverFactory.getDriver());
    }
    
    @AfterMethod
    public void tearDown() {
        DriverFactory.quitDriver();  // Quit WebDriver for the current thread
    }
}

