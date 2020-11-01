package kr.co.azurepassion.common.cipher.symmetric;

import kr.co.azurepassion.common.cipher.encode.Base64CryptoEncoder;
import kr.co.azurepassion.common.cipher.Crypto;
import org.bouncycastle.crypto.engines.SEEDEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PKCS7Padding;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;

/**
 * SEED/CBC/{PKCS5|PKCS7}
 *
 */
public class Base64BcSeedCbcCrypto extends AbstractSymmetricWithIvCrypto {
	Base64CryptoEncoder encoder = new Base64CryptoEncoder();
	
	public Base64BcSeedCbcCrypto() {
		super();
		initializeAlgorithm();
		this.setEncoder(encoder);
	}
	
	// SEED/CBC/{PKCS5|PKCS7}
	private void initializeAlgorithm() {
		super.setCipher(new PaddedBufferedBlockCipher(new CBCBlockCipher(new SEEDEngine()), new PKCS7Padding()));
	}
	
	public Base64BcSeedCbcCrypto(String secretKey) {
		super();
		commonConstructor(secretKey, secretKey, Crypto.DEFAULT_CHARSET);
	}
	
	public Base64BcSeedCbcCrypto(String secretKey, String iv) {
		super();
		commonConstructor(secretKey, iv, Crypto.DEFAULT_CHARSET);
	}
	
	public Base64BcSeedCbcCrypto(String secretKey, String iv, String charset) {
		super();
		commonConstructor(secretKey, iv, Crypto.DEFAULT_CHARSET);
	}
	
	private void commonConstructor(String secretKey, String iv, String charset) {
		initializeAlgorithm();
		this.setEncoder(encoder);
		this.setSecretKey(secretKey);
		this.setIv(iv);
		super.initializeKey();
	}

	@Override
	public void setSecretKey(String secretKey) {
		if (secretKey == null) {
			throw new IllegalArgumentException("'secretKey' must not be null");
		}
		if (encoder.getBytes(secretKey).length != 16) {
			throw new IllegalArgumentException("'secretKey' length must be 16 byte (128 bit) with base64 encoded");
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
		super.setIv(secretKey);
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
