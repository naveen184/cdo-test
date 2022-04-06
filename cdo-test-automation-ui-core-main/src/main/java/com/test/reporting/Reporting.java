package com.test.reporting;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.testng.Reporter;


import com.aventstack.extentreports.MediaEntityModelProvider;
import com.aventstack.extentreports.Status;
import com.epam.reportportal.message.ReportPortalMessage;

import org.apache.commons.logging.Log;
import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

import com.test.logging.Logging;
import com.test.utils.SystemProperties;

public class Reporting extends TestListener {

    private static final Logger LOGGER = Logger.getLogger("log");
	  private static final org.apache.logging.log4j.Logger LOGGER1 =
			   LogManager.getLogger(Reporting.class); 
	private static final String VALIDATION_MESSAGE = "Validation";
    private static final String PRINT_MESSAGE = "-";

    //private static final String EXTENTS_ERROR_PRINT_MESSAGE = "Error To Log Message - "
    private static final String EXTENT_REPORTS_STATUS_TEMPLATE_END = "]].";

    private static final String STATUS_DEBUG = "debug";
    private static final String STATUS_ERROR = "error";
    private static final String STATUS_FAIL = "fail";
    private static final String STATUS_FATAL = "fatal";
    private static final String STATUS_INFO = "info";
    private static final String STATUS_PASS = "pass";
    private static final String STATUS_SKIP = "skip";
    private static final String STATUS_WARNING = "warning";
    //private static final String FREEMARKER_STATUS= System.setProperty("org.freemarker.loggerLibrary", "none")
    private static HashMap<Long, ArrayList<String>> mapMissingMessages = new HashMap<>();
    //protected static String groupTag="";
    //private static StringBuilder groupMessage;
    protected static ThreadLocal<String> groupTag = new ThreadLocal<String>();
    private static ThreadLocal<String> groupMessage = new ThreadLocal<String>();    
    //private static Status groupStatus = Status.PASS;
    private static ThreadLocal<Status> groupStatus = new ThreadLocal<Status>();
    
    /**
     * OBJECTIVE: Add Message to the map, to be use later to print in the report (BEFORE) CURRENT STATUS: WORKS FINE IN SINGLE THREAT BUT CAUSE FAILURE IN MULTITHREAT
     */
    static void addMessagesToMap(Status status, String message) {
        ArrayList<String> newArr = new ArrayList<>();
        ArrayList<String> existingArr = new ArrayList<>();
        message = status.toString() + EXTENT_REPORTS_STATUS_TEMPLATE_END + message;

        if (mapMissingMessages.size() == 0) { // Create New
            newArr.add(message);
            mapMissingMessages.put(Thread.currentThread().getId(), newArr);
        } else {// Update an existing one
            existingArr.addAll(mapMissingMessages.get(Thread.currentThread().getId()));
            existingArr.add(message);
            mapMissingMessages.replace(Thread.currentThread().getId(), existingArr);
        }
    }

    /**
     * OBJECTIVE: Remove the current Threat Id from the map that help us to create sessions
     */
    public static void clearMapSessionForCurrentThreat() {
        mapMissingMessages.remove(Thread.currentThread().getId());
    }

    /**
     * OBJECTIVE: Get all current sessions
     */
    public static HashMap<Long, ArrayList<String>> getMapSessionsAvailable() {
        return mapMissingMessages;
    }

    private static boolean getSkipDebugFlag() {
        String skipDebugLogs = SystemProperties.EXTENT_REPORT_SKIP_DEBUG_LOGS;

        if (skipDebugLogs == null || skipDebugLogs.isEmpty()) {
            return true;
        } else if ("true".equalsIgnoreCase(skipDebugLogs)) {
            return true;
        } else if ("false".equalsIgnoreCase(skipDebugLogs)) {
            return false;
        }

        // Any other value.. will skype by default.
        return true;
    }

