package pages;

import java.util.List;
import java.util.function.Predicate;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import components.Product;

public class ProductsPage extends BasePage {
    private WebDriver driver;

    @FindBy(css = ".search-product.grid div[data-ref='product-card']")
    private List<WebElement> productElements;
    
    public ProductsPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
    }

    /**
     * Returns a list of Product components representing each product on the page.
     *
     * @return List of Product objects.
     */
    public List<Product> getProducts() {
        return productElements
        	.stream()
        	.map(el -> new Product(el)) // Map WebElement to a product component
            .toList();
    }
    /**
     * Return a specific product based on a boolean-valued function (predicate).
     * This method uses the behavioral Strategy Pattern to filter products.
     * 
     * @param condition A Predicate that defines the condition for selecting the product.
     *                  For example, you can use it to filter by product name or price.
     * @return The first product that matches the condition.
     * @throws NoSuchElementException if no product matches the condition.
     */
    public Product getProduct(Predicate<Product> condition) {
        if (condition == null) {
            throw new IllegalArgumentException("Condition must not be null.");
        }
        //IODO: might want to click on 'Show All Options" first.

        return getProducts()
            .stream()
            .filter(condition)
            .findFirst()
            .orElseThrow();
    }

    
}