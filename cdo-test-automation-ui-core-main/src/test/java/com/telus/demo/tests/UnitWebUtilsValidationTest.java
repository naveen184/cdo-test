package com.telus.demo.tests;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.telus.demo.steps.DemoSteps;
import com.test.reporting.Reporting;
import com.test.ui.actions.BaseSteps;
import com.test.ui.actions.BaseTest;
import com.test.ui.actions.Validate;
import com.test.ui.actions.WebDriverSteps;

/**
 * Class designed to validate the Functionality of the Validation class utils/methods, see the class comments for the 3 types of methods and the output expected
 */
public class UnitWebUtilsValidationTest extends BaseTest {

    @AfterMethod(alwaysRun = true)
    public void afterTest() {
        WebDriverSteps.closeTheBrowser();
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeTest() {
        // Load page
        WebDriverSteps.openApplication("GOOGLE");
        BaseSteps.Waits.waitUntilPageLoadComplete();
    }

    @Test(groups = { "Deploy" })
    public void testIsDelayedElementDisplayed() {
       Reporting.logReporter(Status.DEBUG,"RUNNING! - UnitWebUtilsValidationTest.testIsDelayedElementDisplayed");
        boolean firstTestStatus = false;
        boolean secondTestStatus = false;

        // POSITIVE
        firstTestStatus = Validate.isDelayedElementDisplayed(DemoSteps.getExistingWebElement());

        // NEGATIVE FOR ELEMENT THAT DOES NOT EXIST
        secondTestStatus = Validate.isDelayedElementDisplayed(DemoSteps.getNotExistingWebElement());

        // ASSERTIONS
        Assert.assertTrue(firstTestStatus);
        Assert.assertFalse(secondTestStatus);

        // NOTE THIS TEST WILL TAKE THE FAIL SCREENSHOT, BUT THE TEST WILL PASS
    }

    @Test(groups = { "Deploy" })
    public void testIsDelayedElementDisplayedWithoutAssertions() {
       Reporting.logReporter(Status.DEBUG,"RUNNING! - UnitWebUtilsValidationTest.testIsDelayedElementDisplayedWithoutAssertions");
        
        boolean firstTestStatus = false;
        boolean secondTestStatus = false;

        // POSITIVE
        firstTestStatus = Validate.isDelayedElementDisplayedWithoutAssertions(DemoSteps.getExistingWebElement());

        // NEGATIVE FOR ELEMENT THAT DOES NOT EXIST
        secondTestStatus = Validate.isDelayedElementDisplayedWithoutAssertions(DemoSteps.getNotExistingWebElement());

        // ASSERTIONS
        Assert.assertTrue(firstTestStatus);
        Assert.assertFalse(secondTestStatus);

    }

    @Test(groups = { "Deploy" })
    public void testIsElementDisplayed() {
        Reporting.logReporter(Status.DEBUG,"RUNNING! - UnitWebUtilsValidationTest.testIsElementDisplayed");
        
        boolean firstTestStatus = false;
        boolean secondTestStatus = false;

        // POSITIVE
        firstTestStatus = Validate.isElementDisplayed(DemoSteps.getExistingWebElement());

        // NEGATIVE FOR ELEMENT THAT DOES NOT EXIST
        secondTestStatus = Validate.isElementDisplayed(DemoSteps.getNotExistingWebElement());

        // ASSERTIONS
        Assert.assertTrue(firstTestStatus);
        Assert.assertFalse(secondTestStatus);

        // NOTE THIS TEST WILL TAKE THE FAIL SCREENSHOT, BUT THE TEST WILL PASS
    }

    @Test(groups = { "Deploy" })
    public void testIsElementDisplayedWithoutAssertions() {
        Reporting.logReporter(Status.DEBUG,"RUNNING! - UnitWebUtilsValidationTest.testIsElementDisplayedWithoutAssertions");
        
        boolean firstTestStatus = false;
        boolean secondTestStatus = false;

        // POSITIVE
        firstTestStatus = Validate.isElementDisplayedWithoutAssertions(DemoSteps.getExistingWebElement());

        // NEGATIVE FOR ELEMENT THAT DOES NOT EXIST
        secondTestStatus = Validate.isElementDisplayedWithoutAssertions(DemoSteps.getNotExistingWebElement());

        // ASSERTIONS
        Assert.assertTrue(firstTestStatus);
        Assert.assertFalse(secondTestStatus);

    }

    @Test(groups = { "Deploy" })
    public void testIsElementNotDisplayed() {
       Reporting.logReporter(Status.DEBUG,"RUNNING! - UnitWebUtilsValidationTest.testIsElementNotDisplayed");
        
        boolean firstTestStatus = false;
        boolean secondTestStatus = false;

        // POSITIVE
        firstTestStatus = Validate.isElementNotDisplayed(DemoSteps.getNotExistingWebElement());

        // NEGATIVE FOR ELEMENT THAT DOES NOT EXIST
        secondTestStatus = Validate.isElementNotDisplayed(DemoSteps.getExistingWebElement());

        // ASSERTIONS
        Assert.assertTrue(firstTestStatus);
        Assert.assertFalse(secondTestStatus);

        // NOTE THIS TEST WILL TAKE THE FAIL SCREENSHOT, BUT THE TEST WILL PASS
    }

    @Test(groups = { "Deploy" })
    public void testIsElementNotDisplayedWithoutAssertions() {
        Reporting.logReporter(Status.DEBUG,"RUNNING! - UnitWebUtilsValidationTest.testIsElementNotDisplayedWithoutAssertions");
        
        boolean firstTestStatus = false;
        boolean secondTestStatus = false;

        // POSITIVE
        firstTestStatus = Validate.isElementNotDisplayedWhitoutAssertions(DemoSteps.getNotExistingWebElement());

        // NEGATIVE FOR ELEMENT THAT DOES NOT EXIST
        secondTestStatus = Validate.isElementNotDisplayedWhitoutAssertions(DemoSteps.getExistingWebElement());

        // ASSERTIONS
        Assert.assertTrue(firstTestStatus);
        Assert.assertFalse(secondTestStatus);
    }

    @Test(groups = { "Deploy" })
    public void testIsElementPresent() {
        Reporting.logReporter(Status.DEBUG,"RUNNING! - UnitWebUtilsValidationTest.testIsElementPresent");
        
        boolean firstTestStatus = false;
        boolean secondTestStatus = false;

        // POSITIVE
        firstTestStatus = Validate.isElementPresent(DemoSteps.getExistingWebElement());

        // NEGATIVE FOR ELEMENT THAT DOES NOT EXIST
        secondTestStatus = Validate.isElementPresent(DemoSteps.getNotExistingWebElement());

        // ASSERTIONS
        Assert.assertTrue(firstTestStatus);
        Assert.assertFalse(secondTestStatus);

        // NOTE THIS TEST WILL TAKE THE FAIL SCREENSHOT, BUT THE TEST WILL PASS
    }

    @Test(groups = { "Deploy" })
    public void testIsElementPresentWithoutAssertions() {
       Reporting.logReporter(Status.DEBUG,"RUNNING! - UnitWebUtilsValidationTest.testIsElementPresentWithoutAssertions");
        
        boolean firstTestStatus = false;
        boolean secondTestStatus = false;

        // POSITIVE
        firstTestStatus = Validate.isElementPresentWithoutAssertions(DemoSteps.getExistingWebElement());

        // NEGATIVE FOR ELEMENT THAT DOES NOT EXIST
        secondTestStatus = Validate.isElementPresentWithoutAssertions(DemoSteps.getNotExistingWebElement());

        // ASSERTIONS
        Assert.assertTrue(firstTestStatus);
        Assert.assertFalse(secondTestStatus);
    }

    @Test(groups = { "Deploy" })
    public void testIsInString() {
       Reporting.logReporter(Status.DEBUG,"RUNNING! - UnitWebUtilsValidationTest.testIsInString");
        
        String currentUrl = WebDriverSteps.getCurrentUrl();
        String expectedPartialUrl = "www.google.com";
        String unexpectedPartialUrl = "www.leosanchez.com";

        boolean firstTestStatus = false;
        boolean secondTestStatus = false;

        // POSITIVE
        firstTestStatus = Validate.isInString(currentUrl, expectedPartialUrl);

        // NEGATIVE
        secondTestStatus = Validate.isInString(currentUrl, unexpectedPartialUrl);

        // ASSERTIONS
        Assert.assertTrue(firstTestStatus);
        Assert.assertFalse(secondTestStatus);

        // NOTE THIS TEST WILL TAKE THE FAIL SCREENSHOT, BUT THE TEST WILL PASS
    }

    @Test(groups = { "Deploy" })
    public void testIsInStringWithoutAssertions() {
        Reporting.logReporter(Status.DEBUG,"RUNNING! - UnitWebUtilsValidationTest.testIsInStringWithoutAssertions");
        String currentUrl = WebDriverSteps.getCurrentUrl();
        String expectedPartialUrl = "www.google.com";
        String unexpectedPartialUrl = "www.leosanchez.com";

        boolean firstTestStatus = false;
        boolean secondTestStatus = false;

        // POSITIVE
        firstTestStatus = Validate.isInStringWithoutAssertions(currentUrl, expectedPartialUrl);

        // NEGATIVE
        secondTestStatus = Validate.isInStringWithoutAssertions(currentUrl, unexpectedPartialUrl);

        // ASSERTIONS
        Assert.assertTrue(firstTestStatus);
        Assert.assertFalse(secondTestStatus);
    }

}
