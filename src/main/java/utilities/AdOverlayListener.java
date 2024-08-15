package utilities;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverListener;

import logging.LoggingManager;


public class AdOverlayListener implements WebDriverListener {

   
    @Override
    public void beforeClick(WebElement element) {
        closeAdOverlay();
        closeCookieOverlay();
    }

    @Override
    public void beforeClear(WebElement element) {
        closeAdOverlay();
    }
    
    @Override
    public void afterSubmit(WebElement element) {
        closeAdOverlay();
        closeCookieOverlay();
    }
    
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
    
    public static void closeCookieOverlay() {
        try {
            WebElement cookiePopupCloseButton = DriverFactory.getDriver().findElement(By.className("cookies-banner-module_dismiss-button_24Z98"));
            if (cookiePopupCloseButton.isDisplayed()) {
            	cookiePopupCloseButton.click();
                System.out.println("Cookie overlay closed.");
                LoggingManager.info("Cookie blocker overlay closed.");
            }
        } catch (NoSuchElementException e) {
            // Ad overlay not present, do nothing
        }
    }
    

    @Override
    public void afterClick(WebElement element) {
    	closeAdOverlay();
    }

    
}

