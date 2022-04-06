package com.test.reporting;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.test.desktop.actions.DesktopDriverSession;
import com.test.files.interaction.ZipFolder;
import com.test.logging.Logging;
import com.test.screenshots.Screenshots;
import com.test.screenvideorecorder.VideoRecorderSession;
import com.test.sdlc.jira.JiraProperties;
import com.test.sdlc.jira.JiraReporting;
import com.test.ui.actions.BaseTest;
import com.test.ui.actions.Validate;
import com.test.ui.actions.WebDriverSession;
import com.test.utils.DatesUtils;
import com.test.utils.SystemProperties;

public class TestListener implements ITestListener {

	// Retrieve Connection and parameters from JSON
	private static final boolean JIRA_CONNECTION_ENABLE = JiraProperties.getIsConnectionEnable();
	private static final boolean JIRA_ATTACHMENTS_ENABLE = JiraProperties.getIsAttachmentsEnable();
	private static final String JIRA_CREATE_ITEM_TO_REPORT = JiraProperties.getSearchModeCreateItemToReport();
	private static final String JIRA_ISSUES_SEARCH_MODE = JiraProperties.getSearchMode();

	// Execution and retry list for renaming tests
	private static List<String> executedAndRetryTestsList = new ArrayList<>();

	// Used internally, not need to add HH MM SS to it
	private static final String INTERNAL_DATE_FORMATER = DatesUtils.FORMAT_YYYY_MM_DD_SEPARATOR_HYPHENS;

	private static final String RETRY_1 = "Retry1_";

	private static final String RETRY_2 = "Retry2_";
	private static final String RETRY_3 = "Retry3_";
	private static final String RETRY_4 = "Retry4_";
	private static final String RETRY_5 = "Retry5_";
	private static final String RETRY_6 = "Retry6_";

	// CONSTANTS FOR MESSAGES THAT FORMULATE THE SUMMARY/DESCRIPTION FOR REPORTING
	// ISSUES
	private static final String FAILED_MESSAGE = "Failed Url --> ";
	private static final String FAILED_MESSAGE_SECOND_PART = " failed: ";
	private static final String TEST_FAILED = " TEST FAILED: ";
	private static final String TEMPLATE_SDLC_TOOL_ITEM_MESSAGE = "[Automation UI] - ";
	private static final String TEMPLATE_REPORT_TITLE = TEMPLATE_SDLC_TOOL_ITEM_MESSAGE
			+ "Execution Report Results - %s";
	protected static final String VARIABLE_CONTEXT_TEST_NAME = "testName";

	private File reportZip = null;
	protected Boolean hasStarted = true;
	private static final String EXCEPTION_MSG = "Exception Caught : ";
	private static final String FAIL_URL_MSG = "Cannot Retrieve Failed Url";

	/**
	 * Method to create a new tests in the report
	 *
	 * @param iTestResult
	 */
	public static void createNewTestReport(ITestResult iTestResult) {

		String clazzName = getTestClassName(iTestResult);

		String testMethodName = getOnStartTestMethodName(iTestResult, executedAndRetryTestsList);

		// Mapping for Executed list
		String fullTestName = getOnStartFullTestMethodName(iTestResult, executedAndRetryTestsList);
		executedAndRetryTestsList.add(fullTestName);

		String reporterName = clazzName + "." + testMethodName;

		// Create Extent Report Implementation
		ExtentTestManager.startTest(reporterName, "");

		Reporting.logReporter(Status.DEBUG, "TestListener.createNewTestReport");
		Reporting.logReporter(Status.DEBUG, "GROUP_REPORT tag property :  " + SystemProperties.GROUP_REPORT);

	}

	private static String getFullTestUniqueId(ITestResult iTestResult) {
		String packageName = getTestPackageName(iTestResult);

		SimpleDateFormat dateFormat = new SimpleDateFormat(INTERNAL_DATE_FORMATER);
		String date = dateFormat.format(new Date());

		String methodName = getTestMethodName(iTestResult);
		return packageName + "." + methodName + "-" + date;
	}