    /**
     * Main reporter method, driven by the system.properties reporting variables booleans: reporting.extent=${reporting.extent} reporting.testng=${reporting.testng}
     * reporting.log4j=${reporting.log4j}
     *
     * @param status:
     *            PASS,FAIL,ERROR,FATAL,WARNING,INFO
     * @param message:
     *            Message to display in the report
     */
    public static void logReporter(Status status, String message) {

    
 
    
        if (SystemProperties.TESTNG_REPORT) {
            reporterTestNG(status, message);
        }
        if (SystemProperties.LOG4J_REPORT) {
            reporterLog4J(status, message);
        }else {
        	reporterLog4_2J(status, message);
        }
        if (SystemProperties.EXTENT_REPORT) {
            reporterExtent(status, message, null);
            
         }
    }
    /**
     * Overloaded reporter method, driven by the system.properties reporting variables booleans: reporting.extent=${reporting.extent} reporting.testng=${reporting.testng}
     * reporting.log4j=${reporting.log4j}
     *
     * @param status:
     *            PASS,FAIL,ERROR,FATAL,WARNING,INFO
     * @param message:
     *            Message to display in the report
     *            
     *  @param model:        
     *            Screenshot for the log      
     *  @param session: 
     *  Boolean true/False for suppression of Session ID in message       
     */
    
    public static void logReporter(Status status, String message,MediaEntityModelProvider model,boolean session) {
    	Logging.logReporter(Status.DEBUG, "logReporter MediaEntityModelProvider is :  " +model);
       if(session)
    	   {
    	   message = "SessionId" + Thread.currentThread().getId() + " - " + message;
    	   }
     
   		if (SystemProperties.TESTNG_REPORT) {
              reporterTestNG(status, message);
          }
          if (SystemProperties.LOG4J_REPORT) {
              reporterLog4J(status, message);
          }else {
          	reporterLog4_2J(status, message);
          }
          if (SystemProperties.EXTENT_REPORT) {
              reporterExtent(status, message, null);
          }
      }
   
    
    
   
    /**
     * Main reporter method, driven by the system.properties reporting variables booleans: reporting.extent=${reporting.extent} reporting.testng=${reporting.testng}
     * reporting.log4j=${reporting.log4j}
     *
     * @param status:
     *            PASS,FAIL,ERROR,FATAL,WARNING,INFO
     * @param message:
     *            Message to display in the report
     * @param model:
     *            Screenshot for the log
     */
    public static void logReporter(Status status, String message, MediaEntityModelProvider model) {
    	 if (SystemProperties.TESTNG_REPORT) {
             reporterTestNG(status, message);
         }
         if (SystemProperties.LOG4J_REPORT) {
             reporterLog4J(status, message);
         }else {
         	reporterLog4_2J(status, message);
         }
         if (SystemProperties.EXTENT_REPORT) {
             reporterExtent(status, message, model);
         }
    }
    
   
    /**
     * OBJECTIVE: Print the available sessions for debug
     */
    public static void printMapSessionsAvailable() {

        Reporting.logReporter(Status.DEBUG, "Start - printMapSessionsAvailable");

        if (mapMissingMessages.size() == 0) {

            Reporting.logReporter(Status.DEBUG, "Map is empty");
        } else {
            Set<Entry<Long, ArrayList<String>>> entries = mapMissingMessages.entrySet();
            int iteration = 0;

            for (Entry<Long, ArrayList<String>> entry : entries) {
                iteration = iteration + 1;

                Long keyValue = entry.getKey();
                ArrayList<String> arrayValue = entry.getValue();
                
                Reporting.logReporter(Status.DEBUG, "Begin iteration = " + iteration + "******************************");



                
                Reporting.logReporter(Status.DEBUG, keyValue + " => ");
                

                
                for (String e : arrayValue) {
                	Reporting.logReporter(Status.DEBUG, e);

                }

                
                Reporting.logReporter(Status.DEBUG, "End iteration =   " + iteration + "******************************");
                
            }
        }
        
        Reporting.logReporter(Status.DEBUG, "End - printMapSessionsAvailable");
        

    }

    /**
     * Print to TestNG Report following the indicated formatting
     *
     * @param bgColor,
     *            textColor, message, messageType
     */
    private static void printOnTestNGReport(String bgColor, String textColor, String message, String messageType) {
        System.setProperty("org.uncommons.reportng.escape-output", "false");
        Reporter.log("<div id='message" + messageType + "' style='background-color: " + bgColor + "; color: " + textColor + "; font-size: xx-small; border: " + ColorHex.SILVER
                + " 2px solid; padding: 5px;'>" + "<table><tr><td style='width:85px;'>" + messageType + "</td><td>:</td><td id='message'>" + message + "</td></tr></table></div><br/><br/>");
   
     }

