package com.test.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Properties;

import com.aventstack.extentreports.Status;
import com.google.common.io.Resources;
import com.test.logging.Logging;

/**
 ****************************************************************************
 * HIGHLIGHTS: > Used when it is required to have variables in a
 * system.properties file > The variables that can keep could be: - Application
 * environments, drivers location, browser type
 ****************************************************************************
 */
public class SystemProperties {

	private SystemProperties() {
		throw new IllegalStateException("Utility class");
	}

	// private static final String[] DEFAULT_RESOURCE_PATHS = {null,

	private static Properties prop;
	private static Properties rprop;

	/**
	 * OBJECTIVE: Get the system.properties file location.
	 */

    public static final String WEBDRIVER_PROXY = "webdriver.proxy";

	public static final Boolean EMAIL = new Boolean(getProp().getProperty("email"));
	public static final String EMAIL_SENDER = getProp().getProperty("email.sender");
	public static final String EMAIL_SUBJECT = getProp().getProperty("email.subject");
	public static final String EMAIL_RECEIVER = getProp().getProperty("email.receiver");
	public static final String EMAIL_HOST = getProp().getProperty("email.hostname");
	public static final String EMAIL_PORT = getProp().getProperty("email.port");
	public static final String EMAIL_USERID = getProp().getProperty("email.userid");
	public static final String EMAIL_PASSWORD = getProp().getProperty("email.passsword");

	public static final Boolean REMOTE = new Boolean(getProp().getProperty("selenium.remote"));
	public static final Boolean CROSS_BROWSER = new Boolean(getProp().getProperty("cross.browser"));

	public static final String SELENIUM_GRID_URL = getProp().getProperty("selenium.gridurl");
	public static final String BROWSER_OPTIONS = getProp().getProperty("selenium.browser.options");

	public static final String BROWSER = getProp().getProperty("selenium.browser");

	public static final String CHROME_WEBDRIVER = getProp().getProperty("webdriver.chrome.driver");

	public static final String IE_WEBDRIVER = getProp().getProperty("webdriver.ie.driver");
	public static final String GECKO_WEBDRIVER = getProp().getProperty("webdriver.gecko.driver");
	public static final String EXECUTION_ENVIRONMENT = getProp().getProperty("execution.environment");

	public static final int GENERIC_PAGE_WAIT_TIMEOUT_MILLIS = Integer
			.parseInt(getProp().getProperty("selenium.genericpagewaittimeout"));

	public static final int LONG_WAIT_TIMEOUT_MILLIS = Integer
			.parseInt(getProp().getProperty("selenium.longwaittimeout"));
	public static final int SHORT_WAIT_TIMEOUT_MILLIS = Integer
			.parseInt(getProp().getProperty("selenium.shortwaittimeout"));
	public static final boolean EXTENT_REPORT = Boolean.parseBoolean(getProp().getProperty("reporting.extent"));

	public static final boolean TESTNG_REPORT = Boolean.parseBoolean(getProp().getProperty("reporting.testng"));
	public static final boolean LOG4J_REPORT = Boolean.parseBoolean(getProp().getProperty("reporting.log4j"));
	public static final boolean REPORT_MULTIPLE = Boolean.parseBoolean(getProp().getProperty("reporting.multiple"));
	public static final boolean REPORTPORTAL_REPORT = Boolean
			.parseBoolean(getReportportalProp().getProperty("rp.enable"));

	// If true the LOGs of debug Type will be skipped, they are useful for devs but
	// not for client managers and functional
	// Should be string... with value true or false (NOT boolean) due that we have a
	// logic that depends on the string null/empty
	public static final String EXTENT_REPORT_SKIP_DEBUG_LOGS = getProp().getProperty("reporting.extent.skip.debug.log");

	public static final String VIDEO_RECORDING = getProp().getProperty("video.recording");
	public static final String VIDEO_FOLDER_PATH = getProp().getProperty("video.folderpath");
	public static final boolean GROUP_REPORT = Boolean.parseBoolean(getProp().getProperty("reporting.groupReport"));

	// Due to compiler restrictions this number will be converted to int in the
	// method that calls for it.
	public static final String RETRY_TESTNG_COUNTER = getProp().getProperty("retry.testng");

	// REPORT AND SCREENSHOTS STORE PATH
	public static final boolean SCREENSHOTS_TAKE_ALL = Boolean
			.parseBoolean(getProp().getProperty("screenshots.take.all"));
	public static final boolean REPORTING_AND_SCREENSHOTS_KEEP_HISTORY = Boolean
			.parseBoolean(getProp().getProperty("reporting.and.screenshots.keep.history"));

	public static final String REPORTING_AND_SCREENSHOTS_KEEP_HISTORY_PATH = getProp()
			.getProperty("reporting.and.screenshots.keep.history.path");

	// BROWSER AUTO DOWNLOAD PATH - FOR DRIVER OPTIONS (PATH TO STORE THE DOWNLOADS)
	// DO NOT APPLY FOR ALL BROWSERS, DEPENDS ON THE SCENARIO
	public static final String DRIVER_AUTO_DOWNLOAD_PATH = getProp().getProperty("driver.auto.download.path");

	// DESKTOP VALUES(MANDATORY)
	public static final String DESKTOP_SYSTEM_PROCESS_DETAIL_NAME = getProp()
			.getProperty("desktop.system.process.detail.name");

