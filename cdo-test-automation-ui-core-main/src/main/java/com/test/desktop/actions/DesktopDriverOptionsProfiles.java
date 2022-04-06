package com.test.desktop.actions;

import org.openqa.selenium.winium.DesktopOptions;
import org.openqa.selenium.winium.KeyboardSimulatorType;

import com.aventstack.extentreports.Status;
import com.test.reporting.Reporting;
import com.test.utils.SystemProperties;

public class DesktopDriverOptionsProfiles {
	
	private DesktopDriverOptionsProfiles() {
		    throw new IllegalStateException("Utility class");
		  }

    private static final String PROFILE_TO_USE_MESSAGE = "DESKTOP DRIVER will use the OPTIONS profile: ";

    private static final String P_NA = "P_NA";
    private static final String P_APP_KEYBOARD_BASED_ON_WINDOWS = "P_APP_KEYBOARD_BASED_ON_WINDOWS";
    private static final String P_APP_KEYBOARD_BASED_ON_SIMULATOR_LIB = "P_APP_KEYBOARD_BASED_ON_SIMULATOR_LIB";
    private static final String P_APP_PATH = "P_APP_PATH";
    private static final String P_APP_ARGUMENTS = "P_APP_ARGUMENTS";
    private static final String P_APP_DEBUG_CONNECT_TO_RUNNING_APP = "P_APP_DEBUG_CONNECT_TO_RUNNING_APP";
    private static final String P_APP_WITH_LAUNCH_DELAY = "P_APP_WITH_LAUNCH_DELAY";

    public static DesktopOptions desktopOptionsProfiles(String profileName) {
        DesktopOptions options = new DesktopOptions();

        // Set Global Generic Options that apply for all profiles:


        switch (profileName) {
            case P_NA:

                
                Reporting.logReporter(Status.DEBUG, PROFILE_TO_USE_MESSAGE + P_NA);
                

                return new DesktopOptions();

            case P_APP_KEYBOARD_BASED_ON_WINDOWS:

                Reporting.logReporter(Status.DEBUG, PROFILE_TO_USE_MESSAGE + P_APP_KEYBOARD_BASED_ON_WINDOWS);
                options.setKeyboardSimulator(KeyboardSimulatorType.BasedOnWindowsFormsSendKeysClass);
                return options;

            case P_APP_KEYBOARD_BASED_ON_SIMULATOR_LIB:// THIS IS THE DEFAULT

                Reporting.logReporter(Status.DEBUG, PROFILE_TO_USE_MESSAGE + P_APP_KEYBOARD_BASED_ON_SIMULATOR_LIB);
                
                options.setKeyboardSimulator(KeyboardSimulatorType.BasedOnInputSimulatorLib);
                return options;

            case P_APP_PATH:

                Reporting.logReporter(Status.DEBUG, PROFILE_TO_USE_MESSAGE + P_APP_PATH);
                
                options.setApplicationPath(SystemProperties.DESKTOP_APP_PATH);
                return options;

            case P_APP_ARGUMENTS:

                Reporting.logReporter(Status.DEBUG, PROFILE_TO_USE_MESSAGE + P_APP_ARGUMENTS);
                options.setArguments(SystemProperties.DESKTOP_APP_ARGS);
                return options;

            case P_APP_DEBUG_CONNECT_TO_RUNNING_APP:// OFF IS THE DEFAULT

                Reporting.logReporter(Status.DEBUG, PROFILE_TO_USE_MESSAGE + P_APP_DEBUG_CONNECT_TO_RUNNING_APP);
                options.setDebugConnectToRunningApp(true);
                return options;

            case P_APP_WITH_LAUNCH_DELAY:

                Reporting.logReporter(Status.DEBUG, PROFILE_TO_USE_MESSAGE + P_APP_WITH_LAUNCH_DELAY);
                
                options.setLaunchDelay(SystemProperties.DESKTOP_APP_LAUNCH_DELAY_MILISECONDS);
                return options;

            default:
                return null;
        }

    }

}
