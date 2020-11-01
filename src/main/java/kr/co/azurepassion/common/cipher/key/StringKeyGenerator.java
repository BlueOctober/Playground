package kr.co.azurepassion.common.cipher.key;

/**
 * 암호화 키 생성 인터페이스
 *
 */
public interface StringKeyGenerator {
	
	/**
	 * 키 문자열 생성
	 * @param algorithm 생성알고리즘 (default : AES)
	 * @param bits 생성 bits 수 (default : 128)
	 * @return
	 */
	String generate(String algorithm, int bits);
}
