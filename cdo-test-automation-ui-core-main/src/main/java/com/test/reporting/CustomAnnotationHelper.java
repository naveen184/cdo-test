package com.test.reporting;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.testng.IInvokedMethod;
import org.testng.annotations.Test;
import org.testng.internal.ConstructorOrMethod;

import com.aventstack.extentreports.Status;
import com.test.dataprovider.DataProviderCustom;
import com.test.sdlc.SDLCTools;

/**
 * Class that helps to identify the values inside the annotations
 */
public class CustomAnnotationHelper {
	
	private CustomAnnotationHelper() {
		    throw new IllegalStateException("Utility class");
		  }

    /**
     * Look for the annotations, in the Class or Method and return the same.
     */
    public static List<String> getAnnotationsList(IInvokedMethod iInvokedMethod, boolean isAnnotationInClass) {
        ConstructorOrMethod consOrMethod = iInvokedMethod.getTestMethod().getConstructorOrMethod();
        Annotation[] atTestAnnotations = null;

        if (isAnnotationInClass) {
            atTestAnnotations = consOrMethod.getMethod().getDeclaringClass().getAnnotations();
        } else {
            atTestAnnotations = consOrMethod.getMethod().getAnnotations();
        }

        List<String> list = new ArrayList<>();
        for (Annotation a : atTestAnnotations) {
            list.add(a.toString());
        }

        return list;

    }

    /**
     * Look for the custom DataProviderCustom annotation, in the Class or Method and return the same.
     */
    public static DataProviderCustom getDataProviderCustomAnnotation(Method method) {
        DataProviderCustom custom = null;
        custom = method.getAnnotation(DataProviderCustom.class);

        if (custom != null) {
            Reporting.logReporter(Status.DEBUG, "AnnotationHelper - DataProviderCustom Annotation Located");
            return custom;
        } else {
            return null;
        }

    }

    /**
     * Look for the custom SDLCTool annotation, in the Class or Method and return the same.
     */
    public static SDLCTools getSDLCToolsAnnotation(IInvokedMethod iInvokedMethod, boolean isAnnotationInClass) {
        ConstructorOrMethod consOrMethod = iInvokedMethod.getTestMethod().getConstructorOrMethod();
        SDLCTools sdlcTools = null;

        if (isAnnotationInClass) {
            sdlcTools = consOrMethod.getMethod().getDeclaringClass().getAnnotation(SDLCTools.class);
        } else {
            sdlcTools = consOrMethod.getMethod().getAnnotation(SDLCTools.class);
        }

        if (sdlcTools != null) {
            Reporting.logReporter(Status.DEBUG, "AnnotationHelper - SDLCTools Annotation Located");
            return sdlcTools;
        } else {
            return null;
        }

    }

    /**
     * Look for the @Test (testNg) annotation, in the Class or Method and return the same.
     */
    public static boolean isAtTestAnnotationPresent(IInvokedMethod iInvokedMethod, boolean isAnnotationInClass) {
        ConstructorOrMethod consOrMethod = iInvokedMethod.getTestMethod().getConstructorOrMethod();
        Test atTest = null;

        if (isAnnotationInClass) {
            atTest = consOrMethod.getMethod().getDeclaringClass().getAnnotation(Test.class);
        } else {
            atTest = consOrMethod.getMethod().getAnnotation(Test.class);
        }

        if (atTest != null) {
            Reporting.logReporter(Status.DEBUG, "AnnotationHelper - @Test Annotation Located");
            return true;
        } else {
            Reporting.logReporter(Status.DEBUG, "AnnotationHelper - @Test Annotation NOT Located");
            return false;
        }

    }
}
