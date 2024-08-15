package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.openqa.selenium.support.events.WebDriverListener;
import org.testng.annotations.BeforeClass;

import config.ConfigReader;
import pages.BasePage;
import pages.HomePage;
import pages.ProductsPage;
import utilities.AdOverlayListener;
import utilities.DataProviderUtil;
import utilities.DriverFactory;

public class RegistrationPageTest {

	
	private WebDriver driver;
    private ProductsPage productsPage;
    private HomePage homePage;
    private DataProviderUtil dataProviderUtil = new DataProviderUtil();

    @BeforeClass
    public void setUp() {
        driver = DriverFactory.initDriver();

        // Apply the WebDriverListener
        WebDriverListener listener = new AdOverlayListener(driver);
        driver = new EventFiringDecorator<>(listener).decorate(driver);
        driver.get(ConfigReader.getProperty("base_url"));

        String searchProduct = dataProviderUtil.getValue("common info", "search_product");
        // Navigate to a particular products page
        homePage = new HomePage(driver);
        productsPage = homePage.searchValidFor(searchProduct);
        productsPage.getWait().waitImplicitly(0,5);
        BasePage.reporter.setDriver(driver);  // Inject WebDriver into ReportManager
    }
}
