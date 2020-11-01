package kr.co.azurepassion.common.cipher.aop.advice;

import kr.co.azurepassion.common.cipher.aop.annotation.Cryptogram;
import kr.co.azurepassion.common.cipher.aop.annotation.Encrypt;
import kr.co.azurepassion.common.cipher.aop.annotation.EncryptDomain;
import kr.co.azurepassion.common.cipher.aop.annotation.EncryptProperty;
import kr.co.azurepassion.common.cipher.Crypto;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class MethodEncryptInterceptor implements MethodInterceptor {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	// 암호화 오류 발생 시 Exception 처리할 것인가?
	private boolean wantToGetException = true;

	private Class<? extends Encrypt> encryptAnnotationType = Encrypt.class;
	
	private Map<String, Crypto> cryptoMap = new ConcurrentHashMap<String, Crypto>();
	
	public MethodEncryptInterceptor() {
	}
	
	public MethodEncryptInterceptor(Crypto defaultCrypto) {
		commonConstructor(defaultCrypto, null, true);
	}
	
	public MethodEncryptInterceptor(Crypto defaultCrypto, boolean wantToGetException) {
		commonConstructor(defaultCrypto, null, wantToGetException);
	}
	
	public MethodEncryptInterceptor(Crypto defaultCrypto, Map<String, Crypto> cryptoMap) {
		commonConstructor(defaultCrypto, cryptoMap, true);
	}
	
	public MethodEncryptInterceptor(Crypto defaultCrypto, Map<String, Crypto> cryptoMap, boolean wantToGetException) {
		commonConstructor(defaultCrypto, cryptoMap, wantToGetException);
	}
	
	protected void commonConstructor(Crypto defaultCrypto, Map<String, Crypto> cryptoMap, boolean wantToGetException) {
		setDefaultCrypto(defaultCrypto);
		if (cryptoMap != null) {
			setCryptoMap(cryptoMap);
		}
		setWantToGetException(wantToGetException);
	}
	
	public void setDefaultCrypto(Crypto crypto) {
		Assert.notNull(crypto, "'crypto' must not be null");
		cryptoMap.put(Cryptogram.DEFAULT, crypto);
	}
	
	public void setCryptoMap(Map<String, Crypto> cryptoMap) {
		Assert.notNull(cryptoMap, "'cryptoMap' must not be null");
		if (cryptoMap.containsKey(Cryptogram.DEFAULT)) {
			throw new IllegalArgumentException("cryptoMap must have no containsKey 'default'. Please use 'setDefaultCrypto' or 'Constructor(Crypto crypto, ...)'");
		}
		cryptoMap.putAll(cryptoMap);
	}
	
	public void setWantToGetException(boolean wantToGetException) {
		this.wantToGetException = wantToGetException;
	}

	/**
	 * 반환된 객체 중 암호화 할 Property 가 있으면 암호화
	 */
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Method bridgeMethod = invocation.getMethod();
		Method method = invocation.getThis().getClass().getMethod(bridgeMethod.getName(), bridgeMethod.getParameterTypes());
		List<Integer> encryptArgsIdxList = new LinkedList<Integer>();
		Object[] args = invocation.getArguments();
		Annotation[][] annotationMatrix = method.getParameterAnnotations();
		
		for (int i = 0; i < annotationMatrix.length; i++) {
			Annotation[] paramAnnotations = annotationMatrix[i];
			for (Annotation paramAnnotation : paramAnnotations) {
				if (paramAnnotation instanceof Encrypt) {
					encryptArgsIdxList.add(i);
					break;
				}
			}
		}
		for(int encryptArgsIdx : encryptArgsIdxList) {
			innerInvoke(args[encryptArgsIdx]);
		}
		Object returnValue = invocation.proceed();
		Encrypt encrypt = AnnotationUtils.findAnnotation(method, encryptAnnotationType);
		if (encrypt != null && returnValue != null) {
			Class<?> returnClazz = returnValue.getClass();
			if (ClassUtils.isAssignable(returnClazz, List.class)) {
				List<?> returnList = (List<?>)returnValue;
				List<Object> afterList = new CopyOnWriteArrayList<Object>();
				for (Object obj : returnList) {
					innerInvoke(obj);
					afterList.add(obj);
				}
				returnList = null;
				returnValue = afterList;
			} else {
				innerInvoke(returnValue);
			}
		}
		return returnValue;
	}
	
	private Object innerInvoke(Object obj) throws Throwable {
		Class<?> returnClazz = obj.getClass();
		Cryptogram cryptogramDomain = AnnotationUtils.findAnnotation(returnClazz, Cryptogram.class);
		EncryptDomain domain = AnnotationUtils.findAnnotation(returnClazz, EncryptDomain.class);
		List<Field> fieldList = FieldUtils.getAllFieldsList(returnClazz);
		for (Field field : fieldList) {
			Cryptogram cryptogramProp = field.getAnnotation(Cryptogram.class);
			EncryptProperty prop = field.getAnnotation(EncryptProperty.class);
			if ((cryptogramProp != null || prop != null) && ClassUtils.isAssignable(field.getType(), String.class) ) {
				Object cipherObj = FieldUtils.readField(field, obj, true);
				if (cipherObj != null) {
					String cipher = cipherObj.toString();
					Crypto crypto = getCrypto(cryptogramDomain, domain, cryptogramProp, prop);
					try {
						String plain = crypto.encrypt(cipher);
						FieldUtils.writeField(field, obj, plain, true);
					} catch (Exception e) {
						if (wantToGetException) {
							throw e;
						} else {
							log.warn(field.getName() + " error - " + e.getLocalizedMessage(), e);
						}
					}
				}
			}
		}
		return obj;
	}
	
	private Crypto getCrypto(Cryptogram cryptogramDomain, EncryptDomain domain, Cryptogram cryptogramProp, EncryptProperty prop) {
		if (prop != null && cryptoMap.containsKey(prop.value())) {
			return cryptoMap.get(prop.value());
		} else if (cryptogramProp != null && cryptoMap.containsKey(cryptogramProp.value())) {
			return cryptoMap.get(cryptogramProp.value());
		} else if (domain != null && cryptoMap.containsKey(domain.value())) {
			return cryptoMap.get(domain.value());
		} else if (cryptogramDomain != null && cryptoMap.containsKey(cryptogramDomain.value())) {
			return cryptoMap.get(cryptogramDomain.value());
		} else {
			throw new IllegalArgumentException("Not exist from crypto key : " + domain.value() + ". Check MethodEncryptionalInterceptor initialize or EncryptProperty.value, Encrypt.value, Cryptogram.value");
		}
	}
}
