package tests;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.openqa.selenium.support.events.WebDriverListener;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import config.ConfigReader;
import logging.LoggingManager;
import pages.BasePage;
import pages.HomePage;
import pages.LoginPage;
import pages.RegistrationPage;
import utilities.EventListener;
import utilities.DataProviderUtil;
import utilities.DriverFactory;

public class LoginPageTest {

	private WebDriver driver;
    private HomePage homePage;
    private LoginPage loginPage;
    @BeforeClass
    public void setUp() {
        driver = DriverFactory.initDriver();

        // Apply the WebDriverListener
        WebDriverListener listener = new EventListener();
        driver = new EventFiringDecorator<>(listener).decorate(driver);
        driver.get(ConfigReader.getProperty("base_url"));

        // Navigate to a particular products page
        homePage = new HomePage(driver);
        loginPage = homePage.navigateToLogin();
        BasePage.reporter.setDriver(driver);  // Inject WebDriver into ReportManager
        LoggingManager.info(" \n\n*************** STARTING LOGIN TESTS**************");
    }
    
    @Test(dataProvider = "loginData", dataProviderClass = DataProviderUtil.class)
    public void verifyLogin(String caseNum, String email, String password, String expectedResult, String executionRequired) {
    	
    	LoggingManager.info("================ Starting Login Test [ " + caseNum + " ] ================");

        // Check if execution is required for this test case
        if ("No".equalsIgnoreCase(executionRequired)) {
            LoggingManager.info("Skipping this test case as execution is not required");
            throw new SkipException("Skipping this test case as execution is not required");
        }

        
        loginPage.typeEmail(email)
                 .typePassword(password)
                 .submitLogin();

        if ("Success".equalsIgnoreCase(expectedResult)) {
            Assert.assertTrue(!loginPage.isVisible(), "Login modal should not be visible after successful login.");
            LoggingManager.info("Login successful, user redirected to HomePage.\n\n");
        } else {
            Assert.assertTrue(loginPage.getErrorMessages().contains(expectedResult), "Expected error message not displayed.");
            LoggingManager.info("Login failed as expected with error message: " + expectedResult+". --Passed\n\n");
        } 
    }
    
    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
