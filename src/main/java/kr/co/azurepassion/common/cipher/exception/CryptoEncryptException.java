package kr.co.azurepassion.common.cipher.exception;

/**
 * 암호화 오류
 */
public class CryptoEncryptException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2619534549496762982L;

	public CryptoEncryptException(Throwable t) {
		super(t);
	}
	
	public CryptoEncryptException(String message, Throwable t) {
		super(message, t);
	}
}
