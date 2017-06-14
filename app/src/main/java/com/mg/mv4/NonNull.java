package com.mg.mv4;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Created by wuqiyan on 17/3/24.
 */

@Retention(CLASS)
@Target({METHOD, PARAMETER, FIELD})
public @interface NonNull {
}
