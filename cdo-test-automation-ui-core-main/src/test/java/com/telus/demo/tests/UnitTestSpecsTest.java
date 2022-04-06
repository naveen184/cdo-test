package com.telus.demo.tests;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.test.reporting.Reporting;
import com.test.tdm.utils.TestSpecs;
import com.test.ui.actions.BaseTest;

/**
 * The utility Use by the back the environment tdmRepo to run this test
 */
public class UnitTestSpecsTest extends BaseTest {

    String jsonFileDynamic = "\\testSpecs\\TestSpecs_DynamicSpecs.json";
    JSONObject tdmData = new JSONObject();

    private static void printTestSpecs(JSONObject testData) {
        Assert.assertNotNull(testData.getString("ban"));
      //  System.out.println(testData.getString("ban"));
        Reporting.logReporter(Status.DEBUG,testData.getString("ban"));
    }

    @AfterMethod(alwaysRun = true)
    public void afterTest() {
        // No need to have any step here
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeTest() {
        // No need to have any step here
    }

    /**
     * Simple Demo, to show how to Read Json File with the testData Utils
     */
    @Test(groups = { "Deploy" })
    public void testReadSimpleTestSpec() {
        Reporting.logReporter(Status.DEBUG,"RUNNING! - UnitTestSpecsTest.testReadSimpleTestSpec");
        // Example of how to obtain data from testSpec json
        this.testData = TestSpecs.processTestSpec("\\testSpecs\\TestSpecs_SimpleTest1.json");
       Reporting.logReporter(Status.DEBUG,"The application is: " + this.testData.getString("Application"));
    }

    /**
     * TDM Data is provided OUTSIDE the Dynamic File (Multiple Parameters injected as external JSON)
     */
    @Test(groups = { "Deploy", "Maintenance" })
    public void testScenarioTestSpecOutsideParameters() {
        Reporting.logReporter(Status.DEBUG,"RUNNING! - UnitTestSpecsTest.testScenarioTestSpecOutsideParameters");
        this.tdmData = new JSONObject("{'dataRequirement':'BAN and SUB with Sharing Group','accountType':'CONSUMER REGULAR'}");
        this.testData = TestSpecs.processTestSpec(this.jsonFileDynamic, this.tdmData);
        printTestSpecs(this.testData);
    }

    /**
     * TDM Data is provided INSIDE the Dynamic File (Single Parameter, read from the Internal JSON (Index 0))
     */
    @Test(groups = { "Deploy" })
    public void testScenarioWirelessInsideParameter() {
         Reporting.logReporter(Status.DEBUG,"RUNNING! - UnitTestSpecsTest.testScenarioWirelessInsideParameter");
        
        this.testData = TestSpecs.processTestSpec(this.jsonFileDynamic);
        printTestSpecs(this.testData);

    }

    /**
     * TDM Data is provided INSIDE the Dynamic File (Multiple Parameter, read from the Internal JSON with many options to use (Index 1))
     */
    @Test(groups = { "Deploy" })
    public void testScenarioWirelessInsideParameters() {
        Reporting.logReporter(Status.DEBUG,"RUNNING! - UnitTestSpecsTest.testScenarioWirelessInsideParameters");
        
        this.testData = TestSpecs.processTestSpec(this.jsonFileDynamic, 1);
        printTestSpecs(this.testData);
    }

    /**
     * TDM Data is provided OUTSIDE the Dynamic File (Single Parameter injected as external JSON)
     */
    @Test(groups = { "Deploy", "Maintenance" })
    public void testScenarioWirelessOutsideParameter() {
       Reporting.logReporter(Status.DEBUG,"RUNNING! - UnitTestSpecsTest.testScenarioWirelessOutsideParameter");
        
        this.tdmData = new JSONObject("{'dataRequirement':'WIRELESS ACCOUNT'}");
        this.testData = TestSpecs.processTestSpec(this.jsonFileDynamic, this.tdmData);
        printTestSpecs(this.testData);
    }

}