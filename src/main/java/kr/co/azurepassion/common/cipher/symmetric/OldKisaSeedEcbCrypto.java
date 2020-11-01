package kr.co.azurepassion.common.cipher.symmetric;

import kr.co.azurepassion.common.cipher.encode.SunBase64CryptoEncoder;
import kr.co.azurepassion.common.cipher.exception.CryptoEncodingException;
import kr.co.azurepassion.common.cipher.symmetric.seed.kisa.KISA_SEED_ECB;
import kr.co.azurepassion.common.cipher.Crypto;

import java.io.UnsupportedEncodingException;

/**
 * SEED/ECB/? from KISA
 *
 */
public class OldKisaSeedEcbCrypto implements Crypto {
	
	public static final String DEFAULT_CHARSET = "EUC-KR";
	
	SunBase64CryptoEncoder encoder = new SunBase64CryptoEncoder();
	
	
	private String charset = DEFAULT_CHARSET;
	
	private byte[] keyBytes;

	/**
	 * 암호화 키 설정
	 * @param secretKey
	 */
	public void setSecretKey(String secretKey) {
		int keySize = 16;
		byte[] keyBytes = secretKey.getBytes();
		byte[] afterBytes = new byte[keySize];
		System.arraycopy(keyBytes, 0, afterBytes, 0, keySize);
		this.keyBytes = afterBytes;
		keyBytes = null;
	}
	
	/**
	 * Charset 설정 (default : UTF-8)
	 * @param charset
	 */
	public void setCharset(String charset) {
		this.charset = charset;
	}
	
	public OldKisaSeedEcbCrypto() {
	}
	
	public OldKisaSeedEcbCrypto(String secretKey) {
		commonConstructor(secretKey, DEFAULT_CHARSET);
	}
	
	public OldKisaSeedEcbCrypto(String secretKey, String charset) {
		commonConstructor(secretKey, charset);
	}
	
	private void commonConstructor(String secretKey, String charset) {
		this.setSecretKey(secretKey);
	}

	public String encrypt(String plainStr) {
		checkValidation();
		try {
			byte[] plainBytes = plainStr.getBytes(charset);
			return encoder.getString(KISA_SEED_ECB.SEED_ECB_Encrypt(keyBytes, plainBytes, 0, plainBytes.length));
		} catch (UnsupportedEncodingException uee) {
			throw new CryptoEncodingException(uee);
		}
	}

	public String decrypt(String cipherStr) {
		checkValidation();
		byte[] encryptbytes = encoder.getBytes(cipherStr);
		try {
			return new String(KISA_SEED_ECB.SEED_ECB_Decrypt(keyBytes, encryptbytes, 0, encryptbytes.length), charset);
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
