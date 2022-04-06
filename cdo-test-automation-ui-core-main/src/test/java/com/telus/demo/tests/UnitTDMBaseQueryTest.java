package com.telus.demo.tests;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.test.dbutils.TDMBaseQuery;
import com.test.reporting.Reporting;
import com.test.ui.actions.BaseTest;

/**
 * Class designed to test the basic functionality of the TDMBaseQuery calls
 */
public class UnitTDMBaseQueryTest extends BaseTest {

    private static final boolean POSITIVE_SCENARIO = true;
    private static final boolean NEGATIVE_SCENARIO = false;

    @AfterMethod(alwaysRun = true)
    public void afterTest() {
        // No need to have any step here
    }

    private void baseQueryCall(boolean isPositiveScenario) {
        String dbName = null;

        if (isPositiveScenario) {
            dbName = "TDM";
        } else {
            dbName = "IMPOSIBLE";
        }

        String query = "SELECT * FROM TDM_CUSTOMER WHERE ROWNUM <= 1";
        // Map<String, Object> map = BaseQuery.runQueryAndGetResult(dbName, query);
        List<Map<String, Object>> map = TDMBaseQuery.runTDMQueryAndGetResults(dbName, query);

        Reporting.logReporter(Status.DEBUG,"query Data! =================================== ");
        
        // for (Entry<String, Object> entry : map.entrySet()) {
        for (Map<String, Object> entry : map) {

            for (Entry<String, Object> entry2 : entry.entrySet()) {
                String key = entry2.getKey();
                Object value = entry2.getValue();
                Reporting.logReporter(Status.DEBUG,key + " / " + value);
                if (key.equalsIgnoreCase("TECHNOLOGY")) { // ANY VALID / EXPECTED KEY IS FINE TO PUT
                    Assert.assertTrue(true, "Expected Data not found");
                    return; // Exit to avoid failure
                }
            }

        }

       Reporting.logReporter(Status.DEBUG,"query ran! =================================== ");
        Assert.assertTrue(false, "Element searched not found causing failure");
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeTest() {
        // No need to have any step here
    }

    /**
     * To run this Query is necessary to point to execution.environment=DEMO
     */
    @Test(groups = { "Deploy" })
    public void testFailedScenario() {
        Reporting.logReporter(Status.DEBUG,"RUNNING! - UnitTDMBaseQueryTest.testFailedScenario");
        String x = null;

        try {
            this.baseQueryCall(NEGATIVE_SCENARIO);
        } catch (Exception e) {
            x = "hi_not_null";
            Reporting.logReporter(Status.DEBUG,"Expected Exception - as is Negative scenario");
        }
        Assert.assertNotNull(x);
    }

    /**
     * To run this Query is necessary to point to "demo" environment
     */
    @Test(groups = { "Deploy" })
    public void testPositiveScenario() {
        Reporting.logReporter(Status.DEBUG,"RUNNING! - UnitTDMBaseQueryTest.testPositiveScenario");
        this.baseQueryCall(POSITIVE_SCENARIO);
    }
}
