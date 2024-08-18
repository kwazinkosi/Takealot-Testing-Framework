package utilities;

import org.testng.ITestContext;
import org.testng.ITestListener;
import logging.LoggingManager;

/**
 * TestSetup class implements the ITestListener interface to configure logging before tests start.
 * It sets up logging when the test context starts.
 */
public class TestSetup implements ITestListener {

    /**
     * This method is invoked before the start of any test context (suite).
     * It configures the logging system to ensure that all log messages are captured.
     *
     * @param context the test context (suite) that is starting
     */
    @Override
    public void onStart(ITestContext context) {
        // Configure logging
        System.out.println("Setting up logging...");
        LoggingManager.configureLogging();
        System.out.println("Logging configured.");
        LoggingManager.info("Logging configured successfully!!");
    }
}
