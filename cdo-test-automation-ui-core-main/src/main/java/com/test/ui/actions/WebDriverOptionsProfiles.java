package com.test.ui.actions;

import java.awt.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.openqa.selenium.Platform;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.CapabilityType;

import com.aventstack.extentreports.Status;
import com.test.reporting.Reporting;
import com.test.utils.SystemProperties;

/**
 * Class that holds the the different OPTIONS Strategies to create the driver settings.
 */
public class WebDriverOptionsProfiles extends WebDriverProfilesMain {
	
	private static final String BROWSER_DOWNLOAD_FOLDER_LIST = "browser.download.folderList";
	private static final String BROWSER_DOWNLOAD_USE_DOWNLOAD_DIR = "browser.download.useDownloadDir";
	
    public static ChromeOptions chromeOptionsProfiles(String profileName) {

        // Set Global Generic Options that apply for all profiles:
        ChromeOptions globalOptionsChrome = new ChromeOptions();
        globalOptionsChrome.addArguments("--disable-notifications");
        globalOptionsChrome.addArguments("start-maximized");
        
      

        // By the usual the following options are commonly requested to be set, can be removed if not needed
        globalOptionsChrome.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.ACCEPT);
        globalOptionsChrome.setAcceptInsecureCerts(true);

        // Example for merge DesiredCapabilities --> if YOU USE THIS THE FOLLOWING MSG APPEARS --> INFO: Using `new ChromeOptions()` is preferred to `DesiredCapabilities.chrome()`

        
        switch (profileName) {

            /**
             * Return a new OPTION + add headless setting
             */
            case P_HEADLESS_ALONE:
            	Reporting.logReporter(Status.DEBUG,PROFILE_TO_USE_MESSAGE + P_HEADLESS_ALONE);
                

                // Only valid for Chrome 59+

                ChromeOptions options1 = new ChromeOptions();
                options1.addArguments(BROWSER_WINDOW_SIZE_GENERIC);
                return options1.setHeadless(true);

            /**
             * Return existing OPTION that contains GLOBAL Settings + add headless setting
             */
            case P_HEADLESS_WITH_GLOBAL_OPTIONS:
            	Reporting.logReporter(Status.DEBUG,PROFILE_TO_USE_MESSAGE + P_HEADLESS_WITH_GLOBAL_OPTIONS);
                
                // Only valid for Chrome 59+

                globalOptionsChrome.addArguments(BROWSER_WINDOW_SIZE_GENERIC);
                return globalOptionsChrome.setHeadless(true);

            /**
             * Return a new OPTION with all default settings.
             */
            case P_NA:
            	Reporting.logReporter(Status.DEBUG,PROFILE_TO_USE_MESSAGE + P_NA);
            	globalOptionsChrome.addArguments("start-maximized");
            	globalOptionsChrome.addArguments("--disable-notifications");                    

                return globalOptionsChrome;
            
            case P_REMOTE:

                
                Reporting.logReporter(Status.DEBUG, PROFILE_TO_USE_MESSAGE + P_REMOTE);
             
                
               // ChromeOptions options = new ChromeOptions();
               globalOptionsChrome.addArguments("--disable-notifications");
                globalOptionsChrome.addArguments("start-maximized");
                globalOptionsChrome.addArguments("--remote-debugging-port=9222");
                globalOptionsChrome.addArguments("--no-sandbox");
                return globalOptionsChrome;

            /**
             * Return existing OPTION that contains GLOBAL Settings
             */
            case P_GENERIC_ONE:

                Reporting.logReporter(Status.DEBUG,PROFILE_TO_USE_MESSAGE + P_GENERIC_ONE);
                return globalOptionsChrome;

            /**
             * Return existing OPTION that contains GLOBAL Settings + CUSTOM CODE
             */
            case P_GENERIC_TWO:
            	Reporting.logReporter(Status.DEBUG,PROFILE_TO_USE_MESSAGE + P_GENERIC_TWO);
                // TO BE IMPLEMENTED Add some custom generic setting
                return globalOptionsChrome;

            /**
             * Return existing OPTION that contains GLOBAL Settings + Remove InfoBar
             */
            case P_REMOVE_INFOBAR_WITH_GLOBAL_OPTIONS:

                Reporting.logReporter(Status.DEBUG,PROFILE_TO_USE_MESSAGE + P_REMOVE_INFOBAR_WITH_GLOBAL_OPTIONS);
                // How to Disable INFOBAR Chrome 74/75
                globalOptionsChrome.addArguments("disable-infobars");

                // How to Disable INFOBAR Chrome 76
                globalOptionsChrome.setExperimentalOption("excludeSwitches", new String[] { "enable-automation" });
                globalOptionsChrome.setExperimentalOption("useAutomationExtension", false); // CAREFUL WITH THIS SETTING AS IT MIGHT IMPACT OTHER STUFF SEE-->
                // https://github.com/bayandin/chromedriver/commit/22fb21bc10fbaf67e6316cbf1cc71dbf310cf7a9#diff-1a3ffafe40de9dd2731f39f136f20189

                return globalOptionsChrome;

            /**
             * Return existing OPTION that contains GLOBAL Settings + Accept setUnhandledPromptBehaviour + setAcceptInsecureCerts(true)
             */
            case P_ACCEPT_BEHAVIORS_AND_CERTIFICATES:

                Reporting.logReporter(Status.DEBUG,PROFILE_TO_USE_MESSAGE + P_ACCEPT_BEHAVIORS_AND_CERTIFICATES);
                
                globalOptionsChrome.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.ACCEPT);
                globalOptionsChrome.setAcceptInsecureCerts(true);
                return globalOptionsChrome;

