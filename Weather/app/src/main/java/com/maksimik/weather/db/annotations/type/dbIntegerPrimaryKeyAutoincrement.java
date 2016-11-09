package com.maksimik.weather.db.annotations.type;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface dbIntegerPrimaryKeyAutoincrement {

    String value() default "INTEGER PRIMARY KEY AUTOINCREMENT";
}