	public static String getNewIssueDescription(Status testStatus, ITestResult result) {
		Object currentClass = result.getInstance();
		WebDriver driver = ((BaseTest) currentClass).getDriver();
		String issueDescriptionTemplate = null;
		String issueDescription = null;
		String methodName = getTestMethodName(result);

		if (!testStatus.equals(Status.PASS)) {
			issueDescriptionTemplate = "[" + methodName + "] Report details in issue with Summary: "
					+ String.format(TEMPLATE_REPORT_TITLE, ExtentManager.getReportDateBaseFormat()) + "\n" + "\n"
					+ "Environment: " + SystemProperties.EXECUTION_ENVIRONMENT + "\n" + "\n" + "Browser: "
					+ SystemProperties.BROWSER + "\n" + "\n" + "Failed URL: " + driver.getCurrentUrl() + "\n" + "\n"
					+ "Reason: " + result.getThrowable().getMessage() + "\n" + "\n" + "Full StackTrace:" + "\n";
		}

		switch (testStatus) {
		case FAIL:
			issueDescription = "Fail " + issueDescriptionTemplate;
			// Append the full stack trace to the description.
			return issueDescription.concat(ExceptionUtils.getFullStackTrace(result.getThrowable()));

		case SKIP:
			issueDescription = "Skip " + issueDescriptionTemplate;
			// Append the full stack trace to the description.
			return issueDescription.concat(ExceptionUtils.getFullStackTrace(result.getThrowable()));

		case PASS:
			return "[" + methodName + "]" + "Test Case Execution Passed. - " + ExtentManager.getReportDateBaseFormat();
		default:
			throw new UnsupportedOperationException("Status not valid - check the options");
		}
	}

	public static String getNewIssueSummary(Status testStatus, ITestResult result) {
		String issueSummary = TEMPLATE_SDLC_TOOL_ITEM_MESSAGE + getSimpleTestUniqueIdSimple(result) + " - Environment: "
				+ SystemProperties.EXECUTION_ENVIRONMENT;

		switch (testStatus) {
		case FAIL:
			return issueSummary;

		case SKIP:
			return issueSummary;

		case PASS:
			return issueSummary;

		default:
			throw new UnsupportedOperationException("Status not valid - check the options");
		}

	}

	private static String getOnStartFullTestMethodName(ITestResult iTestResult, List<String> listToLookIn) {
		String fullTestName = getFullTestUniqueId(iTestResult);
		String retryStartWith = getRetryStartWith(iTestResult, listToLookIn);

		if (retryStartWith != null) {
			fullTestName = retryStartWith + fullTestName;
		}

		return fullTestName;
	}

	private static String getOnStartTestMethodName(ITestResult iTestResult, List<String> listToLookIn) {
		String testMethodName = getTestMethodName(iTestResult);

		if (Retry.getMaxTryNumber() != 0) {
			String retryStartWith = getRetryStartWith(iTestResult, listToLookIn);

			if (retryStartWith != null) {
				testMethodName = retryStartWith + testMethodName;
			}
		}

		return testMethodName;
	}

	/**
	 * Will return the base format for the date, used across reporter and
	 * screenshots, note that this should be invoked after the set.
	 */
	public static String getReportDateFormat(boolean returnWithSpaces) {
		if (returnWithSpaces) {
			return ExtentManager.getReportDateFormatter();
		}
		return ExtentManager.getReportDateWithoutSpacesFormatter();
	}

	/**
	 * Will return the next iteration starting text example input Retry1 output
	 * Retry2
	 *
	 * @param customName
	 * @return
	 */
	private static String getRetryNextIterationName(String originalName) {
		if (originalName.startsWith(RETRY_1)) {
			return RETRY_2;

		} else if (originalName.startsWith(RETRY_2)) {
			return RETRY_3;

		} else if (originalName.startsWith(RETRY_3)) {
			return RETRY_4;

		} else if (originalName.startsWith(RETRY_4)) {
			return RETRY_5;

		} else if (originalName.startsWith(RETRY_6)) {
			return RETRY_6;
		}
		return null;
	}

