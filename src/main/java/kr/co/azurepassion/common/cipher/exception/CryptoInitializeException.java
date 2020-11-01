package kr.co.azurepassion.common.cipher.exception;

/**
 * 암호화 콤포넌트 초기화 오류
 */
public class CryptoInitializeException extends RuntimeException {
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3425982528637030996L;

	public CryptoInitializeException(String message) {
		super(message);
	}
	
	public CryptoInitializeException(Throwable t) {
		super(t);
	}
}
