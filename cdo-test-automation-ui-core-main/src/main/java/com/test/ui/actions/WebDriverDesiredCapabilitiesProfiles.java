package com.test.ui.actions;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;

import com.aventstack.extentreports.Status;

import com.test.reporting.Reporting;
import com.test.utils.SystemProperties;

/**
 * @deprecated  reason this method is deprecated <br/>
 *              {will be removed in next version} <br/>
 *              Class that holds the DESIREDCAPABILITIES Strategies to create the driver settings
 */
@Deprecated
public class WebDriverDesiredCapabilitiesProfiles extends WebDriverProfilesMain {

	private static final String ACCEPT_MSG = "ACCEPT";
	
	
    public static DesiredCapabilities chromeOptionsProfiles(String profileName) {

        // Set Global Generic Options that apply for all profiles:
        DesiredCapabilities globalCapabilitiesChrome = DesiredCapabilities.chrome();
        ChromeOptions globalOptionsChrome = new ChromeOptions();

        globalOptionsChrome.addArguments("test-type","--disable-notifications", "start-maximized", "--lang=en");
        globalCapabilitiesChrome.setCapability(ChromeOptions.CAPABILITY, globalOptionsChrome);

        // By the usual the following options are commonly requested to be set, can be removed if not needed
        globalCapabilitiesChrome.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
        globalCapabilitiesChrome.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, ACCEPT_MSG);

        switch (profileName) {

            /**
             * Return a new OPTION + add headless setting
             */
            case P_HEADLESS_ALONE:
            	 Reporting.logReporter(Status.DEBUG, PROFILE_TO_USE_MESSAGE + P_HEADLESS_ALONE);

                // Only valid for Chrome 59+
                // TO BE IMPLEMENTED
                return globalCapabilitiesChrome;

            /**
             * Return existing OPTION that contains GLOBAL Settings + add headless setting
             */
            case P_HEADLESS_WITH_GLOBAL_OPTIONS:

                Reporting.logReporter(Status.DEBUG, PROFILE_TO_USE_MESSAGE + P_HEADLESS_WITH_GLOBAL_OPTIONS);
                // Only valid for Chrome 59+
                // TO BE IMPLEMENTED
                return globalCapabilitiesChrome;

            /**
             * Return a new OPTION with all default settings.
             */
            case P_NA:

                
                Reporting.logReporter(Status.DEBUG, PROFILE_TO_USE_MESSAGE + P_NA);
                return DesiredCapabilities.chrome();

            /**
             * Return existing OPTION that contains GLOBAL Settings
             */
                
             case P_REMOTE:

                
                Reporting.logReporter(Status.DEBUG, PROFILE_TO_USE_MESSAGE + P_REMOTE);
             
                
               // ChromeOptions options = new ChromeOptions();
                globalOptionsChrome.addArguments("--disable-notifications");
                globalOptionsChrome.addArguments("start-maximized");
                globalOptionsChrome.addArguments("--remote-debugging-port=9222");
                globalOptionsChrome.addArguments("--no-sandbox");
                return globalCapabilitiesChrome;

            /**
             * Return existing OPTION that contains GLOBAL Settings
             */
            case P_GENERIC_ONE:

                
                Reporting.logReporter(Status.DEBUG, PROFILE_TO_USE_MESSAGE + P_GENERIC_ONE);
                // TO BE IMPLEMENTED
                return globalCapabilitiesChrome;

            /**
             * Return existing OPTION that contains GLOBAL Settings + CUSTOM CODE
             */
            case P_GENERIC_TWO:
            	  Reporting.logReporter(Status.DEBUG, PROFILE_TO_USE_MESSAGE + P_GENERIC_TWO);

                // TO BE IMPLEMENTED
                return globalCapabilitiesChrome;

            /**
             * Return existing OPTION that contains GLOBAL Settings + Remove InfoBar
             */
            case P_REMOVE_INFOBAR_WITH_GLOBAL_OPTIONS:
            	 Reporting.logReporter(Status.DEBUG, PROFILE_TO_USE_MESSAGE + P_REMOVE_INFOBAR_WITH_GLOBAL_OPTIONS);

                // TO BE IMPLEMENTED
                return globalCapabilitiesChrome;

            /**
             * Return existing OPTION that contains GLOBAL Settings + Accept setUnhandledPromptBehaviour + setAcceptInsecureCerts(true)
             */
            case P_ACCEPT_BEHAVIORS_AND_CERTIFICATES:

                
                Reporting.logReporter(Status.DEBUG, PROFILE_TO_USE_MESSAGE + P_ACCEPT_BEHAVIORS_AND_CERTIFICATES);   
                globalCapabilitiesChrome.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
                globalCapabilitiesChrome.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, ACCEPT_MSG);
                return globalCapabilitiesChrome;

