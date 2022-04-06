package com.test.reporting;

import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.test.desktop.actions.DesktopDriverSession;
import com.test.logging.Logging;
import com.test.screenshots.Screenshots;
import com.test.ui.actions.BaseTest;
import com.test.ui.actions.WebDriverSession;
import com.test.utils.SystemProperties;

/**
 * Helper Class to "Listen" the test and find if the test is a retry test or not.
 */
public class Retry implements IRetryAnalyzer {

    private static final String MESSAGE_DEFAULT_ERROR = "DEFAULT ERROR LOGGER - TEST FAILED ";
    private static final String MESSAGE_FAILED = " failed : ";
    private static final String VARIABLE_CONTEXT_TEST_NAME = "testName";
    // Retries Failed tests and takes a screenshot of web driver
    private int count = 0;
	private static final String EXCEPTION_MSG = "Exception Caught : ";

    public static int getMaxTryNumber() {
        String maxTry = SystemProperties.RETRY_TESTNG_COUNTER;

        switch (maxTry) {
            case "0":
                return 0;
            case "1":
                return 1;
            case "2":
                return 2;
            case "3":
                return 3;
            case "4":
                return 4;
            case "5":
                return 5;
            default:
                Reporting.logReporter(Status.WARNING, "Maximun number of retry is 5, you sent[ " + maxTry + " ] retry will not be executed");
                return 0;
        }
    }

    private static String getTestMethodName(ITestResult iTestResult) {
        if (iTestResult.getTestContext().getAttribute(VARIABLE_CONTEXT_TEST_NAME) == null) { // Regular Code (Not Data From Data Provider)
            return iTestResult.getMethod().getConstructorOrMethod().getName();
        } else { // Modification to read the DataProvider Modified name
            return iTestResult.getTestContext().getAttribute(VARIABLE_CONTEXT_TEST_NAME).toString();
        }
    }

