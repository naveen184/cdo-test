package com.test.desktop.actions;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.Status;
import com.test.logging.Logging;
import com.test.reporting.Reporting;
import com.test.utils.SystemProperties;

import winium.elements.desktop.ComboBox;
import winium.elements.desktop.DataGrid;
import winium.elements.desktop.ListBox;
import winium.elements.desktop.Menu;
import winium.elements.desktop.extensions.WebElementExtensions;

/**
 ****************************************************************************
 * HIGHLIGHTS: > Base methods to be used in the STEPS pages/implementations.
 ****************************************************************************
 */

public class DesktopBaseSteps {
	
	private static final String UTILITY_CLASS = "Utility class"; 
	private static final String EXCEPTION_MSG = "Exception Caught : ";
	
	/**
     * Utilities to set with AutomationPatterns in Elements - see https://github.com/2gis/Winium.Desktop/wiki/Command-Execute-Script
     */
    public static class AutomationPatterns {
    	
    	 private AutomationPatterns() {
 		    throw new IllegalStateException(UTILITY_CLASS);
 		  }

        /**
         * OBJECTIVE: set with AutomationPatterns in Element
         */
        public static void setElementAutomationPatternUsingJS(WebElement element, String valueToSet) {
            JavaScripts.executeJavaScript("automation: ValuePattern.SetValue", element, valueToSet);
            Reporting.logReporter(Status.DEBUG, "DESKTOP APP - setElementAutomationPatternUsingJS Done");
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
            Waits.waitForElementToBeClickable(element, waitInSeconds);
            element.click();
            Reporting.logReporter(Status.DEBUG, "DESKTOP APP - click Done");
        }

        /**
         * OBJECTIVE: Short Wait for an element to be Clickable to do the click, Try the regular method before this one
         */
        public static void clickElementCoordinates(WebElement element) {
            Waits.waitForElementVisibility(element);
            SeleniumActions.getActions().click(element).perform();
            Reporting.logReporter(Status.DEBUG, "DESKTOP APP - click ElementCoordinates Done");
        }

        /**
         * OBJECTIVE: Wait for an element to be Clickable to do the click, Try the regular method before this one
         */
        public static void clickElementCoordinatesLongWait(WebElement element) {
            Waits.waitForElementVisibilityLongWait(element);
            SeleniumActions.getActions().click(element).perform();
            Reporting.logReporter(Status.DEBUG, "DESKTOP APP - click ElementCoordinatesLongWait Done");
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
            for (WebElement e : list) {
                clickElementCoordinates(e);
                Reporting.logReporter(Status.DEBUG, "DESKTOP APP - click ElementsCoordinatesInList - Done");
            }
        }

        /**
         * OBJECTIVE: From the given list - Wait for an element to be Clickable to do the click
         */
        public static void clickElementsCoordinatesInListLongWait(List<WebElement> list) {
            for (WebElement e : list) {
                clickElementCoordinatesLongWait(e);
                Reporting.logReporter(Status.DEBUG, "DESKTOP APP - click clickElementsCoordinatesInListLongWait - Done");
            }
        }

        /**
         * OBJECTIVE: From the given list - Wait for an element to be Clickable to do the click
         */
        public static void clickElementsInList(List<WebElement> list) {
            for (WebElement e : list) {
                clickElement(e);
                Reporting.logReporter(Status.DEBUG, "DESKTOP APP - click ElementsInList - Done");
            }
        }

        /**
         * OBJECTIVE: From the given list - Wait for an element to be Clickable to do the click
         */
        public static void clickElementsInListLongWait(List<WebElement> list) {
            for (WebElement e : list) {
                clickElementLongWait(e);
                Reporting.logReporter(Status.DEBUG, "DESKTOP APP - click ElementsInListLongWait - Done");
            }
        }

        /**
         * OBJECTIVE: From the given list - Wait for an element to be Clickable to do the click
         */
        public static void clickElementsUsingJSInList(List<WebElement> list) {
            for (WebElement e : list) {
                clickElementUsingJS(e);
                Reporting.logReporter(Status.DEBUG, "DESKTOP APP - click clickElementsUsingJSInList - Done");
            }
        }

