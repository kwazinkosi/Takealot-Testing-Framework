package components;


import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.DefaultElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

import utilities.DriverFactory;
import wait.WaitUtil;

public abstract class BaseComponent {
    
	protected WebElement root;
    protected WaitUtil waitUtil;
    public BaseComponent(WebElement root) {
        
    	this.root = root;
        this.waitUtil = new WaitUtil(DriverFactory.getDriver());
        ElementLocatorFactory factory = (field) -> new DefaultElementLocator(root, field);
        PageFactory.initElements(factory, this);
    }
    
    public boolean isVisible(WebElement element) {
    	try {
    		
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    public void scrollToElement(WebElement element) {
        ((JavascriptExecutor) DriverFactory.getDriver()).executeScript("arguments[0].scrollIntoView(true);", element);
    }

}
