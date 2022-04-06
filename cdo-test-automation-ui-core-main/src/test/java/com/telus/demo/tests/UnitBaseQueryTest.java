package com.telus.demo.tests;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONException;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.test.dbutils.BaseQuery;
import com.test.reporting.Reporting;
import com.test.ui.actions.BaseTest;
import com.test.utils.DatesUtils;

/**
 * Class designed to test the basic functionality of the BaseQuery calls
 */
public class UnitBaseQueryTest extends BaseTest {

    private static final boolean POSITIVE_SCENARIO = true;
    private static final boolean NEGATIVE_SCENARIO = false;

    private static final String REGULAR_VALID_DBNAME = "CUSTODS";
    private static final String REGULAR_INVALID_DBNAME = "IMPOSIBLE";
    private static final String REGULAR_VALID_KEY_TO_LOOK = "CUSTOMER_ID";
    private static final String REGULAR_VALID_KEYVALUE_TO_LOOK = "501536"; // THIS ID CAN CHANGE AT ANY TIME, IF SO, JUST EXECUTE QUERY AND OBTAIN A NEW ONE (EXISTING ONE)
    private static final String REGULAR_VALID_QUERY_SINGLE = "SELECT * FROM CUSTOMER WHERE ROWNUM <= 1";
    private static final String REGULAR_VALID_QUERY_MULTIPLE = "SELECT * FROM CUSTOMER WHERE ROWNUM <= 10";

    private static final String DELAY_VALID_DBNAME = "CUSTODS";
    private static final String DELAY_INVALID_DBNAME = "IMPOSIBLE";
    private static final String DELAY_VALID_KEY_TO_LOOK = "CUSTOMER_ID";
    private static final String DELAY_INVALID_KEY_TO_LOOK = "CUSTOMER_IDX";
    private static final String DELAY_VALID_KEYVALUE_TO_LOOK = REGULAR_VALID_KEYVALUE_TO_LOOK;
    private static final String DELAY_INVALID_KEYVALUE_TO_LOOK = REGULAR_VALID_KEYVALUE_TO_LOOK + "X";
    private static final String DELAY_VALID_QUERY_SINGLE = "SELECT * FROM CUSTOMER WHERE ROWNUM <= 1";
    private static final String DELAY_INVALID_QUERY_SINGLE = "SELECT * FROM NULLABLE WHERE ROWNUM <= 1";
    private static final String DELAY_VALID_QUERY_MULTIPLE = "SELECT * FROM CUSTOMER WHERE ROWNUM <= 10";
    private static final String DELAY_INVALID_QUERY_MULTIPLE = "SELECT * FROM NULLABLE WHERE ROWNUM <= 10";
    private static final int DELAY_TIMEOUT_IN_SECONDS = 5;

    private static final String DELAY_VALID_KEY_TO_LOOK_WITH_CONSTANT_VALUE = "CUST_TYPE_CD";
    private static final String DELAY_VALID_KEYVALUE_TO_LOOK_WITH_CONSTANT_VALUE = "R";

    @AfterMethod(alwaysRun = true)
    public void afterTest() {
        // No need to have any step here
    }

    private void baseQueryCallMultiple(boolean isPositiveScenario) {
        String dbName = null;

        if (isPositiveScenario) {
            dbName = REGULAR_VALID_DBNAME;
        } else {
            dbName = REGULAR_INVALID_DBNAME;
        }

        String query = REGULAR_VALID_QUERY_MULTIPLE;
        List<Map<String, Object>> maps = BaseQuery.runQueryAndGetResults(dbName, query);
        int expectedCounterAssert = 10;
        int i = 0;
        int counterAssert = 0;

        for (Map<String, Object> map : maps) {
             for (Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                 if (key.equalsIgnoreCase(REGULAR_VALID_KEY_TO_LOOK)) { // ANY VALID / EXPECTED KEY IS FINE TO PUT
                    counterAssert = counterAssert + 1;
                }
            }
            i = i + 1;
        }
        Reporting.logReporter(Status.DEBUG, "query ran! =================================== ");
       Assert.assertEquals(counterAssert, expectedCounterAssert);

        
        Reporting.logReporter(Status.DEBUG, "#Rows Recovered: " + maps.size());
    }