            /**
             * Return existing OPTION that contains GLOBAL Settings + GIACOMAN SETTINGS
             */
            case P_GIACOMAN:
         
                Reporting.logReporter(Status.DEBUG,PROFILE_TO_USE_MESSAGE + P_GIACOMAN);
                globalOptionsChrome.addArguments("no-sandbox");
                globalOptionsChrome.setCapability("dom.forms.number", false);
                globalOptionsChrome.setCapability(ChromeOptions.CAPABILITY, globalOptionsChrome);
                globalOptionsChrome.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
                globalOptionsChrome.setCapability("chrome.switches", Arrays.asList("--incognito"));
                globalOptionsChrome.setCapability(CapabilityType.SUPPORTS_APPLICATION_CACHE, false);

                return globalOptionsChrome;

            case P_INCOGNITO:
                Reporting.logReporter(Status.DEBUG,PROFILE_TO_USE_MESSAGE + P_INCOGNITO);
            	globalOptionsChrome.addArguments("start-maximized");
            	globalOptionsChrome.addArguments("--no-sandbox");
            	globalOptionsChrome.addArguments("--incognito");

                return globalOptionsChrome;                
                
            case P_EW:
                
                Reporting.logReporter(Status.DEBUG,PROFILE_TO_USE_MESSAGE + P_EW);
                HashMap<String, Object> chromeLocalStatePrefs = new HashMap<String, Object>();
                ArrayList<String> experimentalFlags = new ArrayList<String>();
                experimentalFlags.add("same-site-by-default-cookies@2");
                experimentalFlags.add("cookies-without-same-site-must-be-secure@2");
                chromeLocalStatePrefs.put("browser.enabled_labs_experiments", experimentalFlags);
                
                globalOptionsChrome.setExperimentalOption("localState", chromeLocalStatePrefs);


                return globalOptionsChrome;
            	                
            /**
             * Return existing OPTION that contains GLOBAL Settings + Add PLATFORM & BROWSER NAME Settings
             */
            case P_WINDOWS:

                Reporting.logReporter(Status.DEBUG,PROFILE_TO_USE_MESSAGE + P_WINDOWS);
                globalOptionsChrome.setCapability(CapabilityType.PLATFORM_NAME, Platform.WINDOWS);
                globalOptionsChrome.setCapability(CapabilityType.BROWSER_NAME, BROWSER_NAME_CHROME);

                return globalOptionsChrome;

            /**
             * Return existing OPTION that contains GLOBAL Settings + Add PLATFORM & BROWSER NAME Settings
             */
            case P_WINDOWS_10:

                Reporting.logReporter(Status.DEBUG,PROFILE_TO_USE_MESSAGE + P_WINDOWS_10);
                globalOptionsChrome.setCapability(CapabilityType.PLATFORM_NAME, Platform.WIN10);
                globalOptionsChrome.setCapability(CapabilityType.BROWSER_NAME, BROWSER_NAME_CHROME);