            /**
             * Return existing OPTION that contains GLOBAL Settings + GIACOMAN SETTINGS
             */
            case P_GIACOMAN:

                
                Reporting.logReporter(Status.DEBUG, PROFILE_TO_USE_MESSAGE + P_GIACOMAN);   
                
                // TO BE IMPLEMENTED
                return globalCapabilitiesChrome;

            /**
             * Return existing OPTION that contains GLOBAL Settings + Add PLATFORM & BROWSER NAME Settings
             */
            case P_WINDOWS:

                
                Reporting.logReporter(Status.DEBUG, PROFILE_TO_USE_MESSAGE + P_WINDOWS);   
                
                // TO BE IMPLEMENTED
                return globalCapabilitiesChrome;

            /**
             * Return existing OPTION that contains GLOBAL Settings + Add PLATFORM & BROWSER NAME Settings
             */
            case P_WINDOWS_10:

                
                Reporting.logReporter(Status.DEBUG, PROFILE_TO_USE_MESSAGE + P_WINDOWS_10);   
                // TO BE IMPLEMENTED
                return globalCapabilitiesChrome;

            /**
             * Return existing OPTION that contains GLOBAL Settings + Add PLATFORM & BROWSER NAME Settings
             */
            case P_WINDOWS_8:

                
                Reporting.logReporter(Status.DEBUG, PROFILE_TO_USE_MESSAGE + P_WINDOWS_8);   
                
                // TO BE IMPLEMENTED
                return globalCapabilitiesChrome;

            /**
             * Return existing OPTION that contains GLOBAL Settings + Add PLATFORM & BROWSER NAME Settings
             */
            case P_WINDOWS_8_1:

                
                Reporting.logReporter(Status.DEBUG, PROFILE_TO_USE_MESSAGE + P_WINDOWS_8_1); 
                // TO BE IMPLEMENTED
                return globalCapabilitiesChrome;

            /**
             * Return existing OPTION that contains GLOBAL Settings + Add PLATFORM & BROWSER NAME Settings
             */
            case P_WINDOWS_VISTA:

                
                Reporting.logReporter(Status.DEBUG, PROFILE_TO_USE_MESSAGE + P_WINDOWS_VISTA); 
                // TO BE IMPLEMENTED
                return globalCapabilitiesChrome;

            /**
             * Return existing OPTION that contains GLOBAL Settings + Add PLATFORM & BROWSER NAME Settings
             */
            case P_WINDOWS_XP:
            	
            	   Reporting.logReporter(Status.DEBUG, PROFILE_TO_USE_MESSAGE + P_WINDOWS_XP); 

                // TO BE IMPLEMENTED
                return globalCapabilitiesChrome;

            /**
             * Return existing OPTION that contains GLOBAL Settings + Add MANUAL DOWNLOAD Settings This means that whenever a download link is hit, the BROWSER Will desplay a
             * BROWSER POP UP to confirm the download, on SAVE AS selection a OS Window will appear asking us to set the location to save
             **/
            case P_MANUAL_DOWNLOAD:
            	
