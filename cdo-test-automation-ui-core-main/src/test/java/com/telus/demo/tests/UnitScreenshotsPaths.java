package com.telus.demo.tests;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.test.reporting.ExtentManager;
import com.test.reporting.Reporting;
import com.test.screenshots.Screenshots;
import com.test.utils.SystemProperties;

/**
 * Class to test the paths for Screenshots NOTE: This test are planned to be executed with the REPORTING_AND_SCREENSHOTS_KEEP_HISTORY = false (To use default path)
 */
public class UnitScreenshotsPaths {

    // NOTE: This test are planned to be executed with the REPORTING_AND_SCREENSHOTS_KEEP_HISTORY = false (To use default path)
    private static final String ENDS_WITH = ".png";
    private static final String MY_ID = "MyId";

    private static final String WINDOWS_EXPECTED_REPORT_PATH = ExtentManager.DEFAULT_WINDOWS_REPORT_PARTIAL_PATH + "\\";

    private static final String WINDOWS_EXPECTED_FAILED_PARTIAL_PATH = Screenshots.WINDOWS_FAILED_SS_FOLDER + Screenshots.WINDOWS_VALIDATE_FAIL_MESSAGE;
    private static final String WINDOWS_EXPECTED_PASSED_PARTIAL_PATH = Screenshots.WINDOWS_PASSED_SS_FOLDER + Screenshots.WINDOWS_VALIDATE_PASS_MESSAGE;
    private static final String WINDOWS_EXPECTED_SKIPPED_PARTIAL_PATH = Screenshots.WINDOWS_SKIPPED_SS_FOLDER + Screenshots.WINDOWS_VALIDATE_SKIP_MESSAGE;

    private static final String WINDOWS_EXPECTED_VALIDATE_FAILED = WINDOWS_EXPECTED_REPORT_PATH + WINDOWS_EXPECTED_FAILED_PARTIAL_PATH;
    private static final String WINDOWS_EXPECTED_VALIDATE_FAILED_WITH_ID = WINDOWS_EXPECTED_REPORT_PATH + Screenshots.WINDOWS_FAILED_SS_FOLDER + MY_ID;

    private static final String WINDOWS_EXPECTED_VALIDATE_PASSED = WINDOWS_EXPECTED_REPORT_PATH + WINDOWS_EXPECTED_PASSED_PARTIAL_PATH;
    private static final String WINDOWS_EXPECTED_VALIDATE_PASSED_WITH_ID = WINDOWS_EXPECTED_REPORT_PATH + Screenshots.WINDOWS_PASSED_SS_FOLDER + MY_ID;

    private static final String WINDOWS_EXPECTED_VALIDATE_SKIPPED = WINDOWS_EXPECTED_REPORT_PATH + WINDOWS_EXPECTED_SKIPPED_PARTIAL_PATH;
    private static final String WINDOWS_EXPECTED_VALIDATE_SKIPPED_WITH_ID = WINDOWS_EXPECTED_REPORT_PATH + Screenshots.WINDOWS_SKIPPED_SS_FOLDER + MY_ID;

    private static final String WINDOWS_EXPECTED_RELATIVE_FAILED_PATH = ".\\FailedTestsScreenshots\\Validate Fail_"; // "..\\..\\TestReport\\FailedTestsScreenshots\\Validate
                                                                                                                     // Fail_";

