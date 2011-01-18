package com.sandwich.koans;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Koan {
	public static final String DEFAULT = "TODO: Add a description of what the koan is intended to teach the pupil";
	String value() default DEFAULT;
}
