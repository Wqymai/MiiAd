package com.mg.others.v4;

/**
 * Created by wuqiyan on 16/11/16.
 */
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Denotes that a parameter, field or method return value can never be null.
 * <p>
 * This is a marker annotation and it has no specific attributes.
 */
@Documented
@Retention(CLASS)
@Target({METHOD, PARAMETER, FIELD})
public @interface NonNull {
}