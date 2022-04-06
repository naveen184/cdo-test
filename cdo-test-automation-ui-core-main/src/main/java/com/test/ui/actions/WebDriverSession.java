package com.test.ui.actions;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Reporter;

import com.aventstack.extentreports.Status;
import com.test.logging.Logging;
import com.test.reporting.Reporting;
import com.test.utils.SystemProperties;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 ****************************************************************************
 * HIGHLIGHTS: > Reusable methods for all projects and focused on generate the driver. > The "getDriverInstance" creates a new instance of the driver or gets the existing.
 ****************************************************************************
 */

public class WebDriverSession {
	
	private static final boolean USE_LATEST_DRIVER_SETTINGS_STRATEGY = true; // True will Activate the newest strategy (OPTIONS) / False will activate the deprecated DESIRED
                                                                             // CAPABILITIES

    private static final String LEGACY_DRIVER_STRATEGY_MESSAGE = "INFO! The Driver settings are loaded using the LEGACY Strategy";
    private static final String LATEST_DRIVER_STRATEGY_MESSAGE = "INFO! The Driver settings are loaded using the LATEST Strategy";

    private static final String ERROR_MESSAGE_DEFAULT_PROFILE = "WARNING! PROFILE Selected [%s] is not supported. - returning DEFAULT";
    private static final String DEFAULT_PROFILE = "P_NA";

    protected static WebDriver webDriver = null;
    public static HashMap<Long, WebDriver> map = new HashMap<>();

    public static Browser getBrowser() {
        String browser = SystemProperties.BROWSER;

        switch (browser) {
            case "CHROME":
                return Browser.CHROME;
            case "FIREFOX":
                return Browser.FIREFOX;
            case "IE":
                return Browser.IE11;
            case "PHANTOMJS":
                return Browser.PHANTOMJS;
            default:
                throw new UnsupportedOperationException(browser + " Browser is not supported.");
        }
    }

    /**
     * OBJECTIVE: Get the current Driver for the current Threat (useful to validate if there is an active session already) and do not instantiate it on query
     */
    public static WebDriver getWebDriverForCurrentThreat() {
        return map.get(Thread.currentThread().getId());
    }

    /**
     * OBJECTIVE: Get the Webdriver instance so It can be reused during the script.
     */
    public static WebDriver getWebDriverSession() {
        WebDriver toReturn = map.get(Thread.currentThread().getId());
        if (toReturn == null) {
            loadNewWebDriverSession();
            toReturn = map.get(Thread.currentThread().getId());
        }
        return toReturn;
    }

