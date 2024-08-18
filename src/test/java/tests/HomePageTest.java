package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.openqa.selenium.support.events.WebDriverListener;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import config.ConfigReader;
import logging.LoggingManager;
import utilities.EventListener;
import pages.BasePage;
import pages.CartPage;
import pages.HomePage;
import pages.LoginPage;
import pages.RegistrationPage;
import utilities.DataProviderUtil;
import utilities.DriverFactory;

/**
 * Test class for verifying various functionalities on the HomePage.
 */
public class HomePageTest {

    private WebDriver driver;
    private HomePage homePage;

    /**
     * Setup method that runs before the test class.
     * Initializes the WebDriver, applies event listeners, and navigates to the HomePage.
     */
    @BeforeClass
    public void setUp() {

        // Apply the WebDriverListener to capture events during test execution
        WebDriverListener listener = new EventListener();
        driver = new EventFiringDecorator<>(listener).decorate(driver);

        // Navigate to the base URL and initialize the HomePage object
        driver.get(ConfigReader.getProperty("base_url"));
        homePage = new HomePage(driver);

        // Set the WebDriver for reporting purposes
        BasePage.reporter.setDriver(driver);
        LoggingManager.info("\n\n*************** STARTING HOMEPAGE TESTS **************");
    }

    @BeforeMethod
    public void setup() {
        // Optionally, you can set the driver again before each test method
    	BasePage.reporter.setDriver(DriverFactory.getDriver());
    }
    /**
     * Test to verify that the HomePage title matches the expected value.
     */
    @Test(priority = 0, groups = {"homepage", "navigation"})
    public void verifyTitle() {
        LoggingManager.info("=========== Testing home page title. ===========");
        
        // Fetch expected title from dataUtil and compare it with the actual title
        String expectedTitle = homePage.dataUtil.getValue("common info", "home_page_title");
        String actualTitle = homePage.getHomeTitle();
        
        Assert.assertEquals(actualTitle, expectedTitle, "Home page title does not match the expected value.");
        LoggingManager.info("Testing home page title -- PASSED\n");
    }

    /**
     * Test to verify navigation to the Registration page.
     */
    @Test(priority = 11, groups = {"homepage", "navigation", "registration"})
    public void testNavigateToRegister() {
        LoggingManager.info("=========== Testing navigation to registration. ===========");
        
        // Navigate to the Registration page and verify visibility
        RegistrationPage registrationPage = homePage.navigateToRegister();
        Assert.assertTrue(registrationPage.isVisible(), "Signup modal should be visible.");
        
        // Close the registration modal
        registrationPage.closeSignup();
        LoggingManager.info("Navigated to registration successfully -- Passed\n\n");
    }

    /**
     * Test to verify navigation to the Login page.
     */
    @Test(priority = 12, groups = {"homepage", "navigation", "login"})
    public void testNavigateToLogin() {
        LoggingManager.info("=========== Testing navigation to login. ===========");
        
        // Navigate to the Login page and verify visibility
        LoginPage loginPage = homePage.navigateToLogin();
        Assert.assertTrue(loginPage.isVisible(), "Login modal should be visible.");
        
        // Close the login modal
        loginPage.closeLogin();
        LoggingManager.info("Navigated to login successfully -- Passed\n\n");
    }

    /**
     * Test to verify navigation to the Cart page.
     */
    @Test(priority = 13, groups = {"homepage", "navigation", "cart"})
    public void testNavigateToCart() {
        LoggingManager.info("=========== Testing navigation to cart. ===========");
        
        // Navigate to the Cart page and verify visibility
        homePage.navigateToCart();
        CartPage cartPage = new CartPage(DriverFactory.getDriver());
        Assert.assertTrue(cartPage.isVisible(), "Cart page should be visible.");
        
        // Navigate back to Home from the Cart page
        cartPage.getNavBar().clickNavLink("Home");
        LoggingManager.info("Navigated to cart successfully -- Passed\n\n");
    }

    /**
     * Test to verify the search functionality on the HomePage.
     * The test is data-driven and verifies both valid and invalid search scenarios.
     *
     * @param queryName  The name of the search query.
     * @param searchValue  The value to search for.
     * @param expected  The expected outcome of the search ("results-page" or error message).
     */
    @Test(priority = 14, dataProvider = "searchData", dataProviderClass = DataProviderUtil.class, groups ={"homepage", "search"})
    public void verifySearchFunctionality(String queryName, String searchValue, String expected) {
        LoggingManager.info("=========== Testing search functionality with query: " + searchValue + " ===========");
        
        boolean resultsFound;
        
        // Verify the search results based on the expected outcome
        if (expected.equals("results-page")) {
            resultsFound = homePage.searchValidFor(searchValue).isVisible();
        } else {
            resultsFound = homePage.searchForInvalidInput(searchValue, expected);
        }
        
        Assert.assertTrue(resultsFound, "Could not handle query: " + searchValue);
        LoggingManager.info("Testing search functionality {search value = " + searchValue + "} -- PASSED\n");
    }

    /**
     * Tear down method that runs after the test class.
     * Closes the WebDriver instance.
     */
    @AfterClass
    public void tearDown() {
        DriverFactory.quitDriver(); // Quit the WebDriver instance
        LoggingManager.info("Driver quit successfully.");
    }
}
