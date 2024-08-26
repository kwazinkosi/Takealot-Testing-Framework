package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.openqa.selenium.support.events.WebDriverListener;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import config.ConfigReader;
import logging.LoggingManager;
import pages.BasePage;
import utilities.DriverFactory;
import utilities.EventListener;


/**
 * BaseTest is a parent class for all test classes in the test suite.
 * It handles the setup and teardown of the WebDriver, including applying a 
 * WebDriverListener for event handling and navigating to the base URL.
 */
public class BaseTest {

    /**
     * The WebDriver instance that will be used by the test classes.
     * This is initialized before any test methods are run.
     */
    protected WebDriver driver;

    /**
     * Sets up the WebDriver and navigates to the base URL.
     * 
     * This method is annotated with @BeforeClass, so it runs once before all 
     * test methods in the class. It initializes the WebDriver, applies a 
     * WebDriverListener for event handling, and navigates to the base URL 
     * specified in the configuration.
     */
    @BeforeClass
    public void setupClass() {
        try {
            // Initialize WebDriver
            driver = DriverFactory.initDriver();
            // Apply the WebDriverListener to handle events during the test
            WebDriverListener listener = new EventListener();
            driver = new EventFiringDecorator<>(listener).decorate(driver);
            // Navigate to the base URL
            driver.get(ConfigReader.getProperty("base_url"));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize WebDriver or navigate to base URL.", e);
        }
    }

    /**
     * Sets the WebDriver in the BasePage reporter before each test method.
     * 
     * This method is annotated with @BeforeMethod, so it runs before each test 
     * method. It ensures that the correct WebDriver instance is used during the 
     * test execution by setting it in the BasePage reporter.
     */
    @BeforeMethod
    public void setUp() {
        BasePage.reporter.setDriver(DriverFactory.getDriver());
    }

    /**
     * Cleans up the WebDriver instance after all tests in the class have run.
     * 
     * This method is annotated with @AfterClass, so it runs once after all 
     * test methods in the class have completed. It ensures that the WebDriver 
     * instance is properly quit, avoiding resource leaks.
     */
    @AfterClass
    public void tearDown() {
    	DriverFactory.quitDriver(); // Quit the WebDriver instance
        LoggingManager.info("Driver quit successfully.");
    }
}

