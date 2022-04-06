package com.test.sdlc.jira;

import java.io.File;

import org.testng.ITestResult;

import com.aventstack.extentreports.Status;
import com.test.reporting.Reporting;
import com.test.reporting.TestListener;
import com.test.sdlc.jira.JiraServiceProvider.IssueTypeOptions;
import com.test.utils.EncryptionUtils;

import net.rcarz.jiraclient.Issue.SearchResult;

/**
 * Class to create connection and basic calls to interact with JIRA Server
 */
public class JiraReporting {
	
	private JiraReporting() {
		    throw new IllegalStateException("Utility class");
		  }

    // Retrieve Connection and parameters from JSON
    private static final String JIRA_SERVER_URL = JiraProperties.getServerUrl();
    private static final String JIRA_USER = JiraProperties.getUser();
    private static final String JIRA_PASSWORD = EncryptionUtils.decode(JiraProperties.getPassword());
    private static final String JIRA_PROJECT_KEY = JiraProperties.getProjectKey();
    private static final String JIRA_BASE_QUERY_OPTION = JiraProperties.getSearchModeSearchJqlBaseQuery();

    /**
     * Create new issue
     */
    private static String createJiraIssue(Status testStatus, ITestResult result, JiraServiceProvider.IssueTypeOptions issueType, File file) {


        JiraServiceProvider jiraSP = getJiraConnection();

        String issueSummary = TestListener.getNewIssueSummary(testStatus, result);
        String issueDescription = TestListener.getNewIssueDescription(testStatus, result);

        // Create issue
        return jiraSP.createJiraIssue(issueType, issueSummary, issueDescription, JIRA_USER, file);
    }

    public static String createJiraIssue(String issueTypeValue, File file, String issueSummary, String issueDescription) {

        JiraServiceProvider jiraSP = getJiraConnection();
        IssueTypeOptions issueType = JiraServiceProvider.getIssueTypeOptions(issueTypeValue);
        // Create issue
        return jiraSP.createJiraIssue(issueType, issueSummary, issueDescription, JIRA_USER, file);
    }

    /**
     * Open and Get the connection
     **/
    private static JiraServiceProvider getJiraConnection() {
        return new JiraServiceProvider(JIRA_SERVER_URL, JIRA_USER, JIRA_PASSWORD, JIRA_PROJECT_KEY);
    }

    /**
     * Will return the basic JQLQuery sentence based in the property value
     */
    private static String getJiraJqlQuery() {
        String templateBaseType = "resolution = Unresolved AND type = %s";
        String templateAnyType = "resolution = Unresolved";

        switch (JIRA_BASE_QUERY_OPTION) {
            case "Open_AnyType":
                return templateAnyType;
            case "Open_Epic":
                return String.format(templateBaseType, "Epic");
            case "Open_Story":
                return String.format(templateBaseType, "Story");
            case "Open_Task":
                return String.format(templateBaseType, "Task");
            case "Open_Bug":
                return String.format(templateBaseType, "Bug");
            case "Open_Test":
                return String.format(templateBaseType, "Test");
            default:
                // To avoid exception will use default as Test
                return String.format(templateBaseType, "Test");
        }
    }

    public static void testParametersFromJson() {
    	
    	 Reporting.logReporter(Status.DEBUG, "\nJIRA_SERVER_URL-> " + JIRA_SERVER_URL);
    	 Reporting.logReporter(Status.DEBUG, "\nJIRA_USER-> " + JIRA_USER);
    	 Reporting.logReporter(Status.DEBUG, "\nJIRA_PASSWORD> " + JIRA_PASSWORD);


    	 Reporting.logReporter(Status.DEBUG, "\nJIRA_PROJECT_KEY-> " + JIRA_PROJECT_KEY);

    	 
    	 Reporting.logReporter(Status.DEBUG, "\nJIRA_BASE_QUERY_OPTION-> " + JIRA_BASE_QUERY_OPTION);

    	 Reporting.logReporter(Status.DEBUG, "\n");

    }

    /**
     * Add an attachment to an specific issue ID
     *
     * @param issueKey
     * @param attachFile
     */
    public static void updateAttachmentToExistingItems(String issueKey, File attachFile) {
        JiraServiceProvider jiraSP = getJiraConnection();
        jiraSP.updateJiraIssueAttachment(issueKey, attachFile);
    }

    /**
     * Update an issue - when we know the specific Key
     */
    public static boolean updateJiraExistingIssue(Status testStatus, ITestResult result, String issueTypeValue, String issueKey, File attachFile) {
        Reporting.logReporter(Status.DEBUG, "JiraReporting - updateJiraExistingIssue");
        JiraServiceProvider jiraSP = getJiraConnection();
        String issueData = TestListener.getUpdatedDescriptionForExistingIssue(testStatus, result);

        JiraServiceProvider.IssueTypeOptions issueTypeOption = JiraServiceProvider.getIssueTypeOptions(issueTypeValue);

        return jiraSP.updateJiraIssue(testStatus, issueTypeOption, issueKey, issueData, attachFile);
    }

    /**
     * Will check the properties flag and will skip if it's false
     */
    private static void updateJiraExistingIssueOrCreateNew(Status testStatus, ITestResult iTestResult, JiraServiceProvider.IssueTypeOptions issueType, File file) {
        if (!updateJiraExistingIssues(testStatus, iTestResult, file)) {
            String issueId = createJiraIssue(testStatus, iTestResult, issueType, file);

            if (issueId != null && issueType.equals(JiraServiceProvider.IssueTypeOptions.TEST)) {
                JiraServiceProvider.createJiraZapiTestExecutionAndUpdateExecutionStatus(issueId, testStatus);
            }
        }
    }

    /**
     * Main Method will review if there are already items that match the JQL query, if so will update them. Else will create a new item and will add the data to it.
     */
    public static void updateJiraExistingIssueOrCreateNew(Status testStatus, ITestResult iTestresult, String issueTypeValue, File file) {
        Reporting.logReporter(Status.DEBUG, "JiraReporting - updateJiraExistingIssueOrCreateNew");

        JiraServiceProvider.IssueTypeOptions issueTypeOption = JiraServiceProvider.getIssueTypeOptions(issueTypeValue);
        updateJiraExistingIssueOrCreateNew(testStatus, iTestresult, issueTypeOption, file);
    }

    /**
     * Look for the issue by jql sentence, obtain a list and iterate in all items to try to update the data.
     */
    private static boolean updateJiraExistingIssues(Status testStatus, ITestResult iTestResult, File attachFile) {
        JiraServiceProvider jiraSP = getJiraConnection();

        String issueSummary = TestListener.getNewIssueSummary(testStatus, iTestResult);

        String jqlP1 = getJiraJqlQuery();
        String jqlP2 = " AND summary ~ " + "\"" + "\\" + "\"" + issueSummary + "\\" + "\"" + "\"";
        String jqlP3 = " order by created DESC";
        String jql = jqlP1 + jqlP2 + jqlP3;
        Reporting.logReporter(Status.DEBUG, "JIRA - jql: " + jql);

        SearchResult searchResults = jiraSP.getOpenIssuesWithCustomSearchCriteria(jql);
        if (searchResults.issues.isEmpty()) {
            Reporting.logReporter(Status.DEBUG, "JIRA - jql search result: emtpy or null");
            return false;
        }
        String issueDescription = TestListener.getUpdatedDescriptionForExistingIssue(testStatus, iTestResult);
        return jiraSP.updateJiraIssues(testStatus, jql, issueDescription, attachFile);
    }

}
