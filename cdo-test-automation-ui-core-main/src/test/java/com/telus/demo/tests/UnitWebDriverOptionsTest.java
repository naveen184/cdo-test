package com.telus.demo.tests;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.Status;
import com.test.reporting.Reporting;
import com.test.ui.actions.BaseTest;
import com.test.ui.actions.WebDriverOptionsProfiles;

/**
 * Class designed to validate the Functionality of the Options selection is working, seems to have issues with Certain NAMES
 */
public class UnitWebDriverOptionsTest extends BaseTest {

    // COPY THE OPTIONS FROM WebDriverOptionsProfiles
    private static final String P_NA = "P_NA";
    private static final String P_GIACOMAN = "P_GIACOMAN";
    private static final String P_GENERIC_ONE = "P_GENERIC_ONE";
    private static final String P_GENERIC_TWO = "P_GENERIC_TWO";
    private static final String P_WINDOWS = "P_WINDOWS";
    private static final String P_WINDOWS_10 = "P_WINDOWS_10";
    private static final String P_WINDOWS_8 = "P_WINDOWS_8";
    private static final String P_WINDOWS_8_1 = "P_WINDOWS_8_1";
    private static final String P_WINDOWS_VISTA = "P_WINDOWS_VISTA";
    private static final String P_WINDOWS_XP = "P_WINDOWS_XP";
    private static final String P_AUTO_DOWNLOAD = "P_AUTO_DOWNLOAD";
    private static final String P_MANUAL_DOWNLOAD = "P_MANUAL_DOWNLOAD";
    private static final String P_MANUAL_DOWNLOAD_PARTIAL = "P_MANUAL_DOWNLOAD_PARTIAL";
    private static final String P_HEADLESS_ALONE = "P_HEADLESS_ALONE";
    private static final String P_HEADLESS_WITH_GLOBAL_OPTIONS = "P_HEADLESS_WITH_GLOBAL_OPTIONS";
    private static final String P_REMOVE_INFOBAR_WITH_GLOBAL_OPTIONS = "P_REMOVE_INFOBAR_WITH_GLOBAL_OPTIONS";
    private static final String P_ACCEPT_BEHAVIORS_AND_CERTIFICATES = "P_ACCEPT_BEHAVIORS_AND_CERTIFICATES";

    @AfterMethod(alwaysRun = true)
    public void afterTest() {
        setDefaultWebDriverProperties();
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
        options.add(P_GIACOMAN);
        options.add(P_GENERIC_ONE);
        options.add(P_GENERIC_TWO);
        options.add(P_WINDOWS);
        options.add(P_WINDOWS_10);
        options.add(P_WINDOWS_8);
        options.add(P_WINDOWS_8_1);
        options.add(P_WINDOWS_VISTA);
        options.add(P_WINDOWS_XP);
        options.add(P_AUTO_DOWNLOAD);
        options.add(P_MANUAL_DOWNLOAD);
        options.add(P_MANUAL_DOWNLOAD_PARTIAL);
        options.add(P_HEADLESS_ALONE);
        options.add(P_HEADLESS_WITH_GLOBAL_OPTIONS);
        options.add(P_REMOVE_INFOBAR_WITH_GLOBAL_OPTIONS);
        options.add(P_ACCEPT_BEHAVIORS_AND_CERTIFICATES);
        return options;
    }

    @Test(groups = { "Deploy" })
    public void testInvalidOptions() {
        Reporting.logReporter(Status.DEBUG,"RUNNING! - UnitWebDriverOptionsTest.testInvalidOptions");
        
        Assert.assertFalse(this.validateProfile("CHROME", "NOT_VALID_PROFILE"));
    }

    @Test(groups = { "Deploy" })
    public void testValidOptions() {
        Reporting.logReporter(Status.DEBUG,"RUNNING! - UnitWebDriverOptionsTest.testValidOptions");
        
        List<String> options = this.getOptionsList();
        SoftAssert softAssert = new SoftAssert();

        for (String e : options) {
            softAssert.assertTrue(this.validateProfile("CHROME", e));
        }

        softAssert.assertAll();
    }

    private boolean validateProfile(String browserName, String browserProfile) {
        setOverrideWebDriverProperties(browserName, browserProfile);

        // VALIDATE CHROME DATA ----------------------
        if (browserProfile.equalsIgnoreCase(P_MANUAL_DOWNLOAD_PARTIAL)) { // SKIP as this profile is expected to return NULL as is not supported.
            return true;
        }

        ChromeOptions chromeOptions = WebDriverOptionsProfiles.chromeOptionsProfiles(browserProfile);

        if (chromeOptions == null) {
           
            Reporting.logReporter(Status.DEBUG,"\n" + "NULL is Expected for NEGATIVE scenario with Profile: NOT_VALID_PROFILE , and current PROFILE IS: [" + browserProfile + "]");
            
            return false;
        }

        return true;
    }

}
