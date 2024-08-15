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
import pages.ProductsPage;
import pages.RegistrationPage;
import utilities.AdOverlayListener;
import utilities.DataProviderUtil;
import utilities.DriverFactory;

public class RegistrationPageTest {

	
	private WebDriver driver;
    private HomePage homePage;
    @BeforeClass
    public void setUp() {
        driver = DriverFactory.initDriver();

        // Apply the WebDriverListener
        WebDriverListener listener = new AdOverlayListener();
        driver = new EventFiringDecorator<>(listener).decorate(driver);
        driver.get(ConfigReader.getProperty("base_url"));

        // Navigate to a particular products page
        homePage = new HomePage(driver);
        homePage.navigateToRegister();
        BasePage.reporter.setDriver(driver);  // Inject WebDriver into ReportManager
        LoggingManager.info(" \n\n*************** STARTING REGISTRATION TESTS**************");
    }
    
    
    @Test(dataProvider = "registrationData", dataProviderClass = DataProviderUtil.class)
    public void verifyRegistration(String caseNum, String firstName, String lastName, String email, String password, String mobileNumber, String expectedResult, String executionRequired) {
    	
    	LoggingManager.info("================ Starting Registration Test [ "+caseNum+" ] ================");
    	
    	// Check if execution is required for this test case
        if ("No".equalsIgnoreCase(executionRequired)) {
        	LoggingManager.info("Skipping this test case as execution is not required");
            throw new SkipException("Skipping this test case as execution is not required");
        }
    	RegistrationPage registrationPage = new RegistrationPage(driver);
        
        // Fill in registration form
    	registrationPage =registrationPage.typeFirstname(firstName)
                        .typeLastname(lastName)
                        .typeEmail(email)
                        .typePassword(password)
                        .typeMobileNumber(mobileNumber);
        
        
        if (expectedResult.equals("Success")) {
            boolean otpRequest = registrationPage.submitRegistration().isOtpModalVisible();
            // Assert that user is navigated to the HomePage
            Assert.assertTrue(otpRequest, "OTP not sent.");
            registrationPage.closeOtpModal();
            registrationPage.getNavBar().clickNavLink("Register");
            LoggingManager.info(" Registration case -- Passed\n\n");
            
        } else {
            registrationPage.submitRegistration();
            // Verify the expected error message
            List<String> actualErrors = registrationPage.getErrorMessages();
            Assert.assertTrue(actualErrors.contains(expectedResult), "Expected error message not displayed.");
            LoggingManager.info(" Registration case -- Passed\n\n");
        }
    }
    
    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

}
