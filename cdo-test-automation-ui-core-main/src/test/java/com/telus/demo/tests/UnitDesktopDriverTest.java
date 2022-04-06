package com.telus.demo.tests;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.test.desktop.actions.DesktopBaseSteps;
import com.test.desktop.actions.DesktopDriverSession;
import com.test.reporting.Reporting;
import com.test.ui.actions.BaseTest;

/**
 * Class designed to validate the Functionality of the DesktopDriverSession basic methods
 */
public class UnitDesktopDriverTest extends BaseTest {

    @AfterMethod(alwaysRun = true)
    public void afterTest() {
        DesktopDriverSession.closeDesktopDriverSession();
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeTest() {
        // Load app
        DesktopDriverSession.getDesktopDriverSession(); // Will open the app
        DesktopBaseSteps.Waits.waitGeneric(5000);
    }

    @Test(groups = { "DeployTBD" })
    public void testCloseApplication() {
       // System.out.println("RUNNING! - UnitDesktopDriverTest.testCloseApplication");
        
        Reporting.logReporter(Status.DEBUG,"RUNNING! - UnitDesktopDriverTest.testCloseApplication");
        Assert.assertTrue(DesktopDriverSession.closeDesktopDriverSession(), "Desktop session not closed as expected");
    }

    @Test(groups = { "DeployTBD" })
    public void testOpenApplication() {
     //   System.out.println("RUNNING! - UnitDesktopDriverTest.testOpenApplication");
        Reporting.logReporter(Status.DEBUG,"RUNNING! - UnitDesktopDriverTest.testOpenApplication");
        String x = DesktopDriverSession.getDesktopDriverSession().getWindowHandle();
      //  System.out.println("x - " + x);
        Reporting.logReporter(Status.DEBUG,"x - " + x);
        Assert.assertNotNull(x);
        DesktopDriverSession.closeDesktopDriverSession();
    }

}
