package com.test.reporting;

import java.util.HashMap;
import java.util.Map;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.test.utils.SystemProperties;

//ExtentTestManager saves each instance of the extent report on a Map variable
public class ExtentTestManager {
	
	private ExtentTestManager() {
		    throw new IllegalStateException("Utility class");
		  }

    private static Map extentTestMap = new HashMap();
    private static ExtentReports extent = ExtentManager.getReporter();

    // Returns test of current thread
    public static synchronized ExtentTest getTest() {
	return (ExtentTest) extentTestMap.get((int) (long) (Thread.currentThread().getId()));
    }

    // Ends test
    public static synchronized void endTest() {
	ExtentManager.getReporter().flush();
	if(SystemProperties.REPORT_MULTIPLE==(true))
	{
		ExtentManager.extent=null;
		extent = ExtentManager.getReporter();
	}
    }

    // Starts test of current thread and saves thread in map
    public static synchronized ExtentTest startTest(String testName, String desc) {
	ExtentTest test = extent.createTest(testName, desc);
	extentTestMap.put((int) (long) (Thread.currentThread().getId()), test);
	return test;
    }

}