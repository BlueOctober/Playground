package kr.co.azurepassion.common.cipher.symmetric;

import kr.co.azurepassion.common.cipher.encode.CryptoEncoder;
import kr.co.azurepassion.common.cipher.exception.CryptoEncryptException;
import kr.co.azurepassion.common.cipher.exception.CryptoInitializeException;
import kr.co.azurepassion.common.cipher.Crypto;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * 대칭키 암호화 모듈 추상 클래스
 * <p>
 * 	알고리즘, 키, iv 값을 지원하며 기본 암호화 로직이 포함되어 있다.
 * </p>
 */
public abstract class AbstractSimpleSymmetricCrypto implements Crypto{
	
	Logger log = LoggerFactory.getLogger(this.getClass());

	/** 암호화 키 */
	protected String secretKey;
	/** charset */
	protected String charset = Crypto.DEFAULT_CHARSET;
	/**
	 * 암호화 키 객체
	 * <p>
	 * 	iv가 조합될 경우 cipherParams를 사용한다.
	 * </p>
	 */
	protected KeyParameter keyParam;
	
	/**
	 * BC 암호화 알고리즘 (block + crypto algorithm)
	 */
	protected BufferedBlockCipher cipher;
	
	/**
	 * 암호화 시 bytes <-> String 간 변환 인코더 설정
	 */
	protected CryptoEncoder encoder;

	/**
	 * 암호화 알고리즘 설정
	 * See {@linktourl https://www.bouncycastle.org/specifications.html}
	 * @param algorithm 알고리즘
	 */
	protected void setCipher(BufferedBlockCipher cipher) {
		this.cipher = cipher;
	}
	
	/**
	 * 암호화 키(대칭키..) 설정
	 * @param secretKey
	 */
	protected void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	
	/**
	 * charset 설정 : encode, decode 용
	 * @param charset
	 */
	protected void setCharset(String charset) {
		this.charset = charset;
	}
	
	/**
	 * 인코더 (bytes <-> string) 설정
	 * @param encoder
	 */
	protected void setEncoder(CryptoEncoder encoder) {
		this.encoder = encoder;
	}
	
	/**
	 * 초기화 메소드
	 */
	public void initializeAll() {
		initializeKey();
	}
	
	protected void initializeKey() {
		if (secretKey != null) {
			keyParam = null;
			keyParam = new KeyParameter(encoder.getBytes(secretKey));
		}
	}
	
	/**
	 * 암호화
	 * @param plainStr 암호화할 문자열
	 * @return 암호화된 문자열
	 */
	public String encrypt(String plainStr) {
		Assert.notNull(plainStr, "'plainStr' must not be null");
		checkValidation();
		try {
			cipher.init(true, keyParam);
			byte[] plainBytes = plainStr.getBytes(charset);
			
			byte[] cipherBytes = new byte[cipher.getOutputSize(plainBytes.length)];
			int outOff = cipher.processBytes(plainBytes, 0, plainBytes.length, cipherBytes, 0);
			cipher.doFinal(cipherBytes, outOff);
			return encoder.getString(cipherBytes);
		} catch (Exception e) {
			throw new CryptoEncryptException(e);
		}
	}
	
	/**
	 * 복호화
	 * @param cipherStr 암호화된 문자열
	 * @return 복호화된 문자열
	 */
	public String decrypt(String cipherStr) {
		Assert.notNull(cipherStr, "'cipherStr' must not be null");
		checkValidation();
		try {
			cipher.init(false, keyParam);
			byte[] cipherBytes = encoder.getBytes(cipherStr);
			byte[] buff = new byte[cipher.getOutputSize(cipherBytes.length)];
			int len = cipher.processBytes(cipherBytes, 0, cipherBytes.length, buff, 0);
			len += cipher.doFinal(buff, len);
			
			byte[] decryptBytes = new byte[len];
		    System.arraycopy(buff, 0, decryptBytes, 0, len);
			return new String(decryptBytes, charset);
		} catch (Exception e) {
			throw new CryptoEncryptException(e);
		}
	}
	
	protected void checkValidation() {
		if (secretKey == null) {
			throw new CryptoInitializeException("'secretKey' must not be null");
		}
		if ( keyParam == null || cipher == null) {
			String message = "'#initializeAll' method should be called after instantiate";
			throw new CryptoInitializeException(message);
		}
	}
}
