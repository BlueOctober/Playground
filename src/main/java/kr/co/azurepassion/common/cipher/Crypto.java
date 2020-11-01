package kr.co.azurepassion.common.cipher;

/**
 * 공통 암호화-복호화 인터페이스
 *
 */
public interface Crypto {
	
	String DEFAULT_CHARSET = "UTF-8";

	/**
	 * 암호화
	 * @param plainStr 암호화할 문자열
	 * @return 암호화된 문자열
	 */
	String encrypt(String plainStr);
	
	/**
	 * 복호화
	 * @param cipherStr 암호화된 문자열
	 * @return 복호화된 문자열
	 */
	String decrypt(String cipherStr);
}