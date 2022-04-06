package com.test.desktop.actions;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.winium.DesktopOptions;
import org.openqa.selenium.winium.WiniumDriver;

import com.aventstack.extentreports.Status;
import com.test.logging.Logging;
import com.test.reporting.Reporting;
import com.test.ui.actions.BaseTest;
import com.test.utils.SystemProperties;

/**
 ****************************************************************************
 * HIGHLIGHTS: > Reusable methods for all projects and focused on generate the driver. > The "getDriverInstance" creates a new instance of the driver or gets the existing.
 ****************************************************************************
 */

public class DesktopDriverSession {
	
	private static final String ERROR_MESSAGE_DEFAULT_PROFILE = "WARNING! PROFILE Selected [%s] is not supported. - returning DEFAULT";
    private static final String DEFAULT_PROFILE = "P_NA";
    private static final int THREE_SECONDS= 3000;

    protected static WiniumDriver desktopDriver = null;
    protected static HashMap<Long, WiniumDriver> map = new HashMap<>();
	private static final String EXCEPTION_MSG = "Exception Caught : ";
	private static final String INTERRUPTED_MSG = "Interrupted Exception : ";

    /**
     * OBJECTIVE: close the desktopDriver instance (There's an existing issue with the this methods (from the actual tool)
     */
    public static boolean closeDesktopDriverSession() {
        Reporting.logReporter(Status.INFO, "closeDesktopDriverSession");

        try {
            getDesktopDriverSession().close();
        } catch (WebDriverException e) {
            String msg = "Error! Cannot close the Desktop application .exe";

            Reporting.logReporter(Status.DEBUG, msg);
            
            Logging.logReporter(Status.DEBUG, EXCEPTION_MSG + e); // DO NOTHING EXPECTED EXCEPTION - KNWON ISSUE
        }

        removeThreatIdFromMap();

        try {
            quitServerProcess();
            return true;

        } catch (IOException e) {
            String msg = "Error! Cannot close the Desktop Server driver.exe";

            Reporting.logReporter(Status.DEBUG, msg);
            Logging.logReporter(Status.DEBUG, EXCEPTION_MSG + e); // DO NOTHING EXPECTED EXCEPTION
            
        }catch (InterruptedException e) {
        	Logging.logReporter(Status.DEBUG, INTERRUPTED_MSG + e);
        	Thread.currentThread().interrupt();
        }

        return false;
    }

    /**
     * OBJECTIVE: close the desktopDriver instance (This close the app by killing the .exe) e.g. calculator.exe
     */
    public static boolean closeDesktopDriverSessionWithAppProcessName() {
        Reporting.logReporter(Status.INFO, "closeDesktopDriverSessionWithAppProcessName");

        String systemAppNameWithExe = BaseTest.getOverrideAppDetailProcessName();
        if (systemAppNameWithExe == null) {
            systemAppNameWithExe = SystemProperties.DESKTOP_SYSTEM_PROCESS_DETAIL_NAME;
        }

        try {
            quitDesktopAppByRuntime(systemAppNameWithExe);
        } catch (IOException e) {
            String msg = "Error! Cannot close the Desktop application .exe";

            
            Reporting.logReporter(Status.INFO, msg);
            Logging.logReporter(Status.DEBUG, EXCEPTION_MSG + e);
            
        }
        catch (InterruptedException e) {
        	Logging.logReporter(Status.DEBUG, INTERRUPTED_MSG + e);
        	Thread.currentThread().interrupt();
        }

        removeThreatIdFromMap();

        try {
            quitServerProcess();
            return true;

        } catch (IOException e) {
            String msg = "Error! Cannot close the Desktop Server driver.exe";

            
            Reporting.logReporter(Status.INFO, msg);
            Logging.logReporter(Status.DEBUG, EXCEPTION_MSG + e); // DO NOTHING EXPECTED EXCEPTION
            
        }catch (InterruptedException e) {
        	Logging.logReporter(Status.DEBUG, INTERRUPTED_MSG + e);
        	Thread.currentThread().interrupt();
        }

        return false;
    }

    public static DesktopApplication getDesktopApplication() {
        String application = SystemProperties.DESKTOP_APP_NAME;

        switch (application) {
            case "CALCULATOR":
                return DesktopApplication.CALCULATOR;
            case "SAP_750":
                return DesktopApplication.SAP_750;
            case "SIEBEL_IE11":
                return DesktopApplication.SIEBEL_IE11;
            case "OTHER":
                return DesktopApplication.OTHER;
            default:
                throw new UnsupportedOperationException(application + " DESKTOP APP - App is not supported. please add it");
        }
    }

    /**
     * OBJECTIVE: Get the current Driver for the current Threat (useful to validate if there is an active session already) and do not instantiate it on query
     */
    public static WiniumDriver getDesktopDriverForCurrentThreat() {
        return map.get(Thread.currentThread().getId());
    }

    /**
     * OBJECTIVE: Get the desktopDriver instance so It can be reused during the script.
     */
    public static WiniumDriver getDesktopDriverSession() {
        WiniumDriver toReturn = map.get(Thread.currentThread().getId());
        if (toReturn == null) {
            loadNewDesktopDriverSession();
            toReturn = map.get(Thread.currentThread().getId());
        }
        return toReturn;
    }

