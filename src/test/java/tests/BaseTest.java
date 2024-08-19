package tests;

import org.testng.annotations.BeforeMethod;

import pages.BasePage;
import utilities.DriverFactory;

public class BaseTest {
    
    @BeforeMethod
    public void setUp() {
        BasePage.reporter.setDriver(DriverFactory.getDriver());
    }
    
}