    /**
     * OBJECTIVE: Load a NEW session of driver when there is not an existing to work on.
     */
    @SuppressWarnings("deprecation")
	public static void loadNewWebDriverSession() {
        WebDriver webDriver = null;

        String browserType = BaseTest.getOverrideWebBrowser();
        if (browserType == null) {
        	            	
               
            	if(SystemProperties.CROSS_BROWSER == false  ) {
                browserType = SystemProperties.BROWSER;
            	//browserType=browser;
            }
            else {
            	String value = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("browser");
            	browserType=value;
            	System.out.println(" loadNewWebDriverSession browser....>>>"+ browserType);
                
            }
         
        }

        String browserOptions = BaseTest.getOverrideWebOptions();
        if (browserOptions == null) {
            browserOptions = SystemProperties.BROWSER_OPTIONS;
        }


        Reporting.logReporter(Status.DEBUG,"Browser to be utilized: " + browserType);
        
        try {
            DesiredCapabilities capabilities = null;

            switch (browserType) {
                case "FIREFOX":
                    System.setProperty("webdriver.gecko.driver", SystemProperties.GECKO_WEBDRIVER);
                    FirefoxOptions firefoxOptions = WebDriverOptionsProfiles.firefoxOptionsProfiles(browserOptions);

                    if (!USE_LATEST_DRIVER_SETTINGS_STRATEGY) {
                        capabilities = WebDriverDesiredCapabilitiesProfiles.firefoxOptionsProfiles(browserOptions);
                    }

                    if (firefoxOptions == null) {

                        if (USE_LATEST_DRIVER_SETTINGS_STRATEGY) {
                            firefoxOptions = WebDriverOptionsProfiles.firefoxOptionsProfiles(DEFAULT_PROFILE);

                            Reporting.logReporter(Status.DEBUG,String.format(ERROR_MESSAGE_DEFAULT_PROFILE, browserOptions));
                            
                        
                        } else {
                            capabilities = WebDriverDesiredCapabilitiesProfiles.firefoxOptionsProfiles(DEFAULT_PROFILE);
                        }

                    }

                    if (SystemProperties.REMOTE) {

                        Reporting.logReporter(Status.DEBUG,"Remote Grid to be used: " + SystemProperties.SELENIUM_GRID_URL);
                        
                        if (USE_LATEST_DRIVER_SETTINGS_STRATEGY) {
                            webDriver = new RemoteWebDriver(new URL(SystemProperties.SELENIUM_GRID_URL), firefoxOptions);
                        } else {
                            webDriver = new RemoteWebDriver(new URL(SystemProperties.SELENIUM_GRID_URL), capabilities);
                        }

                    } else {
                        if (USE_LATEST_DRIVER_SETTINGS_STRATEGY) {
                        	Reporting.logReporter(Status.DEBUG,LATEST_DRIVER_STRATEGY_MESSAGE);
                            

                            webDriver = new FirefoxDriver(firefoxOptions);

                        } else {
                        	Reporting.logReporter(Status.DEBUG,LEGACY_DRIVER_STRATEGY_MESSAGE);

                            webDriver = new FirefoxDriver(capabilities);
                        }
                    }

                    break;

                case "IE":
                    System.setProperty("webdriver.ie.driver", SystemProperties.IE_WEBDRIVER);
                    InternetExplorerOptions ieOptions = WebDriverOptionsProfiles.internetExplorerOptionsProfiles(browserOptions);

                    if (!USE_LATEST_DRIVER_SETTINGS_STRATEGY) {
                        capabilities = WebDriverDesiredCapabilitiesProfiles.internetExplorerOptionsProfiles(browserOptions);
                    }

                    if (ieOptions == null) {
                        if (USE_LATEST_DRIVER_SETTINGS_STRATEGY) {
                            ieOptions = WebDriverOptionsProfiles.internetExplorerOptionsProfiles(DEFAULT_PROFILE);

                            Reporting.logReporter(Status.DEBUG,String.format(ERROR_MESSAGE_DEFAULT_PROFILE, browserOptions));
                            
                        
                        
                        } else {
                            capabilities = WebDriverDesiredCapabilitiesProfiles.internetExplorerOptionsProfiles(DEFAULT_PROFILE);
                        }
                    }

                    if (SystemProperties.REMOTE) {

                        Reporting.logReporter(Status.DEBUG,"\n" + "Remote Grid: " + SystemProperties.SELENIUM_GRID_URL);
                        if (USE_LATEST_DRIVER_SETTINGS_STRATEGY) {
                            webDriver = new RemoteWebDriver(new URL(SystemProperties.SELENIUM_GRID_URL), ieOptions);
                        } else {
                            webDriver = new RemoteWebDriver(new URL(SystemProperties.SELENIUM_GRID_URL), capabilities);
                        }

                    } else {
                        if (USE_LATEST_DRIVER_SETTINGS_STRATEGY) {
                        	
                        	 Reporting.logReporter(Status.DEBUG,LATEST_DRIVER_STRATEGY_MESSAGE);
                             

                            webDriver = new InternetExplorerDriver(ieOptions);
                        } else {

                            
                       	 Reporting.logReporter(Status.DEBUG,LEGACY_DRIVER_STRATEGY_MESSAGE);
                            webDriver = new InternetExplorerDriver(capabilities);
                        }
                    }

                    break;

                case "CHROME":
                    System.getenv();
                    System.setProperty("webdriver.chrome.driver", SystemProperties.CHROME_WEBDRIVER);
                    ChromeOptions chromeOptions = WebDriverOptionsProfiles.chromeOptionsProfiles(browserOptions);

                    if (!USE_LATEST_DRIVER_SETTINGS_STRATEGY) {
                        capabilities = WebDriverDesiredCapabilitiesProfiles.chromeOptionsProfiles(browserOptions);
                    }

                    if (chromeOptions == null) {
                        if (USE_LATEST_DRIVER_SETTINGS_STRATEGY) {
                            chromeOptions = WebDriverOptionsProfiles.chromeOptionsProfiles(DEFAULT_PROFILE);

                            
                            Reporting.logReporter(Status.DEBUG,String.format(ERROR_MESSAGE_DEFAULT_PROFILE, browserOptions));  
                            
                        } else {
                            capabilities = WebDriverDesiredCapabilitiesProfiles.chromeOptionsProfiles(DEFAULT_PROFILE);
                        }
                    }

                    if (SystemProperties.REMOTE) {

                      
                        Reporting.logReporter(Status.DEBUG,"\n" + "Remote Grid to be used: " + SystemProperties.SELENIUM_GRID_URL);  
                                    
                        if (USE_LATEST_DRIVER_SETTINGS_STRATEGY) {
                            webDriver = new RemoteWebDriver(new URL(SystemProperties.SELENIUM_GRID_URL), chromeOptions);
                        } else {
                            webDriver = new RemoteWebDriver(new URL(SystemProperties.SELENIUM_GRID_URL), capabilities);
                        }

                    } else {
                    	
                       if (USE_LATEST_DRIVER_SETTINGS_STRATEGY) {                        
                            
                            Reporting.logReporter(Status.DEBUG,LATEST_DRIVER_STRATEGY_MESSAGE);  
                            ChromeOptions options = new ChromeOptions();
                        	
                         if(SystemProperties.BROWSER_OPTIONS.equalsIgnoreCase("REMOTE")){
                            options.addArguments("start-maximized");
                            options.addArguments("--remote-debugging-port=9222");
                            options.addArguments("--no-sandbox");
                            options.addArguments("start-maximized");
                            options.addArguments("--disable-notifications");

	                         if(SystemProperties.isValueSet(SystemProperties.WEBDRIVER_PROXY))
	                             WebDriverManager.chromedriver().proxy(SystemProperties.getStringValue(SystemProperties.WEBDRIVER_PROXY)).setup();
	                          webDriver = new ChromeDriver(options);
                         } else {
                         	webDriver = new ChromeDriver(chromeOptions);                      
                         }	                          
	                          
                        } else {

                            Reporting.logReporter(Status.DEBUG,LEGACY_DRIVER_STRATEGY_MESSAGE);  
                            webDriver = new ChromeDriver(capabilities);
                        }                      
                    }

                    break;
                case "PHANTOMJS":{
                	System.setProperty("webdriver.phantomJS.driver", SystemProperties.PHANTOMJS_WEBDRIVER);
                	//PhantomJSDriver driver 
                	System.out.println("inside phantom");
                	DesiredCapabilities capablities = new DesiredCapabilities();
                	capablities.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "D:\\PhantomJS\\phantomjs-2.1.1-windows\\bin\\phantomjs.exe");

                	webDriver = new PhantomJSDriver(capablities);
                	
                }
                break;
                default:
                    throw new UnsupportedOperationException("\n" + "Browser type not supported");
            }
        } catch (RuntimeException | MalformedURLException e) {
        	Logging.logReporter(Status.DEBUG, "Exception Caught : " + e);
        }

        if (webDriver == null) {
            throw new UnsupportedOperationException("webDriver is null");
        } else {

            webDriver.manage().window().maximize();


           
            webDriver.manage().deleteAllCookies();

            // Save driver session
            Reporting.logReporter(Status.DEBUG, "webDriver - Session ID saved to MAP");
            map.put(Thread.currentThread().getId(), webDriver);
        }
    }

}
