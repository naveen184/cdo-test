package com.test.tdm.utils;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.aventstack.extentreports.Status;
import com.test.reporting.Reporting;
import com.test.utils.SystemProperties;

/**
 * Class designed to update the TDM JSON File
 *
 */
public class UpdateJSON {
	
	private UpdateJSON() {
		    throw new IllegalStateException("Utility class");
		  }

    /**
     * @param tdmRepo
     * @param tdmData
     * @return
     */
    public static JSONObject updateJson(JSONArray tdmRepo, JSONObject tdmData) {
        String env = SystemProperties.EXECUTION_ENVIRONMENT;
        JSONObject val = tdmRepo.getJSONObject(0);
        Map<String, Object> map = ConvertJSON.toMap(tdmData);
        Object[] keyValues = map.keySet().toArray();
        Object[] values = map.values().toArray();

        for (int i = 0; i < tdmData.length(); i++) {
            val.put(keyValues[i].toString(), values[i]);
        }

        val.put("environment", env);
        Reporting.logReporter(Status.DEBUG, "Updated JSON: " + val);
        return val;
    }
}
