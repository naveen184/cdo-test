package com.test.ui.actions;

import java.lang.reflect.Method;
import java.util.Map;

import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Listeners;

import com.aventstack.extentreports.Status;
import com.test.desktop.actions.DesktopDriverSession;
import com.test.reporting.Reporting;
import com.test.reporting.TestListener;

/**
 * Class designed to be the SUPER class of all the test classes
 *
 */
@Listeners({ com.test.reporting.TestListener.class, com.test.reporting.TestMethodListener.class, com.test.listeners.ReportPortalListener.class })
public class BaseTest extends TestListener {

    // Variables used for Data Provider
    protected static final String VARIABLE_CONTEXT_TEST_NAME = "testName";
    protected static final String DATA_PROVIDER_TEST_NAME_TEMPLATE = "_DPScenarioData:";

    // Override WebDriver Variables - to be use in single threat
    private static String overrideWebBrowser = null;
    private static String overrideWebOptions = null;

    // Override DesktopDriver Variables - to be use in single threat
    private static String overrideAppName = null;
    private static String overrideAppOptions = null;

    private static String overrideAppPath = null;
    private static String overrideAppDetailProcessName = null;

    // TDM Variables
    protected JSONObject testData = new JSONObject();
    protected JSONObject tdmData = new JSONObject();

    /**
     * Used by TestListenerClass and also standard methods(Steps)
     *
     */
    public WebDriver getDesktopDriver() {
        return DesktopDriverSession.getDesktopDriverSession();
    }

    /**
     * Used by TestListenerClass and also standard methods(Steps)
     */
    public WebDriver getDriver() {
        return WebDriverSession.getWebDriverSession();
    }

    public static String getOverrideAppDetailProcessName() {
        return overrideAppDetailProcessName;
    }

    public static String getOverrideAppName() {
        return overrideAppName;
    }

    public static String getOverrideAppOptions() {
        return overrideAppOptions;
    }

    public static String getOverrideAppPath() {
        return overrideAppPath;
    }

    public static String getOverrideWebBrowser() {
        return overrideWebBrowser;
    }

    public static String getOverrideWebOptions() {
        return overrideWebOptions;
    }

    /**
     * Set all DESKTOP DRIVER properties to default (NULL) so the data is pulled from the properties file
     */
    protected static void setDefaultDesktopDriverProperties() {
        BaseTest.setOverrideDesktopDriverProperties(null, null, null, null);
    }

    /**
     * Set all WEB DRIVER properties to default (NULL) so the data is pulled from the properties file
     */
    protected static void setDefaultWebDriverProperties() {
        BaseTest.setOverrideWebDriverProperties(null, null);
    }

    /**
     * Will set the Browser to use on run Time, should be placed in the BEFORE method call and will Override the system.properties value. Designed for Single Threat Scenarios.
     */
    protected static void setOverrideDesktopDriverProperties(String appName, String appOptions, String appPath, String appDetailProcessName) {
        String warningMsg = "WARNING! DesktopDriver OVERRIDE on run time, once method is used replace it back to the NULL Value..should be use only in Single Threat";
        Reporting.logReporter(Status.INFO, warningMsg);


        overrideAppName = appName;
        overrideAppOptions = appOptions;
        overrideAppPath = appPath;
        overrideAppDetailProcessName = appDetailProcessName;
    }

    /**
     * Will set the Browser to use on run Time, should be placed in the BEFORE method call and will Override the system.properties value. Designed for Single Threat Scenarios.
     */
    protected static void setOverrideWebDriverProperties(String browserName, String optionsValue) {
        String warningMsg = "WARNING! WebDriver OVERRIDE on run time, once method is used replace it back to the NULL Value..should be use only in Single Threat";
        Reporting.logReporter(Status.INFO, warningMsg);


        overrideWebBrowser = browserName;
        overrideWebOptions = optionsValue;
    }

    /**
     * Method for data provider renaming.
     */
    protected String getDataProviderTestNameForIterationData(Method method, Object[] testData) {
        String HEADER_TEST_ID = "TEST_ID";
        String HEADER_ENVIRONMENT_SUPPORTED = "ENVIRONMENT_SUPPORTED";

        if (testData.length > 0) {
            String newTestName = null;

            if (testData.length == 1) {// The Map version only retrieves a single data length
                Map mapData = (Map) testData[0];
                String testName = (String) mapData.get(HEADER_TEST_ID);
                String environmentName = (String) mapData.get(HEADER_ENVIRONMENT_SUPPORTED);
                String scenarioName = testName + "_" + environmentName;
                newTestName = method.getName() + DATA_PROVIDER_TEST_NAME_TEMPLATE + scenarioName;
            }

            else {// The Objects approach returns as many inputs are provided
                StringBuilder dataProviderListBuilder = new StringBuilder();
                dataProviderListBuilder.append("{");

                for (Object obj : testData) {
                    dataProviderListBuilder.append(obj.toString() + ", ");
                }

                dataProviderListBuilder.append("}");
                String dataProviderList = dataProviderListBuilder.toString();

                dataProviderList = dataProviderList.replace(", }", "}");
                newTestName = method.getName() + DATA_PROVIDER_TEST_NAME_TEMPLATE + dataProviderList;
            }

            return newTestName;
        }

        else { // Standard call (No data Provider)
            return method.getName();
        }
    }

}
