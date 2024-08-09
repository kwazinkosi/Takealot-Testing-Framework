package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import logging.LoggingManager;
import reporting.ReportManager;
import wait.WaitUtil;

public class BasePage {

	protected WebDriver driver;
	protected WaitUtil waitUtil;
	public ReportManager reporter;

	public BasePage(WebDriver driver) {

		this.driver = driver;
		this.waitUtil = new WaitUtil(driver);
		this.reporter = new ReportManager(driver);
		PageFactory.initElements(driver, this);
	}

	/**
	 * Clicks on the given web element.
	 * 
	 * @param element The web element to click.
	 */
	public void click(WebElement element) {
		waitUtil.waitForElementToBeClickable(element, 10);
		element.click();
	}

	/**
	 * Sends keys to the given web element.
	 * 
	 * @param element The web element to send keys to.
	 * @param keys    The keys to send.
	 */
	public void sendKeys(WebElement element, String keys) {
		waitUtil.waitForElementToBeVisible(element, 10);
		click(element);
		element.clear();
		waitUtil.waitFor(ExpectedConditions.textToBePresentInElementValue(element, ""), 10);
		element.sendKeys(keys);
	}

	/**
	 * Checks if the given web element is visible.
	 * 
	 * @param element The web element to check.
	 * @return True if the element is visible, false otherwise.
	 */
	public boolean isVisible(WebElement element) {
		try {
			return waitUtil.waitForElementToBeVisible(element, 10) != null;
		} catch (Exception e) {
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
		try {
			return waitUtil.waitForElementToBePresent(element, 10) != null;
		} catch (Exception e) {
			return false;
		}
	}

	public void closeBlocker(boolean blocker, WebElement closeBtn) {

		if (blocker) {

			LoggingManager.info("The page has been blocked");
			System.out.println("The page has been blocked");
			click(closeBtn);
		}
	}
}
