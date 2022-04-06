package com.test.tdm.utils;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.aventstack.extentreports.Status;
import com.test.dbutils.TDMBaseQuery;
import com.test.dbutils.TDMQueryBuilder;
import com.test.files.interaction.ReadJSON;
import com.test.logging.Logging;
import com.test.reporting.Reporting;
import com.test.utils.SystemProperties;

public class TestSpecs {
		
	private TestSpecs() {
		    throw new IllegalStateException("Utility class");
		  }

    private static final String TEST_DATA = "testData";
    private static final String TDM = "TDM";
    private static final String TDM_REPO = "TDMRepo";
    private static final String BUILDER_QUERY = "Builder query: ";
    private static final String RESULT = "Result: ";

    /**
     * Method used to identify the note to update in the testData Section
     *
     * @return
     */
    public static String getNode(List<Map<String, Object>> qry, String input) {
        Map<String, Object> val = qry.get(0);
        String value = (String) val.get(input);
        if (value == null) {
            return "null";
        } else {
            return value;
        }
    }

    /**
     * Process a json file to read data from DB Expects a json with the format { "testDataRequirements": { "TDMRepo": [ { } ], "testData": { "Application": "GOOGLE",
     * "SearchCriteria": "Test" } } }
     *
     * @param path
     *            to the json file to process
     * @return testData json object with the variables and values
     */
    public static JSONObject processTestSpec(String testSpecFilePath) {
        return processTestSpec(testSpecFilePath, 0);
    }

    /**
     * Process a json file to read data from DB Expects a json with the format { "testDataRequirements": { "TDMRepo": [ { }, { } ], "testData": { "Application": "GOOGLE",
     * "SearchCriteria": "Test" } } }
     *
     * @param path
     *            to the json file to process
     * @return testData json object with the variables and values
     */
    public static JSONObject processTestSpec(String testSpecFilePath, int tdmRepoIndex) {
        JSONObject json = new JSONObject(ReadJSON.parse(testSpecFilePath)).getJSONObject("testDataRequirements");
        JSONObject jsonUpdate = new JSONObject();
        try {
            JSONArray tdmRepo = json.getJSONArray(TDM_REPO);

            String qry = TDMQueryBuilder.constructQueryForUI(tdmRepo.getJSONObject(tdmRepoIndex), SystemProperties.EXECUTION_ENVIRONMENT);

            Reporting.logReporter(Status.DEBUG, BUILDER_QUERY + qry);

            List<Map<String, Object>> qryResult = TDMBaseQuery.runTDMQueryAndGetResults(TDM, qry);
            Reporting.logReporter(Status.DEBUG,RESULT + qryResult);

            jsonUpdate = setDataValue(json.getJSONObject(TEST_DATA), qryResult);
        } catch (RuntimeException e) {
        	jsonUpdate = json.getJSONObject(TEST_DATA);
        	Logging.logReporter(Status.DEBUG, "Exception Caught : " + e);
        }
        return jsonUpdate;
    }

    /**
     * Second option to process a json file to read data from DB Difference is that this method can get a json from the test to feed the section TDMRepo with dynamic values
     *
     * @param path
     *            to the json file to process
     * @param json
     *            file to include as part of the TDMRepo section
     * @return testData json object with the variables and values
     */
    public static JSONObject processTestSpec(String testSpecFilePath, JSONObject tdmData) {
        JSONObject json = new JSONObject(ReadJSON.parse(testSpecFilePath)).getJSONObject("testDataRequirements");
        JSONArray tdmRepoArray = json.getJSONArray(TDM_REPO);
        JSONObject tdmRepo = UpdateJSON.updateJson(tdmRepoArray, tdmData);
        String qry = TDMQueryBuilder.constructQueryForUI(tdmRepo, SystemProperties.EXECUTION_ENVIRONMENT);
        
        Reporting.logReporter(Status.DEBUG,BUILDER_QUERY + qry);

        List<Map<String, Object>> qryResult = TDMBaseQuery.runTDMQueryAndGetResults(TDM, qry);

        
        Reporting.logReporter(Status.DEBUG,RESULT + qryResult);
        return setDataValue(json.getJSONObject(TEST_DATA), qryResult);
    }

    /**
     * Method used to update the testData section with values coming from the TDM DB used on the processTestSpecs
     *
     * @param json
     *            file to include as part of the TDMRepo section
     * @return testData json object with the updated TDMRepo section
     */
    public static JSONObject setDataValue(JSONObject json, List<Map<String, Object>> qry) {
        String jsonM = json.toString();
        Map<String, Object> map = ConvertJSON.toMap(json);
        Object[] keyValues = map.values().toArray();
        for (int i = 0; i < map.size(); i++) {
            if (keyValues[i].toString().contains("#")) {
                String input = getNode(qry, keyValues[i].toString().replace("#", ""));
                jsonM = jsonM.replaceAll(keyValues[i].toString(), input);
            }
        }
        Reporting.logReporter(Status.DEBUG,"TestData: " + jsonM);

        return new JSONObject(jsonM);
    }

    
}
