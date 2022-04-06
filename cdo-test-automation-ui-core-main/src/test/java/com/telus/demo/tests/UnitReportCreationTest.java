package com.telus.demo.tests;

import java.io.File;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.test.reporting.Reporting;
import com.test.ui.actions.BaseSteps;
import com.test.ui.actions.BaseTest;
import com.test.ui.actions.Validate;
import com.test.ui.actions.WebDriverSteps;

/**
 * Class designed to validate the EXTENT REPORT creation under Positive/Negative Results. Note that it uses the DEFAULT path and not the Historic One. Restriction: This framework
 * creates the report after the full execution so we cannot test really this output. will need to be refactored to run after the regular job execution as slave process
 */
public class UnitReportCreationTest extends BaseTest {

    private static final String DEFAULT_MAC_PATH = System.getProperty("user.dir") + "/target/extent-reports";
    private static final String DEFAULT_WINDOWS_PATH = System.getProperty("user.dir") + "\\target\\extent-reports";
    private static final String REPORT_NAME = "ExtentReports-Version3-Test-Automation-Report.html";

    @AfterMethod(alwaysRun = true)
    public void afterTest() {
        this.checkIfReportWasCreated();
        WebDriverSteps.closeTheBrowser();
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeTest() {
        // No need to have any step here
        // Load page
        WebDriverSteps.openApplication("GOOGLE");
        BaseSteps.Waits.waitUntilPageLoadComplete();

    }

    private void checkIfReportWasCreated() {
        BaseSteps.Waits.waitGeneric(5000);

        String fullPath = DEFAULT_WINDOWS_PATH + "\\" + REPORT_NAME;

        File tempFile = new File(fullPath);
        boolean exists = tempFile.exists();

        Validate.assertTrue(exists, "ERROR: The report does not exist in the planned path(" + fullPath + ")");
     //   System.out.println("Report exists? " + exists);
        Reporting.logReporter(Status.DEBUG,"Report exists? " + exists);
    }

    @Test(groups = { "Deploy", "Defect" })
    public void testFailedScenario() {
       // System.out.println("RUNNING! - UnitReportCreationTest.testFailedScenario");
        Reporting.logReporter(Status.DEBUG,"RUNNING! - UnitReportCreationTest.testFailedScenario");
        Validate.assertFalse(false);
        // The actual call is inside the After method
    }

    @Test(groups = { "Deploy", "Defect" })
    public void testPositiveScenario() {
     //   System.out.println("RUNNING! - UnitReportCreationTest.testPositiveScenario");
        Reporting.logReporter(Status.DEBUG,"RUNNING! - UnitReportCreationTest.testPositiveScenario");
        Validate.assertTrue(true);
        // The actual call is inside the After method
    }
}
