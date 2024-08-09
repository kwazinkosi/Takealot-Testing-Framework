package utilities;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverListener;


public class AdOverlayListener implements WebDriverListener {

   
	private WebDriver driver;
	public AdOverlayListener(WebDriver driver) {
        this.driver = driver;
    }

    @Override
    public void beforeClick(WebElement element) {
        closeAdOverlay();
    }

    @Override
    public void beforeClear(WebElement element) {
        closeAdOverlay();
    }
    private void closeAdOverlay() {
        try {
            WebElement adPopupCloseButton = driver.findElement(By.xpath("//button[@class='ab-message-button' and text()='NOT NOW']"));
            if (adPopupCloseButton.isDisplayed()) {
                adPopupCloseButton.click();
                System.out.println("Ad overlay closed.");
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