    /**
     * OBJECTIVE: Load a NEW session of driver when there is not an existing to work on.
     */
    public static void loadNewDesktopDriverSession() {

        try {// Try to delete any existing process
            quitServerProcess();
        } catch (IOException e) {
        	Logging.logReporter(Status.DEBUG, EXCEPTION_MSG + e);
        	
        }catch (InterruptedException e) {
        	Logging.logReporter(Status.DEBUG, INTERRUPTED_MSG + e);
        	Thread.currentThread().interrupt();
        }

        startServer();

        WiniumDriver desktopDriver = null;

        String application = BaseTest.getOverrideAppName();
        if (application == null) {
            application = SystemProperties.DESKTOP_APP_NAME;
        }

        String appOptions = BaseTest.getOverrideAppOptions();
        if (appOptions == null) {
            appOptions = SystemProperties.DESKTOP_APP_OPTIONS_PROFILE;
        }

        String appPath = BaseTest.getOverrideAppPath();
        if (appPath == null) {
            appPath = SystemProperties.DESKTOP_APP_PATH;
        }


        Reporting.logReporter(Status.INFO, "Desktop App to be utilized: " + application);
        
        try {
            DesktopOptions options = DesktopDriverOptionsProfiles.desktopOptionsProfiles(appOptions);
            if (options == null) {
                options = DesktopDriverOptionsProfiles.desktopOptionsProfiles(DEFAULT_PROFILE);
                
                Reporting.logReporter(Status.INFO, String.format(ERROR_MESSAGE_DEFAULT_PROFILE, appOptions));

            }

            options.setApplicationPath(appPath);

           
            if (SystemProperties.REMOTE) {
               
                Reporting.logReporter(Status.INFO, "\n" + "Remote Grid to be used: " + SystemProperties.SELENIUM_GRID_URL);
                
                

                desktopDriver = new WiniumDriver(new URL(SystemProperties.SELENIUM_GRID_URL), options);
            } else {

                Reporting.logReporter(Status.INFO, "\n" + "Desktop Driver Will Use the URL: " + SystemProperties.DESKTOP_DRIVER_LOCAL_URL);
                
                
                desktopDriver = new WiniumDriver(new URL(SystemProperties.DESKTOP_DRIVER_LOCAL_URL), options);
            }
            DesktopBaseSteps.Waits.waitGeneric(THREE_SECONDS);

        } catch (RuntimeException | MalformedURLException e) {
        	Logging.logReporter(Status.DEBUG, EXCEPTION_MSG + e);
        }

        if (desktopDriver == null) {
            throw new UnsupportedOperationException("desktopDriver is null");
        } else {
            Reporting.logReporter(Status.DEBUG, "desktopDriver - Session ID saved to MAP");
            map.put(Thread.currentThread().getId(), desktopDriver);
        }
    }

    /**
     * Workaround for a known issue with the app close/quit feature: https://github.com/2gis/Winium.Desktop/issues/86 https://github.com/2gis/Winium.Desktop/issues/192
     */
    private static void quitDesktopAppByRuntime(String systemAppNameWithExe) throws IOException, InterruptedException {
        // TO BE IMPLEMENTED Check behavior in parallel, but as is winium I do not thing it should run in parallel under the same machine.. in any case parallel execution should run in diff
        // machines

        Reporting.logReporter(Status.DEBUG, "Deleting Desktop Application .exe");


        Process process = Runtime.getRuntime().exec("taskkill /F /IM " + systemAppNameWithExe);
        process.waitFor();
        process.destroy();
    }

    /**
     * Delete any call to the driver .exe, to check if affects multithreat
     */
    private static void quitServerProcess() throws IOException, InterruptedException {
        Reporting.logReporter(Status.DEBUG, "Deleting Winnium Server Driver .exe");

        // TO BE IMPLEMENTED Check behavior in parallel, but as is winium I do not thing it should run in parallel under the same machine.. in any case parallel execution should run in diff
        // machines
        Process process = Runtime.getRuntime().exec("taskkill /F /IM Winium.Desktop.Driver.exe");
        DesktopBaseSteps.Waits.waitGeneric(THREE_SECONDS);
        process.waitFor();
        process.destroy();
        DesktopBaseSteps.Waits.waitGeneric(THREE_SECONDS);
    }

    /**
     * OBJECTIVE: Remove the current Threat Id from the map that help us to create sessions
     */
    private static void removeThreatIdFromMap() {
        Reporting.logReporter(Status.DEBUG, "DesktopDriver - Session ID removed from MAP");

        // Remove from Map
        map.remove(Thread.currentThread().getId());

    }

    /**
     * OBJECTIVE: Start the server of winium
     */
    private static void startServer() {
        try {
            Runtime runTime = Runtime.getRuntime();
            String executablePath = SystemProperties.DESKTOP_DRIVER_PATH;
            runTime.exec(executablePath);
            Reporting.logReporter(Status.INFO, "Winium Server Driver.exe started!");
            DesktopBaseSteps.Waits.waitGeneric(THREE_SECONDS);

        } catch (IOException e) {
            Reporting.logReporter(Status.INFO, "Cannot start Winium Server Driver.exe started!");
            Logging.logReporter(Status.DEBUG, EXCEPTION_MSG + e);
        }
    }
}
