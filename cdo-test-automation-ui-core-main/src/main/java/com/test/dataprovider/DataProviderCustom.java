package com.test.dataprovider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that helps to map "Custom" features that help the data provider logic More fields can be added in future
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface DataProviderCustom {

    /**
     * The excel sheetName to be used
     */
    public String sheetName() default "";

}
