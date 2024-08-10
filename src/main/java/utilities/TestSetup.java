package utilities;

import org.testng.ITestContext;
import org.testng.ITestListener;

import logging.LoggingManager;

public class TestSetup implements ITestListener {

    @Override
    public void onStart(ITestContext context) {
        // Configure logging
        System.out.println("Setting up logging...");
        LoggingManager.configureLogging();
        System.out.println("Logging configured.");
        LoggingManager.info("Logging configured succesfully!!");
    }

}