    private static void printStatusAndMessage(String originalMessage) {
        int index = originalMessage.indexOf(EXTENT_REPORTS_STATUS_TEMPLATE_END);
        String realMsg = originalMessage.substring(index + 3);

        Reporting.logReporter(Status.DEBUG, "realMsg = " + realMsg);

        String status = originalMessage.replace(realMsg, "");
        status = status.replace(EXTENT_REPORTS_STATUS_TEMPLATE_END, "");

        Reporting.logReporter(Status.DEBUG, "status = " + status);
        switch (status) {
            case STATUS_DEBUG:
                Reporting.logReporter(Status.DEBUG, realMsg);
                break;
            case STATUS_ERROR:
                Reporting.logReporter(Status.ERROR, realMsg);
                break;
            case STATUS_FAIL:
                Reporting.logReporter(Status.FAIL, realMsg);
                break;
            case STATUS_FATAL:
                Reporting.logReporter(Status.FATAL, realMsg);
                break;
            case STATUS_INFO:
                Reporting.logReporter(Status.INFO, realMsg);
                break;
            case STATUS_PASS:
                Reporting.logReporter(Status.PASS, realMsg);
                break;
            case STATUS_SKIP:
                Reporting.logReporter(Status.SKIP, realMsg);
                break;
            case STATUS_WARNING:
                Reporting.logReporter(Status.WARNING, realMsg);
                break;
            default:
                Reporting.logReporter(Status.DEBUG, realMsg);
        }

    }

    /**
     * Method to print the BEFORE METHOD collected steps in the extent report
     */
    public static void printToReportBeforeStepsLog() {


        HashMap<Long, ArrayList<String>> mapMissingMessages = Reporting.getMapSessionsAvailable();

        if (mapMissingMessages.size() == 0) {
        	Logging.logReporter(Status.DEBUG, "Steps Map is empty");
        } else {


            Set<Entry<Long, ArrayList<String>>> entries = mapMissingMessages.entrySet();
            int iteration = 0;

            for (Entry<Long, ArrayList<String>> entry : entries) {
                iteration = iteration + 1;
                //Long keyValue = entry.getKey()
                ArrayList<String> arrayValue = entry.getValue();

                for (String e : arrayValue) {
                    printStatusAndMessage(e);
                }
            }
        }

        clearMapSessionForCurrentThreat();// This cleans the data for the next test using the same ID
    }

    /**
     * Extent reporter method
     *
     * @param status:
     *            PASS,FAIL,ERROR,FATAL,WARNING,INFO
     * @param message:
     *            Message to display in the report.
     */
    public static void reporterExtent(Status status, String message, MediaEntityModelProvider model) {
        boolean skipDebugLogs = getSkipDebugFlag();

        if (skipDebugLogs && Status.DEBUG == status) { // Skip the log as the client does not want to see the DEBUG Level in the report
            return;
        }
        // WORK-AROUND FOR BEFORE DATA LOGGING IN EXTENT REPORT - PART1
        // This part fixes the BEFORE data Logging in the extend report. (The issue is that in more than one test execution, it logs the data at the end of the report 1srt report
        // this due that the logic trust in the THREAT ID, and as is exactly the same causes this..
        String x = TestMethodListener.mapStatusForLog.get(Thread.currentThread().getId());

        boolean isBeforeAnnotation = new Boolean(x);
        // ------------------------------------------------------------------------------------------->


        if (!isBeforeAnnotation) {

            try {

            	if(SystemProperties.GROUP_REPORT)
                {
                	if("".equals(groupTag.get())||(groupTag.get()==null))
                	{	//System.out.println("GROUPSTATUSENTERED:1 "+groupStatus.get());
                		if (model != null) {
                			//System.out.println("GROUPSTATUSENTERED:2 "+groupStatus.get());
    	                    ExtentTestManager.getTest().log(status, message, model);
    	                } else {
    	                	//System.out.println("GROUPSTATUSENTERED:3 "+groupStatus.get());
    	                    ExtentTestManager.getTest().log(status, message);
    	                }
                	}
                	else
                	{	//System.out.println("GROUPSTATUSENTERED:4 "+groupStatus.get());
                		if(status.toString().equals(Status.FAIL.toString()))
                		{	
                			groupStatus.set(Status.FAIL);
                		}
                		//System.out.println("GROUPSTATUSENTERED:5 "+groupStatus.get());
                		setLogGroupStatements(groupTag.get().toString(), message );
                	}	
                }
            	else 
            	{	//System.out.println("GROUPSTATUSENTERED:6 "+groupStatus.get());
            		if (model != null) {
            			//System.out.println("GROUPSTATUSENTERED:7 "+groupStatus.get());
	                    ExtentTestManager.getTest().log(status, message, model);
	                } else {
	                	//System.out.println("GROUPSTATUSENTERED:8 "+groupStatus.get());
	                    ExtentTestManager.getTest().log(status, message);
	                }
                
            	}

            } catch (NullPointerException e) {
            	Logging.logReporter(Status.DEBUG, "Exception Caught : " + e);
            }
        }

        else {
            // WORK-AROUND FOR BEFORE DATA LOGGING IN EXTENT REPORT - PART2
            // TO BE IMPLEMENTED This approach works fine in single threat but fails in Multi need review and time --->


        }

    }

