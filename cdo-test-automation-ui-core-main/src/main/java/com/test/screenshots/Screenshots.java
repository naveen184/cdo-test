package com.test.screenshots;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.Status;
import com.test.logging.Logging;
import com.test.reporting.ExtentManager;
import com.test.reporting.Reporting;
import com.test.ui.actions.BaseSteps;
import com.test.ui.actions.WebDriverSession;
import com.test.ui.actions.WebDriverSteps;
import com.test.utils.SystemProperties;

import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

/**
 * Class that holds all screenshots logic
 *
 */
public class Screenshots {

	 private static final String WINDOWS_DEFAULT_PATH = ExtentManager.DEFAULT_WINDOWS_REPORT_PARTIAL_PATH + File.separator; // "\\TestReport\\"
    private static final String CUSTOM_HISTORY_PATH = SystemProperties.REPORTING_AND_SCREENSHOTS_KEEP_HISTORY_PATH;

    public static final String WINDOWS_FAILED_SS_FOLDER = "FailedTestsScreenshots" + File.separator;
    public static final String WINDOWS_PASSED_SS_FOLDER = "PassedTestsScreenshots" + File.separator;
    public static final String WINDOWS_SKIPPED_SS_FOLDER = "SkippedTestsScreenshots" + File.separator;

    public static final String WINDOWS_VALIDATE_PASS_MESSAGE = "Validate Pass_";
    public static final String WINDOWS_VALIDATE_FAIL_MESSAGE = "Validate Fail_";
    public static final String WINDOWS_VALIDATE_SKIP_MESSAGE = "Validate Skip_";

    private static final String WINDOWS_RELATIVE_PATH_HTML_TEMPLATE_CUSTOM = "." + File.separator; // Designed for the CUSTOM value   
    private static final String WINDOWS_RELATIVE_PATH_HTML_TEMPLATE_DEFAULT = "."; // "..\\.."; // Designed for the DEFAULT value

    private static final String PNG_IMAGE = ".png";

    private Screenshots() {
        throw new IllegalStateException("Utility class - Not Designed to be Instantiated");
    }

    public static String getBaseFolderPath() {
        if (!SystemProperties.REPORTING_AND_SCREENSHOTS_KEEP_HISTORY) {
            return WINDOWS_DEFAULT_PATH;
        } else {
            return CUSTOM_HISTORY_PATH + ExtentManager.getReportDateBaseFormatWithSpaces() + File.separator;
        }
    }

    private static String getDataDate() {
        return new SimpleDateFormat(ExtentManager.getUniqueDateWithoutSpacesFormatter()).format(new Date());
    }

    public static String getDataFullFailedFolder() {
    	if(SystemProperties.REPORTING_SCREENSHOT_NEW_NAME_FORMAT) {
      	  String testCaseMethodName = callingTestAndMethodName();
      	  return getBaseFolderPath() + WINDOWS_FAILED_SS_FOLDER + testCaseMethodName + "_Fail_" + getDataDate() + PNG_IMAGE;
      	}
      	else {
      		return getBaseFolderPath() + WINDOWS_FAILED_SS_FOLDER + WINDOWS_VALIDATE_FAIL_MESSAGE + getDataDate() + PNG_IMAGE;
      	}
    }

    public static String getDataFullFailedFolderWithTestId(String testId) {//
        return getBaseFolderPath() + WINDOWS_FAILED_SS_FOLDER + testId + "_" + getDataDate() + PNG_IMAGE;
    }
    
    private static String callingTestAndMethodName() {
    	StackTraceElement sts[]=Thread.currentThread().getStackTrace();
    	String testCaseMethodName = "";
    	String currentPageTitle = "";
    	
          if(WebDriverSession.map.get(Thread.currentThread().getId()) != null && WebDriverSteps.getWebDriverSession().getTitle()!=null && (!"untitled".equalsIgnoreCase(WebDriverSteps.getWebDriverSession().getTitle()))) {
        	  currentPageTitle=WebDriverSteps.getWebDriverSession().getTitle().replaceAll("[^a-zA-Z0-9]", "");
          }
	      for (int i=1; i<sts.length; i++)
	      {	
	      	if((!sts[i].getClassName().equals("com.test.screenshots.Screenshots")) && (!sts[i].getClassName().equals("com.test.ui.actions.Validate"))) {
	      		
	      		testCaseMethodName = sts[i].getClassName().substring(sts[i].getClassName().lastIndexOf('.')+1)+"_"+sts[i].getMethodName()+"_"+ currentPageTitle;
	      		//System.out.println(testCaseMethodName.length())
	      		if(testCaseMethodName.length()>=213)
	      		{
	      			testCaseMethodName= testCaseMethodName.substring(0,212);
	      		}
	      		break;
	      	}
	      }
    	
    	return testCaseMethodName;
    }

    public static String getDataFullPassedFolder() {
    	if(SystemProperties.REPORTING_SCREENSHOT_NEW_NAME_FORMAT) {
    	  String testCaseMethodName = callingTestAndMethodName();
    	  return getBaseFolderPath() + WINDOWS_PASSED_SS_FOLDER + testCaseMethodName + "_Pass_" + getDataDate() + PNG_IMAGE;
    	}
    	else {
    		  return getBaseFolderPath() + WINDOWS_PASSED_SS_FOLDER + WINDOWS_VALIDATE_PASS_MESSAGE + getDataDate() + PNG_IMAGE;
    	}
    	}