    private void baseQueryCallObject(boolean isPositiveScenario) {
        String dbName = null;

        if (isPositiveScenario) {
            dbName = REGULAR_VALID_DBNAME;
        } else {
            dbName = REGULAR_INVALID_DBNAME;
        }

        String query = REGULAR_VALID_QUERY_SINGLE;
        Object map = BaseQuery.runQueryAndGetObject(dbName, query);
        Assert.assertNull(map);
    }

    private void baseQueryCallSingle(boolean isPositiveScenario) {
        String dbName = null;

        if (isPositiveScenario) {
            dbName = REGULAR_VALID_DBNAME;
        } else {
            dbName = REGULAR_INVALID_DBNAME;
        }

        String query = REGULAR_VALID_QUERY_SINGLE;
        Map<String, Object> map = BaseQuery.runQueryAndGetResult(dbName, query);

        for (Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            
            if (key.equalsIgnoreCase(REGULAR_VALID_KEY_TO_LOOK)) { // ANY VALID / EXPECTED KEY IS FINE TO PUT
                Assert.assertTrue(value.toString().equalsIgnoreCase(REGULAR_VALID_KEYVALUE_TO_LOOK), "Expected Data not found");
                return; // Exit to avoid failure
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
     * To run this Query is necessary to point to "DEMO" environment This simulates a wrong DB
     */
    @Test(groups = { "DeployNone" })
    public void testFailedDelayScenarioWithInvalidDBWithMultipleRows() {
      
        Reporting.logReporter(Status.DEBUG,"RUNNING! - UnitBaseQueryTest.testFailedDelayScenarioWithInvalidDBWithMultipleRows");
        String x = null;

        try {
            @SuppressWarnings("unused")
            List<Map<String, Object>> maps = BaseQuery.runQueryAndGetResultsWithDelayForFirstMatchingResult(DELAY_INVALID_DBNAME, DELAY_VALID_QUERY_MULTIPLE, DELAY_TIMEOUT_IN_SECONDS,
                    DELAY_VALID_KEY_TO_LOOK, DELAY_VALID_KEYVALUE_TO_LOOK);
        } catch (JSONException | NullPointerException e) {
            x = "hi_not_null";
            Reporting.logReporter(Status.DEBUG,"Expected Exception - as is Negative scenario");
            
        }
        Assert.assertNotNull(x);
    }

    /**
     * To run this Query is necessary to point to "DEMO" environment This simulates a wrong DB
     */
    @Test(groups = { "DeployNone" })
    public void testFailedDelayScenarioWithInvalidDBWithSingleRow() {
       Reporting.logReporter(Status.DEBUG,"RUNNING! - UnitBaseQueryTest.testFailedDelayScenarioWithInvalidDBWithSingleRow");
        String x = null;

        try {
            @SuppressWarnings("unused")
            Map<String, Object> map = BaseQuery.runQueryAndGetResultWithDelay(DELAY_INVALID_DBNAME, DELAY_VALID_QUERY_SINGLE, DELAY_TIMEOUT_IN_SECONDS, DELAY_VALID_KEY_TO_LOOK,
                    DELAY_VALID_KEYVALUE_TO_LOOK);
        } catch (JSONException | NullPointerException e) {
            x = "hi_not_null";
            Reporting.logReporter(Status.DEBUG,"Expected Exception - as is Negative scenario");
            
        }
        Assert.assertNotNull(x);
    }

    /**
     * To run this Query is necessary to point to "DEMO" environment This simulates a wrong KEY Value
     */
    @Test(groups = { "DeployNone" })
    public void testFailedDelayScenarioWithInvalidKeyValueWithMultipleRows() {
      
        Reporting.logReporter(Status.DEBUG,"RUNNING! - UnitBaseQueryTest.testFailedDelayScenarioWithInvalidKeyValueWithMultipleRows()");
        String x = null;

        Date date1 = null;
        Date date2 = null;

        try {
            date1 = DatesUtils.getCurrentDate();
            @SuppressWarnings("unused")
            List<Map<String, Object>> maps = BaseQuery.runQueryAndGetResultsWithDelayForFirstMatchingResult(DELAY_VALID_DBNAME, DELAY_VALID_QUERY_MULTIPLE, DELAY_TIMEOUT_IN_SECONDS,
                    DELAY_VALID_KEY_TO_LOOK, DELAY_INVALID_KEYVALUE_TO_LOOK);
        } catch (UnsupportedOperationException e) {
            date2 = DatesUtils.getCurrentDate();
            x = "hi_not_null";
           
            Reporting.logReporter(Status.DEBUG,"Expected Exception - as is Negative scenario");
            
        }
        Assert.assertNotNull(x);

        // Assert time is Bigger than timeout (depends on the actual iteration, it always complete the execution and then check the time to exit the loop, so time will always be a
        // little bit more than the real one.
        long timeDiff = DatesUtils.getTimeDifferenceInRealSeconds(date1, date2);
        Reporting.logReporter(Status.DEBUG,"timeDiff: " + timeDiff);
        Assert.assertTrue(timeDiff >= DELAY_TIMEOUT_IN_SECONDS);
    }

    /**
     * To run this Query is necessary to point to "DEMO" environment This simulates a wrong KEY Value
     */
    @Test(groups = { "DeployNone" })
    public void testFailedDelayScenarioWithInvalidKeyValueWithSingleRow() {
        Reporting.logReporter(Status.DEBUG,"RUNNING! - UnitBaseQueryTest.testFailedDelayScenarioWithInvalidKeyValueWithSingleRow()");
        
        String x = null;

        Date date1 = null;
        Date date2 = null;

        try {
            date1 = DatesUtils.getCurrentDate();
            @SuppressWarnings("unused")
            Map<String, Object> map = BaseQuery.runQueryAndGetResultWithDelay(DELAY_VALID_DBNAME, DELAY_VALID_QUERY_SINGLE, DELAY_TIMEOUT_IN_SECONDS, DELAY_VALID_KEY_TO_LOOK,
                    DELAY_INVALID_KEYVALUE_TO_LOOK);
        } catch (UnsupportedOperationException e) {
            date2 = DatesUtils.getCurrentDate();
            x = "hi_not_null";
            
            Reporting.logReporter(Status.DEBUG,"Expected Exception - as is Negative scenario");
            
        }
        Assert.assertNotNull(x);

        // Assert time is Bigger than timeout (depends on the actual iteration, it always complete the execution and then check the time to exit the loop, so time will always be a
        // little bit more than the real one.
        long timeDiff = DatesUtils.getTimeDifferenceInRealSeconds(date1, date2);
       Reporting.logReporter(Status.DEBUG,"timeDiff: " + timeDiff);
        Assert.assertTrue(timeDiff >= DELAY_TIMEOUT_IN_SECONDS);
    }

    /**
     * To run this Query is necessary to point to "DEMO" environment This simulates a wrong KEY Value
     */
    @Test(groups = { "DeployNone" })
    public void testFailedDelayScenarioWithInvalidKeyWithMultipleRows() {
        
        Reporting.logReporter(Status.DEBUG,"RUNNING! - UnitBaseQueryTest.testFailedDelayScenarioWithInvalidKeyWithMultipleRows()");
        String x = null;

        Date date1 = null;
        Date date2 = null;

        try {
            date1 = DatesUtils.getCurrentDate();
            @SuppressWarnings("unused")
            List<Map<String, Object>> maps = BaseQuery.runQueryAndGetResultsWithDelayForFirstMatchingResult(DELAY_VALID_DBNAME, DELAY_VALID_QUERY_MULTIPLE, DELAY_TIMEOUT_IN_SECONDS,
                    DELAY_INVALID_KEY_TO_LOOK, DELAY_VALID_KEYVALUE_TO_LOOK);
        } catch (UnsupportedOperationException e) {
            date2 = DatesUtils.getCurrentDate();
            x = "hi_not_null";
            
            Reporting.logReporter(Status.DEBUG,"Expected Exception - as is Negative scenario");
            
        }
        Assert.assertNotNull(x);

        // Assert time is Bigger than timeout (depends on the actual iteration, it always complete the execution and then check the time to exit the loop, so time will always be a
        // little bit more than the real one.
        long timeDiff = DatesUtils.getTimeDifferenceInRealSeconds(date1, date2);
        Reporting.logReporter(Status.DEBUG,"timeDiff: " + timeDiff);
        Assert.assertTrue(timeDiff >= DELAY_TIMEOUT_IN_SECONDS);
    }

    /**
     * To run this Query is necessary to point to "DEMO" environment This simulates a wrong KEY
     */
    @Test(groups = { "DeployNone" })
    public void testFailedDelayScenarioWithInvalidKeyWithSingleRow() {
       
        Reporting.logReporter(Status.DEBUG,"RUNNING! - UnitBaseQueryTest.testFailedDelayScenarioWithInvalidKeyWithSingleRow");
        String x = null;

        Date date1 = null;
        Date date2 = null;

        try {
            date1 = DatesUtils.getCurrentDate();
            @SuppressWarnings("unused")
            Map<String, Object> map = BaseQuery.runQueryAndGetResultWithDelay(DELAY_VALID_DBNAME, DELAY_VALID_QUERY_SINGLE, DELAY_TIMEOUT_IN_SECONDS, DELAY_INVALID_KEY_TO_LOOK,
                    DELAY_VALID_KEYVALUE_TO_LOOK);
        } catch (UnsupportedOperationException e) {
            date2 = DatesUtils.getCurrentDate();
            x = "hi_not_null";
            
            Reporting.logReporter(Status.DEBUG,"Expected Exception - as is Negative scenario");
            
          }
        Assert.assertNotNull(x);

        // Assert time is Bigger than timeout (depends on the actual iteration, it always complete the execution and then check the time to exit the loop, so time will always be a
        // little bit more than the real one.
        long timeDiff = DatesUtils.getTimeDifferenceInRealSeconds(date1, date2);
        Reporting.logReporter(Status.DEBUG,"timeDiff: " + timeDiff);
        Assert.assertTrue(timeDiff >= DELAY_TIMEOUT_IN_SECONDS);
    }

    /**
     * To run this Query is necessary to point to "DEMO" environment This simulates a wrong Query
     */
    @Test(groups = { "DeployNone" })
    public void testFailedDelayScenarioWithInvalidQueryWithMultipleRows() {
        Reporting.logReporter(Status.DEBUG,"RUNNING! - UnitBaseQueryTest.testFailedDelayScenarioWithInvalidQueryWithMultipleRows()");
        
        String x = null;

        Date date1 = null;
        Date date2 = null;

        try {
            date1 = DatesUtils.getCurrentDate();
            @SuppressWarnings("unused")
            List<Map<String, Object>> maps = BaseQuery.runQueryAndGetResultsWithDelayForFirstMatchingResult(DELAY_VALID_DBNAME, DELAY_INVALID_QUERY_MULTIPLE, DELAY_TIMEOUT_IN_SECONDS,
                    DELAY_VALID_KEY_TO_LOOK, DELAY_VALID_KEYVALUE_TO_LOOK);
        } catch (UnsupportedOperationException e) {
            date2 = DatesUtils.getCurrentDate();
            x = "hi_not_null";
            
            Reporting.logReporter(Status.DEBUG,"Expected Exception - as is Negative scenario");
            
        }
        Assert.assertNotNull(x);

        // Assert time is Bigger than timeout (depends on the actual iteration, it always complete the execution and then check the time to exit the loop, so time will always be a
        // little bit more than the real one.
        long timeDiff = DatesUtils.getTimeDifferenceInRealSeconds(date1, date2);
        Reporting.logReporter(Status.DEBUG,"timeDiff: " + timeDiff);
        Assert.assertTrue(timeDiff >= DELAY_TIMEOUT_IN_SECONDS);
    }

    /**
     * To run this Query is necessary to point to "DEMO" environment This simulates a wrong Query
     */
    @Test(groups = { "DeployNone" })
    public void testFailedDelayScenarioWithInvalidQueryWithSingleRow() {
        Reporting.logReporter(Status.DEBUG,"RUNNING! - UnitBaseQueryTest.testFailedDelayScenarioWithInvalidQueryWithSingleRow()");
        
        String x = null;

        Date date1 = null;
        Date date2 = null;

        try {
            date1 = DatesUtils.getCurrentDate();
            @SuppressWarnings("unused")
            Map<String, Object> map = BaseQuery.runQueryAndGetResultWithDelay(DELAY_VALID_DBNAME, DELAY_INVALID_QUERY_SINGLE, DELAY_TIMEOUT_IN_SECONDS, DELAY_VALID_KEY_TO_LOOK,
                    DELAY_VALID_KEYVALUE_TO_LOOK);
        } catch (UnsupportedOperationException e) {
            date2 = DatesUtils.getCurrentDate();
            x = "hi_not_null";
            Reporting.logReporter(Status.DEBUG,"Expected Exception - as is Negative scenario");
            
        }
        Assert.assertNotNull(x);

        // Assert time is Bigger than timeout (depends on the actual iteration, it always complete the execution and then check the time to exit the loop, so time will always be a
        // little bit more than the real one.
        long timeDiff = DatesUtils.getTimeDifferenceInRealSeconds(date1, date2);
        Reporting.logReporter(Status.DEBUG,"timeDiff: " + timeDiff);
        Assert.assertTrue(timeDiff >= DELAY_TIMEOUT_IN_SECONDS);
    }

    /**
     * To run this Query is necessary to point to "DEMO" environment This simulates a wrong DB
     */
    @Test(groups = { "DeployNone" })
    public void testFailedisKeyValuePresentInAllQueryResultsWithDelay() {
        Reporting.logReporter(Status.DEBUG,"RUNNING! - UnitBaseQueryTest.testFailedisKeyValuePresentInAllQueryResultsWithDelay");
        boolean isPresent = BaseQuery.isKeyValuePresentInAllResultsWithDelay(DELAY_VALID_DBNAME, DELAY_VALID_QUERY_MULTIPLE, DELAY_TIMEOUT_IN_SECONDS, DELAY_VALID_KEY_TO_LOOK,
                DELAY_VALID_KEYVALUE_TO_LOOK);

        Assert.assertFalse(isPresent);
    }

    /**
     * To run this Query is necessary to point to "DEMO" environment
     */
    @Test(groups = { "DeployNone" })
    public void testFailedScenarioWithMultipleRows() {
        Reporting.logReporter(Status.DEBUG,"RUNNING! - UnitBaseQueryTest.testFailedScenarioWithMultipleRows");
        
        String x = null;

        try {
            this.baseQueryCallMultiple(NEGATIVE_SCENARIO);
        } catch (Exception e) {
            x = "hi_not_null";
            
            Reporting.logReporter(Status.DEBUG,"Expected Exception - as is Negative scenario");
            
        }
        Assert.assertNotNull(x);
    }

    /**
     * To run this Query is necessary to point to execution.environment=DEMO
     */
    @Test(groups = { "DeployNone" })
    public void testFailedScenarioWithObject() {
        
        Reporting.logReporter(Status.DEBUG,"RUNNING! - UnitBaseQueryTest.testFailedScenarioWithObject");
        String x = null;

        try {
            this.baseQueryCallObject(NEGATIVE_SCENARIO);
        } catch (Exception e) {
            x = "hi_not_null";
            Reporting.logReporter(Status.DEBUG,"Expected Exception - as is Negative scenario");
            
        }
        Assert.assertNotNull(x);
    }

    /**
     * To run this Query is necessary to point to execution.environment=DEMO
     */
    @Test(groups = { "DeployNone" })
    public void testFailedScenarioWithSingleRow() {
        Reporting.logReporter(Status.DEBUG,"RUNNING! - UnitBaseQueryTest.testFailedScenarioWithSingleRow");
        String x = null;

        try {
            this.baseQueryCallSingle(NEGATIVE_SCENARIO);
        } catch (Exception e) {
            x = "hi_not_null";
        //    System.out.println("Expected Exception - as is Negative scenario");
            
            Reporting.logReporter(Status.DEBUG,"Expected Exception - as is Negative scenario");
            
        }
        Assert.assertNotNull(x);
    }

    /**
     * To run this Query is necessary to point to "DEMO" environment This simulates a wrong DB
     */
    @Test(groups = { "DeployNone" })
    public void testPositiveDelayScenarioWithMultipleRow() {
        Reporting.logReporter(Status.DEBUG,"RUNNING! - UnitBaseQueryTest.testPositiveDelayScenarioWithMultipleRow");
        List<Map<String, Object>> maps = BaseQuery.runQueryAndGetResultsWithDelayForFirstMatchingResult(DELAY_VALID_DBNAME, DELAY_VALID_QUERY_MULTIPLE, DELAY_TIMEOUT_IN_SECONDS,
                DELAY_VALID_KEY_TO_LOOK, DELAY_VALID_KEYVALUE_TO_LOOK);

       Reporting.logReporter(Status.DEBUG,"#Rows Recovered: " + maps.size());
        Reporting.logReporter(Status.DEBUG,"Test - Before loop query Data! =================================== ");
    
        int i = 0;

        for (Map<String, Object> map : maps) {
            for (Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
               
                if (key.equalsIgnoreCase(REGULAR_VALID_KEY_TO_LOOK)) { // ANY VALID / EXPECTED KEY IS FINE TO PUT
                    Assert.assertTrue(value.toString().equalsIgnoreCase(REGULAR_VALID_KEYVALUE_TO_LOOK), "Expected Data not found");
                    return; // Exit to avoid failure
                }
            }
            i = i + 1;
        }

        Reporting.logReporter(Status.DEBUG,"query ran! =================================== ");
        
        Assert.assertTrue(false, "Element searched not found causing failure");

    }

    /**
     * To run this Query is necessary to point to "DEMO" environment This simulates a wrong DB
     */
    @Test(groups = { "DeployNone" })
    public void testPositiveDelayScenarioWithSingleRow() {
        Reporting.logReporter(Status.DEBUG,"RUNNING! - UnitBaseQueryTest.testPositiveDelayScenarioWithSingleRow");
        
        Map<String, Object> map = BaseQuery.runQueryAndGetResultWithDelay(DELAY_VALID_DBNAME, DELAY_VALID_QUERY_SINGLE, DELAY_TIMEOUT_IN_SECONDS, DELAY_VALID_KEY_TO_LOOK,
                DELAY_VALID_KEYVALUE_TO_LOOK);

        map.get(DELAY_VALID_KEY_TO_LOOK); // If the key exist then thats ok
    }

    /**
     * To run this Query is necessary to point to "DEMO" environment This simulates a wrong DB
     */
    @Test(groups = { "DeployNone" })
    public void testPositiveisKeyValuePresentInAllQueryResultsWithDelay() {
        Reporting.logReporter(Status.DEBUG,"RUNNING! - UnitBaseQueryTest.testPositiveisKeyValuePresentInAllQueryResultsWithDelay");
        
        boolean isPresent = BaseQuery.isKeyValuePresentInAllResultsWithDelay(DELAY_VALID_DBNAME, DELAY_VALID_QUERY_MULTIPLE, DELAY_TIMEOUT_IN_SECONDS, DELAY_VALID_KEY_TO_LOOK_WITH_CONSTANT_VALUE,
                DELAY_VALID_KEYVALUE_TO_LOOK_WITH_CONSTANT_VALUE);

        Assert.assertTrue(isPresent);
    }

    /**
     * To run this Query is necessary to point to "DEMO" environment
     */
    @Test(groups = { "DeployNone" })
    public void testPositiveScenarioWithMultipleRows() {
        
        
        Reporting.logReporter(Status.DEBUG,"RUNNING! - UnitBaseQueryTest.testPositiveScenarioWithMultipleRows");
        
        this.baseQueryCallMultiple(POSITIVE_SCENARIO);
    }

    /**
     * To run this Query is necessary to point to "DEMO" environment
     */
    @Test(groups = { "DeployNone" })
    public void testPositiveScenarioWithObject() {
       Reporting.logReporter(Status.DEBUG,"RUNNING! - UnitBaseQueryTest.testPositiveScenarioWithObject");
        
        this.baseQueryCallObject(POSITIVE_SCENARIO);
    }

    /**
     * To run this Query is necessary to point to "DEMO" environment
     */
    @Test(groups = { "DeployNone" })
    public void testPositiveScenarioWithSingleRow() {
        Reporting.logReporter(Status.DEBUG,"RUNNING! - UnitBaseQueryTest.testPositiveScenarioWithSingleRow");
        
        this.baseQueryCallSingle(POSITIVE_SCENARIO);
    }

}
