package kr.co.azurepassion.common.cipher.aop.advice;

import kr.co.azurepassion.common.cipher.aop.annotation.Cryptogram;
import kr.co.azurepassion.common.cipher.aop.annotation.Decrypt;
import kr.co.azurepassion.common.cipher.aop.annotation.DecryptDomain;
import kr.co.azurepassion.common.cipher.aop.annotation.DecryptProperty;
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

public class MethodDecryptInterceptor implements MethodInterceptor {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	// 복호화 오류 발생 시 Exception 처리할 것인가?
	private boolean wantToGetException = true;

	private Class<? extends Decrypt> decryptAnnotationType = Decrypt.class;
	
	private Map<String, Crypto> cryptoMap = new ConcurrentHashMap<String, Crypto>();
	
	public MethodDecryptInterceptor() {
	}
	
	public MethodDecryptInterceptor(Crypto defaultCrypto) {
		commonConstructor(defaultCrypto, null, true);
	}
	
	public MethodDecryptInterceptor(Crypto defaultCrypto, boolean wantToGetException) {
		commonConstructor(defaultCrypto, null, wantToGetException);
	}
	
	public MethodDecryptInterceptor(Crypto defaultCrypto, Map<String, Crypto> cryptoMap) {
		commonConstructor(defaultCrypto, cryptoMap, true);
	}
	
	public MethodDecryptInterceptor(Crypto defaultCrypto, Map<String, Crypto> cryptoMap, boolean wantToGetException) {
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
	 * 반환된 객체 중 복호화 할 Property 가 있으면 복호화
	 */
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Method bridgeMethod = invocation.getMethod();
		Method method = invocation.getThis().getClass().getMethod(bridgeMethod.getName(), bridgeMethod.getParameterTypes());
		// 파라메터 복호화
		List<Integer> decryptArgsIdxList = new LinkedList<Integer>();
		Object[] args = invocation.getArguments();
		
		Annotation[][] annotationMatrix = method.getParameterAnnotations();
		for (int i = 0; i < annotationMatrix.length; i++) {
			Annotation[] paramAnnotations = annotationMatrix[i];
			for (Annotation paramAnnotation : paramAnnotations) {
				if (paramAnnotation instanceof Decrypt) {
					decryptArgsIdxList.add(i);
					break;
				}
			}
		}
		for(int decryptArgsIdx : decryptArgsIdxList) {
			innerInvoke(args[decryptArgsIdx]);
		}
		Object returnValue = invocation.proceed();
		Decrypt decrypt = AnnotationUtils.findAnnotation(method, decryptAnnotationType);
		// return 복호화
		if (decrypt != null && returnValue != null) {
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
		DecryptDomain domain = AnnotationUtils.findAnnotation(returnClazz, DecryptDomain.class);
		List<Field> fieldList = FieldUtils.getAllFieldsList(returnClazz);
		for (Field field : fieldList) {
			Cryptogram cryptogramProp = field.getAnnotation(Cryptogram.class);
			DecryptProperty prop = field.getAnnotation(DecryptProperty.class);
			if ((cryptogramProp != null || prop != null) && ClassUtils.isAssignable(field.getType(), String.class) ) {
				Object cipherObj = FieldUtils.readField(field, obj, true);
				if (cipherObj != null) {
					String cipher = cipherObj.toString();
					Crypto crypto = getCrypto(cryptogramDomain, domain, cryptogramProp, prop);
					try {
						String plain = crypto.decrypt(cipher);
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
	
	private Crypto getCrypto(Cryptogram cryptogramDomain, DecryptDomain domain, Cryptogram cryptogramProp, DecryptProperty prop) {
		if (prop != null && cryptoMap.containsKey(prop.value())) {
			return cryptoMap.get(prop.value());
		} else if (cryptogramProp != null && cryptoMap.containsKey(cryptogramProp.value())) {
			return cryptoMap.get(cryptogramProp.value());
		} else if (domain != null && cryptoMap.containsKey(domain.value())) {
			return cryptoMap.get(domain.value());
		} else if (cryptogramDomain != null && cryptoMap.containsKey(cryptogramDomain.value())) {
			return cryptoMap.get(cryptogramDomain.value());
		} else {
			throw new IllegalArgumentException("Not exist from crypto key : " + domain.value() + ". Check MethodDecryptionalInterceptor initialize or DecryptProperty.value, Decrypt.value, Cryptogram.value");
		}
	}
}
