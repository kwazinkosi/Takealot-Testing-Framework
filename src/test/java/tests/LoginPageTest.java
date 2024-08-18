package tests;

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
import utilities.EventListener;
import utilities.DataProviderUtil;
import utilities.DriverFactory;

/**
 * Test class for verifying the login functionality. This class contains
 * data-driven tests that verify successful and unsuccessful login attempts.
 */
public class LoginPageTest {

	private WebDriver driver;
	private HomePage homePage;
	private LoginPage loginPage;

	/**
	 * Setup method that runs before the test class. Initializes the WebDriver,
	 * applies event listeners, and navigates to the login page.
	 */
	@BeforeClass
	public void setUp() {
		// Initialize WebDriver using the DriverFactory
		driver = DriverFactory.initDriver();

		// Apply the WebDriverListener to handle events during the test
		WebDriverListener listener = new EventListener();
		driver = new EventFiringDecorator<>(listener).decorate(driver);

		// Navigate to the base URL
		driver.get(ConfigReader.getProperty("base_url"));

		// Navigate to the login page via the home page
		homePage = new HomePage(driver);
		loginPage = homePage.navigateToLogin();

		// Set the WebDriver for reporting purposes
		BasePage.reporter.setDriver(driver);

		// Log the start of the login tests
		LoggingManager.info("\n\n*************** STARTING LOGIN TESTS **************");
	}

	/**
	 * Test to verify the login functionality. This test is data-driven and verifies
	 * both successful and unsuccessful login attempts.
	 *
	 * @param caseNum           The identifier for the test case.
	 * @param email             The email to be used for login.
	 * @param password          The password to be used for login.
	 * @param expectedResult    The expected result ("Success" for successful login,
	 *                          error message for failure).
	 * @param executionRequired Indicates whether this test case should be executed.
	 */
	@Test(dataProvider = "loginData", dataProviderClass = DataProviderUtil.class)
	public void verifyLogin(String caseNum, String email, String password, String expectedResult,
			String executionRequired) {

		LoggingManager.info("================ Starting Login Test [ " + caseNum + " ] ================");

		// Check if execution is required for this test case
		if ("No".equalsIgnoreCase(executionRequired)) {
			LoggingManager.info("Skipping this test case as execution is not required");
			throw new SkipException("Skipping this test case as execution is not required");
		}

		// Perform login with the provided email and password
		loginPage.typeEmail(email).typePassword(password).submitLogin();

		// Validate the result based on the expected outcome
		if ("Success".equalsIgnoreCase(expectedResult)) {
			// Ensure that the login modal is no longer visible after a successful login
			Assert.assertTrue(!loginPage.isVisible(), "Login modal should not be visible after successful login.");
			LoggingManager.info("Login successful, user redirected to HomePage.\n\n");
		} else {
			// Check if the expected error message is displayed
			Assert.assertTrue(loginPage.getErrorMessages().contains(expectedResult),
					"Expected error message not displayed.");
			LoggingManager.info("Login failed as expected with error message: " + expectedResult + ". -- Passed\n\n");
		}
	}

	/**
	 * Tear down method that runs after the test class. Closes the WebDriver
	 * instance to end the session.
	 */
	@AfterClass
	public void tearDown() {
		if (driver != null) {
			driver.quit();
			LoggingManager.info("Driver quit successfully.");
		}
	}
}
