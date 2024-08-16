package pages;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import components.NavBar;
import components.Product;
import logging.LoggingManager;
import utilities.DataProviderUtil;
import utilities.DriverFactory;
import wait.WaitUtil;

public class HomePage extends BasePage {

    @FindBy(xpath = "//input[@name='search']")
    private WebElement searchBox;

    @FindBy(xpath = "//button[@data-ref='search-submit-button']")
    private WebElement searchButton;

    @FindBy(xpath = "//button[@class='ab-message-button' and text()='NOT NOW']")
    private WebElement adPopupCloseButton;

    @FindBy(className = "listings-container-module_listings-container_AC4LI")
    private WebElement searchResults;

    @FindBy(className = "ab-page-blocker")
    private WebElement pageBlocker;
    
    @FindBy(css = "button.cookies-banner-module_dismiss-button_24Z98")
    private WebElement cookieBlockerBtn;
    
    @FindBy(className = "no-results-title")
    private WebElement emptyResults;
    
    

    private boolean loggedIn = false;
    


	/**
     * Constructor for the HomePage class.
     * 
     * @param driver The WebDriver instance used to interact with the web page.
     */
    public HomePage(WebDriver driver) {
        super(driver);
    }
    
    public boolean isLoggedIn() {
		return loggedIn;
	}


	public HomePage setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
		return this;
	}
    
    /**
     * Enters a query into the search box.
     * 
     * @param query The search term to be entered.
     * @return The current HomePage instance for method chaining.
     */
    public HomePage enterSearchQuery(String query) {
        if (isVisible(searchBox)) {
            sendKeys(searchBox, query);
        } else {
            sendKeys(searchBox, query);
        }
        return this;
    }
    
    /**
     * Submits the search query.
     * 
     * @return A new instance of the ProductsPage.
     */
    public ProductsPage submitSearch() {
    	
        click(searchButton);
        LoggingManager.info("Search submitted");
        return new ProductsPage(driver);
    }

    /**
     * Performs a valid search for a specified query.
     * 
     * @param query The search term to be entered.
     * @return A new instance of the ProductsPage.
     */
    public ProductsPage searchValidFor(String query) {
    	
    	LoggingManager.info("Searching for "+query);
    	enterSearchQuery(query);
        ProductsPage productsPage = submitSearch(); // Assign the returned ProductsPage
        waitUtil.waitForElementToBeVisible(searchResults, normalWaitTime); // Wait for search results to be visible
        LoggingManager.info("Products now visible");
        return productsPage; // Return the ProductsPage instance
    }
    
    
    /**
     * Searches for an invalid input and checks the expected response.
     * 
     * @param query The search term to be entered.
     * @param expectedResponse The expected response type ("default-page" or "empty-results-page").
     * @return The current HomePage instance for method chaining.
     */
    public boolean searchForInvalidInput(String query, String expectedResponse) {
        
    	enterSearchQuery(query);
        submitSearch(); // Call submitSearch and discard the result as it’s not used in this case
        
        // Wait and validate based on the expected response
        switch (expectedResponse) {
            case "default-page":
            	ProductsPage products = new ProductsPage(DriverFactory.getDriver());
            	List<Product> productList = products.getFilteredProductsMax(p -> !p.getProductType().equals("sponsored"), normalWaitTime);
                // if none of the products in the list contain the query
                boolean noneContainQuery = productList.stream().noneMatch(p -> p.getName().toLowerCase().contains(query.toLowerCase()));

                if (noneContainQuery || query.isBlank()) {
                    LoggingManager.info("Default results page displayed");
                    return true;
                }
                LoggingManager.warn("Default results page not displayed");
                
                break;
            case "empty-results-page":
                if (waitUtil.waitForElementToBeVisible(emptyResults, normalWaitTime) != null) {
                    LoggingManager.info("Empty results page displayed");
                    return true;
                } else {
                    LoggingManager.warn("Empty results page not displayed");
                }
                break;
            default:
                LoggingManager.warn("Unexpected response type: " + expectedResponse);
        }
        
        return false; // Return the current HomePage instance for method chaining
    }

    /**
     * Navigates to the Register page.
     * 
     * @return RegistrationPage instance.
     */
    public RegistrationPage navigateToRegister() {
    	
    	NavBar navBar = getNavBar();
        navBar.clickNavLink("Register");
        return new RegistrationPage(driver);
    }

    /**
     * Navigates to the Login page.
     * 
     * @return LoginPage instance.
     */
    public LoginPage navigateToLogin() {
    	
    	NavBar navBar = getNavBar();
    	navBar.clickNavLink("Login");
        return new LoginPage(driver);
    }

    /**
     * Navigates to the Cart page.
     * 
     * @return CartPage instance.
     */
    public CartPage navigateToCart() {
    	
    	NavBar navBar = getNavBar();
    	navBar.clickNavLink("Cart");
        return new CartPage(driver);
    }

    /**
     * Navigates to the Become a Seller page.
     */
    public void navigateToSeller() {
    	
    	NavBar navBar = getNavBar();
    	navBar.clickNavLink("Become a Seller");
    }
    
    /**
     * Retrieves the title of the home page.
     * 
     * @return The title of the home page.
     */
    public String getHomeTitle() {
        return driver.getTitle();
    }

	@Override
	public boolean isVisible() {
		// TODO Auto-generated method stub
		return false;
	}
}
