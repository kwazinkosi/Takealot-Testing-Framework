package tests;

import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import logging.LoggingManager;
import pages.HomePage;
import pages.LoginPage;
import utilities.DataProviderUtil;
import utilities.DriverFactory;

/**
 * Test class for verifying the login functionality. This class contains
 * data-driven tests that verify successful and unsuccessful login attempts.
 */
public class LoginPageTest extends BaseTest {

	private HomePage homePage;
	private LoginPage loginPage;

	/**
	 * Setup method that runs before the test class. Initializes the WebDriver,
	 * applies event listeners, and navigates to the login page.
	 */
	@BeforeClass
	public void setUp() {

		// Navigate to the login page via the home page
		homePage = new HomePage(driver);
		loginPage = homePage.navigateToLogin();

		// Log the start of the login tests
		LoggingManager.info("\n\n\n*************** STARTING LOGIN TESTS **************");
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
	@Test(priority = 15, dataProvider = "loginData", dataProviderClass = DataProviderUtil.class, groups = {"login", "verification"})
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
		DriverFactory.quitDriver(); // Quit the WebDriver instance
        LoggingManager.info("Driver quit successfully.");
	}
}
