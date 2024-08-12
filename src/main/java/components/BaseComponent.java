package components;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.DefaultElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

public abstract class BaseComponent {
    protected WebElement root;

    public BaseComponent(WebElement root) {
        this.root = root;
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
}
