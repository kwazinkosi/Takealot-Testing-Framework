package utilities;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import java.time.Duration;
import config.ConfigReader;

public class DriverFactory {

    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static WebDriver initDriver() {
    	
    	String browserName = ConfigReader.getProperty("browser_name"); // e.g., "chrome"
        String mode = ConfigReader.getProperty("browser_mode"); // e.g., "headless"
        WebDriver webDriver = null;
        switch (browserName.toLowerCase()) {
            case "chrome":
                ChromeOptions cOptions = new ChromeOptions();
                if (mode.equals("headless")) {
                    cOptions.addArguments("--headless", "--disable-gpu", "--ignore-certificate-errors");
                }
                webDriver = new ChromeDriver(cOptions);
                break;
            case "firefox":
                FirefoxOptions fOptions = new FirefoxOptions();
                if (mode.equals("headless")) {
                    fOptions.addArguments("--headless", "--disable-gpu", "--ignore-certificate-errors");
                }
                webDriver = new FirefoxDriver(fOptions);
                break;
            case "edge":
                EdgeOptions eOptions = new EdgeOptions();
                if (mode.equals("headless")) {
                    eOptions.addArguments("--headless", "--disable-gpu", "--ignore-certificate-errors");
                }
                webDriver = new EdgeDriver(eOptions);
                break;
            default:
                throw new IllegalArgumentException("Browser type not supported: " + browserName);
        }

        driver.set(webDriver);
        WebDriver currentDriver = getDriver();
        currentDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0,5));
        currentDriver.manage().deleteAllCookies();
        currentDriver.manage().window().maximize();
        return currentDriver;
    }

    // Quit the WebDriver instance and remove it from the thread
    public static void quitDriver() {
        
    	if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }

    public static WebDriver getDriver() {
        return driver.get();
    }
}