                return globalOptionsChrome;

            /**
             * Return existing OPTION that contains GLOBAL Settings + Add PLATFORM & BROWSER NAME Settings
             */
            case P_WINDOWS_8:

                Reporting.logReporter(Status.DEBUG,PROFILE_TO_USE_MESSAGE + P_WINDOWS_8);
                globalOptionsChrome.setCapability(CapabilityType.PLATFORM_NAME, Platform.WIN8);
                globalOptionsChrome.setCapability(CapabilityType.BROWSER_NAME, BROWSER_NAME_CHROME);

                return globalOptionsChrome;

            /**
             * Return existing OPTION that contains GLOBAL Settings + Add PLATFORM & BROWSER NAME Settings
             */
            case P_WINDOWS_8_1:
           
                Reporting.logReporter(Status.DEBUG,PROFILE_TO_USE_MESSAGE + P_WINDOWS_8_1);
                globalOptionsChrome.setCapability(CapabilityType.PLATFORM_NAME, Platform.WIN8_1);
                globalOptionsChrome.setCapability(CapabilityType.BROWSER_NAME, BROWSER_NAME_CHROME);
                
                return globalOptionsChrome;

            /**
             * Return existing OPTION that contains GLOBAL Settings + Add PLATFORM & BROWSER NAME Settings
             */
            case P_WINDOWS_VISTA:

                Reporting.logReporter(Status.DEBUG,PROFILE_TO_USE_MESSAGE + P_WINDOWS_VISTA);
                globalOptionsChrome.setCapability(CapabilityType.PLATFORM_NAME, Platform.VISTA);
                globalOptionsChrome.setCapability(CapabilityType.BROWSER_NAME, BROWSER_NAME_CHROME);

                return globalOptionsChrome;

            /**
             * Return existing OPTION that contains GLOBAL Settings + Add PLATFORM & BROWSER NAME Settings
             */
            case P_WINDOWS_XP:

                Reporting.logReporter(Status.DEBUG,PROFILE_TO_USE_MESSAGE + P_WINDOWS_XP);
                
                globalOptionsChrome.setCapability(CapabilityType.PLATFORM_NAME, Platform.XP);
                globalOptionsChrome.setCapability(CapabilityType.BROWSER_NAME, BROWSER_NAME_CHROME);
                return globalOptionsChrome;

            /**
             * Return existing OPTION that contains GLOBAL Settings + Add MANUAL DOWNLOAD Settings This means that whenever a download link is hit, the BROWSER Will desplay a
             * BROWSER POP UP to confirm the download, on SAVE AS selection a OS Window will appear asking us to set the location to save
             **/
            case P_MANUAL_DOWNLOAD:
               Reporting.logReporter(Status.DEBUG,PROFILE_TO_USE_MESSAGE + P_MANUAL_DOWNLOAD);
                
                HashMap<String, Object> chromePrefs2 = new HashMap<>();
                chromePrefs2.put("download.prompt_for_download", true);


                globalOptionsChrome.setExperimentalOption("prefs", chromePrefs2);
                return globalOptionsChrome;

            /**
             * Return existing OPTION that contains GLOBAL Settings + Add MANUAL DOWNLOAD Settings This means that whenever a download link is hit, the BROWSER Will desplay a
             * BROWSER POP UP to confirm the download, on SAVE AS selection the File will be stored in Default download location
             **/
            case P_MANUAL_DOWNLOAD_PARTIAL:

                // This feature does not exist for this BROWSER
                
                Reporting.logReporter(Status.DEBUG,String.format(MSG_PROFILE_DOES_NOT_EXIST_TEMPLATE, P_MANUAL_DOWNLOAD_PARTIAL));
                
                return null;

            /**
             * Return existing OPTION that contains GLOBAL Settings + Add AUTO DOWNLOAD Settings This means that whenever a download link is hit, the BROWSER Will Store the file in
             * Default download location
             **/
            case P_AUTO_DOWNLOAD:

                Reporting.logReporter(Status.DEBUG,PROFILE_TO_USE_MESSAGE + P_AUTO_DOWNLOAD);
                
                HashMap<String, Object> chromePrefs = new HashMap<>();
                chromePrefs.put("download.prompt_for_download", false);