    /**
     * Takes screenshot when test has failed and adds log to extent report, in general this is usefull when there is no other logger element so this will at least give us the last
     * error data.
     *
     * @param iTestResult
     */
    public void extendReportsFailOperations(ITestResult iTestResult) {
        String fullFilePath = Screenshots.getFailedScreenShotFullPathWithTestId(iTestResult.getMethod().getConstructorOrMethod().getName());
        String relativeFilePath = Screenshots.getScreenShotRelativePath(fullFilePath);

        Object currentClass = iTestResult.getInstance();

        WebDriver driver = null;
        WebDriver desktopDriver = null;

        boolean multipleDriversActive = false;

        // Get drivers
        if (DesktopDriverSession.getDesktopDriverForCurrentThreat() != null && WebDriverSession.getWebDriverForCurrentThreat() != null) { // Scenario for both drivers active
            multipleDriversActive = true;

            driver = ((BaseTest) currentClass).getDriver();
            desktopDriver = ((BaseTest) currentClass).getDesktopDriver();
        } else if (DesktopDriverSession.getDesktopDriverForCurrentThreat() != null) {// Scenario for single driver active, will validate which one is active and use it as main
                                                                                     // driver
            driver = ((BaseTest) currentClass).getDesktopDriver();
        } else if (WebDriverSession.getWebDriverForCurrentThreat() != null) {
            driver = ((BaseTest) currentClass).getDriver();
        }

        // Take Screenshot (single driver can be any of both versions)
        String driverScreenshot = null;
        driverScreenshot = Screenshots.takeScreenshot(fullFilePath, driver);

        if (driverScreenshot != null) {
            try {
                Reporting.logReporter(Status.WARNING, MESSAGE_DEFAULT_ERROR + iTestResult.getMethod().getConstructorOrMethod().getName() + MESSAGE_FAILED + iTestResult.getThrowable().toString(),
                        MediaEntityBuilder.createScreenCaptureFromPath(relativeFilePath).build());
            } catch (IOException e) {
            	Logging.logReporter(Status.DEBUG, EXCEPTION_MSG + e);

                Reporting.logReporter(Status.WARNING, MESSAGE_DEFAULT_ERROR + iTestResult.getMethod().getConstructorOrMethod().getName() + MESSAGE_FAILED + iTestResult.getThrowable().toString());
            }
        } else {
            Reporting.logReporter(Status.WARNING, MESSAGE_DEFAULT_ERROR + iTestResult.getMethod().getConstructorOrMethod().getName() + MESSAGE_FAILED + iTestResult.getThrowable().toString());
        }

        // Take Screenshot (both drivers)
        if (multipleDriversActive) {

            String desktopScreenshot = null;
            String webScreenshot = null;

            desktopScreenshot = Screenshots.takeScreenshot(fullFilePath, desktopDriver);
            webScreenshot = Screenshots.takeScreenshot(fullFilePath, driver);

            // TAKE WEB SS
            if (webScreenshot != null) {
                try {
                    Reporting.logReporter(Status.WARNING, MESSAGE_DEFAULT_ERROR + iTestResult.getMethod().getConstructorOrMethod().getName() + MESSAGE_FAILED + iTestResult.getThrowable().toString(),
                            MediaEntityBuilder.createScreenCaptureFromPath(relativeFilePath).build());
                } catch (IOException e) {
                	Logging.logReporter(Status.DEBUG, EXCEPTION_MSG + e);

                    Reporting.logReporter(Status.WARNING, MESSAGE_DEFAULT_ERROR + iTestResult.getMethod().getConstructorOrMethod().getName() + MESSAGE_FAILED + iTestResult.getThrowable().toString());
                }
            } else {
                Reporting.logReporter(Status.WARNING, MESSAGE_DEFAULT_ERROR + iTestResult.getMethod().getConstructorOrMethod().getName() + MESSAGE_FAILED + iTestResult.getThrowable().toString());
            }

            // TAKE DESKTOP SS
            if (desktopScreenshot != null) {
                try {
                    Reporting.logReporter(Status.WARNING, MESSAGE_DEFAULT_ERROR + iTestResult.getMethod().getConstructorOrMethod().getName() + MESSAGE_FAILED + iTestResult.getThrowable().toString(),
                            MediaEntityBuilder.createScreenCaptureFromPath(relativeFilePath).build());
                } catch (IOException e) {
                	Logging.logReporter(Status.DEBUG, EXCEPTION_MSG + e);

                    Reporting.logReporter(Status.WARNING, MESSAGE_DEFAULT_ERROR + iTestResult.getMethod().getConstructorOrMethod().getName() + MESSAGE_FAILED + iTestResult.getThrowable().toString());
                }
            } else {
                Reporting.logReporter(Status.WARNING, MESSAGE_DEFAULT_ERROR + iTestResult.getMethod().getConstructorOrMethod().getName() + MESSAGE_FAILED + iTestResult.getThrowable().toString());
            }
        }

    }

    /**
     * Helper method to know if the current test data is coming from the Data Provider
     */
    private boolean isDataProviderTest(ITestResult iTestResult) {
        if (iTestResult.getTestContext().getAttribute(VARIABLE_CONTEXT_TEST_NAME) == null) { // Regular Code (Not Data From Data Provider)
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean retry(ITestResult iTestResult) {
        String testName = getTestMethodName(iTestResult);
        Logging.logReporter(Status.DEBUG, "Retry for TestCase : "+ testName );
        if (!iTestResult.isSuccess()) {
            int maxTry = getMaxTryNumber();

            if (this.count < maxTry) { // Check if maxtry count is reached
                this.count++; // Increase the maxTry count by 1

                iTestResult.setStatus(ITestResult.FAILURE); // Mark test as failed
                this.extendReportsFailOperations(iTestResult); // ExtentReports fail operations

                Reporting.logReporter(Status.DEBUG, "Failed Test detected - Retry Set for Next Iteration");
                return true; // Tells TestNG to re-run the test
            }

            else if (this.count == maxTry && this.isDataProviderTest(iTestResult)) { // Each Data Provider Set will reset the value once the max is reach to allow the next
                                                                                     // iteration to execute again the retry
                this.count = 0;
            }

        } else {
            iTestResult.setStatus(ITestResult.SUCCESS); // If test passes,TestNG marks it as passed
        }
        return false;
    }

}
