package com.telus.demo.tests;

import org.testng.Assert;
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
 * Class designed to validate the Functionality of the WebDriverSteps basic methods
 */
public class UnitWebDriverTest extends BaseTest {

    @AfterMethod(alwaysRun = true)
    public void afterTest() {
        
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeTest() {
        // Load page
        WebDriverSteps.openApplication("GOOGLE");
        BaseSteps.Waits.waitUntilPageLoadComplete();
    }

    @Test(groups = { "Deploy" })
    public void testCloseApplication() {
    	
    	 Reporting.logReporter(Status.DEBUG,"RUNNING! - UnitWebDriverTest.testCloseApplication");
         
        Assert.assertTrue(WebDriverSteps.closeTheBrowser(), "Browser session not closed as expected");
    }

    @Test(groups = { "Deploy" })
    public void testOpenApplication() {
        Reporting.logReporter(Status.DEBUG,"RUNNING! - UnitWebDriverTest.testOpenApplication");
        String currentUrl = WebDriverSteps.getCurrentUrl();
        String expectedPartialUrl = "www.google.com";

        Validate.isInString(currentUrl, expectedPartialUrl);
        WebDriverSteps.closeTheBrowser();
    }

}
