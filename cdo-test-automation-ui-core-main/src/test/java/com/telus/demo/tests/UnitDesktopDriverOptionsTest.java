package com.telus.demo.tests;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.winium.DesktopOptions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.Status;
import com.test.desktop.actions.DesktopDriverOptionsProfiles;
import com.test.reporting.Reporting;
import com.test.ui.actions.BaseTest;

/**
 * Class designed to validate the Functionality of the Options selection is working, seems to have issues with Certain NAMES
 */
public class UnitDesktopDriverOptionsTest extends BaseTest {

    // COPY THE OPTIONS FROM DesktopDriverOptionsProfiles
    private static final String P_NA = "P_NA";
    private static final String P_APP_KEYBOARD_BASED_ON_WINDOWS = "P_APP_KEYBOARD_BASED_ON_WINDOWS";
    private static final String P_APP_KEYBOARD_BASED_ON_SIMULATOR_LIB = "P_APP_KEYBOARD_BASED_ON_SIMULATOR_LIB";
    private static final String P_APP_PATH = "P_APP_PATH";
    private static final String P_APP_ARGUMENTS = "P_APP_ARGUMENTS";
    private static final String P_APP_DEBUG_CONNECT_TO_RUNNING_APP = "P_APP_DEBUG_CONNECT_TO_RUNNING_APP";
    private static final String P_APP_WITH_LAUNCH_DELAY = "P_APP_WITH_LAUNCH_DELAY";

    @AfterMethod(alwaysRun = true)
    public void afterTest() {
        setDefaultDesktopDriverProperties();
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeTest() {
        // NA
    }

    /**
     * Get all options (copy the data from the WebDriverOptionsProfiles.class)
     */
    private List<String> getOptionsList() {
        List<String> options = new ArrayList<>();
        options.add(P_NA);
        options.add(P_APP_KEYBOARD_BASED_ON_WINDOWS);
        options.add(P_APP_KEYBOARD_BASED_ON_SIMULATOR_LIB);
        options.add(P_APP_PATH);
        options.add(P_APP_ARGUMENTS);
        options.add(P_APP_DEBUG_CONNECT_TO_RUNNING_APP);
        options.add(P_APP_WITH_LAUNCH_DELAY);
        return options;
    }

    @Test(groups = { "Deploy" })
    public void testInvalidOptions() {
       Reporting.logReporter(Status.DEBUG,"RUNNING! - UnitDesktopDriverOptionsTest.testInvalidOptions");
        Assert.assertFalse(this.validateProfile("NOT_VALID_PROFILE"));
    }

    @Test(groups = { "Deploy" })
    public void testValidOptions() {
        Reporting.logReporter(Status.DEBUG,"RUNNING! - UnitDesktopDriverOptionsTest.testValidOptions");
        List<String> options = this.getOptionsList();
        SoftAssert softAssert = new SoftAssert();

        for (String e : options) {
            softAssert.assertTrue(this.validateProfile(e));
        }

        softAssert.assertAll();
    }

    private boolean validateProfile(String desktopOptionsProfile) {
        setOverrideDesktopDriverProperties(null, desktopOptionsProfile, null, null);

        DesktopOptions options = DesktopDriverOptionsProfiles.desktopOptionsProfiles(desktopOptionsProfile);

        if (options == null) {
           Reporting.logReporter(Status.DEBUG,"\n" + "NULL is Expected for NEGATIVE scenario with Profile: [" + desktopOptionsProfile + "]");  
            
            return false;
        }

        return true;
    }

}
