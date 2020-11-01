package kr.co.azurepassion.common.cipher.aop;

import kr.co.azurepassion.common.cipher.aop.advice.MethodEncryptInterceptor;
import kr.co.azurepassion.common.cipher.aop.annotation.Encrypted;
import kr.co.azurepassion.common.cipher.Crypto;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.AbstractAdvisingBeanPostProcessor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.factory.InitializingBean;

import java.util.Map;

public class EncryptedAnnotationBeanPostProcessor extends AbstractAdvisingBeanPostProcessor 
		implements InitializingBean {
	
	private static final long serialVersionUID = -1631583752463626338L;

	private Class<? extends Encrypted> encryptedAnnotationType = Encrypted.class;
	
	// 암호화 오류 발생 시 Exception 처리할 것인가?
	private boolean wantToGetException = true;
	
	private Crypto defaultCrypto;
	
	private Map<String, Crypto> cryptoMap;
	
	public void setDefaultCrypto(Crypto defaultCrypto) {
		this.defaultCrypto = defaultCrypto;
	}
	
	public void setCryptoMap(Map<String, Crypto> cryptoMap) {
		this.cryptoMap = cryptoMap;
	}
	
	public void setWantToGetException(boolean wantToGetException) {
		this.wantToGetException = wantToGetException;
	}
	
	public void afterPropertiesSet() {
		Pointcut pointcut = new AnnotationMatchingPointcut(encryptedAnnotationType, false);
		Advice advice = new MethodEncryptInterceptor(defaultCrypto, cryptoMap, wantToGetException);
		this.advisor = new DefaultPointcutAdvisor(pointcut, advice);
	}
}
