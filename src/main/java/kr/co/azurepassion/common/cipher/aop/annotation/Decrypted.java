package kr.co.azurepassion.common.cipher.aop.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Decrypted {
}
