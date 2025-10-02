package com.chat.animal.api.annotation;

import io.swagger.v3.oas.annotations.Hidden;

import java.lang.annotation.*;

@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Hidden
public @interface JwtTokenParsing {
    String cookieName() default "jwt";
    boolean required() default true;
    boolean stripBearerPrefix() default true;
}