            	 Reporting.logReporter(Status.DEBUG, PROFILE_TO_USE_MESSAGE + P_MANUAL_DOWNLOAD);

                // TO BE IMPLEMENTED
                return globalCapabilitiesChrome;

            /**
             * Return existing OPTION that contains GLOBAL Settings + Add MANUAL DOWNLOAD Settings This means that whenever a download link is hit, the BROWSER Will desplay a
             * BROWSER POP UP to confirm the download, on SAVE AS selection the File will be stored in Default download location
             **/
            case P_MANUAL_DOWNLOAD_PARTIAL:
            	
            	 Reporting.logReporter(Status.DEBUG, String.format(MSG_PROFILE_DOES_NOT_EXIST_TEMPLATE, P_MANUAL_DOWNLOAD_PARTIAL));

                // This feature does not exist for this BROWSER
                return null;

            /**
             * Return existing OPTION that contains GLOBAL Settings + Add AUTO DOWNLOAD Settings This means that whenever a download link is hit, the BROWSER Will Store the file in
             * Default download location
             **/
            case P_AUTO_DOWNLOAD:
            	
            	 Reporting.logReporter(Status.DEBUG, PROFILE_TO_USE_MESSAGE + P_AUTO_DOWNLOAD);

                // TO BE IMPLEMENTED
                return globalCapabilitiesChrome;

