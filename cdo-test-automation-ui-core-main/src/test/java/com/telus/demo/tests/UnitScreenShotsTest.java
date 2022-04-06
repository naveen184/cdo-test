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
 * Class designed to validate the ZIP Folder creation under Any Results. Note that it uses the DEFAULT path and not the Historic One.
 */
public class UnitScreenShotsTest extends BaseTest {

    @AfterMethod(alwaysRun = true)
    public void afterTest() {
        WebDriverSteps.closeTheBrowser();
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeTest() {
        // Load page
        WebDriverSteps.openApplication("GOOGLE");
        BaseSteps.Waits.waitUntilPageLoadComplete();
        BaseSteps.Waits.waitGeneric(5000);
    }

    private void checkIfScreenshotWasCreated(String path) {
        File tempFile = new File(path);
        boolean exists = tempFile.exists();

        Validate.assertTrue(exists, "ERROR: The SS File does not exist in the planned path (" + path + ")");
        Reporting.logReporter(Status.DEBUG,"SS exists? " + exists + " Path: " + path);
    }

    @Test(groups = { "Deploy" })
    public void testCustomScreenShotsCreation() {
        Reporting.logReporter(Status.DEBUG,"RUNNING! - UnitScreenShotsTest.testCustomScreenShotsCreation");
        String path = Validate.takeStepScreenShot("CustomSS");
        this.checkIfScreenshotWasCreated(path);
    }

    @Test(groups = { "Deploy" })
    public void testFailedScreenShotsCreation() {
        Reporting.logReporter(Status.DEBUG,"RUNNING! - UnitScreenShotsTest.testFailedScreenShotsCreation");
        
        String path = Validate.sendFail("sendFail");
        this.checkIfScreenshotWasCreated(path);
    }

    @Test(groups = { "Deploy" })
    public void testPassedScreenShotsCreation() {
        Reporting.logReporter(Status.DEBUG,"RUNNING! - UnitScreenShotsTest.testPassedScreenShotsCreation");
        
        String path = Validate.sendPass("sendPass");
        this.checkIfScreenshotWasCreated(path);
    }
}
