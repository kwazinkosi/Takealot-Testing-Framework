package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import wait.WaitUtil;
import logging.LoggingManager;

public class HomePage extends BasePage {
	
    private WebDriver driver;

    @FindBy(xpath = "//input[@name='search']")
    private WebElement searchBox;

    @FindBy(xpath = "//button[@data-ref='search-submit-button']")
    private WebElement searchButton;

    @FindBy(xpath = "//button[@class='ab-message-button' and text()='NOT NOW']")
    private WebElement adPopupCloseButton;

    @FindBy(css = ".related-searches.in-results")
    private WebElement searchResults;

    @FindBy(className = "ab-page-blocker")
    private WebElement pageBlocker;
    
    @FindBy(css = "button.cookies-banner-module_dismiss-button_24Z98")
    private WebElement cookieBlockerBtn;
    
    @FindBy(className = "no-results-title" )
    private WebElement emptyResults;
    
    @FindBy(id ="95726035")
    private WebElement defaultResultsProduct;
    
    public HomePage(WebDriver driver) {
        super(driver);
        this.driver =driver;
    }

    public boolean searchValidFor(String query) {
    	
        
    	if(isVisible(searchBox)) {
        	sendKeys(searchBox, query);
        	click(searchButton);
        	
        }else {
        	boolean blocker = isVisible(pageBlocker); 
            closeBlocker(blocker, adPopupCloseButton);
            sendKeys(searchBox, query);
            click(searchButton);
        }
        
        boolean is_results =false;
        if(waitUtil.waitForElementToBeVisible(searchResults, 20) != null) {
        	is_results =true;
        }
    	LoggingManager.info("Products now visible");
        System.out.println("Products now visible");
        return is_results;
    }
    
    public boolean searchForInvalidInput (String query, String expectedResponse) {
    	
        if(isVisible(searchBox)) {
        	sendKeys(searchBox, query);
        	click(searchButton);
        	
        }else {
        	boolean blocker = isVisible(pageBlocker); 
            closeBlocker(blocker, adPopupCloseButton);
            sendKeys(searchBox, query);
            click(searchButton);
        }
        
        switch(expectedResponse)
        {
        	case "default-page":
        		
        		if(waitUtil.waitForElementToBeVisible(defaultResultsProduct, 20) != null)
                	return true;
        		break;
        	case "empty-results-page":
        		if(waitUtil.waitForElementToBeVisible(emptyResults, 20) != null)
                	return true;
        		break;
        	default:
        		return false;
        		
        }
        return false;
    }
}
