package pages;

import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import components.CartItem;
import components.NavBar;
import config.ConfigReader;
import logging.LoggingManager;
import reporting.ReportManager;
import utilities.ActionUtil;
import utilities.DataProviderUtil;
import wait.WaitUtil;

abstract public class BasePage {

    protected WebDriver driver;
    protected WaitUtil waitUtil;
    public static ReportManager reporter;
    public DataProviderUtil dataUtil;
	protected ActionUtil actionUtil;
	public NavBar navBar;
	public CartItem cartItem;
	
	public static final int slowWaitTime = Integer.parseInt(ConfigReader.getProperty("slow_wait_time"));
	public static final int normalWaitTime = Integer.parseInt(ConfigReader.getProperty("normal_wait_time"));
	public static final int fastWaitTime = Integer.parseInt(ConfigReader.getProperty("fast_wait_time"));
	public static final int fasterWaitTime = Integer.parseInt(ConfigReader.getProperty("faster_wait_time"));
	
	By cart = By.cssSelector(".cart-items-list-module_cart-items-list_3kWF_");
	By nav = By.cssSelector(".top-nav.top-nav-module_top-nav_2cmJW");
	
    public BasePage(WebDriver driver) {
        
    	this.driver = driver;
        this.waitUtil = new WaitUtil(driver);
        reporter = new ReportManager();
        this.dataUtil = new DataProviderUtil();
        this.actionUtil = new ActionUtil(driver);
        PageFactory.initElements(driver, this);
        this.navBar = new NavBar(getNavBarElement());
        this.cartItem =new CartItem(getCartElement());
        
        LoggingManager.info("Page factory initialized for " + this.getClass().getSimpleName());
    }
    
    /**
     * Abstract method to determine the visibility of the page, implemented in the child class.
     * 
     *
     * @return true if page is visible
     */
	abstract public boolean isVisible();
    
	/**
     * Gets the WebElement for the navBar using waitFor.
     *
     * @return The WebElement representing the navBar.
     */
    private WebElement getNavBarElement() {
        return waitUtil.waitFor(driver -> {
            try {
                return driver.findElement(nav);
            } catch (NoSuchElementException e) {
                return null;
            }
        }, 20);
    }
    /**
     * Gets the WebElement for the cart using waitFor.
     *
     * @return The WebElement representing the navBar.
     */
    private WebElement getCartElement() {
        return waitUtil.waitFor(driver -> {
            try {
                return driver.findElement(cart);
            } catch (NoSuchElementException e) {
                return null;
            }
        }, 20);
    }
    /**
     * Gets the NavBar component.
     *
     * @return The NavBar component.
     */
    public NavBar getNavBar() {
        return navBar;
    }

    /**
     * Gets the cart component.
     *
     * @return The cart component.
     */
    public CartItem getCart() {
        return cartItem;
    }
    public WaitUtil getWait() {
    	return waitUtil;
    }
    
    /**
     * Clicks on the given web element.
     * 
     * @param element The web element to click.
     */
    public void click(WebElement element) {
    	
        LoggingManager.info("Attempting to click on element: ");
        reporter.log("Clicking on element: "); // ReportManager log
        waitUtil.waitForElementToBeClickable(element, normalWaitTime);
        element.click();
        LoggingManager.info("Clicked on element: ");
        reporter.log("Clicked on element: "); // ReportManager log
    }

    /**
     * Sends keys to the given web element.
     * 
     * @param element The web element to send keys to.
     * @param keys    The keys to send.
     */
    public void sendKeys(WebElement element, String keys) {
        LoggingManager.info("Sending keys '" + keys + "' to element: ");
        reporter.log("Sending keys '" + keys + "' to element: "); // ReportManager log
        waitUtil.waitForElementToBeVisible(element, normalWaitTime);
        click(element);
        element.clear();
        waitUtil.waitFor(ExpectedConditions.textToBePresentInElementValue(element, ""), normalWaitTime);
        element.sendKeys(keys);
        LoggingManager.info("Sent keys '" + keys + "' to element: " + element.toString());
        reporter.log("Sent keys '" + keys + "' to element: " + element.toString()); // ReportManager log
    }

    /**
     * Checks if the given web element is visible.
     * 
     * @param element The web element to check.
     * @return True if the element is visible, false otherwise.
     */
     public boolean isVisible(WebElement element) {
        LoggingManager.info("Checking visibility of element: " + element.toString());
        reporter.log("Checking visibility of element: " + element.toString()); // ReportManager log
        try {
            boolean visible = waitUtil.waitForElementToBeVisible(element, normalWaitTime) != null;
            LoggingManager.info("Element visibility status: " + visible);
            reporter.log("Element visibility status: " + visible); // ReportManager log
            return visible;
        } catch (Exception e) {
            LoggingManager.error("Error checking visibility of element: " + element.toString(), e);
            reporter.log("Error checking visibility of element: " + element.toString() + " - " + e.getMessage()); // ReportManager log
            return false;
        }
    }

    /**
     * Checks if the given web element is present in the DOM.
     * 
     * @param element The web element to check.
     * @return True if the element is present, false otherwise.
     */
    public boolean isPresent(WebElement element) {
        
    	LoggingManager.info("Checking presence of element: " + element.toString());
        reporter.log("Checking presence of element: " + element.toString()); // ReportManager log
        try {
            boolean present = waitUtil.waitForElementToBePresent(element, normalWaitTime) != null;
            LoggingManager.info("Element presence status: " + present);
            reporter.log("Element presence status: " + present); // ReportManager log
            return present;
        } catch (Exception e) {
            LoggingManager.error("Error checking presence of element: " + element.toString(), e);
            reporter.log("Error checking presence of element: " + element.toString() + " - " + e.getMessage()); // ReportManager log
            return false;
        }
    }
}