        /**
         * OBJECTIVE: From the given list - Wait for an element to be Clickable to do the click
         */
        public static void clickElementsUsingJSInListLongWait(List<WebElement> list) {
            for (WebElement e : list) {
                clickElementUsingJSLongWait(e);
                Reporting.logReporter(Status.DEBUG, "DESKTOP APP - click clickElementsUsingJSInListLongWait - Done");
            }
        }

        /**
         * OBJECTIVE: Short Wait for an element to be Clickable to do the click (With JavaScript).
         */
        public static void clickElementUsingJS(WebElement element) {
            clickElementUsingJS(element, GENERIC_SHORT_TIME_OUT_SECONDS);
        }

        /**
         * OBJECTIVE: Wait for an element to be Clickable to do the click in the center (With JavaScript), Try the regular method before this one, this should be a last option as
         * it is not interacting with the actual page.
         */
        private static void clickElementUsingJS(WebElement element, int waitInSeconds) {
            Waits.waitForElementVisibility(element, waitInSeconds);
            JavaScripts.executeJavaScript("input: brc_click", element);
            Reporting.logReporter(Status.DEBUG, "DESKTOP APP - clickElementUsingJS Done");
        }

        /**
         * OBJECTIVE: Wait for an element to be Clickable to do the click(With JavaScript).
         */
        public static void clickElementUsingJSLongWait(WebElement element) {
            clickElementUsingJS(element, GENERIC_LONG_TIME_OUT_SECONDS);
        }

        /**
         * OBJECTIVE: Wait for an element to be Clickable to do the Ctrl + click(With JavaScript).
         */
        public static void ctrlClickElementUsingJS(WebElement element) {
            ctrlClickElementUsingJS(element, GENERIC_SHORT_TIME_OUT_SECONDS);
        }

        /**
         * OBJECTIVE: Wait for an element to be Clickable to do the Ctrl + click(With JavaScript), Try the regular method before this one, this should be a last option as it is not
         * interacting with the actual page.
         */
        private static void ctrlClickElementUsingJS(WebElement element, int waitInSeconds) {
            Waits.waitForElementVisibility(element, waitInSeconds);
            JavaScripts.executeJavaScript("input: ctrl_click", element);
            Reporting.logReporter(Status.DEBUG, "DESKTOP APP - ctrlclickElementUsingJS Done");
        }

        /**
         * OBJECTIVE: Wait for an element to be Clickable to do the Ctrl + click(With JavaScript).
         */
        public static void ctrlClickElementUsingJSLongWait(WebElement element) {
            ctrlClickElementUsingJS(element, GENERIC_LONG_TIME_OUT_SECONDS);
        }

        /**
         * OBJECTIVE: Short Wait for an element to be Clickable to do the double click.
         */
        public static void doubleClickElement(WebElement element) {
            doubleClickElement(element, GENERIC_SHORT_TIME_OUT_SECONDS);
        }

        /**
         * OBJECTIVE: Wait for an element to be Clickable to do the double click.
         */
        private static void doubleClickElement(WebElement element, int waitSeconds) {
            Waits.waitForElementToBeClickable(element, waitSeconds);
            SeleniumActions.getActions().moveToElement(element).doubleClick().build().perform();
        }

        /**
         * OBJECTIVE: Long Wait for an element to be Clickable to do the double click.
         */
        public static void doubleClickElementLongWait(WebElement element) {
            doubleClickElement(element, GENERIC_LONG_TIME_OUT_SECONDS);
        }

    }

    public static class Debugs {
    	
    	 private Debugs() {
 		    throw new IllegalStateException(UTILITY_CLASS);
 		  }

        /**
         * <p>
         * Method to put Element into the view
         * </p>
         */
        public static void scrollToElement(WebElement element) {
            new Actions(DesktopDriverSession.getDesktopDriverSession()).moveToElement(element).pause(Duration.ofSeconds(2)).build().perform(); // Pause to allow the scroll to
                                                                                                                                               // complete
            Reporting.logReporter(Status.DEBUG, "DESKTOP APP - scrollToElement done");
        }

    }

    /**
     * Utilities to handle desktop Elements - see https://github.com/2gis/Winium.Elements/tree/master/java
     */
    public static class DesktopElementUtils {

