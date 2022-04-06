package com.test.ui.actions;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.aventstack.extentreports.Status;
import com.test.logging.Logging;
import com.test.reporting.Reporting;
import com.test.utils.SystemProperties;
/**
 ****************************************************************************
 * HIGHLIGHTS: > Base methods to be used in the STEPS pages/implementations.
 ****************************************************************************
 */

public class BaseSteps {
	
	private static final String UTILITY_CLASS = "Utility class";
	private static final String EXCEPTION_MSG = "Exception Caught : ";
	private static final String CALLING_MSG = "-Calling_From-";
	private static final String CLEAR_SEND_KEYS_MSG = "clearFieldAndSendKeys done";
	
	/**
     * Utilities to handle JavaScript Alerts
     */
	
	 public static final  String NAME_OF_CURR_CLASS1= Thread.currentThread().getStackTrace()[1].getClassName();
	 public static final  String NAME_OF_CURR_CLASS= Thread.currentThread().getStackTrace()[1].getClassName().substring(NAME_OF_CURR_CLASS1.lastIndexOf('.')+1);
	 public static enum Actionenum{ SEND, SENDJS, CLEARSEND, SCANSEND, CLEARSENDJS, WAITTEXT, CLICK,CLICKJS,WAITVISIBLE,WAITCLICKABLE,VISIBLE,SCROLL,SCROLLACTION,GETTEXTJS,BLANK}    
	   
    public static class Alerts {
    	
    	private Alerts() {
 		    throw new IllegalStateException(UTILITY_CLASS);
 		  }

        private static final String MESSAGE_P1 = "Alert with text [";
    

