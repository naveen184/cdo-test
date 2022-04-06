package com.test.reporting;

import java.util.HashMap;
import java.util.List;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.Status;
import com.test.logging.Logging;
import com.test.sdlc.SDLCTools;
import com.test.sdlc.jira.JiraProperties;
import com.test.utils.SystemProperties;

/**
 * Lister to intercept test Method calls
 */
public class TestMethodListener implements IInvokedMethodListener {

    // Retrieve Connection and parameters from JSON
    private static final boolean JIRA_CONNECTION_ENABLE = JiraProperties.getIsConnectionEnable();
    private static final boolean JIRA_ATTACHMENTS_ENABLE = JiraProperties.getIsAttachmentsEnable();

    protected static HashMap<Long, String> mapStatusForLog = new HashMap<>();

    // IDS TO BE READ BY THE REPORTER METHOD, BASED IN THE FLOW, THIS WILL BE POPULATED BEFORE TEH GETTER METHOD REQUIRE IT
    private static String jiraId = null;

    public static String getJiraId() {
        return jiraId;
    }

    /**
     * OBJECTIVE: Add status for the threat to the map, to be use later to print in the report for (BEFORE LOGS)
     */
    private static void updateMapStatus(boolean containsBeforeAnnotation) {


        String convertion = null;
        if (containsBeforeAnnotation) {
            convertion = "true";
        } else {
            convertion = "false";
        }

        if (mapStatusForLog.size() == 0) { // Create New

            mapStatusForLog.put(Thread.currentThread().getId(), convertion);

        } else if (mapStatusForLog.get(Thread.currentThread().getId()) == null) {

            mapStatusForLog.put(Thread.currentThread().getId(), convertion);

        } else {// Update an existing one

            mapStatusForLog.replace(Thread.currentThread().getId(), convertion);
            if (!containsBeforeAnnotation) { // Print log and clear list
                Reporting.printToReportBeforeStepsLog();
            }
        }
    }

    @Override
    public void afterInvocation(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {

        if (JIRA_CONNECTION_ENABLE && JIRA_ATTACHMENTS_ENABLE && SystemProperties.EXTENT_REPORT) {
            Reporting.logReporter(Status.DEBUG, "TestMethodListener.afterInvocation --> Logic call after @Test Invocation");

            this.extractJiraId(iInvokedMethod, iTestResult, false);
        }

        if (iTestResult.getStatus() == ITestResult.SUCCESS) {
            // TO BE IMPLEMENTED


        } else if (iTestResult.getStatus() == ITestResult.SKIP) {
            // TO BE IMPLEMENTED


        } else if (iTestResult.getStatus() == ITestResult.FAILURE) {
            // TO BE IMPLEMENTED

        } else {
            // UNMMAPED STATUS
            // TO BE IMPLEMENTED

        }

    }

    @Override
    public void beforeInvocation(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {


        String testClassName = iInvokedMethod.getTestMethod().getTestClass().getName();
        String testMethodName = iInvokedMethod.getTestMethod().getMethodName();

        Logging.logReporter(Status.DEBUG, "Class & Method name : "+ testClassName +"." + testMethodName );
        String beforeAnnotationExpected = "@org.testng.annotations.BeforeMethod";

        boolean containsBeforeAnnotation = false;
        for (String a : this.getAnnotationsInCurrentMethod(iInvokedMethod, iTestResult)) {

            if (a.startsWith(beforeAnnotationExpected)) {
                containsBeforeAnnotation = true;
                break;
            }
        }

        if (containsBeforeAnnotation) {
            updateMapStatus(true);
        } else {
            updateMapStatus(false);
        }

        if (iTestResult.getStatus() == ITestResult.SUCCESS) {
            // TO BE IMPLEMENTED


        } else if (iTestResult.getStatus() == ITestResult.SKIP) {
            // TO BE IMPLEMENTED


        } else if (iTestResult.getStatus() == ITestResult.FAILURE) {
            // TO BE IMPLEMENTED

        } else {
            // UNMMAPED STATUS
            // TO BE IMPLEMENTED

        }
    }

    /**
     * Look for the custom SDLCTool annotation, in the Class or Method to proceed to do an action with the same.
     */
    private synchronized void extractJiraId(IInvokedMethod iInvokedMethod, ITestResult iTestResult, boolean isAnnotationInClass) {
        SDLCTools sdlcTools = CustomAnnotationHelper.getSDLCToolsAnnotation(iInvokedMethod, isAnnotationInClass);

        if (sdlcTools != null) {
            String methodName = iTestResult.getMethod().getConstructorOrMethod().getName();
            String id = sdlcTools.jiraId();

            if (id == null || id.isEmpty()) {
                Reporting.logReporter(Status.DEBUG, "TestMethodListener - TestMethod: " + methodName + "  - With Jira ID Located - but is Empty - No action to do");
            } else {
                Reporting.logReporter(Status.DEBUG, "TestMethodListener - TestMethod: " + methodName + " - with jira id Related: " + id);
                jiraId = id;
            }

        } 
    }

    /**
     * List all annotation of the current method
     */
    private List<String> getAnnotationsInCurrentMethod(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {
    	Logging.logReporter(Status.DEBUG, "getAnnotationsInCurrentMethod iTestResult Success status is : "+ iTestResult.isSuccess());
        return CustomAnnotationHelper.getAnnotationsList(iInvokedMethod, false);
    }

    /**
     * Print all annotation of the current method for debug
     */
    void printAnnotationsInCurrentMethod(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {

        for (String a : this.getAnnotationsInCurrentMethod(iInvokedMethod, iTestResult)) {
        	
        	 Reporting.logReporter(Status.DEBUG, "Annotation: " + a);
             

        }
    }
}
