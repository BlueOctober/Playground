package kr.co.azurepassion.common.cipher.symmetric;

import kr.co.azurepassion.common.cipher.encode.Base64CryptoEncoder;
import kr.co.azurepassion.common.cipher.exception.CryptoEncodingException;
import kr.co.azurepassion.common.cipher.symmetric.seed.kisa.KISA_SEED_CBC;
import kr.co.azurepassion.common.cipher.Crypto;

import java.io.UnsupportedEncodingException;

/**
 * SEED/CBC/? from KISA
 */
public class Base64KisaSeedCbcCrypto implements Crypto {
	
	public static final String DEFAULT_CHARSET = "UTF-8";
	
	Base64CryptoEncoder encoder = new Base64CryptoEncoder();
	
	private String charset = DEFAULT_CHARSET;
	
	private byte[] keyBytes;

	/**
	 * 암호화 키 설정
	 * @param secretKey
	 */
	public void setSecretKey(String secretKey) {
		this.keyBytes = encoder.getBytes(secretKey);
	}
	
	/**
	 * Charset 설정 (default : UTF-8)
	 * @param charset
	 */
	public void setCharset(String charset) {
		this.charset = charset;
		encoder.setCharset(charset);
	}
	
	public Base64KisaSeedCbcCrypto() {
	}
	
	public Base64KisaSeedCbcCrypto(String secretKey) {
		commonConstructor(secretKey, DEFAULT_CHARSET);
	}
	
	public Base64KisaSeedCbcCrypto(String secretKey, String charset) {
		commonConstructor(secretKey, charset);
	}
	
	private void commonConstructor(String secretKey, String charset) {
		this.setSecretKey(secretKey);
	}

	public String encrypt(String plainStr) {
		checkValidation();
		try {
			byte[] plainBytes = plainStr.getBytes(charset);
			return encoder.getString(KISA_SEED_CBC.SEED_CBC_Encrypt(keyBytes, keyBytes, plainBytes, 0, plainBytes.length));
		} catch (UnsupportedEncodingException uee) {
			throw new CryptoEncodingException(uee);
		}
	}

	public String decrypt(String cipherStr) {
		checkValidation();
		byte[] encryptbytes = encoder.getBytes(cipherStr);
		try {
			return new String(KISA_SEED_CBC.SEED_CBC_Decrypt(keyBytes, keyBytes, encryptbytes, 0, encryptbytes.length), charset);
		} catch (UnsupportedEncodingException uee) {
			throw new CryptoEncodingException(uee);
		}
	}
	
	private void checkValidation() {
		if (keyBytes == null) {
			throw new IllegalArgumentException("'secretKey' is must not be null");
		}
		if (keyBytes.length != 16) {
			throw new IllegalArgumentException("'secretKey' length must be 16 byte (128 bit) with base64 encoded - bytes : " + keyBytes.length);
		}
	}

}
