package components;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.DefaultElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

import logging.LoggingManager;
import utilities.DriverFactory;
import wait.WaitUtil;

/**
 * The BaseComponent class represents a base component object within a web page.
 * It provides common functionalities such as waiting for elements, checking visibility, 
 * and scrolling to elements, which can be reused by any specific component extending this class.
 */
public abstract class BaseComponent {
    
    // The root WebElement representing the component's root element.
    protected WebElement root;
    
    // Utility for waiting for specific conditions or elements.
    protected WaitUtil waitUtil;

    /**
     * Constructor to initialize the BaseComponent with a root WebElement.
     *
     * @param root The root WebElement of the component.
     */
    public BaseComponent(WebElement root) {
        this.root = root;
        this.waitUtil = new WaitUtil(DriverFactory.getDriver());
        
        // Use a custom ElementLocatorFactory to locate elements within the scope of the root element.
        ElementLocatorFactory factory = (field) -> new DefaultElementLocator(root, field);
        PageFactory.initElements(factory, this);
    }

    /**
     * Checks if a given WebElement is visible on the page.
     *
     * @param element The WebElement to check for visibility.
     * @return true if the element is visible; false otherwise.
     */
    public boolean isVisible(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    /**
     * Clicks on the specified web element.
     * Waits for the element to be clickable before performing the click action.
     * 
     * @param element The web element to click.
     */
    public void click(WebElement element) {
        LoggingManager.info("Attempting to click on element: " + element.toString());
        waitUtil.waitForElementToBeClickable(element, 10);
        element.click();
        LoggingManager.info("Clicked on element: " + element.toString());
    }

    /**
     * Scrolls the page until the specified WebElement is in view.
     *
     * @param element The WebElement to scroll into view.
     */
    public void scrollToElement(WebElement element) {
        ((JavascriptExecutor) DriverFactory.getDriver()).executeScript("arguments[0].scrollIntoView(true);", element);
    }
}
