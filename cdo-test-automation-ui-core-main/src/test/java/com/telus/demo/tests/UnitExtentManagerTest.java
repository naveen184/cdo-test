package com.telus.demo.tests;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.Status;
import com.test.reporting.ExtentManager;
import com.test.reporting.Reporting;
import com.test.utils.SystemProperties;

import junit.framework.Assert;

public class UnitExtentManagerTest {

    @AfterMethod(alwaysRun = true)
    public void afterTest() {
        // NA
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeTest() {
        // NA
    }

    @Test(groups = { "Deploy" })
    public void testGetReportPathLocationForLinux() {
    //    System.out.println("RUNNING! - UnitScreenShotsTest.testGetReportPathLocationForLinux")
        Reporting.logReporter(Status.DEBUG,"RUNNING! - UnitScreenShotsTest.testGetReportPathLocationForLinux");
        String expectedLinux = ExtentManager.DEFAULT_NOT_WINDOWS_REPORT_FILE;
        String actualLinux = ExtentManager.getReportPathLocation(org.openqa.selenium.Platform.LINUX);

      //  System.out.println("expectedLinux: " + expectedLinux)
     //   System.out.println("actualLinux: " + actualLinux)
        
        Reporting.logReporter(Status.DEBUG,"expectedLinux: " + expectedLinux);
        Reporting.logReporter(Status.DEBUG,"actualLinux: " + actualLinux);
        
        if(!SystemProperties.REPORTING_AND_SCREENSHOTS_KEEP_HISTORY) {
        	Assert.assertEquals(expectedLinux, actualLinux);
        }
        
    }

    @Test(groups = { "Deploy" })
    public void testGetReportPathLocationForMac() {
   //     System.out.println("RUNNING! - UnitScreenShotsTest.testGetReportPathLocationForMac")
        Reporting.logReporter(Status.DEBUG,"RUNNING! - UnitScreenShotsTest.testGetReportPathLocationForMac");
        String expectedMac = ExtentManager.DEFAULT_NOT_WINDOWS_REPORT_FILE;
        String actualMac = ExtentManager.getReportPathLocation(org.openqa.selenium.Platform.MAC);

               
        Reporting.logReporter(Status.DEBUG,"expectedMac: " + expectedMac);
        Reporting.logReporter(Status.DEBUG,"actualMac: " + actualMac);
        
        if(!SystemProperties.REPORTING_AND_SCREENSHOTS_KEEP_HISTORY) {
        	Assert.assertEquals(expectedMac, actualMac);
        }
    }

    @Test(groups = { "Deploy" })
    public void testGetReportPathLocationForWindows() {
      //  System.out.println("RUNNING! - UnitScreenShotsTest.testGetReportPathLocationForWindows")
        Reporting.logReporter(Status.DEBUG,"RUNNING! - UnitScreenShotsTest.testGetReportPathLocationForWindows");
        String expectedWindows = ExtentManager.DEFAULT_WINDOWS_REPORT_FILE;
        String actualWindows = ExtentManager.getReportPathLocation(org.openqa.selenium.Platform.WINDOWS);

       // System.out.println("expectedWindows: " + expectedWindows)
     //   System.out.println("actualWindows: " + actualWindows)
        Reporting.logReporter(Status.DEBUG,"expectedWindows: " + expectedWindows);
        
        Reporting.logReporter(Status.DEBUG,"actualWindows: " + actualWindows);
        
    }
}
