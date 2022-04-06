package com.test.dbutils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONObject;

import com.aventstack.extentreports.Status;
import com.test.files.interaction.ReadJSON;
import com.test.logging.Logging;
import com.test.reporting.Reporting;
import com.test.tdm.utils.ConvertJSON;
import com.test.utils.DatesUtils;
import com.test.utils.SystemProperties;

/**
 * Constructor Will return a new class that opens a connection with the DB(dbName), by Looking the provided dbName in the dbAuth.jsonFile, based in the current Environment
 */
public class BaseQuery {
	
	private static final String TIME_OUT_1 = "Time out - query did not contain expected value after [";
	private static final String TIME_OUT_2 = "] seconds";
	
	 private BaseQuery() {
		    throw new IllegalStateException("Utility class");
		  }

    /**
     * Will retrieve the connection parameters from the JSON File - Assuming there is only a single hierarchy in the JSON File that is DBName
     */
    public static List<String> getConnectParametersFromJson(String dbName, String jsonFileLocation) {
        return DbUtils.getConnectParametersFromJson(dbName, jsonFileLocation);
    }

    /**
     * Will retrieve the connection parameters from the JSON File - Assuming there is Environment/DbName Hierarchy
     */
    public static List<String> getConnectParametersFromJsonWithEnvironment(String dbName, String jsonFileLocation) {
        return DbUtils.getConnectParametersFromJson(dbName, jsonFileLocation, SystemProperties.EXECUTION_ENVIRONMENT);
    }

    /**
     * Will return a new class that opens a connection, by Looking the provided dbName in the dbAuth.jsonFile, then based in the current Environment finally return the DB Mapping.
     */
    private static DbUtils getDbUtilsFromDbName(String dbName) {
        String environment = SystemProperties.EXECUTION_ENVIRONMENT;
        JSONObject jDB = DbAuth.authDB(environment);
        return new DbUtils(ConvertJSON.jsonToMap(ReadJSON.getObject(jDB, dbName)));
    }

    /**
     * Will look for a key value with delay - timeout for finding it.
     */
    public static boolean isKeyValuePresentInAllResultsWithDelay(String dbName, String query, int timeOutForDelay, String expectedKey, String expectedValue) {
        List<Map<String, Object>> rows = null;

        Date date1 = DatesUtils.getCurrentDate();
        Date date2 = null;

        long passedSeconds = 0;
        int counterEntryFindings = 0;
        int rowsSize = 0;

        int i = 0;
        int j = 0;

        do {
            j = 0;
            counterEntryFindings = 0;
            rows = runQueryAndGetResults(dbName, query);
            rowsSize = rows.size();

            if (rows != null) {
                String actualValue = null;

                for (Map<String, Object> row : rows) {


                    for (Entry<String, Object> entry : row.entrySet()) {
                        String key = entry.getKey();
                        Object value = entry.getValue();


                        String actualKey = key;
                        if (actualKey.equalsIgnoreCase(expectedKey)) {

                            actualValue = value.toString();
                            if (actualValue.equalsIgnoreCase(expectedValue)) {
                                counterEntryFindings = counterEntryFindings + 1;
                            } 

                        } 

                    } // for entries

                    j = j + 1;
                } // for rows

            } // if
            

            date2 = DatesUtils.getCurrentDate();
            passedSeconds = DatesUtils.getTimeDifferenceInRealSeconds(date1, date2);

            // BEFORE NEW ITERATION
            if (rowsSize == counterEntryFindings) { // ALL ROWS CONTAINED EXPECTED KEY/KEYVALUE

                if (rowsSize == 0) {

                    
                    Reporting.logReporter(Status.DEBUG, "Failed due that the query did not retrieve any Result to analyze");
                    
                    return false;
                }
                Reporting.logReporter(Status.DEBUG, "All the Results/Rows[" + rowsSize + "] contained the expected KEY/KEYVALUE");
                

                return true;
            }

            if (passedSeconds > timeOutForDelay) {

               
                Reporting.logReporter(Status.DEBUG, "Time out - Only [" + counterEntryFindings + "] Results/Rows out of [" + rowsSize + "] contained the expected KEY/KEYVALUE after [" + timeOutForDelay + TIME_OUT_2);
                
                return false;
            }

            i = i + 1;

        } while (passedSeconds < timeOutForDelay);
        Reporting.logReporter(Status.DEBUG, "Time out - Only [" + counterEntryFindings + "] Results/Rows out of [" + rowsSize + "] contained the expected KEY/KEYVALUE after [" + timeOutForDelay + TIME_OUT_2);
        

        return false;
    }

