package components;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import logging.LoggingManager;
import utilities.DriverFactory;

public class NavBar extends BaseComponent {

    private Map<String, Function<WebDriver, WebElement>> navMap;

    @FindBy(xpath = "//a[normalize-space()='Register']")
    private WebElement registerLink;

    @FindBy(xpath = "//a[normalize-space()='Login']")
    private WebElement loginLink;

    @FindBy(xpath = "//ul[@class='top-nav-module_header-links_1Ovug']//a[@data-react-link='true'][normalize-space()='Sell on Takealot']")
    private WebElement becomeASellerLink;

    @FindBy(xpath = "//a[@title='wishlist']")
    private WebElement wishListLink;

    @FindBy(xpath = "//button[@data-ref='badge-button']")
    private WebElement cartLink;
    
    @FindBy(xpath = "//div[@data-ref='badge-count']")
    private WebElement miniCartIconCount;

    
    @FindBy(xpath = "//img[@alt='Takealot']")
    private WebElement homeLink;
    /**
     * Constructor to initialize the navigation bar component.
     *
     * @param root The root WebElement of the navigation bar component.
     */
    public NavBar(WebElement root) {
        
    	super(root);
        PageFactory.initElements(root, this);
        initializeNavMap();
    }

    /**
     * Initializes the map with navigation links.
     */
    private void initializeNavMap() {
    	
        navMap = new HashMap<>();
        navMap.put("Register", driver -> registerLink);
        navMap.put("Login", driver -> loginLink);
        navMap.put("Become a Seller", driver -> becomeASellerLink);
        navMap.put("Wishlist", driver -> wishListLink);
        navMap.put("Cart", driver -> cartLink);
        navMap.put("Home", driver -> homeLink);
    }

    /**
     * Clicks on the specified navigation link.
     *
     * @param linkName The name of the link to click.
     */
    public void clickNavLink(String linkName) {
        Function<WebDriver, WebElement> linkCondition = navMap.get(linkName);

        if (linkCondition != null) {
            WebElement link = linkCondition.apply(DriverFactory.getDriver());
            waitUtil.waitForElementToBeClickable(link, 10);
            link.click();
        } else {
            throw new IllegalArgumentException("No such link: " + linkName);
        }
    }
    
    /**
     * Retrieves the current count of products in the cart.
     * 
     * @return The count of products in the cart. Defaults to 0 if there's an error.
     */
    public int getProductsInCartCount() {
        try {
            // Get text from the element
            String textCount = this.miniCartIconCount.getText().trim();
            
            // Parse the text to an integer
            int count =Integer.parseInt(textCount);
            return count;
            
        } catch (NoSuchElementException e) {
            LoggingManager.error("Mini cart icon count element not found", e);
        } catch (NumberFormatException e) {
            LoggingManager.error("Failed to parse the number of products in the cart. Text might not be a valid number", e);
        } catch (Exception e) {
            LoggingManager.error("Unexpected error occurred while getting the cart product count", e);
        }
        
        // Return default value if an error occurs
        return 0;
    }
    
}