        /**
         * OBJECTIVE: Accept the alert.
         */
        public static void confirmationAlertAccept() {
            Alert simpleAlert = WebDriverSession.getWebDriverSession().switchTo().alert();
            String alertText = simpleAlert.getText();
            simpleAlert.accept();
            String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            
            Reporting.logReporter(Status.DEBUG, MESSAGE_P1 + alertText + "] Accept - Done"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
        }

        /**
         * OBJECTIVE: Dismiss the alert.
         */
        public static void confirmationAlertDismiss() {
            Alert confirmationAlert = WebDriverSession.getWebDriverSession().switchTo().alert();
            String alertText = confirmationAlert.getText();
            confirmationAlert.dismiss();
             String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
             Reporting.logReporter(Status.DEBUG, MESSAGE_P1 + alertText + "] Dismiss - Done"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
        }

/**
		 * OBJECTIVE: Check if alert is displayed
		 */
		public static boolean isAlertDisplayed() {
			
			try {
				WebDriverSession.getWebDriverSession().switchTo().alert();
			} catch (NoAlertPresentException e) {
				Logging.logReporter(Status.DEBUG, EXCEPTION_MSG + e);
				return false;
			}

			return true;
		}
        /**
         * OBJECTIVE: SendKeys to the alert.
         */
        public static void promptAlertSendKeys(String textToSend) {
            Alert promptAlert = WebDriverSession.getWebDriverSession().switchTo().alert();
            String alertText = promptAlert.getText();
            promptAlert.sendKeys(textToSend);
            promptAlert.accept();
            String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
           
            Reporting.logReporter(Status.DEBUG, MESSAGE_P1 + alertText + "] SendKeys with text " + textToSend + " - Done"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
        }

    }

    /**
     * Utilities to handle Checkboxes; Depending on how the page was made, the regular implementation might not work in that case use the ones that check Class properties
     */
    public static class CheckBoxes {
    	
    	private CheckBoxes() {
 		    throw new IllegalStateException(UTILITY_CLASS);
 		  }

        private static final String LOGGER_MESSAGE = "Validating isCheckboxActive: ";

        /**
         * OBJECTIVE: Return if the checkbox is active(selected)
         */
        public static boolean isCheckboxActive(WebElement aCheckbox) {
            boolean value = aCheckbox.isSelected();
            
            
            String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
                   
            Reporting.logReporter(Status.DEBUG, LOGGER_MESSAGE + value+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
            return value;
        }

        /**
         * OBJECTIVE: Return if the checkbox has a class property as active
         */
        public static boolean isCheckboxActiveByClassProperty(WebElement aCheckbox) {
        	
        	 String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
             
            
            if (WebElementUtils.elementHasClass(aCheckbox, "active")) {
            	 
                Reporting.logReporter(Status.DEBUG, LOGGER_MESSAGE + "True"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
                return true;
            }
            
            
            Reporting.logReporter(Status.DEBUG, LOGGER_MESSAGE + "false"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
            return false;
        }

        /**
         * OBJECTIVE: Remove the Check if a Checkbox is active
         */
        public static void uncheckCheckbox(WebElement aCheckbox) {
        	 String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
             
            if (aCheckbox.isSelected()) {
                aCheckbox.click();
               
                Reporting.logReporter(Status.DEBUG, "Checkbox uncheck done"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
                return;
            }
            Reporting.logReporter(Status.DEBUG, "Checkbox uncheck NOT done - Element is not active(Selected)"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
        }

        /**
         * OBJECTIVE: Remove the Check if a Checkbox is active by looking a class property as active
         */
        public static void unCheckCheckboxByClassProperty(WebElement aCheckbox) {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            
            if (isCheckboxActiveByClassProperty(aCheckbox)) {
                aCheckbox.click();
                
                Reporting.logReporter(Status.DEBUG, "Checkbox uncheck done"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
                return;
            }
            Reporting.logReporter(Status.DEBUG, "Checkbox uncheck NOT done - Element is not active(Selected)"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
        }

    }

    /**
     * Utilities to click Elements
     */
    public static class Clicks {
    	
    	private Clicks() {
 		    throw new IllegalStateException(UTILITY_CLASS);
 		  }

        /**
         * OBJECTIVE: Short Wait for an element to be Clickable to do the click.
         */
        public static void clickElement(WebElement element) {
            clickElement(element, GENERIC_SHORT_TIME_OUT_SECONDS);
        }

        /**
         * OBJECTIVE: Custom Wait for an element to be Clickable to do the click.
         */
        private static void clickElement(WebElement element, int waitInSeconds) {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            
            Waits.waitForElementToBeClickable(element, waitInSeconds);
            

            element.click();
            
            Reporting.logReporter(Status.DEBUG, "click Done"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
        }

        /**
         * OBJECTIVE: Short Wait for an element to be Clickable to do the click, Try the regular method before this one
         */
        public static void clickElementCoordinates(WebElement element) {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            
            Waits.waitForElementVisibility(element);
            SeleniumActions.getActions().click(element).perform();
            Reporting.logReporter(Status.DEBUG, "click ElementCoordinates Done"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
           
        }

        /**
         * OBJECTIVE: Wait for an element to be Clickable to do the click, Try the regular method before this one
         */
        public static void clickElementCoordinatesLongWait(WebElement element) {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            
            Waits.waitForElementVisibilityLongWait(element);
            SeleniumActions.getActions().click(element).perform();
            
            Reporting.logReporter(Status.DEBUG, "click ElementCoordinatesLongWait Done"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
        }

        /**
         * OBJECTIVE: Wait for an element to be Clickable to do the click.
         */
        public static void clickElementLongWait(WebElement element) {
            clickElement(element, GENERIC_LONG_TIME_OUT_SECONDS);
        }

        /**
         * OBJECTIVE: From the given list - Wait for an element to be Clickable to do the click
         */
        public static void clickElementsCoordinatesInList(List<WebElement> list) {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            
            for (WebElement e : list) {
                clickElementCoordinates(e);
                
                Reporting.logReporter(Status.DEBUG, "click ElementsCoordinatesInList - Done"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
            }
        }

        /**
         * OBJECTIVE: From the given list - Wait for an element to be Clickable to do the click
         */
        public static void clickElementsCoordinatesInListLongWait(List<WebElement> list) {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            
            for (WebElement e : list) {
                clickElementCoordinatesLongWait(e);
                
                Reporting.logReporter(Status.DEBUG, "click clickElementsCoordinatesInListLongWait - Done"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
            }
        }

        /**
         * OBJECTIVE: From the given list - Wait for an element to be Clickable to do the click
         */
        public static void clickElementsInList(List<WebElement> list) {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            
            for (WebElement e : list) {
                clickElement(e);
                Reporting.logReporter(Status.DEBUG, "click clickElementsCoordinatesInListLongWait - Done"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
                
                Reporting.logReporter(Status.DEBUG, "click ElementsInList - Done"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
            }
        }

        /**
         * OBJECTIVE: From the given list - Wait for an element to be Clickable to do the click
         */
        public static void clickElementsInListLongWait(List<WebElement> list) {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            
            for (WebElement e : list) {
                clickElementLongWait(e);
                Reporting.logReporter(Status.DEBUG, "click ElementsInListLongWait - Done"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
            }
        }

        /**
         * OBJECTIVE: From the given list - Wait for an element to be Clickable to do the click
         */
        public static void clickElementsUsingJSInList(List<WebElement> list) {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            
            for (WebElement e : list) {
                clickElementUsingJS(e);
                Reporting.logReporter(Status.DEBUG, "click clickElementsUsingJSInList - Done"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
            }
        }

        /**
         * OBJECTIVE: From the given list - Wait for an element to be Clickable to do the click
         */
        public static void clickElementsUsingJSInListLongWait(List<WebElement> list) {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            
            for (WebElement e : list) {
                clickElementUsingJSLongWait(e);
                Reporting.logReporter(Status.DEBUG, "click clickElementsUsingJSInListLongWait - Done"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
            }
        }

        /**
         * OBJECTIVE: Short Wait for an element to be Clickable to do the click (With JavaScript).
         */
        public static void clickElementUsingJS(WebElement element) {
            clickElementUsingJS(element, GENERIC_SHORT_TIME_OUT_SECONDS);
        }

        /**
         * OBJECTIVE: Wait for an element to be Clickable to do the click(With JavaScript), Try the regular method before this one, this should be a last option as it is not
         * interacting with the actual page.
         */
        private static void clickElementUsingJS(WebElement element, int waitInSeconds) {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            Waits.waitForElementVisibility(element, waitInSeconds);
            JavaScripts.executeJavaScript("arguments[0].click()", element);
            Reporting.logReporter(Status.DEBUG, "click clickElementUsingJavaScript Done"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
        }

        /**
         * OBJECTIVE: Wait for an element to be Clickable to do the click(With JavaScript).
         */
        public static void clickElementUsingJSLongWait(WebElement element) {
            clickElementUsingJS(element, GENERIC_LONG_TIME_OUT_SECONDS);
        }

    }

    


public static class Exceptions {
	
	private Exceptions() {
		    throw new IllegalStateException(UTILITY_CLASS);
		  }

	 /**
     * Exception Handling Method, 
     * Handles Stale Element Exception
     *
     * @param element:
     *            WebElement Object
     * @param action:
     *            action to perform
     * @param text:         
     *        text to send via sendkeys    
     * @param retry:         
     *    No of iterations of the loop      
     */

	public static void staleElementAction(WebElement element, Object act, String text,int retry)
	{
		
		String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
	Reporting.logReporter(Status.DEBUG, "Trying to Again find same element using xpath which is currently Stale "+retry+"-Calling_From-"+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
			
	int count = 0;
	while (count < retry)
	{

		
		try {



	if (element.isEnabled() && element.isDisplayed() ) {
	  if (act.equals(Actionenum.SEND)) {
	BaseSteps.SendKeys.sendKey(element, text);
	} else if (act.equals(Actionenum.SENDJS)) {
	BaseSteps.SendKeys.sendKeyUsingJS(element, text);
	} else if (act.equals(Actionenum.CLEARSEND)) {
	BaseSteps.SendKeys.clearFieldAndSendKeys(element, text);
	} 
	else if (act.equals(Actionenum.SCANSEND))
	{
	BaseSteps.SeleniumActions.scanAndSendKeys(element,text) ;
	}
	else if (act.equals(Actionenum.CLEARSENDJS))
	{
	BaseSteps.SendKeys.clearFieldAndSendKeysUsingJS(element,text) ;
	}
	else if (act.equals(Actionenum.WAITTEXT))
	{
	BaseSteps.Waits.waitForElementText(element,text) ;
	}

	else if (act.equals(Actionenum.BLANK))
	{
	Reporting.logReporter(Status.DEBUG, "No action is Done while handling exception,Use action as per need in Script"+"-Calling_From-"+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
	}

	  
	Reporting.logReporter(Status.DEBUG, "Element is now  Enabled and clickable and  Stale element Exeception did not come,clearing text"+"-Calling_From-"+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);


	break;
	}
	} catch (StaleElementReferenceException e){
	

	Reporting.logReporter(Status.DEBUG, "Trying to recover from a stale element Exception"+ e+"-Calling_From-"+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);


	count = count + 1;

	BaseSteps.Waits.waitGeneric(1000);
	Reporting.logReporter(Status.DEBUG, "Increase in count value due to stale element Exception retry count"+ count+"-Calling_From-"+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);


	}

	}
	}



	/**
	 * Exception Handling Method (Overloaded function), 
	 * Handles Stale Element Exception
	 *
	 * @param element:
	 *            WebElement Object
	 * @param action:
	 *            action to perform
	 *               
	 * @param retry:         
	 *    No of iterations of the loop      
	 */


	public static void staleElementAction(WebElement element, Object act,int retry)
	{
		String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
		Reporting.logReporter(Status.DEBUG, "Trying to Again find same element using xpath which is currently Stale "+retry+"-Calling_From-"+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);

	int count = 0;
	while (count < retry)
	{
	try {

	if (element.isEnabled() && element.isDisplayed()) {

	if (act.equals(Actionenum.CLICK)) {
	BaseSteps.Clicks.clickElement(element);
	} else if (act.equals(Actionenum.CLICKJS)) {
	BaseSteps.Clicks.clickElementUsingJS(element);
	} else if (act.equals(Actionenum.WAITVISIBLE)) {
	BaseSteps.Waits.waitForElementVisibility(element);
	} else if (act.equals(Actionenum.WAITCLICKABLE)) {
	BaseSteps.Waits.waitForElementToBeClickable(element);
	} else if (act.equals(Actionenum.VISIBLE)) {
	Validate.isElementPresent(element);
	}else if (act.equals(Actionenum.SCROLL))
	{
	BaseSteps.Debugs.scrollToElement(element);
	}
	else if (act.equals(Actionenum.SCROLLACTION))
	{
	BaseSteps.Debugs.scrollToElementWitActions(element) ;
	}
	else if (act.equals(Actionenum.GETTEXTJS))
	{
	BaseSteps.Getters.getElementTextUsingJS(element) ;
	}

	else if (act.equals(Actionenum.BLANK))
	{
	Reporting.logReporter(Status.DEBUG, "No action is Done while handling exception,Use action as per need in Script"+"-Calling_From-"+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
	}
	Reporting.logReporter(Status.DEBUG, "Element is now  Enabled and clickable and  Stale element Exeception did not come,clearing text"+"-Calling_From-"+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
	break;
	}
	} catch (StaleElementReferenceException e){
	
	Reporting.logReporter(Status.DEBUG, "Trying to recover from a stale element Exception"+ e+"-Calling_From-"+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
	count = count + 1;
	BaseSteps.Waits.waitGeneric(1000);
	Reporting.logReporter(Status.DEBUG, "Increase in count value due to stale element Exception retry count"+ count+"-Calling_From-"+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);

	}

	}
	}


/**
 * Exception Handling Method , 
 * Handles No such Element Exception
 *
 * @param element:
 *            WebElement Object
 *               
 * @param retry:         
 *    No of iterations of the loop      
 */

public static void noSuchElementHandler(WebElement element,int retry)
{
	
	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
	Reporting.logReporter(Status.DEBUG, "Trying to Again find same element using xpath which is currently Not Found "+retry+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
int count = 0;
while (count < retry)
{

try {


if ( Validate.isElementPresent(element)) {



Reporting.logReporter(Status.DEBUG, "Element is now  Enabled and clickable and  No such Exeception did not come,clearing text"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);

break;
}
} catch (NoSuchElementException e){
Logging.logReporter(Status.DEBUG, EXCEPTION_MSG + e);
Reporting.logReporter(Status.DEBUG, "Trying to recover from a No such element Exception"+ e.getMessage()+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
count = count + 1;

BaseSteps.Waits.waitGeneric(1000);
Reporting.logReporter(Status.DEBUG, "Increase in count value due to No such element Exception retry count"+ count+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
}

}
}

/**
 * Exception Handling Method, 
 * Handles Element Click Intercepted Exception
 *
 * @param element:
 *            WebElement Object
 *               
 * @param retry:         
 *    No of iterations of the loop      
 */

public static void elementClickInterceptedhandler(WebElement element,int retry)
{
	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
	
Reporting.logReporter(Status.DEBUG, "Trying to Again Click Same element using xpath whose Click is Currently Intercepted "+retry+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);



WebDriverSession.getWebDriverSession().manage().window().maximize();
int count = 0;
while (count < retry)
{

try {


Boolean x=element.isDisplayed();
Boolean y=element.isEnabled();



Logging.logReporter(Status.DEBUG, "//Is Enabled Status "+y);
Reporting.logReporter(Status.DEBUG, "Is Displayed Status  "+x+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
Reporting.logReporter(Status.DEBUG, "Is Enabled Status  "+y+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);

if ( element.isDisplayed()&& element.isEnabled()) {


BaseSteps.Clicks.clickElementUsingJS(element);

Reporting.logReporter(Status.DEBUG, "Element is now clickable and Element Click Intercepted Exception did not come,clearing text"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);

break;
}
} catch (ElementClickInterceptedException e){
Logging.logReporter(Status.DEBUG, EXCEPTION_MSG + e);
Reporting.logReporter(Status.DEBUG, "Trying to recover from Element click interceptable Exception"+ e.getMessage()+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);

count = count + 1;

BaseSteps.Waits.waitGeneric(1000);
Reporting.logReporter(Status.DEBUG, "Increase in count value due to Element Click Intercepted Exception retry count"+ count+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);

}

}
}
}
    
    
    
    public static class Debugs {

        private static long startTime = 0;
    	private static long stopTime = 0;
    	private static boolean running = false;


    	public static void start() {
    	    startTime = System.currentTimeMillis();
    	    running = true;
    	}


    	public static void stop() {
    	    stopTime = System.currentTimeMillis();
    	    running = false;
    	}
    	
    	public static long getElapsedTimeSecs() {
    	    long elapsed;
    	    if (running) {
    	        elapsed = ((System.currentTimeMillis() - startTime) / 1000);
    	    }
    	    else {
    	        elapsed = ((stopTime - startTime) / 1000);
    	    }
    	    return elapsed;
    	}
    	
    	private Debugs() {
 		    throw new IllegalStateException(UTILITY_CLASS);
 		  }

        /**
         * <p>
         * Use this method for debugging only. Will Hide the element modifying the CSS Styles. Note that it might not work depending on how the page was created
         * </p>
         */
        public static void hideElement(String elementName) {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            String script = "document.getElementsByName('%s')[0].style.display='none'";
            JavaScripts.executeJavaScript(String.format(script, elementName));
            Reporting.logReporter(Status.DEBUG, "JS - hideElement done"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
        }

        /**
         * <p>
         * Use this method for debugging only. Remove after Debug.
         * </p>
         */
        public static void highlightElement(WebElement element) {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            String script = "arguments[0].setAttribute('style', 'border: 5px solid #ffcc00;')";
            JavaScripts.executeJavaScript(script, element);
            Reporting.logReporter(Status.DEBUG, "JS - highlightElement done"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
        }

          /**
         * <p>
         * Method to put Element into the view by using JS
         * </p>
         */
        
        public static void scrollToElement(WebElement element) {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
        	Debugs.start();
        	    //code you want to time goes here
        	
        	    
            JavaScripts.executeJavaScript("arguments[0].scrollIntoView();", element);
           
            Debugs.stop();
            System.out.println("elapsed time in seconds for scroll to element: " + Debugs.getElapsedTimeSecs());
            Waits.waitGeneric(2000); // Allow time for scroll to complete
            Reporting.logReporter(Status.DEBUG, "scrollToElement done with time taken "+ Debugs.getElapsedTimeSecs()+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
        }

        /**
         * <p>
         * Method to put Element into the view by using Selenium actions
         * </p>
         */
        
        public static void scrollToElementWitActions(WebElement element) 
        
        {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
        	Debugs.start();
    	    //code you want to time goes here
    	   
    	    new Actions(WebDriverSession.getWebDriverSession()).moveToElement(element).pause(Duration.ofSeconds(2)).build().perform(); 
    	    Debugs.stop();
    	    // Pause to allow the scroll to complete
    	    System.out.println("elapsed time in seconds for scroll to element with Actions" + Debugs.getElapsedTimeSecs());
            
    	    Reporting.logReporter(Status.DEBUG, "scrollToElementWitActions done with time taken"+ Debugs.getElapsedTimeSecs()+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
      
    	     
        }
        /**
         * <p>
         * Use this method for debugging only. Will Hide the element modifying the CSS Styles. Note that it might not work depending on how the page was created
         * </p>
         */
        public static void showElement(String elementName) {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            String script = "document.getElementsByName('%s')[0].style.display='blocked'";
            JavaScripts.executeJavaScript(String.format(script, elementName));
            Reporting.logReporter(Status.DEBUG, "JS - showElement done"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
        }
    }

    // Additional Elements that do not require global Setup (SystemProperties)

    /**
     * Utilities to deal with Dropdowns
     */
    public static class Dropdowns {
    	
    	private Dropdowns() {
 		    throw new IllegalStateException(UTILITY_CLASS);
 		  }

        /**
         * OBJECTIVE: Method to select a drop down option going through all options from the list.
         */
        public static void selectByGoingThroughList(WebElement dropdownElement, String expectedOptionText) {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            Select selectList = new Select(dropdownElement);

            int selectSize = selectList.getOptions().size();
            Reporting.logReporter(Status.DEBUG, "Size of list is " + selectSize+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);

            for (int i = 0; i <= selectSize; i++) {

                selectList.selectByIndex(i);

                String selectedOption = selectList.getFirstSelectedOption().getText();

                Reporting.logReporter(Status.DEBUG, "Selected Item " + selectedOption+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);

                if (selectedOption.trim().equals(expectedOptionText.trim())) {
                    Reporting.logReporter(Status.DEBUG, "Value Matched"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
                    break;
                } else {
                    Reporting.logReporter(Status.DEBUG, "Value Not Matched"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
                    if (i == selectSize) {
                        Reporting.logReporter(Status.DEBUG, "Value NOT FOUND in List " + expectedOptionText+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
                    }

                }
            }
        }

        /**
         * OBJECTIVE: Method to select a drop down option using the index.
         */
        public static void selectByIndex(WebElement dropdownElement, int index) {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
        	Select dropdown = new Select(dropdownElement);
            dropdown.selectByIndex(index);
            Reporting.logReporter(Status.DEBUG, "selectByIndex done"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
        }

        /**
         * OBJECTIVE: Method to select a drop down option using the value text.
         */
        public static void selectByValue(WebElement dropdownElement, String value) {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
        	Select dropdown = new Select(dropdownElement);
            dropdown.selectByValue(value);
            Reporting.logReporter(Status.DEBUG, "selectByValue done"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
        }

        /**
         * OBJECTIVE: Method to select a drop down option using the visible text.
         */
        public static void selectByVisibleText(WebElement dropdownElement, String optionVisibleText) {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
        	Select dropdown = new Select(dropdownElement);
            dropdown.selectByVisibleText(optionVisibleText);
            Reporting.logReporter(Status.DEBUG, "selectByVisibleText done"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
        }
    }

    /**
     * Utilities to Find Elements (By the usual dinamyc elements that cannot be mapped from the very beginning
     */
    public static class Finds {
    	
    	private Finds() {
 		    throw new IllegalStateException(UTILITY_CLASS);
 		  }

        /**
         * Method to be used when the elements are created on runtime, and not mapped as page objects
         */
        public static WebElement findElement(By selector) {
        	 return findElement(ExpectedConditions.presenceOfElementLocated(selector));
        }

        private static WebElement findElement(ExpectedCondition<WebElement> expectedCondition) {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            WebDriverWait wait = new WebDriverWait(WebDriverSession.getWebDriverSession(), GENERIC_LONG_TIME_OUT_SECONDS);
            Reporting.logReporter(Status.DEBUG, "FindElement done"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
            
            return wait.until(expectedCondition);
        }

        /**
         * Method to be used when the elements are created on runtime, and not mapped as page objects
         */
        public static List<WebElement> findElements(By selector) {
        	 return findElements(ExpectedConditions.presenceOfAllElementsLocatedBy(selector));
        }

        private static List<WebElement> findElements(ExpectedCondition<List<WebElement>> expectedCondition) {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            List<WebElement> elements = new ArrayList<>();
            try {
                Wait<WebDriver> wait = new WebDriverWait(WebDriverSession.getWebDriverSession(), GENERIC_LONG_TIME_OUT_SECONDS);
                elements = wait.until(expectedCondition);
            } catch (TimeoutException e) {
                // Ignore the exception as find elements can return an empty list.
            	Logging.logReporter(Status.DEBUG, EXCEPTION_MSG + e);
            }
            Reporting.logReporter(Status.DEBUG, "FindElement done"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
            
            return elements;
        }

        /**
         * Method to be used when the elements are created on runtime, and not mapped as page objects
         */
        public static WebElement findNestedElement(WebElement element, By subSelector) {
            return findElement(ExpectedConditions.presenceOfNestedElementLocatedBy(element, subSelector));
        }

        /**
         * Method to be used when the elements are created on runtime, and not mapped as page objects
         */
        public static List<WebElement> findNestedElements(By selector, By subSelector) {
            return findElements(ExpectedConditions.presenceOfNestedElementsLocatedBy(selector, subSelector));
        }
    }

    /**
     * Utilities to deal with Gets
     */
    public static class Getters {
    	
    	private Getters() {
 		    throw new IllegalStateException(UTILITY_CLASS);
 		  }

        /**
         * OBJECTIVE: Get Text (Using JS) This command will only work when there is direct text in the element.
         */
        public static String getElementTextUsingJS(WebElement e) {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            
            String text = (String) JavaScripts.executeJavaScript("return arguments[0].text;", e);
            Reporting.logReporter(Status.DEBUG, "getElementText"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
            return text;
        }

    }

    /**
     * Utilities to Interact with iFrames
     */
    public static class Iframes {

    	private Iframes() {
 		    throw new IllegalStateException(UTILITY_CLASS);
 		  }
    	
        private static final String MESSAGE_SWITCH_DONE = "switchToIframe done";

        /**
         * OBJECTIVE: Use this method to get a number of all iFrames from the current window.
         */
        public static int getIframesCount() {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            
            int count = WebDriverSession.getWebDriverSession().findElements(By.tagName("iframe")).size();
            Reporting.logReporter(Status.DEBUG, "getIframesCount: " + count+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
            return count;
        }

        /**
         * OBJECTIVE: Use this method to switch back from iFrame.
         */
        public static void switchBackToDefaultContent() {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            
            WebDriverSession.getWebDriverSession().switchTo().defaultContent();
            Reporting.logReporter(Status.DEBUG, "switchBackToDefaultContent done"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
        }

        /**
         * OBJECTIVE: Use this method to switch to an iFrame.
         */
        public static void switchToiFrame(int index) {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            
            WebDriverSession.getWebDriverSession().switchTo().frame(index);
            Reporting.logReporter(Status.DEBUG, MESSAGE_SWITCH_DONE+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
        }

        /**
         * OBJECTIVE: Use this method to switch to an iFrame.
         */
        public static void switchToiFrame(String iFrameName) {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            
            WebDriverSession.getWebDriverSession().switchTo().frame(iFrameName);
            Reporting.logReporter(Status.DEBUG, MESSAGE_SWITCH_DONE+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
        }

        /**
         * OBJECTIVE: Use this method to switch to an iFrame.
         */
        public static void switchToiFrame(WebElement element) {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            
            WebDriverSession.getWebDriverSession().switchTo().frame(element);
            Reporting.logReporter(Status.DEBUG, MESSAGE_SWITCH_DONE+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
        }

        /**
         * OBJECTIVE: switch to the first window that contains the expected iFrame and finally switch to that iFrame.
         */
        public static boolean switchToWindowThatContainsiFrameAndSwitchToiFrame(String iFrameName) {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            
            for (String winHandle : WebDriverSession.getWebDriverSession().getWindowHandles()) {
                WebDriverSession.getWebDriverSession().switchTo().window(winHandle);
                Reporting.logReporter(Status.DEBUG,"Generic wait  1 sec"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);    
      	  		

                Waits.waitGeneric(GENERIC_SHORT_TIME_OUT_SECONDS * ONE_SECOND);

                
                try {
                	  Reporting.logReporter(Status.DEBUG,"Now switching to iframe name"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);    
            	  		          	

                    switchToiFrame(iFrameName);
                    return true;
                } catch (NoSuchFrameException e) {
                    Reporting.logReporter(Status.DEBUG, "Looking for iFrame"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
                    Logging.logReporter(Status.DEBUG, EXCEPTION_MSG + e);
                }
            }

            Reporting.logReporter(Status.DEBUG, "iFrame not found at any window"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
            return false;
        }

    }

    /**
     * Utilities to execute JavaScripts
     */
    public static class JavaScripts {
    	
    	private JavaScripts() {
 		    throw new IllegalStateException(UTILITY_CLASS);
 		  }    	

        /**
         * OBJECTIVE: Execute JS
         */
        public static void executeJavaScript(String script) {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            
            JavascriptExecutor js = getJavaScriptExecutor();
            js.executeScript(script);
            Reporting.logReporter(Status.DEBUG, "JS Execution done"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
        }

        /**
         * OBJECTIVE: Execute JS, with WebElement.
         */
        public static Object executeJavaScript(String script, WebElement element) {
            return ((JavascriptExecutor) WebDriverSession.getWebDriverSession()).executeScript(script, element);
        }

        public static JavascriptExecutor getJavaScriptExecutor() {
            return ((JavascriptExecutor) WebDriverSession.getWebDriverSession());
        }

        /**
         * Handle certificate error
         *
         * @param driver
         *            WebDriver instance
         */
        public static void handleCertificateError() {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            
            WebDriver driver = WebDriverSession.getWebDriverSession();

            String pageTitle = driver.getTitle();
            while ("Certificate Error: Navigation Blocked".equals(pageTitle)) {
                driver.navigate().to("javascript:document.getElementById('overridelink').click()");
                pageTitle = driver.getTitle();
            }
            Reporting.logReporter(Status.DEBUG, "Handle Certificate Error"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
            
        }

        /**
         * Handle invalid certificate Error.
         *
         * @param driver
         *            WebDriver instance
         */
        public static void handleInvalidCertificateError() {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            
            WebDriver driver = WebDriverSession.getWebDriverSession();

            String pageTitle = driver.getTitle();
            while (pageTitle.contains("Privacy error") || pageTitle.contains("Error de privacidad")) {
                 driver.findElement(By.id("details-button")).click();
                driver.findElement(By.id("proceed-link")).click();
                pageTitle = driver.getTitle();
            }
            Reporting.logReporter(Status.DEBUG, "Handle InvalidCertificate Error"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
            
        }

        /**
         * Handle unsecure connection
         *
         * @param driver
         *            WebDriver instance
         */
        public static void handleUnsecureConnectionError() {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            
            WebDriver driver = WebDriverSession.getWebDriverSession();

            String pageTitle = driver.getTitle();
            while ("This site isn’t secure".equals(pageTitle)) {
                driver.navigate().to("javascript:document.getElementById('overridelink').click()");
                pageTitle = driver.getTitle();
            }
            Reporting.logReporter(Status.DEBUG, "Handle UnsecureConnectionError"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
            
        }

        public static void scrollToBottom() {
            executeJavaScript("window.scrollTo(0, document.body.scrollHeight)");
        }

    }

    /**
     * Method to be used when the elements are created on runtime, and not mapped as page objects
     */
    public static class SeleniumActions {
    	
    	private SeleniumActions() {
 		    throw new IllegalStateException(UTILITY_CLASS);
 		  }

        public static Actions getActions() {
            return new Actions(WebDriverSession.getWebDriverSession());
        }
        public static void scanAndSendKeys(WebElement element, String text) {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            
            Waits.waitForElementToBeClickable(element);

            Actions action = getActions();
        	action.sendKeys(text).sendKeys(Keys.ENTER).build().perform();


            Reporting.logReporter(Status.DEBUG, CLEAR_SEND_KEYS_MSG+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
        }
        
        /**
         * OBJECTIVE: Method to ClickElements Using Actions, after each action a wait(Custom Time) is done.
         */
        private static void hoverAndClickTwoElementsWithCustomtWait(int pauseMilliSeconds, WebElement element1, WebElement element2) {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            
        	Actions action = getActions();
            action.moveToElement(element1);
            action.pause(pauseMilliSeconds);
            action.click(element1);
            action.pause(pauseMilliSeconds);
            action.moveToElement(element2);
            action.pause(pauseMilliSeconds);
            action.click(element2);
            action.build();
            action.perform();

            Reporting.logReporter(Status.DEBUG, "hoverAndClickTwoElementsWithCustomtWait done"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
        }

        /**
         * OBJECTIVE: Method to ClickElements Using Actions, after each action a wait(Default Time) is done.
         */
        public static void hoverAndClickTwoElementsWithDefaultWait(WebElement element1, WebElement element2) {
            hoverAndClickTwoElementsWithCustomtWait(1500, element1, element2);
        }

        /**
         * OBJECTIVE: Method to ClickElements Using Actions, after each action a wait(Short Time) is done.
         */
        public static void hoverAndClickTwoElementsWithShortWait(WebElement element1, WebElement element2) {
            hoverAndClickTwoElementsWithCustomtWait(GENERIC_SHORT_TIME_OUT_SECONDS, element1, element2);
        }

    }

    public static class SendKeys {
    	
    	private SendKeys() {
 		    throw new IllegalStateException(UTILITY_CLASS);
 		  }

        /**
         * OBJECTIVE: Waits for element to be visible/enable / Then clear the field / then sent the text
         */
        public static void clearFieldAndSendKeys(WebElement element, Keys keys) {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            
            Waits.waitForElementToBeClickable(element);
            element.clear();
            element.sendKeys(keys);
            Reporting.logReporter(Status.DEBUG, CLEAR_SEND_KEYS_MSG+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
        }

        /**
         * OBJECTIVE: Waits for element to be visible/enable / Then clear the field / then sent the text
         */
        public static void clearFieldAndSendKeys(WebElement element, String text) {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            
            Waits.waitForElementToBeClickable(element);
            element.clear();
            element.sendKeys(text);
            Reporting.logReporter(Status.DEBUG, CLEAR_SEND_KEYS_MSG+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
        }
        
      
        

        /**
         * OBJECTIVE: Waits for element to be visible/enable / Then clear the field / then sent the text
         */
        public static void clearFieldAndSendKeysUsingJS(WebElement element, String text) {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            
            Waits.waitForElementToBeClickable(element);
            element.clear();
            String script = "arguments[0].value='%s';";
            JavaScripts.executeJavaScript(String.format(script, text), element);
            Reporting.logReporter(Status.DEBUG, "clearFieldAndSendKeysUsingJS done"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
        }

        /**
         * OBJECTIVE: Waits for element to be visible/enable / then sent the text. Note this method do not clean the field before
         */
        public static void sendKey(WebElement element, Keys keys) {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            
            Waits.waitForElementToBeClickable(element);
            element.sendKeys(keys);
            Reporting.logReporter(Status.DEBUG, "sendKey done"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
        }

        /**
         * OBJECTIVE: Waits for element to be visible/enable / then sent the text. Note this method do not clean the field before
         */
        public static void sendKey(WebElement element, String text) {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            
            Waits.waitForElementToBeClickable(element);
            element.sendKeys(text);
            Reporting.logReporter(Status.DEBUG, "sendKey done"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
        }

        /**
         * OBJECTIVE: Waits for element to be visible/enable / then sent the text. Note this method do not clean the field before
         */
        public static void sendKeyUsingJS(WebElement element, String text) {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            
            Waits.waitForElementToBeClickable(element);
            String script = "arguments[0].value='%s';";
            JavaScripts.executeJavaScript(String.format(script, text), element);
            Reporting.logReporter(Status.DEBUG, "sendKeyUsingJS done"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
        }
    }

    /**
     * Utilities to do waits
     */
    public static class Waits {
    	
    	private Waits() {
 		    throw new IllegalStateException(UTILITY_CLASS);
 		  }

        private static final String MESSAGE_ANGULAR_TESTABILITIES = "return window.getAllAngularTestabilities().findIndex(x=>!x.isStable()) === -1";

        private static final String MESSAGE_DOCUMENT_READY = "return document.readyState";
        private static final String MESSAGE_COMPLETE = "complete";

        /**
         * Utility for Ajax validations
         */
        private static void ajaxComplete() {
            JavascriptExecutor jsExec = JavaScripts.getJavaScriptExecutor();

            jsExec.executeScript("var callback = arguments[arguments.length - 1];" + "var xhr = new XMLHttpRequest();" + "xhr.open('GET', '/Ajax_call', true);"
                    + "xhr.onreadystatechange = function() {" + "  if (xhr.readyState == 4) {" + "    callback(xhr.responseText);" + "  }" + "};" + "xhr.send();");
        }

        /**
         * Utility for Angular validations
         */
        private static void angularLoads(String angularReadyScript, int waitInSeconds) {
            WebDriverWait wait = new WebDriverWait(WebDriverSession.getWebDriverSession(), waitInSeconds);
            JavascriptExecutor jsExec = JavaScripts.getJavaScriptExecutor();

            try {
                ExpectedCondition<Boolean> angularLoad = driver -> Boolean.valueOf(((JavascriptExecutor) driver).executeScript(angularReadyScript).toString());

                // boolean angularReady =

                boolean angularReady = Boolean.parseBoolean(jsExec.executeScript(angularReadyScript).toString());
                if (!angularReady) {
                    wait.until(angularLoad);
                }
            } catch (WebDriverException ignored) {
            	Logging.logReporter(Status.DEBUG, EXCEPTION_MSG + ignored);
                // Nothing to do
            }
        }

        /**
         * Polling time
         */
        private static void poll(long milis) {
            try {
                Thread.sleep(milis);
            } catch (InterruptedException e) {
            	Logging.logReporter(Status.DEBUG, EXCEPTION_MSG + e);
            	Thread.currentThread().interrupt();
            }
        }

        /**
         * OBJECTIVE: Setup the implicit wait for the driver. Warning will affect all the driver waits.
         */
        public static void setImplicitWait(int waitInSseconds) {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            
            WebDriverSession.getWebDriverSession().manage().timeouts().implicitlyWait(waitInSseconds, TimeUnit.SECONDS);
            Reporting.logReporter(Status.DEBUG, "setImplicitWait done"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
        }

        public static void waitForAjaxLoaderSpinnerInvisibility(int toAppear, int maxTimeout) {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            
            try {
                int i = 0;
                boolean flag = false;

                while (true) {
                    Boolean spinnerIsFound = (Boolean) ((JavascriptExecutor) WebDriverSession.getWebDriverSession()).executeScript("return $('.ajax_loader').is(':visible') == true");

                    if (spinnerIsFound) {
                        flag = true;
                        break;
                    }

                    i++;
                    if (i > toAppear) {
                        break;
                    }

                    Thread.sleep(1000);
                }

                i = 0;
                if (flag) {
                    while (true) {

                        Boolean spinnerIsComplete = (Boolean) ((JavascriptExecutor) WebDriverSession.getWebDriverSession()).executeScript("return $('.ajax_loader').is(':visible') == false");
                        if (spinnerIsComplete) {
                            break;
                        }

                        i++;
                        if (i > maxTimeout) {
                            Assert.fail("Infinite loading");
                        }

                        Thread.sleep(1000);
                    }
                }

                Reporting.logReporter(Status.DEBUG, "waitForAjaxLoaderSpinnerInvisibility Done"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);

            } catch (RuntimeException e) {
                Reporting.logReporter(Status.DEBUG, "Error: " + e.getMessage()+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
                Logging.logReporter(Status.DEBUG, EXCEPTION_MSG + e);
            } catch (InterruptedException e) {
            	Reporting.logReporter(Status.DEBUG, "Error: " + e.getMessage()+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
                Logging.logReporter(Status.DEBUG, EXCEPTION_MSG + e);
                Thread.currentThread().interrupt();
                
			}
        }

	/**
		 * OBJECTIVE: Wait until Alert with custom time
		 */
		public static void waitForAlertVisibility(int secondsWait) {

			for (int i = 0; i < secondsWait; i++) {
				BaseSteps.Waits.waitGeneric(1000);
				if (Alerts.isAlertDisplayed()) {
					return;
				}
			}

			throw new UnsupportedOperationException("Wait Time out, Alert not displayed");
		}

		/**
		 * OBJECTIVE: Short Wait for Alert
		 */
		public static void waitForAlertVisibility() {
			waitForAlertVisibility(GENERIC_SHORT_TIME_OUT_SECONDS);
		}

		/**
		 * OBJECTIVE: Long Wait for Alert
		 */
		public static void waitForAlertVisibilityLongWait() {
			waitForAlertVisibility(GENERIC_LONG_TIME_OUT_SECONDS);
		}

		/**
		 * OBJECTIVE: Wait until Alert is not displayed
		 */
		public static void waitForAlertInvisibility(int secondsWait) {

			for (int i = 0; i < secondsWait; i++) {
				BaseSteps.Waits.waitGeneric(1000);
				if (!Alerts.isAlertDisplayed()) {
					return;
				}
			}

			throw new UnsupportedOperationException("Wait Time out, Alert is displayed");
		}

		/**
		 * OBJECTIVE: Short Wait for Alert Invisibility (Not displayed)
		 */
		public static void waitForAlertInvisibility() {
			waitForAlertInvisibility(GENERIC_SHORT_TIME_OUT_SECONDS);
		}

		/**
		 * OBJECTIVE: Long Wait for Alert Invisibility (Not displayed)
		 */
		public static void waitForAlertInvisibilityLongWait() {
			waitForAlertInvisibility(GENERIC_LONG_TIME_OUT_SECONDS);
		}
        /**
         * OBJECTIVE: Short Wait for an element to be invisible (The element Exist but, is not visible) e.g. A Hidden Menu.
         */
        public static void waitForElementInvisibility(WebElement element) {
            waitForElementInvisibility(element, GENERIC_SHORT_TIME_OUT_SECONDS);
        }

        public static void waitForElementInvisibility(WebElement element, int seconds) {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            
            try {
                element.isDisplayed();
            } catch (NoSuchElementException e) {
                // Expected error if element is not even present
                Reporting.logReporter(Status.DEBUG, "waitForElementInvisibility done"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
                Logging.logReporter(Status.DEBUG, EXCEPTION_MSG + e);
                return;
            }

            try {
                WebDriverWait wait = new WebDriverWait(WebDriverSession.getWebDriverSession(), seconds);
                wait.until(ExpectedConditions.not(ExpectedConditions.visibilityOf(element)));
            } catch (NoSuchElementException e) {
                // Expected error if element is not even present
                Reporting.logReporter(Status.DEBUG, "waitForElementInvisibility done"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
                Logging.logReporter(Status.DEBUG, EXCEPTION_MSG + e);
                return;
            }
        }

        /**
         * OBJECTIVE: Wait for an element to be invisible (The element Exist but, is not visible) e.g. A Hidden Menu.
         */
        public static void waitForElementInvisibilityLongWait(WebElement element) {
            waitForElementInvisibility(element, GENERIC_LONG_TIME_OUT_SECONDS);
        }

        /**
         * OBJECTIVE: Short wait for an element to display specific text for specific time in seconds. Use in the steps and use Element, expected text and seconds to wait as
         * inputs.
         */
        public static void waitForElementText(WebElement element, String strExpectedText) {
            waitForElementText(element, strExpectedText, GENERIC_SHORT_TIME_OUT_SECONDS);
        }

        private static void waitForElementText(WebElement element, String strExpectedText, int seconds) {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            
            WebDriverWait wait = new WebDriverWait(WebDriverSession.getWebDriverSession(), seconds);
            wait.until(ExpectedConditions.textToBePresentInElement(element, strExpectedText));
            Reporting.logReporter(Status.DEBUG, "waitForElementText done"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
        }

        /**
         * OBJECTIVE: Method to wait for an element to display specific text for specific time in seconds. Use in the steps and use Element, expected text and seconds to wait as
         * inputs.
         */
        public static void waitForElementTextLongWait(WebElement element, String strExpectedText) {
            waitForElementText(element, strExpectedText, GENERIC_LONG_TIME_OUT_SECONDS);
        }

        public static void waitForElementToBeClickable(WebElement element) {
            waitForElementToBeClickable(element, GENERIC_SHORT_TIME_OUT_SECONDS);
        }
        
      

        		
        private static void waitForElementToBeClickable(WebElement element, int seconds) 
        
        {
        	
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            
            WebDriverWait wait = new WebDriverWait(WebDriverSession.getWebDriverSession(), seconds);
            wait.until(ExpectedConditions.elementToBeClickable(element));
            Reporting.logReporter(Status.DEBUG, "waitForElementToBeClickable done"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
        }

        public static void waitForElementToBeClickableLongWait(WebElement element) {
            waitForElementToBeClickable(element, GENERIC_LONG_TIME_OUT_SECONDS);
        }

        /**
         * OBJECTIVE: Short Wait for an element visibility
         */
        public static void waitForElementVisibility(WebElement element) {
            waitForElementVisibility(element, GENERIC_SHORT_TIME_OUT_SECONDS);
        }

        public static void waitForElementVisibility(WebElement element, int seconds) {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            
            WebDriverWait wait = new WebDriverWait(WebDriverSession.getWebDriverSession(), seconds);
            wait.until(ExpectedConditions.visibilityOf(element));
            Reporting.logReporter(Status.DEBUG, "waitForElementVisibility done"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
        }

        /**
         * OBJECTIVE: Long Wait for an element visibility
         */
        public static void waitForElementVisibilityLongWait(WebElement element) {
            waitForElementVisibility(element, GENERIC_LONG_TIME_OUT_SECONDS);
        }

        public static void waitForTabsToOpen(int expectedNumberOfTabs) {
            waitForTabsToOpen(expectedNumberOfTabs, GENERIC_SHORT_TIME_OUT_SECONDS);
        }

        public static void waitForTabsToOpen(int expectedNumberOfTabs, int seconds) {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            
            WebDriverWait wait = new WebDriverWait(WebDriverSession.getWebDriverSession(), seconds);
            wait.until(ExpectedConditions.numberOfWindowsToBe(expectedNumberOfTabs));
            Reporting.logReporter(Status.DEBUG, "waitForTabsToOpen done"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
        }

        public static void waitForTabsToOpenLongWait(int expectedNumberOfTabs) {
            waitForTabsToOpen(expectedNumberOfTabs, GENERIC_LONG_TIME_OUT_SECONDS);
        }

        /**
         * OBJECTIVE: Generic wait to wait for an specific period of time in milliseconds. Avoid the use of the same, instead use custom expect waits.
         */
        public static void waitGeneric(int milliseconds) {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            
            try {
                Thread.sleep(milliseconds);
                Reporting.logReporter(Status.DEBUG, "waitGeneric done"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
            } catch (InterruptedException e) {
            	Logging.logReporter(Status.DEBUG, EXCEPTION_MSG + e);
                Reporting.logReporter(Status.DEBUG, "waitGeneric error"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
                Thread.currentThread().interrupt();
            }

        }

        /**
         * OBJECTIVE: Method to do a Wait(Custom) until the Conditions is true
         */
        @SuppressWarnings("deprecation")
		private static void waitUntil(Function<WebDriver, Boolean> waitCondition, Long timeoutInSeconds) {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            
            WebDriver driver = WebDriverSession.getWebDriverSession();

            WebDriverWait webDriverWait = new WebDriverWait(driver, timeoutInSeconds);
            webDriverWait.withTimeout(timeoutInSeconds, TimeUnit.SECONDS);
            try {
                webDriverWait.until(waitCondition);
            } catch (RuntimeException e) {
                Reporting.logReporter(Status.DEBUG, e.getMessage()+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
                Logging.logReporter(Status.DEBUG, EXCEPTION_MSG + e);
            }
        }

        /**
         * OBJECTIVE: Default wait for Angular5 Loads
         */
        private static void waitUntilAngular5Load() {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            
            String angularReadyScript = MESSAGE_ANGULAR_TESTABILITIES;
            angularLoads(angularReadyScript, GENERIC_LONG_TIME_OUT_SECONDS);
            Reporting.logReporter(Status.DEBUG, "waitUntilAngular5Load done"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
        }

        /**
         * OBJECTIVE: Long wait for Angular5 Loads
         */
        private static void waitUntilAngular5LoadLongWait() {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            
            String angularReadyScript = MESSAGE_ANGULAR_TESTABILITIES;
            angularLoads(angularReadyScript, GENERIC_PAGE_TIME_OUT_SECONDS);
            Reporting.logReporter(Status.DEBUG, "waitUntilAngular5LoadLongWait done"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
        }

        /**
         * OBJECTIVE: Default wait for Angular5 Ready
         */
        public static void waitUntilAngular5Ready() {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            
            JavascriptExecutor jsExec = ((JavascriptExecutor) WebDriverSession.getWebDriverSession());

            try {
                Object angular5Check = jsExec.executeScript("return getAllAngularRootElements()[0].attributes['ng-version']");
                if (angular5Check != null) {
                    Boolean angularPageLoaded = (Boolean) jsExec.executeScript(MESSAGE_ANGULAR_TESTABILITIES);
                    if (!angularPageLoaded) {
                        poll(20);

                        waitUntilAngular5Load();

                        poll(20);
                    }
                }
            } catch (WebDriverException ignored) {
            	Logging.logReporter(Status.DEBUG, EXCEPTION_MSG + ignored);
            }
            Reporting.logReporter(Status.DEBUG, "waitUntilAngular5Ready done"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
        }

        /**
         * OBJECTIVE: Long wait for Angular5 Ready
         */
        public static void waitUntilAngular5ReadyLongWait() {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            
            JavascriptExecutor jsExec = ((JavascriptExecutor) WebDriverSession.getWebDriverSession());

            try {
                Object angular5Check = jsExec.executeScript("return getAllAngularRootElements()[0].attributes['ng-version']");
                if (angular5Check != null) {
                    Boolean angularPageLoaded = (Boolean) jsExec.executeScript(MESSAGE_ANGULAR_TESTABILITIES);
                    if (!angularPageLoaded) {
                        poll(20);

                        waitUntilAngular5LoadLongWait();

                        poll(20);
                    }
                }
            } catch (WebDriverException ignored) {
            	Logging.logReporter(Status.DEBUG, EXCEPTION_MSG + ignored);
            }
            Reporting.logReporter(Status.DEBUG, "waitUntilAngular5ReadyLongWait done"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
        }

        /**
         * OBJECTIVE: Default wait for Angular Loads
         */
        // waitForAngularLoad
        private static void waitUntilAngularLoad() {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            
            String angularReadyScript = "return angular.element(document).injector().get('$http').pendingRequests.length === 0";
            angularLoads(angularReadyScript, GENERIC_LONG_TIME_OUT_SECONDS);
            Reporting.logReporter(Status.DEBUG, "waitUntilAngularLoad done"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
        }

        /**
         * OBJECTIVE: Long wait for Angular Loads
         */
        // waitForAngularLoad
        private static void waitUntilAngularLoadLongWait() {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            
            String angularReadyScript = "return angular.element(document).injector().get('$http').pendingRequests.length === 0";
            angularLoads(angularReadyScript, GENERIC_PAGE_TIME_OUT_SECONDS);
            Reporting.logReporter(Status.DEBUG, "waitUntilAngularLoadLongWait done"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
        }

        /**
         * OBJECTIVE: Long wait for Angular Ready
         */
        public static void waitUntilAngularReady() {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            
            JavascriptExecutor jsExec = JavaScripts.getJavaScriptExecutor();

            try {
                Boolean angularUnDefined = (Boolean) jsExec.executeScript("return window.angular === undefined");
                if (!angularUnDefined) {
                    Boolean angularInjectorUnDefined = (Boolean) jsExec.executeScript("return angular.element(document).injector() === undefined");
                    if (!angularInjectorUnDefined) {
                        poll(20);

                        waitUntilAngularLoad();

                        poll(20);
                    }
                }
            } catch (WebDriverException ignored) {
            	Logging.logReporter(Status.DEBUG, EXCEPTION_MSG + ignored);
            }

            Reporting.logReporter(Status.DEBUG, "waitUntilAngularReady done"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
        }

        /**
         * OBJECTIVE: Custom wait for Angular Ready
         */
        public static void waitUntilAngularReadyLongWait() {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            
            JavascriptExecutor jsExec = JavaScripts.getJavaScriptExecutor();

            try {
                Boolean angularUnDefined = (Boolean) jsExec.executeScript("return window.angular === undefined");
                if (!angularUnDefined) {
                    Boolean angularInjectorUnDefined = (Boolean) jsExec.executeScript("return angular.element(document).injector() === undefined");
                    if (!angularInjectorUnDefined) {
                        poll(20);

                        waitUntilAngularLoadLongWait();

                        poll(20);
                    }
                }
            } catch (WebDriverException ignored) {
            	Logging.logReporter(Status.DEBUG, EXCEPTION_MSG + ignored);
            }

            Reporting.logReporter(Status.DEBUG, "waitUntilAngularReadyLongWait done"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
        }

        /**
         * OBJECTIVE: Method to do a Wait(Default) until the JQuery Count is Zero, (By the usual means that the request for certain event has finished, e.g. DB request)
         */
        private static void waitUntilJqueryLoadComplete() {
            waitUntilJqueryLoadCompleteCustomTimeOut(Long.valueOf(GENERIC_LONG_TIME_OUT_SECONDS));
        }

        /**
         * waitForJQueryLoad OBJECTIVE: Method to do a Wait(Custom) until the JQuery Count is Zero, (By the usual means that the request for certain event has finished, e.g. DB
         * request)
         */
        private static void waitUntilJqueryLoadCompleteCustomTimeOut(Long timeoutInSeconds) {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            
            WebDriver driver = WebDriverSession.getWebDriverSession();

            waitUntil(d -> {
                Boolean isJqueryCallDone = (Boolean) ((JavascriptExecutor) driver).executeScript("return jQuery.active==0");
                if (!isJqueryCallDone) {
                	Logging.logReporter(Status.DEBUG, "Jquery Load not completed");
                }
                return isJqueryCallDone;
            }, timeoutInSeconds);

            Reporting.logReporter(Status.DEBUG, "waitUntilJqueryIsDone done"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
        }

        /**
         * OBJECTIVE: Method to do a Wait(Long) until the JQuery Count is Zero, (By the usual means that the request for certain event has finished, e.g. DB request)
         */
        private static void waitUntilJqueryLoadCompleteLongWait() {
            waitUntilJqueryLoadCompleteCustomTimeOut(Long.valueOf(GENERIC_PAGE_TIME_OUT_SECONDS));
        }

        /**
         * OBJECTIVE: Default wait for JQuery Ready
         */
        public static void waitUntilJQueryReady() {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            
            JavascriptExecutor jsExec = ((JavascriptExecutor) WebDriverSession.getWebDriverSession());
            Boolean jQueryDefined = (Boolean) jsExec.executeScript("return typeof jQuery != 'undefined'");
            if (jQueryDefined) {
                poll(20);

                waitUntilJqueryLoadComplete();

                poll(20);
            }
            Reporting.logReporter(Status.DEBUG, "waitUntilJQueryReady done"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
        }

        /**
         * OBJECTIVE: Long wait for JQuery Ready
         */
        public static void waitUntilJQueryReadyLongWait() {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            
            JavascriptExecutor jsExec = ((JavascriptExecutor) WebDriverSession.getWebDriverSession());
            Boolean jQueryDefined = (Boolean) jsExec.executeScript("return typeof jQuery != 'undefined'");
            if (jQueryDefined) {
                poll(20);

                waitUntilJqueryLoadCompleteLongWait();

                poll(20);
            }
            Reporting.logReporter(Status.DEBUG, "waitUntilJQueryReady done"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
        }

        /**
         * OBJECTIVE: Default wait for JS ready state, Ajax call completed, JQuery Ready state, Angular Ready, Angular5 Ready
         */
        public static void waitUntilJsAjaxJqueryAngularAngular5RequestsReady() {
            waitUntilJSReady();
            ajaxComplete();
            waitUntilJQueryReady();
            waitUntilAngularReady();
            waitUntilAngular5Ready();
        }

        /**
         * OBJECTIVE: Long wait for JS ready state, Ajax call completed, JQuery Ready state, Angular Ready, Angular5 Ready
         */
        public static void waitUntilJsAjaxJqueryAngularAngular5RequestsReadyLongWait() {
            waitUntilJSReadyLongWait();
            ajaxComplete();
            waitUntilJQueryReadyLongWait();
            waitUntilAngularReadyLongWait();
            waitUntilAngular5ReadyLongWait();
        }

        /**
         * OBJECTIVE: Custom wait for JS Ready
         */
        public static void waitUntilJSReady() {
            waitUntilJSReadyCustomTimeOut(GENERIC_LONG_TIME_OUT_SECONDS);
        }

        // waitUntilJSReady
        private static void waitUntilJSReadyCustomTimeOut(int waitInSeconds) {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            
            WebDriverWait wait = new WebDriverWait(WebDriverSession.getWebDriverSession(), waitInSeconds);
            JavascriptExecutor jsExec = ((JavascriptExecutor) WebDriverSession.getWebDriverSession());

            try {
                ExpectedCondition<Boolean> jsLoad = driver -> ((JavascriptExecutor) WebDriverSession.getWebDriverSession()).executeScript(MESSAGE_DOCUMENT_READY).toString().equals(MESSAGE_COMPLETE);

                boolean jsReady = jsExec.executeScript(MESSAGE_DOCUMENT_READY).toString().equals(MESSAGE_COMPLETE);

                if (!jsReady) {
                    wait.until(jsLoad);
                }
            } catch (WebDriverException ignored) {
            	Logging.logReporter(Status.DEBUG, EXCEPTION_MSG + ignored);
            }
            Reporting.logReporter(Status.DEBUG, "waitUntilJSReadyCustomTimeOut done"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
        }

        /**
         * OBJECTIVE: lONG wait for JS Ready
         */
        public static void waitUntilJSReadyLongWait() {
            waitUntilJSReadyCustomTimeOut(GENERIC_PAGE_TIME_OUT_SECONDS);
        }

        /**
         * OBJECTIVE: Short wait - Method to create An expectation for checking that the First element from a list is present.
         */
        public static void waitUntilListFirstElementIsPresent(List<WebElement> list) {
            waitUntilListFirstElementIsPresent(list, GENERIC_SHORT_TIME_OUT_SECONDS);
        }

        private static void waitUntilListFirstElementIsPresent(List<WebElement> list, int timeOutInSeconds) {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            
            for (int i = 1; i <= timeOutInSeconds; i++) {
                try {
                    waitGeneric(ONE_SECOND);
                    list.get(0);
                    Reporting.logReporter(Status.DEBUG, "waitUntilListFirstElementIsPresent done"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
                    return; // To Exit after success Found
                } catch (IndexOutOfBoundsException e) {
                	Logging.logReporter(Status.DEBUG, EXCEPTION_MSG + e);
                    // Do nothing Expected Failure
                }
            }

            throw new UnsupportedOperationException("Element not present after try for " + timeOutInSeconds + " seconds");
        }

        /**
         * OBJECTIVE: Method to create An expectation for checking that the First element from a list is present.
         */
        public static void waitUntilListFirstElementIsPresentLongWait(List<WebElement> list) {
            waitUntilListFirstElementIsPresent(list, GENERIC_LONG_TIME_OUT_SECONDS);
        }

        /**
         * OBJECTIVE: Method to do a Wait(Default) until the page return document.readyState = complete, Means the page DOM hhas loaded, but that does not mean that is accessible;
         * Recommended to use in conjunction of a wait for some Element Visibility.
         */
        public static void waitUntilPageLoadComplete() {
            waitUntilPageLoadComplete(Long.valueOf(GENERIC_LONG_TIME_OUT_SECONDS));
        }

        /**
         * OBJECTIVE: Method to do a Wait(Custom) until the page return document.readyState = complete, Means the page DOM has loaded, but that does not mean that is accessible;
         * Recommended to use in conjunction of a wait for some Element Visibility.
         */
        private static void waitUntilPageLoadComplete(Long timeoutInSeconds) {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            
            WebDriver driver = WebDriverSession.getWebDriverSession();

            waitUntil(d -> {
                Boolean isPageLoaded = ((JavascriptExecutor) driver).executeScript(MESSAGE_DOCUMENT_READY).equals(MESSAGE_COMPLETE);
                if (!isPageLoaded) {
                	Logging.logReporter(Status.DEBUG, "Page Load not completed");
                }
                return isPageLoaded;
            }, timeoutInSeconds);

            Reporting.logReporter(Status.DEBUG, "waitUntilPageLoadComplete done"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
        }

        /**
         * OBJECTIVE: Method to do a Wait(Long) until the page return document.readyState = complete, Means the page DOM hhas loaded, but that does not mean that is accessible;
         * Recommended to use in conjunction of a wait for some Element Visibility.
         */
        public static void waitUntilPageLoadCompleteLongWait() {
            waitUntilPageLoadComplete(Long.valueOf(GENERIC_PAGE_TIME_OUT_SECONDS));
        }

    }

    /**
     * Utilities to help interaction with WebElements
     */
    public static class WebElementUtils {
    	
    	private WebElementUtils() {
 		    throw new IllegalStateException(UTILITY_CLASS);
 		  }

        /**
         * OBJECTIVE: Know if a given element has a certain "class" name as attribute, useful for escenarios where some attribute is added under certain conditions
         */
        public static boolean elementHasClass(WebElement element, String classToLook) {
            return element.getAttribute("class") != null && element.getAttribute("class").contains(classToLook);
        }

        /**
         * OBJECTIVE: Some Elements Unique selector is not the one that we need to interact, so by the usual the "parent" will allow to perform some interaction with it.
         */
        public static WebElement getParentElementFromCssClass(WebElement aCheckbox) {
            return aCheckbox.findElement(By.xpath(".."));
        }

        /**
         * get an array resized
         */
        public static Object resizeArray(Object oldArray, int newSize) {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            
            int oldSize = java.lang.reflect.Array.getLength(oldArray);
            Class elementType = oldArray.getClass().getComponentType();
            Object newArray = java.lang.reflect.Array.newInstance(elementType, newSize);
            int preserveLength = Math.min(oldSize, newSize);

            if (preserveLength > 0) {
                System.arraycopy(oldArray, 0, newArray, 0, preserveLength);
            } else {
                Reporting.logReporter(Status.DEBUG, "resizeArray cannot be done"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
            }
            return newArray;
        }
    }

    /**
     * Utilities to Interact with iFrames
     */
    public static class Windows {
    	
    	private Windows() {
 		    throw new IllegalStateException(UTILITY_CLASS);
 		  }

        private static final String JS_OPEN_CUSTOM_VIEWPORT_IN_NEW_WINDOW = "window.open('%s', '','width=%s,height=%s')";

        private static final String JS_OPEN_WINDOW_IN_NEW_TAB = "window.open('%s')";

        public static final void closeAllWindowsExceptOriginal(String originalwinHandle) {
            for (String winHandle : WebDriverSession.getWebDriverSession().getWindowHandles()) {
                WebDriverSession.getWebDriverSession().switchTo().window(winHandle);
                if (winHandle.equals(originalwinHandle)) {
                    // Do nothing
                } else {
                    WebDriverSession.getWebDriverSession().close();
                }
            }
        }

        /**
         * OBJECTIVE: Use this method to go back to the original window
         */
        public static void closeNewWindow(String originalWindow) {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            
            closeAllWindowsExceptOriginal(originalWindow);
            switchBackFromNewWindow(originalWindow);
            Reporting.logReporter(Status.DEBUG, "closeNewWindow done"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
        }

        public static final void closeUniqueWindow(String winHandleToClose) {
            for (String winHandle : WebDriverSession.getWebDriverSession().getWindowHandles()) {
                WebDriverSession.getWebDriverSession().switchTo().window(winHandle);
                if (winHandle.equals(winHandleToClose)) {
                    WebDriverSession.getWebDriverSession().close();
                }
            }
        }

        public static void maximizeWindow() {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            
            WebDriverSession.getWebDriverSession().manage().window().maximize();
            Waits.waitGeneric(ONE_SECOND * 2);
            Reporting.logReporter(Status.DEBUG, "maximizeWindow done"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
        }

        public static void navigateBack() {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            
            WebDriverSession.getWebDriverSession().navigate().back();
            Reporting.logReporter(Status.DEBUG, "navigateBack done"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
        }

        public static void navigateForward() {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            
            WebDriverSession.getWebDriverSession().navigate().forward();
            Reporting.logReporter(Status.DEBUG, "navigateForward done"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
        }

        /**
         * Open a new tab in the same Window and returns the handler (the focus is in the new Tab)
         */
        public static void openNewTab(String urlToLoad) {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            
            String jScript = String.format(JS_OPEN_WINDOW_IN_NEW_TAB, urlToLoad);
            JavaScripts.executeJavaScript(jScript);
            switchToNewWindow();
            Waits.waitUntilPageLoadComplete();
            Reporting.logReporter(Status.DEBUG, "openNewTab done"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
        }

        /**
         * Open a new window and returns the handler (the focus is in the new window)
         */
        public static void openNewWindow(String urlToLoad) {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            
            String jScript = String.format(JS_OPEN_CUSTOM_VIEWPORT_IN_NEW_WINDOW, urlToLoad, 500, 500); // open small then maximize
            JavaScripts.executeJavaScript(jScript);
            switchToNewWindow();
            // Extra steps to get the page similar to a normal one
            maximizeWindow();
            refresh(); // SOme pages are responsive, and some config might get stuck, this will allow us to avoid that.
            Waits.waitUntilPageLoadComplete();

            Reporting.logReporter(Status.DEBUG, "openNewWindow done"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
        }

        /**
         * For responsive testing - Open a new window with custom size and returns the handler (the focus is in the new window)
         */
        public static void openNewWindowViewport(String urlToLoad, int width, int height) {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            
            String originalWindow = WebDriverSession.getWebDriverSession().getWindowHandle();

            String jScript = String.format(JS_OPEN_CUSTOM_VIEWPORT_IN_NEW_WINDOW, urlToLoad, width, height);
            JavaScripts.executeJavaScript(jScript);

            switchToWindowViewport(originalWindow, urlToLoad);
            Waits.waitUntilPageLoadComplete();
            Reporting.logReporter(Status.DEBUG, "openNewWindowWithCustomSize done"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
        }

        public static void refresh() {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            
            WebDriverSession.getWebDriverSession().navigate().refresh();
            Reporting.logReporter(Status.DEBUG, "Refresh done"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
        }

        /**
         * OBJECTIVE: Use this method to go back to the original window when switching to a new one.
         */
        public static void switchBackFromNewWindow(String window) {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            
            WebDriverSession.getWebDriverSession().switchTo().window(window);
            Reporting.logReporter(Status.DEBUG, "switchBackFromNewWindow done"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
        }

        /**
         * OBJECTIVE: Use this method when steps flow open a new window.
         */
        public static String switchToNewWindow() {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            
            BaseSteps.Waits.waitGeneric(GENERIC_SHORT_TIME_OUT_SECONDS * ONE_SECOND);

            ArrayList<String> views = new ArrayList<>(WebDriverSession.getWebDriverSession().getWindowHandles());
            int size = views.size();

            WebDriverSession.getWebDriverSession().switchTo().window(views.get(size - 1));
            String winHandleBefore = WebDriverSession.getWebDriverSession().getWindowHandle();

            Reporting.logReporter(Status.DEBUG, "switchToNewWindow done"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
            return winHandleBefore;
        }

        public static boolean switchToWindowThatContainsPartialUrl(String partialUrl) {
            return switchToWindowThatContainsPartialUrl(partialUrl, GENERIC_SHORT_TIME_OUT_SECONDS);
        }

        private static boolean switchToWindowThatContainsPartialUrl(String partialUrl, int timeToWaitInSeconds) {
        	String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
            
            for (int i = 0; i <= timeToWaitInSeconds; i++) {
                Waits.waitGeneric(ONE_SECOND);

                for (String winHandle : WebDriverSession.getWebDriverSession().getWindowHandles()) {
                    WebDriverSession.getWebDriverSession().switchTo().window(winHandle);

                    if (WebDriverSession.getWebDriverSession().getCurrentUrl().contains(partialUrl)) {
                        Reporting.logReporter(Status.DEBUG, "switchToWindowThatContainsPartialUrl done"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
                        return true;
                    }
                }
            }

            Reporting.logReporter(Status.DEBUG, "Partial Url: " + partialUrl + " not Found in any window"+CALLING_MSG+NAME_OF_CURR_CLASS+"."+nameofCurrMethod);
            return false;
        }

        public static boolean switchToWindowThatContainsPartialUrlLongWait(String partialUrl) {
            return switchToWindowThatContainsPartialUrl(partialUrl, GENERIC_LONG_TIME_OUT_SECONDS);
        }

        /**
         * For responsive testing - Focus is in the new window, close the original window
         */
        private static boolean switchToWindowViewport(String originalWindow, String partialUrlToAssertForSwitch) {
            // Close the original
            closeUniqueWindow(originalWindow);

            // Back to the viewport version
            switchToNewWindow();
            Waits.waitUntilPageLoadComplete();

            return WebDriverSession.getWebDriverSession().getCurrentUrl().contains(partialUrlToAssertForSwitch);
        }
    }

    //private static final String CONSTRUCTOR_MESSAGE = "Utility class - Not Designed to be Instantiated"

    private static final int ONE_SECOND = 1000;

    /**
     * Global Variables, that are set by the SystemProperties file
     */
    public static final int GENERIC_LONG_TIME_OUT_SECONDS = SystemProperties.LONG_WAIT_TIMEOUT_MILLIS / ONE_SECOND;

    public static final int GENERIC_SHORT_TIME_OUT_SECONDS = SystemProperties.SHORT_WAIT_TIMEOUT_MILLIS / ONE_SECOND;

    public static final int GENERIC_PAGE_TIME_OUT_SECONDS = SystemProperties.GENERIC_PAGE_WAIT_TIMEOUT_MILLIS / ONE_SECOND;

}
