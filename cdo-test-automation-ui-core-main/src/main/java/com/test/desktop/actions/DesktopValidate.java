package com.test.desktop.actions;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.winium.WiniumDriver;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.test.logging.Logging;
import com.test.reporting.Reporting;
import com.test.screenshots.Screenshots;
import com.test.utils.SystemProperties;

/**
 * <p>
 * 1)Any method that STARTS with Assert* e.g. AssertTrue() --> On failure will END the execution.
 * </p>
 *
 * <p>
 * 2)Any method that Does NOT STARTS with Assert* e.g. isElementDisplayed() --> On Negative response will NOT Fail the test execution, But it will only LOG in the extent-report a
 * FAIL and will take the screenshot, on POSITIVE Response it will only LOG in the extent-report a PASS and will take the screenshot, All these methods are meant to be used in
 * conjuction of a softassertion, so at the end the softAssertion will recieve the response + the info in the report and the screenshot of each soft assertion, and finally will end
 * the test if any Failure was found.
 * </p>
 *
 * <p>
 * 3)Any method that Does NOT STARTS with Assert* and that Ends with "WithoutAssertions" e.g. isElementDisplayedWithoutAssertions() --> On Negative response will NOT Fail the test
 * execution,hit will only retrieve expected output (most of the cases a boolean). These methods are think to be used as LOGIC helpers, to query for a status and based on the
 * results do an action.
 * </p>
 *
 ****************************************************************************
 * HIGHLIGHTS: > Base methods to perform checkpoints on the test steps. > These return a True or false to the steps where they are called to perform validation. > ALL Static
 * methods that can be called without instantiate the class. - example in steps use: Validate.elementDisplayed(WebElement) > Use the "soft assert" in the steps to perform multiple
 * validations and not stop execution until the asserALL is run. - Example, in steps use below to check all tabs displayed in a page.
 * softAssert.assertTrue(Validate.elementDisplayed(searchPage.getTodosTab())); softAssert.assertTrue(Validate.elementDisplayed(searchPage.getNoticiasTab()));
 * softAssert.assertAll();
 ****************************************************************************
 */

public class DesktopValidate extends Reporting {

    //private static final int ONE_SECOND = 1000

    // Global Variables, that are set by the SystemProperties file
    //private static final int GENERIC_LONG_TIME_OUT_SECONDS = SystemProperties.LONG_WAIT_TIMEOUT_MILLIS / ONE_SECOND
    // private static final int GENERIC_SHORT_TIME_OUT_SECONDS =


    // Data to create Generic Messages for the logger
    private static final String GENERIC_PASSED_MESSAGE_TEMPLATE = "Assertion Passed: %s %s";
    private static final String GENERIC_FAILED_MESSAGE_TEMPLATE = "Assertion Failed: %s %s";
    private static final String GENERIC_NO_MESSAGE_TEMPLATE = " - Without description message";

    private static final String GENERIC_ASSERT_TRUE_MESSAGE = "AssertTrue";
    private static final String GENERIC_ASSERT_FALSE_MESSAGE = "AssertTrue";
    private static final String GENERIC_ASSERT_EQUALS_MESSAGE = "AssertEquals";
    private static final String GENERIC_ASSERT_EQUALS_NO_ORDER_MESSAGE = "AssertEqualsNoOrder";
    private static final String GENERIC_ASSERT_NOT_NULL_MESSAGE = "AssertNotNull";
    private static final String GENERIC_ASSERT_NULL_MESSAGE = "AssertNull";
    private static final String GENERIC_ASSERT_SAME_MESSAGE = "AssertSame";
    private static final String GENERIC_ASSERT_NOT_SAME_MESSAGE = "AssertNotSame";
    private static final String GENERIC_ASSERT_NOT_EQUALS_MESSAGE = "AssertNotEquals";
    private static final String GENERIC_ASSERT_CONTAINS_MESSAGE = "AssertContains";
    private static final String GENERIC_ASSERT_NOT_CONTAINS_MESSAGE = "AssertNotContains";
	private static final String EXCEPTION_MSG = "Exception Caught : ";

    /**
     * Asserts that String Contains a partial String. If it isn't, an UnsupportedOperationException, with the given message, is thrown.
     *
     * @param condition
     *            the condition to evaluate
     * @param message
     *            the assertion error message
     */
    public static void assertContains(String actual, String expected) {
        assertContains(actual, expected, null);
    }

