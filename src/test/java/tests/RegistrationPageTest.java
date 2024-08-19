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
import pages.RegistrationPage;
import utilities.EventListener;
import utilities.DataProviderUtil;
import utilities.DriverFactory;

/**
 * Test class for verifying the registration process on the website. It uses
 * data-driven testing with TestNG's DataProvider to run multiple test scenarios
 * for the registration functionality.
 */
public class RegistrationPageTest extends BaseTest{

	private WebDriver driver;
	private HomePage homePage;

	/**
	 * Setup method that runs before the test class. Initializes the WebDriver,
	 * applies event listeners, and navigates to the registration page.
	 */
	@BeforeClass
	public void setUp() {
		// Initialize WebDriver
		driver = DriverFactory.initDriver();

		// Apply the WebDriverListener to handle events during the test
		WebDriverListener listener = new EventListener();
		driver = new EventFiringDecorator<>(listener).decorate(driver);

		// Navigate to the base URL
		driver.get(ConfigReader.getProperty("base_url"));

		// Initialize HomePage and navigate to the registration page
		homePage = new HomePage(driver);
		homePage.navigateToRegister();

		// Set the WebDriver for reporting purposes
		BasePage.reporter.setDriver(driver);

		// Log the start of the registration tests
		LoggingManager.info("\n\n*************** STARTING REGISTRATION TESTS **************");
	}

	
	/**
	 * Test method for verifying the registration functionality. This method is
	 * data-driven and runs multiple test scenarios based on the provided data.
	 *
	 * @param caseNum           The test case number.
	 * @param firstName         The first name of the user.
	 * @param lastName          The last name of the user.
	 * @param email             The email address of the user.
	 * @param password          The password for the account.
	 * @param mobileNumber      The mobile number of the user.
	 * @param expectedResult    The expected result of the test (e.g., "Success" or an error message).
	 * @param executionRequired Indicates whether this test case should be executed.
	 */
	@Test(priority = 16, dataProvider = "registrationData", dataProviderClass = DataProviderUtil.class, groups = {"registration", "verification"})
	public void verifyRegistration(String caseNum, String firstName, String lastName, String email, String password,
			String mobileNumber, String expectedResult, String executionRequired) {

		LoggingManager.info("================ Starting Registration Test [ " + caseNum + " ] ================");

		// Skip test case if execution is not required
		if ("No".equalsIgnoreCase(executionRequired)) {
			LoggingManager.info("Skipping this test case as execution is not required");
			throw new SkipException("Skipping this test case as execution is not required");
		}

		// Initialize the RegistrationPage object
		RegistrationPage registrationPage = new RegistrationPage(driver);

		// Fill in the registration form
		registrationPage = registrationPage.typeFirstname(firstName).typeLastname(lastName).typeEmail(email)
				.typePassword(password).typeMobileNumber(mobileNumber);

		// Handle expected results based on the provided data
		if (expectedResult.equals("Success")) {
			boolean otpRequest = registrationPage.submitRegistration().isOtpModalVisible();

			// Assert that OTP request modal is visible after successful registration
			Assert.assertTrue(otpRequest, "OTP not sent.");

			// Close OTP modal and navigate back to registration page for further tests
			registrationPage.closeOtpModal();
			registrationPage.getNavBar().clickNavLink("Register");
			LoggingManager.info("Registration case -- Passed\n\n");

		} else {
			// Submit the registration form and verify error messages
			registrationPage.submitRegistration();
			List<String> actualErrors = registrationPage.getErrorMessages();
			Assert.assertTrue(actualErrors.contains(expectedResult), "Expected error message not displayed.");
			LoggingManager.info("Registration case -- Passed\n\n");
		}
	}

	/**
	 * Tear down method that runs after the test class. Closes the WebDriver
	 * instance to end the session.
	 */
	@AfterClass
	public void tearDown() {
		DriverFactory.quitDriver(); // Quit the WebDriver instance
        LoggingManager.info("Driver quit successfully.");
	}
}