    public static void runQueryAndExecuteUpdate(String dbName, String query) {
        getDbUtilsFromDbName(dbName).executeUpdate(query);
    }

    public static Object runQueryAndGetObject(String dbName, String query) {
        return getDbUtilsFromDbName(dbName).readValue(query);
    }

    public static Map<String, Object> runQueryAndGetResult(String dbName, String query) {
        return getDbUtilsFromDbName(dbName).readRow(query);
    }

    public static List<Map<String, Object>> runQueryAndGetResults(String dbName, String query) {
        return getDbUtilsFromDbName(dbName).readRows(query);
    }

    /**
     * Will do a query to obtain multiple results (rows) as a list, then look for a particular Column/Value in each one during a certain time, if found will retrieve the row for
     * that result
     */
    public static List<Map<String, Object>> runQueryAndGetResultsWithDelayForFirstMatchingResult(String dbName, String query, int timeOutForDelay, String expectedKey, String expectedValue) {
        List<Map<String, Object>> rows = null;
        Date date1 = DatesUtils.getCurrentDate();
        Date date2 = null;

        long passedSeconds = 0;

        int i = 0;
        int j = 0;

        do {
            j = 0;
            rows = runQueryAndGetResults(dbName, query);

            if (rows != null) {

                String actualValue = null;

                for (Map<String, Object> row : rows) {


                    for (Entry<String, Object> entry : row.entrySet()) {
                        String key = entry.getKey();
                        Object value = entry.getValue();


                        String actualKey = key;
                        if (actualKey.equalsIgnoreCase(expectedKey)) {

                            actualValue = value.toString();
                            if (actualValue.equalsIgnoreCase(expectedValue)) {
                                //date2 = DatesUtils.getCurrentDate()
                                //passedSeconds = DatesUtils.getTimeDifferenceInRealSeconds(date1, date2)
                            	return rows;
                            } 

                        } 

                    } // for entries
                    j = j + 1;
                } // for rows

            } // if
           

            date2 = DatesUtils.getCurrentDate();
            passedSeconds = DatesUtils.getTimeDifferenceInRealSeconds(date1, date2);


            if (passedSeconds > timeOutForDelay) {
                throw new UnsupportedOperationException(TIME_OUT_1 + timeOutForDelay + TIME_OUT_2);
            }

            i = i + 1;
        } while (passedSeconds < timeOutForDelay);

        throw new UnsupportedOperationException(TIME_OUT_1 + timeOutForDelay + TIME_OUT_2);
    }

    /**
     * Will do a query to obtain single results (rows) as a map, then look for a particular Column/Value in each entry during a certain time, if found will retrieve the map.
     */
    public static Map<String, Object> runQueryAndGetResultWithDelay(String dbName, String query, int timeOutForDelay, String expectedKey, String expectedValue) {
        Map<String, Object> queryResultSingleRow = null;
        Date date1 = DatesUtils.getCurrentDate();
        Date date2 = null;

        long passedSeconds = 0;

        int i = 0;

        do {
            queryResultSingleRow = runQueryAndGetResult(dbName, query);

            if (queryResultSingleRow != null) {

                String actualValue = null;
                try {
                    actualValue = queryResultSingleRow.get(expectedKey).toString();

                    if (actualValue.equalsIgnoreCase(expectedValue)) {
                        //date2 = DatesUtils.getCurrentDate()
                        //passedSeconds = DatesUtils.getTimeDifferenceInRealSeconds(date1, date2)

                        return queryResultSingleRow;
                    } 

                } catch (NullPointerException e) {
                	Logging.logReporter(Status.DEBUG, "Exception Caught : " + e);
                }

            } // !=null

           

            date2 = DatesUtils.getCurrentDate();
            passedSeconds = DatesUtils.getTimeDifferenceInRealSeconds(date1, date2);


            if (passedSeconds > timeOutForDelay) {
                throw new UnsupportedOperationException(TIME_OUT_1 + timeOutForDelay + TIME_OUT_2);
            }

            i = i + 1;
        } while (passedSeconds < timeOutForDelay);

        throw new UnsupportedOperationException(TIME_OUT_1 + timeOutForDelay + TIME_OUT_2);
    }

}
