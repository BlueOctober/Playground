package kr.co.azurepassion.common.cipher.symmetric;

import kr.co.azurepassion.common.cipher.encode.CryptoEncoder;
import kr.co.azurepassion.common.cipher.encode.SunBase64CryptoEncoder;
import kr.co.azurepassion.common.cipher.exception.CryptoEncodingException;
import kr.co.azurepassion.common.cipher.symmetric.seed.old.SeedCipher;
import kr.co.azurepassion.common.cipher.Crypto;

import java.io.UnsupportedEncodingException;

public class OldSeedCrypto implements Crypto {
	CryptoEncoder encoder = new SunBase64CryptoEncoder();
	
	public static final String DEFAULT_CHARSET = "EUC-KR";
	
	private String charset = DEFAULT_CHARSET;
	
	private byte[] keyBytes;
	
	private SeedCipher seed = new SeedCipher();
	
	/**
	 * 암호화 키 설정
	 * @param secretKey
	 */
	public void setSecretKey(String secretKey) {
		this.keyBytes = secretKey.getBytes();
	}
	
	/**
	 * Charset 설정 (default : EUC-KR)
	 * @param charset
	 */
	public void setCharset(String charset) {
		this.charset = charset;
	}
	
	public OldSeedCrypto() {
	}
	
	public OldSeedCrypto(String secretKey) {
		commonConstructor(secretKey, DEFAULT_CHARSET);
	}
	
	private void commonConstructor(String secretKey, String charset) {
		this.setSecretKey(secretKey);
	}

	public String encrypt(String plainStr) {
		checkValidation();
		try {
			return encoder.getString(seed.encrypt(plainStr, keyBytes, charset));
		} catch (UnsupportedEncodingException uee) {
			throw new CryptoEncodingException(uee);
		}
	}

	public String decrypt(String cipherStr) {
		checkValidation();
		byte[] encryptbytes = encoder.getBytes(cipherStr);
		try {
			return seed.decryptAsString(encryptbytes, keyBytes, charset);
		} catch (UnsupportedEncodingException uee) {
			throw new CryptoEncodingException(uee);
		}
	}
	
	private void checkValidation() {
		if (keyBytes == null) {
			throw new IllegalArgumentException("'secretKey' is must not be null");
		}
	}
}