    /**
     * Asserts that String Contains a partial String. If it isn't, an UnsupportedOperationException, with the given message, is thrown.
     *
     * @param condition
     *            the condition to evaluate
     * @param message
     *            the assertion error message
     */
    public static void assertContains(String actual, String expected, String message) {
        boolean result = actual.contains(expected);

        if (result) {
            Reporting.logReporter(Status.PASS, String.format(GENERIC_PASSED_MESSAGE_TEMPLATE, GENERIC_ASSERT_CONTAINS_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
        } else {
            if (message == null) {
                message = String.format(GENERIC_FAILED_MESSAGE_TEMPLATE, GENERIC_ASSERT_CONTAINS_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE);
            }
            sendFail(message);
            throw new UnsupportedOperationException(message);
        }
    }

    /**
     * Asserts that two booleans are equal. If they are not, an AssertionError, with the given message, is thrown.
     *
     * @param actual
     *            the actual value
     * @param expected
     *            the expected value
     * @param message
     *            the assertion error message
     */
    public static void assertEquals(boolean actual, boolean expected, String message) {
        try {
            org.testng.Assert.assertEquals(actual, expected, message);
            Reporting.logReporter(Status.PASS, String.format(GENERIC_PASSED_MESSAGE_TEMPLATE, GENERIC_ASSERT_EQUALS_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            sendPass(null); // No need to sent message again
        } catch (AssertionError e) {
            if (message == null) {
                message = String.format(GENERIC_FAILED_MESSAGE_TEMPLATE, GENERIC_ASSERT_EQUALS_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE);
            }
            sendFail(message);
            throw new AssertionError(e);
        }
    }

    /**
     * Asserts that two bytes are equal. If they are not, an AssertionError is thrown.
     *
     * @param actual
     *            the actual value
     * @param expected
     *            the expected value
     */
    public static void assertEquals(byte actual, byte expected) {
        try {
            org.testng.Assert.assertEquals(actual, expected);
            Reporting.logReporter(Status.PASS, String.format(GENERIC_PASSED_MESSAGE_TEMPLATE, GENERIC_ASSERT_EQUALS_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            sendPass(null); // No need to sent message again
        } catch (AssertionError e) {
            sendFail(String.format(GENERIC_FAILED_MESSAGE_TEMPLATE, GENERIC_ASSERT_FALSE_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            throw new AssertionError(e);
        }
    }

    /**
     * Asserts that two bytes are equal. If they are not, an AssertionError, with the given message, is thrown.
     *
     * @param actual
     *            the actual value
     * @param expected
     *            the expected value
     * @param message
     *            the assertion error message
     */
    public static void assertEquals(byte actual, byte expected, String message) {
        try {
            org.testng.Assert.assertEquals(actual, expected, message);
            Reporting.logReporter(Status.PASS, String.format(GENERIC_PASSED_MESSAGE_TEMPLATE, GENERIC_ASSERT_EQUALS_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            sendPass(null); // No need to sent message again
        } catch (AssertionError e) {
            if (message == null) {
                message = String.format(GENERIC_FAILED_MESSAGE_TEMPLATE, GENERIC_ASSERT_EQUALS_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE);
            }
            sendFail(message);
            throw new AssertionError(e);
        }
    }

    /**
     * Asserts that two arrays contain the same elements in the same order. If they do not, an AssertionError is thrown.
     *
     * @param actual
     *            the actual value
     * @param expected
     *            the expected value
     */
    public static void assertEquals(byte[] actual, byte[] expected) {
        try {
            org.testng.Assert.assertEquals(actual, expected);
            Reporting.logReporter(Status.PASS, String.format(GENERIC_PASSED_MESSAGE_TEMPLATE, GENERIC_ASSERT_EQUALS_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            sendPass(null); // No need to sent message again
        } catch (AssertionError e) {
            sendFail(String.format(GENERIC_FAILED_MESSAGE_TEMPLATE, GENERIC_ASSERT_EQUALS_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            throw new AssertionError(e);
        }

    }

    /**
     * Asserts that two arrays contain the same elements in the same order. If they do not, an AssertionError, with the given message, is thrown.
     *
     * @param actual
     *            the actual value
     * @param expected
     *            the expected value
     * @param message
     *            the assertion error message
     */
    public static void assertEquals(byte[] actual, byte[] expected, String message) {
        try {
            org.testng.Assert.assertEquals(actual, expected, message);
            Reporting.logReporter(Status.PASS, String.format(GENERIC_PASSED_MESSAGE_TEMPLATE, GENERIC_ASSERT_EQUALS_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            sendPass(null); // No need to sent message again
        } catch (AssertionError e) {
            if (message == null) {
                message = String.format(GENERIC_FAILED_MESSAGE_TEMPLATE, GENERIC_ASSERT_EQUALS_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE);
            }
            sendFail(message);
            throw new AssertionError(e);
        }
    }

    /**
     * Asserts that two chars are equal. If they are not, an AssertionError is thrown.
     *
     * @param actual
     *            the actual value
     * @param expected
     *            the expected value
     */
    public static void assertEquals(char actual, char expected) {
        try {
            org.testng.Assert.assertEquals(actual, expected);
            Reporting.logReporter(Status.PASS, String.format(GENERIC_PASSED_MESSAGE_TEMPLATE, GENERIC_ASSERT_EQUALS_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            sendPass(null); // No need to sent message again
        } catch (AssertionError e) {
            sendFail(String.format(GENERIC_FAILED_MESSAGE_TEMPLATE, GENERIC_ASSERT_FALSE_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            throw new AssertionError(e);
        }
    }

    /**
     * Asserts that two chars are equal. If they are not, an AssertionFailedError, with the given message, is thrown.
     *
     * @param actual
     *            the actual value
     * @param expected
     *            the expected value
     * @param message
     *            the assertion error message
     */
    public static void assertEquals(char actual, char expected, String message) {
        try {
            org.testng.Assert.assertEquals(actual, expected, message);
            Reporting.logReporter(Status.PASS, String.format(GENERIC_PASSED_MESSAGE_TEMPLATE, GENERIC_ASSERT_EQUALS_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            sendPass(null); // No need to sent message again
        } catch (AssertionError e) {
            if (message == null) {
                message = String.format(GENERIC_FAILED_MESSAGE_TEMPLATE, GENERIC_ASSERT_EQUALS_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE);
            }
            sendFail(message);
            throw new AssertionError(e);
        }
    }

    /**
     * Asserts that two collections contain the same elements in the same order. If they do not, an AssertionError is thrown.
     *
     * @param actual
     *            the actual value
     * @param expected
     *            the expected value
     */
    public static void assertEquals(Collection<?> actual, Collection<?> expected) {
        try {
            org.testng.Assert.assertEquals(actual, expected);
            Reporting.logReporter(Status.PASS, String.format(GENERIC_PASSED_MESSAGE_TEMPLATE, GENERIC_ASSERT_EQUALS_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            sendPass(null); // No need to sent message again
        } catch (AssertionError e) {
            sendFail(String.format(GENERIC_FAILED_MESSAGE_TEMPLATE, GENERIC_ASSERT_FALSE_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            throw new AssertionError(e);
        }
    }

    /**
     * Asserts that two collections contain the same elements in the same order. If they do not, an AssertionError, with the given message, is thrown.
     *
     * @param actual
     *            the actual value
     * @param expected
     *            the expected value
     * @param message
     *            the assertion error message
     */
    public static void assertEquals(Collection<?> actual, Collection<?> expected, String message) {
        try {
            org.testng.Assert.assertEquals(actual, expected, message);
            Reporting.logReporter(Status.PASS, String.format(GENERIC_PASSED_MESSAGE_TEMPLATE, GENERIC_ASSERT_EQUALS_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            sendPass(null); // No need to sent message again
        } catch (AssertionError e) {
            if (message == null) {
                message = String.format(GENERIC_FAILED_MESSAGE_TEMPLATE, GENERIC_ASSERT_EQUALS_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE);
            }
            sendFail(message);
            throw new AssertionError(e);
        }
    }

    /**
     * Asserts that two doubles are equal concerning a delta. If they are not, an AssertionError is thrown. If the expected value is infinity then the delta value is ignored.
     *
     * @param actual
     *            the actual value
     * @param expected
     *            the expected value
     * @param delta
     *            the absolute tolerable difference between the actual and expected values
     */
    public static void assertEquals(double actual, double expected, double delta) {
        try {
            org.testng.Assert.assertEquals(actual, expected, delta);
            Reporting.logReporter(Status.PASS, String.format(GENERIC_PASSED_MESSAGE_TEMPLATE, GENERIC_ASSERT_EQUALS_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            sendPass(null); // No need to sent message again
        } catch (AssertionError e) {
            sendFail(String.format(GENERIC_FAILED_MESSAGE_TEMPLATE, GENERIC_ASSERT_FALSE_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            throw new AssertionError(e);
        }
    }

    /**
     * Asserts that two doubles are equal concerning a delta. If they are not, an AssertionError, with the given message, is thrown. If the expected value is infinity then the
     * delta value is ignored.
     *
     * @param actual
     *            the actual value
     * @param expected
     *            the expected value
     * @param delta
     *            the absolute tolerable difference between the actual and expected values
     * @param message
     *            the assertion error message
     */
    public static void assertEquals(double actual, double expected, double delta, String message) {
        try {
            org.testng.Assert.assertEquals(actual, expected, delta, message);
            Reporting.logReporter(Status.PASS, String.format(GENERIC_PASSED_MESSAGE_TEMPLATE, GENERIC_ASSERT_EQUALS_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            sendPass(null); // No need to sent message again
        } catch (AssertionError e) {
            if (message == null) {
                message = String.format(GENERIC_FAILED_MESSAGE_TEMPLATE, GENERIC_ASSERT_EQUALS_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE);
            }
            sendFail(message);
            throw new AssertionError(e);
        }
    }

    /**
     * Asserts that two floats are equal concerning a delta. If they are not, an AssertionError is thrown. If the expected value is infinity then the delta value is ignored.
     *
     * @param actual
     *            the actual value
     * @param expected
     *            the expected value
     * @param delta
     *            the absolute tolerable difference between the actual and expected values
     */
    public static void assertEquals(float actual, float expected, float delta) {
        try {
            org.testng.Assert.assertEquals(actual, expected, delta);
            Reporting.logReporter(Status.PASS, String.format(GENERIC_PASSED_MESSAGE_TEMPLATE, GENERIC_ASSERT_EQUALS_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            sendPass(null); // No need to sent message again
        } catch (AssertionError e) {
            sendFail(String.format(GENERIC_FAILED_MESSAGE_TEMPLATE, GENERIC_ASSERT_FALSE_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            throw new AssertionError(e);
        }
    }

    /**
     * Asserts that two floats are equal concerning a delta. If they are not, an AssertionError, with the given message, is thrown. If the expected value is infinity then the delta
     * value is ignored.
     *
     * @param actual
     *            the actual value
     * @param expected
     *            the expected value
     * @param delta
     *            the absolute tolerable difference between the actual and expected values
     * @param message
     *            the assertion error message
     */
    public static void assertEquals(float actual, float expected, float delta, String message) {
        try {
            org.testng.Assert.assertEquals(actual, expected, delta, message);
            Reporting.logReporter(Status.PASS, String.format(GENERIC_PASSED_MESSAGE_TEMPLATE, GENERIC_ASSERT_EQUALS_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            sendPass(null); // No need to sent message again
        } catch (AssertionError e) {
            if (message == null) {
                message = String.format(GENERIC_FAILED_MESSAGE_TEMPLATE, GENERIC_ASSERT_EQUALS_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE);
            }
            sendFail(message);
            throw new AssertionError(e);
        }
    }

    /**
     * Asserts that two ints are equal. If they are not, an AssertionError is thrown.
     *
     * @param actual
     *            the actual value
     * @param expected
     *            the expected value
     */
    public static void assertEquals(int actual, int expected) {
        try {
            org.testng.Assert.assertEquals(actual, expected);
            Reporting.logReporter(Status.PASS, String.format(GENERIC_PASSED_MESSAGE_TEMPLATE, GENERIC_ASSERT_EQUALS_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            sendPass(null); // No need to sent message again
        } catch (AssertionError e) {
            sendFail(String.format(GENERIC_FAILED_MESSAGE_TEMPLATE, GENERIC_ASSERT_FALSE_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            throw new AssertionError(e);
        }
    }

    /**
     * Asserts that two ints are equal. If they are not, an AssertionFailedError, with the given message, is thrown.
     *
     * @param actual
     *            the actual value
     * @param expected
     *            the expected value
     * @param message
     *            the assertion error message
     */
    public static void assertEquals(int actual, int expected, String message) {
        try {
            org.testng.Assert.assertEquals(actual, expected, message);
            Reporting.logReporter(Status.PASS, String.format(GENERIC_PASSED_MESSAGE_TEMPLATE, GENERIC_ASSERT_EQUALS_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            sendPass(null); // No need to sent message again
        } catch (AssertionError e) {
            if (message == null) {
                message = String.format(GENERIC_FAILED_MESSAGE_TEMPLATE, GENERIC_ASSERT_EQUALS_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE);
            }
            sendFail(message);
            throw new AssertionError(e);
        }
    }

    /**
     * Asserts that two iterables return iterators with the same elements in the same order. If they do not, an AssertionError is thrown.
     *
     * @param actual
     *            the actual value
     * @param expected
     *            the expected value
     */
    public static void assertEquals(Iterable<?> actual, Iterable<?> expected) {
        try {
            org.testng.Assert.assertEquals(actual, expected);
            Reporting.logReporter(Status.PASS, String.format(GENERIC_PASSED_MESSAGE_TEMPLATE, GENERIC_ASSERT_EQUALS_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            sendPass(null); // No need to sent message again
        } catch (AssertionError e) {
            sendFail(String.format(GENERIC_FAILED_MESSAGE_TEMPLATE, GENERIC_ASSERT_FALSE_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            throw new AssertionError(e);
        }
    }

    /**
     * Asserts that two iterables return iterators with the same elements in the same order. If they do not, an AssertionError, with the given message, is thrown.
     *
     * @param actual
     *            the actual value
     * @param expected
     *            the expected value
     * @param message
     *            the assertion error message
     */
    public static void assertEquals(Iterable<?> actual, Iterable<?> expected, String message) {
        try {
            org.testng.Assert.assertEquals(actual, expected, message);
            Reporting.logReporter(Status.PASS, String.format(GENERIC_PASSED_MESSAGE_TEMPLATE, GENERIC_ASSERT_EQUALS_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            sendPass(null); // No need to sent message again
        } catch (AssertionError e) {
            if (message == null) {
                message = String.format(GENERIC_FAILED_MESSAGE_TEMPLATE, GENERIC_ASSERT_EQUALS_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE);
            }
            sendFail(message);
            throw new AssertionError(e);
        }
    }

    /**
     * Asserts that two iterators return the same elements in the same order. If they do not, an AssertionError is thrown. Please note that this assert iterates over the elements
     * and modifies the state of the iterators.
     *
     * @param actual
     *            the actual value
     * @param expected
     *            the expected value
     */
    public static void assertEquals(Iterator<?> actual, Iterator<?> expected) {
        try {
            org.testng.Assert.assertEquals(actual, expected);
            Reporting.logReporter(Status.PASS, String.format(GENERIC_PASSED_MESSAGE_TEMPLATE, GENERIC_ASSERT_EQUALS_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            sendPass(null); // No need to sent message again
        } catch (AssertionError e) {
            sendFail(String.format(GENERIC_FAILED_MESSAGE_TEMPLATE, GENERIC_ASSERT_FALSE_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            throw new AssertionError(e);
        }
    }

    /**
     * Asserts that two iterators return the same elements in the same order. If they do not, an AssertionError, with the given message, is thrown. Please note that this assert
     * iterates over the elements and modifies the state of the iterators.
     *
     * @param actual
     *            the actual value
     * @param expected
     *            the expected value
     * @param message
     *            the assertion error message
     */
    public static void assertEquals(Iterator<?> actual, Iterator<?> expected, String message) {
        try {
            org.testng.Assert.assertEquals(actual, expected, message);
            Reporting.logReporter(Status.PASS, String.format(GENERIC_PASSED_MESSAGE_TEMPLATE, GENERIC_ASSERT_EQUALS_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            sendPass(null); // No need to sent message again
        } catch (AssertionError e) {
            if (message == null) {
                message = String.format(GENERIC_FAILED_MESSAGE_TEMPLATE, GENERIC_ASSERT_EQUALS_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE);
            }
            sendFail(message);
            throw new AssertionError(e);
        }
    }

    /**
     * Asserts that two longs are equal. If they are not, an AssertionError is thrown.
     *
     * @param actual
     *            the actual value
     * @param expected
     *            the expected value
     */
    public static void assertEquals(long actual, long expected) {
        try {
            org.testng.Assert.assertEquals(actual, expected);
            Reporting.logReporter(Status.PASS, String.format(GENERIC_PASSED_MESSAGE_TEMPLATE, GENERIC_ASSERT_EQUALS_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            sendPass(null); // No need to sent message again
        } catch (AssertionError e) {
            sendFail(String.format(GENERIC_FAILED_MESSAGE_TEMPLATE, GENERIC_ASSERT_FALSE_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            throw new AssertionError(e);
        }
    }

    /**
     * Asserts that two longs are equal. If they are not, an AssertionError, with the given message, is thrown.
     *
     * @param actual
     *            the actual value
     * @param expected
     *            the expected value
     * @param message
     *            the assertion error message
     */
    public static void assertEquals(long actual, long expected, String message) {
        try {
            org.testng.Assert.assertEquals(actual, expected, message);
            Reporting.logReporter(Status.PASS, String.format(GENERIC_PASSED_MESSAGE_TEMPLATE, GENERIC_ASSERT_EQUALS_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            sendPass(null); // No need to sent message again
        } catch (AssertionError e) {
            if (message == null) {
                message = String.format(GENERIC_FAILED_MESSAGE_TEMPLATE, GENERIC_ASSERT_EQUALS_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE);
            }
            sendFail(message);
            throw new AssertionError(e);
        }
    }

    /**
     * Asserts that two maps are equal.
     */
    public static void assertEquals(Map<?, ?> actual, Map<?, ?> expected) {
        try {
            org.testng.Assert.assertEquals(actual, expected);
            Reporting.logReporter(Status.PASS, String.format(GENERIC_PASSED_MESSAGE_TEMPLATE, GENERIC_ASSERT_EQUALS_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            sendPass(null); // No need to sent message again
        } catch (AssertionError e) {
            sendFail(String.format(GENERIC_FAILED_MESSAGE_TEMPLATE, GENERIC_ASSERT_FALSE_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            throw new AssertionError(e);
        }
    }

    /**
     * Asserts that two objects are equal. If they are not, an AssertionError is thrown.
     *
     * @param actual
     *            the actual value
     * @param expected
     *            the expected value
     */
    public static void assertEquals(Object actual, Object expected) {
        try {
            org.testng.Assert.assertEquals(actual, expected);
            Reporting.logReporter(Status.PASS, String.format(GENERIC_PASSED_MESSAGE_TEMPLATE, GENERIC_ASSERT_EQUALS_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            sendPass(null); // No need to sent message again
        } catch (AssertionError e) {
            sendFail(String.format(GENERIC_FAILED_MESSAGE_TEMPLATE, GENERIC_ASSERT_FALSE_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            throw new AssertionError(e);
        }
    }

    /**
     * Asserts that two objects are equal. If they are not, an AssertionError, with the given message, is thrown.
     *
     * @param actual
     *            the actual value
     * @param expected
     *            the expected value
     * @param message
     *            the assertion error message
     */
    public static void assertEquals(Object actual, Object expected, String message) {
        try {
            org.testng.Assert.assertEquals(actual, expected, message);
            Reporting.logReporter(Status.PASS, String.format(GENERIC_PASSED_MESSAGE_TEMPLATE, GENERIC_ASSERT_EQUALS_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            sendPass(null); // No need to sent message again
        } catch (AssertionError e) {
            if (message == null) {
                message = String.format(GENERIC_FAILED_MESSAGE_TEMPLATE, GENERIC_ASSERT_EQUALS_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE);
            }
            sendFail(message);
            throw new AssertionError(e);
        }
    }

    /**
     * Asserts that two arrays contain the same elements in the same order. If they do not, an AssertionError is thrown.
     *
     * @param actual
     *            the actual value
     * @param expected
     *            the expected value
     */
    public static void assertEquals(Object[] actual, Object[] expected) {
        try {
            org.testng.Assert.assertEquals(actual, expected);
            Reporting.logReporter(Status.PASS, String.format(GENERIC_PASSED_MESSAGE_TEMPLATE, GENERIC_ASSERT_EQUALS_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            sendPass(null); // No need to sent message again
        } catch (AssertionError e) {
            sendFail(String.format(GENERIC_FAILED_MESSAGE_TEMPLATE, GENERIC_ASSERT_FALSE_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            throw new AssertionError(e);
        }
    }

    /**
     * Asserts that two arrays contain the same elements in the same order. If they do not, an AssertionError, with the given message, is thrown.
     *
     * @param actual
     *            the actual value
     * @param expected
     *            the expected value
     * @param message
     *            the assertion error message
     */
    public static void assertEquals(Object[] actual, Object[] expected, String message) {
        try {
            org.testng.Assert.assertEquals(actual, expected, message);
            Reporting.logReporter(Status.PASS, String.format(GENERIC_PASSED_MESSAGE_TEMPLATE, GENERIC_ASSERT_EQUALS_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            sendPass(null); // No need to sent message again
        } catch (AssertionError e) {
            if (message == null) {
                message = String.format(GENERIC_FAILED_MESSAGE_TEMPLATE, GENERIC_ASSERT_EQUALS_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE);
            }
            sendFail(message);
            throw new AssertionError(e);
        }
    }

    /**
     * Asserts that two sets are equal.
     */
    public static void assertEquals(Set<?> actual, Set<?> expected) {
        try {
            org.testng.Assert.assertEquals(actual, expected);
            Reporting.logReporter(Status.PASS, String.format(GENERIC_PASSED_MESSAGE_TEMPLATE, GENERIC_ASSERT_EQUALS_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            sendPass(null); // No need to sent message again
        } catch (AssertionError e) {
            sendFail(String.format(GENERIC_FAILED_MESSAGE_TEMPLATE, GENERIC_ASSERT_FALSE_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            throw new AssertionError(e);
        }
    }

    /**
     * Assert set equals
     */
    public static void assertEquals(Set<?> actual, Set<?> expected, String message) {
        try {
            org.testng.Assert.assertEquals(actual, expected, message);
            Reporting.logReporter(Status.PASS, String.format(GENERIC_PASSED_MESSAGE_TEMPLATE, GENERIC_ASSERT_EQUALS_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            sendPass(null); // No need to sent message again
        } catch (AssertionError e) {
            if (message == null) {
                message = String.format(GENERIC_FAILED_MESSAGE_TEMPLATE, GENERIC_ASSERT_EQUALS_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE);
            }
            sendFail(message);
            throw new AssertionError(e);
        }
    }

    /**
     * Asserts that two shorts are equal. If they are not, an AssertionError is thrown.
     *
     * @param actual
     *            the actual value
     * @param expected
     *            the expected value
     */
    public static void assertEquals(short actual, short expected) {
        try {
            org.testng.Assert.assertEquals(actual, expected);
            Reporting.logReporter(Status.PASS, String.format(GENERIC_PASSED_MESSAGE_TEMPLATE, GENERIC_ASSERT_EQUALS_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            sendPass(null); // No need to sent message again
        } catch (AssertionError e) {
            sendFail(String.format(GENERIC_FAILED_MESSAGE_TEMPLATE, GENERIC_ASSERT_FALSE_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            throw new AssertionError(e);
        }
    }

    /**
     * Asserts that two shorts are equal. If they are not, an AssertionFailedError, with the given message, is thrown.
     *
     * @param actual
     *            the actual value
     * @param expected
     *            the expected value
     * @param message
     *            the assertion error message
     */
    public static void assertEquals(short actual, short expected, String message) {
        try {
            org.testng.Assert.assertEquals(actual, expected, message);
            Reporting.logReporter(Status.PASS, String.format(GENERIC_PASSED_MESSAGE_TEMPLATE, GENERIC_ASSERT_EQUALS_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            sendPass(null); // No need to sent message again
        } catch (AssertionError e) {
            if (message == null) {
                message = String.format(GENERIC_FAILED_MESSAGE_TEMPLATE, GENERIC_ASSERT_EQUALS_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE);
            }
            sendFail(message);
            throw new AssertionError(e);
        }
    }

    /**
     * Asserts that two Strings are equal. If they are not, an AssertionError is thrown.
     *
     * @param actual
     *            the actual value
     * @param expected
     *            the expected value
     */
    public static void assertEquals(String actual, String expected) {
        try {
            org.testng.Assert.assertEquals(actual, expected);
            Reporting.logReporter(Status.PASS, String.format(GENERIC_PASSED_MESSAGE_TEMPLATE, GENERIC_ASSERT_EQUALS_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            sendPass(null); // No need to sent message again
        } catch (AssertionError e) {
            sendFail(String.format(GENERIC_FAILED_MESSAGE_TEMPLATE, GENERIC_ASSERT_FALSE_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            throw new AssertionError(e);
        }
    }

    /**
     * Asserts that two Strings are equal. If they are not, an AssertionError, with the given message, is thrown.
     *
     * @param actual
     *            the actual value
     * @param expected
     *            the expected value
     * @param message
     *            the assertion error message
     */
    public static void assertEquals(String actual, String expected, String message) {
        try {
            org.testng.Assert.assertEquals(actual, expected, message);
            Reporting.logReporter(Status.PASS, String.format(GENERIC_PASSED_MESSAGE_TEMPLATE, GENERIC_ASSERT_EQUALS_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            sendPass(null); // No need to sent message again
        } catch (AssertionError e) {
            if (message == null) {
                message = String.format(GENERIC_FAILED_MESSAGE_TEMPLATE, GENERIC_ASSERT_EQUALS_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE);
            }
            sendFail(message);
            throw new AssertionError(e);
        }
    }

    /**
     * Asserts that two arrays contain the same elements in no particular order. If they do not, an AssertionError is thrown.
     *
     * @param actual
     *            the actual value
     * @param expected
     *            the expected value
     */
    public static void assertEqualsNoOrder(Object[] actual, Object[] expected) {
        try {
            org.testng.Assert.assertEqualsNoOrder(actual, expected);
            Reporting.logReporter(Status.PASS, String.format(GENERIC_PASSED_MESSAGE_TEMPLATE, GENERIC_ASSERT_EQUALS_NO_ORDER_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            sendPass(null); // No need to sent message again
        } catch (AssertionError e) {
            sendFail(String.format(GENERIC_FAILED_MESSAGE_TEMPLATE, GENERIC_ASSERT_EQUALS_NO_ORDER_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            throw new AssertionError(e);
        }
    }

    /**
     * Asserts that two arrays contain the same elements in no particular order. If they do not, an AssertionError, with the given message, is thrown.
     *
     * @param actual
     *            the actual value
     * @param expected
     *            the expected value
     * @param message
     *            the assertion error message
     */
    public static void assertEqualsNoOrder(Object[] actual, Object[] expected, String message) {
        try {
            org.testng.Assert.assertEqualsNoOrder(actual, expected, message);
            Reporting.logReporter(Status.PASS, String.format(GENERIC_PASSED_MESSAGE_TEMPLATE, GENERIC_ASSERT_EQUALS_NO_ORDER_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            sendPass(null); // No need to sent message again
        } catch (AssertionError e) {
            if (message == null) {
                message = String.format(GENERIC_FAILED_MESSAGE_TEMPLATE, GENERIC_ASSERT_EQUALS_NO_ORDER_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE);
            }
            sendFail(message);
            throw new AssertionError(e);
        }
    }

    /**
     * Asserts that a condition is false. If it isn't, an AssertionError is thrown.
     *
     * @param condition
     *            the condition to evaluate
     */
    public static void assertFalse(boolean condition) {
        try {
            org.testng.Assert.assertFalse(condition);
            Reporting.logReporter(Status.PASS, String.format(GENERIC_PASSED_MESSAGE_TEMPLATE, GENERIC_ASSERT_FALSE_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            sendPass(null); // No need to sent message again
        } catch (AssertionError e) {
            sendFail(String.format(GENERIC_FAILED_MESSAGE_TEMPLATE, GENERIC_ASSERT_FALSE_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            throw new AssertionError(e);
        }
    }

    /**
     * Asserts that a condition is false. If it isn't, an AssertionError, with the given message, is thrown.
     *
     * @param condition
     *            the condition to evaluate
     * @param message
     *            the assertion error message
     */
    public static void assertFalse(boolean condition, String message) {
        try {
            org.testng.Assert.assertFalse(condition, message);
            Reporting.logReporter(Status.PASS, String.format(GENERIC_PASSED_MESSAGE_TEMPLATE, GENERIC_ASSERT_FALSE_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            sendPass(null); // No need to sent message again
        } catch (AssertionError e) {
            if (message == null) {
                message = String.format(GENERIC_FAILED_MESSAGE_TEMPLATE, GENERIC_ASSERT_FALSE_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE);
            }
            sendFail(message);
            throw new AssertionError(e);
        }
    }

    /**
     * Asserts that String Not Contains a partial String. If it isn't, an UnsupportedOperationException, with the given message, is thrown.
     *
     * @param condition
     *            the condition to evaluate
     * @param message
     *            the assertion error message
     */
    public static void assertNotContains(String actual, String expected) {
        assertNotContains(actual, expected, null);
    }

    /**
     * Asserts that String Not Contains a partial String. If it isn't, an UnsupportedOperationException, with the given message, is thrown.
     *
     * @param condition
     *            the condition to evaluate
     * @param message
     *            the assertion error message
     */
    public static void assertNotContains(String actual, String expected, String message) {
        boolean result = actual.contains(expected);

        if (!result) {
            Reporting.logReporter(Status.PASS, String.format(GENERIC_PASSED_MESSAGE_TEMPLATE, GENERIC_ASSERT_NOT_CONTAINS_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
        } else {
            if (message == null) {
                message = String.format(GENERIC_FAILED_MESSAGE_TEMPLATE, GENERIC_ASSERT_NOT_CONTAINS_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE);
            }
            sendFail(message);
            throw new UnsupportedOperationException(message);
        }
    }

    public static void assertNotEquals(double actual1, double actual2, double delta) {
        try {
            org.testng.Assert.assertNotEquals(actual1, actual2, delta);
            Reporting.logReporter(Status.PASS, String.format(GENERIC_PASSED_MESSAGE_TEMPLATE, GENERIC_ASSERT_NOT_EQUALS_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            sendPass(null); // No need to sent message again
        } catch (AssertionError e) {
            sendFail(String.format(GENERIC_FAILED_MESSAGE_TEMPLATE, GENERIC_ASSERT_NOT_EQUALS_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            throw new AssertionError(e);
        }
    }

    public static void assertNotEquals(double actual1, double actual2, double delta, String message) {
        try {
            org.testng.Assert.assertNotEquals(actual1, actual2, delta, message);
            Reporting.logReporter(Status.PASS, String.format(GENERIC_PASSED_MESSAGE_TEMPLATE, GENERIC_ASSERT_NOT_EQUALS_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            sendPass(null); // No need to sent message again
        } catch (AssertionError e) {
            if (message == null) {
                message = String.format(GENERIC_FAILED_MESSAGE_TEMPLATE, GENERIC_ASSERT_NOT_EQUALS_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE);
            }
            sendFail(message);
            throw new AssertionError(e);
        }
    }

    public static void assertNotEquals(float actual1, float actual2, float delta) {
        try {
            org.testng.Assert.assertNotEquals(actual1, actual2, delta);
            Reporting.logReporter(Status.PASS, String.format(GENERIC_PASSED_MESSAGE_TEMPLATE, GENERIC_ASSERT_NOT_EQUALS_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            sendPass(null); // No need to sent message again
        } catch (AssertionError e) {
            sendFail(String.format(GENERIC_FAILED_MESSAGE_TEMPLATE, GENERIC_ASSERT_NOT_EQUALS_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            throw new AssertionError(e);
        }
    }

    public static void assertNotEquals(float actual1, float actual2, float delta, String message) {
        try {
            org.testng.Assert.assertNotEquals(actual1, actual2, delta, message);
            Reporting.logReporter(Status.PASS, String.format(GENERIC_PASSED_MESSAGE_TEMPLATE, GENERIC_ASSERT_NOT_EQUALS_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            sendPass(null); // No need to sent message again
        } catch (AssertionError e) {
            if (message == null) {
                message = String.format(GENERIC_FAILED_MESSAGE_TEMPLATE, GENERIC_ASSERT_NOT_EQUALS_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE);
            }
            sendFail(message);
            throw new AssertionError(e);
        }
    }

    public static void assertNotEquals(Object actual1, Object actual2) {
        try {
            org.testng.Assert.assertNotEquals(actual1, actual2);
            Reporting.logReporter(Status.PASS, String.format(GENERIC_PASSED_MESSAGE_TEMPLATE, GENERIC_ASSERT_NOT_EQUALS_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            sendPass(null); // No need to sent message again
        } catch (AssertionError e) {
            sendFail(String.format(GENERIC_FAILED_MESSAGE_TEMPLATE, GENERIC_ASSERT_NOT_EQUALS_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            throw new AssertionError(e);
        }
    }

    /////
    // assertNotEquals
    //
    public static void assertNotEquals(Object actual1, Object actual2, String message) {
        try {
            org.testng.Assert.assertNotEquals(actual1, actual2, message);
            Reporting.logReporter(Status.PASS, String.format(GENERIC_PASSED_MESSAGE_TEMPLATE, GENERIC_ASSERT_NOT_EQUALS_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            sendPass(null); // No need to sent message again
        } catch (AssertionError e) {
            if (message == null) {
                message = String.format(GENERIC_FAILED_MESSAGE_TEMPLATE, GENERIC_ASSERT_NOT_EQUALS_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE);
            }
            sendFail(message);
            throw new AssertionError(e);
        }
    }

    /**
     * Asserts that an object isn't null. If it is, an AssertionError is thrown.
     *
     * @param object
     *            the assertion object
     */
    public static void assertNotNull(Object object) {
        try {
            org.testng.Assert.assertNotNull(object);
            Reporting.logReporter(Status.PASS, String.format(GENERIC_PASSED_MESSAGE_TEMPLATE, GENERIC_ASSERT_NOT_NULL_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            sendPass(null); // No need to sent message again
        } catch (AssertionError e) {
            sendFail(String.format(GENERIC_FAILED_MESSAGE_TEMPLATE, GENERIC_ASSERT_NOT_NULL_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            throw new AssertionError(e);
        }
    }

    /**
     * Asserts that an object isn't null. If it is, an AssertionFailedError, with the given message, is thrown.
     *
     * @param object
     *            the assertion object
     * @param message
     *            the assertion error message
     */
    public static void assertNotNull(Object object, String message) {
        try {
            org.testng.Assert.assertNotNull(object, message);
            Reporting.logReporter(Status.PASS, String.format(GENERIC_PASSED_MESSAGE_TEMPLATE, GENERIC_ASSERT_NOT_NULL_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            sendPass(null); // No need to sent message again
        } catch (AssertionError e) {
            if (message == null) {
                message = String.format(GENERIC_FAILED_MESSAGE_TEMPLATE, GENERIC_ASSERT_NOT_NULL_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE);
            }
            sendFail(message);
            throw new AssertionError(e);
        }
    }

    /**
     * Asserts that two objects do not refer to the same object. If they do, an AssertionError is thrown.
     *
     * @param actual
     *            the actual value
     * @param expected
     *            the expected value
     */
    public static void assertNotSame(Object actual, Object expected) {
        try {
            org.testng.Assert.assertNotSame(actual, expected);
            Reporting.logReporter(Status.PASS, String.format(GENERIC_PASSED_MESSAGE_TEMPLATE, GENERIC_ASSERT_NOT_SAME_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            sendPass(null); // No need to sent message again
        } catch (AssertionError e) {
            sendFail(String.format(GENERIC_FAILED_MESSAGE_TEMPLATE, GENERIC_ASSERT_NOT_SAME_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            throw new AssertionError(e);
        }
    }

    /**
     * Asserts that two objects do not refer to the same objects. If they do, an AssertionError, with the given message, is thrown.
     *
     * @param actual
     *            the actual value
     * @param expected
     *            the expected value
     * @param message
     *            the assertion error message
     */
    public static void assertNotSame(Object actual, Object expected, String message) {
        try {
            org.testng.Assert.assertNotSame(actual, expected, message);
            Reporting.logReporter(Status.PASS, String.format(GENERIC_PASSED_MESSAGE_TEMPLATE, GENERIC_ASSERT_NOT_SAME_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            sendPass(null); // No need to sent message again
        } catch (AssertionError e) {
            if (message == null) {
                message = String.format(GENERIC_FAILED_MESSAGE_TEMPLATE, GENERIC_ASSERT_NOT_SAME_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE);
            }
            sendFail(message);
            throw new AssertionError(e);
        }
    }

    /**
     * Asserts that an object is null. If it is not, an AssertionError, with the given message, is thrown.
     *
     * @param object
     *            the assertion object
     */
    public static void assertNull(Object object) {
        try {
            org.testng.Assert.assertNull(object);
            Reporting.logReporter(Status.PASS, String.format(GENERIC_PASSED_MESSAGE_TEMPLATE, GENERIC_ASSERT_NULL_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            sendPass(null); // No need to sent message again
        } catch (AssertionError e) {
            sendFail(String.format(GENERIC_FAILED_MESSAGE_TEMPLATE, GENERIC_ASSERT_NULL_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            throw new AssertionError(e);
        }
    }

    /**
     * Asserts that an object is null. If it is not, an AssertionFailedError, with the given message, is thrown.
     *
     * @param object
     *            the assertion object
     * @param message
     *            the assertion error message
     */
    public static void assertNull(Object object, String message) {
        try {
            org.testng.Assert.assertNull(object, message);
            Reporting.logReporter(Status.PASS, String.format(GENERIC_PASSED_MESSAGE_TEMPLATE, GENERIC_ASSERT_NULL_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            sendPass(null); // No need to sent message again
        } catch (AssertionError e) {
            if (message == null) {
                message = String.format(GENERIC_FAILED_MESSAGE_TEMPLATE, GENERIC_ASSERT_NULL_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE);
            }
            sendFail(message);
            throw new AssertionError(e);
        }
    }

    /**
     * Asserts that two objects refer to the same object. If they do not, an AssertionError is thrown.
     *
     * @param actual
     *            the actual value
     * @param expected
     *            the expected value
     */
    public static void assertSame(Object actual, Object expected) {
        try {
            org.testng.Assert.assertSame(actual, expected);
            Reporting.logReporter(Status.PASS, String.format(GENERIC_PASSED_MESSAGE_TEMPLATE, GENERIC_ASSERT_SAME_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            sendPass(null); // No need to sent message again
        } catch (AssertionError e) {
            sendFail(String.format(GENERIC_FAILED_MESSAGE_TEMPLATE, GENERIC_ASSERT_SAME_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            throw new AssertionError(e);
        }
    }

    /**
     * Asserts that two objects refer to the same object. If they do not, an AssertionFailedError, with the given message, is thrown.
     *
     * @param actual
     *            the actual value
     * @param expected
     *            the expected value
     * @param message
     *            the assertion error message
     */
    public static void assertSame(Object actual, Object expected, String message) {
        try {
            org.testng.Assert.assertSame(actual, expected, message);
            Reporting.logReporter(Status.PASS, String.format(GENERIC_PASSED_MESSAGE_TEMPLATE, GENERIC_ASSERT_SAME_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            sendPass(null); // No need to sent message again
        } catch (AssertionError e) {
            if (message == null) {
                message = String.format(GENERIC_FAILED_MESSAGE_TEMPLATE, GENERIC_ASSERT_SAME_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE);
            }
            sendFail(message);
            throw new AssertionError(e);
        }
    }

    /**
     * Asserts that a condition is true. If it isn't, an AssertionError is thrown.
     *
     * @param condition
     *            the condition to evaluate
     */
    public static void assertTrue(boolean condition) {
        try {
            org.testng.Assert.assertTrue(condition);
            Reporting.logReporter(Status.PASS, String.format(GENERIC_PASSED_MESSAGE_TEMPLATE, GENERIC_ASSERT_TRUE_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            sendPass(null); // No need to sent message again
        } catch (AssertionError e) {
            sendFail(String.format(GENERIC_FAILED_MESSAGE_TEMPLATE, GENERIC_ASSERT_TRUE_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            throw new AssertionError(e);
        }
    }

    /**
     * Asserts that a condition is true. If it isn't, an AssertionError, with the given message, is thrown.
     *
     * @param condition
     *            the condition to evaluate
     * @param message
     *            the assertion error message
     */
    public static void assertTrue(boolean condition, String message) {
        try {
            org.testng.Assert.assertTrue(condition, message);
            Reporting.logReporter(Status.PASS, String.format(GENERIC_PASSED_MESSAGE_TEMPLATE, GENERIC_ASSERT_TRUE_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE));
            sendPass(null); // No need to sent message again
        } catch (AssertionError e) {
            if (message == null) {
                message = String.format(GENERIC_FAILED_MESSAGE_TEMPLATE, GENERIC_ASSERT_TRUE_MESSAGE, GENERIC_NO_MESSAGE_TEMPLATE);
            }
            sendFail(message);
            throw new AssertionError(e);
        }
    }

    /**
     * <p>
     * For SOFTASSERTIONS USEFULL - Will LOG Status (PASS/FAIL) to extent-report and take screnshot. But will NOT FAIL The TEST, see the 2nd comment in the class description for
     * more information
     * </p>
     * OBJECTIVE: return if a element is displayed with a delay timeout.
     */
    public static boolean isDelayedElementDisplayed(WebElement myElement) {
        return isDelayedElementDisplayed(myElement, true);
    }

    private static boolean isDelayedElementDisplayed(WebElement myElement, boolean doAssertions) {
        try {
            DesktopBaseSteps.Waits.waitForElementVisibility(myElement);

            if (doAssertions) {
                Reporting.logReporter(Status.PASS, String.format(GENERIC_PASSED_MESSAGE_TEMPLATE, "isDelayedElementDisplayed", GENERIC_NO_MESSAGE_TEMPLATE));
                sendPass(null); // No need to sent message again
            }

            return true;

        } catch (RuntimeException e) {
        	Logging.logReporter(Status.DEBUG, EXCEPTION_MSG + e);
            if (doAssertions) {
                String message = String.format(GENERIC_FAILED_MESSAGE_TEMPLATE, "isDelayedElementDisplayed", GENERIC_NO_MESSAGE_TEMPLATE);
                sendFail(message);
            }
            return false;
        }
    }

    /**
     * <p>
     * For SIMPLE QUERY Return, (Will NOT Log any PASS/FAIL - Screenshot ) See the 3rd comment in the class description for more information
     * </p>
     * OBJECTIVE: return if a element is displayed with a delay timeout.
     */
    public static boolean isDelayedElementDisplayedWithoutAssertions(WebElement myElement) {
        return isDelayedElementDisplayed(myElement, false);
    }

    /**
     * <p>
     * For SOFTASSERTIONS USEFULL - Will LOG Status (PASS/FAIL) to extent-report and take screnshot. But will NOT FAIL The TEST, see the 2nd comment in the class description for
     * more information
     * </p>
     * OBJECTIVE: Check whether an element is displayed.
     */
    public static boolean isElementDisplayed(WebElement myElement) {
        return isElementDisplayed(myElement, true);
    }

    private static boolean isElementDisplayed(WebElement myElement, boolean doAssertions) {

        try {
            boolean status = myElement.isDisplayed();

            if (status && doAssertions) {
                Reporting.logReporter(Status.PASS, "PASSED-element displayed: " + myElement.getText());
                sendPass(null); // No need to sent message again
            }

            else if (!status && doAssertions) {
                sendFail("FAILED-element NOT displayed: ");
            }
            return status;
        } catch (org.openqa.selenium.NoSuchElementException e) {
        	Logging.logReporter(Status.DEBUG, EXCEPTION_MSG + e);
            StringWriter outError = new StringWriter();
            e.printStackTrace(new PrintWriter(outError));
            if (doAssertions) {
                String errorString = outError.toString();
                sendFail("FAILED-element NOT displayed: " + errorString);
            }
            return false;
        }
    }

    /**
     * <p>
     * For SIMPLE QUERY Return, (Will NOT Log any PASS/FAIL - Screenshot ) See the 3rd comment in the class description for more information
     * </p>
     * OBJECTIVE: Check whether an element is displayed.
     */
    public static boolean isElementDisplayedWithoutAssertions(WebElement myElement) {
        return isElementDisplayed(myElement, false);
    }

    /**
     * <p>
     * For SOFTASSERTIONS USEFULL - Will LOG Status (PASS/FAIL) to extent-report and take screnshot. But will NOT FAIL The TEST, see the 2nd comment in the class description for
     * more information
     * </p>
     * OBJECTIVE: Check whether an element is NOT displayed.
     */
    public static boolean isElementNotDisplayed(WebElement myElement) {
        return isElementNotDisplayed(myElement, true);
    }

    private static boolean isElementNotDisplayed(WebElement myElement, boolean doAssertions) {

        try {
            boolean status = myElement.isDisplayed();

            if (status && doAssertions) {
                sendFail("FAILED-element displayed - causing errors");
                return false;

            } else if (!status && doAssertions) {
                Reporting.logReporter(Status.PASS, "PASSED-element NOT displayed");
                sendPass(null); // No need to sent message again
                return true;
            }

            return status == true ? false : true;

        } catch (org.openqa.selenium.NoSuchElementException e) {
        	Logging.logReporter(Status.DEBUG, EXCEPTION_MSG + e);
            if (doAssertions) {
                Reporting.logReporter(Status.PASS, "PASSED-element NOT displayed");
                sendPass(null); // No need to sent message again
            }
            return true;
        }
    }

    /**
     * <p>
     * For SIMPLE QUERY Return, (Will NOT Log any PASS/FAIL - Screenshot ) See the 3rd comment in the class description for more information
     * </p>
     * OBJECTIVE: Check whether an element is NOT displayed.
     */
    public static boolean isElementNotDisplayedWhitoutAssertions(WebElement myElement) {
        return isElementNotDisplayed(myElement, false);
    }

    /**
     * <p>
     * For SOFTASSERTIONS USEFULL - Will LOG Status (PASS/FAIL) to extent-report and take screnshot. But will NOT FAIL The TEST, see the 2nd comment in the class description for
     * more information
     * </p>
     * OBJECTIVE: Check whether an element is present.
     */
    public static boolean isElementPresent(WebElement myElement) {
        return isElementPresent(myElement, true);
    }

    private static boolean isElementPresent(WebElement myElement, boolean doAssertions) {

        try {
            boolean status = false;

            int location = myElement.getLocation().getX();

            status = true;

            if (location != 0 && doAssertions) {
                Reporting.logReporter(Status.PASS, "PASSED-element Present (And Visible): " + myElement.getText());
                sendPass(null); // No need to sent message again

            } else if (location == 0 && doAssertions) {
                Reporting.logReporter(Status.PASS, "PASSED-element Present (Not visible): " + myElement.getText());
                sendPass(null); // No need to sent message again
            }

            return status;

        } catch (org.openqa.selenium.NoSuchElementException e) {
        	Logging.logReporter(Status.DEBUG, EXCEPTION_MSG + e);
            StringWriter outError = new StringWriter();
            e.printStackTrace(new PrintWriter(outError));
            if (doAssertions) {
                String errorString = outError.toString();
                sendFail("FAILED-element NOT Present: " + errorString);
            }
            return false;
        }
    }

    /**
     * <p>
     * For SIMPLE QUERY Return, (Will NOT Log any PASS/FAIL - Screenshot ) See the 3rd comment in the class description for more information
     * </p>
     * OBJECTIVE: Check whether an element is present.
     */
    public static boolean isElementPresentWithoutAssertions(WebElement myElement) {
        return isElementPresent(myElement, false);
    }

    /**
     * <p>
     * For SOFTASSERTIONS USEFULL - Will LOG Status (PASS/FAIL) to extent-report and take screnshot. But will NOT FAIL The TEST, see the 2nd comment in the class description for
     * more information
     * </p>
     * OBJECTIVE: Check two strings and see if the expected is IN the actual string displayed
     */
    public static boolean isInString(String strActual, String strExpected) {
        return isInString(strActual, strExpected, true);
    }

    private static boolean isInString(String strActual, String strExpected, boolean doAssertions) {

        boolean result = strActual.contains(strExpected);

        if (result && doAssertions) {
            Reporting.logReporter(Status.PASS, "PASSED-strings match actual/expected: " + strActual + " / " + strExpected);
            sendPass(null); // No need to sent message again
            return true;
        }

        if (!result && doAssertions) {
            sendFail("FAILED-strings mismatch actual/expected: " + strActual + " / " + strExpected);
            return false;
        }

        return result;
    }

    /**
     * <p>
     * For SIMPLE QUERY Return, (Will NOT Log any PASS/FAIL - Screenshot ) See the 3rd comment in the class description for more information
     * </p>
     * OBJECTIVE: Check two strings and see if the expected is IN the actual string displayed
     */
    public static boolean isInStringWithoutAssertions(String strActual, String strExpected) {
        return isInString(strActual, strExpected, false);
    }

    /**
     * <p>
     * Helper Util that is used in the Hard And Soft assertions to take an screenshot with the FAIL status
     * </p>
     * OBJECTIVE: If there is an Exception while doing an assertion, send the message fail to the log reporter and add a screenshot for extent reports.
     */
    public static String sendFail(String message) {
        String screenshotPath = null;
        String fullFilePath = Screenshots.getFailedScreenShotFullPath();
        String relativeFilePath = Screenshots.getScreenShotRelativePath(fullFilePath);

        WiniumDriver driver = DesktopDriverSession.getDesktopDriverSession();

        String screenshot = null;
        screenshot = Screenshots.takeScreenshot(fullFilePath, driver);

        if (screenshot != null) {
            try {
                logReporter(Status.FAIL, message, MediaEntityBuilder.createScreenCaptureFromPath(relativeFilePath).build());
            } catch (IOException e) {
                Reporting.logReporter(Status.FAIL, message);
                Logging.logReporter(Status.DEBUG, EXCEPTION_MSG + e);
            }
        } else {
            Reporting.logReporter(Status.FAIL, message);
        }

        return screenshotPath;
    }

    /**
     * <p>
     * Helper Util that is used in the Hard And Soft assertions to take an screenshot with the PASS status
     * </p>
     * OBJECTIVE: Send the message Pass to the log reporter and add a screenshot for extent reports.
     */
    public static String sendPass(String message) {
        String screenshotPath = null;

        if (SystemProperties.SCREENSHOTS_TAKE_ALL) {
            String fullFilePath = Screenshots.getPassedScreenShotFullPath();
            String relativeFilePath = Screenshots.getScreenShotRelativePath(fullFilePath);

            WiniumDriver driver = DesktopDriverSession.getDesktopDriverSession();

            if (message == null) { // Not sent anything, change to empty string
                message = "";
            }

            String screenshot = null;
            screenshot = Screenshots.takeScreenshot(fullFilePath, driver);
            screenshotPath = screenshot;

            if (screenshot != null) {
                try {
                    logReporter(Status.PASS, message, MediaEntityBuilder.createScreenCaptureFromPath(relativeFilePath).build());
                } catch (IOException e) {
                    Reporting.logReporter(Status.PASS, message);
                    Logging.logReporter(Status.DEBUG, EXCEPTION_MSG + e);
                }
            } else {
                Reporting.logReporter(Status.PASS, message);
            }
        } else {
            Reporting.logReporter(Status.INFO, "Warning - Skipping Take Screenshot as Toggle it's false");
        }
        return screenshotPath;
    }

    /**
     * OBJECTIVE: Send the message Pass to the log reporter and add a screenshot for extent reports.
     */
    public static String takeStepScreenShot(String message) {
        // TO BE IMPLEMENTED In case of more customization needed then copy paste the code of the sendPass method and customize the same here (without touching the original)
        return sendPass("takeStepScreenShot: " + message);
    }
}