	/**
	 * Analyze if the given was already executed, and if so if it was the 1st retry
	 * or not
	 *
	 * @param iTestResult
	 * @return
	 */
	private static String getRetryStartWith(ITestResult iTestResult, List<String> listToLookIn) {
		String retryStartWith = null;

		String defaultNewFailedTestName = getFullTestUniqueId(iTestResult);
		String firstRetryNewFailedTestName = RETRY_1 + defaultNewFailedTestName;
		String secondRetryNewFailedTestName = RETRY_2 + defaultNewFailedTestName;
		String thirdRetryNewFailedTestName = RETRY_3 + defaultNewFailedTestName;
		String fourthRetryNewFailedTestName = RETRY_4 + defaultNewFailedTestName;
		String fifthRetryNewFailedTestName = RETRY_5 + defaultNewFailedTestName;

		boolean defaultTestIsInList = listToLookIn.contains(defaultNewFailedTestName);
		boolean firstRetryTestIsInList = listToLookIn.contains(firstRetryNewFailedTestName);
		boolean secondRetryIsInList = listToLookIn.contains(secondRetryNewFailedTestName);
		boolean thirdRetryIsInList = listToLookIn.contains(thirdRetryNewFailedTestName);
		boolean fourthRetryIsInList = listToLookIn.contains(fourthRetryNewFailedTestName);
		boolean fifthRetryIsInList = listToLookIn.contains(fifthRetryNewFailedTestName);

		boolean isRetryWordPresentInDefaultTest = defaultNewFailedTestName.startsWith("Retry");
		boolean retryCondition1 = firstRetryTestIsInList || secondRetryIsInList || thirdRetryIsInList;
		boolean isRetryWordPresentInAnyRetry = retryCondition1 || fourthRetryIsInList || fifthRetryIsInList;

		String lastFailedTestName = null;
		if (isRetryWordPresentInAnyRetry) {
			if (firstRetryTestIsInList) {
				lastFailedTestName = firstRetryNewFailedTestName;
			}
			if (secondRetryIsInList) {
				lastFailedTestName = secondRetryNewFailedTestName;
			}
			if (thirdRetryIsInList) {
				lastFailedTestName = thirdRetryNewFailedTestName;
			}
			if (fourthRetryIsInList) {
				lastFailedTestName = fourthRetryNewFailedTestName;
			}
			if (fifthRetryIsInList) {
				lastFailedTestName = fifthRetryNewFailedTestName;
			}
		}

		if (defaultTestIsInList && isRetryWordPresentInAnyRetry) {
			retryStartWith = getRetryNextIterationName(lastFailedTestName);
			return retryStartWith;

		} else if (defaultTestIsInList && !isRetryWordPresentInDefaultTest) {
			retryStartWith = RETRY_1;
			return retryStartWith;

		} else {
			return retryStartWith;
		}
	}

	/**
	 * Will extract and take back the retry iteration from the string
	 *
	 * @param customName
	 * @return
	 */
	private static String getRetryStartWithFromCustomName(String customName) {
		if (customName.startsWith(RETRY_1)) {
			return RETRY_1;

		} else if (customName.startsWith(RETRY_2)) {
			return RETRY_2;

		} else if (customName.startsWith(RETRY_3)) {
			return RETRY_3;

		} else if (customName.startsWith(RETRY_4)) {
			return RETRY_4;

		} else if (customName.startsWith(RETRY_5)) {
			return RETRY_5;

		} else if (customName.startsWith(RETRY_6)) {
			return RETRY_6;
		}
		return null;
	}

	private static String getSimpleTestUniqueId(ITestResult iTestResult) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(INTERNAL_DATE_FORMATER);
		String date = dateFormat.format(new Date());