    	 private DesktopElementUtils() {
 		    throw new IllegalStateException(UTILITY_CLASS);
 		  }
    	
        /**
         * OBJECTIVE: Convert the Desktop-WebElement in a ComboBox make the interaction simpler
         */
        public static ComboBox getComboBoxElement(WebElement element) {
            return WebElementExtensions.toComboBox(element);
        }

        /**
         * OBJECTIVE: Convert the Desktop-WebElement in a DataGrid make the interaction simpler
         */
        public static DataGrid getDataGridElement(WebElement element) {
            return WebElementExtensions.toDataGrid(element);
        }

        /**
         * OBJECTIVE: Convert the Desktop-WebElement in a ListBox make the interaction simpler
         */
        public static ListBox getListBoxElement(WebElement element) {
            return WebElementExtensions.toListBox(element);
        }

        /**
         * OBJECTIVE: Convert the Desktop-WebElement in a Menu make the interaction simpler
         */
        public static Menu getMenuElement(WebElement element) {
            return WebElementExtensions.toMenu(element);
        }

    }

    /**
     * Utilities to Find Elements (By the usual dynamic elements that cannot be mapped from the very beginning - see https://github.com/2gis/Winium.Desktop/wiki/Finding-Elements
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
            WebDriverWait wait = new WebDriverWait(DesktopDriverSession.getDesktopDriverSession(), GENERIC_LONG_TIME_OUT_SECONDS);
            return wait.until(expectedCondition);
        }

        /**
         * Method to be used when the elements are created on runtime, and not mapped as page objects
         */
        public static List<WebElement> findElements(By selector) {
            return findElements(ExpectedConditions.presenceOfAllElementsLocatedBy(selector));
        }

        private static List<WebElement> findElements(ExpectedCondition<List<WebElement>> expectedCondition) {
            List<WebElement> elements = new ArrayList<>();
            try {
                Wait<WebDriver> wait = new WebDriverWait(DesktopDriverSession.getDesktopDriverSession(), GENERIC_LONG_TIME_OUT_SECONDS);
                elements = wait.until(expectedCondition);
            } catch (TimeoutException e) {
            	Logging.logReporter(Status.DEBUG, EXCEPTION_MSG + e);
                // Ignore the exception as find elements can return an empty list.
            }
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

        /**
         * OBJECTIVE: Get the element on the page that currently has focus.
         */
        public static WebElement switchToActiveElement() {
            return DesktopDriverSession.getDesktopDriverSession().switchTo().activeElement();
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
            JavascriptExecutor js = getJavaScriptExecutor();
            js.executeScript(script);
            Reporting.logReporter(Status.DEBUG, "DESKTOP APP - JS Execution done");
        }

        /**
         * OBJECTIVE: Execute JS, with WebElement.
         */
        public static Object executeJavaScript(String script, WebElement element) {
            return ((JavascriptExecutor) DesktopDriverSession.getDesktopDriverSession()).executeScript(script, element);
        }

        /**
         * OBJECTIVE: Execute JS, with WebElement and value.
         */
        public static void executeJavaScript(String script, WebElement element, String valueToSet) {
            DesktopDriverSession.getDesktopDriverSession().executeScript(script, element, valueToSet);
        }

        public static JavascriptExecutor getJavaScriptExecutor() {
            return (DesktopDriverSession.getDesktopDriverSession());
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
            return new Actions(DesktopDriverSession.getDesktopDriverSession());
        }

        /**
         * OBJECTIVE: Method to ClickElements Using Actions, after each action a wait(Custom Time) is done.
         */
        private static void hoverAndClickTwoElementsWithCustomtWait(int pauseMilliSeconds, WebElement element1, WebElement element2) {
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

            Reporting.logReporter(Status.DEBUG, "DESKTOP APP - hoverAndClickTwoElementsWithCustomtWait done");
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
            Waits.waitForElementToBeClickable(element);
            element.clear();
            element.sendKeys(keys);
            Reporting.logReporter(Status.DEBUG, "DESKTOP APP - clearFieldAndSendKeys done");
        }

        /**
         * OBJECTIVE: Waits for element to be visible/enable / Then clear the field / then sent the text
         */
        public static void clearFieldAndSendKeys(WebElement element, String text) {
            Waits.waitForElementToBeClickable(element);
            element.clear();
            element.sendKeys(text);
            Reporting.logReporter(Status.DEBUG, "DESKTOP APP - clearFieldAndSendKeys done");
        }

        /**
         * OBJECTIVE: Waits for element to be visible/enable / then sent the text. Note this method do not clean the field before
         */
        public static void sendKey(WebElement element, Keys keys) {
            Waits.waitForElementToBeClickable(element);
            element.sendKeys(keys);
            Reporting.logReporter(Status.DEBUG, "DESKTOP APP - sendKey done");
        }

        /**
         * OBJECTIVE: Waits for element to be visible/enable / then sent the text. Note this method do not clean the field before
         */
        public static void sendKey(WebElement element, String text) {
            Waits.waitForElementToBeClickable(element);
            element.sendKeys(text);
            Reporting.logReporter(Status.DEBUG, "DESKTOP APP - sendKey done");
        }

    }

