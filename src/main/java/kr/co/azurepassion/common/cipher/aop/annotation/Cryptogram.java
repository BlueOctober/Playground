package kr.co.azurepassion.common.cipher.aop.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cryptogram {
	String DEFAULT = "default";
	String value() default(DEFAULT);
}