    /**
     * Log4J reporter method, this method used to sent customized messages to the Log4J logs. Utilized in the main "reporter" method.
     *
     * @param status:
     *            One of the available status enum: PASS,FAIL,ERROR,FATAL,WARNING,INFO
     * @param message:
     *            Message to display in the report.
     */
    public static void reporterLog4J(Status status, String message) {
        switch (status) {
            case PASS:
                LOGGER.info(PRINT_MESSAGE + message);
                break;
            case FAIL:
                LOGGER.error(PRINT_MESSAGE + message);
                break;
            case ERROR:
                LOGGER.error(PRINT_MESSAGE + message);
                break;
            case WARNING:
                LOGGER.warn(PRINT_MESSAGE + message);
                break;
            case INFO:
                LOGGER.info(PRINT_MESSAGE + message);
                break;
            case FATAL:
                LOGGER.fatal(PRINT_MESSAGE + message);
                break;
            case DEBUG:
                LOGGER.debug(PRINT_MESSAGE + message);
                break;
            default:
                LOGGER.info(PRINT_MESSAGE + message);
                break;
        }
    }

    /**
     * Log4_2J reporter method, this method used to sent customized messages to the Log4J logs. Utilized in the main "reporter" method.
     *
     * @param status:
     *            One of the available status enum: PASS,FAIL,ERROR,FATAL,WARNING,INFO
     * @param message:
     *            Message to display in the report.
     */
    public static void reporterLog4_2J(Status status, String message) {
        switch (status) {
            case PASS:
                LOGGER1.info(PRINT_MESSAGE + message);
                break;
            case FAIL:
                LOGGER1.error(PRINT_MESSAGE + message);
                break;
            case ERROR:
                LOGGER1.error(PRINT_MESSAGE + message);
                break;
            case WARNING:
                LOGGER1.warn(PRINT_MESSAGE + message);
                break;
            case INFO:
                LOGGER1.info(PRINT_MESSAGE + message);
                break;
            case FATAL:
                LOGGER1.fatal(PRINT_MESSAGE + message);
                break;
            case DEBUG:
                LOGGER1.debug(PRINT_MESSAGE + message);
                break;
            default:
                LOGGER1.info(PRINT_MESSAGE + message);
                break;
        }
    }

    
    
