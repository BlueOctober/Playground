package kr.co.azurepassion.common.cipher.aop.annotation;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EncryptProperty {
	String value() default(Cryptogram.DEFAULT);
}
