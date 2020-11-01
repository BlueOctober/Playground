package kr.co.azurepassion.common.cipher.symmetric;

import kr.co.azurepassion.common.cipher.encode.Base64CryptoEncoder;
import kr.co.azurepassion.common.cipher.Crypto;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PKCS7Padding;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;

/**
 * AES/CBC/(PKCS7/PKCS5)Padding 256 bit 암호화 콤포넌트
 */
public class Base64Aes256Crypto extends AbstractSymmetricWithIvCrypto {
	
	Base64CryptoEncoder encoder = new Base64CryptoEncoder();
	
	public Base64Aes256Crypto() {
		super();
		initializeAlgorithm();
		this.setEncoder(encoder);
	}
	
	// AES/CBC/(PKCS7/PKCS5)
	private void initializeAlgorithm() {
		super.setCipher(new PaddedBufferedBlockCipher(new CBCBlockCipher(new AESEngine()), new PKCS7Padding()));
	}
	
	public Base64Aes256Crypto(String secretKey, String iv) {
		super();
		commonConstructor(secretKey, iv, Crypto.DEFAULT_CHARSET);
	}
	
	public Base64Aes256Crypto(String secretKey, String iv, String charset) {
		super();
		commonConstructor(secretKey, iv, charset);
	}
	
	private void commonConstructor(String secretKey, String iv, String charset) {
		initializeAlgorithm();
		this.setEncoder(encoder);
		this.setSecretKey(secretKey);
		this.setIv(iv);
	}

	@Override
	public void setSecretKey(String secretKey) {
		if (secretKey == null) {
			throw new IllegalArgumentException("'secretKey' must not be null");
		}
		if (encoder.getBytes(secretKey).length != 32) {
			throw new IllegalArgumentException("'secretKey' length must be 32 byte (256 bit) with base64 encoded");
		}
		super.setSecretKey(secretKey);
		super.initializeKey();
	}
	
	@Override
	public void setIv(String iv) {
		if (iv == null) {
			throw new IllegalArgumentException("'iv' must not be null");
		}
		if (encoder.getBytes(iv).length != 16) {
			throw new IllegalArgumentException("'iv' length must be 16 byte (128 bit) with base64 encoded");
		}
		super.setIv(iv);
		super.initializeKey();
	}
	
	@Override
	public void setCharset(String charset) {
		if (charset != null) {
			encoder.setCharset(charset);
			super.setCharset(charset);
		}
	}
}
