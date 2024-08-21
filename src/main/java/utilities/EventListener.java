package utilities;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverListener;
import logging.LoggingManager;

/**
 * A WebDriver listener that handles the automatic closing of ad and cookie overlays
 * that might appear during WebDriver interactions. This listener is particularly useful
 * for avoiding interruptions caused by popups while interacting with elements on the page.
 */
public class EventListener implements WebDriverListener {

    /**
     * Called before a click event occurs. This method attempts to close any ad or cookie overlays
     * that may obstruct the element.
     * 
     * @param element the WebElement that will be clicked.
     */
    @Override
    public void beforeClick(WebElement element) {
//        closeAdOverlay();
//        closeCookieOverlay();
    }

    /**
     * Called before a clear event occurs on an element (e.g., clearing a text field).
     * This method attempts to close any ad overlays that may obstruct the element.
     * 
     * @param element the WebElement that will be cleared.
     */
    @Override
    public void beforeClear(WebElement element) {

    }

    /**
     * Called after a submit event occurs on an element (e.g., submitting a form).
     * This method attempts to close any ad or cookie overlays that may appear after the submit action.
     * 
     * @param element the WebElement that was submitted.
     */
    @Override
    public void afterSubmit(WebElement element) {
        closeAdOverlay();
        closeCookieOverlay();
    }

    @Override
    public void beforeSendKeys(WebElement element, CharSequence... keysToSend) {
    	element.clear();
    	closeAdOverlay();
    }
    /**
     * Attempts to close any ad overlay that may appear on the page. This method looks for
     * an ad popup close button with the text "NOT NOW" and clicks it if found.
     */
    public static void closeAdOverlay() {
        try {
            WebElement adPopupCloseButton = DriverFactory.getDriver().findElement(By.xpath("//button[@class='ab-message-button' and text()='NOT NOW']"));
            if (adPopupCloseButton.isDisplayed()) {
                adPopupCloseButton.click();
                System.out.println("Ad overlay closed.");
                LoggingManager.info("Ad blocker overlay closed.");
            }
        } catch (NoSuchElementException e) {
            // Ad overlay not present, do nothing
        }
    }

    /**
     * Attempts to close any cookie overlay that may appear on the page. This method looks for
     * a cookie popup close button and clicks it if found.
     */
    public static void closeCookieOverlay() {
        try {
            WebElement cookiePopupCloseButton = DriverFactory.getDriver().findElement(By.className("cookies-banner-module_dismiss-button_24Z98"));
            if (cookiePopupCloseButton.isDisplayed()) {
                cookiePopupCloseButton.click();
                System.out.println("Cookie overlay closed.");
                LoggingManager.info("Cookie blocker overlay closed.");
            }
        } catch (NoSuchElementException e) {
            // Cookie overlay not present, do nothing
        }
    }

    public void moveToWishlistAlert() {
        JavascriptExecutor js = (JavascriptExecutor) DriverFactory.getDriver();
        Boolean alertPresent = (Boolean) js.executeScript(
            "return document.querySelector('.Toastify__toast.Toastify__toast--success.toast.cursor-default') !== null;"
        );
        if (alertPresent) {
            LoggingManager.info("Custom alert detected.");
            js.executeScript("document.querySelector('.button[aria-label='close']').click();");
        }
    }
    
    /**
     * Called after a click event occurs. This method attempts to close any ad overlay
     * that may appear after the click action.
     * 
     * @param element the WebElement that was clicked.
     */
    @Override
    public void afterClick(WebElement element) {
//        closeAdOverlay();
    }
}
