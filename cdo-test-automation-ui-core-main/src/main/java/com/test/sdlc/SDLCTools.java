package com.test.sdlc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to map the SDLC Tools related IDs, and report back the results for the execution. Any Tool can be mapped in it, with the proper KEY/VALUE.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface SDLCTools {

    /**
     * Map ALMS Ids (Test/Bugs etc)
     */
    public String almId() default "";

    /**
     * Map JIRA Ids (Test/Bugs etc)
     */
    public String jiraId() default "";

    /**
     * Map qTest Ids (Test/Bugs etc)
     */
    public String qTestId() default "";
}
