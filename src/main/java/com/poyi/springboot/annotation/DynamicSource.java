package com.poyi.springboot.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface DynamicSource {

    String value();

    boolean clear() default true;

}
