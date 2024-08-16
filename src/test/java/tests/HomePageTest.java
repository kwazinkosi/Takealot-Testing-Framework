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
import pages.CartPage;
import pages.HomePage;
import pages.LoginPage;
import pages.RegistrationPage;
import utilities.DataProviderUtil;
import utilities.DriverFactory;

public class HomePageTest {

    private WebDriver driver;
    private HomePage homePage;

    @BeforeClass
    public void setUp() {
        
        driver = DriverFactory.initDriver();

        // Apply the WebDriverListener
        WebDriverListener listener = new AdOverlayListener();
        driver = new EventFiringDecorator<>(listener).decorate(driver);

        driver.get(ConfigReader.getProperty("base_url"));
        homePage = new HomePage(driver);
        BasePage.reporter.setDriver(driver);
        LoggingManager.info(" \n\n*************** STARTING HOMEPAGE TESTS**************");
    }

    @Test(priority = 0)
    public void verifyTitle() {
    	
        LoggingManager.info("=========== Testing home page title. ===========");
        String expectedTitle = homePage.dataUtil.getValue("common info", "home_page_title");
        String actualTitle = homePage.getHomeTitle();
        Assert.assertEquals(actualTitle, expectedTitle, "Home page title does not match the expected value.");
        LoggingManager.info("Testing home page title -- PASSED \n");
    }

    @Test(priority = 1)
    public void testNavigateToRegister() {
    	
    	LoggingManager.info("===========Testing navigation to registration. ===========");
        RegistrationPage registrationPage = homePage.navigateToRegister();
        Assert.assertTrue(registrationPage.isVisible(), "Signup modal should be visible.");
        registrationPage.closeSignup();
        LoggingManager.info("navigated to registration succesfully -- Passed\n\n");
    }

    @Test(priority = 2)
    public void testNavigateToLogin() {
    	
    	LoggingManager.info("=========== Testing navigation to login. ===========");
        LoginPage loginPage = homePage.navigateToLogin();
        Assert.assertTrue(loginPage.isVisible(), "Login modal should be visible.");
        loginPage.closeLogin();
        LoggingManager.info("navigated to login succesfully -- Passed\n\n");
    }
    
    @Test(priority = 3)
    public void testNavigateToCart() {
    	
    	LoggingManager.info("=========== Testing navigation to cart. ===========");
    	CartPage cartPage = homePage.navigateToCart();
        Assert.assertTrue(cartPage.isVisible(), "Login modal should be visible.");
        cartPage.navBar.clickNavLink("Home");
        LoggingManager.info("navigated to cart succesfully -- Passed\n\n");
    }
    
    @Test(priority = 4, dataProvider = "searchData", dataProviderClass = DataProviderUtil.class)
    public void verifySearchFunctionality(String queryName, String searchValue, String expected) {
        
    	LoggingManager.info("===========Testing search functionality with query: " + searchValue+" ===========");   
        boolean resultsFound = false;
        if (expected.equals("results-page")) {
            resultsFound = homePage.searchValidFor(searchValue).isVisible();
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