    @AfterMethod(alwaysRun = true)
    public void afterTest() {
        // NA
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeTest() {
     //   ExtentManager.setDatesForDebugUnitTest(); // METHOD TO SET THE FORMATTERS.. ONLY FOR UNIT TEST INTENDED
    }

    @Test(groups = { "Deploy" })
    public void testGetDataFullFailedFolder() {
    	
    	  Reporting.logReporter(Status.DEBUG,"RUNNING! - UnitScreenshotsPaths.testGetDataFullFailedFolder");
     
        String main = Screenshots.getDataFullFailedFolder();
        Reporting.logReporter(Status.DEBUG,"main: " + main);
        Reporting.logReporter(Status.DEBUG,"expected: " + WINDOWS_EXPECTED_VALIDATE_FAILED);
        Boolean startWithExpected = main.startsWith(WINDOWS_EXPECTED_VALIDATE_FAILED);
        if((!SystemProperties.REPORTING_AND_SCREENSHOTS_KEEP_HISTORY) && (!SystemProperties.REPORTING_SCREENSHOT_NEW_NAME_FORMAT)) {
        	Assert.assertTrue(startWithExpected);
        }
        Boolean endWithExpected = main.endsWith(ENDS_WITH);
        Assert.assertTrue(endWithExpected);
    }

    @Test(groups = { "Deploy" })
    public void testGetDataFullFailedFolderWithTestId() {
       Reporting.logReporter(Status.DEBUG,"RUNNING! - UnitScreenshotsPaths.testGetDataFullFailedFolderWithTestId");
        String main = Screenshots.getDataFullFailedFolderWithTestId(MY_ID);
        Reporting.logReporter(Status.DEBUG,"main: " + main);
        
        Reporting.logReporter(Status.DEBUG,"expected: " + WINDOWS_EXPECTED_VALIDATE_FAILED_WITH_ID);
        Boolean startWithExpected = main.startsWith(WINDOWS_EXPECTED_VALIDATE_FAILED_WITH_ID);
        if(!SystemProperties.REPORTING_AND_SCREENSHOTS_KEEP_HISTORY) {
        	Assert.assertTrue(startWithExpected);
        }
        Boolean endWithExpected = main.endsWith(ENDS_WITH);
        Assert.assertTrue(endWithExpected);
    }

    @Test(groups = { "Deploy" })
    public void testGetDataFullPassedFolder() {
        Reporting.logReporter(Status.DEBUG,"RUNNING! - UnitScreenshotsPaths.testGetDataFullPassedFolder");
        String main = Screenshots.getDataFullPassedFolder();
         Reporting.logReporter(Status.DEBUG,"main: " + main);
        String expected = WINDOWS_EXPECTED_VALIDATE_PASSED;
      
        Reporting.logReporter(Status.DEBUG,"expected: " + expected);
        
        
        Boolean startWithExpected = main.startsWith(expected);
        if((!SystemProperties.REPORTING_AND_SCREENSHOTS_KEEP_HISTORY) && (!SystemProperties.REPORTING_SCREENSHOT_NEW_NAME_FORMAT)) {
        	Assert.assertTrue(startWithExpected);
        }
        Boolean endWithExpected = main.endsWith(ENDS_WITH);
        Assert.assertTrue(endWithExpected);
    }

    @Test(groups = { "Deploy" })
    public void testGetDataFullPassedFolderWithTestId() {
        Reporting.logReporter(Status.DEBUG,"RUNNING! - UnitScreenshotsPaths.testGetDataFullPassedFolderWithTestId");
        String main = Screenshots.getDataFullPassedFolderWithTestId(MY_ID);
        Reporting.logReporter(Status.DEBUG,"main: " + main);
        
        String expected = WINDOWS_EXPECTED_VALIDATE_PASSED_WITH_ID;
        Reporting.logReporter(Status.DEBUG,"expected: " + expected);
        Boolean startWithExpected = main.startsWith(expected);
        if(!SystemProperties.REPORTING_AND_SCREENSHOTS_KEEP_HISTORY) {
        	Assert.assertTrue(startWithExpected);
        }
        Boolean endWithExpected = main.endsWith(ENDS_WITH);
        Assert.assertTrue(endWithExpected);
    }

    @Test(groups = { "Deploy" })
    public void testGetDataFullSkippedFolder() {
       Reporting.logReporter(Status.DEBUG,"RUNNING! - UnitScreenshotsPaths.testGetDataFullSkippedFolder");
        String main = Screenshots.getDataFullSkippedFolder();
        Reporting.logReporter(Status.DEBUG,"main: " + main);
        
        String expected = WINDOWS_EXPECTED_VALIDATE_SKIPPED;
       Reporting.logReporter(Status.DEBUG,"expected: " + expected);
        Boolean startWithExpected = main.startsWith(expected);
        if(!SystemProperties.REPORTING_AND_SCREENSHOTS_KEEP_HISTORY) {
        	Assert.assertTrue(startWithExpected);
        }
        Boolean endWithExpected = main.endsWith(ENDS_WITH);
        Assert.assertTrue(endWithExpected);
    }

    @Test(groups = { "Deploy" })
    public void testGetDataFullSkippedFolderWithTestId() {
        Reporting.logReporter(Status.DEBUG,"RUNNING! - UnitScreenshotsPaths.testGetDataFullSkippedFolderWithTestId");
        String main = Screenshots.getDataFullSkippedFolderWithTestId(MY_ID);
        Reporting.logReporter(Status.DEBUG,"main: " + main);
        
        String expected = WINDOWS_EXPECTED_VALIDATE_SKIPPED_WITH_ID;
        Reporting.logReporter(Status.DEBUG,"expected: " + expected);
        Boolean startWithExpected = main.startsWith(expected);
        if(!SystemProperties.REPORTING_AND_SCREENSHOTS_KEEP_HISTORY) {
        	Assert.assertTrue(startWithExpected);
        }
        Boolean endWithExpected = main.endsWith(ENDS_WITH);
        Assert.assertTrue(endWithExpected);
    }

    @Test(groups = { "Deploy" })
    public void testGetDataProjectPath() {
        Reporting.logReporter(Status.DEBUG,"RUNNING! - UnitScreenshotsPaths.testGetDataProjectPath");
        String main = Screenshots.getDataProjectPath();
       Reporting.logReporter(Status.DEBUG,"main: " + main);
        String expected = System.getProperty("user.dir");
        Reporting.logReporter(Status.DEBUG,"expected: " + expected);
        Assert.assertEquals(main, expected);
    }

    @Test(groups = { "Deploy" })
    public void testGetFailedScreenShotFullPath() {
        Reporting.logReporter(Status.DEBUG,"RUNNING! - UnitScreenshotsPaths.testGetFailedScreenShotFullPath");
        String main = Screenshots.getFailedScreenShotFullPath();
        Reporting.logReporter(Status.DEBUG,"main: " + main);
        
        String expected = Screenshots.getDataProjectPath() + WINDOWS_EXPECTED_VALIDATE_FAILED;
        Reporting.logReporter(Status.DEBUG,"expected: " + expected);
        Boolean startWithExpected = main.startsWith(expected);
        if((!SystemProperties.REPORTING_AND_SCREENSHOTS_KEEP_HISTORY) && (!SystemProperties.REPORTING_SCREENSHOT_NEW_NAME_FORMAT)) {
        	Assert.assertTrue(startWithExpected);
        }
        Boolean endWithExpected = main.endsWith(ENDS_WITH);
        Assert.assertTrue(endWithExpected);
    }

    @Test(groups = { "Deploy" })
    public void testGetFailedScreenShotFullPathWithTestId() {
        Reporting.logReporter(Status.DEBUG,"RUNNING! - UnitScreenshotsPaths.testGetFailedScreenShotFullPathWithTestId");
        String main = Screenshots.getFailedScreenShotFullPathWithTestId(MY_ID);
        Reporting.logReporter(Status.DEBUG,"main: " + main);
        
        String expected = Screenshots.getDataProjectPath() + WINDOWS_EXPECTED_VALIDATE_FAILED_WITH_ID;
         Reporting.logReporter(Status.DEBUG,"expected: " + expected);
        Boolean startWithExpected = main.startsWith(expected);
        if(!SystemProperties.REPORTING_AND_SCREENSHOTS_KEEP_HISTORY) {
        	Assert.assertTrue(startWithExpected);
        }
        Boolean endWithExpected = main.endsWith(ENDS_WITH);
        Assert.assertTrue(endWithExpected);
    }

    @Test(groups = { "Deploy" })
    public void testGetPassedScreenShotFullPath() {
       Reporting.logReporter(Status.DEBUG,"RUNNING! - UnitScreenshotsPaths.testGetPassedScreenShotFullPath");
        String main = Screenshots.getPassedScreenShotFullPath();
       Reporting.logReporter(Status.DEBUG,"main: " + main);
        
        String expected = Screenshots.getDataProjectPath() + WINDOWS_EXPECTED_VALIDATE_PASSED;
        Reporting.logReporter(Status.DEBUG,"expected: " + expected);
        Boolean startWithExpected = main.startsWith(expected);
        if((!SystemProperties.REPORTING_AND_SCREENSHOTS_KEEP_HISTORY) && (!SystemProperties.REPORTING_SCREENSHOT_NEW_NAME_FORMAT)) {
        	Assert.assertTrue(startWithExpected);
        }
        Boolean endWithExpected = main.endsWith(ENDS_WITH);
        Assert.assertTrue(endWithExpected);
    }

    @Test(groups = { "Deploy" })
    public void testGetPassedScreenShotFullPathWithTestId() {
       Reporting.logReporter(Status.DEBUG,"RUNNING! - UnitScreenshotsPaths.testGetPassedScreenShotFullPathWithTestId");
        String main = Screenshots.getPassedScreenShotFullPathWithTestId(MY_ID);
       Reporting.logReporter(Status.DEBUG,"main: " + main);
        
        String expected = Screenshots.getDataProjectPath() + WINDOWS_EXPECTED_VALIDATE_PASSED_WITH_ID;
        Reporting.logReporter(Status.DEBUG,"expected: " + expected);
        Boolean startWithExpected = main.startsWith(expected);
        if(!SystemProperties.REPORTING_AND_SCREENSHOTS_KEEP_HISTORY) {
        	Assert.assertTrue(startWithExpected);
        }
        Boolean endWithExpected = main.endsWith(ENDS_WITH);
        Assert.assertTrue(endWithExpected);
    }

    @Test(groups = { "Deploy" })
    public void testGetScreenShotRelativePath() {
       Reporting.logReporter(Status.DEBUG,"RUNNING! - UnitScreenshotsPaths.testGetScreenShotRelativePath");
        String main = Screenshots.getScreenShotRelativePath(Screenshots.getFailedScreenShotFullPath());
        Reporting.logReporter(Status.DEBUG,"main: " + main);
        
        String expected = WINDOWS_EXPECTED_RELATIVE_FAILED_PATH;
        Reporting.logReporter(Status.DEBUG,"expected: " + expected);
        Boolean startWithExpected = main.startsWith(expected);
        if((!SystemProperties.REPORTING_AND_SCREENSHOTS_KEEP_HISTORY) && (!SystemProperties.REPORTING_SCREENSHOT_NEW_NAME_FORMAT)) {
        Assert.assertTrue(startWithExpected);
        }
        Boolean endWithExpected = main.endsWith(ENDS_WITH);
        Assert.assertTrue(endWithExpected);
    }

    @Test(groups = { "Deploy" })
    public void testGetSkippedScreenShotFullPath() {
        Reporting.logReporter(Status.DEBUG,"RUNNING! - UnitScreenshotsPaths.testGetSkippedScreenShotFullPath");
        String main = Screenshots.getSkippedScreenShotFullPath();
        Reporting.logReporter(Status.DEBUG,"main: " + main);
        
        String expected = Screenshots.getDataProjectPath() + WINDOWS_EXPECTED_VALIDATE_SKIPPED;
       Reporting.logReporter(Status.DEBUG,"expected: " + expected);
        Boolean startWithExpected = main.startsWith(expected);
        if(!SystemProperties.REPORTING_AND_SCREENSHOTS_KEEP_HISTORY) {
        	Assert.assertTrue(startWithExpected);
        }
        Boolean endWithExpected = main.endsWith(ENDS_WITH);
        Assert.assertTrue(endWithExpected);
    }

    @Test(groups = { "Deploy" })
    public void testGetSkippedScreenShotFullPathWithTestId() {
        Reporting.logReporter(Status.DEBUG,"RUNNING! - UnitScreenshotsPaths.testGetSkippedScreenShotFullPathWithTestId");
        String main = Screenshots.getSkippedScreenShotFullPathWithTestId(MY_ID);
       Reporting.logReporter(Status.DEBUG,"main: " + main);
        
        String expected = Screenshots.getDataProjectPath() + WINDOWS_EXPECTED_VALIDATE_SKIPPED_WITH_ID;
        Reporting.logReporter(Status.DEBUG,"expected: " + expected);
        Boolean startWithExpected = main.startsWith(expected);
        if(!SystemProperties.REPORTING_AND_SCREENSHOTS_KEEP_HISTORY) {
        	Assert.assertTrue(startWithExpected);
        }
        Boolean endWithExpected = main.endsWith(ENDS_WITH);
        Assert.assertTrue(endWithExpected);
    }

}
