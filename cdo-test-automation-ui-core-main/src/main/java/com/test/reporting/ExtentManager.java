package com.test.reporting;

import java.io.File;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

import org.openqa.selenium.Platform;
import org.testng.ITestResult;

import com.aventstack.extentreports.AnalysisStrategy;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.test.screenshots.Screenshots;
import com.test.utils.DatesUtils;
import com.test.utils.SystemProperties;
import com.test.screenshots.*;

/**
 * ExtentManager creates the instance of the reporter and specifies the report
 * path location
 */
public class ExtentManager {

	private ExtentManager() {
		throw new IllegalStateException("Utility class");
	}

	private static final String ERROR_MESSAGE = "ExtentReport path has not been set! There is a problem!\n";

	protected static ExtentReports extent;
	private static Platform platform;
	public static String REPORT_FILE_NAME = REPORT_FILE_NAME_New();
	public static String name;

	public static String REPORT_FILE_NAME_New() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYYMMddhhmmssSS");
		String dateAsString = simpleDateFormat.format(new Date());

		if (SystemProperties.REPORT_MULTIPLE == (true)) {
			name = File.separator + "ExtentReports-Version3-Test-Automation-Report_" + dateAsString + ".html";

		} else {
			name = File.separator + "ExtentReports-Version3-Test-Automation-Report.html";

		}
		return name;
	}

	public static final String DEFAULT_NOT_WINDOWS_REPORT_PARTIAL_PATH = File.separator + "target" + File.separator + "extent-reports";
	public static final String DEFAULT_WINDOWS_REPORT_PARTIAL_PATH = File.separator + "target" + File.separator + "extent-reports";

	public static final String DEFAULT_NOT_WINDOWS_PATH = System.getProperty("user.dir")
			+ DEFAULT_NOT_WINDOWS_REPORT_PARTIAL_PATH;
	public static final String DEFAULT_WINDOWS_PATH = System.getProperty("user.dir")
			+ DEFAULT_WINDOWS_REPORT_PARTIAL_PATH;

	public static String DEFAULT_NOT_WINDOWS_REPORT_FILE = DEFAULT_NOT_WINDOWS_PATH + File.separator + REPORT_FILE_NAME;
	public static String DEFAULT_WINDOWS_REPORT_FILE = DEFAULT_WINDOWS_PATH + File.separator + REPORT_FILE_NAME;

	// DATE USED BY THE REPORT
	private static String reportDate;
	private static String reportDateWithSpaces;
	private static String reportDateWithoutSpaces;

	// DATE FORMATTERS FOR REPORT OUTPUT
	private static String reportDateFormatter;
	private static String reportDateWithSpacesFormatter;
	private static String reportDateWithoutSpacesFormatter;

	// DATE FORMATTERS FOR UNIQUE FILES / SCREENSHOTS OUTPUT
	private static String uniqueDateFormatter;
	private static String uniqueDateWithSpacesFormatter;
	private static String uniqueDateWithoutSpacesFormatter;

	// Create the report path if it does not exist
	private static void createReportPath(String path) {

		File file = new File(SystemProperties.REPORTING_AND_SCREENSHOTS_KEEP_HISTORY_PATH);
		// Creating the directory

		if (!file.exists()) {
			boolean bool = file.mkdir();
			if (bool) {

				Reporting.logReporter(Status.DEBUG, "Directory created successfully");
			} else {
				Reporting.logReporter(Status.DEBUG, "Sorry couldn’t create specified directory");

			}

		}
		File testDirectory = new File(path);
		if (!testDirectory.exists()) {
			if (testDirectory.mkdir()) {
				String message = "Extent-Report Directory [Succesfuly Created]: " + path;

				Reporting.logReporter(Status.DEBUG, message);

				Reporting.logReporter(Status.DEBUG, message);
			} else {
				String message = "Extent-Report Directory [Failed On Creation]: " + path;
				Reporting.logReporter(Status.DEBUG, message);

				Reporting.logReporter(Status.DEBUG, message);
				throw new UnsupportedOperationException(
						"As we cannot create the directory the report will fail to be created, please review the path or assign one that you have privilege to write/modify");
			}
		} else {
			String message = "Extent-Report Directory [already exists]: " + path;
			Reporting.logReporter(Status.DEBUG, message);

			Reporting.logReporter(Status.DEBUG, message);
		}
	}

	// Get current platform
	public static synchronized Platform getCurrentPlatform() {
		String operSys = System.getProperty("os.name").toLowerCase();

		if (operSys.contains("win")) {
			platform = Platform.WINDOWS;
		} else if (operSys.contains("nix") || operSys.contains("nux") || operSys.contains("aix")) {
			platform = Platform.LINUX;
		} else if (operSys.contains("mac")) {
			platform = Platform.MAC;
		}

		return platform;
	}

	public static String getReportDateBaseFormat() {
		return reportDate;
	}

	public static String getReportDateBaseFormatWithoutSpaces() {
		return reportDateWithoutSpaces;
	}

	public static String getReportDateBaseFormatWithSpaces() {
		return reportDateWithSpaces;
	}

	public static String getReportDateFormatter() {
		return reportDateFormatter;
	}

	public static String getReportDateWithoutSpacesFormatter() {
		return reportDateWithoutSpacesFormatter;
	}

	public static String getReportDateWithSpacesFormatter() {
		return reportDateWithSpacesFormatter;
	}

	// Creates an instance of the reporter
	public static synchronized ExtentReports getReporter() {
		ExtentHtmlReporter htmlReporter;

		if (extent == null) {
			setDatesForReport();
			if (SystemProperties.REPORT_MULTIPLE == true) {
				REPORT_FILE_NAME = REPORT_FILE_NAME_New();
			}

			// Set file location of extent report
			platform = getCurrentPlatform();
			String fileName = getReportFileLocation(platform);
			htmlReporter = new ExtentHtmlReporter(fileName);
			extent = new ExtentReports();

			// Extent report configuration is loaded
			htmlReporter.loadXMLConfig("extent-config.xml");
			extent.attachReporter(htmlReporter);
			if (SystemProperties.GROUP_REPORT) {
				extent.setAnalysisStrategy(AnalysisStrategy.CLASS);
			}
		}
		return extent;
	}

	public static synchronized String getReportFileLocation(Platform platform) {
		String reportFileLocation = null;

		if (SystemProperties.REPORTING_JENKINS_NAME != null && !SystemProperties.REPORTING_JENKINS_NAME.isEmpty()
				&& SystemProperties.REPORT_MULTIPLE == false) {

			if (SystemProperties.REPORTING_JENKINS_NAME.equalsIgnoreCase("${reporting.jenkins.name}")) {
				REPORT_FILE_NAME = "ExtentReports-Version3-Test-Automation-Report.html";

			} else {

				REPORT_FILE_NAME = SystemProperties.REPORTING_JENKINS_NAME;

			}
		}
		// else block to be added

		DEFAULT_NOT_WINDOWS_REPORT_FILE = DEFAULT_NOT_WINDOWS_PATH + File.separator + REPORT_FILE_NAME;
		DEFAULT_WINDOWS_REPORT_FILE = DEFAULT_WINDOWS_PATH + File.separator + REPORT_FILE_NAME;

		if (!SystemProperties.REPORTING_AND_SCREENSHOTS_KEEP_HISTORY) {
			switch (platform) {
			case MAC:
				reportFileLocation = DEFAULT_NOT_WINDOWS_REPORT_FILE;
				createReportPath(DEFAULT_NOT_WINDOWS_PATH);
				Reporting.logReporter(Status.DEBUG, "ExtentReport Path for MAC: " + DEFAULT_NOT_WINDOWS_PATH + "\n");
				break;

			case WINDOWS:
				reportFileLocation = DEFAULT_WINDOWS_REPORT_FILE;
				createReportPath(DEFAULT_WINDOWS_PATH);
				Reporting.logReporter(Status.DEBUG, "ExtentReport Path for WINDOWS: " + DEFAULT_WINDOWS_PATH + "\n");
				break;

			case LINUX:
				reportFileLocation = DEFAULT_NOT_WINDOWS_REPORT_FILE;
				createReportPath(DEFAULT_NOT_WINDOWS_PATH);
				Reporting.logReporter(Status.DEBUG, "ExtentReport Path for LINUX: " + DEFAULT_NOT_WINDOWS_PATH + "\n");
				break;

			default:
				Reporting.logReporter(Status.DEBUG, ERROR_MESSAGE);
				break;
			}
		} else {

			switch (platform) {

			case MAC:
				reportFileLocation = getReportPathLocation(platform) + "/" + REPORT_FILE_NAME;
				createReportPath(getReportPathLocation(platform));
				Reporting.logReporter(Status.DEBUG,
						"ExtentReport Path for MAC: " + getReportPathLocation(platform) + "\n");
				break;

			case WINDOWS:
				reportFileLocation = getReportPathLocation(platform) + REPORT_FILE_NAME;
				createReportPath(getReportPathLocation(platform));
				Reporting.logReporter(Status.DEBUG,
						"ExtentReport Path for WINDOWS: " + getReportPathLocation(platform) + "\n");
				break;

			case LINUX:
				reportFileLocation = getReportPathLocation(platform) + "/" + REPORT_FILE_NAME;
				createReportPath(getReportPathLocation(platform));
				Reporting.logReporter(Status.DEBUG,
						"ExtentReport Path for LINUX: " + getReportPathLocation(platform) + "\n");
				break;

			default:
				Reporting.logReporter(Status.DEBUG, ERROR_MESSAGE);
				break;
			}

		}
		return reportFileLocation;
	}

	public static String getReportPathLocation(Platform platform) {

		if (!SystemProperties.REPORTING_AND_SCREENSHOTS_KEEP_HISTORY) {
			switch (platform) {
			case MAC:
				return DEFAULT_NOT_WINDOWS_REPORT_FILE;
			case WINDOWS:
				return DEFAULT_WINDOWS_REPORT_FILE;
			case LINUX:
				return DEFAULT_NOT_WINDOWS_REPORT_FILE;
			default:
				Reporting.logReporter(Status.DEBUG, ERROR_MESSAGE);
				return null;
			}
		} else {
			switch (platform) {
			case MAC:
				return Screenshots.getReportFolderFullPath();
			case WINDOWS:
				return Screenshots.getReportFolderFullPath();
			case LINUX:
				return Screenshots.getReportFolderFullPath();
			default:
				Reporting.logReporter(Status.DEBUG, ERROR_MESSAGE);
				return null;
			}
		}

	}

	public static String getUniqueDateFormatter() {
		return uniqueDateFormatter;
	}

	public static String getUniqueDateWithoutSpacesFormatter() {
		return uniqueDateWithoutSpacesFormatter;
	}

	public static String getUniqueDateWithSpacesFormatter() {
		return uniqueDateWithSpacesFormatter;
	}

	private static synchronized void setDateFormatterForReport() {
		reportDateFormatter = DatesUtils.FORMAT_YYYY_MM_DD_HH_MM_SS_SEPARATOR_SPACE;
		reportDateWithSpacesFormatter = DatesUtils.FORMAT_YYYY_MM_DD_HH_MM_SS_SEPARATOR_SPACE_ALL;
		reportDateWithoutSpacesFormatter = DatesUtils.FORMAT_YYYY_MM_DD_HH_MM_SS_SEPARATOR_NO_SPACE_ALL;
	}

	/**
	 * Formatters shared across the framework like Screenshots, using the
	 * milliseconds by the usual
	 */
	private static synchronized void setDateFormatterForUniqueFiles() {
		uniqueDateFormatter = DatesUtils.FORMAT_YYYY_MM_DD_HH_MM_SSS_SEPARATOR_UNDERSCORE;
		uniqueDateWithSpacesFormatter = DatesUtils.FORMAT_YYYY_MM_DD_HH_MM_SSS_SEPARATOR_UNDERSCORE_ALL;
		uniqueDateWithoutSpacesFormatter = DatesUtils.FORMAT_YYYY_MM_DD_HH_MM_SSS_SEPARATOR_NO_UNDERSCORE_ALL;
	}

	/**
	 * Feature to call the unit test, only use it for that.
	 */
	public static void setDatesForDebugUnitTest() {
		setDatesForReport();
	}

	private static synchronized void setDatesForReport() {
		setDateFormatterForUniqueFiles();
		setDateFormatterForReport();

		Date todayDate = DatesUtils.getCurrentDate();

		reportDate = DatesUtils.convertDateToString(todayDate, reportDateFormatter);
		reportDateWithSpaces = DatesUtils.convertDateToString(todayDate, reportDateWithSpacesFormatter);
		reportDateWithoutSpaces = DatesUtils.convertDateToString(todayDate, reportDateWithoutSpacesFormatter);
	}

	/**
	 * Feature to call the unit test, only use it for that.
	 */
	public static synchronized void setPlatformForDebugUnitTest(Platform customPlatform) {
		platform = customPlatform;
	}

}
