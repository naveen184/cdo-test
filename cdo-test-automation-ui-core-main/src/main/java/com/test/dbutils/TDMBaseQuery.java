package com.test.dbutils;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.test.files.interaction.ReadJSON;
import com.test.tdm.utils.ConvertJSON;

/**
 * Constructor Will return a new class that opens a connection with the TDM DB, by Looking the db connection details in the dbAuth.jsonFile
 */
public class TDMBaseQuery {
	
	 private TDMBaseQuery() {
		    throw new IllegalStateException("Utility class");
		  }

    //private static final String CONSTRUCTOR_MESSAGE = "Utility class - Not Designed to be Instantiated"

    private static final String TDM_REPO = "tdmRepo";

    /**
     * Will return a new class that opens a TDM db connection, by Looking the TDM db details in the dbAuth.jsonFile, finally return the DB Mapping.
     */

    private static DbUtils getTDMDbUtils(String dbName) {
        JSONObject jDB = DbAuth.authDB(TDM_REPO);
        return new DbUtils(ConvertJSON.jsonToMap(ReadJSON.getObject(jDB, dbName)));
    }

    public static void runTDMQueryAndExecuteUpdate(String dbName, String query) {
        getTDMDbUtils(dbName).executeUpdate(query);
    }

    public static List<Map<String, Object>> runTDMQueryAndGetResults(String dbName, String query) {
        return getTDMDbUtils(dbName).readRows(query);
    }

}