                chromePrefs.put("profile.default_content_settings.popups", 0);
                chromePrefs.put("download.default_directory", getAutoDownloadPath());

                globalOptionsChrome.setExperimentalOption("prefs", chromePrefs);
                return globalOptionsChrome;

            default:
                return null;
        }
    }

    public static FirefoxOptions firefoxOptionsProfiles(String profileName) {

        // Set Global Generic Options that apply for all profiles:
        FirefoxOptions globalOptionsFirefox = new FirefoxOptions();
        globalOptionsFirefox.addArguments("start-maximized");

        // By the usual the following options are commonly requested to be set, can be removed if not needed
        globalOptionsFirefox.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.ACCEPT);
        globalOptionsFirefox.setAcceptInsecureCerts(true);

        switch (profileName) {

            /**
             * Return a new OPTION + add headless setting
             */
            case P_HEADLESS_ALONE:
              
                Reporting.logReporter(Status.DEBUG,PROFILE_TO_USE_MESSAGE + P_AUTO_DOWNLOAD);
                // Only valid for Firefox 56+PROFILE_TO_USE_MESSAGE + P_HEADLESS_ALONE

                FirefoxOptions options1 = new FirefoxOptions();
                options1.addArguments("--headless");
                options1.addArguments(BROWSER_WINDOW_SIZE_GENERIC);
                return options1.setHeadless(true);

            /**
             * Return existing OPTION that contains GLOBAL Settings + add headless setting
             */
            case P_HEADLESS_WITH_GLOBAL_OPTIONS:

                
                Reporting.logReporter(Status.DEBUG,PROFILE_TO_USE_MESSAGE + P_HEADLESS_WITH_GLOBAL_OPTIONS);
                // Only valid for Firefox 56+

                globalOptionsFirefox.addArguments("--headless");
                globalOptionsFirefox.addArguments(BROWSER_WINDOW_SIZE_GENERIC);
                return globalOptionsFirefox.setHeadless(true);

            /**
             * Return a new OPTION with all default settings.
             */
            case P_NA:

                
                Reporting.logReporter(Status.DEBUG,PROFILE_TO_USE_MESSAGE + P_NA);
                

                return new FirefoxOptions();

            /**
             * Return existing OPTION that contains GLOBAL Settings
             */
            case P_GENERIC_ONE:

                Reporting.logReporter(Status.DEBUG,PROFILE_TO_USE_MESSAGE + P_GENERIC_ONE);
                return globalOptionsFirefox;

            /**
             * Return existing OPTION that contains GLOBAL Settings + CUSTOM CODE
             */
            case P_GENERIC_TWO:

                
                Reporting.logReporter(Status.DEBUG,PROFILE_TO_USE_MESSAGE + P_GENERIC_TWO);     
                // TO BE IMPLEMENTED
                return globalOptionsFirefox;

            /**
             * Return existing OPTION that contains GLOBAL Settings + Remove InfoBar
             */
            case P_REMOVE_INFOBAR_WITH_GLOBAL_OPTIONS:

                // This feature does not exist for this BROWSER.
                
                Reporting.logReporter(Status.DEBUG,String.format(MSG_PROFILE_DOES_NOT_EXIST_TEMPLATE, P_REMOVE_INFOBAR_WITH_GLOBAL_OPTIONS)); 
                return null;

            /**
             * Return existing OPTION that contains GLOBAL Settings + Accept setUnhandledPromptBehaviour + setAcceptInsecureCerts(true)
             */
            case P_ACCEPT_BEHAVIORS_AND_CERTIFICATES:
            	 Reporting.logReporter(Status.DEBUG,PROFILE_TO_USE_MESSAGE + P_ACCEPT_BEHAVIORS_AND_CERTIFICATES); 
                 


                globalOptionsFirefox.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.ACCEPT);
                globalOptionsFirefox.setAcceptInsecureCerts(true);
                return globalOptionsFirefox;

            /**
             * Return existing OPTION that contains GLOBAL Settings + GIACOMAN SETTINGS
             */
            case P_GIACOMAN:
            	 Reporting.logReporter(Status.DEBUG,String.format(MSG_PROFILE_DOES_NOT_EXIST_TEMPLATE, P_GIACOMAN)); 
                 

                // TO BE IMPLEMENTED // This feature does not exist for this BROWSER, need to be created
                return null;

            /**
             * Return existing OPTION that contains GLOBAL Settings + Add PLATFORM & BROWSER NAME Settings
             */
            case P_WINDOWS:

                Reporting.logReporter(Status.DEBUG,PROFILE_TO_USE_MESSAGE + P_WINDOWS); 
                
                globalOptionsFirefox.setCapability(CapabilityType.PLATFORM_NAME, Platform.WINDOWS);
                globalOptionsFirefox.setCapability(CapabilityType.BROWSER_NAME, BROWSER_NAME_FIREFOX);

                return globalOptionsFirefox;

            /**
             * Return existing OPTION that contains GLOBAL Settings + Add PLATFORM & BROWSER NAME Settings
             */
            case P_WINDOWS_10:

                Reporting.logReporter(Status.DEBUG,PROFILE_TO_USE_MESSAGE + P_WINDOWS_10); 
                
                globalOptionsFirefox.setCapability(CapabilityType.PLATFORM_NAME, Platform.WIN10);
                globalOptionsFirefox.setCapability(CapabilityType.BROWSER_NAME, BROWSER_NAME_FIREFOX);

                return globalOptionsFirefox;

            /**
             * Return existing OPTION that contains GLOBAL Settings + Add PLATFORM & BROWSER NAME Settings
             */
            case P_WINDOWS_8:

                Reporting.logReporter(Status.DEBUG,PROFILE_TO_USE_MESSAGE + P_WINDOWS_8); 
                
                globalOptionsFirefox.setCapability(CapabilityType.PLATFORM_NAME, Platform.WIN8);
                globalOptionsFirefox.setCapability(CapabilityType.BROWSER_NAME, BROWSER_NAME_FIREFOX);

                return globalOptionsFirefox;

            /**
             * Return existing OPTION that contains GLOBAL Settings + Add PLATFORM & BROWSER NAME Settings
             */
            case P_WINDOWS_8_1:

                Reporting.logReporter(Status.DEBUG,PROFILE_TO_USE_MESSAGE + P_WINDOWS_8_1);
                globalOptionsFirefox.setCapability(CapabilityType.PLATFORM_NAME, Platform.WIN8_1);
                globalOptionsFirefox.setCapability(CapabilityType.BROWSER_NAME, BROWSER_NAME_FIREFOX);

                return globalOptionsFirefox;

            /**
             * Return existing OPTION that contains GLOBAL Settings + Add PLATFORM & BROWSER NAME Settings
             */
            case P_WINDOWS_VISTA:

                Reporting.logReporter(Status.DEBUG,PROFILE_TO_USE_MESSAGE + P_WINDOWS_VISTA);
                
                globalOptionsFirefox.setCapability(CapabilityType.PLATFORM_NAME, Platform.VISTA);
                globalOptionsFirefox.setCapability(CapabilityType.BROWSER_NAME, BROWSER_NAME_FIREFOX);

                return globalOptionsFirefox;

            /**
             * Return existing OPTION that contains GLOBAL Settings + Add PLATFORM & BROWSER NAME Settings
             */
            case P_WINDOWS_XP:

                Reporting.logReporter(Status.DEBUG,PROFILE_TO_USE_MESSAGE + P_WINDOWS_XP);
                
                globalOptionsFirefox.setCapability(CapabilityType.PLATFORM_NAME, Platform.XP);
                globalOptionsFirefox.setCapability(CapabilityType.BROWSER_NAME, BROWSER_NAME_FIREFOX);
                return globalOptionsFirefox;

            /**
             * Return existing OPTION that contains GLOBAL Settings + Add MANUAL DOWNLOAD Settings This means that whenever a download link is hit, the BROWSER Will desplay a
             * BROWSER POP UP to confirm the download, on SAVE AS selection a OS Window will appear asking us to set the location to save
             **/
            case P_MANUAL_DOWNLOAD:

                Reporting.logReporter(Status.DEBUG,PROFILE_TO_USE_MESSAGE + P_MANUAL_DOWNLOAD);
                
                FirefoxProfile firefoxProfile2 = new FirefoxProfile();
                firefoxProfile2.setPreference(BROWSER_DOWNLOAD_FOLDER_LIST, 2);

                firefoxProfile2.setPreference(BROWSER_DOWNLOAD_USE_DOWNLOAD_DIR, false);

                globalOptionsFirefox.setProfile(firefoxProfile2);

                return globalOptionsFirefox;

            /**
             * Return existing OPTION that contains GLOBAL Settings + Add MANUAL DOWNLOAD Settings This means that whenever a download link is hit, the BROWSER Will desplay a
             * BROWSER POP UP to confirm the download, on SAVE AS selection the File will be stored in Default download location
             **/
            case P_MANUAL_DOWNLOAD_PARTIAL:

                Reporting.logReporter(Status.DEBUG,PROFILE_TO_USE_MESSAGE + P_MANUAL_DOWNLOAD_PARTIAL);
                
                FirefoxProfile firefoxProfile3 = new FirefoxProfile();
                firefoxProfile3.setPreference(BROWSER_DOWNLOAD_FOLDER_LIST, 2);

                firefoxProfile3.setPreference(BROWSER_DOWNLOAD_USE_DOWNLOAD_DIR, true);
                firefoxProfile3.setPreference("browser.download.dir", getAutoDownloadPath());
                firefoxProfile3.setPreference("browser.download.downloadDir", getAutoDownloadPath());
                firefoxProfile3.setPreference("browser.download.defaultFolder", getAutoDownloadPath());

                globalOptionsFirefox.setProfile(firefoxProfile3);
                return globalOptionsFirefox;

            /**
             * Return existing OPTION that contains GLOBAL Settings + Add AUTO DOWNLOAD Settings This means that whenever a download link is hit, the BROWSER Will Store the file in
             * Default download location
             **/
            case P_AUTO_DOWNLOAD:

                Reporting.logReporter(Status.DEBUG,PROFILE_TO_USE_MESSAGE + P_AUTO_DOWNLOAD);
                
                // See https://www.toolsqa.com/selenium-webdriver/how-to-download-files-using-selenium/ for more reference about the settings

                FirefoxProfile firefoxProfile = new FirefoxProfile();
                firefoxProfile.setPreference(BROWSER_DOWNLOAD_FOLDER_LIST, 2);
                // firefoxProfile.setPreference("browser.download.manager.showWhenStarting", false); //Not valid anymore -

                firefoxProfile.setPreference("browser.helperApps.neverAsk.saveToDisk",
                        "multipart/x-zip,application/zip,application/x-zip-compressed,application/x-compressed,application/msword,application/csv,text/csv,image/png ,image/jpeg, application/pdf, text/html,text/plain,  application/excel, application/vnd.ms-excel, application/x-excel, application/x-msexcel, application/octet-stream");
                // firefoxProfile.setPreference("browser.helperApps.neverAsk.openFile","multipart/x-zip,application/zip,application/x-zip-compressed,application/x-compressed,application/msword,application/csv,text/csv,image/png
                // ,image/jpeg, application/pdf, text/html,text/plain, application/excel, application/vnd.ms-excel, application/x-excel, application/x-msexcel,


                firefoxProfile.setPreference(BROWSER_DOWNLOAD_USE_DOWNLOAD_DIR, true);
                firefoxProfile.setPreference("browser.download.dir", getAutoDownloadPath());
                firefoxProfile.setPreference("browser.download.defaultFolder", getAutoDownloadPath());

                firefoxProfile.setPreference("browser.download.manager.closeWhenDone", true);
                firefoxProfile.setPreference("browser.download.manager.focusWhenStarting", false);

                globalOptionsFirefox.setProfile(firefoxProfile);
                return globalOptionsFirefox;

            default:
                return null;
        }
    }

    // Will retrieve a path to download.
    private static String getAutoDownloadPath() {
        String path = SystemProperties.DRIVER_AUTO_DOWNLOAD_PATH;

        if (path == null || path.isEmpty()) {
            path = System.getProperty("user.dir");
        }

        return path;
    }

    public static InternetExplorerOptions internetExplorerOptionsProfiles(String profileName) {

        // Set Global Generic Options that apply for all profiles:
        InternetExplorerOptions globalOptionsIE = new InternetExplorerOptions();

        globalOptionsIE.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
        globalOptionsIE.setCapability("dom.forms.number", false);
        globalOptionsIE.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
        globalOptionsIE.setCapability(InternetExplorerDriver.ENABLE_PERSISTENT_HOVERING, false);
        globalOptionsIE.setCapability(InternetExplorerDriver.INITIAL_BROWSER_URL, "about:blank");
        globalOptionsIE.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);

        // By the usual the following options are commonly requested to be set, can be removed if not needed
        globalOptionsIE.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.ACCEPT);

        switch (profileName) {

            /**
             * Return a new OPTION + add headless setting
             */
            case P_HEADLESS_ALONE:

                // This feature does not exist for this BROWSER
               
                Reporting.logReporter(Status.DEBUG,String.format(MSG_PROFILE_DOES_NOT_EXIST_TEMPLATE, P_HEADLESS_ALONE));
                
                
                return null;

            /**
             * Return existing OPTION that contains GLOBAL Settings + add headless setting
             */
            case P_HEADLESS_WITH_GLOBAL_OPTIONS:

                // This feature does not exist for this BROWSER
                Reporting.logReporter(Status.DEBUG,String.format(MSG_PROFILE_DOES_NOT_EXIST_TEMPLATE, P_HEADLESS_WITH_GLOBAL_OPTIONS));
                
                
                return null;

            /**
             * Return a new OPTION with all default settings.
             */
            case P_NA:
            	 Reporting.logReporter(Status.DEBUG,PROFILE_TO_USE_MESSAGE + P_NA);
                 

                return new InternetExplorerOptions();

            /**
             * Return existing OPTION that contains GLOBAL Settings
             */
            case P_GENERIC_ONE:

           	 Reporting.logReporter(Status.DEBUG,PROFILE_TO_USE_MESSAGE + P_GENERIC_ONE);
                return globalOptionsIE;

            /**
             * Return existing OPTION that contains GLOBAL Settings + CUSTOM CODE
             */
            case P_GENERIC_TWO:

           	 Reporting.logReporter(Status.DEBUG,PROFILE_TO_USE_MESSAGE + P_GENERIC_TWO);
                // TO BE IMPLEMENTED
                return globalOptionsIE;

            /**
             * Return existing OPTION that contains GLOBAL Settings + Remove InfoBar
             */
            case P_REMOVE_INFOBAR_WITH_GLOBAL_OPTIONS:

                Reporting.logReporter(Status.DEBUG,String.format(MSG_PROFILE_DOES_NOT_EXIST_TEMPLATE, P_REMOVE_INFOBAR_WITH_GLOBAL_OPTIONS));
                   
                
                // This feature does not exist for this BROWSER.
                return null;

            /**
             * Return existing OPTION that contains GLOBAL Settings + Accept setUnhandledPromptBehaviour + setAcceptInsecureCerts(true)
             */
            case P_ACCEPT_BEHAVIORS_AND_CERTIFICATES:

                
                Reporting.logReporter(Status.DEBUG,PROFILE_TO_USE_MESSAGE + P_ACCEPT_BEHAVIORS_AND_CERTIFICATES);
                
                // globalOptionsIE.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true); //Do not use, cause failure in recen driver versions
                globalOptionsIE.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.ACCEPT);
                return globalOptionsIE;

            /**
             * Return existing OPTION that contains GLOBAL Settings + GIACOMAN SETTINGS
             */
            case P_GIACOMAN:

                Reporting.logReporter(Status.DEBUG,String.format(MSG_PROFILE_DOES_NOT_EXIST_TEMPLATE, P_GIACOMAN));
                
                // TO BE IMPLEMENTED // This feature does not exist for this BROWSER, need to be created
                return null;

            /**
             * Return existing OPTION that contains GLOBAL Settings + Add PLATFORM & BROWSER NAME Settings
             */
            case P_WINDOWS:

                Reporting.logReporter(Status.DEBUG,PROFILE_TO_USE_MESSAGE + P_WINDOWS);
                
                globalOptionsIE.setCapability(CapabilityType.PLATFORM_NAME, Platform.WINDOWS);
                globalOptionsIE.setCapability(CapabilityType.BROWSER_NAME, BROWSER_NAME_IE11);

                return globalOptionsIE;

            /**
             * Return existing OPTION that contains GLOBAL Settings + Add PLATFORM & BROWSER NAME Settings
             */
            case P_WINDOWS_10:

                Reporting.logReporter(Status.DEBUG,PROFILE_TO_USE_MESSAGE + P_WINDOWS_10);
                globalOptionsIE.setCapability(CapabilityType.PLATFORM_NAME, Platform.WIN10);
                globalOptionsIE.setCapability(CapabilityType.BROWSER_NAME, BROWSER_NAME_IE11);

                return globalOptionsIE;

            /**
             * Return existing OPTION that contains GLOBAL Settings + Add PLATFORM & BROWSER NAME Settings
             */
            case P_WINDOWS_8:

                Reporting.logReporter(Status.DEBUG,PROFILE_TO_USE_MESSAGE + P_WINDOWS_8);
                
                globalOptionsIE.setCapability(CapabilityType.PLATFORM_NAME, Platform.WIN8);
                globalOptionsIE.setCapability(CapabilityType.BROWSER_NAME, BROWSER_NAME_IE11);

                return globalOptionsIE;

            /**
             * Return existing OPTION that contains GLOBAL Settings + Add PLATFORM & BROWSER NAME Settings
             */
            case P_WINDOWS_8_1:

                Reporting.logReporter(Status.DEBUG,PROFILE_TO_USE_MESSAGE + P_WINDOWS_8_1);
                
                globalOptionsIE.setCapability(CapabilityType.PLATFORM_NAME, Platform.WIN8_1);
                globalOptionsIE.setCapability(CapabilityType.BROWSER_NAME, BROWSER_NAME_IE11);

                return globalOptionsIE;

            /**
             * Return existing OPTION that contains GLOBAL Settings + Add PLATFORM & BROWSER NAME Settings
             */
            case P_WINDOWS_VISTA:

                Reporting.logReporter(Status.DEBUG,PROFILE_TO_USE_MESSAGE + P_WINDOWS_VISTA);
                
                globalOptionsIE.setCapability(CapabilityType.PLATFORM_NAME, Platform.VISTA);
                globalOptionsIE.setCapability(CapabilityType.BROWSER_NAME, BROWSER_NAME_IE11);

                return globalOptionsIE;

            /**
             * Return existing OPTION that contains GLOBAL Settings + Add PLATFORM & BROWSER NAME Settings
             */
            case P_WINDOWS_XP:

                Reporting.logReporter(Status.DEBUG,PROFILE_TO_USE_MESSAGE + P_WINDOWS_XP);
                
                globalOptionsIE.setCapability(CapabilityType.PLATFORM_NAME, Platform.XP);
                globalOptionsIE.setCapability(CapabilityType.BROWSER_NAME, BROWSER_NAME_IE11);

                return globalOptionsIE;

            /**
             * Return existing OPTION that contains GLOBAL Settings + Add MANUAL DOWNLOAD Settings This means that whenever a download link is hit, the BROWSER Will desplay a
             * BROWSER POP UP to confirm the download, on SAVE AS selection a OS Window will appear asking us to set the location to save
             **/
            case P_MANUAL_DOWNLOAD:

                // This feature does not exist for this BROWSER
                Reporting.logReporter(Status.DEBUG,String.format(MSG_PROFILE_DOES_NOT_EXIST_TEMPLATE, P_MANUAL_DOWNLOAD));
                
                return null;

            /**
             * Return existing OPTION that contains GLOBAL Settings + Add MANUAL DOWNLOAD Settings This means that whenever a download link is hit, the BROWSER Will desplay a
             * BROWSER POP UP to confirm the download, on SAVE AS selection the File will be stored in Default download location
             **/
            case P_MANUAL_DOWNLOAD_PARTIAL:

                // This feature does not exist for this BROWSER
                Reporting.logReporter(Status.DEBUG,String.format(MSG_PROFILE_DOES_NOT_EXIST_TEMPLATE, P_MANUAL_DOWNLOAD_PARTIAL));
                
                return null;

            /**
             * Return existing OPTION that contains GLOBAL Settings + Add AUTO DOWNLOAD Settings This means that whenever a download link is hit, the BROWSER Will Store the file in
             * Default download location
             **/
            case P_AUTO_DOWNLOAD:
                
                Reporting.logReporter(Status.DEBUG,String.format(MSG_PROFILE_DOES_NOT_EXIST_TEMPLATE, P_AUTO_DOWNLOAD));
                
                // This feature does not exist for this BROWSER
                return null;

            default:
                return null;
        }
    }

}