		String clazzAndMethodName = getSimpleTestUniqueIdSimple(iTestResult);
		return clazzAndMethodName + "-" + date;
	}

	private static String getSimpleTestUniqueIdSimple(ITestResult iTestResult) {
		String clazzName = getTestClassName(iTestResult);
		String methodName = getTestMethodName(iTestResult);
		return clazzName + "." + methodName;
	}

	private static String getSimpleTestUniqueIdWithCustomName(ITestResult iTestResult, String customName) {
		String startWith = "";
		if (getRetryStartWithFromCustomName(customName) != null) {
			startWith = getRetryStartWithFromCustomName(customName);
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat(INTERNAL_DATE_FORMATER);
		String date = dateFormat.format(new Date());
		String clazzName = getTestClassName(iTestResult);
		String methodName = getTestMethodName(iTestResult);
		return startWith + clazzName + "." + methodName + "-" + date;
	}

	private static String getTestClassName(ITestResult iTestResult) {
		String pack = getTestPackageName(iTestResult);
		List<String> list = Arrays.asList(pack.split(File.separator + "."));

		if (list.isEmpty()) {
			Reporting.logReporter(Status.DEBUG,
					"Could not retrieve ClassName - Methods for identify Duplicated / retry test will fail");
			return "ClassName";
		}
		// System.out.println("SessionId" + Thread.currentThread().getId() + " - " +
		// "getTestClassName--> " + list.get(list.size() - 1));// TO BE IMPLEMENTED
		return list.get(list.size() - 1);
	}

	public static String getTestMethodName(ITestResult iTestResult) {
		if (iTestResult.getTestContext().getAttribute(VARIABLE_CONTEXT_TEST_NAME) == null) { // Regular Code (Not Data
																								// From Data Provider)
			return iTestResult.getMethod().getConstructorOrMethod().getName();
		} else { // Modification to read the DataProvider Modified name
			// System.out.println("SessionId" + Thread.currentThread().getId() + " - " +
			// "getTestMethodName--> ORIGINAL NAME= " +
			// iTestResult.getMethod().getConstructorOrMethod().getName());// TO BE
			// IMPLEMENTED
			// System.out
			// .println("SessionId" + Thread.currentThread().getId() + " - " +
			// "getTestMethodName--> NEW NAME" +
			// iTestResult.getTestContext().getAttribute(VARIABLE_CONTEXT_TEST_NAME).toString());//
			// TO BE IMPLEMENTED
			return iTestResult.getTestContext().getAttribute(VARIABLE_CONTEXT_TEST_NAME).toString();
		}
	}

	private static String getTestPackageName(ITestResult iTestResult) {
		return iTestResult.getTestClass().getName();
	}

	public static String getUpdatedDescriptionForExistingIssue(Status testStatus, ITestResult result) {
		String dateData = DatesUtils.convertDateToString(DatesUtils.getCurrentDate(), INTERNAL_DATE_FORMATER);

		String description = "\n" + "\n" + "----------------" + dateData + "------------------" + "\n" + "\n";
		description = description + getNewIssueDescription(testStatus, result);

		return description;
	}

	/**
	 * Method to print status out of the logger, to visualize time to create
	 * report/update external tools with the report information
	 */
	private static void printTraceByDateTime(String message) {
		Date todayDate = DatesUtils.getCurrentDate();

		String dateReportEnd = DatesUtils.convertDateToString(todayDate,
				DatesUtils.FORMAT_YYYY_MM_DD_HH_MM_SS_SEPARATOR_SPACE);
		Reporting.logReporter(Status.DEBUG, message + dateReportEnd);

	}

	private String removeExtentReportsPath = "";

	private File generateReportAttachmentFile() {
		String originalReportPath = null;
		String newReportPath = null;
		// NOTE: Do not use the target as will be a really heavy file

		if (SystemProperties.REPORTING_JENKINS_NAME != null && !SystemProperties.REPORTING_JENKINS_NAME.isEmpty()) {

			if (SystemProperties.REPORTING_JENKINS_NAME.equalsIgnoreCase("${reporting.jenkins.name}")) {
				if (SystemProperties.REPORT_MULTIPLE == (true)) {
					removeExtentReportsPath = File.separator + ExtentManager.name;
				} else {
					removeExtentReportsPath = File.separator + "ExtentReports-Version3-Test-Automation-Report.html";

				}
			} else {
				removeExtentReportsPath = File.separator + SystemProperties.REPORTING_JENKINS_NAME;
			}

		} else {
			if (SystemProperties.REPORT_MULTIPLE == (true)) {
				removeExtentReportsPath = File.separator + ExtentManager.name;
			} else {
				removeExtentReportsPath = File.separator + "ExtentReports-Version3-Test-Automation-Report.html";
			}
		}

		originalReportPath = ExtentManager.getReportFileLocation(ExtentManager.getCurrentPlatform());
		originalReportPath = originalReportPath.replace(removeExtentReportsPath, "");

		newReportPath = originalReportPath + ".zip";

		ZipFolder.createZipWithFolderInternalElements(originalReportPath, newReportPath);

		java.io.File tempFile = null;

		try {
			tempFile = new java.io.File(newReportPath);
			Reporting.logReporter(Status.DEBUG, newReportPath + " - File Located");
		} catch (NullPointerException e) {
			tempFile = null;
			Reporting.logReporter(Status.DEBUG,
					newReportPath + " - File NOT Located, please review it as it was created succesfully" + e);
		}

		return tempFile;
	}

	// After ending all tests, below method runs.
	@Override
	public void onFinish(ITestContext iTestContext) {
		// ExtentTestManager.endTest();
		ExtentManager.getReporter().flush();
		printTraceByDateTime("Report created and published done: ");

		this.reportZip = this.generateReportAttachmentFile();
		printTraceByDateTime("ZIP created and published done: ");

		this.updateJiraWithExtentReportFile();
	}

	// Before starting all tests, below method runs.
	@Override
	public void onStart(ITestContext iTestContext) {
		this.hasStarted = true;
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
		Reporting.logReporter(Status.DEBUG, "TestListener.onTestFailedButWithinSuccessPercentage");

		Object currentClass = iTestResult.getInstance();
		WebDriver driver = ((BaseTest) currentClass).getDriver();

		Reporting.logReporter(Status.FAIL,
				" TEST FAILED WITH SUCCESS PERCENTAGE: " + iTestResult.getMethod().getConstructorOrMethod().getName()
						+ " failed with success percentage : " + iTestResult.getThrowable().toString());

		Reporting.logReporter(Status.FAIL, FAILED_MESSAGE + driver.getCurrentUrl());

		VideoRecorderSession.stopRecording();
		VideoRecorderSession.afterStopRecordingDeleteVideoFileIfNeeded(getSimpleTestUniqueId(iTestResult), Status.FAIL);

	}

	@Override
	public void onTestFailure(ITestResult iTestResult) {
		Reporting.logReporter(Status.DEBUG, "TestListener.onTestFailure");
		Object currentClass = iTestResult.getInstance();

		String fullFilePath = Screenshots.getFailedScreenShotFullPathWithTestId(getTestMethodName(iTestResult));
		String relativeFilePath = Screenshots.getScreenShotRelativePath(fullFilePath);

		WebDriver driver = null;
		WebDriver desktopDriver = null;

		boolean multipleDriversActive = false;

		// Get drivers
		if (DesktopDriverSession.getDesktopDriverForCurrentThreat() != null
				&& WebDriverSession.getWebDriverForCurrentThreat() != null) { // Scenario for both drivers active
			multipleDriversActive = true;

			driver = ((BaseTest) currentClass).getDriver();
			desktopDriver = ((BaseTest) currentClass).getDesktopDriver();
		} else if (DesktopDriverSession.getDesktopDriverForCurrentThreat() != null) {// Scenario for single driver
																						// active, will validate which
																						// one is active and use it as
																						// main
																						// driver
			driver = ((BaseTest) currentClass).getDesktopDriver();
		} else if (WebDriverSession.getWebDriverForCurrentThreat() != null) {
			driver = ((BaseTest) currentClass).getDriver();
		}

		// Take Screenshot (single driver can be any of both versions)
		String driverScreenshot = null;
		
		if(SystemProperties.FULL_SCREENSHOT=false)
		{
		
		driverScreenshot = Screenshots.takeScreenshot(fullFilePath, driver);
		}else
		{
			driverScreenshot = Screenshots.takeScreenshotFull(fullFilePath, driver);
		}
		
		
		if (driverScreenshot != null) {
			try {
				Reporting.logReporter(Status.FAIL,
						TEST_FAILED + getTestMethodName(iTestResult) + FAILED_MESSAGE_SECOND_PART
								+ iTestResult.getThrowable().toString(),
						MediaEntityBuilder.createScreenCaptureFromPath(relativeFilePath).build());
				if (SystemProperties.GROUP_REPORT && !"".equals(Reporting.groupTag.get())) {

					Reporting.logReporter(Status.FAIL, relativeFilePath); // Concatenate screenshot in current group

				}

			} catch (IOException e) {

				Reporting.logReporter(Status.FAIL,
						TEST_FAILED + iTestResult.getMethod().getConstructorOrMethod().getName()
								+ FAILED_MESSAGE_SECOND_PART + iTestResult.getThrowable().toString());
				Logging.logReporter(Status.DEBUG, EXCEPTION_MSG + e);
			}
		} else {
			Reporting.logReporter(Status.FAIL, TEST_FAILED + iTestResult.getMethod().getConstructorOrMethod().getName()
					+ FAILED_MESSAGE_SECOND_PART + iTestResult.getThrowable().toString());
		}

		// Take Screenshot (both drivers)
		if (multipleDriversActive) {

			String desktopScreenshot = null;
			String webScreenshot = null;

			desktopScreenshot = Screenshots.takeScreenshot(fullFilePath, desktopDriver);
			webScreenshot = Screenshots.takeScreenshot(fullFilePath, driver);

			// TAKE WEB SS
			if (webScreenshot != null) {
				try {
					Reporting.logReporter(Status.FAIL,
							TEST_FAILED + getTestMethodName(iTestResult) + FAILED_MESSAGE_SECOND_PART
									+ iTestResult.getThrowable().toString(),
							MediaEntityBuilder.createScreenCaptureFromPath(relativeFilePath).build());
					if (SystemProperties.GROUP_REPORT && !"".equals(Reporting.groupTag.get())) {

						Reporting.logReporter(Status.FAIL, relativeFilePath); // Concatenate screenshot in current group

					}

				} catch (IOException e) {
					Reporting.logReporter(Status.FAIL,
							TEST_FAILED + iTestResult.getMethod().getConstructorOrMethod().getName()
									+ FAILED_MESSAGE_SECOND_PART + iTestResult.getThrowable().toString());
					Logging.logReporter(Status.DEBUG, EXCEPTION_MSG + e);
				}
			} else {
				Reporting.logReporter(Status.FAIL,
						TEST_FAILED + iTestResult.getMethod().getConstructorOrMethod().getName()
								+ FAILED_MESSAGE_SECOND_PART + iTestResult.getThrowable().toString());
			}

			// TAKE DESKTOP SS
			if (desktopScreenshot != null) {
				try {
					Reporting.logReporter(Status.FAIL,
							TEST_FAILED + getTestMethodName(iTestResult) + FAILED_MESSAGE_SECOND_PART
									+ iTestResult.getThrowable().toString(),
							MediaEntityBuilder.createScreenCaptureFromPath(relativeFilePath).build());

					if (SystemProperties.GROUP_REPORT && !"".equals(Reporting.groupTag.get())) {

						Reporting.logReporter(Status.FAIL, relativeFilePath); // Concatenate screenshot in current group

					}
				} catch (IOException e) {
					Reporting.logReporter(Status.FAIL,
							TEST_FAILED + iTestResult.getMethod().getConstructorOrMethod().getName()
									+ FAILED_MESSAGE_SECOND_PART + iTestResult.getThrowable().toString());
					Logging.logReporter(Status.DEBUG, EXCEPTION_MSG + e);
				}
			} else {
				Reporting.logReporter(Status.FAIL,
						TEST_FAILED + iTestResult.getMethod().getConstructorOrMethod().getName()
								+ FAILED_MESSAGE_SECOND_PART + iTestResult.getThrowable().toString());
			}
		}

		// Print final msg
		if (WebDriverSession.getWebDriverForCurrentThreat() != null) {
			String failedUrl = FAIL_URL_MSG;

			try {
				if (driver != null) {
					failedUrl = driver.getCurrentUrl();
				}
			} catch (RuntimeException e) {
				Logging.logReporter(Status.DEBUG, EXCEPTION_MSG + e);
				// Catch any error, nothing to do
			}

			Reporting.logReporter(Status.FAIL, FAILED_MESSAGE + failedUrl);
		}

		VideoRecorderSession.stopRecording();
		VideoRecorderSession.afterStopRecordingDeleteVideoFileIfNeeded(getSimpleTestUniqueId(iTestResult), Status.FAIL);

		this.updateJiraTestResults(iTestResult, Status.FAIL);
		Reporting.printAndClearLogGroupStatements();

	}

	@Override
	public void onTestSkipped(ITestResult iTestResult) {
		Reporting.logReporter(Status.DEBUG, "TestListener.onTestSkipped");

		String testMethodName = getSimpleTestUniqueId(iTestResult);
		boolean isInList = executedAndRetryTestsList.contains(getFullTestUniqueId(iTestResult));

		Object currentClass = iTestResult.getInstance();

		WebDriver driver = null;
		WebDriver desktopDriver = null;

		boolean multipleDriversActive = false;

		// Get drivers
		if (DesktopDriverSession.getDesktopDriverForCurrentThreat() != null
				&& WebDriverSession.getWebDriverForCurrentThreat() != null) { // Scenario for both drivers active
			multipleDriversActive = true;

			driver = ((BaseTest) currentClass).getDriver();
			desktopDriver = ((BaseTest) currentClass).getDesktopDriver();
		} else if (DesktopDriverSession.getDesktopDriverForCurrentThreat() != null) {// Scenario for single driver
																						// active, will validate which
																						// one is active and use it as
																						// main
																						// driver
			driver = ((BaseTest) currentClass).getDesktopDriver();
		} else if (WebDriverSession.getWebDriverForCurrentThreat() != null) {
			driver = ((BaseTest) currentClass).getDriver();
		}

		if (!isInList) {// REAL SKIP
			String newFailedTestName = getSimpleTestUniqueId(iTestResult);

			String failedUrlMessage = FAIL_URL_MSG;
			try {
				if (driver != null) {
					failedUrlMessage = driver.getCurrentUrl();
				}
			} catch (RuntimeException e) {
				Logging.logReporter(Status.DEBUG, EXCEPTION_MSG + e);
				// Catch any error, nothing to do
			}

			// Print final msg
			if (WebDriverSession.getWebDriverForCurrentThreat() != null) {

				Reporting.logReporter(Status.DEBUG, "SKIP TEST - FAILED URL= " + failedUrlMessage);

			}

			String errorMessage = newFailedTestName + " skipped : " + iTestResult.getThrowable().toString();

			Reporting.logReporter(Status.DEBUG, "SKIP TEST - ERROR MSG= " + errorMessage);

			VideoRecorderSession.stopRecording();
			// FOR THIS STATUS SHOULD BE ALWAYS SKIP
			VideoRecorderSession.afterStopRecordingDeleteVideoFileIfNeeded(newFailedTestName, Status.SKIP);
			this.updateJiraTestResults(iTestResult, Status.SKIP);

		}

		else { // WORK-AROUND FOR FAILED TEST (WHEN RETRY IS ON WILL BE MARKED AS SKIPPED)
			Reporting.logReporter(Status.FAIL, TEST_FAILED + testMethodName + FAILED_MESSAGE_SECOND_PART + " "
					+ iTestResult.getThrowable().toString());

			String failedUrlMessage = FAIL_URL_MSG;
			try {
				if (driver != null) {
					failedUrlMessage = driver.getCurrentUrl();
				}
			} catch (RuntimeException e) {
				Logging.logReporter(Status.DEBUG, EXCEPTION_MSG + e);
				// Catch any error, nothing to do
			}

			// Print final msg
			if (WebDriverSession.getWebDriverForCurrentThreat() != null) {

				Reporting.logReporter(Status.FAIL, "FAILED TEST - FAILED URL= " + failedUrlMessage);

			}

			VideoRecorderSession.stopRecording();
			VideoRecorderSession.afterStopRecordingDeleteVideoFileIfNeeded(testMethodName, Status.FAIL);
			this.updateJiraTestResults(iTestResult, Status.FAIL);
		}
	}

	@Override
	public void onTestStart(ITestResult iTestResult) {

		if (SystemProperties.REPORT_MULTIPLE == (true)) {
			if (ExtentManager.extent == null) {
				createNewTestReport(iTestResult);
			} else {
				ExtentTestManager.endTest();
				createNewTestReport(iTestResult);
			}
		} else {
			createNewTestReport(iTestResult);
		}
		String testMethodName = getOnStartTestMethodName(iTestResult, executedAndRetryTestsList);

		VideoRecorderSession.startRecording(getSimpleTestUniqueIdWithCustomName(iTestResult, testMethodName));

		Reporting.logReporter(Status.DEBUG, "TestListener.onTestStart");
	}

	@Override
	public void onTestSuccess(ITestResult iTestResult) {
		Reporting.printAndClearLogGroupStatements();
		Reporting.logReporter(Status.DEBUG, "TestListener.onTestSuccess");

		VideoRecorderSession.stopRecording();
		VideoRecorderSession.afterStopRecordingDeleteVideoFileIfNeeded(getSimpleTestUniqueId(iTestResult), Status.PASS);
		this.updateJiraTestResults(iTestResult, Status.PASS);

	}

	/**
	 * EXECUTE JIRA ISSUE CREATION/UPDATE
	 */
	private void updateJiraTestResults(ITestResult iTestResult, Status testStatus) {
		String methodName = getTestMethodName(iTestResult);

		if (JIRA_CONNECTION_ENABLE && JIRA_ATTACHMENTS_ENABLE && SystemProperties.EXTENT_REPORT) {
			String beginMessage = "Begin updateJiraTestResults - TestName =" + methodName + " ";

			String type = JIRA_ISSUES_SEARCH_MODE;
			switch (type) {
			case "SUMMARY":
				printTraceByDateTime(beginMessage);
				JiraReporting.updateJiraExistingIssueOrCreateNew(testStatus, iTestResult, JIRA_CREATE_ITEM_TO_REPORT,
						null);
				printTraceByDateTime("Finish updateJiraTestResults - TestName =" + methodName);
				break;

			case "ANNOTATION":
				if (TestMethodListener.getJiraId() != null) {
					printTraceByDateTime(beginMessage);
					JiraReporting.updateJiraExistingIssue(testStatus, iTestResult, JIRA_CREATE_ITEM_TO_REPORT,
							TestMethodListener.getJiraId(), null);
					printTraceByDateTime("Finish updateJiraTestResults - TestName =" + methodName);
				}
				break;

			default:
				Reporting.logReporter(Status.DEBUG,
						"The provided type[" + type + "] is not supported, no action will be executed");
				break;
			}

		}

		else {
			Reporting.logReporter(Status.DEBUG, "JIRA - Skipping Jira Update as Toggle it's false");
		}
	}

	/**
	 * At the end of the execution will create a new item and attach the report to
	 * it.
	 */
	private void updateJiraWithExtentReportFile() {
		if (JIRA_CONNECTION_ENABLE && JIRA_ATTACHMENTS_ENABLE && SystemProperties.EXTENT_REPORT) {

			String todayDate = ExtentManager.getReportDateBaseFormat();
			String summary = String.format(TEMPLATE_REPORT_TITLE, todayDate);
			String description = summary + " -  Attached is the Extent report and images";

			JiraReporting.createJiraIssue(JIRA_CREATE_ITEM_TO_REPORT, this.reportZip, summary, description);
			printTraceByDateTime("JIRA Finish upload of report: ");
		}

		else {
			Reporting.logReporter(Status.DEBUG, "JIRA - Skipping Jira Upload report file as Toggle it's false");
		}
	}

}