    public static String getDataFullPassedFolderWithTestId(String testId) {//
        return getBaseFolderPath() + WINDOWS_PASSED_SS_FOLDER + testId + "_" + getDataDate() + PNG_IMAGE;
    }

    public static String getDataFullSkippedFolder() {
        return getBaseFolderPath() + WINDOWS_SKIPPED_SS_FOLDER + WINDOWS_VALIDATE_SKIP_MESSAGE + getDataDate() + PNG_IMAGE;
    }

    public static String getDataFullSkippedFolderWithTestId(String testId) {//
        return getBaseFolderPath() + WINDOWS_SKIPPED_SS_FOLDER + testId + "_" + getDataDate() + PNG_IMAGE;
    }

    public static String getDataProjectPath() {//
        return System.getProperty("user.dir");
    }

    /**
     * To get a relative path accept any kind (PASS/FAIL)
     *
     */
    public static String getFailedScreenShotFullPath() {//
        if (!SystemProperties.REPORTING_AND_SCREENSHOTS_KEEP_HISTORY) {
            return getDataProjectPath() + getDataFullFailedFolder();
        } else {
            return getDataFullFailedFolder();
        }
    }

    public static String getFailedScreenShotFullPathWithTestId(String testId) {//
        if (!SystemProperties.REPORTING_AND_SCREENSHOTS_KEEP_HISTORY) {
            return getDataProjectPath() + getDataFullFailedFolderWithTestId(testId);
        } else {
            return getDataFullFailedFolderWithTestId(testId);
        }
    }

    public static String getPassedScreenShotFullPath() {//
        if (!SystemProperties.REPORTING_AND_SCREENSHOTS_KEEP_HISTORY) {
            return getDataProjectPath() + getDataFullPassedFolder();
        } else {
            return getDataFullPassedFolder();
        }
    }

    public static String getPassedScreenShotFullPathWithTestId(String testId) {//
        if (!SystemProperties.REPORTING_AND_SCREENSHOTS_KEEP_HISTORY) {
            return getDataProjectPath() + getDataFullPassedFolderWithTestId(testId);
        } else {
            return getDataFullPassedFolderWithTestId(testId);
        }
    }

    public static String getReportFolderFullPath() {
        // TO BE IMPLEMENTED
        if (!SystemProperties.REPORTING_AND_SCREENSHOTS_KEEP_HISTORY) {
            return getDataProjectPath() + getBaseFolderPath();
        } else {
            return getBaseFolderPath();
        }
    }

    public static String getScreenShotRelativePath(String screenshotPath) {//
        if (!SystemProperties.REPORTING_AND_SCREENSHOTS_KEEP_HISTORY) {
            screenshotPath = screenshotPath.replace(getDataProjectPath() + ExtentManager.DEFAULT_WINDOWS_REPORT_PARTIAL_PATH, WINDOWS_RELATIVE_PATH_HTML_TEMPLATE_DEFAULT);
        } else {
            screenshotPath = screenshotPath.replace(getBaseFolderPath(), WINDOWS_RELATIVE_PATH_HTML_TEMPLATE_CUSTOM);
        }

        return screenshotPath;
    }

    public static String getSkippedScreenShotFullPath() {
        if (!SystemProperties.REPORTING_AND_SCREENSHOTS_KEEP_HISTORY) {
            return getDataProjectPath() + getDataFullSkippedFolder();
        } else {
            return getDataFullFailedFolder();
        }
    }

    public static String getSkippedScreenShotFullPathWithTestId(String testId) {
        if (!SystemProperties.REPORTING_AND_SCREENSHOTS_KEEP_HISTORY) {
            return getDataProjectPath() + getDataFullSkippedFolderWithTestId(testId);
        } else {
            return getDataFullFailedFolderWithTestId(testId);
        }
    }

    // Takes a screenshot of the driver and saves it extent report's location
    public static String takeScreenshot(String path, WebDriver webDriver) {
        String screenshotTaken = null;
        try {
            File scrFile = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(scrFile, new File(path));
            screenshotTaken = path;
        } catch (RuntimeException | IOException e) {
            Reporting.logReporter(Status.DEBUG, "Could not add the screenshot to the log.");
            Logging.logReporter(Status.DEBUG, "Exception Caught : " + e);
        }
        return screenshotTaken;
    }
    
    
    
    public static String takeScreenshotFull(String path, WebDriver webDriver) {
    	
    	int min=1;
    	
    	int range=65556;
    	
    	  int rand = (int)(Math.random() * range) + min;
    	
    	 Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1000)).takeScreenshot(WebDriverSession.getWebDriverSession());
         try {
        	 
         File file = new File(path);
             
             // true if the directory was created, false otherwise
             if(!file.exists())
             {
             if (file.mkdirs()) {
                 System.out.println("Directory is created!");
             } else {
                 System.out.println("Failed to create directory!");
             }
             
             }
        	  
        	  
         	ImageIO.write(screenshot.getImage(), "PNG", new File(path));
 		} catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}
         
    	
    	
    	
    	
    	
    	
    	
    	
    	
      
        return path;
    }

}