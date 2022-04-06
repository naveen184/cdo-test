package com.test.ui.actions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONObject;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.Status;
import com.test.files.interaction.ReadJSON;
import com.test.logging.Logging;
import com.test.reporting.Reporting;
import com.test.utils.SystemProperties;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
/**
 ****************************************************************************
 * HIGHLIGHTS: > Methods reusable for all projects. > Methods that apply action on the driver. Like navigating to a URL. > Not reference to application objects. > The URL to use is
 * project specific and can be obtained from package com.projectname.data
 ****************************************************************************
 */



public class WebDriverSteps extends WebDriverSession {

   //private static final int FIVE_SECONDS_MILLISECONDS = 5000
    private String url;
	private static final String CALLING_MSG = "-Calling_From-";
    public static final  String NAME_OF_CURR_CLASS1= Thread.currentThread().getStackTrace()[1].getClassName();
    public static final  String NAME_OF_CURR_CLASS= Thread.currentThread().getStackTrace()[1].getClassName().substring(NAME_OF_CURR_CLASS1.lastIndexOf('.')+1);
    /**
     * OBJECTIVE: Use this method to close browser for each scenario.
     */
    public static boolean closeTheBrowser() {
        Reporting.logReporter(Status.INFO, "closeTheBrowser");
        WebDriverSession.getWebDriverSession().quit();
        removeThreatIdFromMap();
        return true;
    }

    /**
     * OBJECTIVE: Use this method to get the current URL
     */
    public static String getCurrentUrl() {
        return WebDriverSession.getWebDriverSession().getCurrentUrl();
    }

    public static void openApplication(String appName) {
    	
               
       Reporting.logReporter(Status.INFO, "openApplication"+appName ,null,true);
       WebDriverSteps nav = new WebDriverSteps();
       nav.navigateToApplication(appName);
    }
    
    public static String getApplicationURL(String appName) {
    	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
        
        JSONObject jsonFile = new JSONObject(ReadJSON.parse("Environments.json"));
        JSONObject env = jsonFile.getJSONObject(SystemProperties.EXECUTION_ENVIRONMENT);
        Reporting.logReporter(Status.DEBUG, "URL Value Returned for environment " + env+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);

        return ReadJSON.getString(env, appName);
    }
    public static String [] getApplicationURLArray(String healthcheckstring) {
    	//DEMO.TELUS_GITHUB.GOOGLE,PT140.TELUS_OMNI.GOOGLE,PT148.TELUS_OMNI.GOOGLE
    	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
        
        JSONObject jsonFile = new JSONObject(ReadJSON.parse("Environments.json"));
        String [] h = new String[SystemProperties.URL_ITERATION];
        h= healthcheckstring.split(",", 0);
        
        
        int len_h=h.length;
        String [] urlvalues = new String[SystemProperties.URL_ITERATION] ;
        String [] k = new String[SystemProperties.URL_ITERATION];
        int g = 0;
        for(int i=0;i<len_h;i++)
        {
        	
        	 String temp=h[i];
        	 k= temp.split(":", -2);
        	
        	JSONObject env = jsonFile.getJSONObject(k[0]);
        	
        	for(int j=1;j<k.length;j++)
        	{
        		 
        	    String  x= ReadJSON.getString(env, k[j]);
        	   
        	   urlvalues[g] = x;
        	   g++;
        	   
        	}
        	  
        }
        String [] urlvalues2 = new String[g+1] ;
         // DEMO.TELUS_GITHUB.GOOGLE
         // PT140.TELUS_OMNI.GOOGLE
         // PT148.TELUS_OMNI.GOOGLE
        
       
        urlvalues2=urlvalues;
      
      //  Reporting.logReporter(Status.DEBUG, "URL Value Returned for environment " + env+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);

             
        
        return urlvalues2;
        
        
        
    }
    
  
    
 public static String [] apacheHttpClientGetArray(String [] url) throws IOException 
    
    {
	 
    	
    	String result ;
    	 String [] resultvalues = new String[url.length];
    	 
    	 int len=url.length;
    	 
    	for(int j=0;j<url.length;j++)
    	{
    	 if(url[j] != null)
    	 {
    		 CloseableHttpClient httpClient = HttpClients.createDefault();
    	     HttpGet httpGet = new HttpGet(url[j]);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int status=statusLine.getStatusCode();
            
            Reporting.logReporter(Status.INFO, "Status code of the Health check"+status);
            
            if(status==404)
            {
            	
            	result="Fail";
            }else if(status==200)
            {
            	result="Pass";
            }else
            {
            	result="Fail";
            }
            
            resultvalues[j] = result; 
    	}  
    	
    	}
		return resultvalues;
       
    }
    
    
    /**
     * OBJECTIVE: Print the available sessions for debug
     */
    static void printMapSessionsAvailable() {
    	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
        
        Reporting.logReporter(Status.DEBUG, "WebDriverSteps.printMapSessionsAvailable"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);

        if (map.isEmpty()) {
        	Logging.logReporter(Status.DEBUG, "Thread ID empty");
        } else {
            Set<Entry<Long, WebDriver>> entries = map.entrySet();

            for (Entry<Long, WebDriver> entry : entries) {
                Long keyValue = entry.getKey();
                WebDriver driverValue = entry.getValue();

                Reporting.logReporter(Status.INFO, keyValue + " => " + driverValue);
            }
        }

    }

    /**
     * OBJECTIVE: Remove the current Threat Id from the map that help us to create sessions
     */
    private static void removeThreatIdFromMap() {
    	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
        
        Reporting.logReporter(Status.DEBUG, "WebDriver - Session ID removed from MAP"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);

        // printMapSessionsAvailable(); //ACTIVATE TO DEBUG

        // Remove from Map
        map.remove(Thread.currentThread().getId());


        // printMapSessionsAvailable(); //ACTIVATE TO DEBUG
    }

  

    protected String getUrl() {
        return this.url;
    }

    /**
     * OBJECTIVE: Steps to launch the driver and navigate to a url Input: name of the environment to be used. The url need to be in the com.projectname.data.Environments
     */
    private void navigateToApplication(String appName) {
    	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
        
          
    	String appUrl = "";
         
        if(appName.contains("http"))
          appUrl = appName;
        else
          appUrl = WebDriverSteps.getApplicationURL(appName);

        this.setUrl(appUrl);
        WebDriverSession.getWebDriverSession().get(this.url);

        Reporting.logReporter(Status.DEBUG, "URL opened: " + url+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);

        // HANDLE ERRORS WITH JS IF THE PROFILE TO DO IT IS NOT VALID OR BROWSER FEATURES DO NOT ALLOW IT (IE11/EDGE)
        BaseSteps.JavaScripts.handleCertificateError();
        BaseSteps.JavaScripts.handleUnsecureConnectionError();
        BaseSteps.JavaScripts.handleInvalidCertificateError();
    }

    private void setUrl(String url) {
        this.url = url;
    }

}