            default:
                return null;
        }
    }

    public static DesiredCapabilities firefoxOptionsProfiles(String profileName) {

    	final String BROWSER_DOWNLOAD_FOLDER_LIST = "browser.download.folderList";
    	final String BROWSER_DOWNLOAD_USE_DOWNLOAD_DIR = "browser.download.useDownloadDir";
    	final String BROWSER_DOWNLOAD_DIR = "browser.download.dir";
    	final String BROWSER_DOWNLOAD_DEFAULT_FOLDER = "browser.download.defaultFolder";
        // Set Global Generic Options that apply for all profiles:
        FirefoxProfile globalProfileFirefox = new FirefoxProfile();
        DesiredCapabilities globalCapabilitiesFirefox = DesiredCapabilities.firefox();

        // By the usual the following options are commonly requested to be set, can be removed if not needed


        // Final action to pass the profile data to the capabilities
        globalCapabilitiesFirefox.setCapability(FirefoxDriver.PROFILE, globalProfileFirefox);

        // By the usual the following options are commonly requested to be set, can be removed if not needed
        globalCapabilitiesFirefox.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
        globalCapabilitiesFirefox.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, ACCEPT_MSG);

        switch (profileName) {

            /**
             * Return a new OPTION + add headless setting
             */
            case P_HEADLESS_ALONE:
            	 Reporting.logReporter(Status.DEBUG, PROFILE_TO_USE_MESSAGE + P_HEADLESS_ALONE);

                // Only valid for Firefox 56+

                DesiredCapabilities capabilities2 = DesiredCapabilities.firefox();
                // TO BE IMPLEMENTED

                return capabilities2;

            /**
             * Return existing OPTION that contains GLOBAL Settings + add headless setting
             */
            case P_HEADLESS_WITH_GLOBAL_OPTIONS:
            	 Reporting.logReporter(Status.DEBUG, PROFILE_TO_USE_MESSAGE + P_HEADLESS_WITH_GLOBAL_OPTIONS);

                // Only valid for Firefox 56+
                // TO BE IMPLEMENTED

                // Final action to pass the profile data to the capabilities
                globalCapabilitiesFirefox.setCapability(FirefoxDriver.PROFILE, globalProfileFirefox);
                return globalCapabilitiesFirefox;

            /**
             * Return a new OPTION with all default settings.
             */
            case P_NA:
           	 Reporting.logReporter(Status.DEBUG, PROFILE_TO_USE_MESSAGE + P_NA);
             

                return DesiredCapabilities.firefox();

            /**
             * Return existing OPTION that contains GLOBAL Settings
             */
                
  

            case P_GENERIC_ONE:
            	 Reporting.logReporter(Status.DEBUG, PROFILE_TO_USE_MESSAGE + P_GENERIC_ONE);

                // TO BE IMPLEMENTED

                // Final action to pass the profile data to the capabilities
                globalCapabilitiesFirefox.setCapability(FirefoxDriver.PROFILE, globalProfileFirefox);
                return globalCapabilitiesFirefox;

            /**
             * Return existing OPTION that contains GLOBAL Settings + CUSTOM CODE
             */
            case P_GENERIC_TWO:
            	 Reporting.logReporter(Status.DEBUG, PROFILE_TO_USE_MESSAGE + P_GENERIC_TWO);

                // TO BE IMPLEMENTED

                // Final action to pass the profile data to the capabilities
                globalCapabilitiesFirefox.setCapability(FirefoxDriver.PROFILE, globalProfileFirefox);
                return globalCapabilitiesFirefox;

            /**
             * Return existing OPTION that contains GLOBAL Settings + Remove InfoBar
             */
            case P_REMOVE_INFOBAR_WITH_GLOBAL_OPTIONS:
            	// This feature does not exist for this BROWSER.
                
                Reporting.logReporter(Status.DEBUG, String.format(MSG_PROFILE_DOES_NOT_EXIST_TEMPLATE, P_REMOVE_INFOBAR_WITH_GLOBAL_OPTIONS));
                return null;

            /**
             * Return existing OPTION that contains GLOBAL Settings + Accept setUnhandledPromptBehaviour + setAcceptInsecureCerts(true)
             */
            case P_ACCEPT_BEHAVIORS_AND_CERTIFICATES:

                Reporting.logReporter(Status.DEBUG, PROFILE_TO_USE_MESSAGE + P_ACCEPT_BEHAVIORS_AND_CERTIFICATES);
                
                globalCapabilitiesFirefox.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
                globalCapabilitiesFirefox.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, ACCEPT_MSG);
                return globalCapabilitiesFirefox;

            /**
             * Return existing OPTION that contains GLOBAL Settings + GIACOMAN SETTINGS
             */
            case P_GIACOMAN:
            	
            	 Reporting.logReporter(Status.DEBUG, String.format(MSG_PROFILE_DOES_NOT_EXIST_TEMPLATE, P_GIACOMAN));
                 

                // TO BE IMPLEMENTED // This feature does not exist for this BROWSER, need to be created
                return null;

            /**
             * Return existing OPTION that contains GLOBAL Settings + Add PLATFORM & BROWSER NAME Settings
             */
            case P_WINDOWS:

                Reporting.logReporter(Status.DEBUG, PROFILE_TO_USE_MESSAGE + P_WINDOWS);
                
                // TO BE IMPLEMENTED
                return globalCapabilitiesFirefox;

            /**
             * Return existing OPTION that contains GLOBAL Settings + Add PLATFORM & BROWSER NAME Settings
             */
            case P_WINDOWS_10:

                
                Reporting.logReporter(Status.DEBUG, PROFILE_TO_USE_MESSAGE + P_WINDOWS_10);
                // TO BE IMPLEMENTED
                return globalCapabilitiesFirefox;

            /**
             * Return existing OPTION that contains GLOBAL Settings + Add PLATFORM & BROWSER NAME Settings
             */
            case P_WINDOWS_8:
            	 Reporting.logReporter(Status.DEBUG, PROFILE_TO_USE_MESSAGE + P_WINDOWS_8);

                // TO BE IMPLEMENTED
                return globalCapabilitiesFirefox;

            /**
             * Return existing OPTION that contains GLOBAL Settings + Add PLATFORM & BROWSER NAME Settings
             */
            case P_WINDOWS_8_1:

                Reporting.logReporter(Status.DEBUG, PROFILE_TO_USE_MESSAGE + P_WINDOWS_8_1);
                // TO BE IMPLEMENTED
                return globalCapabilitiesFirefox;

            /**
             * Return existing OPTION that contains GLOBAL Settings + Add PLATFORM & BROWSER NAME Settings
             */
            case P_WINDOWS_VISTA:

                Reporting.logReporter(Status.DEBUG, PROFILE_TO_USE_MESSAGE + P_WINDOWS_VISTA);
                // TO BE IMPLEMENTED
                return globalCapabilitiesFirefox;

            /**
             * Return existing OPTION that contains GLOBAL Settings + Add PLATFORM & BROWSER NAME Settings
             */
            case P_WINDOWS_XP:

                Reporting.logReporter(Status.DEBUG, PROFILE_TO_USE_MESSAGE + P_WINDOWS_XP);
                // TO BE IMPLEMENTED
                return globalCapabilitiesFirefox;

            /**
             * Return existing OPTION that contains GLOBAL Settings + Add MANUAL DOWNLOAD Settings This means that whenever a download link is hit, the BROWSER Will desplay a
             * BROWSER POP UP to confirm the download, on SAVE AS selection a OS Window will appear asking us to set the location to save
             **/
            case P_MANUAL_DOWNLOAD:

                Reporting.logReporter(Status.DEBUG, PROFILE_TO_USE_MESSAGE + P_MANUAL_DOWNLOAD);
                // TO BE IMPLEMENTED
                globalProfileFirefox.setPreference(BROWSER_DOWNLOAD_FOLDER_LIST, 2);

                globalProfileFirefox.setPreference(BROWSER_DOWNLOAD_USE_DOWNLOAD_DIR , false);
                globalProfileFirefox.setPreference(BROWSER_DOWNLOAD_DIR, getAutoDownloadPath());
                globalProfileFirefox.setPreference("browser.download.downloadDir", getAutoDownloadPath());
                globalProfileFirefox.setPreference(BROWSER_DOWNLOAD_DEFAULT_FOLDER, getAutoDownloadPath());

                return globalCapabilitiesFirefox;

            /**
             * Return existing OPTION that contains GLOBAL Settings + Add MANUAL DOWNLOAD Settings This means that whenever a download link is hit, the BROWSER Will desplay a
             * BROWSER POP UP to confirm the download, on SAVE AS selection the File will be stored in Default download location
             **/
            case P_MANUAL_DOWNLOAD_PARTIAL:
            	 Reporting.logReporter(Status.DEBUG, PROFILE_TO_USE_MESSAGE + P_MANUAL_DOWNLOAD_PARTIAL);


                globalProfileFirefox.setPreference(BROWSER_DOWNLOAD_FOLDER_LIST, 2);

                globalProfileFirefox.setPreference(BROWSER_DOWNLOAD_USE_DOWNLOAD_DIR , true);
                globalProfileFirefox.setPreference(BROWSER_DOWNLOAD_DIR, getAutoDownloadPath());
                globalProfileFirefox.setPreference("browser.download.downloadDir", getAutoDownloadPath());
                globalProfileFirefox.setPreference(BROWSER_DOWNLOAD_DEFAULT_FOLDER, getAutoDownloadPath());

                return globalCapabilitiesFirefox;

            /**
             * Return existing OPTION that contains GLOBAL Settings + Add AUTO DOWNLOAD Settings This means that whenever a download link is hit, the BROWSER Will Store the file in
             * Default download location
             **/
            case P_AUTO_DOWNLOAD:
            	
            	Reporting.logReporter(Status.DEBUG, PROFILE_TO_USE_MESSAGE + P_AUTO_DOWNLOAD);


                // See https://www.toolsqa.com/selenium-webdriver/how-to-download-files-using-selenium/ for more reference about the settings

                globalProfileFirefox.setPreference(BROWSER_DOWNLOAD_FOLDER_LIST, 2);
                // globalProfileFirefox.setPreference("browser.download.manager.showWhenStarting", false); //Not valid anymore -

                globalProfileFirefox.setPreference("browser.helperApps.neverAsk.saveToDisk",
                        "multipart/x-zip,application/zip,application/x-zip-compressed,application/x-compressed,application/msword,application/csv,text/csv,image/png ,image/jpeg, application/pdf, text/html,text/plain,  application/excel, application/vnd.ms-excel, application/x-excel, application/x-msexcel, application/octet-stream");
                // globalProfileFirefox.setPreference("browser.helperApps.neverAsk.openFile","multipart/x-zip,application/zip,application/x-zip-compressed,application/x-compressed,application/msword,application/csv,text/csv,image/png
                // ,image/jpeg, application/pdf, text/html,text/plain, application/excel, application/vnd.ms-excel, application/x-excel, application/x-msexcel,


                globalProfileFirefox.setPreference(BROWSER_DOWNLOAD_USE_DOWNLOAD_DIR , true);
                globalProfileFirefox.setPreference(BROWSER_DOWNLOAD_DIR, getAutoDownloadPath());
                globalProfileFirefox.setPreference(BROWSER_DOWNLOAD_DEFAULT_FOLDER, getAutoDownloadPath());

                globalProfileFirefox.setPreference("browser.download.manager.closeWhenDone", true);
                globalProfileFirefox.setPreference("browser.download.manager.focusWhenStarting", false);

                return globalCapabilitiesFirefox;

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

    public static DesiredCapabilities phantomJSOptionsProfiles(String profileName) {
        DesiredCapabilities globalCapabilitiesPhantomJS = DesiredCapabilities.phantomjs();
        
       // globalCapabilitiesPhantomJS.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
       // globalCapabilitiesPhantomJS.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, ACCEPT_MSG);
        switch (profileName) {
        case P_NA:
            
            Reporting.logReporter(Status.DEBUG, PROFILE_TO_USE_MESSAGE + P_NA);
            
            // TO BE IMPLEMENTED
            return globalCapabilitiesPhantomJS;
        case P_MANUAL_DOWNLOAD:

            Reporting.logReporter(Status.DEBUG,String.format(MSG_PROFILE_DOES_NOT_EXIST_TEMPLATE, P_MANUAL_DOWNLOAD) );
            // This feature does not exist for this BROWSER
            return null;

        /**
         * Return existing OPTION that contains GLOBAL Settings + Add MANUAL DOWNLOAD Settings This means that whenever a download link is hit, the BROWSER Will desplay a
         * BROWSER POP UP to confirm the download, on SAVE AS selection the File will be stored in Default download location
         **/
        case P_MANUAL_DOWNLOAD_PARTIAL:

            
            Reporting.logReporter(Status.DEBUG,String.format(MSG_PROFILE_DOES_NOT_EXIST_TEMPLATE, P_MANUAL_DOWNLOAD_PARTIAL) );
            
            
            // This feature does not exist for this BROWSER
            return null;

        /**
         * Return existing OPTION that contains GLOBAL Settings + Add AUTO DOWNLOAD Settings This means that whenever a download link is hit, the BROWSER Will Store the file in
         * Default download location
         **/
        case P_AUTO_DOWNLOAD:
        	 Reporting.logReporter(Status.DEBUG,String.format(MSG_PROFILE_DOES_NOT_EXIST_TEMPLATE, P_AUTO_DOWNLOAD) );
             

            // This feature does not exist for this BROWSER
            return null;

        default:
            return null;

        } 
        
    }
    
    public static DesiredCapabilities internetExplorerOptionsProfiles(String profileName) {
        DesiredCapabilities globalCapabilitiesIE = DesiredCapabilities.internetExplorer();

        // Set Global Generic Options that apply for all profiles:
        globalCapabilitiesIE.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
        globalCapabilitiesIE.setCapability("dom.forms.number", false);
        globalCapabilitiesIE.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
        globalCapabilitiesIE.setCapability(InternetExplorerDriver.ENABLE_PERSISTENT_HOVERING, false);
        globalCapabilitiesIE.setCapability(InternetExplorerDriver.INITIAL_BROWSER_URL, "about:blank");
        globalCapabilitiesIE.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);

        // By the usual the following options are commonly requested to be set, can be removed if not needed
        globalCapabilitiesIE.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, true);

        switch (profileName) {

            /**
             * Return a new OPTION + add headless setting
             */
            case P_HEADLESS_ALONE:
            	
            	Reporting.logReporter(Status.DEBUG, String.format(MSG_PROFILE_DOES_NOT_EXIST_TEMPLATE, P_HEADLESS_ALONE));

                // This feature does not exist for this BROWSER
                return null;

            /**
             * Return existing OPTION that contains GLOBAL Settings + add headless setting
             */
            case P_HEADLESS_WITH_GLOBAL_OPTIONS:
            	Reporting.logReporter(Status.DEBUG, String.format(MSG_PROFILE_DOES_NOT_EXIST_TEMPLATE, P_HEADLESS_WITH_GLOBAL_OPTIONS));
                

                // This feature does not exist for this BROWSER
                return null;

            /**
             * Return a new OPTION with all default settings.
             */
            case P_NA:
                                
                Reporting.logReporter(Status.DEBUG, PROFILE_TO_USE_MESSAGE + P_NA);
                
                // TO BE IMPLEMENTED
                return DesiredCapabilities.internetExplorer();

            /**
             * Return existing OPTION that contains GLOBAL Settings
             */
            case P_GENERIC_ONE:
            	  Reporting.logReporter(Status.DEBUG, PROFILE_TO_USE_MESSAGE + P_GENERIC_ONE);

                // TO BE IMPLEMENTED
                return globalCapabilitiesIE;

            /**
             * Return existing OPTION that contains GLOBAL Settings + CUSTOM CODE
             */
            case P_GENERIC_TWO:
            	Reporting.logReporter(Status.DEBUG, PROFILE_TO_USE_MESSAGE + P_GENERIC_TWO);

                // TO BE IMPLEMENTED
                return globalCapabilitiesIE;

            /**
             * Return existing OPTION that contains GLOBAL Settings + Remove InfoBar
             */
            case P_REMOVE_INFOBAR_WITH_GLOBAL_OPTIONS:
            	Reporting.logReporter(Status.DEBUG, String.format(MSG_PROFILE_DOES_NOT_EXIST_TEMPLATE, P_REMOVE_INFOBAR_WITH_GLOBAL_OPTIONS));

                // This feature does not exist for this BROWSER.
                return null;

            /**
             * Return existing OPTION that contains GLOBAL Settings + Accept setUnhandledPromptBehaviour + setAcceptInsecureCerts(true)
             */
            case P_ACCEPT_BEHAVIORS_AND_CERTIFICATES:

                
                Reporting.logReporter(Status.DEBUG, PROFILE_TO_USE_MESSAGE + P_ACCEPT_BEHAVIORS_AND_CERTIFICATES);
                
                // globalCapabilitiesIE.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true); //Do not use, cause failure in recen driver versions
                globalCapabilitiesIE.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, true);

                return globalCapabilitiesIE;

            /**
             * Return existing OPTION that contains GLOBAL Settings + GIACOMAN SETTINGS
             */
            case P_GIACOMAN:

                
                Reporting.logReporter(Status.DEBUG,String.format(MSG_PROFILE_DOES_NOT_EXIST_TEMPLATE, P_GIACOMAN) );
                
                // TO BE IMPLEMENTED // This feature does not exist for this BROWSER, need to be created
                return null;

            /**
             * Return existing OPTION that contains GLOBAL Settings + Add PLATFORM & BROWSER NAME Settings
             */
            case P_WINDOWS:

                Reporting.logReporter(Status.DEBUG,PROFILE_TO_USE_MESSAGE + P_WINDOWS );
                
                // TO BE IMPLEMENTED
                return globalCapabilitiesIE;

            /**
             * Return existing OPTION that contains GLOBAL Settings + Add PLATFORM & BROWSER NAME Settings
             */
            case P_WINDOWS_10:

                Reporting.logReporter(Status.DEBUG,PROFILE_TO_USE_MESSAGE + P_WINDOWS_10 );
                // TO BE IMPLEMENTED
                return globalCapabilitiesIE;

            /**
             * Return existing OPTION that contains GLOBAL Settings + Add PLATFORM & BROWSER NAME Settings
             */
            case P_WINDOWS_8:
            	Reporting.logReporter(Status.DEBUG,PROFILE_TO_USE_MESSAGE + P_WINDOWS_8 );

                // TO BE IMPLEMENTED
                return globalCapabilitiesIE;

            /**
             * Return existing OPTION that contains GLOBAL Settings + Add PLATFORM & BROWSER NAME Settings
             */
            case P_WINDOWS_8_1:
            	Reporting.logReporter(Status.DEBUG,PROFILE_TO_USE_MESSAGE + P_WINDOWS_8_1 );

                // TO BE IMPLEMENTED
                return globalCapabilitiesIE;

            /**
             * Return existing OPTION that contains GLOBAL Settings + Add PLATFORM & BROWSER NAME Settings
             */
            case P_WINDOWS_VISTA:
            	Reporting.logReporter(Status.DEBUG,PROFILE_TO_USE_MESSAGE + P_WINDOWS_VISTA );

                // TO BE IMPLEMENTED
                return globalCapabilitiesIE;

            /**
             * Return existing OPTION that contains GLOBAL Settings + Add PLATFORM & BROWSER NAME Settings
             */
            case P_WINDOWS_XP:

                Reporting.logReporter(Status.DEBUG,PROFILE_TO_USE_MESSAGE + P_WINDOWS_XP );
                // TO BE IMPLEMENTED
                return globalCapabilitiesIE;

            /**
             * Return existing OPTION that contains GLOBAL Settings + Add MANUAL DOWNLOAD Settings This means that whenever a download link is hit, the BROWSER Will desplay a
             * BROWSER POP UP to confirm the download, on SAVE AS selection a OS Window will appear asking us to set the location to save
             **/
            case P_MANUAL_DOWNLOAD:

                Reporting.logReporter(Status.DEBUG,String.format(MSG_PROFILE_DOES_NOT_EXIST_TEMPLATE, P_MANUAL_DOWNLOAD) );
                // This feature does not exist for this BROWSER
                return null;

            /**
             * Return existing OPTION that contains GLOBAL Settings + Add MANUAL DOWNLOAD Settings This means that whenever a download link is hit, the BROWSER Will desplay a
             * BROWSER POP UP to confirm the download, on SAVE AS selection the File will be stored in Default download location
             **/
            case P_MANUAL_DOWNLOAD_PARTIAL:

                
                Reporting.logReporter(Status.DEBUG,String.format(MSG_PROFILE_DOES_NOT_EXIST_TEMPLATE, P_MANUAL_DOWNLOAD_PARTIAL) );
                
                
                // This feature does not exist for this BROWSER
                return null;

            /**
             * Return existing OPTION that contains GLOBAL Settings + Add AUTO DOWNLOAD Settings This means that whenever a download link is hit, the BROWSER Will Store the file in
             * Default download location
             **/
            case P_AUTO_DOWNLOAD:
            	 Reporting.logReporter(Status.DEBUG,String.format(MSG_PROFILE_DOES_NOT_EXIST_TEMPLATE, P_AUTO_DOWNLOAD) );
                 

                // This feature does not exist for this BROWSER
                return null;

            default:
                return null;
        }
    }

}