    /**
     * Utilities to do waits
     */
    public static class Waits {
    	
    	private Waits() {
 		    throw new IllegalStateException(UTILITY_CLASS);
 		  }

        //private static final String MESSAGE_DOCUMENT_READY = "return document.readyState"
        //private static final String MESSAGE_COMPLETE = "complete"

        /**
         * OBJECTIVE: Setup the implicit wait for the driver. Warning will affect all the driver waits.
         */
        public static void setImplicitWait(int waitInSseconds) {
            DesktopDriverSession.getDesktopDriverSession().manage().timeouts().implicitlyWait(waitInSseconds, TimeUnit.SECONDS);
            Reporting.logReporter(Status.DEBUG, "DESKTOP APP - setImplicitWait done");
        }

        /**
         * OBJECTIVE: Short Wait for an element to be invisible (The element Exist but, is not visible) e.g. A Hidden Menu.
         */
        public static void waitForElementInvisibility(WebElement element) {
            waitForElementInvisibility(element, GENERIC_SHORT_TIME_OUT_SECONDS);
        }

        public static void waitForElementInvisibility(WebElement element, int seconds) {
            try {
                element.isDisplayed();
                Reporting.logReporter(Status.DEBUG, "element.isDisplayed(" + element.isDisplayed());
            
            } catch (NoSuchElementException e) {
                // Expected error if element is not even present
                Reporting.logReporter(Status.DEBUG, "DESKTOP APP - waitForElementInvisibility done");
                Logging.logReporter(Status.DEBUG, EXCEPTION_MSG + e);
                return;
            }

            try {
                WebDriverWait wait = new WebDriverWait(DesktopDriverSession.getDesktopDriverSession(), seconds);
                wait.until(ExpectedConditions.not(ExpectedConditions.visibilityOf(element)));
            } catch (NoSuchElementException e) {
                // Expected error if element is not even present
                Reporting.logReporter(Status.DEBUG, "DESKTOP APP - waitForElementInvisibility done");
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

        public static void waitForElementText(WebElement element, String strExpectedText, int seconds) {
            WebDriverWait wait = new WebDriverWait(DesktopDriverSession.getDesktopDriverSession(), seconds);
            wait.until(ExpectedConditions.textToBePresentInElement(element, strExpectedText));
            Reporting.logReporter(Status.DEBUG, "DESKTOP APP - waitForElementText done");
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

        public static void waitForElementToBeClickable(WebElement element, int seconds) {
            WebDriverWait wait = new WebDriverWait(DesktopDriverSession.getDesktopDriverSession(), seconds);
            wait.until(ExpectedConditions.elementToBeClickable(element));
            Reporting.logReporter(Status.DEBUG, "DESKTOP APP - waitForElementToBeClickable done");
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
            WebDriverWait wait = new WebDriverWait(DesktopDriverSession.getDesktopDriverSession(), seconds);
            wait.until(ExpectedConditions.visibilityOf(element));
            Reporting.logReporter(Status.DEBUG, "DESKTOP APP - waitForElementVisibility done");
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
            WebDriverWait wait = new WebDriverWait(DesktopDriverSession.getDesktopDriverSession(), seconds);
            wait.until(ExpectedConditions.numberOfWindowsToBe(expectedNumberOfTabs));
            Reporting.logReporter(Status.DEBUG, "DESKTOP APP - waitForTabsToOpen done");
        }

        public static void waitForTabsToOpenLongWait(int expectedNumberOfTabs) {
            waitForTabsToOpen(expectedNumberOfTabs, GENERIC_LONG_TIME_OUT_SECONDS);
        }

        /**
         * OBJECTIVE: Generic wait to wait for an specific period of time in milliseconds. Avoid the use of the same, instead use custom expect waits.
         */
        public static void waitGeneric(int milliseconds) {
            try {
                Thread.sleep(milliseconds);
                Reporting.logReporter(Status.DEBUG, "DESKTOP APP - waitGeneric done");
            } catch (InterruptedException e) {
            	Logging.logReporter(Status.DEBUG, EXCEPTION_MSG + e);
                Reporting.logReporter(Status.DEBUG, "DESKTOP APP - waitGeneric error");
                Thread.currentThread().interrupt();
            }

        }

        /**
         * OBJECTIVE: Method to do a Wait(Custom) until the Conditions is true
         */
        @SuppressWarnings("deprecation")
		static void waitUntil(Function<WebDriver, Boolean> waitCondition, Long timeoutInSeconds) {
            WebDriver driver = DesktopDriverSession.getDesktopDriverSession();

            WebDriverWait webDriverWait = new WebDriverWait(driver, timeoutInSeconds);
            webDriverWait.withTimeout(timeoutInSeconds, TimeUnit.SECONDS);
            try {
                webDriverWait.until(waitCondition);
            } catch (RuntimeException e) {
            	Logging.logReporter(Status.DEBUG, EXCEPTION_MSG + e);
                Reporting.logReporter(Status.DEBUG, e.getMessage());
            }
        }

        /**
         * OBJECTIVE: Short wait - Method to create An expectation for checking that the First element from a list is present.
         */
        public static void waitUntilListFirstElementIsPresent(List<WebElement> list) {
            waitUntilListFirstElementIsPresent(list, GENERIC_SHORT_TIME_OUT_SECONDS);
        }

        public static void waitUntilListFirstElementIsPresent(List<WebElement> list, int timeOutInSeconds) {

            for (int i = 1; i <= timeOutInSeconds; i++) {
                try {
                    waitGeneric(ONE_SECOND);
                    list.get(0);
                    Reporting.logReporter(Status.DEBUG, "DESKTOP APP - waitUntilListFirstElementIsPresent done");
                    return; // To Exit after success Found
                } catch (IndexOutOfBoundsException e) {
                    // Do nothing Expected Failure
                	Logging.logReporter(Status.DEBUG, EXCEPTION_MSG + e);
                }
            }

            throw new UnsupportedOperationException("DESKTOP APP - Element not present after try for " + timeOutInSeconds + " seconds");
        }

        /**
         * OBJECTIVE: Method to create An expectation for checking that the First element from a list is present.
         */
        public static void waitUntilListFirstElementIsPresentLongWait(List<WebElement> list) {
            waitUntilListFirstElementIsPresent(list, GENERIC_LONG_TIME_OUT_SECONDS);
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

    }

    //private static final String CONSTRUCTOR_MESSAGE = "Utility class - Not Designed to be Instantiated"

    private static final int ONE_SECOND = 1000;

    /**
     * Global Variables, that are set by the SystemProperties file
     */
    private static final int GENERIC_LONG_TIME_OUT_SECONDS = SystemProperties.DESKTOP_LONG_WAIT_TIMEOUT_MILLIS / ONE_SECOND;

    private static final int GENERIC_SHORT_TIME_OUT_SECONDS = SystemProperties.DESKTOP_SHORT_WAIT_TIMEOUT_MILLIS / ONE_SECOND;

    //private static final int GENERIC_PAGE_TIME_OUT_SECONDS = SystemProperties.DESKTOP_GENERIC_PAGE_WAIT_TIMEOUT_MILLIS / ONE_SECOND

}
