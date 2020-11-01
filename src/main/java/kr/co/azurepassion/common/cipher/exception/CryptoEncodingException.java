package kr.co.azurepassion.common.cipher.exception;

/**
 * 암호화 시 encoding 관련 오류
 * String - bytes 간 변환 시 발생하는 오류
 */
public class CryptoEncodingException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7980932038821497271L;

	public CryptoEncodingException(Throwable t) {
		super(t);
	}
}