    /**
     * TestNG reporter method, this method used to sent customized messages to the TestNG report. Utilized in the main "reporter" method.
     *
     * @param status:
     *            One of the available status enum: PASS,FAIL,ERROR,FATAL,WARNING,INFO
     * @param message:
     *            Message to display in the report.
     */
    public static void reporterTestNG(Status status, String message) {
        switch (status) {
            case PASS:
                printOnTestNGReport(ColorHex.GREEN, ColorHex.WHITE, message, VALIDATION_MESSAGE);
                break;
            case FAIL:
                printOnTestNGReport(ColorHex.RED, ColorHex.WHITE, message, VALIDATION_MESSAGE);
                break;
            case ERROR:
                printOnTestNGReport(ColorHex.RED, ColorHex.WHITE, message, VALIDATION_MESSAGE);
                break;
            case WARNING:
                printOnTestNGReport(ColorHex.YELLOW, ColorHex.BLACK, message, VALIDATION_MESSAGE);
                break;
            case INFO:
                printOnTestNGReport(ColorHex.SILVER, ColorHex.BLACK, message, VALIDATION_MESSAGE);
                break;
            case FATAL:
                printOnTestNGReport(ColorHex.BLACK, ColorHex.RED, message, VALIDATION_MESSAGE);
                break;
            default:
                printOnTestNGReport(ColorHex.SILVER, ColorHex.BLACK, message, VALIDATION_MESSAGE);
                break;
        }
    }
    
    
    
    
	/***
	 * Description - This method is used to set group logs for reporting based on
	 * parameter
	 * 
	 * @param tag
	 * @param message
	 */
	private static void setLogGroupStatements(String tag, String message) {

		if (groupTag.get() == null) {
			groupTag.set("");
		}

		if ("".equals(groupTag.get()) || (!(groupTag.get().equalsIgnoreCase(tag)))) {
			printAndClearLogGroupStatements();
			groupTag.set(tag);
			groupMessage.set("");
		}

		if ("".equals(message)) {
			return;
		}
		if (message.contains(".png")) {
			String messageLog = groupMessage.get();
			groupMessage.set(messageLog+"<img data-featherlight= \"" + message + "\" width=\"10%\" src=\"" + message
					+ "\" data-src=\"" + message + "\"><br><br>");
		} else {
			String messageLog = groupMessage.get();
			groupMessage.set(messageLog+message + "<br><br>");
		}
		////System.out.println("GROUPSTATUSENTERED: "+groupStatus.get());
	}
	
	/***
	 * Description - This method is used to get and clear all the log group
	 * statements set in setLogGroupStatements method
	 */

	
	public static void printAndClearLogGroupStatements() {
		//System.out.println("GROUPSTATUSENTERED: "+groupStatus.get());
		if (groupTag.get() == null || "".equals(groupTag.get())) {
			return;
		} else if (groupMessage.get() == null || "".equals(groupMessage.get())) {
			return;
		}else if (groupStatus.get() == null || "".equals(groupStatus.get())) {
			groupStatus.set(Status.PASS);
		}
		
		//System.out.println("GROUPSTATUS : "+groupStatus.get());
		ExtentTestManager.getTest().createNode(groupTag.get().toString()).log(groupStatus.get(), groupMessage.get().toString());
		
		groupTag.remove();
		groupStatus.remove();
		groupStatus.set(Status.PASS);

	}

	/***
	 * Description - This method is used to set group names for reporting based on
	 * parameter
	 * 
	 * @param tag
	 */
	public static void setNewGroupName(String tag) {

		if (groupTag.get() == null) {
			groupTag.set("");
		}

		if ("".equals(groupTag.get()) || (!(groupTag.get().equalsIgnoreCase(tag)))) {
			printAndClearLogGroupStatements();
			groupTag.set(tag);
			groupMessage.set("");
		}
	}
    
    /**
	 * Description - This method is used to attach screenshot in report portal.
	 * 
	 * @param status
	 * @param filepath
	 * @param message
	 * @throws IOException
	 */
	public static void attachScreenshotInReportPortal(Status status, String filepath, String message)
			throws IOException {
		boolean flagRP;
		
		if(SystemProperties.isValueSet("rp.enable")) {
			flagRP = SystemProperties.getBooleanValue("rp.enable");	
		}else {
			flagRP = SystemProperties.REPORTPORTAL_REPORT;
		}

		if (flagRP) {
			ReportPortalMessage rpmessage = new ReportPortalMessage(new File(filepath), message);
			switch (status) {
			case PASS:
				LOGGER.info(rpmessage);
				break;
			case FAIL:
				LOGGER.error(rpmessage);
				break;
			case ERROR:
				LOGGER.error(rpmessage);
				break;
			case WARNING:
				LOGGER.warn(rpmessage);
				break;
			case INFO:
				LOGGER.info(rpmessage);
				break;
			case FATAL:
				LOGGER.fatal(rpmessage);
				break;
			case DEBUG:
				LOGGER.debug(rpmessage);
				break;
			default:
				LOGGER.info(rpmessage);
				break;
			}
		}
	}
    
    
    
    
    
}