	public static final int DESKTOP_LONG_WAIT_TIMEOUT_MILLIS = Integer
			.parseInt(getProp().getProperty("desktop.longwaittimeout"));
	public static final int DESKTOP_SHORT_WAIT_TIMEOUT_MILLIS = Integer
			.parseInt(getProp().getProperty("desktop.shortwaittimeout"));
	public static final int DESKTOP_GENERIC_PAGE_WAIT_TIMEOUT_MILLIS = Integer
			.parseInt(getProp().getProperty("desktop.pagewaittimeout"));

	public static final String DESKTOP_KEYBOARD_APP_PATH = getProp().getProperty("desktop.keyboard.app.path");

	public static final String DESKTOP_DRIVER_PATH = getProp().getProperty("desktop.driver.path");
	public static final String DESKTOP_DRIVER_LOCAL_URL = getProp().getProperty("desktop.driver.local.url");

	public static final String DESKTOP_APP_NAME = getProp().getProperty("desktop.app.name");
	public static final String DESKTOP_APP_PATH = getProp().getProperty("desktop.app.path");
	public static final String DESKTOP_APP_OPTIONS_PROFILE = getProp().getProperty("desktop.app.options.profile");

	// DESKTOP VALUES(OPTIONAL)
	public static final String DESKTOP_APP_ARGS = getProp().getProperty("desktop.app.args");
	public static final int DESKTOP_APP_LAUNCH_DELAY_MILISECONDS = Integer
			.parseInt(getProp().getProperty("desktop.app.launch.delay.miliseconds"));

	public static final String SFTP_ENVIRONMENT = getProp().getProperty("sftp.environment");
	public static final String SFTP_KEY = getProp().getProperty("sftp.key");
	public static final String SFTP_FILE_JSON = getProp().getProperty("sftp.file.json");
	public static final String SFTP_SOURCE_UP = getProp().getProperty("sftp.source.up");
	public static final String SFTP_DESTINATION_UP = getProp().getProperty("sftp.destination.up");
	public static final String SFTP_SOURCE_DOWN = getProp().getProperty("sftp.source.down");
	public static final String SFTP_DESTINATION_DOWN = getProp().getProperty("sftp.destination.down");
	public static final String SFTP_READFILEPATH = getProp().getProperty("sftp.read.filepath");
	public static final String SFTP_READFILENAME = getProp().getProperty("sftp.read.filename");

	public static final String SFTP_SECURITY_ALGO_CHECK = getProp().getProperty("sftp.security.algo.check");

	public static final String REPORTING_JENKINS_NAME = getProp().getProperty("reporting.jenkins.name");
	public static final String HEALTHCHECK_ENV_NAME = getProp().getProperty("healthcheck.environment");

    public static final int URL_ITERATION = getIntValue("url.iteration");

    public static boolean FULL_SCREENSHOT = getBooleanValue("full.screenshot");

	public static final String REPORTING_TESTNG_PATH = getProp().getProperty("reporting.testng.path");

    public static final boolean REPORTING_AND_TESTNG_KEEP_HISTORY = getBooleanValue("reporting.and.testng.keep.history");


    public static final int REPORTING_TESTNG_TIME = getIntValue("reporting.and.testng.time");


	public static final String PHANTOMJS_WEBDRIVER = getProp().getProperty("webdriver.phantomJS.driver");
	public static final boolean REPORTING_SCREENSHOT_NEW_NAME_FORMAT = Boolean
			.parseBoolean(getProp().getProperty("reporting.screenshot.newNameFormat"));

	/**
	 * OBJECTIVE: Load all variables from system.properties.
	 */
	private static Properties getProp() {

		if (prop == null) {
			prop = new Properties();
			InputStream input = null;

			try {

				input = new FileInputStream(new File(Resources.getResource("system.properties").toURI()));

				prop.load(input);
			} catch (RuntimeException | URISyntaxException | IOException e) {
				Logging.logReporter(Status.DEBUG, "Exception Caught : " + e);
			}
		}

		return prop;
	}

	/**
	 * OBJECTIVE: Load all variables from reportportal.properties.
	 */
	private static Properties getReportportalProp() {

		if (rprop == null) {
			rprop = new Properties();
			InputStream input = null;

			try {

				input = new FileInputStream(new File(Resources.getResource("reportportal.properties").toURI()));

				rprop.load(input);
			} catch (RuntimeException | URISyntaxException | IOException e) {
				Logging.logReporter(Status.DEBUG, "Exception Caught : " + e);
			}
		}

		return rprop;
	}

	public static boolean getBooleanValue(String property) {
		String value = getStringValue(property);
		try {
			if (value == null || value.isEmpty())
				return false;
		
			return Boolean.parseBoolean(getProp().getProperty(property));
		} catch (Exception e) {
			return false;
		}
	}

	public static int getIntValue(String property) {
		String value = getStringValue(property);
		try {
			if (value == null || value.isEmpty())
				return -1;
			return Integer.parseInt(getProp().getProperty(property));
		} catch (Exception e) {
			return -1;
		}
	}

	public static String getStringValue(String property) {
		String value = getProp().getProperty(property);
		try {
			if (value == null || value.isEmpty())
				return "";
			return getProp().getProperty(property);
		} catch (Exception e) {
			return "";
		}
	}

	public static boolean isValueSet(String property) {
		String value = getStringValue(property);
		if (value == null || value.isEmpty())
			return false;
		return true;
	}

	public static void putValue(String key, String value) {
		if (key.isEmpty() || value.isEmpty())
			return;
		getProp().put(key, value);
	}
}
