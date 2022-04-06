package com.test.dbutils;

import java.io.File;

import org.json.JSONObject;

import com.aventstack.extentreports.Status;
import com.test.files.interaction.ReadJSON;
import com.test.reporting.Reporting;

/**
 * Utility to get the Environment supported DB's Data based on the Environment Name
 *
 */
public class DbAuth {
	private static final String PT140 = "PT140";
	
	 private DbAuth() {
		    throw new IllegalStateException("Utility class");
		  }

    public static JSONObject authDB(String envDB) {
        JSONObject jFile = new JSONObject(ReadJSON.parse(File.separator + "dbAuth.json"));
        JSONObject jDB = null;
        
        JSONObject jDB1= authDBbifuracted1(envDB,jFile);
        JSONObject jDB2= authDBbifuracted2(envDB,jFile);
        if(jDB1!=null)
        {
     	   return jDB1;
        }else if(jDB2!=null)
        {
     	   return jDB2;
        }
        else
        {
        	switch (envDB) {
	            case "PT148":
	                jDB = jFile.getJSONObject("PT148");
	                break;
	            case "NC_SYSTEM":
	                jDB = jFile.getJSONObject("NC_SYSTEM");
	                break;
	            case "ST101":
	                jDB = jFile.getJSONObject("ST101");
	                break;
	            case "PS101":
	                jDB = jFile.getJSONObject(PT140); // The environment PT140 can be also find as PS101.
	                break;
	            default:
	                String msg = "DbAuth - Invalid JDB name: ";
	                Reporting.logReporter(Status.DEBUG, msg + envDB);
	                throw new UnsupportedOperationException(msg);
	        }
        }
        return jDB;
    }
    
    private static JSONObject authDBbifuracted1(String envDB,JSONObject jFile) {
    	JSONObject jDB = null;
    	switch (envDB) {	
		    case "tdmRepo":
		        jDB = jFile.getJSONObject("tdmRepo");
		        break;
		    case "demo":
		        jDB = jFile.getJSONObject("demo");
		        break;
		    case "DEMO":
		        jDB = jFile.getJSONObject("DEMO");
		        break;
		    case "KB168":
		        jDB = jFile.getJSONObject("KB168");
		        break;
		    case "KB148":
		        jDB = jFile.getJSONObject("KB148");
		        break;
		    case "KB140":
		        jDB = jFile.getJSONObject("KB140");
		        break;
		    case "CODS140":
		        jDB = jFile.getJSONObject("CODS140");
		        break;
		    default:
		    	break;
		    }
    	return jDB;
    }
    
    private static JSONObject authDBbifuracted2(String envDB,JSONObject jFile) {
    	JSONObject jDB = null;
    	switch (envDB) {	
		    case "CODS148":
		        jDB = jFile.getJSONObject("CODS148");
		        break;
		    case "RWMS140":
		        jDB = jFile.getJSONObject("RWMS140");
		        break;
		   case "CRDB148":
		        jDB = jFile.getJSONObject("CRDB148");
		        break;
		   case "billPreference":
               jDB = jFile.getJSONObject("billPreference");
               break;
           case "EBS148":
               jDB = jFile.getJSONObject("EBS148");
               break;
           case "PT168":
               jDB = jFile.getJSONObject("PT168");
               break;
           case PT140:
               jDB = jFile.getJSONObject(PT140);
               break;
		    default:
		    	break;
		    }
    	return jDB;
    }
}
