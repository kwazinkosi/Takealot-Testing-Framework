package utilities;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import logging.LoggingManager;

public class ActionUtil {

	private WebDriver driver;
    private Actions actions;
    private JavascriptExecutor jsExecutor;

    public ActionUtil(WebDriver driver) {
        this.driver = driver;
        this.actions = new Actions(driver);
        this.jsExecutor = (JavascriptExecutor) driver;
    }

    /**
     * Scrolls to a specific WebElement.
     *
     * @param element The WebElement to scroll to.
     */
    public void scrollToElement(WebElement element) {
        LoggingManager.info("Scrolling to element");
        jsExecutor.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    /**
     * Scrolls by a specific number of pixels vertically.
     *
     * @param pixels The number of pixels to scroll by. Positive values scroll down, negative values scroll up.
     */
    public void scrollBy(int pixels) {
    	 LoggingManager.info("Scrolling by {} pixels vertically."+ pixels);
        jsExecutor.executeScript("window.scrollBy(0," + pixels + ");");
    }

    /**
     * Moves the mouse to the center of the WebElement.
     *
     * @param element The WebElement to move the mouse to.
     */
    public void moveToElement(WebElement element) {
    	 LoggingManager.info("Moving mouse to element: ");
        actions.moveToElement(element).perform();
    }

    /**
     * Clicks on a WebElement using JavaScript.
     *
     * @param element The WebElement to click.
     */
    public void clickElementUsingJS(WebElement element) {
    	 LoggingManager.info("Clicking element using JavaScript: {}");
        jsExecutor.executeScript("arguments[0].click();", element);
    }

    /**
     * Double-clicks on a WebElement.
     *
     * @param element The WebElement to double-click on.
     */
    public void doubleClickElement(WebElement element) {
        LoggingManager.info("Double-clicking on element: {}");
        actions.doubleClick(element).perform();
    }

    /**
     * Right-clicks on a WebElement.
     *
     * @param element The WebElement to right-click on.
     */
    public void rightClickElement(WebElement element) {
    	 LoggingManager.info("Right-clicking on element: {}");
        actions.contextClick(element).perform();
    }

    /**
     * Drags and drops a source element to a target element.
     *
     * @param source The source WebElement to drag.
     * @param target The target WebElement to drop.
     */
    public void dragAndDrop(WebElement source, WebElement target) {
    	 LoggingManager.info("Dragging element from source: { "+ source+" } to target: { "+target+"}");
        actions.dragAndDrop(source, target).perform();
    }

    /**
     * Clicks and holds on an element.
     *
     * @param element The WebElement to click and hold.
     */
    public void clickAndHoldElement(WebElement element) {
    	 LoggingManager.info("Clicking and holding element: {}");
        actions.clickAndHold(element).perform();
    }

    /**
     * Releases a clicked and held element.
     *
     * @param element The WebElement to release.
     */
    public void releaseElement(WebElement element) {
    	 LoggingManager.info("Releasing element: {}");
        actions.release(element).perform();
    }

    /**
     * Sends a sequence of keys to an element.
     *
     * @param element The WebElement to send keys to.
     * @param keys The keys to send.
     */
    public void sendKeysToElement(WebElement element, CharSequence... keys) {
    	LoggingManager.info("Sending keys: {} to element: {}"+ String.join(", ", keys)+ element);
        actions.sendKeys(element, keys).perform();
    }

    /**
     * Scrolls to the element and then clicks on it.
     *
     * @param element The WebElement to scroll to and click.
     */
    public void scrollToAndClick(WebElement element) {
        try {
            LoggingManager.info("Scrolling to element: " + element);
            scrollToElement(element);

            LoggingManager.info("Attempting to click element: " + element);
            element.click();
            LoggingManager.info("Successfully clicked element: " + element);
        } catch (Exception e) {
            LoggingManager.error("Error scrolling to and clicking element: " + element, e);
            throw e;  // Rethrow the exception after logging
        }
    }

}
