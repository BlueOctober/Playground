package kr.co.azurepassion.common.cipher.encode;

/**
 * 암호화를 위한 문자열 <-> 바이너리 간 인코더
 */
public interface CryptoEncoder {
	
	/**
	 * 바이트배열을 문자열로 인코딩
	 * @param bytes 바이트배열
	 * @return 수정할 문자열
	 */
	String getString(byte[] bytes);
	
	/**
	 * 바이트배열을 문자열로 디코딩
	 * @param bytes 바이트배열
	 * @return 수정할 문자열
	 */
	byte[] getBytes(String str);
}
