package pages;

import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import components.NavBar;
import config.ConfigReader;
import logging.LoggingManager;
import reporting.ReportManager;
import utilities.ActionUtil;
import utilities.DataProviderUtil;
import utilities.DriverFactory;
import wait.WaitUtil;

/**
 * BasePage is an abstract class that serves as the base for all page classes.
 * It provides common functionality for interacting with web elements and
 * managing page-specific actions.
 */
abstract public class BasePage {

	protected WebDriver driver;
	protected WaitUtil waitUtil;
	public static ReportManager reporter;
	public DataProviderUtil dataUtil;
	public ActionUtil actionUtil;
	private static NavBar navBar;

	public static final int slowWaitTime = Integer.parseInt(ConfigReader.getProperty("slow_wait_time"));
	public static final int normalWaitTime = Integer.parseInt(ConfigReader.getProperty("normal_wait_time"));
	public static final int fastWaitTime = Integer.parseInt(ConfigReader.getProperty("fast_wait_time"));
	public static final int fasterWaitTime = Integer.parseInt(ConfigReader.getProperty("faster_wait_time"));

	/**
	 * Constructor for the BasePage class. Initializes common utilities and
	 * PageFactory.
	 * 
	 * @param driver The WebDriver instance used to interact with the web page.
	 */
	public BasePage(WebDriver driver) {
		this.driver = driver;
		this.waitUtil = new WaitUtil(driver);
		reporter = new ReportManager();
		this.dataUtil = new DataProviderUtil();
		this.actionUtil = new ActionUtil(driver);
		navBar = new NavBar(getNavBarElement());
		PageFactory.initElements(driver, this);

		LoggingManager.info("Page factory initialized for " + this.getClass().getSimpleName());
	}

	/**
	 * Abstract method to determine the visibility of the page. Must be implemented
	 * in the child class.
	 * 
	 * @return True if the page is visible, otherwise false.
	 */
	abstract public boolean isVisible();

	/**
	 * Abstract method to determine the visibility of an alert on the page. Must be
	 * implemented in the child class.
	 * 
	 * @return True if the alert is visible, otherwise false.
	 */
	abstract public boolean isAlertVisible();

	/**
	 * Retrieves the WebElement for the navigation bar using waitFor.
	 * 
	 * @return The WebElement representing the navBar.
	 */
	private WebElement getNavBarElement() {
		waitUtil.waitImplicitly(1);
		return waitUtil.waitFor(driver -> {
			try {
				LoggingManager.info("Getting nav element");
				return driver.findElement(By.cssSelector(".top-nav.top-nav-module_top-nav_2cmJW"));
			} catch (NoSuchElementException e) {
				LoggingManager.error("Failed to locate element: " + e.getMessage(), e);
				return null;
			}
		}, 20);
	}

	/**
	 * Retrieves the NavBar component. Initializes the NavBar if it's null.
	 * 
	 * @return The NavBar component.
	 */
	public NavBar getNavBar() {
		if (navBar == null) {
			navBar = new NavBar(getNavBarElement());
		}
		return navBar;
	}

	/**
	 * Gets the WaitUtil instance used for waiting operations.
	 * 
	 * @return The WaitUtil instance.
	 */
	public WaitUtil getWait() {
		return waitUtil;
	}

	/**
	 * Clicks on the specified web element. Waits for the element to be clickable
	 * before performing the click action.
	 * 
	 * @param element The web element to click.
	 */
	public void click(WebElement element) {
		LoggingManager.info("Attempting to click on element: " + element.toString());
		reporter.log("Clicking on element: " + element.toString()); // ReportManager log
		waitUtil.waitForElementToBeClickable(element, fastWaitTime);
		try {
			element.click();
		} catch (Exception e) {
			actionUtil.clickElementUsingJS(element);
		}
		LoggingManager.info("Clicked on element: "+element.toString());reporter.log("Clicked on element: "+element.toString()); // ReportManager
																																// log

	}

	/**
	 * Sends the specified keys to the given web element. Waits for the element to
	 * be visible before sending keys.
	 * 
	 * @param element The web element to send keys to.
	 * @param keys    The keys to send.
	 */
	public void sendKeys(WebElement element, String keys) {
		LoggingManager.info("Sending keys '" + keys + "' to element: " + element.toString());
		reporter.log("Sending keys '" + keys + "' to element: " + element.toString()); // ReportManager log
		waitUtil.waitForElementToBeVisible(element, fastWaitTime);
		click(element);
		element.clear();
		waitUtil.waitFor(ExpectedConditions.textToBePresentInElementValue(element, ""), fastWaitTime);
		element.sendKeys(keys);
		LoggingManager.info("Sent keys '" + keys + "' to element: " + element.toString());
		reporter.log("Sent keys '" + keys + "' to element: " + element.toString()); // ReportManager log
	}

	/**
	 * Checks if the specified web element is visible.
	 * 
	 * @param element The web element to check.
	 * @return True if the element is visible, otherwise false.
	 */
	public boolean isVisible(WebElement element) {
		LoggingManager.info("Checking visibility of element: " + element.toString());
		reporter.log("Checking visibility of element: " + element.toString()); // ReportManager log
		try {
			boolean visible = waitUtil.waitForElementToBeVisible(element, fastWaitTime) != null;
			LoggingManager.info("Element visibility status: " + visible);
			reporter.log("Element visibility status: " + visible); // ReportManager log
			return visible;
		} catch (Exception e) {
			LoggingManager.error("Error checking visibility of element: " + element.toString(), e);
			reporter.log("Error checking visibility of element: " + element.toString() + " - " + e.getMessage()); // ReportManager
																													// log
			return false;
		}
	}

	/**
	 * Checks if the specified web element is present in the DOM.
	 * 
	 * @param element The web element to check.
	 * @return True if the element is present, otherwise false.
	 */
	public boolean isPresent(WebElement element) {
		LoggingManager.info("Checking presence of element: " + element.toString());
		reporter.log("Checking presence of element: " + element.toString()); // ReportManager log
		try {
			boolean present = waitUtil.waitForElementToBePresent(element, fastWaitTime) != null;
			LoggingManager.info("Element presence status: " + present);
			reporter.log("Element presence status: " + present); // ReportManager log
			return present;
		} catch (Exception e) {
			LoggingManager.error("Error checking presence of element: " + element.toString(), e);
			reporter.log("Error checking presence of element: " + element.toString() + " - " + e.getMessage()); // ReportManager
																												// log
			return false;
		}
	}

	/**
	 * Returns the title of the current page.
	 */
	public String getTitle() {

		return DriverFactory.getDriver().getTitle();
	}
}
