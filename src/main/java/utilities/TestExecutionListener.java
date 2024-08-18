package utilities;

import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.SkipException;

import logging.LoggingManager;


public class TestExecutionListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String executionRequired = DataProviderUtil.getExecutionStatus(testName);
        LoggingManager.info("Gettiing execution status");
        if ("No".equalsIgnoreCase(executionRequired)) {
        	LoggingManager.info("Skipping execution for: "+ testName);
            result.setStatus(ITestResult.SKIP);
            result.setThrowable(new SkipException("Skipping test: " + testName));
            
        }
    }
}
